package nicelee.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nicelee.file.FileUtil;
import nicelee.file.Printer;

public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 文件夹选择
	JButton btnFolderChooser = new JButton("...");
	JTextField folderText = new JTextField();
	JCheckBox isSelectAllFolders = new JCheckBox("仅遍历当前文件夹");
	JCheckBox isRegx = new JCheckBox("正则");

	// 匹配文本
	JTextField txtMatcher = new JTextField(".*");

	// 代替换文本
	JTextField txtToReplace = new JTextField();
	JButton btnReplace = new JButton("替换");

	// 为将符合条件的文件创建同名文件夹，并移入其中
	JButton btnMove = new JButton("开始");

	// 列出包含匹配文件的路径
	JButton btnList = new JButton("开始");
	JCheckBox isNotList = new JCheckBox("条件取反");
	JCheckBox isAbsPath = new JCheckBox("绝对路径");
	JCheckBox isDealName = new JCheckBox("文件名处理");

	// 列出小于size的文件夹
	JTextField txtSize = new JTextField("10");
	JButton btnSize = new JButton("开始");
	
	JTextArea consoleArea = new JTextArea(12, 50);

	final int WIDTH_JLABEL = 100;
	final int HEIGHT = 30;

	public static void main(String[] args) {
		MainFrame log = new MainFrame();
		log.InitUI();
	}

	public void InitUI() {

		// 设置窗口名称
		this.setTitle("文件批量重命名");
		this.setSize(620, 480);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		URL iconURL = this.getClass().getResource("/resources/favicon.png");
//		ImageIcon icon = new ImageIcon(iconURL);
//		this.setIconImage(icon.getImage());

		// 此处使用流式布局FlowLayout，流式布局类似于word的布局
		FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
		// this窗口设置为f1的流式左对齐
		this.setLayout(f1);

		/**
		 * 文件夹选项
		 */
		this.addBlank(40, HEIGHT);
		JLabel lbDstFolder = new JLabel("目标文件夹：");
		lbDstFolder.setPreferredSize(new Dimension(WIDTH_JLABEL, HEIGHT));
		this.add(lbDstFolder);

		folderText.setPreferredSize(new Dimension(270, HEIGHT));
		this.add(folderText);

		btnFolderChooser.addActionListener(this);
		btnFolderChooser.setPreferredSize(new Dimension(20, HEIGHT));
		this.add(btnFolderChooser);

		isSelectAllFolders.setSelected(true);
		isSelectAllFolders.setEnabled(false);
		this.add(isSelectAllFolders);

		/**
		 * 匹配文本
		 */
		this.addBlank(40, HEIGHT);
		JLabel lbMatcher = new JLabel("匹配文本：");
		lbMatcher.setPreferredSize(new Dimension(WIDTH_JLABEL, HEIGHT));
		this.add(lbMatcher);

		txtMatcher.setPreferredSize(new Dimension(270, HEIGHT));
		isRegx.setSelected(true);
		this.add(txtMatcher);
		this.add(isRegx);
		// this.addBlank(90, HEIGHT);

		// 常用正则匹配
		JComboBox<String> cbRegx = new JComboBox<String>();
		cbRegx.addItem(" ");
		cbRegx.addItem("任意匹配");
		cbRegx.addItem("常见图片格式");
		cbRegx.addItem("常见视频格式");
		cbRegx.setSelectedIndex(1);
		this.add(cbRegx);

		/**
		 * 将匹配文本替换成
		 */
		this.addBlank(40, HEIGHT);
		JLabel lbReplacer = new JLabel("将内容替换成：");
		lbReplacer.setPreferredSize(new Dimension(WIDTH_JLABEL, HEIGHT));
		this.add(lbReplacer);
		txtToReplace.setPreferredSize(new Dimension(270, HEIGHT));
		this.add(txtToReplace);
		this.add(btnReplace);
		this.addBlank(90, HEIGHT);
		/**
		 * 为将符合条件的文件创建同名文件夹，并移入其中
		 */
		this.addBlank(40, HEIGHT);
		JLabel lbMover = new JLabel("为符合条件的文件创建同名文件夹，并移入其中");
		lbMover.setPreferredSize(new Dimension(300, HEIGHT));
		this.add(lbMover);
		this.add(btnMove);
		this.addBlank(190, HEIGHT);
		/**
		 * 列出包含匹配文件的路径
		 */
		this.addBlank(40, HEIGHT);
		JLabel lbFolderList = new JLabel("列出文件夹下包含匹配文件的路径");
		lbFolderList.setPreferredSize(new Dimension(200, HEIGHT));
		this.add(lbFolderList);
		this.add(btnList);
		this.add(isNotList);
		this.add(isAbsPath);
		this.add(isDealName);
		this.addBlank(20, HEIGHT);
		/**
		 * 列出小于指定大小的文件夹
		 */
		this.addBlank(40, HEIGHT);
		JLabel lbFolderSizeList = new JLabel("列出大小小于");
		this.add(lbFolderSizeList);
		txtSize.setPreferredSize(new Dimension(50, HEIGHT));
		this.add(txtSize);
		this.add(txtSize);
		JLabel lbFolderSizeList2 = new JLabel("(M)的文件夹");
		this.add(lbFolderSizeList2);
		this.add(btnSize);
		this.addBlank(280, HEIGHT);

		/**
		 * console
		 */
		this.addBlank(20, HEIGHT);
		consoleArea.setEditable(false);
		JScrollPane js = new JScrollPane(consoleArea);
		// 分别设置水平和垂直滚动条出现方式
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(js);

		/**
		 * 功能实现
		 */
		btnReplace.addActionListener(this);
		btnMove.addActionListener(this);
		btnList.addActionListener(this);
		btnSize.addActionListener(this);
		cbRegx.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				switch (cbRegx.getSelectedIndex()) {
				case 0:
					txtMatcher.setText("");
					break;
				case 1:
					txtMatcher.setText(".*");
					break;
				case 2:
					txtMatcher.setText("(?i)\\.(jpg|jpeg|png|bmp|ico|gif)$");
					break;
				case 3:
					txtMatcher.setText("(?i)\\.(mp4|avi|mov|rm|rmvb|ts|wmv|mpg|mpeg|flv|f4v)$");
					break;
				}
			}
		});

		Printer.init(consoleArea);
		// 设置窗口可见，此句一定要在窗口属性设置好了之后才能添加，不然无法正常显示
		this.setVisible(true);
	}

	/**
	 * 增加空白格，用于调整位置
	 */
	private void addBlank(int width, int height) {
		JLabel blank = new JLabel();
		blank.setPreferredSize(new Dimension(width, height));
		//blank.setBorder(BorderFactory.createLineBorder(Color.red));
		this.add(blank);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnFolderChooser) {
			JFileChooser fileChooser = null;
			String currentFolder = folderText.getText();
			File f = new File(currentFolder);
			if (f.exists()) {
				fileChooser = new JFileChooser(f);
			} else {
				fileChooser = new JFileChooser(".");

			}
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogTitle("请选择文件夹...");
			fileChooser.setApproveButtonText("确定");
			fileChooser.showOpenDialog(this);// 显示打开的文件对话框
			f = fileChooser.getSelectedFile();// 使用文件类获取选择器选择的文件
			if (f != null) {
				String s = f.getAbsolutePath();// 返回路径名
				folderText.setText(s);
			}
		} else if (e.getSource() == btnReplace) {
			File folder = new File(folderText.getText());
			if (!folder.exists() || txtMatcher.getText().isEmpty() || txtToReplace.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "请确保输入有效！");
				return;
			}
			FileUtil.rename(folder, true, isRegx.isSelected(), txtMatcher.getText(), txtToReplace.getText());
		} else if (e.getSource() == btnMove) {
			File folder = new File(folderText.getText());
			if (!folder.exists() || txtMatcher.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "请确保输入有效！");
				return;
			}
			FileUtil.createFolderAndMove(folder, isRegx.isSelected(), txtMatcher.getText());
		} else if (e.getSource() == btnList) {
			File folder = new File(folderText.getText());
			if (!folder.exists() || txtMatcher.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "请确保输入有效！");
				return;
			}
			FileUtil.list(folder, isRegx.isSelected(), txtMatcher.getText(), !isNotList.isSelected(),
					isAbsPath.isSelected(), isDealName.isSelected(), txtToReplace.getText());
		}else if (e.getSource() == btnSize){
			File folder = new File(folderText.getText());
			if (!folder.exists()) {
				JOptionPane.showMessageDialog(null, "请确保输入有效！");
				return;
			}
			txtSize.setText(txtSize.getText().replaceAll("[^0-9]", ""));
			FileUtil.listEmptyFile(folder, Integer.parseInt(txtSize.getText()));
		}
	}
}
