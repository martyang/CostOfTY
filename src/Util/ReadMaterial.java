package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Bean.Material;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadMaterial {
	private File file;
	private ArrayList<Material> faeces ;//粪便提取
	private ArrayList<Material> mouth ;//口腔提取
	private ArrayList<Material> plasma;//血浆提取
	private ArrayList<Material> blood;//血液提取
	
	public ReadMaterial(File file) {
		this.file = file;
		faeces = new ArrayList<>();
		mouth = new ArrayList<>();
		plasma= new ArrayList<>();
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
		Sheet sheet = workbook.getSheet(0);
		String type = null;
		
		for(int i=0;i<sheet.getRows();i++) {
			Material material = new Material();
			Cell cell = null;
			//开始读取sheet
			for(int j=0;j<sheet.getColumns();j++) {
				
				cell = sheet.getCell(j, i);//该方法的前一个参数为列，后一个参数为行
				String content = cell.getContents();
				
				if(content.contains("方法")) {
					type = content.split("：")[1];
					if(type.equals("唾液DNA提取")) {
						materList = mouth;
					}else if(type.contains("血浆DNA提取")) {
						materList = plasma;
					}else {
						materList = blood;
					}
					System.out.println("type:"+type);
					break;
				}else if(content.contentEquals("物料编码")) {
					break;
				}
				//读取内容写入到bean
				material.setType(type);
				if(j==0) {
					material.setSn(content);
				}else if(j==1){
					material.setName(content);
				}else if(j==2) {
					material.setNumber(Float.parseFloat(content));
				}
			}
			if(material.getSn()!=null) {
				materList.add(material);
			}
			
		}
			
	}
	/**
	 * 获取粪便提取使用量
	 * @param sn
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
	 * 获取口腔提取使用量
	 * @param sn
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
	 * 获取血浆提取使用量
	 * @param sn
	 * @return
	 */
	public float getPlaNumber(String sn) {
		float number = 0;
		for(int i=0;i<plasma.size();i++) {
			if(plasma.get(i).getSn().equals(sn)) {
				number = plasma.get(i).getNumber();
			}
		}
		return number;
	}
	/**
	 * 获取血液提取使用量
	 * @param sn
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
