package ru.clevertec.knyazev.jsonparser.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Family {
	private String name;
	private int ageTogether;
	private Human man;
	private Human woman;

}
