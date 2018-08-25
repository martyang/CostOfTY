package Bean;

public class OperateBean {
	private String type;
	private String name;
	private float materialCost; //直接材料
	private float manalCoat;	//直接人工
	private float depreciatCost;	//直接折旧
	private float productCost;	//直接生产
	private float inMaterialCost;	//间接材料
	private float inManalCost;	//间接人工
	private float inDepreciatCost;	//间接折旧
	private float inProductCost;	//间接生产
	private float totalCost;	//总费用
	private float manhour;	//标准工时
	private int product;	//产量
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public float getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}
	public float getManhour() {
		return manhour;
	}
	public void setManhour(float manhour) {
		this.manhour = manhour;
	}
	public int getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	/**
	 * 单例材料成本
	 * @return
	 */
	public float getSingleMaterialCost() {
		return materialCost/product;
	}
	/**
	 * 单例人工成本
	 * @return
	 */
	public float getSingleManCost() {
		return manalCoat/product;
	}
	/**
	 * 单例折旧成本
	 * @return
	 */
	public float getSingleDepreciatCost() {
		return depreciatCost/product;
	}
	/**
	 * 单例生成成本
	 * @return
	 */
	public float getSingleProductCost() {
		return productCost/product;
	}
	/**
	 * 单例间接材料成本
	 * @return
	 */
	public float getSingleInMaterialCost() {
		return inMaterialCost/product;
	}
	/**
	 * 单例间接人工成本
	 * @return
	 */
	public float getSingleInManCost() {
		return inManalCost/product;
	}
	/**
	 * 单例间接折旧成本
	 * @return
	 */
	public float getSingleInDepreciatCost() {
		return inDepreciatCost/product;
	}
	/**
	 * 单例间接生产成本
	 * @return
	 */
	public float getSingleInProductCost() {
		return inProductCost/product;
	}
	
	public String getString() {
		return "直接材料"+materialCost+"直接人工"+manalCoat+"直接折旧"+depreciatCost+"其他生产"+productCost+"间接材料"+inMaterialCost
				+"间接人工"+inManalCost+"间接折旧"+inDepreciatCost+"间接生产"+inProductCost;
	}
}
