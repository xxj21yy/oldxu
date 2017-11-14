package com.example.citroen;

import java.io.Serializable;

public class CarData implements Serializable{
	
	private static final long serialVersionUID = 4847694472766936072L;
	
	private String referenceAffichable;
	private String reference;
	public String getReferenceAffichable() {
		return referenceAffichable;
	}
	public void setReferenceAffichable(String referenceAffichable) {
		this.referenceAffichable = referenceAffichable;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
}
