package
{
	import com.gaslightgames.nativeExtensions.AIROUYAFacadeANE.AIROUYAFacadeANE;
	import com.gaslightgames.nativeExtensions.AIROUYAFacadeANE.AIROUYAFacadeANEEvent;
	import com.gaslightgames.nativeExtensions.AIROUYAFacadeANE.Gamer;
	import com.gaslightgames.nativeExtensions.AIROUYAFacadeANE.Product;
	
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.events.Event;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	import flash.utils.Endian;
	
	public class AIROUYAFacadeANETest extends Sprite
	{
		private var ouyaFacade:AIROUYAFacadeANE;
		
		public function AIROUYAFacadeANETest()
		{
			super();
			
			// support autoOrients
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			
			this.init();
		}
		
		private function init():void
		{
			var urlRequest:URLRequest = new URLRequest( "key.der" );	// Needs to be in your bin directory!
			var urlLoader:URLLoader = new URLLoader();
			urlLoader.addEventListener( Event.COMPLETE, onKeyLoad );
			urlLoader.dataFormat = URLLoaderDataFormat.BINARY;
			urlLoader.load( urlRequest );
		}
		
		private function onKeyLoad( event:Event ):void
		{
			( event.target as URLLoader ).removeEventListener( Event.COMPLETE, onKeyLoad );
			
			// Get the Key data - as a ByteArray so we can pass it to the ANE
			var key:ByteArray = ( event.target as URLLoader ).data as ByteArray;
			key.endian = Endian.LITTLE_ENDIAN;
			
			if( AIROUYAFacadeANE.isSupported )
			{
				trace( "ANE isSupported." );
				this.ouyaFacade = AIROUYAFacadeANE.getInstance( "your-developer-key-here", key );
				this.ouyaFacade.addEventListener( AIROUYAFacadeANEEvent.MENU_UPDATE, OUYAMenuUpdate );
				this.ouyaFacade.addEventListener( AIROUYAFacadeANEEvent.GAMER, onGamer );
				this.ouyaFacade.addEventListener( AIROUYAFacadeANEEvent.PRODUCT, onProduct );
				
				// The first call to this will take time - as it is requested from the Native Side.
				// But subsequent calls will return a Gamer instance.
				this.ouyaFacade.getGamerInfo();
				
				this.ouyaFacade.putGameData( "high_score", "1,2,3,4,5,6,7,8,9,10" );
				var test:String = this.ouyaFacade.getGameData( "high_score" );
				trace( "High Score: " + test );
				
				this.ouyaFacade.putGameData( "level_data", "stuff,stuff,more stuff" );
				var dict:Dictionary = this.ouyaFacade.getAllGameData();
				var highScore:String = dict["high_score"];
				var levelData:String = dict["level_data"];
				
				trace( "Data from Dictionary. High Score: " + highScore + ", Level Data: " + levelData );
				
				// Call this - which contacts the ODK, then OUYAs servers and gets a list of products.
				// Once this has been done successfully, you only need to call getProductList() and not need comm with the server again.
				this.ouyaFacade.requestProductList( "test", "test_entitlement" );
			}
			else
			{
				trace( "ANE is NOT supported." );
			}
		}
		
		private function OUYAMenuUpdate( facadeEvent:AIROUYAFacadeANEEvent ):void
		{
			trace( "OUYA Menu Event!" );
		}
		
		private function onGamer( facadeEvent:AIROUYAFacadeANEEvent ):void
		{
			// We no longer need this - as you can call getGamerInfo() and get a Gamer instance immediately.
			this.ouyaFacade.removeEventListener( AIROUYAFacadeANEEvent.GAMER, onGamer );
			
			// Gamer Info from Event
			var gamer:Gamer = facadeEvent.data as Gamer;
			if( null != gamer )
			{
				trace( "Gamer Username: " + gamer.username + ", and UUID: " + gamer.uuid );
			}
			
			var gamer2:Gamer = this.ouyaFacade.getGamerInfo();
			if( null != gamer2 )
			{
				trace( "Gamer 2 Also shows Username: " + gamer2.username + ", and UUID: " + gamer2.uuid );
			}
		}
		
		private function onProduct( facadeEvent:AIROUYAFacadeANEEvent ):void
		{
			this.ouyaFacade.removeEventListener( AIROUYAFacadeANEEvent.PRODUCT, onProduct );
			
			var products:Vector.<Product> = this.ouyaFacade.getProductList();
			
			var i:int = 0;
			while( products.length > i )
			{
				trace( "Product " + i + " is: " + products[i].name + " and costs: " + products[i].originalPrice );
				
				i++;
			}
			
			this.ouyaFacade.addEventListener( AIROUYAFacadeANEEvent.PURCHASE, onPurchase );
			this.ouyaFacade.makeProductPurchase( products[0].identifier );
		}
		
		private function onPurchase( facadeEvent:AIROUYAFacadeANEEvent ):void
		{
			this.ouyaFacade.removeEventListener( AIROUYAFacadeANEEvent.PURCHASE, onPurchase );
			
			trace( "Product Purchase Request Result: " + facadeEvent.status + ", " + facadeEvent.data );
			
			this.ouyaFacade.addEventListener( AIROUYAFacadeANEEvent.RECEIPT, onReceipt );
			this.ouyaFacade.getProductReceipt();
		}
		
		private function onReceipt( facadeEvent:AIROUYAFacadeANEEvent ):void
		{
			this.ouyaFacade.removeEventListener( AIROUYAFacadeANEEvent.RECEIPT, onReceipt );
			
			trace( "Receipt Request Result: " + facadeEvent.status + ", " + facadeEvent.data );
		}
	}
}