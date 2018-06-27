import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Bean.Material;
import Util.ReadExcel;
import jxl.read.biff.BiffException;

public class CostCalcuation {
	

	public static void main(String[] args) {
		List<Material> list = null;
		ChoiceFrame mainFrame = new ChoiceFrame();
		mainFrame.showFrame();
		ReadExcel readExcel = new ReadExcel(new File("D:/JAVA/material.xls"));
		try {
			list =  readExcel.getMaterData();
			for(int i=0;i<list.size();i++) {
				System.out.println(list.get(i).getSn());
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
