package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Bean.OperateBean;
import Bean.ProjectcostBean;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import sql.SQLUtil;

/**
 * 将全部的成本分配到各种方法中，
 * @author min.yang
 *
 */
public class CostAllot {
	private File sourceFile;
	private File resultFolder;	//结果目录
	private Workbook workbook;	//原始数据的工作表
	private ArrayList<OperateBean> opetateList;
	private File result;
	
	public CostAllot(File file){
		this.sourceFile = file;
		resultFolder = new File(file.getParent()+"\\結果文件");
		resultFolder.mkdirs();
	}
	
	public void initData() throws BiffException, IOException {
		
		opetateList = new ArrayList<>();
		workbook = Workbook.getWorkbook(sourceFile);
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
					content = cell.getContents();
					if(!cell.getContents().equals("人工成本")&&!content.equals(null)&&!"".equals(content)) {
						OperateBean operate = new OperateBean();
						operate.setType(type);	//设置文件来源
						operate.setName(cell.getContents());	//设置操作名称
						opetateList.add(operate);
					}
					
				}
				System.out.println("操作方法数："+opetateList.size());
			}else if(content.equals("标准工时")) {
				Cell[] cells = sheet.getRow(i);
				int j = 0;
				for (Cell cell : cells) {
					content = cell.getContents();
					if(!cell.getContents().equals("标准工时")&&!content.equals(null)&&!"".equals(content)) {
						content = cell.getContents();
						opetateList.get(j++).setManhour(Float.parseFloat(content));	//去读取人工工时				
					}
					
				}
			}else if(content.equals("产量")) {
				Cell[] cells = sheet.getRow(i);
				int j = 0;
				for (Cell cell : cells) {
					content = cell.getContents();
					if(!cell.getContents().equals("产量")&&!content.equals(null)&&!"".equals(content)) {
						content = cell.getContents();
						opetateList.get(j++).setProduct(Integer.parseInt(content));	//设置产量
					}
					
				}
			}else if(content.equals("直接材料")) {
				
				Cell[] cells = sheet.getRow(i);
				int number = cells.length;
//				System.out.println("列数："+number+"cell数"+cells.length);
//				float total = Float.parseFloat(cells[1].getContents());
				int j=0;
				OperateBean operate;
				for(int m=2;m<number;m++) {
					content = cells[m].getContents();
					if(!content.equals(null)&&!"".equals(content)) {
						operate = opetateList.get(j++);
						operate.setMaterialCost(Float.parseFloat(content));//设置直接材料消耗
					}
					
				}	
			}else if(content.equals("直接人工")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				float sumManal = 0 ;	//总直接人工
				for (OperateBean operateBean : opetateList) {
					sumManal += operateBean.getManhour()*operateBean.getProduct(); 
				}
				for (OperateBean operateBean : opetateList) {
					//人工分配比例
					float manRatio = operateBean.getManhour()*operateBean.getProduct()/sumManal;
//					System.out.println(operateBean.getManhour());
					operateBean.setManalCoat(manRatio*total);//人工工时分配
//					System.out.println(sumManal);
				}
			}else if(content.equals("直接折旧")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					//直接人工和直接材料金额的总数，其他费用按此计算分配比例
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
	
	/**
	 * 输出计算结果
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void outData() throws IOException, RowsExceededException, WriteException {
		String resultname = resultFolder.getAbsolutePath()+"\\"+sourceFile.getName()+"结果.xls";
		WritableSheet resultSheet;
		WritableSheet allotSheet;
		result = new File(resultname);
		if(result.exists()) {
			result.delete();
		}
		result.createNewFile();
		//创建输出结果sheet表格
		WritableWorkbook workbookResult = Workbook.createWorkbook(result);
		if(workbookResult.getNumberOfSheets()==0) {
			resultSheet = workbookResult.createSheet("分配率结果", 1);
			allotSheet = workbookResult.createSheet(opetateList.get(0).getType(), 0);
		}else {
			resultSheet = workbookResult.getSheet(0);
			resultSheet.setName("计算结果");
			allotSheet = workbookResult.getSheet(1);
			allotSheet.setName("分配结果");
		}
		
		Label label ;
		//输出总体分配结果,i为列
		for (int i=0;i<=opetateList.size();i++) {
			//输出首行标题
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
		
		//输出项目分类的计算结果
		Sheet sheet = workbook.getSheet(1);
		System.err.println(sheet.getName()+sheet.getRows());
		for(int k=0;k<sheet.getRows();k++) {
			if(k==0)
			{
				label = new Label(0, k, "类型");
				allotSheet.addCell(label);
				label = new Label(1,k,"项目");
				allotSheet.addCell(label);
				label = new Label(2,k,"任务类型");
				allotSheet.addCell(label);
				label = new Label(3,k,"样本类型");
				allotSheet.addCell(label);
				label = new Label(4,k,"产量");
				allotSheet.addCell(label);
				label = new Label(5, k, "直接材料");
				allotSheet.addCell(label);
				label = new Label(6, k, "直接人工");
				allotSheet.addCell(label);
				label = new Label(7, k, "直接折旧");
				allotSheet.addCell(label);
				label = new Label(8, k, "其他制造费用");
				allotSheet.addCell(label);
				label = new Label(9, k, "间接材料");
				allotSheet.addCell(label);
				label = new Label(10, k, "间接工资");
				allotSheet.addCell(label);
				label = new Label(11, k, "间接折旧");
				allotSheet.addCell(label);
				label = new Label(12, k, "其他间接制造费用");
				allotSheet.addCell(label);
				
			}else {
				ProjectcostBean projectcostBean = new ProjectcostBean();				
				Cell cell = sheet.getCell(0, k);
				projectcostBean.setProductType(cell.getContents());
				label = new Label(0, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(1, k);
				String content = cell.getContents();
				projectcostBean.setProductName(content);
				if(content==""||content==null) {
					break;
				}
//				SQLUtil.addName(content);
				label = new Label(1, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(2, k);
				content = cell.getContents();
				projectcostBean.setOperateType(content);
				label = new Label(2, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(3, k);
				String name = cell.getContents();	//提取方式的名称，用来区分不同的样本类型
				projectcostBean.setOperateName(name);
				label = new Label(3, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(4, k);
				content = cell.getContents();
				label = new Label(4, k, cell.getContents());
				allotSheet.addCell(label);
				
				int production = 0;
				if(!content.equals("")) {
					production = Integer.parseInt(content);
					projectcostBean.setProductOutput(production);
				}
				for (OperateBean operateBean : opetateList) {
					if(operateBean.getName().equals(name)) {
						projectcostBean.setSourceFile(operateBean.getType());
						
						float material = production*operateBean.getSingleMaterialCost();
						projectcostBean.setMaterialCost(material);
						label = new Label(5, k, material+"");
						allotSheet.addCell(label);
						
						float man = production*operateBean.getSingleManCost();
						projectcostBean.setManalCoat(man);
						label = new Label(6, k, man+"");
						allotSheet.addCell(label);
						
						float deprecial = production*operateBean.getSingleDepreciatCost();
						projectcostBean.setDepreciatCost(deprecial);
						label = new Label(7, k, deprecial+"");
						allotSheet.addCell(label);
						
						float product = production*operateBean.getSingleProductCost();
						projectcostBean.setProductCost(product);
						label = new Label(8, k, product+"");
						allotSheet.addCell(label);
						
						float inmater = production*operateBean.getSingleInMaterialCost();
						projectcostBean.setInMaterialCost(inmater);
						label = new Label(9, k, inmater+"");
						allotSheet.addCell(label);
						
						float inman = production*operateBean.getSingleInManCost();
						projectcostBean.setInManalCost(inman);
						label = new Label(10, k, inman+"");
						allotSheet.addCell(label);
						
						float indepre = production*operateBean.getSingleInDepreciatCost();
						projectcostBean.setInDepreciatCost(indepre);
						label = new Label(11, k, indepre+"");
						allotSheet.addCell(label);
						
						float inproduct = production*operateBean.getSingleInProductCost();
						projectcostBean.setInProductCost(inproduct);
						label = new Label(12, k, inproduct+"");
						allotSheet.addCell(label);	
//						SQLUtil.add(projectcostBean);
					}
				}
			}
	
		}
		workbookResult.write();
		workbookResult.close();
	}
	
}
