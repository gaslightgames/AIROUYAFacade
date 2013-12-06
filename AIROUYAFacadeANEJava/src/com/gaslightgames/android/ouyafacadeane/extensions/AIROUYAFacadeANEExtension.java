package com.gaslightgames.android.ouyafacadeane.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

public class AIROUYAFacadeANEExtension implements FREExtension
{
	public static FREContext extensionContext;
	
	public FREContext createContext( String contextType )
	{
		// Return a new AIROUYAFacadeANEExtensionContext
		return new AIROUYAFacadeANEExtensionContext();
	}

	public void dispose()
	{
		
	}

	public void initialize()
	{
		
	}

}
