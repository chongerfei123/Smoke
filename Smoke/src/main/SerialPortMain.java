package main;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import exception.NoSuchPort;
import exception.NotASerialPort;
import exception.PortInUse;
import exception.ReadDataFromSerialPortFailure;
import exception.SendDataToSerialPortFailure;
import exception.SerialPortInputStreamCloseFailure;
import exception.SerialPortOutputStreamCloseFailure;
import exception.SerialPortParameterFailure;
import exception.TooManyListeners;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import main.Deal.ReadDataListener;
import util.SerialTool;

public class SerialPortMain {

	private static SerialTool serialTool;
	private static Choice CKchoice;
	private static Choice bTChoice;
	private static JProgressBar progressBar;
	private static Button openSerialButton;
	private static Label value;
	private static byte[] Data;
	private static Button closeSerialButton;

	public static void main(String[] args) {
	
		Deal deal = init();
		deal.dealWithButton(closeSerialButton,openSerialButton,CKchoice,bTChoice,new ReadDataListener() {
			@Override
			public void OnSucceed(byte[] data) {
				Data = data;
				for (byte b : data) {
					//System.out.println(b);
					deal.dealWithData(Data,value,progressBar);
				}
			}
		});
		
	}

	
	private static Deal init() {
		serialTool = SerialTool.getSerialTool();
		View view = new View();
		view.launchFrame();
		CKchoice = view.getcKchoice();
		bTChoice = view.getbTChoice();
		progressBar = view.getProgressBar();
		openSerialButton = view.getOpenSerialButton();
		closeSerialButton = view.getCloseSerialButton();
		value = view.getValue();
		Deal deal = new Deal();
		ArrayList<String> portList = deal.getPortList(serialTool);
		if (portList.size() == 0){
			CKchoice.add("还没有串口");
		}else{
			CKchoice.removeAll();
			for (String string : portList) {
				CKchoice.add(string);
			}
		}
		return deal;
	}
}

