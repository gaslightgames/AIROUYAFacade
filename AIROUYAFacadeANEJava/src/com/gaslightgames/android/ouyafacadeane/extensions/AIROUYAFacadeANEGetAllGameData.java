package com.gaslightgames.android.ouyafacadeane.extensions;

import java.util.Map;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class AIROUYAFacadeANEGetAllGameData implements FREFunction
{

	@Override
	public FREObject call( FREContext context, FREObject[] args )
	{
		FREObject returnObj = null;
		
		try
		{
			// Get the String Map of all Data
			Map<String, String> mapData = ((AIROUYAFacadeANEExtensionContext)context).ouyaFacade.getAllGameData();
			
			Log.i( "AIR_OUYA_FACADE_ANE_GET_ALL_GAME_DATA", "Map Data Size: " + mapData.size() );
			
			String data = "";
			
			// Loop through all data in the Map
			for( Map.Entry<String, String> entry : mapData.entrySet() )
			{
				String key = entry.getKey();
				String value = entry.getValue();
				
				Log.i( "AIR_OUA_FACADE_ANE_GET_ALL_GAME_DATA", "Key: " + key + ", Value: " + value );
				
				// Concatenate the String
				//data.concat( entry.getKey() + "," + entry.getValue() + "," );
				data += key + "|";
				data += value + "|";
			}
			
			Log.i( "AIR_OUA_FACADE_ANE_GET_ALL_GAME_DATA", data );
			
			// Convert the String to an FREObject
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
