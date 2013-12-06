package com.gaslightgames.android.ouyafacadeane.extensions;

import java.util.ArrayList;

import com.adobe.fre.FREContext;

import android.os.Bundle;
import tv.ouya.console.api.OuyaResponseListener;
import tv.ouya.console.api.Product;

public class AIROUYAFacadeANEProductListListener implements OuyaResponseListener<ArrayList<Product>>
{
	private FREContext context;
	
	public AIROUYAFacadeANEProductListListener( FREContext context )
	{
		this.context = context;
	}
	
	@Override
	public void onCancel()
	{
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "PRODUCT_CANCEL", "cancelled" );
	}

	@Override
	public void onFailure( int errorCode, String errorMessage, Bundle bundle )
	{
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "PRODUCT_FAILURE", errorMessage );
	}

	@Override
	public void onSuccess( ArrayList<Product> products )
	{
		// Set AvailableProducts - we need to keep a list of all Products, as we cannot instantiate a Product object.
		((AIROUYAFacadeANEExtensionContext)context).availableProducts = products;
		
		String productsString = "";
		
		// Pass the products to AIR
		// All products are separated by a Pipe "|", with all the elements separated by a Comma ",".
		int i = 0;
		while( products.size() > i )
		{
			Product p = products.get( i );
			
			String product = p.getIdentifier() + "," + p.getName() + "," + p.getDescription() + "," + p.getCurrencyCode() + "," +
					 		 p.getLocalPrice() + "," + p.getOriginalPrice() + "," + p.getPercentOff();
			
			// If we're not the last element, then add a pipe.
			// This helps prevent extra elements in the AS3 level.
			if( i != products.size() - 1 )
			{
				product += "|";
			}
			
			// Concatenate the string - as we can only send strings back to AS3.
			productsString += product;
			
			i++;
		}
		
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "PRODUCT_SUCCESS", productsString );
	}
}
