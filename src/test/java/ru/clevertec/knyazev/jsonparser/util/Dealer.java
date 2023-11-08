package ru.clevertec.knyazev.jsonparser.util;

import java.util.Map;

public class Dealer {
	private String name;
	private int age;
	private Boolean isTop;
	private Map<Integer, Car> sellingHistory;	
	
	public static class Builder {
		private Dealer dealer;
		
		public Builder() {
			dealer = new Dealer();
		}		
		public Builder setName(String name) {
			dealer.name = name;
			return this;
		}
		public Builder setAge(int age) {
			dealer.age = age;
			return this;
		}
		public Builder setIsTop(Boolean isTop) {
			dealer.isTop = isTop;
			return this;
		}
		public Builder setSellingHistory(Map<Integer, Car> sellingHistory) {
			dealer.sellingHistory = sellingHistory;
			return this;
		}
		
		public Dealer build() {
			return dealer;
		}
	}
}
