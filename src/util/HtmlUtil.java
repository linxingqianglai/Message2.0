/*******************************************************************************
 * $Header$
 * $Revision$
 * UYBXML
 * ${date} ����01:40:47Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2012-3-2
 *******************************************************************************/


package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;


/**
 * HtmlUtil HTML������
 *
 * @author UYBXML 
 */
/*
 * �޸���ʷ
 * $Log$ 
 */
public class HtmlUtil {
	/**
	 * Comment for <code>�ؼ����б�</code>
	 */
	private static final String[] KEYCHAR = 
	{"ENDIF","ELSEIF","IF","ELSE",
	"BEGSR","ENDSR","EXSR",
	"DOW","ENDDO","DOU",
	"SELECT","WHEN","OTHER","ENDSL",
	"SETLL","SETGT",
	"CHAIN","READ","READE","READP","UPDATE","WRITE",
	//"%EOF","%FOUND","%OPEN","%SCAN","%TRIM","%SUBST","%DATE",
	"EXFMT","CLEAR","RESET","SORTA","RETURN",
	"LEAVESR","LEAVE","ITER",
	"AND","OR","NOT",
	"OPEN","CLOSE",
	"/END-FREE","/FREE","/COPY","/INCLUDE",
	"EXEC SQL","EXECUTE","IMMEDIATE",
	"EVAL","CALLP","CALLE","CALL",
	"*BLANK","*LOVAL","*HIVAL"};
	

	public static void HtmlToText(String htmlFile,String textFile,String encoding){
		//��ȡHtml�ļ�
		File html = new File(htmlFile);
		//����ļ��Ƿ���ڼ��Ƿ�Ϊ�ļ���
		if(html.exists() && html.isFile()){
			
	        String str = readerFile(htmlFile,encoding);  
	        StringBuffer buff = new StringBuffer(); 
	        
	        int maxindex = str.length() - 1;  
	        int begin = 0;  
	        int end;  
	        // ��ȡ>��<֮�������   
	        while (true) {
	        	begin = str.indexOf('>', begin);
	            end = str.indexOf('<', begin);  
	            if (end - begin > 1) {  
	                buff.append(str.substring(++begin, end));  
	            }
	            if(end<=0){
	            	begin = maxindex;  
	            	break ;
	            }else{
	            	begin = end + 1;  
	            }
	            
	        }  
	        // д���ļ�����   
	        writeFile(textFile,buff.toString(),encoding);


			
		}
	}
	
