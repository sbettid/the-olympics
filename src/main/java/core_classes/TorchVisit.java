package core_classes;

public class TorchVisit {

	private String noc, cityName;
	int order;
	/**
	 * @param noc
	 * @param cityName
	 * @param order
	 */
	public TorchVisit(String noc, String cityName, int order) {
		super();
		this.noc = noc;
		this.cityName = cityName;
		this.order = order;
		
	}
	
	
	/**
	 * @return the noc
	 */
	public String getNoc() {
		return noc;
	}
	/**
	 * @param noc the noc to set
	 */
	public void setNoc(String noc) {
		this.noc = noc;
	}
	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	} 
	
	
}
