package com.init.barcode.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EncodedPDF417 {

	@JsonProperty("dataBase64")
	private String dataBase64;

	public String getDataBase64() {
		return dataBase64;
	}

	public void setDataBase64(String dataBase64) {
		this.dataBase64 = dataBase64;
	}

}
