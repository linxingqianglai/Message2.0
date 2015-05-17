package server;

import head.Head;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import socket.ClientInformation;

import message.TextMessage;


import constpage.Const;

public class Listen extends Thread {
	private Server server;
	private boolean  isStop; //停止标志
	private List<Receive> clientReceiveList;//表示客户端监听类集合
	private boolean isClosed ; //状态是否关闭,默认是false，表示未关闭
	
	public Listen(Server server){
		this.server=server;
		this.setIsClosed(false);
		this.open();
		this.clientReceiveList = new ArrayList<Receive>();
	}

	/**
	 * 方法成对存在
	 */
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
	 * 打开服务守护进程
	 */
	private synchronized void open(){
		this.isStop =false;
	}
	
	/**
	 * 停止侦听
	 */
	public synchronized void setStop(){
		this.isStop = true;
	}
	/**
	 * 是否停止
	 * @return
	 */
	private synchronized boolean isStop(){
		return this.isStop;
	}

	/**
	 * 检查服务是否已关闭,是返回true，否返回false
	 * @return
	 */
	public synchronized boolean checkServer(){
		if(this.server.isClosed()){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
			
			while(true){   //侦听客户端连接守护进程
				if(isStop() || checkServer()){
					break;
				}
				
				try {
					
					Socket s = this.server.getSocket();
					ClientInformation  client = new ClientInformation();
					client.setSocket(s);  //获取客户端连接
					String name = client.readLoginMessage(); //读取登录信息
					if(client.isColsed()){ //读取操作失败则不创建连接
						client.close();
						continue;
					}
					
					if(name==null){//未读取到内容，则登录失败，不创建连接
						client.close();
						continue;
					}
					//创建连接
					client.setName(name);
					this.server.addClient(client);
					sendUserList();   //客户端进入时，更新所有客户端的用户列表
					Receive re = new Receive(client,this,this.server); 
					this.clientReceiveList.add(re);
					re.start();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				}

				removeClosedReceive(); //移除已关闭的客户端接收器

				if(isStop() || checkServer()){
					break;
				}
			}

			this.close();  //关闭所有连接
			this.setIsClosed(true);

	}

	
	/**
	 * 关闭所有连接，
	 * 服务器关闭要向客户报告
	 */
	private synchronized void close(){
			//向所有客户端发送服务器关闭消息
			sendServerCloseMessage();
			closeAllReceive(); //关闭所有的收发端
	}
	
	/**
	 * 发送服务器关闭消息给所有客户端
	 * @throws IOException
	 */
	private void sendServerCloseMessage() {
		
			Vector<String>clientNameList = this.server.getClientNameList();
			int size = clientNameList.size();
			for(int i=0;i<size;i++){
				String name = clientNameList.get(i);
				ClientInformation toClient = this.server.findClient(name);
				if(toClient!=null){
					try {
						toClient.sendObject(Head.textHead); 
						TextMessage  tm = new TextMessage();
						tm.setColor(Const.MESSAGE_COLOR_SYSTEM);
						tm.setMessage(Const.MSG_VALUE_PREFIX_SYSTEM+Const.MSG_VALUE_SERVER_CLOSED);
						toClient.sendObject(tm);
						System.out.println("已向"+toClient.getName()+"发送服务器关闭");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

	}
	
	/**
	 * 发送用户列表给所有用户
	 * @throws IOException
	 */
	public synchronized void sendUserList() {
		// TODO Auto-generated method stub
		Vector<String>clientNameList = this.server.getClientNameList();
		int size = clientNameList.size();
		System.out.println(size);
		for(int i=0;i<size;i++){
			String name = clientNameList.get(i);
			System.out.println(name);
			ClientInformation toClient = this.server.findClient(name);
			if(toClient!=null){
				
					System.out.println("客户端有变更，给"+toClient.getName()+"发送新的客户端列表");
					System.out.println("Size："+this.server.getClientNameList().size()+"内容："+this.server.getClientNameList());
					toClient.sendObject(Head.userListHead); //写消息头
					toClient.sendObject(this.server.getClientNameList().clone());
			}

		}

	}
	
	/**
	 * 关闭所有的客户端接收器
	 * @return
	 */
	private void closeAllReceive(){
		int size =  this.clientReceiveList.size();
		for(int i=0;i<size;i++){
			Receive r = this.clientReceiveList.get(i);
			try {
				r.close();  //关闭接收器守护进程
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


	}
	
	/**
	 * 移除已关闭的接收器
	 */
	private synchronized void removeClosedReceive(){
		int size =  this.clientReceiveList.size();
		for(int i=0;i<size;i++){
			Receive r = this.clientReceiveList.get(i);
			if(r.isClosed()){
				this.clientReceiveList.remove(r);
				r.remove();
				//移除的时候要通知客户端
				sendUserList();
				
			}
			
		}
		 
	}
	

	
}
