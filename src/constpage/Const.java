package constpage;

import java.awt.Color;

/**
 * 常量类
 * @author itprogrammer
 *
 */
public class Const {
	/**
	 * 消息头类型为普通消息
	 */
	public static final String TYPE_HEAD_COMMON_MESSAGE = "MESSAGE";
	
	/**
	 * 消息头类型为系统消息
	 */
	public static final String TYPE_HEAD_SYSTEM_MESSAGE = "SYSTEMMESSAGE" ;

	
	/**
	 * 接收用户-所有人
	 */
	public static final String TOUSER_ALL = "all";
	/**
	 * 消息内容类型为TextMessage
	 */
	public static final String MSG_TYP_TEXT = "TEXT";
	/**
	 * 消息内容类型为ImageMessage
	 */
	public static final String MSG_TYP_IMAGE="IMAGE";
	
	
	/**
	 * 消息内容类型为FileMessage
	 */
	public static final String MSG_TYP_FILE = "FILE";
	
	/**
	 *  消息内容类型为客户端请求下线消息（服务端发送为客户端可以下线，客户端发送则为客户端请求下线）
	 */
	public static final String MSG_TYP_CLIENTLOGOUT = "CLIENTLOGOUT";
	/**
	 * 消息内容类型为用户列表
	 */
	public static final String MSG_TYP_USERLIST="USERLIST";
	/**
	 *  消息内容类型为服务器关闭消息
	 */
	public static final String MSG_TYP_SERVERCLOSE = "SERVERCLOSE";
	
	
	public static final String MSG_VALUE_CLIENT_NOTEXIST="User not exist";
	
	public static final String MSG_VALUE_CLIENT_LOGOUT="User logoff";
	
	public static final String MSG_VALUE_SERVER_CLOSED="Server closed";
	
	public static final String MSG_VALUE_FILE_SERVER_ISCLOSED = "File server closed";
	
	/**
	 * 系统消息发送用户
	 */
	public static final String MSG_FROM_USR_SYSTEM= "System";
	
	/**
	 * 消息消息前缀
	 */
	public static final String MSG_VALUE_PREFIX_SYSTEM="[System message]";
	
	/**
	 * 默认消息端口
	 */
	public final static int DEFAULT_MESSAGE_PORT=47382;
	
	/**
	 * 默认文件传输端口
	 */
	public final static int DEFALUT_FILE_PORT = 47383 ;
	
	
	/**
	 *  默认消息颜色
	 */
	public final static Color DEFALUT_MESSAGE_COLOR = Color.black;
	/**
	 *  系统消息颜色
	 */
	public final static Color MESSAGE_COLOR_SYSTEM=Color.red;
	
	/**
	 * Image 本地存放临时目录
	 */
	public final static String IMAGE_TEMP_PATH = "./TEMPIMAGE";
	
	/**
	 *  默认日期格式
	 */
	public final static String DEFALUT_DATE_FORMA="HH:mm:ss";
	
	/**
	 * Image 保存时默认文件后缀名
	 */
	public final static String DEFALUT_IMAGE_SAVF_POSTFIX=".png";
	
	
	/**
	 * 消息落地存放目录
	 */
	public final static String MESSAGE_SAVF_PATH="./TEMPMESSAGE";
	/**
	 * 默认落地文件名
	 */
	public final static String DEFALUT_MESSAGE_SAVF_FILE="message.html";
	/**
	 * 默认IP地址是本机
	 */

	
	
	
	
}
