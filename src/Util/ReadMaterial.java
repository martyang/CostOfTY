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
	private static String FAECES = "粪便提取";
	private static String MOUTH = "唾液提取";
	private static String BLOOD = "血液提取";
	private static String DAN = "核酸提取";
	private static String PRODUCT = "产量表";
	private File file;
	private ArrayList<Material> faeces ;//粪便提取
	private ArrayList<Material> mouth ;//唾液提取
	private ArrayList<Material> dna;//核酸提取
	private ArrayList<Material> blood;//血液提取
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
	 * 从Excel表格中读取标准物料
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
					if(content.equals("DNA提取")){
						content = sheet.getCell(j+1, i).getContents();
						if(content.equals("粪便")){
							faeceProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}else if(content.equals("核酸")){
							dnaProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}else if (content.equals("血液")){
							bloodProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}else if(content.equals("唾液")){
							mouthProduct = Integer.valueOf(sheet.getCell(j+2, i).getContents());
						}
					}else {
						if(content.equals("血液")){
							rnaProduct = Integer.valueOf(sheet.getCell(j+1, i).getContents());
						}
					}
				}
				
			}	
		}else{
			//i为行数
			for(int i=1;i<sheet.getRows();i++) {
				Material material = new Material();
				Cell cell = null;
				material.setType(sheet.getName());
				//j为类数，开始读取sheet
				for(int j=0;j<sheet.getColumns();j++) {
					
					cell = sheet.getCell(j, i);//该方法的前一个参数为列，后一个参数为行
					String content = cell.getContents();
					if(content!=null&&!"".equals(content)){
						//读取内容写入到bean				
						if(j==0) {
							material.setSn(content);	//读取序列号
						}else if(j==2){
							material.setName(content);	//物料名称
						}else if(j==7) {
							material.setNumber(Float.parseFloat(content));	//物料使用量
						}
					}
					
				}
				if(material.getSn()!=null) {
//					System.out.println(material.getName());
					list.add(material);
				}
				
			}
		}
		System.out.println("产量"+faeceProduct+";"+dnaProduct+";"+bloodProduct+";"+mouthProduct);
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
	 * 获取粪便提取中制定编码的物料的使用量
	 * @param sn 物料编码
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
	 * 获取口腔提取中制定编码的物料的使用量
	 * @param sn 物料编码
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
	 * 获取核酸提取中制定编码的物料的使用量
	 * @param sn 物料编码
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
	 * 获取血液提取中制定编码的物料的使用量
	 * @param sn 物料编码
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
