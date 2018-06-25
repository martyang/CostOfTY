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
	private String frameName = "̼�Ƴɱ����㹤��";
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
		
		JLabel typeLabel = new JLabel("ѡ��������ͣ�");
		JLabel fileChoiceLabel = new JLabel("ѡ������ļ���");
		//�ɱ�����ѡ���
		DefaultComboBoxModel<String> type = new DefaultComboBoxModel<>();
		type.addElement("���ϳɱ�");
		type.addElement("�˹��ɱ�");
		type.addElement("QC�ɱ�");
		JComboBox<String> typeCombobox = new JComboBox<>(type);
		typeCombobox.setSelectedItem(0);
		JScrollPane typeScrollPanel = new JScrollPane(typeCombobox);
		//�ļ�ѡ��ť
		JFileChooser fileChooser = new JFileChooser();
		JButton openFile = new JButton("ѡ���ļ�");
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showOpenDialog(frame);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					File chooseFile = fileChooser.getSelectedFile();
					System.out.println("ѡ���ļ���"+chooseFile.getPath());
				}
			}
		});	
		
		JButton startCalcution = new JButton("��ʼ");
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
