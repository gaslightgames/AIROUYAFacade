package com.gaslightgames.nativeExtensions.AIROUYAFacadeANE
{
	import com.gaslightgames.nativeExtensions.AIROUYAFacadeANE.Gamer;
	
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	
	public class AIROUYAFacadeANE extends EventDispatcher
	{
		public static var instance:AIROUYAFacadeANE;
		
		private static var extContext:ExtensionContext = null;
		
		private static var _isSupported:Boolean = false;
		private static var _isSupportedSet:Boolean = false;
		
		private static var _gamer:Gamer;
		private static var _developerId:String;
		
		private static var _products:Vector.<Product>;
		
		/**
		 * Attempts to get the current instance of the AIROUYAFacadeANE.
		 * <p>
		 * developerId: Your ID from the OUYA Developer website.
		 * <br>applicationKey: A ByteArray loaded from *.der key file.  The first launch this MUST be supplied.
		 * <p>
		 * Returns: an instance of AIROUYAFacadeANE.
		 */
		public static function getInstance( developerId:String, applicationKey:ByteArray = null ):AIROUYAFacadeANE
		{
			if( null == instance )
			{
				if( null != applicationKey )
				{
					_developerId = developerId;
					instance = new AIROUYAFacadeANE( developerId, applicationKey, new SingletonEnforcer() );
				}
				else
				{
					throw new Error( "Application Key Required on First Call!" );
				}
			}
			
			if( _developerId == developerId )
			{
				return instance;
			}
			else
			{
				throw new Error( "Developer ID does not match." );
			}
		}
		
		/**
		 * AIROUYAFacadeANE is a Singletone, use getInstance instead!
		 */
		public function AIROUYAFacadeANE( developerId:String, applicationKey:ByteArray, enforcer:SingletonEnforcer, target:IEventDispatcher=null )
		{
			trace( "Building OUYA Facade ANE" );
			if( !extContext )
			{
				trace( "Building Extension Context" );
				extContext = ExtensionContext.createExtensionContext( "com.gaslightgames.AIROUYAFacadeANE", "AIROUYAFacadeANE" );
				
				extContext.call( "initFacade", developerId, applicationKey );
				
				if( extContext )
				{
					trace( "OUYA Facade ANE Initialised." );
					
					extContext.addEventListener( StatusEvent.STATUS, onStatus );
				}
				else
				{
					trace( "OUYA Facade ANE Initiasation FAILED!" );
				}
			}
			
			super(target);
		}
		
		private function onStatus( statusEvent:StatusEvent ):void
		{
			var codeSplitStrings:Array = String( statusEvent.code ).split( "_" );
			var levelSplitStrings:Array = String( statusEvent.level ).split( "," );
			
			if( "GAMERINFO" == codeSplitStrings[0] )
			{
				this.processGamerInfo( codeSplitStrings, levelSplitStrings );
			}
			
			if( "MENU" == statusEvent.code )
			{
				this.dispatchEvent( new AIROUYAFacadeANEEvent( AIROUYAFacadeANEEvent.MENU_UPDATE, null, levelSplitStrings[0] ) );
			}
			
			if( "GAMEDATA" == codeSplitStrings[0] )
			{
				this.dispatchEvent( new AIROUYAFacadeANEEvent( AIROUYAFacadeANEEvent.GAMEDATA, null, levelSplitStrings[0] ) );
			}
			
			// Request a list of available Products
			if( "PRODUCT" == codeSplitStrings[0] )
			{
				this.processProduct( codeSplitStrings[1], statusEvent.level );
			}
			
			// If a purchase request has been made
			if( "PURCHASE" == codeSplitStrings[0] )
			{
				this.processPurchase( codeSplitStrings[1], statusEvent.level );
			}
			
			// Request a list of purchased items
			if( "RECEIPT" == codeSplitStrings[0] )
			{
				this.processReceipt( codeSplitStrings[1], statusEvent.level );
			}
		}
		
		private function processGamerInfo( code:Array, level:Array ):void
		{
			if( "SUCCESS" == code[1] )
			{
				// Build Gamer with complete information - username, uuid
				_gamer = new Gamer( level[0], level[1] );
			}
			
			// Dispatch Gamer Event
			this.dispatchEvent( new AIROUYAFacadeANEEvent( AIROUYAFacadeANEEvent.GAMER, _gamer, code[1]) );
		}
		
		private function processProduct( status:String, data:String ):void
		{
			var product:Product;
			
			if( "SUCCESS" == status )
			{
				// Check if the Products Vector has been instantiated
				if( _products == null )
				{
					_products = new Vector.<Product>();
				}
				
				// Split the data string on "|" pipes.
				var dataSplitStrings:Array = String( data ).split( "|" );
				
				// Now loop through each element - which is a product.
				// Split the products propertis on a "," comma
				var i:int = 0;
				while( dataSplitStrings.length > i )
				{
					var productSplit:Array = String( dataSplitStrings[i] ).split( "," );
					
					// Build a product with complete information - identifier, name etc
					product = new Product( productSplit[0], productSplit[1], productSplit[2], productSplit[3], productSplit[4], productSplit[5], productSplit[6] );
					
					// Make sure we have a successfully built a product - and add it to the Products Vector
					if( null != product )
					{
						// Check the product does NOT already exist in our list
						var j:int = 0;
						while( _products.length > j )
						{
							if( _products[j].identifier == product.identifier )
							{
								break;
							}
							
							j++;
						}
						
						// if j = _products.length, then we have exhausted all items in the list
						// and this product is not currently in our products Vector.
						if( _products.length <= j )
						{
							_products.push( product );
						}
					}
					
					i++;
				}
			}
			
			this.dispatchEvent( new AIROUYAFacadeANEEvent( AIROUYAFacadeANEEvent.PRODUCT, null, status ) );
		}
		
		private function processPurchase( status:String, data:String ):void
		{
			// Need to handle if SUCCESS or FAILURE.
			// If SUCCESS, then we can create a Product instance - as that is what is returned,
			// and then add this to the new Event.
			
			this.dispatchEvent( new AIROUYAFacadeANEEvent( AIROUYAFacadeANEEvent.PURCHASE, data, status ) );
		}
		
		private function processReceipt( status:String, data:String ):void
		{
			// Need to handle if SUCCESS or FAILURE
			// If SUCCESS, then create a Receipt instance.
			
			this.dispatchEvent( new AIROUYAFacadeANEEvent( AIROUYAFacadeANEEvent.RECEIPT, data, status ) );
		}
		
		/**
		 * Gets the GamerInfo of the currently signed in Gamer
		 * <p>
		 * The first run will query the Extension Context (Native Extension) so you will
		 * <br>have to add an event listener.
		 * <br>Subsequent calls will return a valid Gamer object.
		 */
		public function getGamerInfo():Gamer
		{
			if( _gamer == null )
			{
				extContext.call( "getGamerInfo" );
			}
			else
			{
				return _gamer;
			}
			
			return null;
		}
		
		/**
		 * Puts the Data into the Save Data
		 * <p>
		 * identifier: The identifier for the data pair.
		 * <br>data: The data to for the identifier/data pair.
		 */
		public function putGameData( identifier:String, data:String ):void
		{
			extContext.call( "putGameData", identifier, data );
		}
		
		/**
		 * Gets the Game Data.
		 * <p>
		 * identifier: The identifier that matches the data pair.
		 * <p>
		 * Returns a String with the Game Data.
		 */
		public function getGameData( identifier:String ):String
		{
			var returnString:String = extContext.call( "getGameData", identifier ) as String;
			
			return returnString;
		}
		
		/**
		 * Gets all Game Data, parses and returns a Dictionary.
		 */
		public function getAllGameData():Dictionary
		{
			var allData:String = extContext.call( "getAllGameData" ) as String;
			
			// Split string, against commas
			var splitStrings:Array = String( allData ).split( "|" );
			
			// Process and apply to Dictionary
			var returnDictionary:Dictionary = new Dictionary();
			
			var i:int = 0;
			while( splitStrings.length > i )
			{
				// Get the Identifier and the Data
				//returnDictionary[splitStrings[i]] = splitStrings[i+1];
				
				//i+2;
				
				var key:String = splitStrings[i];
				i++;
				var data:String = splitStrings[i];
				i++;
				
				returnDictionary[key] = data;
			}
			
			return returnDictionary;
		}
		
		/**
		 * Make the request to OUYA for all the products.
		 */
		public function requestProductList( ... args ):void
		{
			var products:String = "";
			
			var i:int = 0;
			while( args.length > i )
			{
				// Convert all passed arguments to a single, commad delimited string
				if( args[i] is String )
				{
					products += args[i] + ",";
				}
				else
				{
					trace( "All desired products MUST be a String" );
				}
				
				i++;
			}
			
			if( args.length > 0 )
			{
				extContext.call( "getProdInfo", products );
			}
			else
			{
				trace( "Must pass some product names!" );
			}
		}
		
		/**
		 * Once you have called requestProdList and the Products have been returned, then this will be populated.
		 */
		public function getProductList():Vector.<Product>
		{
			return _products;
		}
		
		public function makeProductPurchase( identifier:String ):void
		{
			extContext.call( "makeProdPurchase", identifier );
		}
		
		public function getProductReceipt():void
		{
			extContext.call( "getProdReceipt" );
		}
		
		/**
		 * Queries the Native Extension if this platform is supported
		 */
		public static function get isSupported():Boolean
		{
			if( !_isSupportedSet )
			{
				try
				{
					_isSupportedSet = true;
					
					trace( "Building Context..." );
					var testContext:ExtensionContext = ExtensionContext.createExtensionContext( "com.gaslightgames.AIROUYAFacadeANE", "AIROUYAFacadeANE" );
					trace( "Running Native isSupported..." );
					_isSupported = testContext.call( "isFacadeSupported" );// as Boolean;
					testContext.dispose();
				}
				catch( error:Error )
				{
					trace( error.message, error.errorID );
					
					return _isSupported;
				}
			}
			
			return _isSupported;
		}
	}
}

internal class SingletonEnforcer{}