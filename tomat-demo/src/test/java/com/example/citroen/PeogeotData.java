package com.example.citroen;

import java.io.Serializable;

public class PeogeotData implements Serializable{
	
	private String vehCom;
	private String cat;
	private String lcdv;
	public String getVehCom() {
		return vehCom;
	}
	public void setVehCom(String vehCom) {
		this.vehCom = vehCom;
	}
	public String getCat() {
		return cat;
	}
	public void setCat(String cat) {
		this.cat = cat;
	}
	public String getLcdv() {
		return lcdv;
	}
	public void setLcdv(String lcdv) {
		this.lcdv = lcdv;
	}
	
	

}
