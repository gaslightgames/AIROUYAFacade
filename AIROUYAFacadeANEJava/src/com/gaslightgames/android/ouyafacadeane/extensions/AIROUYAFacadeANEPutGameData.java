package com.gaslightgames.android.ouyafacadeane.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AIROUYAFacadeANEPutGameData implements FREFunction
{
	@Override
	public FREObject call( FREContext context, FREObject[] args )
	{
		try
		{
			// Get the two arguments
			// - First is the Dictionary identifier
			// - Second is the Data
			FREObject fro = args[0];
			String identifier = fro.getAsString();
			
			fro = args[1];
			String data = fro.getAsString();
			
			((AIROUYAFacadeANEExtensionContext)context).ouyaFacade.putGameData( identifier, data );
		}
		catch( Exception exception )
		{
			exception.printStackTrace();
			
			((AIROUYAFacadeANEExtensionContext)context).dispatchStatusEventAsync( "GAMEDATA_FAILURE", "Error putting game data," + exception.getMessage() );
		}
		
		return null;
	}

}