	public static void TextToHtml(String textFile,String htmlFile,String encoding){
		//��ȡHtml�ļ�
		File text = new File(textFile);
		//����ļ��Ƿ���ڼ��Ƿ�Ϊ�ļ���
		if(text.exists() && text.isFile()){
			
	        //String str = readerFile(textFile,encoding);  
	        StringBuffer buff = new StringBuffer(); 
	        Scanner sc;
			try {
				if("".equals(encoding.trim())){
					sc = new Scanner(new InputStreamReader(new FileInputStream(text)));
				}else{
					sc = new Scanner(new InputStreamReader(new FileInputStream(text),"GB2312"));
				}
				
				buff.append("<html>");
				buff.append("<body>"); 
				buff.append("<table border=\"1\">"); 
		        while (sc.hasNextLine()) {
		        	buff.append(sc.nextLine());
		        	buff.append("</br>");
		        }
		        buff.append("</table>");
				buff.append("</html>");
				buff.append("</body>"); 
		        // д���ļ�����   
		        writeFile(htmlFile,buff.toString(),encoding);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			
		}
		
	}
	

 
	/**
	 * ����ָ���ı����ȡָ�����ļ����ݡ�
	 * @param fileStr �ļ�·��
	 * @param encoding �ļ�����
	 * @return ��ȡ���ַ������ݡ�
	 */
	public static String readerFile(String fileStr,String encoding){
		File file = new File(fileStr);
		//����ļ��Ƿ���ڼ��Ƿ�Ϊ�ļ���
		if(file.exists() && file.isFile()){
			BufferedReader br = null;  
	        StringBuffer sb = new StringBuffer();  


			
			try {
				if("".equals(encoding.trim())){
					br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				}else{
					br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GB2312"));
				}
				
	            String temp = null;  
	            while ((temp = br.readLine()) != null) {  
	                sb.append(temp);  
	                sb.append("\r\n");
	            } 
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return sb.toString();

			
		}
		return "" ;
		
	}
	
	/**
	 *���ָ�����ݵ�ָ���ļ����ļ��������滻���������򴴽���
	 * @param file ����ļ�·��
	 * @param data �ļ�����
	 * @param encoding �ļ�����
	 */
	public static void writeFile(String file,String data,String encoding){
		try {
		 	File text = new File(file);
   		 	if(text.exists()){//�ж��ļ��Ƿ���ڣ��������滻
   		 		text.delete();
   		 		text.createNewFile();
   		 	}
   		 	BufferedWriter bw;
			if("".equals(encoding.trim())){
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(text)));
			}else{
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(text),encoding));
			}
			if(data!=null){
				bw.write(data);
			}else{
				bw.write("");
			}
			bw.flush();
			bw.close();
			
   		 	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * �ַ���תΪHTML�ĵ�
	 * @param str
	 * @return
	 */
	public static Document stringToDocument(String str) {
		if(str!=null){
			StringBuilder buff = new StringBuilder(); 
			buff.append("<html>");
			buff.append("<body>");
			buff.append(str);
			buff.append("</body>");
			buff.append("</html>");
			
   		 	HTMLEditorKit hk = new HTMLEditorKit();
   		 	Document doc = hk.createDefaultDocument();
   		 	StringReader sr = new StringReader(buff.toString());
   		 	//System.out.println(buff.toString());
   		 	try {
				hk.read(sr, doc, 0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   		 	
   		 	return doc ;
			
		}
		return null ;
		
		
	}
	
	/**
	 * �ַ���תΪHtml��ʽ
	 * @param str
	 * @return
	 */
	public static String stringToHtml(String str){
		if(str!=null){
			StringBuffer buff = new StringBuffer();
			int beging = 0 ;
			beging = str.indexOf("*");
			if(beging ==6){ //�ж��Ƿ�Ϊ����ע��
				buff.append("<font color=\"48D1CC\">");
				buff.append(replaceHtmlChar(str));
				buff.append("</font>");
			}else{
				if(str.length()>6){
					String flag = str.toUpperCase().substring(5,6);
					if(flag.equals("D")){
						buff.append(dStringToHtml(str));
					}else if(flag.equals("F")){
						buff.append(fStringToHtml(str));
					}else if(flag.equals("I")){
						buff.append(iStringToHtml(str));
					}else if(flag.equals("C")){
						buff.append(cStringToHtml(str));
					}else{
						buff.append(freeStringToHtml(str));
					}

				}else{
					buff.append(replaceHtmlChar(str));
				}

				
			}
			return buff.toString();
		}else{
			return null ;
		}
		
	}
	
	/**
	 * D��ת������
	 * @param str
	 * @return
	 */
	private static String dStringToHtml(String str){
		return replaceHtmlChar(str);
	}
	
	/**
	 * F��ת������
	 * @param str
	 * @return
	 */
	private static String fStringToHtml(String str){
		return replaceHtmlChar(str);
	}
	
	/**
	 * I ��ת������
	 * @param str
	 * @return
	 */
	private static String iStringToHtml(String str){
		return replaceHtmlChar(str);
	}
	
	/**
	 * C��ת������
	 * @param str
	 * @return
	 */
	private static String cStringToHtml(String str){
		return replaceHtmlChar(str);
	}
	
	
	/**
	 * ���ɸ�ʽת������
	 * @param str
	 * @return
	 */
	private static String freeStringToHtml(String str){
		StringBuffer buff = new StringBuffer();
		int beging = str.indexOf("//"); //�ж��Ƿ�Ϊ����ע��
		if(beging!=-1){
			buff.append(keyChar(str.substring(0, beging)));
			buff.append("<font color=\"48D1CC\">");
			buff.append(replaceHtmlChar(str.substring(beging)));
			buff.append("</font>");
			
		}else{
			
			buff.append(keyChar(str));
		}
		
		return buff.toString();
	}
	
	/**
	 * �ؼ��ָ�ʽ������
	 * @param str
	 * @return
	 */
	public static String keyChar(String str){
		if(str!=null){
			StringBuffer buff = new StringBuffer();
			int begin =0;
			for (int i=0;i<KEYCHAR.length;i++){
				begin = str.toUpperCase().indexOf(" "+KEYCHAR[i]);
				if(begin!=-1){//����ĳָ���ؼ���
					if(!str.toUpperCase().endsWith(" "+KEYCHAR[i])){//��ĩβ�ַ�
						//��ȡ��һ���ַ�������Ƿ�Ϊ�ո���ǿո��򲻴���
						if(!str.substring(begin+KEYCHAR[i].length()+1,begin+KEYCHAR[i].length()+2).equals(" ")){
							continue;
						}
					}
					begin = begin +1 ;
					buff.append(keyChar(str.substring(0,begin)));
					buff.append("<b>");
					buff.append(replaceHtmlChar(str.substring(begin,begin+KEYCHAR[i].length())));
					buff.append("</b>");
					buff.append(keyChar(str.substring(begin+KEYCHAR[i].length())));
					break ;
				}
			}
			if(buff.length()==0){
				buff.append(replaceHtmlChar(str));
			}
			return buff.toString();

		}else{
			return null ;
		}
	}
	/**
	 * �滻�ַ����е�Html�Ĳ�����ʾ�ַ���
	 * @param str
	 * @return
	 */
	public static String replaceHtmlChar(String str){
		if(str!=null){
			return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
			.replaceAll("\"", "&quot;").replaceAll(" ", "&#8194;");
		}else{
			return null ;
		}
	}
	
	
	public static void main(String[] args){
		//HtmlUtil.HtmlToText("test.html", "test.txt", "");
		HtmlUtil.TextToHtml("test.txt", "test.html", "");
		
	}
}
