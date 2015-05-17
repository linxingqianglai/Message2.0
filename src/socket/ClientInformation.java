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
 * Socket 客户端对象
 * 这里作为一个Socket对象，里面包含了Socket对象的名字。这个是刚开始建立的。
 * 还有对应的输入输出流。作为读写的流。
 * 还有一个判断当前的Socket是否已经关闭。
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
	 * 设置Socket
	 * @param socket
	 * @throws IOException
	 */
	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.socket.setSoTimeout(10000); //设置阻塞超时为10秒
		this.output =  new ObjectOutputStream(this.socket.getOutputStream());
		this.input  =  new ObjectInputStream(this.socket.getInputStream());
	}

	/**
	 * 刷新输出流,刷新之前状态必须未关闭<br/>
	 * 刷新失败置状态为关闭
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
			//这里做异常处理
			this.setColse(true);
		}
		
	}



	/**
	 * 	写对象，写之前状态必须未关闭<br/>
	 *	写失败置状态为关闭
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
	 * 读取对象，读取前状态必须未关闭<br/>
	 * 读取成功返回Object，已关闭或超时返回null，失败返回null并置状态为关闭
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
	 * 读取字符串对象
	 * @return
	 */
	public  String readString()  {
		if(this.isColsed()){
			return null;
		}
		return (String)this.readObject();
	}
	
	/**
	 * 读取TextMessage对象
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
	 * 读取ImageMessage对象
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
	 * 读取FileMessage对象
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
	 * 读取消息头对象
	 * @return
	 */
	public  Head readHeadObject() {
		if(this.isColsed()){
			return null;
		}
		return (Head)this.readObject();
		
	}

	/**
	 * 关闭Socket
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
	 * 发送登录消息
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
	 * 读取登录消息
	 * @return
	 */
	public  String readLoginMessage(){
		if(this.isColsed()){
			return null ;
		}
		return this.readString();  
		
	}
	
	/**
	 * 发送客户端下线消息
	 * @return
	 */
	public  boolean sendClientLogoutMessage(){
		if(this.isColsed()){
			return  false;
		}
		
		this.writeObject(Head.clientLogoutHead);  //信息为客户端下线消息
		this.outputFlush();
		System.out.println("发送客户端下线消息");
		if(this.isColsed()){
			return false;
		}else{
			return true;
		}
		
	}

	/**
	 * 发送对象
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
