package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Bean.OperateBean;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class CostAllot {
	private File file;	//原始数据文件
	private File resultFolder;	//结果目录
	private Workbook workbook;	//原始数据的工作表
	private ArrayList<OperateBean> opetateList;
	private File result;
	
	public CostAllot(File file){
		this.file = file;
		resultFolder = new File(file.getParent()+"\\結果文件");
		resultFolder.mkdirs();
	}
	
	public void initData() throws BiffException, IOException {
		opetateList = new ArrayList<>();
		workbook = Workbook.getWorkbook(file);
		Sheet sheet = workbook.getSheet(0);
		String content;
		String type = sheet.getName();
		float sum = 0;
		float ratio = 0;
		
		for(int i=0;i<sheet.getRows();i++) {
			content = sheet.getCell(0, i).getContents();
			//初始化全部的操作类
			if(content.equals("人工成本")) {
				Cell[] cells = sheet.getRow(i);
				for (Cell cell : cells) {
					if(!cell.getContents().equals(content)) {
						OperateBean operate = new OperateBean();
						operate.setType(type);
						operate.setName(cell.getContents());
						opetateList.add(operate);
					}
					
				}
				System.out.println("操作方法数："+opetateList.size());
			}else if(content.equals("标准工时")) {
				Cell[] cells = sheet.getRow(i);
				int j = 0;
				for (Cell cell : cells) {
					if(!cell.getContents().equals(content)) {
						content = cell.getContents();
						opetateList.get(j++).setManhour(Float.parseFloat(content));
					}
					
				}
			}else if(content.equals("产量")) {
				Cell[] cells = sheet.getRow(i);
				int j = 0;
				for (Cell cell : cells) {
					if(!cell.getContents().equals(content)) {
						content = cell.getContents();
						opetateList.get(j++).setProduct(Integer.parseInt(content));
					}
					
				}
			}else if(content.equals("直接材料")) {
				int number = sheet.getColumns();
				Cell[] cells = sheet.getRow(i);
				System.out.println("列数："+number);
//				float total = Float.parseFloat(cells[1].getContents());
				int j=0;
				OperateBean operate;
				for(int m=2;m<number;m++) {
					content = cells[m].getContents();
					operate = opetateList.get(j++);
					operate.setMaterialCost(Float.parseFloat(content));
				}	
			}else if(content.equals("直接人工")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				float sumManal = 0 ;	//总直接人工
				for (OperateBean operateBean : opetateList) {
					sumManal += operateBean.getManhour()*operateBean.getProduct(); 
				}
				for (OperateBean operateBean : opetateList) {
					float manRatio = operateBean.getManhour()*operateBean.getProduct()/sumManal;
					operateBean.setManalCoat(manRatio*total);
				}
			}else if(content.equals("直接折旧")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					sum += operateBean.getMaterialCost()+operateBean.getManalCoat();
				}
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setDepreciatCost(ratio*total);
				}
			}else if(content.equals("其他制造费用")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setProductCost(ratio*total);
				}
			}else if(content.equals("间接材料")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInMaterialCost(ratio*total);
				}
			}else if(content.equals("间接工资")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInManalCost(ratio*total);
				}
			}else if(content.equals("间接折旧")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInDepreciatCost(total*ratio);
				}
			}else if(content.equals("其他间接制造费用")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInProductCost(total*ratio);
				}
			}else if(content.equals("合计")) {
				float total = 0;
				for (OperateBean operate : opetateList) {
					total = operate.getMaterialCost()+operate.getManalCoat()+operate.getDepreciatCost()+operate.getProductCost()
					+operate.getInMaterialCost()+operate.getInManalCost()+operate.getInDepreciatCost()+operate.getInProductCost();
					operate.setTotalCost(total);
				}
			}

		}
