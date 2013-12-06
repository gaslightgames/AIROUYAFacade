package com.gaslightgames.nativeExtensions.AIROUYAFacadeANE
{
	public class Receipt
	{
		private var _identifier:String;
		private var _currency:String;
		private var _localPrice:String;
		private var _purcaseDate:String;
		private var _generatedDate:String;
		private var _gamer:String;
		private var _uuid:String;
		
		public function Receipt( identifier:String, currency:String, localPrice:String, purchaseDate:String,
								 generatedDate:String, gamer:String, uuid:String )
		{
			this._identifier = identifier;
			this._currency = currency;
			this._localPrice = localPrice;
			this._purcaseDate = purchaseDate;
			this._generatedDate = generatedDate;
			this._gamer = gamer;
			this._uuid = uuid;
		}
		
		public function get identifier():String
		{
			return this._identifier;
		}
		
		public function get currency():String
		{
			return this._currency;
		}
		
		public function get localPrice():String
		{
			return this._localPrice;
		}
		
		public function get purchaseDate():String
		{
			return this._purcaseDate;
		}
		
		public function get generatedDate():String
		{
			return this._generatedDate;
		}
		
		public function get gamer():String
		{
			return this._gamer;
		}
		
		public function get uuid():String
		{
			return this._uuid;
		}
	}
}