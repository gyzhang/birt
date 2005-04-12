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

import java.util.List;

import org.eclipse.birt.report.model.api.extension.PropertyDefinition;

/**
 *  
 */
public final class ChartPropertyDefinitionImpl extends PropertyDefinition
{
    /**
     *  
     */
    private String sGroupNameID = null;

    /**
     *  
     */
    private String sName = null;

    /**
     *  
     */
    private String sDisplayNameID = null;

    /**
     *  
     */
    private boolean bList = false;

    /**
     *  
     */
    private int iType = -1;

    /**
     *  
     */
    private List liChoices = null;

    /**
     *  
     */
    private List liMembers = null;

    /**
     *  
     */
    private Object oDefaultValue = null;

    /**
     * 
     * @param sGroupNameID
     * @param sName
     * @param sDisplayNameID
     * @param bList
     * @param iType
     * @param liChoices
     * @param liMembers
     * @param oDefaultValue
     */
    ChartPropertyDefinitionImpl(String sGroupNameID, String sName, String sDisplayNameID, boolean bList, int iType,
        List liChoices, List liMembers, Object oDefaultValue)
    {
        this.sGroupNameID = sGroupNameID;
        this.sName = sName;
        this.sDisplayNameID = sDisplayNameID;
        this.bList = bList;
        this.iType = iType;
        this.liChoices = liChoices;
        this.liMembers = liMembers;
        this.oDefaultValue = oDefaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getGroupNameID()
     */
    public String getGroupNameID()
    {
        return sGroupNameID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getName()
     */
    public String getName()
    {
        return sName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getDisplayNameID()
     */
    public String getDisplayNameID()
    {
        return sDisplayNameID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getType()
     */
    public int getType()
    {
        return iType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#isList()
     */
    public boolean isList()
    {
        return bList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getChoices()
     */
    public List getChoices()
    {
        return liChoices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getMembers()
     */
    public List getMembers()
    {
        return liMembers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getDefaultValue()
     */
    public Object getDefaultValue()
    {
        return null;
    }

}