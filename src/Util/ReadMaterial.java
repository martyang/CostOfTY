package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Bean.Material;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadMaterial {
	private static String FAECES = "�����ȡ";
	private static String MOUTH = "��Һ��ȡ";
	private static String BLOOD = "ѪҺ��ȡ";
	private static String DAN = "������ȡ";
	private static String PRODUCT = "������";
	private File file;
	private ArrayList<Material> faeces ;//�����ȡ
	private ArrayList<Material> mouth ;//��Һ��ȡ
	private ArrayList<Material> dna;//������ȡ
	private ArrayList<Material> blood;//ѪҺ��ȡ
	private int faeceProduct;
	private int mouthProduct;
	private int bloodProduct;
	private int dnaProduct;
	private int rnaProduct;
	
	public ReadMaterial(File file) {
		this.file = file;
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
			}else if(type.equals(DAN)){
				materList = dna;
				sheet = workbook.getSheet(type);
				readMaterial(sheet, materList);
			}else if(type.equals(PRODUCT)){
				sheet = workbook.getSheet(type);
				System.out.println(""+sheet.getName());
				readMaterial(sheet, null);
			}
		}		
	}
	
	private void readMaterial(Sheet sheet,ArrayList<Material> list){
		
		if(sheet.getName().equals(PRODUCT)){
			for(int i=1;i<sheet.getRows();i++){
				for(int j=0;j<sheet.getColumns();j++){
					String content = sheet.getCell(j, i).getContents();
					if(content.equals("DNA��ȡ")){
						content = sheet.getCell(j+1, i).getContents();
						if(content.equals("���")){
							faeceProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}else if(content.equals("����")){
							dnaProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}else if (content.equals("ѪҺ")){
							bloodProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}else if(content.equals("��Һ")){
							mouthProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}
					}else {
						if(content.equals("ѪҺ")){
							rnaProduct = Integer.valueOf(sheet.getCell(j+1, i).getContents());
						}
					}
				}
				
			}	
		}else{
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
						}else if(j==7) {
							material.setNumber(Float.parseFloat(content));	//����ʹ����
						}
					}
					
				}
				if(material.getSn()!=null) {
//					System.out.println(material.getName());
					list.add(material);
				}
				
			}
		}
		System.out.println("����"+faeceProduct+";"+dnaProduct+";"+bloodProduct+";"+mouthProduct);
	}
	
	public float getFaeceRatio(String sn){
		float ratio = 0;
		float faece = getFaecesNumber(sn)*faeceProduct;
		float sum = getFaecesNumber(sn)*faeceProduct+getMouthNumber(sn)*mouthProduct
				+getBloodNumber(sn)*bloodProduct+getDNANumber(sn)*dnaProduct;
		ratio = faece/sum;
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
