package ru.clevertec.knyazev.jsonparser.util;

import java.util.List;
import java.util.Set;

public class ManPassport {
	private String name;
	private String family;
	private List<String> addresses;
	private List<Human> wives;
	private Set<Human> childrens;
	
	
	public static class Builder {
		private ManPassport manPassport;
		
		public Builder() {
			manPassport = new ManPassport();
		}
		
		public Builder setName(String name) {
			manPassport.name = name;
			return this;
		}
		public Builder setFamily(String family) {
			manPassport.family = family;
			return this;
		}
		public Builder setAddresses(List<String> addresses) {
			manPassport.addresses = addresses;
			return this;
		}
		public Builder setWives(List<Human> wives) {
			manPassport.wives = wives;
			return this;
		}
		public Builder setChildrens(Set<Human> childrens) {
			manPassport.childrens = childrens;
			return this;
		}
		
		public ManPassport build() {
			return manPassport;
		}
	}
}
