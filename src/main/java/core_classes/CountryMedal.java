package core_classes;

public class CountryMedal {
	private String noc, country;
	private int gold, silver, bronze, total;
	
	public CountryMedal (String country_noc, int gold, int silver, int bronze) {
		this.noc = country_noc.split("-")[0];
		this.country = country_noc.split("-")[1];
		this.gold = gold;
		this.silver = silver;
		this.bronze = bronze;
		this.total = gold + silver + bronze;
	}

	public String getNoc() {
		return noc;
	}

	public void setNoc(String noc) {
		this.noc = noc;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getSilver() {
		return silver;
	}

	public void setSilver(int silver) {
		this.silver = silver;
	}

	public int getBronze() {
		return bronze;
	}

	public void setBronze(int bronze) {
		this.bronze = bronze;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
