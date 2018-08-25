package Bean;

public class OperateBean {
	private String type;
	private String name;
	private float materialCost; //ֱ�Ӳ���
	private float manalCoat;	//ֱ���˹�
	private float depreciatCost;	//ֱ���۾�
	private float productCost;	//ֱ������
	private float inMaterialCost;	//��Ӳ���
	private float inManalCost;	//����˹�
	private float inDepreciatCost;	//����۾�
	private float inProductCost;	//�������
	private float totalCost;	//�ܷ���
	private float manhour;	//��׼��ʱ
	private int product;	//����
	
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
	 * �������ϳɱ�
	 * @return
	 */
	public float getSingleMaterialCost() {
		return materialCost/product;
	}
	/**
	 * �����˹��ɱ�
	 * @return
	 */
	public float getSingleManCost() {
		return manalCoat/product;
	}
	/**
	 * �����۾ɳɱ�
	 * @return
	 */
	public float getSingleDepreciatCost() {
		return depreciatCost/product;
	}
	/**
	 * �������ɳɱ�
	 * @return
	 */
	public float getSingleProductCost() {
		return productCost/product;
	}
	/**
	 * ������Ӳ��ϳɱ�
	 * @return
	 */
	public float getSingleInMaterialCost() {
		return inMaterialCost/product;
	}
	/**
	 * ��������˹��ɱ�
	 * @return
	 */
	public float getSingleInManCost() {
		return inManalCost/product;
	}
	/**
	 * ��������۾ɳɱ�
	 * @return
	 */
	public float getSingleInDepreciatCost() {
		return inDepreciatCost/product;
	}
	/**
	 * ������������ɱ�
	 * @return
	 */
	public float getSingleInProductCost() {
		return inProductCost/product;
	}
	
	public String getString() {
		return "ֱ�Ӳ���"+materialCost+"ֱ���˹�"+manalCoat+"ֱ���۾�"+depreciatCost+"��������"+productCost+"��Ӳ���"+inMaterialCost
				+"����˹�"+inManalCost+"����۾�"+inDepreciatCost+"�������"+inProductCost;
	}
}
