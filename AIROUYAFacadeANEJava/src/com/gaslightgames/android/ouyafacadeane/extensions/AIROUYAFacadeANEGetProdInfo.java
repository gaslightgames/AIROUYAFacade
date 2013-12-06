package com.gaslightgames.android.ouyafacadeane.extensions;

import java.util.ArrayList;
import java.util.List;

import tv.ouya.console.api.Purchasable;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AIROUYAFacadeANEGetProdInfo implements FREFunction
{

	@Override
	public FREObject call( FREContext context, FREObject[] passedArgs )
	{
		try
		{
			FREObject fro = passedArgs[0];
			String product = fro.getAsString();

			// Create a new List to pass to OUYAs servers
			List<Purchasable> productList = new ArrayList<Purchasable>();
			
			// Separate all products - delimited by commas
			String[] products = product.split( "," );
			
			// loop through all "products" and populate the List
			int i = 0;
			while( products.length > i )
			{
				productList.add( new Purchasable( products[i] ) );
				
				i++;
			}
			
			((AIROUYAFacadeANEExtensionContext)context).ouyaFacade.requestProductList( productList, ((AIROUYAFacadeANEExtensionContext)context).productListListener );
		}
		catch( Exception exception )
		{
			exception.printStackTrace();
			
			((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "PRODUCT_FAILURE", "Error getting Product," + exception.getMessage() );
		}
		
		return null;
	}

}
