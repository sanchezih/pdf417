package com.init.barcode.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecodedPDF417 {

	@JsonProperty("data")
	private String data = null;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
