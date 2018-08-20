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
import javax.swing.JScrollPane;

import Util.ReadMaterial;
import jxl.read.biff.BiffException;

public class ChoiceFrame {
	private String frameName = "̼�Ƴɱ����㹤��";
	private String choiceType;
	private File chooseFile;
	private File[] chooseFiles;
	private static String MATERIAL = "���ϳɱ�";
	private static String MANUAL = "�˹��ɱ�";
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
		type.addElement(MANUAL);
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
				if(choiceType.equals(MATERIAL)) {
					System.out.println(chooseFile.getName());
					ReadMaterial readMaterial = new ReadMaterial(chooseFile);
					try {
						readMaterial.initData();
					} catch (BiffException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					float number = readMaterial.getBloodNumber("1020004");
					System.out.println("�������ϳɱ�");
					System.out.println("ʹ����"+number);
				}else if(choiceType.equals(MANUAL)) {
					System.out.println("�����˹��ɱ�");
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
