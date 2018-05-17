package cn.madeai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class Notepad extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JMenuBar menuBar;

	JMenu menu0;

	JMenu menu1;

	// JMenuItem item0_0,item0_1,item0_2,item0_3, item1_0,item1_1;

	JTextArea textArea;
	// 文件路径的全局变量;
	String absolutePath;

	public Notepad(String s, int x, int y, int w, int h) {

		init(s);

		setLocation(x, y);

		setSize(w, h);

		setVisible(true);
		

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	void init(String s) {

		setTitle(s);
		
		ImageIcon icon=new ImageIcon("resources\\icon.png");
		
		setIconImage(icon.getImage());

		menuBar = new JMenuBar();

		String[][] itemTitle = { 
				{ "新建", "打开", "保存", "另存", "退出" },
				{ "作者：小明", "版本：v1.0" }
			};

		menu0 = new JMenu("文件");
		JMenuItem[] item0 = new JMenuItem[itemTitle[0].length];
		for (int i = 0; i < item0.length; i++) {
			item0[i] = null;
			item0[i] = new JMenuItem(itemTitle[0][i]);
			menu0.add(item0[i]);
			if (i != item0.length - 1)
				menu0.addSeparator();
			item0[i].addActionListener(this);
			item0[i].setActionCommand(i + "");
		}
		menuBar.add(menu0);

		menu1 = new JMenu("关于");
		JMenuItem[] item1 = new JMenuItem[itemTitle[1].length];
		for (int i = 0; i < item1.length; i++) {
			item1[i] = null;
			item1[i] = new JMenuItem(itemTitle[1][i]);
			menu1.add(item1[i]);
			if (i != item1.length - 1)
				menu1.addSeparator();
			item1[i].addActionListener(this);
			item1[i].setActionCommand(i + item0.length + "");
		}
		menuBar.add(menu1);

		setJMenuBar(menuBar);

		textArea = new JTextArea(10, 15);
		textArea.setTabSize(4);
		textArea.setFont(new Font("标楷体", Font.BOLD, 16));
		textArea.setLineWrap(true);// 激活自动换行功能
		textArea.setWrapStyleWord(true);// 激活断行不断字功能
		textArea.setBackground(Color.white);

		this.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

	}

	/**
	 * 弹出对话框,选择txt文件; 
	 * @param要打开的文件目录; 
	 * @return选中的文件的绝对路径;
	 */
	protected String choseFile(String path) {
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		JFileChooser fc = new JFileChooser(f);
		TxtFileFilter txtFilter = new TxtFileFilter();
		fc.addChoosableFileFilter(txtFilter);
		fc.setFileFilter(txtFilter);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		absolutePath = fc.getSelectedFile().getAbsolutePath();
		return absolutePath;
	}

	/**
	 * 读取.txt文件;
	 * 以后还是乖乖用throws吧/(ㄒoㄒ)/~~
	 */
	protected char[] readFile(String absolutePath) {
		FileReader fr = null;
		char[] ch = null;
		try {
			File f = new File(absolutePath);
			fr = new FileReader(f);
			// 不清楚FileReader怎么获取文件大小,以后填坑;
			// 巨坑,保存后一大堆空格,坑以填;
			ch = new char[(int) f.length()];
			fr.read(ch);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return ch;
	}

	protected void writeFile(String absolutePath, String text) {
		FileWriter fw = null;
		try {
			// 保存后该换行换行;
			String[] str = text.split("\n");
			// true 追加;
			fw = new FileWriter(absolutePath);
			for (String string : str) {
				fw.write(string + "\r\n");
			}
			fw.flush();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	protected void savaAs(String path, String text) {
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		JFileChooser fc = new JFileChooser(f);
		fc.setDialogTitle("sava as");
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			f = fc.getSelectedFile();
		String fileName = f.getName();
		if (fileName == null || fileName.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "文件名为空！");
		}
		writeFile(f.getAbsolutePath(), text);
	}

	public void goBlog() {
		if (java.awt.Desktop.isDesktopSupported()) {
			try {
				// 创建一个URI实例
				java.net.URI uri = java.net.URI.create("https://blog.madeai.cn");
				// 获取当前系统桌面扩展
				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
				// 判断系统桌面是否支持要执行的功能
				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
					// 获取系统默认浏览器打开链接
					dp.browse(uri);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * if(e.getSource()==item0_0) { textArea.setText("hello world!!"); }
		 */

		switch (e.getActionCommand().charAt(0)) {
		case '0':
			Date d = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			textArea.setText(sf.format(d) + "\r\n\t");
			System.out.println(e.getActionCommand().charAt(0));
			break;
		case '1':
			char[] text = readFile(choseFile("E:\\notebook\\"));
			textArea.setText(String.valueOf(text));
			break;
		case '2':
			String writeText = textArea.getText();
			writeFile(absolutePath, writeText);
			break;
		case '3':
			String str = textArea.getText();
			savaAs("E:\\notebook\\", str);
			break;
		case '4':
			System.exit(0);
			dispose();
			break;
		case '5':
			goBlog();
		}
	}

	/*
	 * 内部类实现过滤文件类型;
	 */
	class TxtFileFilter extends FileFilter {
		public String getDescription() {
			return "*.cpp;*.txt;*.java;*md";
		}

		public boolean accept(File file) {
			String name = file.getName();
			return file.isDirectory() || name.toLowerCase().endsWith(".cpp") || name.toLowerCase().endsWith(".txt")
					|| name.toLowerCase().endsWith(".java")|| name.toLowerCase().endsWith(".md"); // 仅显示目录和cpp和txt文件
		}
	}

	public static void main(String[] args) {
		Notepad notepad = new Notepad("记事本", 300, 300, 500, 250);
	}
}
