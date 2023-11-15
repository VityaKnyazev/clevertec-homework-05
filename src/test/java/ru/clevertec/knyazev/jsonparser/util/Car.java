package ru.clevertec.knyazev.jsonparser.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Car {

	private String[] producers;

	private Integer productionYear;

	private Boolean isExclusive;

	private Human[][] passengers;

}
