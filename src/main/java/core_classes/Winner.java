package core_classes;

public class Winner {

	String name, surname, gender, medal;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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

	public Winner(String name, String surname, String gender, String medal) {
		super();
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.medal = medal;
	}
	
}
