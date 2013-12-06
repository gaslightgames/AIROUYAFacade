package com.gaslightgames.android.ouyafacadeane.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AIROUYAFacadeANEGetGameData implements FREFunction
{

	@Override
	public FREObject call( FREContext context, FREObject[] args )
	{
		FREObject returnObj = null;
		
		try
		{
			// Get the argument
			// - First is the Dictionary identifier
			FREObject fro = args[0];
			String identifier = fro.getAsString();
			
			// Use the identifier to get the Data
			String data = ((AIROUYAFacadeANEExtensionContext)context).ouyaFacade.getGameData( identifier );
			
			returnObj = FREObject.newObject( data );
		}
		catch( Exception exception )
		{
			exception.printStackTrace();
			
			((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "GAMEDATA_FAILURE", "Error putting game data," + exception.getMessage() );
		}
		
		return returnObj;
	}

}
