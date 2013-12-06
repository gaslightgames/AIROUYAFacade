package com.gaslightgames.android.ouyafacadeane.extensions;

import java.util.List;

import org.json.JSONObject;

import com.adobe.fre.FREContext;

import android.os.Bundle;
import tv.ouya.console.api.OuyaEncryptionHelper;
import tv.ouya.console.api.OuyaResponseListener;
import tv.ouya.console.api.Receipt;

public class AIROUYAFacadeANEReceiptListener implements OuyaResponseListener<String>
{
	private FREContext context;
	
	public AIROUYAFacadeANEReceiptListener( FREContext context )
	{
		this.context = context;
	}
	
	@Override
	public void onCancel()
	{
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "RECEIPT_CANCEL", "receipt cancelled." );
	}

	@Override
	public void onFailure( int errorCode, String errorMessage, Bundle bundle )
	{
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "RECEIPT_FAILURE", errorMessage );
	}

	@Override
	public void onSuccess( String receiptResponse )
	{
		OuyaEncryptionHelper helper = new OuyaEncryptionHelper();
		List<Receipt> receipts = null;
		
		try
		{
			JSONObject jsonResponse = new JSONObject( receiptResponse );
			
			if( jsonResponse.has( "key" ) && jsonResponse.has( "iv" ) )
			{
				receipts = helper.decryptReceiptResponse( jsonResponse, ((AIROUYAFacadeANEExtensionContext)context).publicKey );
			}
			else
			{
				receipts = helper.parseJSONReceiptResponse( receiptResponse );
			}
		}
		catch( Exception exception )
		{
			((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "RECEIPT_FAILURE", exception.getMessage() );
			
			throw new RuntimeException( exception );
		}
		
		String receiptsString = "";
		
		int i = 0;
		while( receipts.size() > i )
		{
			Receipt r = receipts.get( i );
			
			String receipt = r.getIdentifier() + "," + r.getCurrency() + "," + r.getLocalPrice() + "," + r.getPurchaseDate().toString() + "," +
					 		 r.getGeneratedDate().toString() + "," + r.getGamer() + "," + r.getUuid();
			
			// If we're not the last element, then add a pipe.
			// This helps prevent extra elements in the AS3 level.
			if( i != receipts.size() - 1 )
			{
				receipt += "|";
			}
			
			// Concatenate the string - as we can only send strings back to AS3.
			receiptsString += receipt;
			
			i++;
		}
		
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "RECEIPT_SUCCESS", receiptsString );
	}
}
