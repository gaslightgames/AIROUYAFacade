package com.gaslightgames.android.ouyafacadeane.extensions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AIROUYAFacadeANEBroadcastReceiver extends BroadcastReceiver
{
	AIROUYAFacadeANEExtensionContext parent;
	
	public AIROUYAFacadeANEBroadcastReceiver( AIROUYAFacadeANEExtensionContext parent )
	{
		super();
		
		this.parent = parent;
	}
	
	@Override
	public void onReceive( Context context, Intent service )
	{
		// We only receive tv.ouya.metrics.action.UPDATE events - when the Menu is appearing/disappearing
		this.parent.dispatchStatusEventAsync( "MENU", "update" );
	}

}
