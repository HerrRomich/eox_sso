package com.ebase.eox.test.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Health {
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
