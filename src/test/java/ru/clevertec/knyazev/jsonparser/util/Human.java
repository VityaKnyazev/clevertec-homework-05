package ru.clevertec.knyazev.jsonparser.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Human {

	private String name;

	private String family;

	private int age;

	private boolean isGod;

	private Integer childrenQuantity;

}
