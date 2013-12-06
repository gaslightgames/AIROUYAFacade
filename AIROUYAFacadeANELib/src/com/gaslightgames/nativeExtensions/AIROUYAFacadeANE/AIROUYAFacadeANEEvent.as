package com.gaslightgames.nativeExtensions.AIROUYAFacadeANE
{
	import flash.events.Event;
	
	public class AIROUYAFacadeANEEvent extends Event
	{
		public static const MENU_UPDATE:String 				= "menuupdate";
		public static const GAMEDATA:String					= "gamedata";
		public static const GAMER:String					= "gamer";
		
		public static const PRODUCT:String					= "product";
		public static const PURCHASE:String					= "purchase";
		public static const RECEIPT:String					= "receipt";
		
		private var _status:String;
		private var _data:*;
		
		public function AIROUYAFacadeANEEvent( type:String, data:*, status:String = "", bubbles:Boolean = false, cancelable:Boolean = false )
		{
			_status = status;
			_data = data;
			
			super( type, bubbles, cancelable );
		}
		
		/**
		 * Status can be one of 3 types:
		 * <br> SUCCESS
		 * <br> FAILURE
		 * <br> CANCEL
		 */
		public function get status():String
		{
			return _status;
		}
		
		/**
		 * Data can be returned as one of several objects:
		 * <br> Product: 	returned after Product Info Request.
		 * <br> Receipt: 	returned after Receipt Request.
		 * <br> Purchase:	returned after Purchase Request.
		 * <br> Gamer:		returned after a Gamer UUID Request.
		 * <br> null:		returned after any FAILURE/CANCEL event status.
		 */
		public function get data():*
		{
			return _data;
		}
	}
}