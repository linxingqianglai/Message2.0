package message;

import java.awt.Color;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import util.DateUtil;
import util.HtmlUtil;

import constpage.Const;

/**
 *  ��Ϣ������
 * @author itprogrammer
 *
 */
public abstract class Message implements Externalizable  {
	private String fromUser;// ��Ϣ������
	private boolean toALL;  //���͸�������
	private String toUser;  //��Ϣ������
	private String message; //�ı���Ϣ
	private Color color; //��Ϣ��ɫ
	
	public Message(){
			this.fromUser ="";
			this.toALL=false;
			this.toUser="";
			this.message="";
			this.color=Const.DEFALUT_MESSAGE_COLOR;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isToALL() {
		return toALL;
	}
	public void setToALL(boolean toALL) {
		this.toALL = toALL;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	
	/**
	 * ��ȡ��Ϣ����ɫRGB��ʽֵ
	 * @return
	 */
	public String getStringRGB(){
		return "rgb("+this.color.getRed()+","+this.color.getGreen()+","+this.color.getBlue()+")";
	}


	/**
	 * ��ȡ�ͻ��˷�����Ϣ��ʾ����
	 * @return
	 */
	public String getClientTitle(){
		if(this.toALL){
			return HtmlUtil.replaceHtmlChar("To ["+Const.TOUSER_ALL+"]��");
		}else{
			return HtmlUtil.replaceHtmlChar(" To ["+this.toUser+"]��");
		}
	}
	
	/**
	 *  ��ȡ����˷�����Ϣ��ʾ����
	 * @return
	 */
	public String getServerTitle(){
		if(this.toALL){
			return HtmlUtil.replaceHtmlChar("["+this.fromUser+"] To ["+Const.TOUSER_ALL+"]��");
		}else{
			return HtmlUtil.replaceHtmlChar("["+this.fromUser+"]��");
		}
	}
	
	
	/**
	 * ��ȡ�滻��Html��ʾ��Message
	 * @return
	 */
	public String getReplaceHtmlMessage(){
		return HtmlUtil.replaceHtmlChar(this.message);
	}
	/**
	 * ��ȡĬ��Tilte ��ʽ�����Title 
	 * @param title
	 * @return
	 */
	public static String getDefaultTitlSytle(String title){
		StringBuilder sb =  new StringBuilder();
		sb.append(DateUtil.getCurrentDate(Const.DEFALUT_DATE_FORMA));
		sb.append("  ");
		sb.append("<I>");
		sb.append(title);
		sb.append("</I>");
		return sb.toString();
	}
	
	/**
	 * ��ȡMessage��ʽ�����Message
	 * @return
	 */
	public String getMessageSytle(){
		StringBuilder sb =  new StringBuilder();
		sb.append("<font color=\""+this.getStringRGB()+"\">");
		sb.append(this.getReplaceHtmlMessage());
		sb.append("</font><br/>");
		return sb.toString();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeObject(this.getFromUser());
		out.writeObject(this.isToALL());
		out.writeObject(this.getToUser());
		out.writeObject(this.getMessage());
		out.writeObject(this.getColor());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		this.setFromUser((String)in.readObject());
		this.setToALL(((Boolean)in.readObject()).booleanValue());
		this.setToUser((String)in.readObject());
		this.setMessage((String)in.readObject());
		this.setColor((Color)in.readObject());
	}
	
	
}
