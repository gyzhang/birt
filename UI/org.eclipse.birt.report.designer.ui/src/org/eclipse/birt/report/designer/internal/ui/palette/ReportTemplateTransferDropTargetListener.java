/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LibraryElementsToolHandleExtends;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DataSetColumnToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DataSetToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DimensionHandleToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.ImageToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.MeasureHandleToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.ParameterToolExtends;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertInLayoutAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * Drag&Drop listener
 */
public class ReportTemplateTransferDropTargetListener extends
		TemplateTransferDropTargetListener
{

	private static final String TRANS_LABEL_CREATE_ELEMENT = Messages.getString( "ReportTemplateTransferDropTargetListener.transLabel.createElement" ); //$NON-NLS-1$
	private static final String IMG_TRANS_MSG = Messages.getString( "ImageEditPart.trans.editImage" ); //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public ReportTemplateTransferDropTargetListener( EditPartViewer viewer )
	{
		super( viewer );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.TemplateTransferDropTargetListener#getFactory(java.lang.Object)
	 */
	protected CreationFactory getFactory( Object template )
	{
		if ( handleValidateDrag( template ) )
		{
			if ( template instanceof String )
			{
				return new ReportElementFactory( template );
			}
			return new ReportElementFactory( getSingleTransferData( template ),
					template );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
	 */
	protected void handleDrop( )
	{
		boolean isScalarparameter = false;
		boolean isResultSetColumn = false;
		boolean isEmbeddImage = false;
		final Object template = TemplateTransfer.getInstance( ).getTemplate( );
		Assert.isNotNull( template );

		Assert.isTrue( handleValidateDrag( template ) );

		updateTargetRequest( );
		updateTargetEditPart( );

		AbstractToolHandleExtends preHandle = null;
		String transName = null;
		if ( template instanceof String )
		{
			PaletteEntryExtension[] entries = EditpartExtensionManager.getPaletteEntries( );
			if ( template.toString( )
					.startsWith( IReportElementConstants.REPORT_ELEMENT_EXTENDED ) )
			{
				String extensionName = template.toString( )
						.substring( IReportElementConstants.REPORT_ELEMENT_EXTENDED.length( ) );
				for ( int i = 0; i < entries.length; i++ )
				{
					if ( entries[i].getItemName( ).equals( extensionName ) )
					{
						try
						{
							CommandUtils.setVariable( "targetEditPart",
									getTargetEditPart( ) );
							getCreateRequest( ).getExtendedData( )
									.put( DesignerConstants.KEY_NEWOBJECT,
											entries[i].executeCreate( ) );
							selectAddedObject( );
							return;
						}
						catch ( Exception e )
						{
							ExceptionHandler.handle( e );
						}
					}
				}
			}
			transName = TRANS_LABEL_CREATE_ELEMENT;
			preHandle = BasePaletteFactory.getAbstractToolHandleExtendsFromPaletteName( template );
		}
		else if ( handleValidateInsert( template ) )
		{
			transName = InsertInLayoutAction.DISPLAY_TEXT;
			Object objectType = getFactory( template ).getObjectType( );

			if ( objectType instanceof DataSetHandle )
			{
				preHandle = new DataSetToolExtends( );
			}
			else if ( objectType instanceof DataSetItemModel )
			{
				preHandle = new DataSetColumnToolExtends( );
			}
			else if ( objectType instanceof ResultSetColumnHandle )
			{
				isResultSetColumn = true;
				preHandle = new DataSetColumnToolExtends( );
			}
			else if ( objectType instanceof ScalarParameterHandle )
			{
				isScalarparameter = true;
				preHandle = new ParameterToolExtends( );
			}
			else if ( objectType instanceof DimensionHandle )
			{
				preHandle = new DimensionHandleToolExtends( );
			}
			else if ( objectType instanceof MeasureHandle )
			{
				preHandle = new MeasureHandleToolExtends( );

			}

		}
		else if ( handleValidateLibrary( template ) )
		{
			Object dragObj = getSingleTransferData( template );
			if ( dragObj instanceof EmbeddedImageHandle )
			{
				ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( );
				LibraryHandle library = (LibraryHandle) ( (EmbeddedImageHandle) dragObj ).getElementHandle( )
						.getRoot( );

				try
				{
					if ( UIUtil.includeLibrary( moduleHandle, library ) )
					{
						EmbeddedImage image = StructureFactory.newEmbeddedImageFrom( (EmbeddedImageHandle) dragObj,
								moduleHandle );
						DNDUtil.addEmbeddedImageHandle( getTargetEditPart( ).getModel( ),
								image );
					}
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
				isEmbeddImage = true;
				preHandle = new ImageToolExtends( );
			}
			else
				preHandle = new LibraryElementsToolHandleExtends( (DesignElementHandle) dragObj );
		}
		else if ( handleValidateOutline( template ) )
		{
			Object dragObj = getSingleTransferData( template );
			if ( dragObj instanceof EmbeddedImageHandle )
			{
				isEmbeddImage = true;
				preHandle = new ImageToolExtends( );
			}
		}

		if ( preHandle != null )
		{
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getCommandStack( )
					.startTrans( transName );
			preHandle.setRequest( this.getCreateRequest( ) );
			preHandle.setTargetEditPart( getTargetEditPart( ) );

			Command command = this.getCommand( );
			if ( command != null && command.canExecute( ) )
			{
				if ( !( preHandle.preHandleMouseUp( ) ) )
				{
					SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.getCommandStack( )
							.rollback( );
					return;
				}
			}
			boolean isTheme = checkTheme( preHandle,
					getSingleTransferData( template ) );
			if ( isTheme )
			{
				SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getCommandStack( )
						.commit( );
			}
			else
			{
				super.handleDrop( );

				// fix bugzilla#145284
				if ( !preHandle.postHandleCreation( ) )
				{
					SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.getCommandStack( )
							.rollback( );
					return;
				}

				SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getCommandStack( )
						.commit( );

				if ( isScalarparameter || isResultSetColumn )
				{
					Request request = new Request( ReportRequest.CREATE_SCALARPARAMETER_OR_RESULTSETCOLUMN );
					selectAddedObject( request );
				}
				else if ( isEmbeddImage )
				{
					Object dragObj = getSingleTransferData( template );
					final Object model = getCreateRequest( ).getExtendedData( )
							.get( DesignerConstants.KEY_NEWOBJECT );
					try
					{
						( (ImageHandle) model ).setImageName( ( (EmbeddedImageHandle) dragObj ).getName( ) );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
					CommandStack stack = SessionHandleAdapter.getInstance( )
							.getCommandStack( );
					stack.startTrans( IMG_TRANS_MSG );
					stack.commit( );
				}
				else
					selectAddedObject( );
			}
		}

	}

	private boolean checkTheme( AbstractToolHandleExtends preHandle, Object obj )
	{
		if ( preHandle instanceof LibraryElementsToolHandleExtends
				&& obj instanceof ThemeHandle )
		{
			return true;
		}
		return false;
	}

	/**
	 * Validates drag source from palette, layout, or data view
	 * 
	 * @param dragObj
	 * @return validate result
	 */
	private boolean handleValidateDrag( Object dragObj )
	{
		return dragObj != null
				&& ( handleValidatePalette( dragObj )
						|| handleValidateOutline( dragObj )
						|| handleValidateInsert( dragObj ) || handleValidateLibrary( dragObj ) );
	}

	private boolean handleValidatePalette( Object dragObj )
	{
		return dragObj instanceof String
				&& ( getTargetEditPart( ) == null || ReportCreationTool.handleValidatePalette( dragObj,
						getTargetEditPart( ) ) );
	}

	/**
	 * Validates drag from data view to layout
	 * 
	 * @param template
	 * @return validate result
	 */
	private boolean handleValidateInsert( Object template )
	{
		return InsertInLayoutUtil.handleValidateInsert( template )
				&& ( getTargetEditPart( ) == null || InsertInLayoutUtil.handleValidateInsertToLayout( template,
						getTargetEditPart( ) ) );
	}

	// test for the crosstab
	private boolean isCrossType( Object obj )
	{
		return obj instanceof DimensionHandle || obj instanceof MeasureHandle;
	}

	/**
	 * Validates drag source of outline view and drop target of layout
	 * 
	 * @return validate result
	 */
	private boolean handleValidateOutline( Object dragSource )
	{
		EditPart targetEditPart = getTargetEditPart( );

		if ( targetEditPart == null )
		{
			return true;
		}

		if ( dragSource != null )
		{
			Object[] dragObjs;
			if ( dragSource instanceof Object[] )
			{
				dragObjs = (Object[]) dragSource;
			}
			else
			{
				dragObjs = new Object[]{
					dragSource
				};
			}
			if ( dragObjs.length == 0 )
			{
				return false;
			}
			for ( int i = 0; i < dragObjs.length; i++ )
			{
				if ( dragObjs[i] instanceof EmbeddedImageHandle
						&& !( ( (EmbeddedImageHandle) dragObjs[i] ).getElementHandle( )
								.getRoot( ) instanceof LibraryHandle ) )
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}

	private boolean handleValidateLibrary( Object dragObj )
	{
		EditPart targetEditPart = getTargetEditPart( );
		if ( targetEditPart == null )
		{
			return true;
		}
		if ( dragObj != null )
		{
			Object[] dragObjs;
			if ( dragObj instanceof Object[] )
			{
				dragObjs = (Object[]) dragObj;
			}
			else
			{
				dragObjs = new Object[]{
					dragObj
				};
			}
			if ( dragObjs.length == 0 )
			{
				return false;
			}
			for ( int i = 0; i < dragObjs.length; i++ )
			{
				dragObj = dragObjs[i];
				if ( dragObj instanceof ReportElementHandle )
				{
					if ( ( (ReportElementHandle) dragObj ).getRoot( ) instanceof LibraryHandle )
					{
						// enable DataSetHandle,ParameterHandle to drag in lib
						// explorer view.
						if ( dragObj instanceof DataSetHandle
								|| dragObj instanceof ParameterHandle )
							return true;
						if ( !DNDUtil.handleValidateTargetCanContain( targetEditPart.getModel( ),
								dragObj )
								|| !DNDUtil.handleValidateTargetCanContainMore( targetEditPart.getModel( ),
										1 ) )
						{
							return false;
						}
					}
					else
					{
						return false;
					}
				}
				else if ( dragObj instanceof EmbeddedImageHandle
						&& ( (EmbeddedImageHandle) dragObj ).getElementHandle( )
								.getRoot( ) instanceof LibraryHandle )
				{
					int canContain = DNDUtil.handleValidateTargetCanContain( targetEditPart.getModel( ),
							dragObj,
							true );
					return canContain == DNDUtil.CONTAIN_THIS;
				}

				else
				{
					return false;
				}
			}
			return true;
		}
		return false;
	} /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */

	public void dragOver( DropTargetEvent event )
	{
		super.dragOver( event );
		if ( !handleValidateDrag( TemplateTransfer.getInstance( ).getTemplate( ) ) )
		{
			event.detail = DND.DROP_NONE;
		}
	}

	/*
	 * Add the newly created object to the viewer's selected objects.
	 */
	private void selectAddedObject( )
	{
		final Object model = getCreateRequest( ).getExtendedData( )
				.get( DesignerConstants.KEY_NEWOBJECT );
		final EditPartViewer viewer = getViewer( );
		viewer.getControl( ).setFocus( );
		ReportCreationTool.selectAddedObject( model, viewer );
	}

	/*
	 * Add the newly created object(ScalarParameter or ResultSetColumn) to the
	 * viewer's selected objects.
	 */
	private void selectAddedObject( Request request )
	{
		final Object model = getCreateRequest( ).getExtendedData( )
				.get( DesignerConstants.KEY_NEWOBJECT );
		final EditPartViewer viewer = getViewer( );
		viewer.getControl( ).setFocus( );
		ReportCreationTool.selectAddedObject( model, viewer, request );
	}

	/**
	 * Gets single transfer data from TemplateTransfer
	 * 
	 * @param template
	 *            object transfered by TemplateTransfer
	 * @return single transfer data in array or itself
	 */
	private Object getSingleTransferData( Object template )
	{
		if ( template instanceof Object[] )
		{
			return ( (Object[]) template )[0];
		}
		return template;
	}

}
