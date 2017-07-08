package main;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import exception.ExceptionWriter;


public class View extends Frame{

	private static final int FRAMEWIDTH = 800;
	private static final int FRAMEHEIGHT = 550;
	Image offScreen = null;
	private Choice cKchoice;
	private Choice bTChoice;
	private Button openSerialButton;
	private Button closeSerialButton;
	private JProgressBar progressBar;
	private Label value;

	public Choice getcKchoice() {
		return cKchoice;
	}

	public void setcKchoice(Choice cKchoice) {
		this.cKchoice = cKchoice;
	}

	public Choice getbTChoice() {
		return bTChoice;
	}

	public void setbTChoice(Choice bTChoice) {
		this.bTChoice = bTChoice;
	}

	public Button getOpenSerialButton() {
		return openSerialButton;
	}

	public void setOpenSerialButton(Button openSerialButton) {
		this.openSerialButton = openSerialButton;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public Label getValue() {
		return value;
	}

	public void setValue(Label value) {
		this.value = value;
	}

	public void launchFrame() {
		this.setBounds(200, 70, FRAMEWIDTH, FRAMEHEIGHT);	
		this.setTitle("烟雾传感器");
		this.setBackground(Color.white);	
		this.setLayout(null);
		this.addWindowListener(new WindowAdapter() {
			//添加对窗口状态的监听
			public void windowClosing(WindowEvent arg0) {
				//当窗口关闭时
				System.exit(0);	//退出程序
			}
			
		});

		this.setResizable(false);
		this.setVisible(true);	
		cKchoice = new Choice();
		cKchoice.setFont(new Font("楷体", Font.ITALIC, 15));
		cKchoice.setBounds(160, 140, 180, 180);
		add(cKchoice);
		
		bTChoice = new Choice();
		bTChoice.setFont(new Font("楷体", Font.ITALIC, 15));
		bTChoice.setBounds(160, 210, 180, 100);
		bTChoice.add("1200");
		bTChoice.add("2400");
		bTChoice.add("4800");
		bTChoice.add("9600");
		bTChoice.add("14400");
		bTChoice.add("19200");
		bTChoice.add("115200");
		add(bTChoice);
		
		openSerialButton = new Button("打开串口");
		openSerialButton.setBounds(100, 350, 200, 50);
		openSerialButton.setBackground(Color.lightGray);
		openSerialButton.setFont(new Font("楷体", Font.CENTER_BASELINE, 20));
		add(openSerialButton);
		
		closeSerialButton = new Button("关闭串口");
		closeSerialButton.setBounds(100, 450, 200, 50);
		closeSerialButton.setBackground(Color.lightGray);
		closeSerialButton.setFont(new Font("楷体", Font.CENTER_BASELINE, 20));
		add(closeSerialButton);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(460, 250, 250, 20);
		progressBar.setString("烟雾值");
		progressBar.setMaximum(100);
		progressBar.setValue(50);
		progressBar.setForeground(Color.gray);
		add(progressBar);
		
		value = new Label("暂无数据", Label.CENTER);
		value.setBounds(530, 125, 225, 50);
		value.setBackground(Color.white);
		value.setFont(new Font("楷体", Font.ITALIC, 25));
		value.setForeground(Color.gray);
		add(value);
		new Thread(new RepaintThread()).start();	//开启重画线程
	}
	
	public Button getCloseSerialButton() {
		return closeSerialButton;
	}

	public void setCloseSerialButton(Button closeSerialButton) {
		this.closeSerialButton = closeSerialButton;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("微软雅黑", Font.ITALIC, 30));
		g.setColor(Color.black);
		g.drawString("设置", 90, 90);
		
		g.setFont(new Font("楷体", Font.BOLD, 25));
		g.drawString("串口：", 40, 160);
		g.drawString("波特率：", 40, 230);
		g.drawString("烟雾值：", 440, 160);
		g.drawLine(FRAMEWIDTH/2, 0, FRAMEWIDTH/2, FRAMEHEIGHT);
	}
	
	
	
	@Override
	public void update(Graphics g) {
		if (offScreen == null)	offScreen = this.createImage(FRAMEWIDTH, FRAMEHEIGHT);
		Graphics gOffScreen = offScreen.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.white);
		gOffScreen.fillRect(0, 0, FRAMEWIDTH, FRAMEHEIGHT);	//重画背景画布
		this.paint(gOffScreen);	//重画界面元素
		gOffScreen.setColor(c);
		g.drawImage(offScreen, 0, 0, null);	//将新画好的画布“贴”在原画布上
	}
	private class RepaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					String err = ExceptionWriter.getErrorInfoFromException(e);
					JOptionPane.showMessageDialog(null, err, "错误", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
		}
		
	}
}
