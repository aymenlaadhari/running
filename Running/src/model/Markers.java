package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Markers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private List<String> lat;
	private List<String> len;
	private String temps;
	private String calories;
	private String vitesse;
	
	public List<String> getLat() {
		return lat;
	}
	public void setLat(List<String> lat) {
		this.lat = lat;
	}
	public List<String> getLen() {
		return len;
	}
	public void setLen(List<String> len) {
		this.len = len;
	}
	public String getTemps() {
		return temps;
	}
	public void setTemps(String temps) {
		this.temps = temps;
	}
	public String getCalories() {
		return calories;
	}
	public void setCalories(String calories) {
		this.calories = calories;
	}
	public String getVitesse() {
		return vitesse;
	}
	public void setVitesse(String vitesse) {
		this.vitesse = vitesse;
	}
	
	

	

	
	
}
