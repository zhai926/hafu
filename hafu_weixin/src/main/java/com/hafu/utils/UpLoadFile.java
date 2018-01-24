package com.hafu.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.hafu.modules.utils.BasicTypeUtils;
import org.hafu.modules.utils.VSFtpUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


public class UpLoadFile {
	
    private static final Logger LOGGER = Logger.getLogger(UpLoadFile.class);

	public static final ResourceBundle BUNDLE = PropertyResourceBundle.getBundle("smsconfig");
	public static final String HOSTNAME = BasicTypeUtils.notNull(BUNDLE
			.getString("ftp.server.host"));// ftp IP地址
	public static final String USERNAME = BasicTypeUtils.notNull(BUNDLE
			.getString("ftp.server.user")); // ftp 用户名
	public static final String PASSWORD = BasicTypeUtils.notNull(BUNDLE
			.getString("ftp.server.pwd"));// ftp 密码
	public static final String REMOTEFILEPATH = BasicTypeUtils.notNull(BUNDLE
			.getString("ftp.server.path"));// bbsftp上传路径
	public static final String PORT = BUNDLE.getString("ftp.server.port");// ftp端口号

	/*public static final String RELATIVEATTACHMENTPATH=BasicTypeUtils.notNull(BUNDLE.
			getString("apache.attachment.relative"));*/
	/*public static final String BBSPATH=BasicTypeUtils.notNull(BUNDLE.
			getString("apache.bbsimg.relative"));*/
	//public static final String HEADPIC=BasicTypeUtils.notNull(BUNDLE.getString("apache.picimg.relative"));
	
	//ftp路径
	public static final String VOICEPATH=BasicTypeUtils.notNull(BUNDLE.getString("apache.attachment.voicePath"));
	//本地下载缓存路径
	public static final String LOCALVOICEPATH=BasicTypeUtils.notNull(BUNDLE.getString("local.voicePath"));
	
	/**
	 * 上传附件，发布产品附件
	 * @param file
	 * @param prefix
	 * @return
	 */
	/*public static String uploadAttachmentImage(MultipartFile file,String prefix){
		return getFileNameAndUpload(  file, prefix, REMOTEFILEPATH+RELATIVEATTACHMENTPATH);
	}*/
	public static String validateImage(MultipartFile picFile){
		String error="";
		CommonsMultipartFile cf = (CommonsMultipartFile) picFile;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file = fi.getStoreLocation();
		if (file != null) {
			if(file.length() < 200 * 1024) {
				Pattern p = Pattern .compile("^(([a-zA-Z]*)+\\/)+(bmp|png|gif|jpeg|jpg|x-png|pjpeg)$");
				Matcher m = p.matcher(picFile.getContentType());
				if (m.find()) {
					return "right";
				} else {
					error = "上传图片格式不正确";
				}
			} else {
				error = "图片大小不能超过200kb";
			}
		}
		return error;
	}
	private static String getFileNameAndUpload(MultipartFile file,String prefix,String remoteFilePath) {
		CommonsMultipartFile cf= (CommonsMultipartFile)file; 
        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
        File f = fi.getStoreLocation();
		return getFileName(file.getOriginalFilename(), f, prefix, remoteFilePath);
	}
	/**
	 * 获得文件名并上传文件
	 * 
	 * @param fileName
	 * 
	 * @return
	 */
	private static String getFileName(String fileName, File file,String prefix,String remoteFilePath) {
		String extension = ""; // 保存上传文件的后缀名
		String picName= "";
		if (fileName.lastIndexOf(".") != -1) {
			extension = fileName.substring(fileName.lastIndexOf("."));
			picName= prefix + new Date().getTime() + extension;// 文件地址
			upLoadFile(remoteFilePath, file, picName);
		}
		return picName;
	}
	/**
	 * 上传文件
	 * @param remoteFolder ftp服务器文件夹 例如：/test或/test/test
	 * @param file 文件
	 * @param fileName 文件名称
	 */
	private static void upLoadFile(String remoteFolder,File file, String fileName){
		/*VSFtpUtils.setFtphost(HOSTNAME);
		VSFtpUtils.setFtpuser(USERNAME);
		VSFtpUtils.setFtppass(PASSWORD);
		VSFtpUtils.setFtpport(Integer.parseInt(PORT));
		VSFtpUtils.setFtppath(remoteFolder);
		VSFtpUtils.put(file, fileName, "", 0);*/
		
		try {
			FtpUtil ftp = new FtpUtil(HOSTNAME, Integer.parseInt(PORT), USERNAME, PASSWORD);
	        ftp.upload(file.getPath(), remoteFolder+"/"+fileName);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	/**
	 * 上传语音
	 * @param filePath 文件路径 例：d:\\demo.log
	 * @param fileName 文件名称 例：test5.log
	 * @return ftp虚拟目录相对路径
	 */
	public static String upLoadVoiceFile(String filePath,String fileName){
		File file=new File(filePath);
		/*UpLoadFile.upLoadFile(REMOTEFILEPATH+VOICEPATH, file, fileName);
		return REMOTEFILEPATH+VOICEPATH+fileName;*/
		try {
			FtpUtil ftp = new FtpUtil(HOSTNAME, Integer.parseInt(PORT), USERNAME, PASSWORD);
	        ftp.upload(file.getPath(), VOICEPATH+"/"+fileName);
			return  REMOTEFILEPATH+VOICEPATH+"/"+fileName;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return "";
		}
	}
	
	/**
	 * 字符流方式上传文件(可缩放) 
	 * @param remoteFilePath 服务器目录 例如:/test
	 * @param in 字符流
	 * @param fileName 文件名称 1.jpg
	 * @gWidth 
	 * @return
	 */
	public static boolean upLoadInputStream(String remoteFilePath,InputStream in,String fileName,int gWidth){
		boolean flag = false;
		try {
			/*if(gWidth > 0) {
	        BufferedImage sourceImg = ImageIO.read(in);
	        // 在文本框内的图片宽度应比文本框本身宽度小100px
	        if(sourceImg.getWidth() > gWidth - 100) {
	        	in = getImageStream(zoomOutImage(sourceImg, gWidth - 100));
	        }
        }*/
			FtpUtil ftp = new FtpUtil(HOSTNAME, Integer.parseInt(PORT), USERNAME, PASSWORD);
			ftp.uploadFile(remoteFilePath+"/"+fileName, in);
			flag=true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	public static InputStream getImageStream(BufferedImage bi) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();         
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs); 
        ImageIO.write(bi, "png", imOut); 
        return new ByteArrayInputStream(bs.toByteArray());
    }
	/**
     * 对图片进行缩小
     * @param originalImage 原始图片
     * @param times 缩小倍数
     * @return 缩小后的Image
     */
    public static BufferedImage zoomOutImage(BufferedImage originalImage, Integer toWidth) {        
        double height = (double)originalImage.getHeight() / ((double)originalImage.getWidth() / (double)toWidth);
        BufferedImage newImage = new BufferedImage(toWidth, (int)height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, toWidth, (int)height, null);
        g.dispose();
        return newImage;
    }

	
	public static void main(String[] args) {
		//String str = UpLoadFile.upLoadVoiceFile("d:\\demo.log", "test6.log");
		//System.out.println(str);
		
		//upLoadFile("/headpic", new File("d:\\demo.log"), "test6.log");
		try {
			upLoadInputStream("/headpic", new FileInputStream(new File("d:\\1.jpg")), "00.jpg", 0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
