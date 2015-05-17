package head;

import java.io.Serializable;

import constpage.Const;

/**
 * 消息头
 * @author itprogrammer
 *
 */
public class Head  implements Serializable{
		/**
		 * 普通消息-文本消息类型
		 * transient的作用是临时变量，也就是序列化的时候不会被存储下来
		 */
		public static final transient Head  textHead = new Head(Const.TYPE_HEAD_COMMON_MESSAGE,Const.MSG_TYP_TEXT);
		/**
		 * 普通消息- 图片消息类型
		 */
		public static final transient Head imageHead = new Head(Const.TYPE_HEAD_COMMON_MESSAGE,Const.MSG_TYP_IMAGE);
		
		/**
		 * 普通消息- 文件消息类型
		 */
		public static final transient Head fileHead = new Head(Const.TYPE_HEAD_COMMON_MESSAGE,Const.MSG_TYP_FILE);
		
		/**
		 * 系统消息-用户列表消息
		 */
		public static final transient Head userListHead = new Head(Const.TYPE_HEAD_SYSTEM_MESSAGE,Const.MSG_TYP_USERLIST);
		
		
		/**
		 * 系统消息-客户端下线消息
		 */
		public static final transient Head clientLogoutHead = new Head(Const.TYPE_HEAD_SYSTEM_MESSAGE,Const.MSG_TYP_CLIENTLOGOUT);
		
		/**
		 * 服务器关闭消息
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
		 * 判断消息头是否为普通消息
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
		 * 判断消息头是否为系统消息
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
		 * 判断消息头是否为普通文本消息
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
		 * 判断消息头是否为普通图片消息
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
		 * 判断消息头是否为系统消息-用户列表消息
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
		 * 判断消息头是否为系统消息-用户注销消息
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
		 * 判断消息头是否为系统消息-服务器关闭消息
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
