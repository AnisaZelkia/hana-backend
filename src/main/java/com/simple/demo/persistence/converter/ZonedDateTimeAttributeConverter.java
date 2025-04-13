package com.simple.demo.persistence.converter;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
		return zonedDateTime == null ? null : Timestamp.from(zonedDateTime.toInstant());
	}

	@Override
	public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toInstant().atZone(ZoneId.systemDefault());
	}
}
