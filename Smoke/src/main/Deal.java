package main;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import exception.NoSuchPort;
import exception.NotASerialPort;
import exception.PortInUse;
import exception.ReadDataFromSerialPortFailure;
import exception.SerialPortInputStreamCloseFailure;
import exception.SerialPortParameterFailure;
import exception.TooManyListeners;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import util.SerialTool;

public class Deal {
	private SerialPort openPort;

	public ArrayList<String> getPortList(SerialTool serialTool) {
		ArrayList<String> findPort = serialTool.findPort();
		return findPort;
	}

	public void dealWithButton(Button closeSerialButton, Button openSerialButton, Choice cKchoice, Choice bTChoice,ReadDataListener dataListener) {
		closeSerialButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SerialTool.closePort(openPort);
				openSerialButton.setEnabled(true);
			}
		});
		
		openSerialButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				//获取串口名称
				String commName = cKchoice.getSelectedItem();			
				//获取波特率
				String bpsStr = bTChoice.getSelectedItem();
				
				//检查串口名称是否获取正确
				if (commName == null || commName.equals("") || commName.equals("还没有串口")) {
					JOptionPane.showMessageDialog(null, "没有搜索到有效串口！", "错误", JOptionPane.INFORMATION_MESSAGE);			
				}
				else {
					//检查波特率是否获取正确
					if (bpsStr == null || bpsStr.equals("")) {
						JOptionPane.showMessageDialog(null, "波特率获取错误！", "错误", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						//串口名、波特率均获取正确时
						int bps = Integer.parseInt(bpsStr);
						try {
							
							openPort = SerialTool.openPort(commName, bps);
							//在该串口对象上添加监听器
							SerialTool.addListener(openPort, new SerialPortEventListener() {
								
								@Override
								public void serialEvent(SerialPortEvent serialPortEvent) {
									// TODO Auto-generated method stub
									switch (serialPortEvent.getEventType()) {

							            case SerialPortEvent.BI: // 10 通讯中断
							            	JOptionPane.showMessageDialog(null, "与串口设备通讯中断", "错误", JOptionPane.INFORMATION_MESSAGE);
							            	break;
	
							            case SerialPortEvent.OE: // 7 溢位（溢出）错误
	
							            case SerialPortEvent.FE: // 9 帧错误
	
							            case SerialPortEvent.PE: // 8 奇偶校验错误
	
							            case SerialPortEvent.CD: // 6 载波检测
	
							            case SerialPortEvent.CTS: // 3 清除待发送数据
	
							            case SerialPortEvent.DSR: // 4 待发送数据准备好了
	
							            case SerialPortEvent.RI: // 5 振铃指示
	
							            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
							            	break;
							            
							            case SerialPortEvent.DATA_AVAILABLE:

							            	byte[] data = readData(openPort);
							            	dataListener.OnSucceed(data);
							            	break;
									}
								}
							});
							//监听成功进行提示
							JOptionPane.showMessageDialog(null, "监听串口成功", "提示", JOptionPane.INFORMATION_MESSAGE);
							openSerialButton.setEnabled(false);
						} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
							//发生错误时使用一个Dialog提示具体的错误信息
							JOptionPane.showMessageDialog(null, e1, "错误", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				
			}
		});
	}
	
	private byte[] readData(SerialPort openPort) {
		byte[] data = null;
		try {
			data = SerialTool.readFromPort(openPort);
		} catch (ReadDataFromSerialPortFailure e) {
			e.printStackTrace();
		} catch (SerialPortInputStreamCloseFailure e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public interface ReadDataListener {
		void OnSucceed(byte[] data);
	}

	public void dealWithData(byte[] data, Label value, JProgressBar progressBar) {
		// TODO Auto-generated method stub
		for (byte b : data) {
			value.setText(b+"");
			progressBar.setValue(b);
			if (b >= 65){
				progressBar.setForeground(Color.red);
			}else{
				progressBar.setForeground(Color.gray);
			}
		}
	}
}
