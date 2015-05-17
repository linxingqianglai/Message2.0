package server;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


import socket.ClientInformation;

import constpage.Const;

public class Server extends JFrame implements ActionListener {
	
	public final static Dimension DEFAULT_FRAME_SIZE = new Dimension(400,400); //Ĭ�ϴ��ڴ�С
	private int messagePort ;//��ǰ�˿�
	private Vector<String> clientNameList; //�ͻ��������б�
	private Map<String,ClientInformation> clientList; //�ͻ����б�
	
	
	private JList ui_Client_JList; //��ʾ�û��б�
	private JScrollPane ui_Client_JScrollPane; //��ʾ�û��б������
	private JLabel ui_Client_JLabel;//��ʾ�û��б���ʾ��Ϣ
	private JButton ui_Start_JButton; //��������ť
	private JButton ui_Stop_JButton; //ֹͣ����ť
	private JLabel ui_Port_JLabel; //�޸Ķ˿���ʾ��Ϣ
	private JTextField ui_Port_JTextField;//�޸Ķ˿������
	private ServerSocket serverMessageSocket; //��������ϢSocket
	private ServerSocket serverFileSocket ;//�������ļ�Socket
	private Listen mesageListen ; //��������Ϣ��������
	private Listen fileListen; //�������ļ���������
	private boolean isClose;
	
	public Server(){
			this.clientNameList = new Vector<String>();
			this.clientList = new TreeMap<String,ClientInformation>();
			this.messagePort = Const.DEFAULT_MESSAGE_PORT;
			isClose = true;
			
			//��ʼ��UI
			initUI();
			
			
			this.setVisible(true);
			
	}
	
	/**
	 *  ��ʼ��UI 
	 */
	private void initUI(){
			this.ui_Client_JLabel = new JLabel("�ͻ����б�");
			this.ui_Client_JList = new JList(this.clientNameList);
			this.ui_Client_JList .setEnabled(false);
			this.ui_Client_JScrollPane = new JScrollPane(ui_Client_JList,
					JScrollPane. VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.ui_Port_JLabel = new JLabel("�˿ںţ�");
			this.ui_Port_JTextField = new JTextField(20);
			this.ui_Port_JTextField.setText(String.valueOf(Const.DEFAULT_MESSAGE_PORT));
			
			this.ui_Start_JButton = new JButton("��������");
			this.ui_Stop_JButton = new JButton("ֹͣ����");
			this.ui_Stop_JButton.setEnabled(false);
			
			
			
			this.ui_Start_JButton.addActionListener(this);
			this.ui_Stop_JButton.addActionListener(this);
			
			this.setLayout(null);
			this.ui_Port_JLabel.setBounds(20, 50, 70, 20);
			this.ui_Port_JTextField.setBounds(80, 50,100,20);
			this.ui_Client_JLabel.setBounds(250, 50,120,20);
			this.ui_Start_JButton.setBounds(20, 100,90,30);
			this.ui_Stop_JButton.setBounds(130, 100,90,30);
			this.ui_Client_JScrollPane.setBounds(250, 80,120,200);
			
			this.add(this.ui_Port_JLabel);
			this.add(this.ui_Port_JTextField);
			this.add(this.ui_Client_JLabel);
			this.add(this.ui_Start_JButton);
			this.add(this.ui_Stop_JButton);
			this.add(this.ui_Client_JScrollPane);
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(DEFAULT_FRAME_SIZE);
			this.setResizable(false);
			this.setLocation(400, 300);
			this.setTitle("�����");
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
						stopService();
						System.exit(0);
				}

			});
			
			
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==this.ui_Start_JButton){
			this.messagePort = Integer.valueOf(this.ui_Port_JTextField.getText());
			startService();
			
		}else if(e.getSource()==this.ui_Stop_JButton){
			stopService();
		}
		
	}

	/**
	 * ��������u
	 */
	private void startService() {
		// TODO Auto-generated method stub
		if(isClose){
			
			try {
				serverMessageSocket = new ServerSocket(messagePort);
				serverMessageSocket.setSoTimeout(1000);
				
				this.ui_Start_JButton.setEnabled(false);
				this.ui_Port_JTextField.setEditable(false);
				this.ui_Stop_JButton.setEnabled(true);
				
				this.mesageListen = new Listen(this);
				this.mesageListen.start();
				
				isClose = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "��������ʧ�ܣ�����ԭ��"+e.getMessage());
			}
			

		}



	}
	
	/**
	 * ֹͣ����
	 */
	private void stopService() {
		// TODO Auto-generated method stub
		if(!isClose){
			try {
				
				this.mesageListen.setStop();
				int time = 30; 
				while(time>0){
					if(this.mesageListen.isClosed()){
						break;
					}	
					System.out.println("�ȴ��������ر�");
					time--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				this.serverMessageSocket.close();
				this.ui_Start_JButton.setEnabled(true);
				this.ui_Port_JTextField.setEditable(true);
				this.ui_Stop_JButton.setEnabled(false);
				this.clientList.clear();
				this.clientNameList.removeAllElements();
				this.ui_Client_JList.setListData(this.clientNameList);
				isClose = true;
				System.out.println("�������ѹر�");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "ֹͣ����ʧ�ܣ�����ԭ��"+e.getMessage());
			}

		}

	}
	
	/**
	 * ���ݿͻ������ƻ�ȡ�ͻ��˶���
	 * @param name
	 * @return
	 */
	public ClientInformation findClient(String name){
		return this.clientList.get(name);
	}
	
	/**
	 * �Ƴ��ͻ���
	 * @param name
	 * @return
	 */
	public boolean removeClient(String name){
		if(findClient(name)!=null){
			this.clientList.remove(name);
			this.clientNameList.remove(name);
			this.ui_Client_JList.setListData(this.clientNameList);
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * ���ӿͻ���
	 * @param client
	 */
	public void addClient(ClientInformation client){
		this.clientNameList.add(client.getName());
		this.clientList.put(client.getName(),client);
		this.ui_Client_JList.setListData(this.clientNameList);
	}
	

	/**
	 * ���ؿͻ������б�
	 * @return
	 */
	public Vector<String> getClientNameList(){
			return this.clientNameList;
	}

	
	/**
	 * �����Ƿ�ر�
	 * @return
	 */
	public synchronized boolean isClosed(){
			return this.isClose;
	}
	/**
	 *  ��ȡSocket
	 * @return
	 * @throws IOException
	 */
	public Socket getSocket() throws IOException{
		return serverMessageSocket.accept();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Server();
	}
}
