import java.io.File;
import java.io.IOException;
import java.util.List;

import Bean.Material;
import Util.ReadExcel;
import jxl.read.biff.BiffException;

public class CostCalcuation {
	

	public static void main(String[] args) {
		List<Material> list = null;
		ChoiceFrame mainFrame = new ChoiceFrame();
		mainFrame.showFrame();
		ReadExcel readExcel = new ReadExcel(new File("D:/JAVA/≥Ã–Ú≤‚ ‘/≥Ã–Ú≤‚ ‘/material.xls"));
		
	}

}
