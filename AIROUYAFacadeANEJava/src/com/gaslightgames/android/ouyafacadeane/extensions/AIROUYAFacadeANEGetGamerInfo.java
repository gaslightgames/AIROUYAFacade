package com.gaslightgames.android.ouyafacadeane.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AIROUYAFacadeANEGetGamerInfo implements FREFunction
{

	@Override
	public FREObject call( FREContext context, FREObject[] args )
	{
		// Query the OUYA Facade
		// Get the Player Info instance?
		if( null == ((AIROUYAFacadeANEExtensionContext)context).gamerInfoListener )
		{
			((AIROUYAFacadeANEExtensionContext)context).gamerInfoListener = new AIROUYAFacadeANEGamerInfoListener( context );
		}
		
		((AIROUYAFacadeANEExtensionContext)context).ouyaFacade.requestGamerInfo( ((AIROUYAFacadeANEExtensionContext)context).gamerInfoListener );
		
		return null;
	}

}
