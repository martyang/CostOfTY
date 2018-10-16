import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import Util.CollectProjectCost;
import Util.CommonMaterialCost;
import Util.CostAllot;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ChoiceFrame {
	private String frameName = "碳云成本核算工具";
	private String choiceType;
	private File chooseFile = null;
//	private File[] chooseFiles;
	private static String MATERIAL = "物料成本";
	private static String ALLOCT = "分配全部成本";
	private static String PROJECT = "分配到项目";
	private CostAllot costAllot = null;
	
	public ChoiceFrame() {
		// TODO Auto-generated constructor stub
	}
	public ChoiceFrame(String frameName) {
		// TODO Auto-generated constructor stub
		this.frameName = frameName;
	}
	public void showFrame() {
		JFrame frame = new JFrame(frameName);
		frame.setSize(350, 250);
		frame.setLocation(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(5, 1));
		frame.setResizable(false);
		
		JLabel typeLabel = new JLabel("选择核算类型：");
		JLabel fileChoiceLabel = new JLabel("选择核算文件：");
		//成本类型选择框
		DefaultComboBoxModel<String> type = new DefaultComboBoxModel<>();
		type.addElement(MATERIAL);
		type.addElement(ALLOCT);
		type.addElement(PROJECT);
		JComboBox<String> typeCombobox = new JComboBox<>(type);
		typeCombobox.setSelectedItem(0);
		choiceType = (String) typeCombobox.getSelectedItem();
		typeCombobox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				choiceType = (String) typeCombobox.getSelectedItem();
			}
		});
		JScrollPane typeScrollPanel = new JScrollPane(typeCombobox);
		
		//文件选择按钮
		JFileChooser fileChooser = new JFileChooser();
		JButton openFile = new JButton("选择文件");
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showOpenDialog(frame);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					chooseFile = fileChooser.getSelectedFile();
//					chooseFiles = fileChooser.getSelectedFiles();
					
				}
			}
		});	
		
		JButton startCalcution = new JButton("开始");
		startCalcution.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooseFile==null) {
					
				}else if(choiceType.equals(MATERIAL)) {
					System.out.println(chooseFile.getName());
					CommonMaterialCost comMaterial = new CommonMaterialCost(chooseFile);
					try {
						comMaterial.initCostData();
					} catch (BiffException | WriteException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "计算完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
					
				}else if(choiceType.equals(ALLOCT)) {
					System.out.println(chooseFile.getName());
					File folder = chooseFile.getParentFile();
					File[] files = folder.listFiles();
					try {
						for (File file : files) {
							if(file.isFile()) {
								costAllot = new CostAllot(file);
								costAllot.initData();
								costAllot.outData();
							}
						}
					} catch (BiffException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (RowsExceededException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (WriteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "计算完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
				}else if(choiceType.equals(PROJECT)) {
					
					CollectProjectCost projectCost = new CollectProjectCost(chooseFile);
					try {
						projectCost.start();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (RowsExceededException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (WriteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "计算完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});
		
		frame.add(typeLabel);
		frame.add(typeScrollPanel);
		frame.add(fileChoiceLabel);
		frame.add(openFile);
		frame.add(startCalcution);
		frame.setVisible(true);
	}
	
	

}
