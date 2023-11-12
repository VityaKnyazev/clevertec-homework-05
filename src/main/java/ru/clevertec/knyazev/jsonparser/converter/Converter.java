package ru.clevertec.knyazev.jsonparser.converter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

public interface Converter {

	Logger log = LoggerFactory.getLogger(Converter.class);

	String CONVERTING_ERROR = "Error when converting String value=%s to %s%n";
	
	public static Boolean convertToBoolean(String value) {
		Boolean converted = null;

		return Boolean.valueOf(value);
	}
	
	public static Byte convertToByte(String value) {
		Byte converted = null;

		try {
			converted = Byte.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(String.format(CONVERTING_ERROR), value, "Byte");
		}

		return converted;
	}
	
	public static Short convertToShort(String value) {
		Short converted = null;

		try {
			converted = Short.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(String.format(CONVERTING_ERROR), value, "Short");
		}

		return converted;
	}
	
	public static Integer convertToInteger(String value) {
		Integer converted = null;

		try {
			converted = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(String.format(CONVERTING_ERROR), value, "Integer");
		}

		return converted;
	}
	
	public static Long convertToLong(String value) {
		Long converted = null;

		try {
			converted = Long.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(String.format(CONVERTING_ERROR), value, "Long");
		}

		return converted;
	}
	
	public static Float convertToFloat(String value) {
		Float converted = null;

		try {
			converted = Float.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(String.format(CONVERTING_ERROR), value, "Float");
		}

		return converted;
	}
	
	public static Double convertToDouble(String value) {
		Double converted = null;

		try {
			converted = Double.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(String.format(CONVERTING_ERROR), value, "Double");
		}

		return converted;
	}
	
	public static Character convertToCharacter(String value) {
		Character converted = null;

		if (value == null || value.length() != 1) {
			return converted;
		} else {
			converted = Character.valueOf(value.charAt(0));
		}

		return converted;
	}
}
