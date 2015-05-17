package message;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * 图片消息对象
 * @author itprogrammer
 *
 */
public class ImageMessage extends Message {

	private	Image image;


	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * 获取默认Image样式转化后的Image输出样式
	 * @param filePath  Image文件本地存放路径
	 * @return
	 */
	public static String getDefalutImageSytle(String filePath){
		StringBuilder sb =  new StringBuilder();
		sb.append("<img src=\"file:\\"+filePath+"\">");
		sb.append("</img><br/>");
		return sb.toString();
	}
	

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		super.writeExternal(out);
		//序列化Image
		BufferedImage bi = (BufferedImage)this.image;
		
		//写入BufferedImage属性信息

//		System.out.println(bi.getHeight());
//		System.out.println(bi.getWidth());
//		System.out.println(bi.getType());
		out.writeObject(bi.getHeight());
		out.writeObject(bi.getWidth());
		out.writeObject(bi.getType());
		
		
		
		Raster raster = bi.getData();
		
		SampleModel  slm = raster.getSampleModel();
		//写入SampleModel 属性
		if(slm instanceof ComponentSampleModel){
			out.writeObject(1);
			out.writeObject(slm.getDataType());
			out.writeObject(slm.getWidth());
			out.writeObject(slm.getHeight());
			
			ComponentSampleModel cSlm = (ComponentSampleModel)slm;
			out.writeObject(cSlm.getPixelStride());
			out.writeObject(cSlm.getScanlineStride());
			out.writeObject(cSlm.getBandOffsets());
			
		}else if(slm instanceof MultiPixelPackedSampleModel){
			out.writeObject(2);
			out.writeObject(slm.getDataType());
			out.writeObject(slm.getWidth());
			out.writeObject(slm.getHeight());
			
			MultiPixelPackedSampleModel mppSlm = (MultiPixelPackedSampleModel)slm;
			out.writeObject(mppSlm.getNumDataElements());
		}else if(slm instanceof SinglePixelPackedSampleModel){
			out.writeObject(3);
			out.writeObject(slm.getDataType());
			out.writeObject(slm.getWidth());
			out.writeObject(slm.getHeight());
			
			SinglePixelPackedSampleModel sppSlm = (SinglePixelPackedSampleModel)slm;
			out.writeObject(sppSlm.getBitMasks());
		}

		
		DataBuffer dbf = raster.getDataBuffer();
		int type = dbf.getDataType();
		int size = dbf.getSize();
		//写入 DataBuffer 属性信息
		out.writeObject(type);
		out.writeObject(size);
		out.writeObject(raster.getSampleModelTranslateX());
		out.writeObject(raster.getSampleModelTranslateY());
		
		if(type==DataBuffer.TYPE_BYTE){
			DataBufferByte dbfb = (DataBufferByte) dbf;
			out.writeObject(dbfb.getBankData());
			
		}else if(type == DataBuffer.TYPE_INT){
			DataBufferInt dbfi = (DataBufferInt) dbf;
			out.writeObject(dbfi.getBankData());
			
		}else if(type == DataBuffer.TYPE_DOUBLE){
			DataBufferDouble dbfd = (DataBufferDouble) dbf;
			out.writeObject(dbfd.getBankData());
			
		}else if(type== DataBuffer.TYPE_FLOAT){
			DataBufferFloat dbff = (DataBufferFloat) dbf;
			out.writeObject(dbff.getBankData());
		}else if(type == DataBuffer.TYPE_SHORT){
			DataBufferShort dbfs = (DataBufferShort) dbf;
			out.writeObject(dbfs.getBankData());
		}else if(type == DataBuffer.TYPE_USHORT){
			DataBufferUShort dbfus = (DataBufferUShort) dbf;
			out.writeObject(dbfus.getBankData());
		}else{
			//不支持的序列化对象
			System.out.println("无法序列化");
		}
		
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		super.readExternal(in);

		//读取BufferedImage 信息

		int h = ((Integer)in.readObject()).intValue();
		int w = ((Integer)in.readObject()).intValue();
		int imageType = ((Integer)in.readObject()).intValue();
		
		BufferedImage bi = new BufferedImage(w,h,imageType);
		
		//读取SampleModel 信息
		int slmType = ((Integer)in.readObject()).intValue();
		int slmDataType = ((Integer)in.readObject()).intValue();
		int slmW =((Integer)in.readObject()).intValue();
		int slmH= ((Integer)in.readObject()).intValue();
		SampleModel slm =null ;
		if(slmType==1){
			int pixelStride = ((Integer)in.readObject()).intValue();
			int scanlineStride =((Integer)in.readObject()).intValue();
			int[] bandOffsets = (int[]) in.readObject();
			ComponentSampleModel cslm = new ComponentSampleModel(slmDataType,slmW,slmH,pixelStride,scanlineStride,bandOffsets);
			slm = cslm;
		}else if(slmType ==2){
			int numData= ((Integer)in.readObject()).intValue();
			MultiPixelPackedSampleModel mpSlm = new MultiPixelPackedSampleModel(slmDataType,slmW,slmH,numData);
			slm = mpSlm;
		}else if(slmType ==3){
			int [] bitMasks =(int []) in.readObject();
			SinglePixelPackedSampleModel sppSlm = new SinglePixelPackedSampleModel(slmDataType,slmW,slmH,bitMasks);
			slm = sppSlm;
		}
		
		//读取DataBuffer 信息
		int dataType = ((Integer)in.readObject()).intValue();
		int dataSize = ((Integer)in.readObject()).intValue();
		int pX =((Integer)in.readObject()).intValue();
		int pY = ((Integer)in.readObject()).intValue();
		Point point = new Point(pX,pY);
		DataBuffer dbf =null;
		
		
		if(dataType==DataBuffer.TYPE_BYTE){
			byte[][] datasB= (byte[][])in.readObject();
			DataBufferByte dbfb = new DataBufferByte(datasB,dataSize);
			dbf = dbfb;

		}else if(dataType == DataBuffer.TYPE_INT){
			int[][] datasI =(int[][])in.readObject();
			DataBufferInt dbfI= new DataBufferInt(datasI,dataSize);
			dbf = dbfI;
			
		}else if(dataType == DataBuffer.TYPE_DOUBLE){
			double[][] datasD =(double[][])in.readObject();
			DataBufferDouble dbfD= new DataBufferDouble(datasD,dataSize);
			dbf = dbfD;
			
		}else if(dataType== DataBuffer.TYPE_FLOAT){
			float[][] datasF =(float[][])in.readObject();
			DataBufferFloat dbfF= new DataBufferFloat(datasF,dataSize);
			dbf = dbfF;
			
		}else if(dataType == DataBuffer.TYPE_SHORT){
			short[][] datasS =(short[][])in.readObject();
			DataBufferShort dbfS= new DataBufferShort(datasS,dataSize);
			dbf = dbfS;
			
		}else if(dataType == DataBuffer.TYPE_USHORT){
			short[][] datasUS =(short[][])in.readObject();
			DataBufferUShort dbfUS= new DataBufferUShort(datasUS,dataSize);
			dbf = dbfUS;
		}else{
			//不支持的序列化对象
			System.out.println("无法序列化");
		}
		
		
		Raster raster = Raster.createRaster(slm,dbf,point);
		bi.setData(raster);
		this.setImage(bi) ;
		
	}

}
