import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import Util.CommonMaterialCost;
import Util.CostAllot;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ChoiceFrame {
	private String frameName = "̼�Ƴɱ����㹤��";
	private String choiceType;
	private File chooseFile = null;
//	private File[] chooseFiles;
	private static String MATERIAL = "���ϳɱ�";
	private static String ALLOCT = "����ȫ���ɱ�";
	private static String QC = "QC�ɱ�";
	
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
		
		JLabel typeLabel = new JLabel("ѡ��������ͣ�");
		JLabel fileChoiceLabel = new JLabel("ѡ������ļ���");
		//�ɱ�����ѡ���
		DefaultComboBoxModel<String> type = new DefaultComboBoxModel<>();
		type.addElement(MATERIAL);
		type.addElement(ALLOCT);
		type.addElement(QC);
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
		
		//�ļ�ѡ��ť
		JFileChooser fileChooser = new JFileChooser();
		JButton openFile = new JButton("ѡ���ļ�");
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
		
		JButton startCalcution = new JButton("��ʼ");
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
					JOptionPane.showMessageDialog(null, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					
				}else if(choiceType.equals(ALLOCT)) {
					System.out.println(chooseFile.getName());
					CostAllot costAllot = new CostAllot(chooseFile);
					try {
						costAllot.initData();
						costAllot.outData();
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
					JOptionPane.showMessageDialog(null, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				}else if(choiceType.equals(QC)) {
					System.out.println("����QC�ɱ�");
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
