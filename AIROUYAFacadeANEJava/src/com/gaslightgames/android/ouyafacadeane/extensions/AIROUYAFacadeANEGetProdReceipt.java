package com.gaslightgames.android.ouyafacadeane.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AIROUYAFacadeANEGetProdReceipt implements FREFunction
{

	@Override
	public FREObject call( FREContext context, FREObject[] passedArgs )
	{
		try
		{
			((AIROUYAFacadeANEExtensionContext)context).ouyaFacade.requestReceipts( ((AIROUYAFacadeANEExtensionContext)context).receiptListener );
		}
		catch( Exception exception )
		{
			exception.printStackTrace();
			
			((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "RECEIPT_FAILURE", "Error getting receipts," + exception.getMessage() );
		}
		
		return null;
	}

}
