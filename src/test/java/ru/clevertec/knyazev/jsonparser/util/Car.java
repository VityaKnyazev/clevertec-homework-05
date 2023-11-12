package ru.clevertec.knyazev.jsonparser.util;

public class Car {
	private String[] producers;
	private Integer productionYear;
	private Boolean isExclusive;
	private Human[][] passengers;

	Car() {
	}
	
	public static class Builder {
		Car car;
		
		public Builder() {
			car = new Car();
		}
		
		public Builder setProducers(String[] producers) {
			car.producers = producers;
			return this;
		}
		public Builder setProductionYear(Integer productionYear) {
			car.productionYear = productionYear;
			return this;
		}
		public Builder setIsExclusive(Boolean isExclusive) {
			car.isExclusive = isExclusive;
			return this;
		}
		public Builder setPassengers(Human[][] passengers) {
			car.passengers = passengers;
			return this;
		}
		
		public Car build() {
			return car;
		}
	}

}
