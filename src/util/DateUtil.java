package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ���ڹ�����
 * @author itprogrammer
 *
 */
public class DateUtil {
	/**
	 * ���ص�ǰϵͳ����
	 * @return
	 */
	public static  String getCurrentDate(String format){
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//�������ڸ�ʽ
			return df.format(new Date());
	}
}
