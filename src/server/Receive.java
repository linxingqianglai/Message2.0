package server;

import head.Head;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;

import socket.ClientInformation;

import message.FileMessage;
import message.ImageMessage;
import message.Message;
import message.TextMessage;


import constpage.Const;

public class Receive extends Thread {


	private ClientInformation client;
	private Server server;
	private Listen listen;
	private boolean isClosed;//״̬�Ƿ��ѹر�
	private ServerSocket fileServerSocket ;
	public Receive(ClientInformation client,Listen listen,Server server){
		this.client = client;
		this.server = server;
		this.listen =listen;
		this.fileServerSocket = null ;
		this.setIsClosed(false);
	}

	/**
	 * ���ùر�״̬
	 * @param isClosed
	 */
	private synchronized void setIsClosed(boolean isClosed){
		this.isClosed=isClosed;
	}

	/**
	 * �����Ƿ��ѹر�
	 * @return
	 */
	public synchronized boolean isClosed(){
		return this.isClosed;
	}


	/**
	 * �������Ƿ��ѹر�,�Ƿ���true���񷵻�false
	 * @return
	 */
	public synchronized boolean checkServer(){
		if(this.listen.isClosed() || this.server.isClosed()||this.client.isColsed()){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(checkServer()){
			this.setIsClosed(true);
			return;
		}
		
		System.out.println(this.client.getName()+"�ͻ���������");

		while(true){
			if(checkServer()){
				this.setIsClosed(true);
				break;
			}
			Head head = this.client.readHeadObject();   //��ȡ��Ϣͷ
			if(checkServer()){  //���ͻ����Ƿ��ѹر�
				this.setIsClosed(true);
				break;
			}
			
			if(head!=null){
				if(Head.isCommonMessage(head)){  //��ͨͨѶ��Ϣ
					if(!commonMessageHandle(head)){  //��ͨ��Ϣ��������ʧ�ܽ�����Ϣ������
						 break;
					}
				}else if(Head.isSystemMessage(head)){ //ϵͳͨѶ��Ϣ������ʧ�ܽ�����Ϣ������
					if(!systemMessageHandle(head)){
						break;
					}	
				}
			}
			
			
			if(checkServer()){
				this.setIsClosed(true);
				break;
			}

		}

		//ֹͣ
		this.client.close();
		this.setIsClosed(true); //�ͻ���״̬��Ϊ�ر�
		System.out.println("�ͻ��˽�����"+this.getName()+"�ѹر�");
	}

	/**
	 * ��ͨ��Ϣ�Ĵ�������ɹ�����true��ʧ�ܷ���false��ͨѶ�쳣����false���رս�������
	 * @param head
	 * @return
	 */
	private boolean commonMessageHandle(Head head){
		if(Head.isTextMessage(head)){  //��Ϣ����Ϊ�ı���Ϣ
			TextMessage message = this.client.reaadTextMessageObject();
			if(checkServer()){  //����Ƿ��ѹر�
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){  //��ȡ�ɹ�
				sendTextMessage(message);
			}
			
		}else if(Head.isImageMessage(head)){//��Ϣ����ΪͼƬ��Ϣ
			ImageMessage message =this.client.reaadImageMessageObject();
			if(checkServer()){  
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){
				sendImageMessage(message);
			}
			
		}else if(Head.isFileMessage(head)){//��Ϣ����Ϊ�ļ���Ϣ
			FileMessage message = this.client.reaadFileMessageObject();
			if(checkServer()){  
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){
				sendFileMessage(message);
			}
		}
		
		return true;
		
	}

	/**
	 * ϵͳ��Ϣ�Ĵ�������ɹ�����true��ʧ�ܷ���false��ͨѶ�쳣����false���رս�������
	 * @param head
	 * @return
	 */
	private boolean systemMessageHandle(Head head){
			if(Head.isClientLogoutMessage(head)){  //�ͻ��˿���������Ϣ
				System.out.println("�յ��ͻ�������������Ϣ");
				this.close();
				return false;
			}
			return true;
	}
	

	/**
	 * ת���ı���Ϣ
	 * @param message
	 */
	private  void sendTextMessage(TextMessage message){
			if(message.isToALL()){
				sendTextMsgToAll(message);
			}else{
				sendTextMsgToUser(message,message.getToUser());
			}
	}
	

	/**
	 * ת��ͼƬ��Ϣ
	 * @param message
	 */
	private  void sendImageMessage(ImageMessage message){
		if(message.isToALL()){
			sendImageMsgToAll(message);
		}else{
			sendImageMsgToUser(message,message.getToUser());
		}
	}
	
	/**
	 * ת���ļ���Ϣ
	 * @param message
	 */
	private  void sendFileMessage(FileMessage message){
		if(message.getStatus()==FileMessage.STATUS_START_SERVER){  //�����ļ�����
				if(startFileServer()){
					message.setStatus(FileMessage.STATUS_SEND_FILE);
					sendMessage(this.client, message, Head.fileHead); //�����ͷ����������ļ���������
					message.setStatus(FileMessage.STATU_RECEIVE_FILE); //�����շ����������ļ���������
					if(message.isToALL()){  //��������
						sendMsgToAll(message,Head.fileHead);
					}else{
						sendMsgToUser(message,message.getToUser(),Head.fileHead);
					}
				}else{
					sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+Const.MSG_VALUE_FILE_SERVER_ISCLOSED);  //�ļ�����������ʧ��
				}

		}else if(message.getStatus()==FileMessage.STATUS_ENDSEND_FILE){ //�ļ����ͽ���
			
			//�رմ��ͷ���Socket

			
		}else if(message.getStatus()==FileMessage.STATUS_ENDRECEIVE_FILE){ //�ļ����ս���
			
			//�رս��շ���Socket

			
		}

	}

