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

public class ReadExcel {
	private File file;
	
	public ReadExcel(File file) {
		this.file = file;
	}

	public List<Material> getMaterData() throws BiffException, IOException {
		ArrayList<Material> materList = new ArrayList<>();
		Workbook workbook = Workbook.getWorkbook(file);
		Sheet sheet = workbook.getSheet(0);
		
		for(int i=0;i<sheet.getRows();i++) {
			Material material = new Material();
			Cell cell = null;
			String type = null;
			
			//开始读取sheet
			for(int j=0;j<sheet.getColumns();j++) {
				
				cell = sheet.getCell(j, i);//该方法的前一个参数为列，后一个参数为行
				String content = cell.getContents();
				
				if(content.contains("方法")) {
					type = content.split("：")[1];
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
		System.out.println("物料个数："+materList.size());
		return materList;	
	}
}
