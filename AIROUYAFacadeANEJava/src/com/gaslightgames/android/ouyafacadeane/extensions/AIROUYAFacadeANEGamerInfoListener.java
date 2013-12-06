package com.gaslightgames.android.ouyafacadeane.extensions;

import com.adobe.fre.FREContext;

import android.os.Bundle;
import tv.ouya.console.api.GamerInfo;
import tv.ouya.console.api.OuyaResponseListener;

public class AIROUYAFacadeANEGamerInfoListener implements OuyaResponseListener<GamerInfo>
{
	private FREContext context;
	
	public AIROUYAFacadeANEGamerInfoListener( FREContext context )
	{
		this.context = context;
	}

	@Override
	public void onCancel()
	{
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "GAMERINFO_CANCEL", "Gamer UUID cancelled." );
	}

	@Override
	public void onFailure( int errorCode, String errorMessage, Bundle bundle )
	{
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "GAMERINFO_FAILURE", errorMessage );
	}

	@Override
	public void onSuccess( GamerInfo gamerInfo )
	{
		((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "GAMERINFO_SUCCESS", gamerInfo.getUsername() + "," + gamerInfo.getUuid() );
	}

}
