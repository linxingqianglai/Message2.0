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
	
	public final static Dimension DEFAULT_FRAME_SIZE = new Dimension(400,400); //默认窗口大小
	private int messagePort ;//当前端口
	private Vector<String> clientNameList; //客户端名称列表
	private Map<String,ClientInformation> clientList; //客户端列表
	
	
	private JList ui_Client_JList; //显示用户列表
	private JScrollPane ui_Client_JScrollPane; //显示用户列表滚动条
	private JLabel ui_Client_JLabel;//显示用户列表提示信息
	private JButton ui_Start_JButton; //启动服务按钮
	private JButton ui_Stop_JButton; //停止服务按钮
	private JLabel ui_Port_JLabel; //修改端口提示信息
	private JTextField ui_Port_JTextField;//修改端口输入框
	private ServerSocket serverMessageSocket; //服务器消息Socket
	private ServerSocket serverFileSocket ;//服务器文件Socket
	private Listen mesageListen ; //服务器消息侦听对象
	private Listen fileListen; //服务器文件侦听对象。
	private boolean isClose;
	
	public Server(){
			this.clientNameList = new Vector<String>();
			this.clientList = new TreeMap<String,ClientInformation>();
			this.messagePort = Const.DEFAULT_MESSAGE_PORT;
			isClose = true;
			
			//初始化UI
			initUI();
			
			
			this.setVisible(true);
			
	}
	
	/**
	 *  初始化UI 
	 */
	private void initUI(){
			this.ui_Client_JLabel = new JLabel("客户端列表");
			this.ui_Client_JList = new JList(this.clientNameList);
			this.ui_Client_JList .setEnabled(false);
			this.ui_Client_JScrollPane = new JScrollPane(ui_Client_JList,
					JScrollPane. VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.ui_Port_JLabel = new JLabel("端口号：");
			this.ui_Port_JTextField = new JTextField(20);
			this.ui_Port_JTextField.setText(String.valueOf(Const.DEFAULT_MESSAGE_PORT));
			
			this.ui_Start_JButton = new JButton("启动服务");
			this.ui_Stop_JButton = new JButton("停止服务");
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
			this.setTitle("服务端");
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
	 * 启动服务u
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
				JOptionPane.showMessageDialog(this, "启动服务失败，错误原因："+e.getMessage());
			}
			

		}



	}
	
	/**
	 * 停止服务
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
					System.out.println("等待侦听器关闭");
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
				System.out.println("服务器已关闭");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "停止服务失败，错误原因："+e.getMessage());
			}

		}

	}
	
	/**
	 * 根据客户端名称获取客户端对象
	 * @param name
	 * @return
	 */
	public ClientInformation findClient(String name){
		return this.clientList.get(name);
	}
	
	/**
	 * 移除客户端
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
	 * 增加客户端
	 * @param client
	 */
	public void addClient(ClientInformation client){
		this.clientNameList.add(client.getName());
		this.clientList.put(client.getName(),client);
		this.ui_Client_JList.setListData(this.clientNameList);
	}
	

	/**
	 * 返回客户名称列表
	 * @return
	 */
	public Vector<String> getClientNameList(){
			return this.clientNameList;
	}

	
	/**
	 * 服务是否关闭
	 * @return
	 */
	public synchronized boolean isClosed(){
			return this.isClose;
	}
	/**
	 *  获取Socket
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
