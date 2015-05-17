package message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FileMessage extends Message {
	public static final int STATUS_INIT = -1;
	public static final int STATUS_START_SERVER = 1;
	public static final int STATUS_SEND_FILE = 2;
	public static final int STATU_RECEIVE_FILE = 3;
	public static final int STATUS_ENDSEND_FILE = 4;
	public static final int STATUS_ENDRECEIVE_FILE = 5;
	private String file ;  //文件名
	private int status; //信息状态
	
	public FileMessage() {
		// TODO Auto-generated constructor stub
		super();
		this.file = "" ;
		this.status = -1 ;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		super.writeExternal(out);
		out.writeObject(this.file);
		out.writeObject(this.status);
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		super.readExternal(in);
		this.setFile((String)in.readObject());
		this.setStatus(((Integer)in.readObject()).intValue());
	}
	
	

}
