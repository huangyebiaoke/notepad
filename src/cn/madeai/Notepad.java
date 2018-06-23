package cn.madeai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

public class Notepad extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;

	private JMenu menu0,menu1,menu2;

	// MenuItem item0_0,item0_1,item0_2,item0_3, item1_0,item1_1;

	private JTextArea textArea;
	// 文件路径的全局变量;
	private String absolutePath;

	public Notepad(String s, int w, int h) {

		init(s);
		
		setSize(w, h);
		
		//居中显示JFrame;
		Toolkit tk=this.getToolkit();
		Dimension dim=tk.getScreenSize();
		this.setLocation((int)(dim.getWidth()-this.getWidth())/2,(int)(dim.getHeight()-this.getHeight())/2);

		setVisible(true);
		

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	protected void init(String s) {

		setTitle(s);
		
		ImageIcon icon=new ImageIcon("resources\\icon.png");
		
		setIconImage(icon.getImage());

		menuBar = new JMenuBar();

		String[][] itemTitle = { 
				{ "新建", "打开", "保存", "另存", "退出" },
				{"剪切","复制","黏贴"},
				{ "作者：小明", "版本：v1.1" }
			};

		menu0 = new JMenu("文件(F)");
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
		menu0.setMnemonic('f');
		item0[0].setAccelerator(KeyStroke.getKeyStroke('N', java.awt.Event.CTRL_MASK));
		item0[1].setAccelerator(KeyStroke.getKeyStroke('O', java.awt.Event.CTRL_MASK));
		item0[2].setAccelerator(KeyStroke.getKeyStroke('S', java.awt.Event.CTRL_MASK));
		item0[3].setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK+KeyEvent.ALT_MASK));
		item0[4].setAccelerator(KeyStroke.getKeyStroke('X', KeyEvent.CTRL_MASK+KeyEvent.ALT_MASK));
		menuBar.add(menu0);

		menu1 = new JMenu("编辑(E)");
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
		menu1.setMnemonic('e');
		item1[0].setAccelerator(KeyStroke.getKeyStroke('X', java.awt.Event.CTRL_MASK));
		item1[1].setAccelerator(KeyStroke.getKeyStroke('C', java.awt.Event.CTRL_MASK));
		item1[2].setAccelerator(KeyStroke.getKeyStroke('V', java.awt.Event.CTRL_MASK));
		menuBar.add(menu1);
		
		
		menu2 = new JMenu("关于(A)");
		JMenuItem[] item2 = new JMenuItem[itemTitle[2].length];
		for (int i = 0; i < item2.length; i++) {
			item2[i] = null;
			item2[i] = new JMenuItem(itemTitle[2][i]);
			menu2.add(item2[i]);
			if (i != item2.length - 1)
				menu2.addSeparator();
			item2[i].addActionListener(this);
			item2[i].setActionCommand(i + item0.length +item1.length+ "");
		}
		menu2.setMnemonic('a');
		item2[0].setToolTipText("access my blog?");
		item2[1].setToolTipText("access the project website?");
		menuBar.add(menu2);

		setJMenuBar(menuBar);
		
		ImageIcon background=new ImageIcon("resources\\background.png");

		textArea = new JTextArea(10, 15) {
			   {
				    setOpaque(false); // 设置透明
				   }

				   protected void paintComponent(Graphics g) {
				    g.drawImage(background.getImage(), 0, 0, this.getWidth(),this.getHeight(),this);
				    super.paintComponent(g);
				   }
		};
		textArea.setTabSize(4);
		textArea.setFont(new Font("标楷体", Font.BOLD, 16));
		textArea.setLineWrap(true);// 激活自动换行功能
		textArea.setWrapStyleWord(true);// 激活断行不断字功能
		//防止背景图片加载不出来,背景设为黑色;
		if(background==null) {
			textArea.setBackground(Color.BLACK);
		}
		textArea.setForeground(Color.WHITE);
		textArea.setCaretColor(Color.WHITE);
//		textArea.getCaret().setBlinkRate(WAIT_CURSOR);

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
	/*
	 * 另存,第一次一定要另存;
	 */
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
	
	protected void goWebsite(String url) {
		if (java.awt.Desktop.isDesktopSupported()) {
			try {
				// 创建一个URI实例
				java.net.URI uri = java.net.URI.create(url);
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
			JOptionPane.showMessageDialog(null, "功能待实现,请使用Ctrl+X快捷键操作");
			break;
		case '6':
			JOptionPane.showMessageDialog(null, "功能待实现,请使用Ctrl+C快捷键操作");
			break;
		case '7':
			JOptionPane.showMessageDialog(null, "功能待实现,请使用Ctrl+V快捷键操作");
			break;
		case '8':
			goWebsite("https://blog.madeai.cn");
			break;
		case '9':
			goWebsite("https://github.com/huangyebiaoke/notepad");
		}
	}

	/*
	 * 内部类实现过滤文件类型;
	 * 打开操作用的上;
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
		Notepad notepad = new Notepad("记事本", 640, 400);
	}
}