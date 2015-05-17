package constpage;

import java.awt.Color;

/**
 * ������
 * @author itprogrammer
 *
 */
public class Const {
	/**
	 * ��Ϣͷ����Ϊ��ͨ��Ϣ
	 */
	public static final String TYPE_HEAD_COMMON_MESSAGE = "MESSAGE";
	
	/**
	 * ��Ϣͷ����Ϊϵͳ��Ϣ
	 */
	public static final String TYPE_HEAD_SYSTEM_MESSAGE = "SYSTEMMESSAGE" ;

	
	/**
	 * �����û�-������
	 */
	public static final String TOUSER_ALL = "all";
	/**
	 * ��Ϣ��������ΪTextMessage
	 */
	public static final String MSG_TYP_TEXT = "TEXT";
	/**
	 * ��Ϣ��������ΪImageMessage
	 */
	public static final String MSG_TYP_IMAGE="IMAGE";
	
	
	/**
	 * ��Ϣ��������ΪFileMessage
	 */
	public static final String MSG_TYP_FILE = "FILE";
	
	/**
	 *  ��Ϣ��������Ϊ�ͻ�������������Ϣ������˷���Ϊ�ͻ��˿������ߣ��ͻ��˷�����Ϊ�ͻ����������ߣ�
	 */
	public static final String MSG_TYP_CLIENTLOGOUT = "CLIENTLOGOUT";
	/**
	 * ��Ϣ��������Ϊ�û��б�
	 */
	public static final String MSG_TYP_USERLIST="USERLIST";
	/**
	 *  ��Ϣ��������Ϊ�������ر���Ϣ
	 */
	public static final String MSG_TYP_SERVERCLOSE = "SERVERCLOSE";
	
	
	public static final String MSG_VALUE_CLIENT_NOTEXIST="User not exist";
	
	public static final String MSG_VALUE_CLIENT_LOGOUT="User logoff";
	
	public static final String MSG_VALUE_SERVER_CLOSED="Server closed";
	
	public static final String MSG_VALUE_FILE_SERVER_ISCLOSED = "File server closed";
	
	/**
	 * ϵͳ��Ϣ�����û�
	 */
	public static final String MSG_FROM_USR_SYSTEM= "System";
	
	/**
	 * ��Ϣ��Ϣǰ׺
	 */
	public static final String MSG_VALUE_PREFIX_SYSTEM="[System message]";
	
	/**
	 * Ĭ����Ϣ�˿�
	 */
	public final static int DEFAULT_MESSAGE_PORT=47382;
	
	/**
	 * Ĭ���ļ�����˿�
	 */
	public final static int DEFALUT_FILE_PORT = 47383 ;
	
	
	/**
	 *  Ĭ����Ϣ��ɫ
	 */
	public final static Color DEFALUT_MESSAGE_COLOR = Color.black;
	/**
	 *  ϵͳ��Ϣ��ɫ
	 */
	public final static Color MESSAGE_COLOR_SYSTEM=Color.red;
	
	/**
	 * Image ���ش����ʱĿ¼
	 */
	public final static String IMAGE_TEMP_PATH = "./TEMPIMAGE";
	
	/**
	 *  Ĭ�����ڸ�ʽ
	 */
	public final static String DEFALUT_DATE_FORMA="HH:mm:ss";
	
	/**
	 * Image ����ʱĬ���ļ���׺��
	 */
	public final static String DEFALUT_IMAGE_SAVF_POSTFIX=".png";
	
	
	/**
	 * ��Ϣ��ش��Ŀ¼
	 */
	public final static String MESSAGE_SAVF_PATH="./TEMPMESSAGE";
	/**
	 * Ĭ������ļ���
	 */
	public final static String DEFALUT_MESSAGE_SAVF_FILE="message.html";
	/**
	 * Ĭ��IP��ַ�Ǳ���
	 */

	
	
	
	
}
