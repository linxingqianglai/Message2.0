package client;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.html.HTMLEditorKit;

import message.ImageMessage;
import message.Message;
import message.TextMessage;

import util.HtmlUtil;
import util.ImageUtil;

import constpage.Const;

public class Client extends JFrame implements ActionListener {
	public final static Dimension DEFAULT_FRAME_SIZE = new Dimension(600,800); //Ĭ�ϴ��ڴ�С
	public static final String ALL_USER="All";
	private int port ;//��ǰ�˿�
	private String ip ;//������IP
	private Vector<String> clientNameList; //�ͻ��������б�
	private String user;//��ǰ�û�
	private boolean isLogin;
	private Color messageColor ;//��Ϣ��ɫ
	private JComboBox ui_SendTo_JComboBox;//������ѡ��
	private JList ui_Client_JList; //��ʾ�û��б�
	private JScrollPane ui_Client_JScrollPane; //��ʾ�û��б������
	private JLabel ui_Client_JLabel;//��ʾ�û��б���ʾ��Ϣ
	private JLabel ui_Port_JLabel; //�޸Ķ˿���ʾ��Ϣ
	private JLabel ui_Ip_JLabel;// �޸�IP��ʾ��Ϣ
	private JLabel ui_User_JLabel;//�û���
	private JLabel ui_SendTo_JLabel;//��������ʾ
	private JLabel ui_MessageColor_JLabel; //��ɫ��ʾ
	private JTextField ui_Port_JTextField;//�޸Ķ˿������
	private JTextField ui_Ip_JTextField;//�޸�IP�����
	private  JTextField ui_User_JTextField;//�޸��û������
	
	private JEditorPane ui_MessageShow_JEditorPane;// ��ʾ��Ϣ
	private JScrollPane ui_MessageShow_JScrollPane;
	
	private JEditorPane ui_MessageInput_JEditorPane;//������Ϣ
	private JScrollPane ui_MessageInput_JScrollPane; 
	
	private JButton ui_Login_JButton; //��¼
	private JButton ui_LogOut_JButton;//ע��
	private JButton ui_Send_JButton ;//����
	private JButton ui_MessageColor_JButton;//�û���ɫ
	private JButton ui_Image_JButton; //���ͼ��а�ͼƬ
	private ClientReceive clientReceiv;//��Ϣ�շ�����
	private StringBuilder messageStringBuilder ;//��Ϣ�б�
	
	private SystemTray sysTray ;//ϵͳ���̶���
	private TrayIcon trayIcon ;//��ǰ���������
	private Image iconNoMessage;//����ͼ��
	private Image iconHasMessage;//����ͼ��
	
	public Client(){
		this.clientNameList = new Vector<String>();
		this.port=Const.DEFAULT_MESSAGE_PORT;
		this.ip="";
		this.user="";
		this.isLogin =false;
		this.messageStringBuilder = new StringBuilder();
		this.iconNoMessage =ImageUtil.getImage("1.gif");
		this.iconHasMessage=ImageUtil.getImage("3.gif");


		createTrayIcon();//�������̶���
		//��ʼ��UI
		initUI();
		this.setMessageColor(Const.DEFALUT_MESSAGE_COLOR);
		this.setVisible(true);
	}
	
