package com.gaslightgames.android.ouyafacadeane.extensions;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.Product;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

public class AIROUYAFacadeANEExtensionContext extends FREContext
{
	public OuyaFacade ouyaFacade;
	public PublicKey publicKey;
	
	public AIROUYAFacadeANEBroadcastReceiver broadcast;
	public AIROUYAFacadeANEGamerInfoListener gamerInfoListener;
	
	public final Map<String, Product> outstandingPurchaseRequests = new HashMap<String, Product>();
	public ArrayList<Product> availableProducts = new ArrayList<Product>();
	public AIROUYAFacadeANEProductListListener productListListener;
	public AIROUYAFacadeANEPurchaseListener	purchaseListener;
	public AIROUYAFacadeANEReceiptListener receiptListener;
	
	@Override
	public void dispose()
	{
		if( null != broadcast )
		{
			this.getActivity().unregisterReceiver( broadcast );
		}
		
		gamerInfoListener = null;
		productListListener = null;
		purchaseListener = null;
		receiptListener = null;
	}

	@Override
	public Map<String, FREFunction> getFunctions()
	{
		Map<String, FREFunction> functionMap = new HashMap<String, FREFunction>();
		
		functionMap.put( "initFacade", new AIROUYAFacadeANEInit() );
		functionMap.put( "isFacadeSupported", new AIROUYAFacadeANESupported() );
		functionMap.put( "getGamerInfo", new AIROUYAFacadeANEGetGamerInfo() );
		functionMap.put( "putGameData", new AIROUYAFacadeANEPutGameData() );
		functionMap.put( "getGameData", new AIROUYAFacadeANEGetGameData() );
		functionMap.put( "getAllGameData", new AIROUYAFacadeANEGetAllGameData() );
		functionMap.put( "getProdInfo", new AIROUYAFacadeANEGetProdInfo() );
		functionMap.put( "makeProdPurchase", new AIROUYAFacadeANEMakeProdPurchase() );
		functionMap.put( "getProdReceipt", new AIROUYAFacadeANEGetProdReceipt() );

		return functionMap;
	}

}
