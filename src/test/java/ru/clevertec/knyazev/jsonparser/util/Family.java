package ru.clevertec.knyazev.jsonparser.util;

public class Family {
	private String name;
	private int ageTogether;
	private Human man;
	private Human woman;
	
	public Family() {		
	}

	public static class Builder {
		Family family;

		public Builder() {
			family = new Family();
		}
		
		public Builder setName(String name) {
			family.name = name;
			return this;
		}

		public Builder setAgeTogether(int ageTogether) {
			family.ageTogether = ageTogether;
			return this;
		}

		public Builder setMan(Human man) {
			family.man = man;
			return this;
		}

		public Builder setWoman(Human woman) {
			family.woman = woman;
			return this;
		}

		public Family build() {
			return family;
		}
	}
}