//		
//		for (OperateBean operateBean : opetateList) {
//			System.out.println(operateBean.getString());
//			System.out.println(operateBean.getSingleDepreciatCost());
//		}		
	}
	
	public void outData() throws IOException, RowsExceededException, WriteException {
		String resultname = resultFolder.getAbsolutePath()+"\\"+file.getName()+"结果.xls";
		WritableSheet resultSheet;
		WritableSheet allotSheet;
		result = new File(resultname);
		if(result.exists()) {
			result.delete();
		}
		result.createNewFile();
		//创建sheet表格
		WritableWorkbook workbookResult = Workbook.createWorkbook(result);
		if(workbookResult.getNumberOfSheets()==0) {
			resultSheet = workbookResult.createSheet("计算结果", 0);
			allotSheet = workbookResult.createSheet("分配结果", 1);
		}else {
			resultSheet = workbookResult.getSheet(0);
			resultSheet.setName("计算结果");
			resultSheet = workbookResult.getSheet(1);
			resultSheet.setName("分配结果");
		}
		
		Label label ;
		//输出总体分配结果,i为列
		for (int i=0;i<=opetateList.size();i++) {
			if(i==0) 
			{
				label = new Label(i, 0, "类型");
				resultSheet.addCell(label);
				label = new Label(i, 1, "直接材料");
				resultSheet.addCell(label);
				label = new Label(i, 2, "直接人工");
				resultSheet.addCell(label);
				label = new Label(i, 3, "直接折旧");
				resultSheet.addCell(label);
				label = new Label(i, 4, "其他制造费用");
				resultSheet.addCell(label);
				label = new Label(i, 5, "间接材料");
				resultSheet.addCell(label);
				label = new Label(i, 6, "间接工资");
				resultSheet.addCell(label);
				label = new Label(i, 7, "间接折旧");
				resultSheet.addCell(label);
				label = new Label(i, 8, "其他间接制造费用");
				resultSheet.addCell(label);
			}else {
				label = new Label(i, 0, opetateList.get(i-1).getName());
				resultSheet.addCell(label);
				label = new Label(i, 1, ""+opetateList.get(i-1).getMaterialCost());
				resultSheet.addCell(label);
				label = new Label(i, 2, ""+opetateList.get(i-1).getManalCoat());
				resultSheet.addCell(label);
				label = new Label(i, 3, ""+opetateList.get(i-1).getDepreciatCost());
				resultSheet.addCell(label);
				label = new Label(i, 4, ""+opetateList.get(i-1).getProductCost());
				resultSheet.addCell(label);
				label = new Label(i, 5, ""+opetateList.get(i-1).getInMaterialCost());
				resultSheet.addCell(label);
				label = new Label(i, 6, ""+opetateList.get(i-1).getInManalCost());
				resultSheet.addCell(label);
				label = new Label(i, 7, ""+opetateList.get(i-1).getInDepreciatCost());
				resultSheet.addCell(label);
				label = new Label(i, 8, ""+opetateList.get(i-1).getInProductCost());
				resultSheet.addCell(label);
			}
			
		}
		int row = resultSheet.getRows();
		System.out.println("行数"+row);
		//输出单列分配结果,j为行数
		for(int j=0;j<=opetateList.size();j++) {
			if(j==0) 
			{
				label = new Label(0, row+j, "类型");
				resultSheet.addCell(label);
				label = new Label(1, row+j, "直接材料");
				resultSheet.addCell(label);
				label = new Label(2, row+j, "直接人工");
				resultSheet.addCell(label);
				label = new Label(3, row+j, "直接折旧");
				resultSheet.addCell(label);
				label = new Label(4, row+j, "其他制造费用");
				resultSheet.addCell(label);
				label = new Label(5, row+j, "间接材料");
				resultSheet.addCell(label);
				label = new Label(6, row+j, "间接工资");
				resultSheet.addCell(label);
				label = new Label(7, row+j, "间接折旧");
				resultSheet.addCell(label);
				label = new Label(8, row+j, "其他间接制造费用");
				resultSheet.addCell(label);
			}else {
				label = new Label(0, row+j, opetateList.get(j-1).getName());
				resultSheet.addCell(label);
				label = new Label(1, row+j, ""+opetateList.get(j-1).getSingleMaterialCost());
				resultSheet.addCell(label);
				label = new Label(2, row+j, ""+opetateList.get(j-1).getSingleManCost());
				resultSheet.addCell(label);
				label = new Label(3, row+j, ""+opetateList.get(j-1).getSingleDepreciatCost());
				resultSheet.addCell(label);
				label = new Label(4, row+j, ""+opetateList.get(j-1).getSingleProductCost());
				resultSheet.addCell(label);
				label = new Label(5, row+j, ""+opetateList.get(j-1).getSingleInMaterialCost());
				resultSheet.addCell(label);
				label = new Label(6, row+j, ""+opetateList.get(j-1).getSingleInManCost());
				resultSheet.addCell(label);
				label = new Label(7, row+j, ""+opetateList.get(j-1).getSingleInDepreciatCost());
				resultSheet.addCell(label);
				label = new Label(8, row+j, ""+opetateList.get(j-1).getSingleInProductCost());
				resultSheet.addCell(label);
			}
		}
		
		workbookResult.write();
		workbookResult.close();
	}
	
	
}
