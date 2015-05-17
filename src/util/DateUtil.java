package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author itprogrammer
 *
 */
public class DateUtil {
	/**
	 * 返回当前系统日期
	 * @return
	 */
	public static  String getCurrentDate(String format){
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
			return df.format(new Date());
	}
}
