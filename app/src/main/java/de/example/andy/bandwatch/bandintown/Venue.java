package de.example.andy.bandwatch.bandintown;

public class Venue {

	private int id;

	private double latitude;

	private double longitude;

	private String name;

	private String country;

	private String city;

	private String region;

	public Venue() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Venue [latitude=" + latitude + ", longitude=" + longitude + ", name=" + name + ", region=" + region
				+ ", country=" + country + ", city=" + city + "]";
	}

}