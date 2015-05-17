package util;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

import javax.imageio.ImageIO;

import constpage.Const;

public class ImageUtil {
	/**
	 * 获取图片资源，必须导出包则必须放在指定包下
	 * @param fileName
	 * @return
	 */
	public static Image getImage(String fileName){
		   //System.out.println(ImageUtil.class.getResource("../../src/Image/1.gif"));
			Image	image =Toolkit.getDefaultToolkit().getImage(ImageUtil.class.getResource(fileName));
			return image;
			
	}
	
	/**
	 * 获取剪切板的图片
	 * @return
	 */
	public static Image getImageClipboard(){
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
			if(t!=null && t.isDataFlavorSupported(DataFlavor.imageFlavor)){
				Image image = (Image)t.getTransferData(DataFlavor.imageFlavor);
				BufferedImage bi = (BufferedImage)image;
				System.out.println(bi.getType());
				return image;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
		return null ;
	}
	
	/**
	 * 将Image 保存至默认临时目录
	 * @param image
	 * @return
	 */
	public static String saveImageToFile(Image image){
		try {
		 String fileName = String.valueOf(new Date().getTime())+".png";
		 String path="";
		 File fpath = new File(System.getProperty("user.dir"),Const.IMAGE_TEMP_PATH);
		 System.out.println(fpath.getCanonicalPath());
		 if(!fpath.exists()){
			 fpath.mkdirs();
		 }
		 File f = new File(Const.IMAGE_TEMP_PATH,fileName);
		 System.out.println(f.getCanonicalPath());
		 if(f.exists()){
			 f.delete();
		 }
		 f.createNewFile();
		 
			 BufferedImage bi = (BufferedImage)image;
			ImageIO.write(bi, "png",f );
			path = f.getCanonicalPath() ;
			return path;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
