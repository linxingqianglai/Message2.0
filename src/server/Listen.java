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
	private boolean  isStop; //ֹͣ��־
	private List<Receive> clientReceiveList;//��ʾ�ͻ��˼����༯��
	private boolean isClosed ; //״̬�Ƿ�ر�,Ĭ����false����ʾδ�ر�
	
	public Listen(Server server){
		this.server=server;
		this.setIsClosed(false);
		this.open();
		this.clientReceiveList = new ArrayList<Receive>();
	}

	/**
	 * �����ɶԴ���
	 */
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
	 * �򿪷����ػ�����
	 */
	private synchronized void open(){
		this.isStop =false;
	}
	
	/**
	 * ֹͣ����
	 */
	public synchronized void setStop(){
		this.isStop = true;
	}
	/**
	 * �Ƿ�ֹͣ
	 * @return
	 */
	private synchronized boolean isStop(){
		return this.isStop;
	}

	/**
	 * �������Ƿ��ѹر�,�Ƿ���true���񷵻�false
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
			
			while(true){   //�����ͻ��������ػ�����
				if(isStop() || checkServer()){
					break;
				}
				
				try {
					
					Socket s = this.server.getSocket();
					ClientInformation  client = new ClientInformation();
					client.setSocket(s);  //��ȡ�ͻ�������
					String name = client.readLoginMessage(); //��ȡ��¼��Ϣ
					if(client.isColsed()){ //��ȡ����ʧ���򲻴�������
						client.close();
						continue;
					}
					
					if(name==null){//δ��ȡ�����ݣ����¼ʧ�ܣ�����������
						client.close();
						continue;
					}
					//��������
					client.setName(name);
					this.server.addClient(client);
					sendUserList();   //�ͻ��˽���ʱ���������пͻ��˵��û��б�
					Receive re = new Receive(client,this,this.server); 
					this.clientReceiveList.add(re);
					re.start();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				}

				removeClosedReceive(); //�Ƴ��ѹرյĿͻ��˽�����

				if(isStop() || checkServer()){
					break;
				}
			}

			this.close();  //�ر���������
			this.setIsClosed(true);

	}

	
	/**
	 * �ر��������ӣ�
	 * �������ر�Ҫ��ͻ�����
	 */
	private synchronized void close(){
			//�����пͻ��˷��ͷ������ر���Ϣ
			sendServerCloseMessage();
			closeAllReceive(); //�ر����е��շ���
	}
	
	/**
	 * ���ͷ������ر���Ϣ�����пͻ���
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
						System.out.println("����"+toClient.getName()+"���ͷ������ر�");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

	}
	
	/**
	 * �����û��б�������û�
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
				
					System.out.println("�ͻ����б������"+toClient.getName()+"�����µĿͻ����б�");
					System.out.println("Size��"+this.server.getClientNameList().size()+"���ݣ�"+this.server.getClientNameList());
					toClient.sendObject(Head.userListHead); //д��Ϣͷ
					toClient.sendObject(this.server.getClientNameList().clone());
			}

		}

	}
	
	/**
	 * �ر����еĿͻ��˽�����
	 * @return
	 */
	private void closeAllReceive(){
		int size =  this.clientReceiveList.size();
		for(int i=0;i<size;i++){
			Receive r = this.clientReceiveList.get(i);
			try {
				r.close();  //�رս������ػ�����
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


	}
	
	/**
	 * �Ƴ��ѹرյĽ�����
	 */
	private synchronized void removeClosedReceive(){
		int size =  this.clientReceiveList.size();
		for(int i=0;i<size;i++){
			Receive r = this.clientReceiveList.get(i);
			if(r.isClosed()){
				this.clientReceiveList.remove(r);
				r.remove();
				//�Ƴ���ʱ��Ҫ֪ͨ�ͻ���
				sendUserList();
				
			}
			
		}
		 
	}
	

	
}
