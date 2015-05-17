package socket;

import head.Head;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


import message.FileMessage;
import message.ImageMessage;
import message.TextMessage;


/**
 * Socket �ͻ��˶���
 * ������Ϊһ��Socket�������������Socket��������֡�����Ǹտ�ʼ�����ġ�
 * ���ж�Ӧ���������������Ϊ��д������
 * ����һ���жϵ�ǰ��Socket�Ƿ��Ѿ��رա�
 * @author itprogrammer
 *
 */
public class ClientInformation {
	private String name;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private boolean isColse;


	public synchronized boolean isColsed() {
		return isColse;
	}
	public synchronized void setColse(boolean isColse) {
		this.isColse = isColse;
	}
	public synchronized String getName() {
		return name;
	}
	public synchronized  void setName(String name) {
		this.name = name;
	}
	public Socket getSocket() {
		return socket;
	}
	/**
	 * ����Socket
	 * @param socket
	 * @throws IOException
	 */
	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.socket.setSoTimeout(10000); //����������ʱΪ10��
		this.output =  new ObjectOutputStream(this.socket.getOutputStream());
		this.input  =  new ObjectInputStream(this.socket.getInputStream());
	}

	/**
	 * ˢ�������,ˢ��֮ǰ״̬����δ�ر�<br/>
	 * ˢ��ʧ����״̬Ϊ�ر�
	 */
	private  void  outputFlush() {
		if(this.isColsed()){
			return  ;
		}
		try {
			this.output.flush();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			return  ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//�������쳣����
			this.setColse(true);
		}
		
	}



	/**
	 * 	д����д֮ǰ״̬����δ�ر�<br/>
	 *	дʧ����״̬Ϊ�ر�
	 * 	@param obj
	 */
	private  void writeObject(Object obj) {
		if(this.isColsed()){
			return  ;
		}
		try {
			this.output.writeObject(obj);
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			return  ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.setColse(true);
		}
	}



	/**
	 * ��ȡ���󣬶�ȡǰ״̬����δ�ر�<br/>
	 * ��ȡ�ɹ�����Object���ѹرջ�ʱ����null��ʧ�ܷ���null����״̬Ϊ�ر�
	 * @return
	 */
	public  Object readObject()  {
		if(this.isColsed()){
			return null ;
		}
		try {
			return this.input.readObject();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			return null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.setColse(true);
		}
		return null;
	}
	

	/**
	 * ��ȡ�ַ�������
	 * @return
	 */
	public  String readString()  {
		if(this.isColsed()){
			return null;
		}
		return (String)this.readObject();
	}
	
	/**
	 * ��ȡTextMessage����
	 * @return
	 */
	public  TextMessage reaadTextMessageObject()  {
		// TODO Auto-generated method stub
		if(this.isColsed()){
			return null;
		}
		return (TextMessage)this.readObject();
	}

	/**
	 * ��ȡImageMessage����
	 * @return

	 */
	public  ImageMessage reaadImageMessageObject()  {
		// TODO Auto-generated method stub
		if(this.isColsed()){
			return null;
		}
		return (ImageMessage)this.readObject();
	}
	
	/**
	 * ��ȡFileMessage����
	 * @return
	 */
	public FileMessage reaadFileMessageObject() {
		// TODO Auto-generated method stub
		if(this.isColsed()){
			return null;
		}
		return (FileMessage)this.readObject();
	}

	/**
	 * ��ȡ��Ϣͷ����
	 * @return
	 */
	public  Head readHeadObject() {
		if(this.isColsed()){
			return null;
		}
		return (Head)this.readObject();
		
	}

	/**
	 * �ر�Socket
	 * @throws IOException
	 */
	public synchronized void close(){
		try {
			if(this.socket!=null  && !this.socket.isClosed()){
				this.output.close();
				this.input.close();
				this.socket.close();
				this.setColse(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	/**
	 * ���͵�¼��Ϣ
	 * @return
	 */
	public  boolean sendLoginMessage(){
			if(this.isColsed()){
				return  false;
			}
			this.writeObject(this.name);
			this.outputFlush();
			if(this.isColsed()){
				return false;
			}else{
				return true;
			}
			
	}
	
	/**
	 * ��ȡ��¼��Ϣ
	 * @return
	 */
	public  String readLoginMessage(){
		if(this.isColsed()){
			return null ;
		}
		return this.readString();  
		
	}
	
	/**
	 * ���Ϳͻ���������Ϣ
	 * @return
	 */
	public  boolean sendClientLogoutMessage(){
		if(this.isColsed()){
			return  false;
		}
		
		this.writeObject(Head.clientLogoutHead);  //��ϢΪ�ͻ���������Ϣ
		this.outputFlush();
		System.out.println("���Ϳͻ���������Ϣ");
		if(this.isColsed()){
			return false;
		}else{
			return true;
		}
		
	}

	/**
	 * ���Ͷ���
	 * @param message
	 * @return
	 */
	public  boolean sendObject(Object message){
		if(this.isColsed()){
			return  false;
		}
		this.writeObject(message);  
		this.outputFlush();
		if(this.isColsed()){
			return false;
		}else{
			return true;
		}
	}
}
