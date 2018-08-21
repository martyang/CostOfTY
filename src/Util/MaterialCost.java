package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;
import Bean.Material;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class MaterialCost {
	private static String FAECES = "�����ȡ";
	private static String MOUTH = "��Һ��ȡ";
	private static String BLOOD = "ѪҺ��ȡ";
	private static String DNA = "������ȡ";
	private static String PRODUCT = "������";
	private static String COST = "ʵ�ʺ�����";
	private File file;	//ԭʼ�Y���ļ�
	private ArrayList<Material> faeces ;//�����ȡ
	private ArrayList<Material> mouth ;//��Һ��ȡ
	private ArrayList<Material> dna;//������ȡ
	private ArrayList<Material> blood;//ѪҺ��ȡ
	private int faeceProduct;
	private int mouthProduct;
	private int bloodProduct;
	private int dnaProduct;
	private int rnaProduct;
	private File costFile;	//����Ӌ��ĽY���ļ�
	private static Logger log = LogFactory.getGlobalLog();
	
	public MaterialCost(File file) {
		this.file = file;
		costFile = new File(file.getParent()+"\\�Y���ļ�");
		costFile.mkdirs();
		faeces = new ArrayList<>();
		mouth = new ArrayList<>();
		dna = new ArrayList<>();
		blood= new ArrayList<>();
	}
	/**
	 * ��Excel����ж�ȡ��׼����
	 * @throws BiffException
	 * @throws IOException
	 */
	public void initData() throws BiffException, IOException {
		ArrayList<Material> materList = faeces;
		Workbook workbook = Workbook.getWorkbook(file);
		
		String[] sheetNames = workbook.getSheetNames();
		Sheet sheet;
		for(int i=0;i<sheetNames.length;i++){
			String type = sheetNames[i];
			if(type.equals(FAECES)){
				materList = faeces;
				sheet = workbook.getSheet(type);
				readMaterial(sheet, materList);
			}else if(type.equals(MOUTH)){
				materList = mouth;
				sheet = workbook.getSheet(type);
				readMaterial(sheet, materList);
			}else if(type.equals(BLOOD)){
				materList = blood;
				sheet = workbook.getSheet(type);
				readMaterial(sheet, materList);
			}else if(type.equals(DNA)){
				materList = dna;
				sheet = workbook.getSheet(type);
				readMaterial(sheet, materList);
			}else if(type.equals(PRODUCT)){
				sheet = workbook.getSheet(type);
				System.out.println(""+sheet.getName());
				readMaterial(sheet, null);
			}else if(type.equals(COST)) {
				sheet = workbook.getSheet(type);
				try {
					allotCost(sheet);
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
	
	private void readMaterial(Sheet sheet,ArrayList<Material> list){
		
		if(sheet.getName().equals(PRODUCT)){	//��ȡ������
			for(int i=1;i<sheet.getRows();i++){
				
					String content = sheet.getCell(0, i).getContents();
					if(content.equals("DNA��ȡ")){
						content = sheet.getCell(1, i).getContents();
						if(content.equals("���")){
							faeceProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}else if(content.equals("����")){
							dnaProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}else if (content.equals("ѪҺ")){
							bloodProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}else if(content.equals("��Һ")){
							mouthProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}
					}else {
						content = sheet.getCell(1, i).getContents();
						if(content.equals("ѪҺ")){
							rnaProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
							log.info("RNA������"+rnaProduct);
						}
					}	
			}	
		}else{		//��ȡ��׼����
			//iΪ����
			for(int i=1;i<sheet.getRows();i++) {
				Material material = new Material();
				Cell cell = null;
				material.setType(sheet.getName());
				//jΪ��������ʼ��ȡsheet
				for(int j=0;j<sheet.getColumns();j++) {
					
					cell = sheet.getCell(j, i);//�÷�����ǰһ������Ϊ�У���һ������Ϊ��
					String content = cell.getContents();
					if(content!=null&&!"".equals(content)){
						//��ȡ����д�뵽bean				
						if(j==0) {
							material.setSn(content);	//��ȡ���к�
						}else if(j==2){
							material.setName(content);	//��������
						}else if(j==9) {
							material.setNumber(Float.parseFloat(content));	//����ʹ����
						}
					}
					
				}
				if(material.getSn()!=null) {
//					log.info("�Ϻţ�"+material.getSn()+"���ƣ�"+material.getName()+"ʹ������"+material.getNumber());
					list.add(material);
				}
				
			}
		}	
	}
	
	public void allotCost(Sheet sheet) throws IOException, WriteException {
		float faeceCost;
		float mouthCost;
		float bloodCost;
		float dnaCost;
		String sn;
		String name;
		String total;
		float cost;
		Label lable =null;
		WritableWorkbook workbook = null ;
		WritableSheet matrialSheet = null;
		String titles[] = {"���ϱ���","��������","�ܽ��",MOUTH,BLOOD,DNA,FAECES};
		
		//��ʼ�����ϱ��
		File materialFile = new File(costFile.getAbsolutePath()+"\\���ϳɱ�.xls");
		if(materialFile.exists()) {
			materialFile.delete();
		}
		materialFile.createNewFile();
		workbook = Workbook.createWorkbook(materialFile);
		if(workbook.getNumberOfSheets()==0) {
			matrialSheet = workbook.createSheet("���ϳɱ�", 0);
		}else {
			matrialSheet = workbook.getSheet(0);
			matrialSheet.setName("���ϳɱ�");
		}
		//��ʼ���б���
		for(int j=0;j<titles.length;j++) {
			lable = new Label(j, 0, titles[j]);
			matrialSheet.addCell(lable);
		}
		
		for(int i=1;i<sheet.getRows();i++) {
			sn = sheet.getCell(0, i).getContents();
			name = sheet.getCell(1, i).getContents();
			total = sheet.getCell(5, i).getContents();
			cost = Float.parseFloat(total);
			faeceCost = getFaeceRatio(sn)*cost;
			mouthCost = getMouthRatio(sn)*cost;
			bloodCost = getBloodRatio(sn)*cost;
			dnaCost = getDNARatio(sn)*cost;
			log.info(sn);
			
			lable = new Label(0, i, sn);
			matrialSheet.addCell(lable);
			lable = new Label(1, i, name);
			matrialSheet.addCell(lable);
			lable = new Label(2, i, total);
			matrialSheet.addCell(lable);
			lable = new Label(3, i, mouthCost+"");
			matrialSheet.addCell(lable);
			lable = new Label(4, i, bloodCost+"");
			matrialSheet.addCell(lable);
			lable = new Label(5, i, dnaCost+"");
			matrialSheet.addCell(lable);
			lable = new Label(6, i, faeceCost+"");
			matrialSheet.addCell(lable);
		}
		workbook.write();
		workbook.close();
	}
	/**
	 * ��ȡָ��sn���ϵĲ�������
	 * @param sn
	 * @return 
	 */
	public float getFaeceRatio(String sn){
		float ratio = 0;
		float faece = getFaecesNumber(sn)*faeceProduct;
		float sum = getFaecesNumber(sn)*faeceProduct+getMouthNumber(sn)*mouthProduct
				+getBloodNumber(sn)*bloodProduct+getDNANumber(sn)*dnaProduct;
		ratio = faece/sum;
		return ratio;
	}
	/**
	 * ��ȡָ��sn���ϵĲ�������
	 * @param sn
	 * @return 
	 */
	public float getMouthRatio(String sn){
		float ratio = 0;
		float mouth = getMouthNumber(sn)*mouthProduct;
		float sum = getFaecesNumber(sn)*faeceProduct+getMouthNumber(sn)*mouthProduct
				+getBloodNumber(sn)*bloodProduct+getDNANumber(sn)*dnaProduct;
		ratio = mouth/sum;
		return ratio;
	}
	/**
	 * ��ȡָ��sn���ϵĲ�������
	 * @param sn
	 * @return 
	 */
	public float getBloodRatio(String sn){
		float ratio = 0;
		float blood = getBloodNumber(sn)*bloodProduct;
		float sum = getFaecesNumber(sn)*faeceProduct+getMouthNumber(sn)*mouthProduct
				+getBloodNumber(sn)*bloodProduct+getDNANumber(sn)*dnaProduct;
		ratio = blood/sum;
		return ratio;
	}
	/**
	 * ��ȡָ��sn���ϵĲ�������
	 * @param sn
	 * @return 
	 */
	public float getDNARatio(String sn){
		float ratio = 0;
		float dna = getDNANumber(sn)*dnaProduct;
		float sum = getFaecesNumber(sn)*faeceProduct+getMouthNumber(sn)*mouthProduct
				+getBloodNumber(sn)*bloodProduct+getDNANumber(sn)*dnaProduct;
		ratio = dna/sum;
		return ratio;
	}
	/**
	 * ��ȡ�����ȡ���ƶ���������ϵ�ʹ����
	 * @param sn ���ϱ���
	 * @return
	 */
	public float getFaecesNumber(String sn) {
		float number = 0;
		for(int i=0;i<faeces.size();i++) {
			if(faeces.get(i).getSn().equals(sn)) {
				number = faeces.get(i).getNumber();
			}
		}
		return number;
	}
	/**
	 * ��ȡ��ǻ��ȡ���ƶ���������ϵ�ʹ����
	 * @param sn ���ϱ���
	 * @return
	 */
	public float getMouthNumber(String sn) {
		float number = 0;
		for(int i=0;i<mouth.size();i++) {
			if(mouth.get(i).getSn().equals(sn)) {
				number = mouth.get(i).getNumber();
			}
		}
		return number;
	}
	/**
	 * ��ȡ������ȡ���ƶ���������ϵ�ʹ����
	 * @param sn ���ϱ���
	 * @return
	 */
	public float getDNANumber(String sn) {
		float number = 0;
		for(int i=0;i<dna.size();i++) {
			if(dna.get(i).getSn().equals(sn)) {
				number = dna.get(i).getNumber();
			}
		}
		return number;
	}
	/**
	 * ��ȡѪҺ��ȡ���ƶ���������ϵ�ʹ����
	 * @param sn ���ϱ���
	 * @return
	 */
	public float getBloodNumber(String sn) {
		float number = 0;
		for(int i=0;i<blood.size();i++) {
			if(blood.get(i).getSn().equals(sn)) {
				number = blood.get(i).getNumber();
			}
		}
		return number;
	}
}
