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
	private boolean isClosed;//状态是否已关闭
	private ServerSocket fileServerSocket ;
	public Receive(ClientInformation client,Listen listen,Server server){
		this.client = client;
		this.server = server;
		this.listen =listen;
		this.fileServerSocket = null ;
		this.setIsClosed(false);
	}

	/**
	 * 设置关闭状态
	 * @param isClosed
	 */
	private synchronized void setIsClosed(boolean isClosed){
		this.isClosed=isClosed;
	}

	/**
	 * 返回是否已关闭
	 * @return
	 */
	public synchronized boolean isClosed(){
		return this.isClosed;
	}


	/**
	 * 检查服务是否已关闭,是返回true，否返回false
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
		
		System.out.println(this.client.getName()+"客户端已上线");

		while(true){
			if(checkServer()){
				this.setIsClosed(true);
				break;
			}
			Head head = this.client.readHeadObject();   //读取消息头
			if(checkServer()){  //检查客户端是否已关闭
				this.setIsClosed(true);
				break;
			}
			
			if(head!=null){
				if(Head.isCommonMessage(head)){  //普通通讯消息
					if(!commonMessageHandle(head)){  //普通消息处理，处理失败结束消息接收器
						 break;
					}
				}else if(Head.isSystemMessage(head)){ //系统通讯消息，处理失败结束消息接收器
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

		//停止
		this.client.close();
		this.setIsClosed(true); //客户端状态置为关闭
		System.out.println("客户端接受器"+this.getName()+"已关闭");
	}

	/**
	 * 普通消息的处理，处理成功返回true，失败返回false。通讯异常返回false，关闭接收器。
	 * @param head
	 * @return
	 */
	private boolean commonMessageHandle(Head head){
		if(Head.isTextMessage(head)){  //消息内容为文本消息
			TextMessage message = this.client.reaadTextMessageObject();
			if(checkServer()){  //检查是否已关闭
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){  //读取成功
				sendTextMessage(message);
			}
			
		}else if(Head.isImageMessage(head)){//消息内容为图片消息
			ImageMessage message =this.client.reaadImageMessageObject();
			if(checkServer()){  
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){
				sendImageMessage(message);
			}
			
		}else if(Head.isFileMessage(head)){//消息内容为文件消息
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
	 * 系统消息的处理，处理成功返回true，失败返回false。通讯异常返回false，关闭接收器。
	 * @param head
	 * @return
	 */
	private boolean systemMessageHandle(Head head){
			if(Head.isClientLogoutMessage(head)){  //客户端可以下线消息
				System.out.println("收到客户端申请下线消息");
				this.close();
				return false;
			}
			return true;
	}
	

	/**
	 * 转发文本消息
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
	 * 转发图片消息
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
	 * 转发文件消息
	 * @param message
	 */
	private  void sendFileMessage(FileMessage message){
		if(message.getStatus()==FileMessage.STATUS_START_SERVER){  //启动文件传输
				if(startFileServer()){
					message.setStatus(FileMessage.STATUS_SEND_FILE);
					sendMessage(this.client, message, Head.fileHead); //给发送方发送启动文件传送请求
					message.setStatus(FileMessage.STATU_RECEIVE_FILE); //给接收方发送启动文件接受请求
					if(message.isToALL()){  //发所有人
						sendMsgToAll(message,Head.fileHead);
					}else{
						sendMsgToUser(message,message.getToUser(),Head.fileHead);
					}
				}else{
					sendSystemTextMessage(this.client,Const.MSG_VALUE_PREFIX_SYSTEM+Const.MSG_VALUE_FILE_SERVER_ISCLOSED);  //文件服务器启动失败
				}

		}else if(message.getStatus()==FileMessage.STATUS_ENDSEND_FILE){ //文件传送结束
			
			//关闭传送方的Socket

			
		}else if(message.getStatus()==FileMessage.STATUS_ENDRECEIVE_FILE){ //文件接收结束
			
			//关闭接收方的Socket

			
		}

	}

	/**
	 * 转发文本信息至指定用户
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
	 * 转发文本信息至所有用户
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
	 * 发送系统TextMessage消息
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
	 * 转发指定客户端TextMessage消息
	 * @param client
	 * @param message
	 */
	public void sendTextMessage(ClientInformation client,TextMessage message) {
		client.sendObject(Head.textHead);
		client.sendObject(message);
	}
	
	
	/**
	 * 转发图片信息至指定用户
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
	 * 转发图片信息至所有用户
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
	 * 转发ImageMessage消息
	 * @param client
	 * @param message
	 */
	public void sendImageMessage(ClientInformation client,ImageMessage message) {
		client.sendObject(Head.imageHead);
		client.sendObject(message);
	}
	
	
	
	

	/**
	 * 启动文件传输服务器
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
	 * 转发信息至指定用户
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
	 * 转发信息至所有用户
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
	 * 转发Message消息
	 * @param client
	 * @param message
	 * @param head
	 */
	public void sendMessage(ClientInformation client,Message message,Head head) {
		client.sendObject(head);
		client.sendObject(message);
	}
	/**
	 * 关闭接收器（要求客户端发送下线消息)
	 */
	public void close(){
		this.client.sendClientLogoutMessage(); //发送客户端下线消息
	}
	
	/**
	 * 移除自己
	 */
	public void remove(){
		this.server.removeClient(this.client.getName());
	}
	
}
