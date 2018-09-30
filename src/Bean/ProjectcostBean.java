package Bean;

public class ProjectcostBean {
	/*产品类型*/
	private String productType;
	/*产品名称*/
	private String productName;
	/*产品使用的技术类型*/
	private String operateType;
	/*产品使用的技术名称*/
	private String operateName;
	/*产品产量*/
	private int productOutput;
	private float materialCost; //直接材料
	private float manalCoat;	//直接人工
	private float depreciatCost;	//直接折旧
	private float productCost;	//直接生产
	private float inMaterialCost;	//间接材料
	private float inManalCost;	//间接人工
	private float inDepreciatCost;	//间接折旧
	private float inProductCost;	//间接生产
	
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public int getProductOutput() {
		return productOutput;
	}
	public void setProductOutput(int productOutput) {
		this.productOutput = productOutput;
	}
	public float getMaterialCost() {
		return materialCost;
	}
	public void setMaterialCost(float materialCost) {
		this.materialCost = materialCost;
	}
	public float getManalCoat() {
		return manalCoat;
	}
	public void setManalCoat(float manalCoat) {
		this.manalCoat = manalCoat;
	}
	public float getDepreciatCost() {
		return depreciatCost;
	}
	public void setDepreciatCost(float depreciatCost) {
		this.depreciatCost = depreciatCost;
	}
	public float getProductCost() {
		return productCost;
	}
	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}
	public float getInMaterialCost() {
		return inMaterialCost;
	}
	public void setInMaterialCost(float inMaterialCost) {
		this.inMaterialCost = inMaterialCost;
	}
	public float getInManalCost() {
		return inManalCost;
	}
	public void setInManalCost(float inManalCost) {
		this.inManalCost = inManalCost;
	}
	public float getInDepreciatCost() {
		return inDepreciatCost;
	}
	public void setInDepreciatCost(float inDepreciatCost) {
		this.inDepreciatCost = inDepreciatCost;
	}
	public float getInProductCost() {
		return inProductCost;
	}
	public void setInProductCost(float inProductCost) {
		this.inProductCost = inProductCost;
	}
	
}
