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
	private File file;	//ԭʼ�����ļ�
	private File resultFolder;	//���Ŀ¼
	private Workbook workbook;	//ԭʼ���ݵĹ�����
	private ArrayList<OperateBean> opetateList;
	private File result;
	
	public CostAllot(File file){
		this.file = file;
		resultFolder = new File(file.getParent()+"\\�Y���ļ�");
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
			//��ʼ��ȫ���Ĳ�����
			if(content.equals("�˹��ɱ�")) {
				Cell[] cells = sheet.getRow(i);
				for (Cell cell : cells) {
					if(!cell.getContents().equals("�˹��ɱ�")) {
						OperateBean operate = new OperateBean();
						operate.setType(type);
						operate.setName(cell.getContents());
						opetateList.add(operate);
					}
					
				}
				System.out.println("������������"+opetateList.size());
			}else if(content.equals("��׼��ʱ")) {
				Cell[] cells = sheet.getRow(i);
				int j = 0;
				for (Cell cell : cells) {
					if(!cell.getContents().equals("��׼��ʱ")) {
						content = cell.getContents();
						if(!content.equals("")) {
							opetateList.get(j++).setManhour(Float.parseFloat(content));
						}						
					}
					
				}
			}else if(content.equals("����")) {
				Cell[] cells = sheet.getRow(i);
				int j = 0;
				for (Cell cell : cells) {
					if(!cell.getContents().equals("����")) {
						content = cell.getContents();
						if(!content.equals("")) {
							opetateList.get(j++).setProduct(Integer.parseInt(content));
						}
					}
					
				}
			}else if(content.equals("ֱ�Ӳ���")) {
				int number = sheet.getColumns();
				Cell[] cells = sheet.getRow(i);
//				System.out.println("������"+number);
//				float total = Float.parseFloat(cells[1].getContents());
				int j=0;
				OperateBean operate;
				for(int m=2;m<number;m++) {
					content = cells[m].getContents();
					operate = opetateList.get(j++);
					operate.setMaterialCost(Float.parseFloat(content));
				}	
			}else if(content.equals("ֱ���˹�")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				float sumManal = 0 ;	//��ֱ���˹�
				for (OperateBean operateBean : opetateList) {
					sumManal += operateBean.getManhour()*operateBean.getProduct(); 
				}
				for (OperateBean operateBean : opetateList) {
					float manRatio = operateBean.getManhour()*operateBean.getProduct()/sumManal;
//					System.out.println(operateBean.getManhour());
					operateBean.setManalCoat(manRatio*total);
//					System.out.println(sumManal);
				}
			}else if(content.equals("ֱ���۾�")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					sum += operateBean.getMaterialCost()+operateBean.getManalCoat();
				}
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setDepreciatCost(ratio*total);
				}
			}else if(content.equals("�����������")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setProductCost(ratio*total);
				}
			}else if(content.equals("��Ӳ���")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInMaterialCost(ratio*total);
				}
			}else if(content.equals("��ӹ���")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInManalCost(ratio*total);
				}
			}else if(content.equals("����۾�")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInDepreciatCost(total*ratio);
				}
			}else if(content.equals("��������������")) {
				float total = Float.parseFloat(sheet.getCell(1, i).getContents());
				for (OperateBean operateBean : opetateList) {
					ratio = (operateBean.getMaterialCost()+operateBean.getManalCoat())/sum;
					operateBean.setInProductCost(total*ratio);
				}
			}else if(content.equals("�ϼ�")) {
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
		String resultname = resultFolder.getAbsolutePath()+"\\"+file.getName()+"���.xls";
		WritableSheet resultSheet;
		WritableSheet allotSheet;
		result = new File(resultname);
		if(result.exists()) {
			result.delete();
		}
		result.createNewFile();
		//����sheet���
		WritableWorkbook workbookResult = Workbook.createWorkbook(result);
		if(workbookResult.getNumberOfSheets()==0) {
			resultSheet = workbookResult.createSheet("������", 0);
			allotSheet = workbookResult.createSheet("������", 1);
		}else {
			resultSheet = workbookResult.getSheet(0);
			resultSheet.setName("������");
			allotSheet = workbookResult.getSheet(1);
			allotSheet.setName("������");
		}
		
		Label label ;
		//������������,iΪ��
		for (int i=0;i<=opetateList.size();i++) {
			if(i==0) 
			{
				label = new Label(i, 0, "����");
				resultSheet.addCell(label);
				label = new Label(i, 1, "ֱ�Ӳ���");
				resultSheet.addCell(label);
				label = new Label(i, 2, "ֱ���˹�");
				resultSheet.addCell(label);
				label = new Label(i, 3, "ֱ���۾�");
				resultSheet.addCell(label);
				label = new Label(i, 4, "�����������");
				resultSheet.addCell(label);
				label = new Label(i, 5, "��Ӳ���");
				resultSheet.addCell(label);
				label = new Label(i, 6, "��ӹ���");
				resultSheet.addCell(label);
				label = new Label(i, 7, "����۾�");
				resultSheet.addCell(label);
				label = new Label(i, 8, "��������������");
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
		System.out.println("����"+row);
		//������з�����,jΪ����
		for(int j=0;j<=opetateList.size();j++) {
			if(j==0) 
			{
				label = new Label(0, row+j, "����");
				resultSheet.addCell(label);
				label = new Label(1, row+j, "ֱ�Ӳ���");
				resultSheet.addCell(label);
				label = new Label(2, row+j, "ֱ���˹�");
				resultSheet.addCell(label);
				label = new Label(3, row+j, "ֱ���۾�");
				resultSheet.addCell(label);
				label = new Label(4, row+j, "�����������");
				resultSheet.addCell(label);
				label = new Label(5, row+j, "��Ӳ���");
				resultSheet.addCell(label);
				label = new Label(6, row+j, "��ӹ���");
				resultSheet.addCell(label);
				label = new Label(7, row+j, "����۾�");
				resultSheet.addCell(label);
				label = new Label(8, row+j, "��������������");
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
		
		Sheet sheet = workbook.getSheet(1);
		for(int k=0;k<sheet.getRows();k++) {
			if(k==0)
			{
				label = new Label(0, k, "����");
				allotSheet.addCell(label);
				label = new Label(1,k,"��Ŀ");
				allotSheet.addCell(label);
				label = new Label(2,k,"��������");
				allotSheet.addCell(label);
				label = new Label(3,k,"��������");
				allotSheet.addCell(label);
				label = new Label(4,k,"����");
				allotSheet.addCell(label);
				label = new Label(5, k, "ֱ�Ӳ���");
				allotSheet.addCell(label);
				label = new Label(6, k, "ֱ���˹�");
				allotSheet.addCell(label);
				label = new Label(7, k, "ֱ���۾�");
				allotSheet.addCell(label);
				label = new Label(8, k, "�����������");
				allotSheet.addCell(label);
				label = new Label(9, k, "��Ӳ���");
				allotSheet.addCell(label);
				label = new Label(10, k, "��ӹ���");
				allotSheet.addCell(label);
				label = new Label(11, k, "����۾�");
				allotSheet.addCell(label);
				label = new Label(12, k, "��������������");
				allotSheet.addCell(label);
				
			}else {
				Cell cell = sheet.getCell(0, k);
				label = new Label(0, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(1, k);
				String content = cell.getContents();
				label = new Label(1, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(2, k);
				content = cell.getContents();
				label = new Label(2, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(3, k);
				String name = cell.getContents();	//��ȡ��ʽ�����ƣ��������ֲ�ͬ����������
				label = new Label(3, k, cell.getContents());
				allotSheet.addCell(label);
				
				cell = sheet.getCell(4, k);
				content = cell.getContents();
				label = new Label(4, k, cell.getContents());
				allotSheet.addCell(label);
				
				int production = 0;
				if(!content.equals("")) {
					production = Integer.parseInt(content);
				}
				for (OperateBean operateBean : opetateList) {
					if(operateBean.getName().equals(name)) {
						label = new Label(5, k, production*operateBean.getSingleMaterialCost()+"");
						allotSheet.addCell(label);
						label = new Label(6, k, production*operateBean.getSingleManCost()+"");
						allotSheet.addCell(label);
						label = new Label(7, k, production*operateBean.getSingleDepreciatCost()+"");
						allotSheet.addCell(label);
						label = new Label(8, k, production*operateBean.getSingleProductCost()+"");
						allotSheet.addCell(label);
						label = new Label(9, k, production*operateBean.getSingleInMaterialCost()+"");
						allotSheet.addCell(label);
						label = new Label(10, k, production*operateBean.getSingleInManCost()+"");
						allotSheet.addCell(label);
						label = new Label(11, k, production*operateBean.getSingleInDepreciatCost()+"");
						allotSheet.addCell(label);
						label = new Label(12, k, production*operateBean.getSingleInProductCost()+"");
						allotSheet.addCell(label);			
					}
				}
			}
	
		}
		workbookResult.write();
		workbookResult.close();
	}
	
	
}
