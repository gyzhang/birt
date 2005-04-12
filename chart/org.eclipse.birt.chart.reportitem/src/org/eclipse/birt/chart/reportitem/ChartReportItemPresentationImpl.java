/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.reportitem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.PluginException;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.DefaultReportItemPresentationImpl;
import org.eclipse.birt.report.engine.extension.IReportItemSerializable;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;

/**
 *  
 */
public final class ChartReportItemPresentationImpl extends DefaultReportItemPresentationImpl
{
    /**
     *  
     */
    private transient Chart cm = null;

    /**
     *  
     */
    private transient ExtendedItemHandle eih = null;

    /**
     *  
     */
    private transient File fChartImage = null;

    /**
     *  
     */
    private transient FileInputStream fis = null;

    /**
     *  
     */
    private final String sExtension;

    /**
     *  
     */
    private RunTimeContext rtc = null;

    /**
     *  
     */
    public ChartReportItemPresentationImpl()
    {
        super();
        sExtension = "png";
    }

    /**
     *  
     */
    public final void initialize(HashMap hm) throws BirtException
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
            "ChartReportItemPresentationImpl: initialize(...) - start");
        super.initialize(hm);
        cm = getModelFromWrapper(hm.get(MODEL_OBJ));
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemPresentationImpl: initialize(...) - end");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#restoreGenerationState(org.eclipse.birt.report.engine.extension.IReportItemSerializable)
     */
    public void restoreGenerationState(IReportItemSerializable iris)
    {
        final SerializedState ss = (SerializedState) iris;
        rtc = (RunTimeContext) ss.deserialize(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getOutputType(java.lang.String,
     *      java.lang.String)
     */
    public int getOutputType(String format, String mimeType)
    {
        return OUTPUT_AS_IMAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getSize()
     */
    public final Size getSize()
    {
        if (cm != null)
        {
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                "ChartReportItemPresentationImpl: getSize(...) - start");
            final Size sz = new Size();
            sz.setWidth((float) cm.getBlock().getBounds().getWidth());
            sz.setHeight((float) cm.getBlock().getBounds().getHeight());
            sz.setUnit(Size.UNITS_PT);
            DefaultLoggerImpl.instance()
                .log(ILogger.INFORMATION, "ChartReportItemPresentationImpl: getSize(...) - end");
            return sz;
        }
        return super.getSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#process()
     */
    public final Object process() throws BirtException
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemPresentationImpl: process(...) - start");
        // SETUP A TEMP FILE FOR STREAMING
        try
        {
            fChartImage = File.createTempFile("chart", "." + sExtension);
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                "Writing to " + sExtension + " file at " + fChartImage.getPath());
        }
        catch (IOException ioex )
        {
            throw new BirtException("tmp png file creation", ioex);
        }

        // FETCH A HANDLE TO THE DEVICE RENDERER
        IDeviceRenderer idr = null;
        try
        {
            idr = PluginSettings.instance().getDevice("dv." + sExtension.toUpperCase(Locale.US));
        }
        catch (PluginException pex )
        {
            DefaultLoggerImpl.instance().log(pex);
            throw new BirtException(sExtension.toUpperCase(Locale.US) + " device retrieval", pex);
        }

        // BUILD THE CHART
        final Bounds bo = cm.getBlock().getBounds();
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Presentation uses bounds bo=" + bo);
        final Generator gr = Generator.instance();
        GeneratedChartState gcs = null;
        try
        {
            gcs = gr.build(idr.getDisplayServer(), cm, null, bo, rtc);
        }
        catch (GenerationException gex )
        {
            DefaultLoggerImpl.instance().log(gex);
            throw new BirtException("chart build", gex);
        }

        // WRITE TO THE IMAGE FILE
        idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, fChartImage.getPath());
        try
        {
            gr.render(idr, gcs);
        }
        catch (RenderingException rex )
        {
            DefaultLoggerImpl.instance().log(rex);
            throw new BirtException("chart render", rex);
        }

        // RETURN A STREAM HANDLE TO THE NEWLY CREATED IMAGE
        try
        {
            fis = new FileInputStream(fChartImage.getPath());
        }
        catch (IOException ioex )
        {
            DefaultLoggerImpl.instance().log(ioex);
            throw new BirtException("input stream creation", ioex);
        }

        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemPresentationImpl: process(...) - end");
        return fis;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#finish()
     */
    public final void finish()
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemPresentationImpl: finish(...) - start");
        // CLOSE THE TEMP STREAM PROVIDED TO THE CALLER
        try
        {
            fis.close();
        }
        catch (IOException ioex )
        {
            DefaultLoggerImpl.instance().log(ioex);
        }

        // DELETE THE TEMP CHART IMAGE FILE CREATED
        if (!fChartImage.delete())
        {
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "Could not delete temporary " + sExtension + " file created at " + fChartImage.getPath());
        }
        else
        {
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                "Successfully deleted temporary " + sExtension + " file created at " + fChartImage.getPath());
        }
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemPresentationImpl: finish(...) - end");
    }

    /**
     * 
     * @param oReportItemImpl
     * @return
     */
    private final Chart getModelFromWrapper(Object oReportItemImpl)
    {
        eih = (ExtendedItemHandle) oReportItemImpl;
        IReportItem item = ((ExtendedItem) eih.getElement()).getExtendedElement();
        if (item == null)
        {
            try
            {
                eih.loadExtendedElement();
            }
            catch (ExtendedElementException eeex )
            {
                DefaultLoggerImpl.instance().log(eeex);
            }
            item = ((ExtendedItem) eih.getElement()).getExtendedElement();
            if (item == null)
            {
                DefaultLoggerImpl.instance()
                    .log(ILogger.ERROR, "Unable to locate report item wrapper for chart object");
                return null;
            }
        }
        final ChartReportItemImpl crii = ((ChartReportItemImpl) item);
        return crii.getModel();
    }
}