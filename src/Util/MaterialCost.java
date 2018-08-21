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
	private static String FAECES = "粪便提取";
	private static String MOUTH = "唾液提取";
	private static String BLOOD = "血液提取";
	private static String DNA = "核酸提取";
	private static String PRODUCT = "产量表";
	private static String COST = "实际耗用量";
	private File file;	//原始資料文件
	private ArrayList<Material> faeces ;//粪便提取
	private ArrayList<Material> mouth ;//唾液提取
	private ArrayList<Material> dna;//核酸提取
	private ArrayList<Material> blood;//血液提取
	private int faeceProduct;
	private int mouthProduct;
	private int bloodProduct;
	private int dnaProduct;
	private int rnaProduct;
	private File costFile;	//保存計算的結果文件
	private static Logger log = LogFactory.getGlobalLog();
	
	public MaterialCost(File file) {
		this.file = file;
		costFile = new File(file.getParent()+"\\結果文件");
		costFile.mkdirs();
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
		
		if(sheet.getName().equals(PRODUCT)){	//读取产量表
			for(int i=1;i<sheet.getRows();i++){
				
					String content = sheet.getCell(0, i).getContents();
					if(content.equals("DNA提取")){
						content = sheet.getCell(1, i).getContents();
						if(content.equals("粪便")){
							faeceProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}else if(content.equals("核酸")){
							dnaProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}else if (content.equals("血液")){
							bloodProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}else if(content.equals("唾液")){
							mouthProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
						}
					}else {
						content = sheet.getCell(1, i).getContents();
						if(content.equals("血液")){
							rnaProduct = Integer.valueOf(sheet.getCell(2, i).getContents());
							log.info("RNA产量："+rnaProduct);
						}
					}	
			}	
		}else{		//读取标准物料
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
						}else if(j==9) {
							material.setNumber(Float.parseFloat(content));	//物料使用量
						}
					}
					
				}
				if(material.getSn()!=null) {
//					log.info("料号："+material.getSn()+"名称："+material.getName()+"使用量："+material.getNumber());
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
		String titles[] = {"物料编码","物料名称","总金额",MOUTH,BLOOD,DNA,FAECES};
		
		//初始化物料表格
		File materialFile = new File(costFile.getAbsolutePath()+"\\物料成本.xls");
		if(materialFile.exists()) {
			materialFile.delete();
		}
		materialFile.createNewFile();
		workbook = Workbook.createWorkbook(materialFile);
		if(workbook.getNumberOfSheets()==0) {
			matrialSheet = workbook.createSheet("物料成本", 0);
		}else {
			matrialSheet = workbook.getSheet(0);
			matrialSheet.setName("物料成本");
		}
		//初始化列标题
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
	 * 获取指定sn物料的产量比例
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
	 * 获取指定sn物料的产量比例
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
	 * 获取指定sn物料的产量比例
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
	 * 获取指定sn物料的产量比例
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