	/**
	 * ת���ı���Ϣ��ָ���û�
	 * @param message
	 * @param name
	 */
	private void sendTextMsgToUser(TextMessage message, String name)  {
		// TODO Auto-generated method stub
		//sendTextMessage(this.client,message);
		ClientInformation toClient = this.server.findClient(name);
		if(toClient!=null){
			sendTextMessage(toClient,message);
		}else{
			sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+name+Const.MSG_VALUE_CLIENT_NOTEXIST);
		}

	}
	


	/**
	 * ת���ı���Ϣ�������û�
	 * @param message
	 */
	private void sendTextMsgToAll(TextMessage message)  {
		// TODO Auto-generated method stub
		//sendTextMessage(this.client,message);
		Vector<String>clientNameList = this.server.getClientNameList();
		int size = clientNameList.size();
		for(int i=0;i<size;i++){
			String name = clientNameList.get(i);
			if(this.client.getName().equalsIgnoreCase(name)){
				continue;
			}
			ClientInformation toClient = this.server.findClient(name);
			if(toClient!=null){
				sendTextMessage(toClient,message);
			}else{
				sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+name+Const.MSG_VALUE_CLIENT_NOTEXIST);
			}
		}
	}

	/**
	 * ����ϵͳTextMessage��Ϣ
	 * @param client
	 * @param meesage
	 */
	private void sendSystemTextMessage(ClientInformation client,String meesage){
		TextMessage  tm = new TextMessage();
		tm.setToALL(false);
		tm.setFromUser("");
		tm.setToUser(this.client.getName());
		tm.setColor(Const.MESSAGE_COLOR_SYSTEM);
		tm.setMessage(meesage);
		sendTextMessage(client,tm);
	}
	
	/**
	 * ת��ָ���ͻ���TextMessage��Ϣ
	 * @param client
	 * @param message
	 */
	public void sendTextMessage(ClientInformation client,TextMessage message) {
		client.sendObject(Head.textHead);
		client.sendObject(message);
	}
	
	
	/**
	 * ת��ͼƬ��Ϣ��ָ���û�
	 * @param message
	 * @param name
	 */
	private void sendImageMsgToUser(ImageMessage message, String name)  {
		// TODO Auto-generated method stub
		//sendImageMessage(this.client,message);
		ClientInformation toClient = this.server.findClient(name);
		if(toClient!=null){
			sendImageMessage(toClient,message);
		}else{
			sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+name+Const.MSG_VALUE_CLIENT_NOTEXIST);
		}

	}
	


	/**
	 * ת��ͼƬ��Ϣ�������û�
	 * @param message
	 */
	private void sendImageMsgToAll(ImageMessage message)  {
		// TODO Auto-generated method stub
		//sendImageMessage(this.client,message);
		Vector<String>clientNameList = this.server.getClientNameList();
		int size = clientNameList.size();
		for(int i=0;i<size;i++){
			String name = clientNameList.get(i);
			if(this.client.getName().equalsIgnoreCase(name)){
				continue;
			}

			ClientInformation toClient = this.server.findClient(name);
			if(toClient!=null){
				sendImageMessage(toClient,message);
			}else{
				sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+name+Const.MSG_VALUE_CLIENT_NOTEXIST);
			}

		}
	}
	
	/**
	 * ת��ImageMessage��Ϣ
	 * @param client
	 * @param message
	 */
	public void sendImageMessage(ClientInformation client,ImageMessage message) {
		client.sendObject(Head.imageHead);
		client.sendObject(message);
	}
	
	
	
	

	/**
	 * �����ļ����������
	 * @return
	 */
	private boolean startFileServer(){
		try {
			 fileServerSocket = new  ServerSocket(Const.DEFALUT_FILE_PORT);
			 return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	

	/**
	 * ת����Ϣ��ָ���û�
	 * @param message
	 * @param name
	 * @param head
	 */
	private void sendMsgToUser(Message message, String name,Head head )  {
		// TODO Auto-generated method stub
		//sendImageMessage(this.client,message);
		ClientInformation toClient = this.server.findClient(name);
		if(toClient!=null){
			sendMessage(toClient,message,head);
		}else{
			sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+name+Const.MSG_VALUE_CLIENT_NOTEXIST);
		}

	}
	



	/**
	 * ת����Ϣ�������û�
	 * @param message
	 * @param head
	 */
	private void sendMsgToAll(Message message,Head head)  {
		// TODO Auto-generated method stub
		//sendImageMessage(this.client,message);
		Vector<String>clientNameList = this.server.getClientNameList();
		int size = clientNameList.size();
		for(int i=0;i<size;i++){
			String name = clientNameList.get(i);
			if(this.client.getName().equalsIgnoreCase(name)){
				continue;
			}
			ClientInformation toClient = this.server.findClient(name);
			if(toClient!=null){
				sendMessage(toClient,message,head);
			}else{
				sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+name+Const.MSG_VALUE_CLIENT_NOTEXIST);
			}

		}
	}
	

	/**
	 * ת��Message��Ϣ
	 * @param client
	 * @param message
	 * @param head
	 */
	public void sendMessage(ClientInformation client,Message message,Head head) {
		client.sendObject(head);
		client.sendObject(message);
	}
	/**
	 * �رս�������Ҫ��ͻ��˷���������Ϣ)
	 */
	public void close(){
		this.client.sendClientLogoutMessage(); //���Ϳͻ���������Ϣ
	}
	
	/**
	 * �Ƴ��Լ�
	 */
	public void remove(){
		this.server.removeClient(this.client.getName());
	}
	
}
