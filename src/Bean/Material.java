package Bean;

public class Material {
	String sn;//物料号
	String name;//物料名称
	String type;//检验类型
	float number;//使用量
	float cost;//消耗金额
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getNumber() {
		return number;
	}
	public void setNumber(float number) {
		this.number = number;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	

}
