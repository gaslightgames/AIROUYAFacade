package com.gaslightgames.android.ouyafacadeane.extensions;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import tv.ouya.console.api.Product;
import tv.ouya.console.api.Purchasable;

import android.util.Base64;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AIROUYAFacadeANEMakeProdPurchase implements FREFunction
{

	@Override
	public FREObject call( FREContext context, FREObject[] passedArgs )
	{
		try
		{
			// Create a Product - from the parameters passed
			FREObject fro = passedArgs[0];
			String identifier = fro.getAsString();
			
			if( ((AIROUYAFacadeANEExtensionContext)context).availableProducts != null )
			{
				// Using the Identifier - loop through the Products, and get the matching Product
				int i = 0;
				Product product = null;
				
				while( ((AIROUYAFacadeANEExtensionContext)context).availableProducts.size() > i )
				{
					if( ((AIROUYAFacadeANEExtensionContext)context).availableProducts.get( i ).getIdentifier().equals( identifier ) )
					{
						product = ((AIROUYAFacadeANEExtensionContext)context).availableProducts.get( i );
						
						break;
					}
					
					i++;
				}
			
				if( null != product )
				{
					SecureRandom sr = SecureRandom.getInstance( "SHA1PRNG" );
					
					String uniqueId = Long.toHexString( sr.nextLong() );
					
					JSONObject purchaseRequestJson = new JSONObject();
					purchaseRequestJson.put( "uuid", uniqueId );
					purchaseRequestJson.put( "identifier", identifier );
					
					String purchaseRequest = purchaseRequestJson.toString();
					
					byte[] keyBytes = new byte[16];
					sr.nextBytes( keyBytes );
					SecretKey key = new SecretKeySpec( keyBytes, "AES" );
					
					byte[] ivBytes = new byte[16];
					sr.nextBytes( ivBytes );
					IvParameterSpec iv = new IvParameterSpec( ivBytes );
					
					Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding", "BC" );
					cipher.init( Cipher.ENCRYPT_MODE, key, iv );
					byte[] payload = cipher.doFinal( purchaseRequest.getBytes( "UTF-8" ) );
					
					cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding", "BC" );
					cipher.init( Cipher.ENCRYPT_MODE, ((AIROUYAFacadeANEExtensionContext)context).publicKey );
					byte[] encryptedKey = cipher.doFinal( keyBytes );
					
					Purchasable purchasable = new Purchasable( identifier,
															   Base64.encodeToString( encryptedKey, Base64.NO_WRAP ),
															   Base64.encodeToString( ivBytes, Base64.NO_WRAP ),
															   Base64.encodeToString( payload, Base64.NO_WRAP ) );
					
					synchronized( ((AIROUYAFacadeANEExtensionContext)context).outstandingPurchaseRequests )
					{
						((AIROUYAFacadeANEExtensionContext)context).outstandingPurchaseRequests.put( uniqueId, product );
					}
					
					if( ((AIROUYAFacadeANEExtensionContext)context).purchaseListener == null )
					{
						((AIROUYAFacadeANEExtensionContext)context).purchaseListener = new AIROUYAFacadeANEPurchaseListener( context, product );
					}
					
					((AIROUYAFacadeANEExtensionContext)context).ouyaFacade.requestPurchase( purchasable, ((AIROUYAFacadeANEExtensionContext)context).purchaseListener );
				}
				else
				{
					((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "PURCHASE_FAILURE", "Error making Purchase, Identifier does not match any downloaded products." );
				}
			}
			else
			{
				((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "PURCHASE_FAILURE", "Error making Purchase, Products have not been gathered from OUYA Servers" );
			}
		}
		catch( Exception exception )
		{
			exception.printStackTrace();
			
			((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "PURCHASE_FAILURE", "Error making Purchase," + exception.getMessage() );
		}
		
		return null;
	}

}
