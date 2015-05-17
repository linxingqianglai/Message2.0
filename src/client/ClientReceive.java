package client;

import head.Head;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import socket.ClientInformation;

import message.FileMessage;
import message.ImageMessage;
import message.TextMessage;

public class ClientReceive extends Thread {
	private Client client ;
	private ClientInformation clientInfor;
	private boolean isClosed;  //关闭状态
	//构造方法中的Client表示对应的上下文环境
	public ClientReceive(Client client,Socket socket) throws IOException{
		this.client =client;
		this.clientInfor = new ClientInformation();
		this.clientInfor.setSocket(socket);
		this.clientInfor.setName(this.client.getUser());
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
	 * 关闭接收 
	 */
	public synchronized void close() {
		this.clientInfor.close();	
	}


	/**
	 * 检查客户端是否已关闭,是返回true，否返回false
	 * @return
	 */
	public synchronized boolean check(){
		if(this.client.isClosed() || this.clientInfor.isColsed()){
			return true;
		}else{
			return false;
		}
	}

	public void run(){

		if(check()){  //客户端界面已退出，则无需启动接收器
			this.setIsClosed(true);
			return ;
		}
		//向服务器发送登录信息
		
		//在刚开始的时候，一定要先向服务端发送登录信息
		/**
		 * 发送失败就关闭客户端，否则就开始启动循环监听从服务端过来的信息
		 */
		if(!this.clientInfor.sendLoginMessage()){
			System.out.println("登录服务器失败");
			this.clientInfor.close();
			this.setIsClosed(true);
			return;
		}


		while(true){
			if(check()){  //检查客户端是否已关闭
				this.setIsClosed(true);
				break;
			}

			Head head = this.clientInfor.readHeadObject();   //读取消息头
			if(check()){  //检查客户端是否已关闭
				this.setIsClosed(true);
				break;
			}
			
			if(head!=null){
				if(Head.isCommonMessage(head)){  //普通通讯消息
					if(!commonMessageHandle(head)){  //普通消息处理，处理失败结束消息接收器
						 break;
					}
				}else if(Head.isSystemMessage(head)){ //系统通讯消息
					if(!systemMessageHandle(head)){
						break;
					}	
				}
			}
			
			if( check()){
				this.setIsClosed(true);
				break;
			}
		}
		
		//关闭接受器
		this.close();
		this.client.setLoginStatr(false);
	
	}

	

	/**
	 * 普通消息的处理，处理成功返回true，失败返回false。通讯异常返回false，关闭接收器。
	 * @param head
	 * @return
	 */
	private boolean commonMessageHandle(Head head){
		if(Head.isTextMessage(head)){  //消息内容为文本消息
			TextMessage message = this.clientInfor.reaadTextMessageObject();
			if(check()){  //检查客户端是否已关闭
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){  //读取成功
				this.client.addTextMessage(message,true);
			}
			
		}else if(Head.isImageMessage(head)){//消息内容为图片消息
			ImageMessage message =this.clientInfor.reaadImageMessageObject();
			if(check()){  //检查客户端是否已关闭
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){
				this.client.addImageMessage(message,true);
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
			if(Head.isUserListMessage(head)){  //更新用户列表消息
				Object obj = this.clientInfor.readObject();
				if(check()){  //检查客户端是否已关闭
					this.setIsClosed(true);
					return false;
				}
				if(obj!=null){ //读取成功
					if(obj instanceof Vector){
						Vector<String> v = (Vector<String>)obj;
						this.client.setClientList(v);
					}
				}

			}else if(Head.isClientLogoutMessage(head)){  //客户端可以下线消息
				System.out.println("收到客户端可以下线消息");
				this.setIsClosed(true);
				return false;
				
			}else if(Head.isServerCloseMessage(head)){  //服务器关闭消息
				System.out.println("收到服务器关闭消息");
				return logout();
			}

			return true;
	}

	/**
	 * 在发对应的消息的时候要加上对应的头文件，这样子服务端才能
	 * 识别所对应的消息是属于哪块内容，否则服务端不知道需要将消息发给服务端
	 * 还是对应的用户
	 * 发送文本信息
	 * @param message
	 * @throws IOException
	 */
	public void sendTextMessage(TextMessage message) throws IOException{
		if(this.clientInfor.sendObject(Head.textHead)) { //信息头为TextMessage消息
			this.clientInfor.sendObject(message);
		}
	}

	/**
	 * 发送图片消息
	 * @param image
	 * @throws IOException 
	 */
	public void sendImageMessage(ImageMessage message) throws IOException{
		if(this.clientInfor.sendObject(Head.imageHead)) { //信息头为ImageMessage消息
			this.clientInfor.sendObject(message);
		}
	}

	
	/**
	 * 发送文件消息
	 * @param image
	 * @throws IOException 
	 */
	public void sendFileMessage(FileMessage message) throws IOException{
		if(this.clientInfor.sendObject(Head.fileHead)) { //信息头为FileMessage消息
			this.clientInfor.sendObject(message);
		}
	}
	
	
	/**
	 * 发送客户端注销消息
	 * @return
	 */
	public boolean logout(){
		if(!this.clientInfor.sendClientLogoutMessage()){
			return false;
		}else{
			return true;
		}
	}






}
