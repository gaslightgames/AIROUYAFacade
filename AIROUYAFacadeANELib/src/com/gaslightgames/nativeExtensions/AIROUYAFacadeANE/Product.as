package com.gaslightgames.nativeExtensions.AIROUYAFacadeANE
{
	public class Product
	{
		private var _identifier:String;
		private var _name:String;
		private var _desc:String;
		private var _currencyCode:String;
		private var _localPrice:String;
		private var _originalPrice:String;
		private var _percentOff:String;
		
		public function Product( identifier:String, name:String, desc:String, currencyCode:String, localPrice:String, 
								 originalPrice:String, percentOff:String )
		{
			this._identifier = identifier;
			this._name = name;
			this._desc = desc;
			this._currencyCode = currencyCode;
			this._localPrice = localPrice;
			this._originalPrice = originalPrice;
			this._percentOff = percentOff;
		}
		
		public function get identifier():String
		{
			return this._identifier;
		}
		
		public function get name():String
		{
			return this._name;
		}
		
		public function get description():String
		{
			return this._desc;
		}
		
		public function get currencyCode():String
		{
			return this._currencyCode;
		}
		
		public function get localPrice():String
		{
			return this._localPrice;
		}
		
		public function get originalPrice():String
		{
			return this._originalPrice;
		}
		
		public function get percentOff():String
		{
			return this._percentOff;
		}
	}
}