package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Bean.Material;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class CommonMaterialCost {
	private File file;	//原始资料文件
	private File costFile;	//保存計算的結果文件
	private Sheet[] sheets;
	private WritableWorkbook workbookResult;
	private WritableSheet resultSheet;
	private Sheet productSheet; 
	private static String PRODUCT = "产量表";
	private static String COST = "实际物料消耗表";
	
	public CommonMaterialCost(File file) {
		this.file = file;
		costFile = new File(file.getParent()+"\\結果文件");
		costFile.mkdirs();
	}
	
	public void initCostData() throws BiffException, IOException, RowsExceededException, WriteException {
		
		String sn;
		String materialName;
		Float totalCost;
		Label lable;
		ArrayList<Material> standMaterial;
		Workbook workbook = Workbook.getWorkbook(file);
		sheets = workbook.getSheets();
		productSheet = workbook.getSheet(PRODUCT);
		Sheet costSheet = workbook.getSheet(COST);
		String[] titles = new String[15];
		titles[0] = "物料编码";
		titles[1] = "物料名称";
		titles[2] = "总金额";
		System.arraycopy(workbook.getSheetNames(), 0, titles, 3, workbook.getNumberOfSheets()-2);
		//初始化物料成本結果表格
		String filename = file.getName().substring(0, 6);
		File materialFile = new File(costFile.getAbsolutePath()+"\\"+filename+"-物料成本.xls");
		if(materialFile.exists()) {
			materialFile.delete();
		}
		materialFile.createNewFile();
		workbookResult = Workbook.createWorkbook(materialFile);
		if(workbookResult.getNumberOfSheets()==0) {
			resultSheet = workbookResult.createSheet("物料成本", 0);
		}else {
			resultSheet = workbookResult.getSheet(0);
			resultSheet.setName("物料成本");
		}
		//初始化列标题
		for(int j=0;j<titles.length;j++) {
			lable = new Label(j, 0, titles[j]);
			resultSheet.addCell(lable);
		}
		
		for(int i=1;i<costSheet.getRows();i++) {
			sn = costSheet.getCell(0, i).getContents();
			materialName = costSheet.getCell(1, i).getContents();
			totalCost = Float.valueOf(costSheet.getCell(5, i).getContents());
			standMaterial = checkMaterial(sheets,sn);
			float sum = 0 ;
			for (Material material : standMaterial) {
				sum += material.getNumber()*material.getProduct();
			}
			
			//写入数据
			int j = 0;
			lable = new Label(j++, i, sn);
			resultSheet.addCell(lable);
			lable = new Label(j++, i, materialName);
			resultSheet.addCell(lable);
			lable = new Label(j++, i, totalCost+"");
			resultSheet.addCell(lable);
			if(standMaterial.isEmpty()) {
				WritableCellFormat format = new WritableCellFormat();
				format.setBackground(Colour.RED);
				for(int k=0;k<sheets.length-2;k++) {
					lable = new Label(j++, i,"NA" ,format);
					resultSheet.addCell(lable);
				}			
			}else {
//				for (int m=0;m<sheets.length-2;m++) {
					String type;
//					if(m<standMaterial.size()) {
//						Material material = standMaterial.get(m);
//						type = material.getType();
//						float materialRatio = material.getNumber()*material.getProduct()/sum;
//						lable = new Label(j+getIndex(type), i, materialRatio*totalCost+"" );
//					}
				for (Material material : standMaterial) {
					type = material.getType();
					float materialRatio = material.getNumber()*material.getProduct()/sum;
					lable = new Label(j+getIndex(type), i, materialRatio*totalCost+"" );
					resultSheet.addCell(lable);
				}
								
			}	
		}
		
		workbookResult.write();
		workbookResult.close();
	}

	private int getIndex(String type) {
		for(int i=0;i<sheets.length;i++) {
			if(sheets[i].getName().equals(type)) {
				return i;
			}
		}
		return 0;
	}
	/**
	 * 查找表格中料号为sn的所有标准物料
	 * @param sheets
	 * @param sn
	 * @return
	 */
	private ArrayList<Material> checkMaterial(Sheet[] sheets,String sn) {
		ArrayList<Material> materialList = new ArrayList<>();
		Material material;
		Sheet materialSheet;
		for(int i=0;i<sheets.length-2;i++) {
			materialSheet = sheets[i];
			if((material=searchMaterial(materialSheet, sn))!=null) {
				materialList.add(material);
			}
		}
		
		return materialList;
	}
	
	/**
	 * 在sheet表格中搜索料号为sn的物料，找到返回一个Material，没找到返回null
	 * @param sheet
	 * @param sn
	 * @return 找到的material 或者null
	 */
	private Material searchMaterial(Sheet sheet,String sn) {
		Material material;
		for(int i=1;i<sheet.getRows();i++) {
			String content = sheet.getCell(0, i).getContents();
			if(content.equals(sn)) {
				material = new Material();
				material.setSn(sn);
				material.setName(sheet.getCell(1, i).getContents());
				material.setNumber(Float.valueOf(sheet.getCell(5, i).getContents()));
				System.out.println(sheet.getCell(5, i).getContents());
				material.setType(sheet.getName());
				material.setProduct(searchProduct(sheet));
				System.out.println(sn);
				System.out.println(material.getNumber());
				return material;
			}
		}		
		return null;
	}
	/**
	 * 查找指定sheet表中的产量
	 * @param productSheet
	 * @return
	 */
	private int searchProduct(Sheet sheet) {
		int product = 0;
		for(int i=1;i<productSheet.getRows();i++) {
			String name = productSheet.getCell(0, i).getContents();
			if(name.equals(sheet.getName())) {
				product = Integer.parseInt(productSheet.getCell(1, i).getContents());
			}
		}
		return product;
	}

}
