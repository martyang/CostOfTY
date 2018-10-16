package Util;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class CollectProjectCost {
	ResultSet resultSet;
	private File inputFile;	//结果目录
	private WritableWorkbook workbook;	//结果工作表
	
	public CollectProjectCost(File folder) {
		this.inputFile = folder;
	}
	
	public void start() throws SQLException, IOException, RowsExceededException, WriteException {
		File folder = inputFile.getParentFile();
		File resultFolder = new File(inputFile.getParentFile()+"\\计算结果");
		resultFolder.mkdirs();
		File result = new File(resultFolder.getAbsolutePath(),"\\归集结果.xls");
		if(result.exists()) {
			result.delete();
		}
		result.createNewFile();
		workbook = Workbook.createWorkbook(result);
		WritableSheet sheet = workbook.createSheet("结果归集",0);
		initSheet(sheet);
		
		readData(folder);
		ResultSet names = SQLUtil.queryAllName();
		int row = 1;
		while (names.next()) {
			String name = names.getString(1);
//			System.out.println(name);
			resultSet = SQLUtil.query(name);
			while(resultSet.next()) {
				for(int i=1;i<15;i++) {
					Label lable = new Label(i-1, row, resultSet.getObject(i).toString());
					sheet.addCell(lable);
//					System.out.println(resultSet.getObject(i));
				}
				row++;			
			}
			row++;	
		}
		workbook.write();
		workbook.close();
	}

    private void readData(File folder) {
		File[] files = folder.listFiles();
		for (File file : files) {
			try {
				if(file.isDirectory()) {
					continue;
				}
				System.err.println(file.getName());
				Workbook workbook = Workbook.getWorkbook(file);
				Sheet sheet = workbook.getSheet(0);
				String sourceFile = sheet.getName();
				int rows = sheet.getRows();
				for(int i=1;i<rows;i++) {
					ProjectcostBean projectcostBean = new ProjectcostBean();
					projectcostBean.setSourceFile(sourceFile);
					int index = 0;
					String content;
/**					
					int cloum = sheet.getColumns();
					for(int j=0;j<cloum;j++) {
						if(j==0) {
							projectcostBean.setProductType(sheet.getCell(0, i).getContents());
						}else if(j==1) {
							content = sheet.getCell(1, i).getContents();
							if(content==null||"".equals(content)) {
								break;
							}else{
								projectcostBean.setProductName(content);
								SQLUtil.addName(content);
							}
						}else if(j==2) {
							projectcostBean.setOperateType(sheet.getCell(2, i).getContents());
						}else if(j==3) {
							projectcostBean.setOperateName(sheet.getCell(3, i).getContents());
						}else if(j==4) {
							content = sheet.getCell(4, i).getContents();
							projectcostBean.setProductOutput(Integer.parseInt(content));
						}else if(j==5) {
							content = sheet.getCell(5, i).getContents();
							projectcostBean.setMaterialCost(Float.parseFloat(content));
						}else if(j==6) {
							content = sheet.getCell(6, i).getContents();
							projectcostBean.setManalCoat(Float.parseFloat(content));
						}else if(j==7) {
							content = sheet.getCell(7, i).getContents();
							projectcostBean.setDepreciatCost(Float.parseFloat(content));
						}else if(j==8) {
							content = sheet.getCell(8, i).getContents();
							projectcostBean.setProductCost(Float.parseFloat(content));
						}else if(j==9) {
							content = sheet.getCell(9, i).getContents();
							projectcostBean.setInMaterialCost(Float.parseFloat(content));
						}else if(j==10) {
							content = sheet.getCell(10, i).getContents();
							projectcostBean.setInManalCost(Float.parseFloat(content));
						}else if(j==11) {
							content = sheet.getCell(11, i).getContents();
							projectcostBean.setInDepreciatCost(Float.parseFloat(content));
						}else if(j==12) {
							content = sheet.getCell(12, i).getContents();
							projectcostBean.setInProductCost(Float.parseFloat(content));
						}
						
					}
**/
					

					switch(index) {
						case 0:
							projectcostBean.setProductType(sheet.getCell(0, i).getContents());
						case 1:
							content = sheet.getCell(1, i).getContents();
							if(content==null||"".equals(content)) {
								break;
							}else{
								projectcostBean.setProductName(content);
								SQLUtil.addName(content);
							}
						case 2:
							projectcostBean.setOperateType(sheet.getCell(2, i).getContents());
						case 3:
							projectcostBean.setOperateName(sheet.getCell(3, i).getContents());
						case 4:
							content = sheet.getCell(4, i).getContents();
							projectcostBean.setProductOutput(Integer.parseInt(content));
						case 5:
							content = sheet.getCell(5, i).getContents();
							projectcostBean.setMaterialCost(Float.parseFloat(content));
						case 6:
							content = sheet.getCell(6, i).getContents();
							projectcostBean.setManalCoat(Float.parseFloat(content));
						case 7:
							content = sheet.getCell(7, i).getContents();
							projectcostBean.setDepreciatCost(Float.parseFloat(content));
						case 8:
							content = sheet.getCell(8, i).getContents();
							projectcostBean.setProductCost(Float.parseFloat(content));
						case 9:
							content = sheet.getCell(9, i).getContents();
							projectcostBean.setInMaterialCost(Float.parseFloat(content));
						case 10:
							content = sheet.getCell(10, i).getContents();
							projectcostBean.setInManalCost(Float.parseFloat(content));
						case 11:
							content = sheet.getCell(11, i).getContents();
							projectcostBean.setInDepreciatCost(Float.parseFloat(content));
						case 12:
							content = sheet.getCell(12, i).getContents();
							projectcostBean.setInProductCost(Float.parseFloat(content));
					}

					if(projectcostBean.getProductName()!=null) {
						SQLUtil.add(projectcostBean);
					}					
				}
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void initSheet(WritableSheet sheet) throws RowsExceededException, WriteException {
		Label label = new Label(0, 0, "产品来源");
		sheet.addCell(label);
		label = new Label(1, 0, "类型");
		sheet.addCell(label);
		label = new Label(2,0,"项目");
		sheet.addCell(label);
		label = new Label(3,0,"任务类型");
		sheet.addCell(label);
		label = new Label(4,0,"样本类型");
		sheet.addCell(label);
		label = new Label(5,0,"产量");
		sheet.addCell(label);
		label = new Label(6, 0, "直接材料");
		sheet.addCell(label);
		label = new Label(7, 0, "直接人工");
		sheet.addCell(label);
		label = new Label(8, 0, "直接折旧");
		sheet.addCell(label);
		label = new Label(9, 0, "其他制造费用");
		sheet.addCell(label);
		label = new Label(10, 0, "间接材料");
		sheet.addCell(label);
		label = new Label(11, 0, "间接工资");
		sheet.addCell(label);
		label = new Label(12, 0, "间接折旧");
		sheet.addCell(label);
		label = new Label(13, 0, "其他间接制造费用");
		sheet.addCell(label);
	}
	
}
