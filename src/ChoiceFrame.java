import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ChoiceFrame {
	private String frameName = "碳云成本核算工具";
	private String choiceType;
	private String choicePatch;
	
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
		type.addElement("物料成本");
		type.addElement("人工成本");
		type.addElement("QC成本");
		JComboBox<String> typeCombobox = new JComboBox<>(type);
		typeCombobox.setSelectedItem(0);
		JScrollPane typeScrollPanel = new JScrollPane(typeCombobox);
		//文件选择按钮
		JFileChooser fileChooser = new JFileChooser();
		JButton openFile = new JButton("选择文件");
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showOpenDialog(frame);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					File chooseFile = fileChooser.getSelectedFile();
					System.out.println("选择文件："+chooseFile.getPath());
				}
			}
		});	
		
		JButton startCalcution = new JButton("开始");
		startCalcution.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
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
