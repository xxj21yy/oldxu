package com.example.citroen;

import java.io.Serializable;
import java.util.List;

public class DataJson implements Serializable{
	
	private static final long serialVersionUID = 4847694472766936072L;
	
	private String libelleFct;
	private String niveauFct;
	private String ordreAffichage;
	private String id;
	private boolean grisee;
	private List<DataJson> fctionsDependantes;
	
	public String getLibelleFct() {
		return libelleFct;
	}
	public void setLibelleFct(String libelleFct) {
		this.libelleFct = libelleFct;
	}
	public String getNiveauFct() {
		return niveauFct;
	}
	public void setNiveauFct(String niveauFct) {
		this.niveauFct = niveauFct;
	}
	public String getOrdreAffichage() {
		return ordreAffichage;
	}
	public void setOrdreAffichage(String ordreAffichage) {
		this.ordreAffichage = ordreAffichage;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isGrisee() {
		return grisee;
	}
	public void setGrisee(boolean grisee) {
		this.grisee = grisee;
	}
	public List<DataJson> getFctionsDependantes() {
		return fctionsDependantes;
	}
	public void setFctionsDependantes(List<DataJson> fctionsDependantes) {
		this.fctionsDependantes = fctionsDependantes;
	}
	
}
