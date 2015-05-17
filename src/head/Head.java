package head;

import java.io.Serializable;

import constpage.Const;

/**
 * ��Ϣͷ
 * @author itprogrammer
 *
 */
public class Head  implements Serializable{
		/**
		 * ��ͨ��Ϣ-�ı���Ϣ����
		 * transient����������ʱ������Ҳ�������л���ʱ�򲻻ᱻ�洢����
		 */
		public static final transient Head  textHead = new Head(Const.TYPE_HEAD_COMMON_MESSAGE,Const.MSG_TYP_TEXT);
		/**
		 * ��ͨ��Ϣ- ͼƬ��Ϣ����
		 */
		public static final transient Head imageHead = new Head(Const.TYPE_HEAD_COMMON_MESSAGE,Const.MSG_TYP_IMAGE);
		
		/**
		 * ��ͨ��Ϣ- �ļ���Ϣ����
		 */
		public static final transient Head fileHead = new Head(Const.TYPE_HEAD_COMMON_MESSAGE,Const.MSG_TYP_FILE);
		
		/**
		 * ϵͳ��Ϣ-�û��б���Ϣ
		 */
		public static final transient Head userListHead = new Head(Const.TYPE_HEAD_SYSTEM_MESSAGE,Const.MSG_TYP_USERLIST);
		
		
		/**
		 * ϵͳ��Ϣ-�ͻ���������Ϣ
		 */
		public static final transient Head clientLogoutHead = new Head(Const.TYPE_HEAD_SYSTEM_MESSAGE,Const.MSG_TYP_CLIENTLOGOUT);
		
		/**
		 * �������ر���Ϣ
		 */
		public static final transient Head serverCloseHead = new Head(Const.TYPE_HEAD_SYSTEM_MESSAGE,Const.MSG_TYP_SERVERCLOSE);
	
		String headType ;
		String messageType;
		public Head(){
			this.headType = "";
			this.messageType="";
		}
		
		public Head(String headType,String messageType){
			this.headType =headType;
			this.messageType=messageType;
		}
		public String getHeadType() {
			return headType;
		}
		public void setHeadType(String headType) {
			this.headType = headType;
		}
		public String getMessageType() {
			return messageType;
		}
		public void setMessageType(String messageType) {
			this.messageType = messageType;
		}
		
	

		/**
		 * �ж���Ϣͷ�Ƿ�Ϊ��ͨ��Ϣ
		 * @param head
		 * @return
		 */
		public static boolean isCommonMessage(Head head){
				if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_COMMON_MESSAGE)){
					return true;
				}else{
					return false;
				}
		}
		
		/**
		 * �ж���Ϣͷ�Ƿ�Ϊϵͳ��Ϣ
		 * @param head
		 * @return
		 */
		public static boolean isSystemMessage(Head head){
			if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_SYSTEM_MESSAGE)){
				return true;
			}else{
				return false;
			}
	}
		
		/**
		 * �ж���Ϣͷ�Ƿ�Ϊ��ͨ�ı���Ϣ
		 * @param head
		 * @return
		 */
		public static boolean  isTextMessage(Head head){
			if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_COMMON_MESSAGE) 
					&& head.getMessageType().equalsIgnoreCase(Const.MSG_TYP_TEXT)){
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * �ж���Ϣͷ�Ƿ�Ϊ��ͨͼƬ��Ϣ
		 * @param head
		 * @return
		 */
		public static boolean  isImageMessage(Head head){
			if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_COMMON_MESSAGE) 
					&& head.getMessageType().equalsIgnoreCase(Const.MSG_TYP_IMAGE)){
				return true;
			}else{
				return false;
			}
		}
		
		public static boolean isFileMessage(Head head){
			if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_COMMON_MESSAGE) 
					&& head.getMessageType().equalsIgnoreCase(Const.MSG_TYP_FILE)){
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * �ж���Ϣͷ�Ƿ�Ϊϵͳ��Ϣ-�û��б���Ϣ
		 * @param head
		 * @return
		 */
		public static boolean  isUserListMessage(Head head){
			if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_SYSTEM_MESSAGE) 
					&& head.getMessageType().equalsIgnoreCase(Const.MSG_TYP_USERLIST)){
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * �ж���Ϣͷ�Ƿ�Ϊϵͳ��Ϣ-�û�ע����Ϣ
		 * @param head
		 * @return
		 */
		public static boolean  isClientLogoutMessage(Head head){
			if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_SYSTEM_MESSAGE) 
					&& head.getMessageType().equalsIgnoreCase(Const.MSG_TYP_CLIENTLOGOUT)){
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * �ж���Ϣͷ�Ƿ�Ϊϵͳ��Ϣ-�������ر���Ϣ
		 * @param head
		 * @return
		 */
		public static boolean  isServerCloseMessage(Head head){
			if(head.getHeadType().equalsIgnoreCase(Const.TYPE_HEAD_SYSTEM_MESSAGE) 
					&& head.getMessageType().equalsIgnoreCase(Const.MSG_TYP_SERVERCLOSE)){
				return true;
			}else{
				return false;
			}
		}
}
