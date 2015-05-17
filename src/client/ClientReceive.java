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
	private boolean isClosed;  //�ر�״̬
	//���췽���е�Client��ʾ��Ӧ�������Ļ���
	public ClientReceive(Client client,Socket socket) throws IOException{
		this.client =client;
		this.clientInfor = new ClientInformation();
		this.clientInfor.setSocket(socket);
		this.clientInfor.setName(this.client.getUser());
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
	 * �رս��� 
	 */
	public synchronized void close() {
		this.clientInfor.close();	
	}


	/**
	 * ���ͻ����Ƿ��ѹر�,�Ƿ���true���񷵻�false
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

		if(check()){  //�ͻ��˽������˳�������������������
			this.setIsClosed(true);
			return ;
		}
		//����������͵�¼��Ϣ
		
		//�ڸտ�ʼ��ʱ��һ��Ҫ�������˷��͵�¼��Ϣ
		/**
		 * ����ʧ�ܾ͹رտͻ��ˣ�����Ϳ�ʼ����ѭ�������ӷ���˹�������Ϣ
		 */
		if(!this.clientInfor.sendLoginMessage()){
			System.out.println("��¼������ʧ��");
			this.clientInfor.close();
			this.setIsClosed(true);
			return;
		}


		while(true){
			if(check()){  //���ͻ����Ƿ��ѹر�
				this.setIsClosed(true);
				break;
			}

			Head head = this.clientInfor.readHeadObject();   //��ȡ��Ϣͷ
			if(check()){  //���ͻ����Ƿ��ѹر�
				this.setIsClosed(true);
				break;
			}
			
			if(head!=null){
				if(Head.isCommonMessage(head)){  //��ͨͨѶ��Ϣ
					if(!commonMessageHandle(head)){  //��ͨ��Ϣ��������ʧ�ܽ�����Ϣ������
						 break;
					}
				}else if(Head.isSystemMessage(head)){ //ϵͳͨѶ��Ϣ
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
		
		//�رս�����
		this.close();
		this.client.setLoginStatr(false);
	
	}

	

	/**
	 * ��ͨ��Ϣ�Ĵ�������ɹ�����true��ʧ�ܷ���false��ͨѶ�쳣����false���رս�������
	 * @param head
	 * @return
	 */
	private boolean commonMessageHandle(Head head){
		if(Head.isTextMessage(head)){  //��Ϣ����Ϊ�ı���Ϣ
			TextMessage message = this.clientInfor.reaadTextMessageObject();
			if(check()){  //���ͻ����Ƿ��ѹر�
				this.setIsClosed(true);
				return false;
			}
			if(message!=null){  //��ȡ�ɹ�
				this.client.addTextMessage(message,true);
			}
			
		}else if(Head.isImageMessage(head)){//��Ϣ����ΪͼƬ��Ϣ
			ImageMessage message =this.clientInfor.reaadImageMessageObject();
			if(check()){  //���ͻ����Ƿ��ѹر�
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
	 * ϵͳ��Ϣ�Ĵ�������ɹ�����true��ʧ�ܷ���false��ͨѶ�쳣����false���رս�������
	 * @param head
	 * @return
	 */
	private boolean systemMessageHandle(Head head){
			if(Head.isUserListMessage(head)){  //�����û��б���Ϣ
				Object obj = this.clientInfor.readObject();
				if(check()){  //���ͻ����Ƿ��ѹر�
					this.setIsClosed(true);
					return false;
				}
				if(obj!=null){ //��ȡ�ɹ�
					if(obj instanceof Vector){
						Vector<String> v = (Vector<String>)obj;
						this.client.setClientList(v);
					}
				}

			}else if(Head.isClientLogoutMessage(head)){  //�ͻ��˿���������Ϣ
				System.out.println("�յ��ͻ��˿���������Ϣ");
				this.setIsClosed(true);
				return false;
				
			}else if(Head.isServerCloseMessage(head)){  //�������ر���Ϣ
				System.out.println("�յ��������ر���Ϣ");
				return logout();
			}

			return true;
	}

	/**
	 * �ڷ���Ӧ����Ϣ��ʱ��Ҫ���϶�Ӧ��ͷ�ļ��������ӷ���˲���
	 * ʶ������Ӧ����Ϣ�������Ŀ����ݣ��������˲�֪����Ҫ����Ϣ���������
	 * ���Ƕ�Ӧ���û�
	 * �����ı���Ϣ
	 * @param message
	 * @throws IOException
	 */
	public void sendTextMessage(TextMessage message) throws IOException{
		if(this.clientInfor.sendObject(Head.textHead)) { //��ϢͷΪTextMessage��Ϣ
			this.clientInfor.sendObject(message);
		}
	}

	/**
	 * ����ͼƬ��Ϣ
	 * @param image
	 * @throws IOException 
	 */
	public void sendImageMessage(ImageMessage message) throws IOException{
		if(this.clientInfor.sendObject(Head.imageHead)) { //��ϢͷΪImageMessage��Ϣ
			this.clientInfor.sendObject(message);
		}
	}

	
	/**
	 * �����ļ���Ϣ
	 * @param image
	 * @throws IOException 
	 */
	public void sendFileMessage(FileMessage message) throws IOException{
		if(this.clientInfor.sendObject(Head.fileHead)) { //��ϢͷΪFileMessage��Ϣ
			this.clientInfor.sendObject(message);
		}
	}
	
	
	/**
	 * ���Ϳͻ���ע����Ϣ
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