	/**
	 * �������̶���
	 */
	private void createTrayIcon(){
		sysTray = SystemTray.getSystemTray();//��ȡ��ǰ����ϵͳ�����̶���
		
		PopupMenu popuMenu = new PopupMenu();//�����˵�
		MenuItem showM = new MenuItem("Popup");
		MenuItem exitM = new MenuItem("Exit");
		popuMenu.add(showM);
		popuMenu.add(exitM);
		
		
		showM.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Client.this.setVisible(true);
				//Client.this.sysTray.remove(Client.this.trayIcon);
			}
		});
		
		exitM.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				logout();
				System.exit(0);
			}
		});
		
		
		trayIcon = new TrayIcon(this.iconNoMessage,"Client",popuMenu);
		
		this.trayIcon.addMouseListener(new MouseAdapter() {
			private long time =0;
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(this.time==0){
					this.time = new Date().getTime();
				}else{
					long tempTime = new Date().getTime();
					if(tempTime - this.time <=300){  //˫��ͼ��
							Client.this.setVisible(true);
							//Client.this.setFocusableWindowState(true);
							//Client.this.sysTray.remove(Client.this.trayIcon);
					}else{
						this.time =tempTime;
					}
					
				}
				
			}
			
		});
	}

	/**
	 * ��ʼ������
	 */
	private void initUI() {
		// TODO Auto-generated method stub
		this.ui_Client_JLabel = new JLabel("Online users��");
		this.ui_Port_JLabel = new JLabel("Port��");
		this.ui_Ip_JLabel = new JLabel("Server  IP��");
		this.ui_User_JLabel = new JLabel("User name��");
		this.ui_SendTo_JLabel = new JLabel("Send to��");
		this.ui_MessageColor_JLabel = new JLabel("Font color��");
		
		this.ui_Client_JList = new JList(this.clientNameList);
		this.ui_Client_JList .setEnabled(false);
		this.ui_Client_JScrollPane = new JScrollPane(ui_Client_JList,
				JScrollPane. VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.ui_Port_JTextField = new JTextField(20);
		this.ui_Port_JTextField.setText(String.valueOf(Const.DEFAULT_MESSAGE_PORT));
		
		this.ui_Ip_JTextField = new JTextField(20);
		this.ui_Ip_JTextField.setText("133.10.44.85");
		
		this.ui_User_JTextField = new JTextField(30);
		this.ui_User_JTextField.setText("");
		
		this.ui_MessageShow_JEditorPane = new JEditorPane();
		this.ui_MessageShow_JEditorPane.setContentType("text/html");
		this.ui_MessageShow_JEditorPane.setText("");
		this.ui_MessageShow_JEditorPane.setEditable(false);
		this.ui_MessageShow_JScrollPane = new JScrollPane(ui_MessageShow_JEditorPane,
				JScrollPane. VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.ui_MessageInput_JEditorPane = new JEditorPane();
		this.ui_MessageInput_JEditorPane.setText("");
		//this.ui_MessageInput_JEditorPane.setEditorKit(new DefaultEditorKit());
		this.ui_MessageInput_JEditorPane.setEnabled(true);
		this.ui_MessageInput_JEditorPane.setEditable(true);
		this.ui_MessageInput_JScrollPane = new JScrollPane(ui_MessageInput_JEditorPane,
				JScrollPane. VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.ui_SendTo_JComboBox = new JComboBox(this.clientNameList.toArray());
		this.ui_SendTo_JComboBox.insertItemAt(ALL_USER, 0);
		this.ui_SendTo_JComboBox.setSelectedIndex(0);
		
		this.ui_Login_JButton = new JButton("Logon");
		this.ui_LogOut_JButton = new JButton("Logoff");
		this.ui_LogOut_JButton.setEnabled(false);
		
		this.ui_Send_JButton = new JButton("Send");
		this.ui_MessageColor_JButton = new  JButton("");
		
		this.ui_Image_JButton = new  JButton("<html><body>Send clipboard  <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; picture</body></html>");
		
		
		this.ui_Login_JButton.addActionListener(this);
		this.ui_LogOut_JButton.addActionListener(this);
		this.ui_Send_JButton.addActionListener(this);
		this.ui_MessageColor_JButton.addActionListener(this);
		this.ui_Image_JButton.addActionListener(this);
		
		
		
		this.setLayout(null);
		this.ui_Login_JButton.setBounds(70, 50, 70, 30);
		this.ui_LogOut_JButton.setBounds(200, 50, 70, 30);
		this.ui_Ip_JLabel.setBounds(20, 15, 70, 20);
		this.ui_Ip_JTextField.setBounds(80, 15,100,20);
		
		this.ui_Port_JLabel.setBounds(200, 15, 70, 20);
		this.ui_Port_JTextField.setBounds(250, 15,70,20);
		
		this.ui_User_JLabel.setBounds(360, 15, 100, 20);
		this.ui_User_JTextField.setBounds(440, 15,130,20);
		
		this.ui_MessageShow_JScrollPane .setBounds(20, 100, 550, 300);

		
		this.ui_SendTo_JLabel.setBounds(20,420, 100, 20);
		this.ui_SendTo_JComboBox.setBounds(70,420, 100, 20);
		this.ui_MessageColor_JLabel.setBounds(200,420, 100, 20);
		this.ui_MessageColor_JButton.setBounds(270,420, 20,20);
		this.ui_Client_JLabel.setBounds(450, 420, 100, 20);
		this.ui_Client_JScrollPane.setBounds(450,450, 120, 200);
		this.ui_MessageInput_JScrollPane .setBounds(20, 450, 420, 200);
		this.ui_Send_JButton.setBounds(340, 650, 100, 40);
		
		this.ui_Image_JButton.setBounds(20, 650, 130, 40);

		
		
		this.add(this.ui_Client_JLabel);
		this.add(this.ui_Port_JLabel);
		this.add(this.ui_Ip_JLabel);
		this.add(this.ui_User_JLabel);
		this.add(this.ui_SendTo_JLabel);
		this.add(this.ui_Client_JScrollPane);
		this.add(this.ui_Port_JTextField);
		this.add(this.ui_Ip_JTextField);
		this.add(this.ui_User_JTextField);
		this.add(this.ui_MessageShow_JScrollPane);
		this.add(this.ui_MessageInput_JScrollPane);
		this.add(this.ui_Login_JButton);
		this.add(this.ui_LogOut_JButton);
		this.add(this.ui_Send_JButton);
		this.add(this.ui_SendTo_JComboBox);
		this.add(this.ui_MessageColor_JLabel);
		this.add(this.ui_MessageColor_JButton);
		this.add(this.ui_Image_JButton);
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(DEFAULT_FRAME_SIZE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);//��Ļ����
		this.setTitle("Client");
	
		this.ui_MessageInput_JEditorPane.addKeyListener(new KeyAdapter() {  //���÷�����Ϣ��ݼ�
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ENTER && e.isControlDown()){
						Client.this.sendTextMessage();
				}
			}
				
		});
		
		
		this.addWindowListener(new WindowAdapter() {
			
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
					
					trayIcon.setImage(iconNoMessage);
					Client.this.setVisible(false);

			}
			

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				trayIcon.setImage(iconNoMessage);
			}



			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				trayIcon.setImage(iconNoMessage);
				try {
					sysTray.add(trayIcon);
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}


			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
					logout();
					System.exit(0);
			}

		});
		
	}

	


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==this.ui_Login_JButton){
				login();
		}else if(e.getSource()==this.ui_LogOut_JButton){
			logout();
		}else if(e.getSource()==this.ui_Send_JButton){
			sendTextMessage();
		}else if(e.getSource()==this.ui_MessageColor_JButton){
			setMessageColor(getOptionColr(this.messageColor));
		}else if(e.getSource()==this.ui_Image_JButton){
			sendImageMessage();
		}
		
	}
	
	/**
	 * ����ͼƬ��Ϣ
	 */
	private void sendImageMessage() {
		// TODO Auto-generated method stub
		if(this.isLogin){
			Image image = ImageUtil.getImageClipboard();
			String message = this.ui_MessageInput_JEditorPane.getText();
			
			if(image!=null){
				this.ui_MessageInput_JEditorPane.setText("");
				
				ImageMessage im = new ImageMessage();

				if(this.ui_SendTo_JComboBox.getSelectedIndex()==0){
					im.setToALL(true);
					im.setToUser("");
				}else{
					im.setToALL(false);
					im.setToUser((String)this.ui_SendTo_JComboBox.getSelectedItem());
				}
				
				im.setMessage(message);
				im.setColor(this.messageColor);
				im.setImage(image);
				try {
					this.clientReceiv.sendImageMessage(im);
					//������ʾ
					addImageMessage(im,false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				JOptionPane.showMessageDialog(this, "No picture in clipboard");
			}
		}
	}

	/**
	 * �����ı���Ϣ
	 */
	private void sendTextMessage() {
		// TODO Auto-generated method stub
		if(this.isLogin){
			String message = this.ui_MessageInput_JEditorPane.getText();
			if(message.equals("")){
				return ;
			}
			TextMessage tm = new TextMessage();
			
			this.ui_MessageInput_JEditorPane.setText("");
			if(this.ui_SendTo_JComboBox.getSelectedIndex()==0){
				tm.setToALL(true);
				tm.setToUser("");
			}else{
				tm.setToALL(false);
				tm.setToUser((String)this.ui_SendTo_JComboBox.getSelectedItem());
			}
			
			try {
				tm.setMessage(message);
				tm.setColor(this.messageColor);
				this.clientReceiv.sendTextMessage(tm);
				addTextMessage(tm,false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * ע��
	 */
	public void logout() {
		// TODO Auto-generated method stub
		if(this.isLogin){
			try {
				this.clientReceiv.logout();
				int time = 10; 
				while(time>0){
					if(this.clientReceiv.isClosed()){
						break;
					}	
					time--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				this.clientReceiv.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.setLoginStatr(false);
			

			

		}
	}

	/**
	 * ���õ�¼״̬, True �ѵ�¼ False ��ע��
	 * @param b
	 */
	public void setLoginStatr(boolean b){
		if(b){
			this.isLogin=true;
			this.ui_Login_JButton.setEnabled(false);
			this.ui_Ip_JTextField.setEditable(false);
			this.ui_Port_JTextField.setEditable(false);
			this.ui_User_JTextField.setEditable(false);
			this.ui_LogOut_JButton.setEnabled(true);
		}else{
			this.setClientList(new Vector<String>());
			this.ui_Login_JButton.setEnabled(true);
			this.ui_Ip_JTextField.setEditable(true);
			this.ui_Port_JTextField.setEditable(true);
			this.ui_User_JTextField.setEditable(true);
			this.ui_LogOut_JButton.setEnabled(false);
			this.isLogin=false;
		}
	}


	/**
	 * ��¼
	 */
	private void login() {
		// TODO Auto-generated method stub
		if(!this.isLogin){
			this.ip = this.ui_Ip_JTextField.getText();
			this.port=Integer.valueOf(this.ui_Port_JTextField.getText());
			this.user=this.ui_User_JTextField.getText().trim();
			if(this.user.equals("")){
				JOptionPane.showMessageDialog(this, "Must inpu user nam");
				return;
			}
			try {
				Socket s = new Socket(ip,port);
				s.setSoTimeout(10000);
				this.clientReceiv = new ClientReceive(this,s);
				this.clientReceiv.start();
				
				this.setLoginStatr(true);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Logon Server failed"+e.getMessage());
			}

			
		}
		
	}



	public int getPort() {
		return port;
	}



	public String getIp() {
		return ip;
	}



	public String getUser() {
		return user;
	}

	/**
	 * �����Ƿ���ע��
	 * @return
	 */
	public boolean isClosed(){
		return !this.isLogin;
	}
	

	/**
	 * ������ʾ�ı���Ϣ
	 * @param tm
	 * @param newFlag
	 */
	public void addTextMessage(TextMessage tm,boolean  newFlag){
		String title = "";
		if(newFlag){
			showNewMessagePopupDialog(tm);
			title = tm.getServerTitle();
		}else{
			title = tm.getClientTitle();
		}
		
		//���滻�����Ϣ������Ϣ�б�
		this.messageStringBuilder.append(TextMessage.getDefaultTitlSytle(title));
		this.messageStringBuilder.append(tm.getMessageSytle());
		this.ui_MessageShow_JEditorPane.setDocument(HtmlUtil.stringToDocument(this.messageStringBuilder.toString()));
		this.ui_MessageShow_JEditorPane.select(Integer.MAX_VALUE, Integer.MAX_VALUE);
		this.SaveMessageToFile();
	}
	
	

	/**
	 * ������ʾͼƬ��Ϣ
	 * @param im
	 * @param newFlag
	 */
	public void addImageMessage(ImageMessage im,boolean  newFlag){
		String title = "";
		if(newFlag){
			showNewMessagePopupDialog(im);
			title = im.getServerTitle();
		}else{
			title = im.getClientTitle();
		}

		//ͼƬ���
		String filePath = ImageUtil.saveImageToFile(im.getImage());
		//���滻�����Ϣ������Ϣ�б�
		this.messageStringBuilder.append(ImageMessage.getDefaultTitlSytle(title));
		this.messageStringBuilder.append(im.getMessageSytle());
		this.messageStringBuilder.append("<img src=\"file:\\"+filePath+"\">");
		this.messageStringBuilder.append("</img><br/>");
		this.ui_MessageShow_JEditorPane.setDocument(HtmlUtil.stringToDocument(this.messageStringBuilder.toString()));
		this.ui_MessageShow_JEditorPane.select(Integer.MAX_VALUE, Integer.MAX_VALUE);
		this.SaveMessageToFile();
	}


	/**
	 * ���������û��б�
	 * @param clientNameList
	 */
	public void setClientList(Vector<String> clientNameList){
		this.clientNameList = clientNameList;
		this.ui_Client_JList.setListData(clientNameList.toArray());
		this.ui_SendTo_JComboBox.removeAllItems();
		this.ui_SendTo_JComboBox.insertItemAt(ALL_USER, 0);
		this.ui_SendTo_JComboBox.setSelectedIndex(0);
		for(String s:clientNameList){
			if(s.equalsIgnoreCase(this.user)) continue;
				
			this.ui_SendTo_JComboBox.addItem(s);
		}
	}
	

	
	
	/**
	 * ������ɫѡ��Ի��򣬷���ѡ�����ɫ
	 * @param color
	 * @return
	 */
	private Color getOptionColr(Color color){
		ColorOptionDialog cod = new ColorOptionDialog(color,this);
		return cod.getColor();
	}
	
	/**
	 * ��ʾ����Ϣ������Ϣ��
	 * @param ms
	 */
	private void showNewMessagePopupDialog(Message ms){
		this.trayIcon.displayMessage("New message", ms.getMessage(), TrayIcon.MessageType.INFO);
		this.trayIcon.setImage(iconHasMessage);
		this.getToolkit().beep();
	}
	
	/**
	 * ������Ϣ��ɫ
	 * @param color
	 */
	private void setMessageColor(Color color){
			this.messageColor = color;
			this.ui_MessageColor_JButton.setBackground(color);
	}
	
	
	/**
	 * SaveMessage���ļ�
	 */
	private void SaveMessageToFile(){
		File fpath = new File(System.getProperty("user.dir"),Const.MESSAGE_SAVF_PATH);
		 if(!fpath.exists()){
			 fpath.mkdirs();
		 }
		 
		 File f = new File(Const.MESSAGE_SAVF_PATH,Const.DEFALUT_MESSAGE_SAVF_FILE);
		 try {
			if(!f.exists()){
					f.createNewFile();
			 }
			FileWriter fw= new FileWriter(f);
			fw.write("<html> <body>");
			String message = this.messageStringBuilder.toString();
			message =message.replace("<img src=\"file:\\","<img src=\"");
			fw.write(message);
			fw.write("</body> </html>");
			fw.flush();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client();
	}
}
