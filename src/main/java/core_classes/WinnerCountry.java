package core_classes;

public class WinnerCountry {

	String country, gender, medal;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMedal() {
		return medal;
	}

	public void setMedal(String medal) {
		this.medal = medal;
	}

	public WinnerCountry(String country, String gender, String medal) {
		super();
		this.country = country;
		this.gender = gender;
		this.medal = medal;
	}
	
	
}
