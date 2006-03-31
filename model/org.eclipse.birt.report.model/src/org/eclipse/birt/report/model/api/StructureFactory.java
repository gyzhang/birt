/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.JoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * Provides the factory method to create empty structures.
 */

public class StructureFactory
{

	/**
	 * Creates an empty data set cached meta-data structure.
	 * 
	 * @return an empty data set cached meta-data structure.
	 */

	public static CachedMetaData createCachedMetaData( )
	{
		return new CachedMetaData( );
	}

	/**
	 * Creates an empty computed column structure.
	 * 
	 * @return an empty computed column structure
	 */

	public static ComputedColumn createComputedColumn( )
	{
		return new ComputedColumn( );
	}

	/**
	 * Creates an empty action structure.
	 * 
	 * @return an empty action structure.
	 */

	public static Action createAction( )
	{
		return new Action( );
	}

	/**
	 * Creates an empty config variable structure.
	 * 
	 * @return an empty config variable structure
	 */

	public static ConfigVariable createConfigVar( )
	{
		return new ConfigVariable( );
	}

	/**
	 * Creates an empty custom color structure.
	 * 
	 * @return an empty custom color structure
	 */

	public static CustomColor createCustomColor( )
	{
		return new CustomColor( );
	}

	/**
	 * Creates an empty data-set parameter structure.
	 * 
	 * @return an empty data-set parameter structure
	 */

	public static DataSetParameter createDataSetParameter( )
	{
		return new DataSetParameter( );
	}

	/**
	 * Creates an empty embedded image structure.
	 * 
	 * @return an empty embedded image structure
	 */

	public static EmbeddedImage createEmbeddedImage( )
	{
		return new EmbeddedImage( );
	}

	/**
	 * Creates an empty filter condition structure.
	 * 
	 * @return an empty filter condition structure
	 */

	public static FilterCondition createFilterCond( )
	{
		return new FilterCondition( );
	}

	/**
	 * Creates an empty hide rule structure.
	 * 
	 * @return an empty hide rule structure
	 */

	public static HideRule createHideRule( )
	{
		return new HideRule( );
	}

	/**
	 * Creates an empty include script structure.
	 * 
	 * @return an empty include script structure
	 */

	public static IncludeScript createIncludeScript( )
	{
		return new IncludeScript( );
	}

	/**
	 * Creates an empty include library structure.
	 * 
	 * @return an empty include library structure
	 */

	public static IncludedLibrary createIncludeLibrary( )
	{
		return new IncludedLibrary( );
	}

	/**
	 * Creates an empty parameter binding structure.
	 * 
	 * @return an empty parameter binding structure
	 */

	public static ParamBinding createParamBinding( )
	{
		return new ParamBinding( );
	}

	/**
	 * Creates an empty property mask structure.
	 * 
	 * @return an empty property mask structure
	 */

	public static PropertyMask createPropertyMask( )
	{
		return new PropertyMask( );
	}

	/**
	 * Creates an empty result set column structure.
	 * 
	 * @return an empty result set column structure
	 */

	public static ResultSetColumn createResultSetColumn( )
	{
		return new ResultSetColumn( );
	}

	/**
	 * Creates an empty search key structure.
	 * 
	 * @return an empty search key structure
	 */

	public static SearchKey createSearchKey( )
	{
		return new SearchKey( );
	}

	/**
	 * Creates an empty selection choice structure.
	 * 
	 * @return an empty selection choice structure
	 */

	public static SelectionChoice createSelectionChoice( )
	{
		return new SelectionChoice( );
	}

	/**
	 * Creates an empty sort key structure.
	 * 
	 * @return an empty sort key structure
	 */

	public static SortKey createSortKey( )
	{
		return new SortKey( );
	}

	/**
	 * Creates an empty column hint structure.
	 * 
	 * @return an empty column hint structure
	 */

	public static ColumnHint createColumnHint( )
	{
		return new ColumnHint( );
	}

	/**
	 * Creates an empty highlight rule structure.
	 * 
	 * @return an empty highlight rule structure
	 */

