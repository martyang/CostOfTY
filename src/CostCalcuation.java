import java.sql.SQLException;

import sql.SQLUtil;

public class CostCalcuation {
	

	public static void main(String[] args) {
		ChoiceFrame mainFrame = new ChoiceFrame();
		mainFrame.showFrame();
		try {
			SQLUtil.initDataSQL();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
		}
	}

}
