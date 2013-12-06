package com.gaslightgames.nativeExtensions.AIROUYAFacadeANE
{
	public class Gamer
	{
		private var _username:String;
		private var _uuid:String
		
		public function Gamer( username:String, uuid:String )
		{
			this._username = username;
			this._uuid = uuid;
		}
		
		public function get username():String
		{
			return this._username;
		}
		
		public function get uuid():String
		{
			return this._uuid;
		}
	}
}