	public static HighlightRule createHighlightRule( )
	{
		return new HighlightRule( );
	}

	/**
	 * Creates an empty map rule structure.
	 * 
	 * @return an empty map rule structure
	 */

	public static MapRule createMapRule( )
	{
		return new MapRule( );
	}

	/**
	 * Creates an empty extended property structure.
	 * 
	 * @return an empty extended property structure
	 */

	public static ExtendedProperty createExtendedProperty( )
	{
		return new ExtendedProperty( );
	}

	/**
	 * Creates an empty join condition structure.
	 * 
	 * @return an empty join condition structure
	 */

	public static JoinCondition createJoinCondition( )
	{
		return new JoinCondition( );
	}

	/**
	 * Creates an empty oda designer state.
	 * 
	 * @return an empty oda designer state.
	 */

	public static OdaDesignerState createOdaDesignerState( )
	{
		return new OdaDesignerState( );
	}

	/**
	 * Creates an embedded image from another library embedded image.
	 * 
	 * @param baseImage
	 * @return the created embedded image
	 * 
	 * @deprecated by
	 *             {@link #newEmbeddedImageFrom(EmbeddedImageHandle, String, ModuleHandle)}
	 */

	public static EmbeddedImage createEmbeddedImage(
			EmbeddedImageHandle baseImage )
	{
		if ( baseImage == null )
			return null;
		EmbeddedImage image = new EmbeddedImage( );
		Module module = baseImage.getModule( );
		String namespace = module instanceof Library ? ( (Library) module )
				.getNamespace( ) : null;
		StructRefValue libReference = new StructRefValue( namespace, baseImage
				.getName( ) );
		image.setProperty( EmbeddedImage.LIB_REFERENCE_MEMBER, libReference );
		return image;
	}

	/**
	 * Creates an embedded image from another library embedded image. The name
	 * of the return embedded image fully depends on <code>name</code>.
	 * 
	 * @param baseImage
	 *            the base image
	 * @param name
	 *            the name of the return embedded image
	 * @param targetModule
	 *            the target module that is inserted to
	 * 
	 * @return the created embedded image
	 * @throws LibraryException
	 *             if the library has the <code>baseImage</code> is not
	 *             included in the <code>targetModule</code>
	 */

	public static EmbeddedImage newEmbeddedImageFrom(
			EmbeddedImageHandle baseImage, String name,
			ModuleHandle targetModule ) throws LibraryException
	{
		if ( baseImage == null || targetModule == null )
			return null;
		EmbeddedImage image = new EmbeddedImage( );
		Module baseModule = baseImage.getModule( );

		if ( baseModule instanceof ReportDesign )
			return null;

		// the library with the location path is never included

		Library lib = targetModule.getModule( ).getLibraryByLocation(
				baseModule.getLocation( ) );
		if ( lib == null )
		{
			throw new LibraryException( baseModule,
					new String[]{( (Library) baseModule ).getNamespace( )},
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );
		}

		String namespace = lib.getNamespace( );
		StructRefValue libReference = new StructRefValue( namespace, baseImage
				.getName( ) );
		image.setProperty( EmbeddedImage.LIB_REFERENCE_MEMBER, libReference );
		image.setName( name );

		return image;
	}

	/**
	 * Creates an embedded image from another library embedded image. This
	 * method creates the embedded image name automatically.
	 * 
	 * @param baseImage
	 *            the base image
	 * @param targetModule
	 *            the target module that is inserted to
	 * 
	 * @return the created embedded image
	 * @throws LibraryException
	 *             if the library has the <code>baseImage</code> is not
	 *             included in the <code>targetModule</code>
	 */

	public static EmbeddedImage newEmbeddedImageFrom(
			EmbeddedImageHandle baseImage, ModuleHandle targetModule )
			throws LibraryException
	{
		if ( baseImage == null )
			return null;

		EmbeddedImage newImage = newEmbeddedImageFrom( baseImage, baseImage
				.getName( ), targetModule );
		targetModule.rename( newImage );
		return newImage;
	}

}