package com.example.demo.petstore.shared.dto;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * pet status in the store
 */
public enum PetSex {
	M("Male"),

	F("Female");

	private String value;

	PetSex(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return value;
	}
}
