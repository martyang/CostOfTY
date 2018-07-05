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
	private ArrayList<Material> faeces ;//�����ȡ
	private ArrayList<Material> mouth ;//��ǻ��ȡ
	private ArrayList<Material> plasma;//Ѫ����ȡ
	private ArrayList<Material> blood;//ѪҺ��ȡ
	
	public ReadMaterial(File file) {
		this.file = file;
		faeces = new ArrayList<>();
		mouth = new ArrayList<>();
		plasma= new ArrayList<>();
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
		Sheet sheet = workbook.getSheet(0);
		String type = null;
		
		for(int i=0;i<sheet.getRows();i++) {
			Material material = new Material();
			Cell cell = null;
			//��ʼ��ȡsheet
			for(int j=0;j<sheet.getColumns();j++) {
				
				cell = sheet.getCell(j, i);//�÷�����ǰһ������Ϊ�У���һ������Ϊ��
				String content = cell.getContents();
				
				if(content.contains("����")) {
					type = content.split("��")[1];
					if(type.equals("��ҺDNA��ȡ")) {
						materList = mouth;
					}else if(type.contains("Ѫ��DNA��ȡ")) {
						materList = plasma;
					}else {
						materList = blood;
					}
					System.out.println("type:"+type);
					break;
				}else if(content.contentEquals("���ϱ���")) {
					break;
				}
				//��ȡ����д�뵽bean
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
	 * ��ȡ�����ȡʹ����
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
	 * ��ȡ��ǻ��ȡʹ����
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
	 * ��ȡѪ����ȡʹ����
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
	 * ��ȡѪҺ��ȡʹ����
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
