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
				
				//��ȡ��������
				String commName = cKchoice.getSelectedItem();			
				//��ȡ������
				String bpsStr = bTChoice.getSelectedItem();
				
				//��鴮�������Ƿ��ȡ��ȷ
				if (commName == null || commName.equals("") || commName.equals("��û�д���")) {
					JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����", JOptionPane.INFORMATION_MESSAGE);			
				}
				else {
					//��鲨�����Ƿ��ȡ��ȷ
					if (bpsStr == null || bpsStr.equals("")) {
						JOptionPane.showMessageDialog(null, "�����ʻ�ȡ����", "����", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						//�������������ʾ���ȡ��ȷʱ
						int bps = Integer.parseInt(bpsStr);
						try {
							
							openPort = SerialTool.openPort(commName, bps);
							//�ڸô��ڶ�������Ӽ�����
							SerialTool.addListener(openPort, new SerialPortEventListener() {
								
								@Override
								public void serialEvent(SerialPortEvent serialPortEvent) {
									// TODO Auto-generated method stub
									switch (serialPortEvent.getEventType()) {

							            case SerialPortEvent.BI: // 10 ͨѶ�ж�
							            	JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
							            	break;
	
							            case SerialPortEvent.OE: // 7 ��λ�����������
	
							            case SerialPortEvent.FE: // 9 ֡����
	
							            case SerialPortEvent.PE: // 8 ��żУ�����
	
							            case SerialPortEvent.CD: // 6 �ز����
	
							            case SerialPortEvent.CTS: // 3 �������������
	
							            case SerialPortEvent.DSR: // 4 ����������׼������
	
							            case SerialPortEvent.RI: // 5 ����ָʾ
	
							            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
							            	break;
							            
							            case SerialPortEvent.DATA_AVAILABLE:

							            	byte[] data = readData(openPort);
							            	dataListener.OnSucceed(data);
							            	break;
									}
								}
							});
							//�����ɹ�������ʾ
							JOptionPane.showMessageDialog(null, "�������ڳɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							openSerialButton.setEnabled(false);
						} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
							//��������ʱʹ��һ��Dialog��ʾ����Ĵ�����Ϣ
							JOptionPane.showMessageDialog(null, e1, "����", JOptionPane.INFORMATION_MESSAGE);
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
