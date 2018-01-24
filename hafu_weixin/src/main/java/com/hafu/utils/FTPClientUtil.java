package com.hafu.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * FTP客户端工具类
 */
public class FTPClientUtil {

    private static final Logger LOGGER = Logger.getLogger(FTPClientUtil.class);

    /**
     * 连接文件服务器
     * @param addr 文件服务器地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @param isTextMode 传输方式 true ASCII传输模式 | false 以二进制方式传输
     * @throws Exception 
     */
    public static FTPClient connect(String addr, int port, String username, String password,boolean isTextMode) {
        LOGGER.debug("【连接文件服务器】addr = " + addr + " , port : " + port + " , username = " + username + " , password = "
                    + password);

        FTPClient ftpClient = new FTPClient();
        try {
            // 连接
            ftpClient.connect(addr, port);
            // 登录
            ftpClient.login(username, password);
            if(isTextMode){
            	ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
            }else{
            	//以二进制方式传输
            	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            }
            
        } catch (Exception e) {
            LOGGER.error("【连接文件服务器失败】", e);
            throw new RuntimeException("连接文件服务器失败");
        }
        // 判断文件服务器是否可用？？
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            closeConnection(ftpClient);
        }
        return ftpClient;
    }

    /**
     * 连接文件服务器
     * @param addr 文件服务器地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @param isTextMode 传输方式 true ASCII传输模式 | false 以二进制方式传输
     * @param workingDirectory 目标连接工作目录
     * @throws Exception 
     */
    public static FTPClient connect(String addr, int port, String username, String password,boolean isTextMode,String workingDirectory){
        FTPClient ftpClient = connect(addr, port, username, password,isTextMode);
        changeWorkingDirectory(workingDirectory, ftpClient);
        return ftpClient;
    }

    /**
     * 关闭连接，使用完连接之后，一定要关闭连接，否则服务器会抛出 Connection reset by peer的错误
     * @throws IOException
     */
    public static void closeConnection(FTPClient ftpClient) {
        LOGGER.debug("【关闭文件服务器连接】");
        if (ftpClient == null) {
            return;
        }

        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            LOGGER.error("【关闭连接失败】", e);
            throw new RuntimeException("关闭连接失败");
        }
    }

    /**
     * 切换工作目录
     * @param remote 远程服务器目录绝对路径
     * @param ftpClient 
     * @throws IOException
     */
    public static void changeWorkingDirectory(String remote, FTPClient ftpClient) {
        LOGGER.debug("【切换工作目录】directory : " + remote);
        // 切换到目标工作目录
        try {
        	remote = remote+"/";
        	String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
            // 如果远程目录不存在，则递归创建远程服务器目录
            if (!directory.equalsIgnoreCase("/")
                    && !ftpClient.changeWorkingDirectory(new String(directory))) {
            	int start = 0;
                int end = 0;
                if (directory.startsWith("/")) {
                    start = 1;
                } else {
                    start = 0;
                }
                end = directory.indexOf("/", start);
                while (true) {
                    String subDirectory = new String(remote.substring(start, end));
                    if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                        if (ftpClient.makeDirectory(subDirectory)) {
                        	ftpClient.changeWorkingDirectory(subDirectory);
                        } else {
                        	LOGGER.debug("创建目录失败");
                        }
                    }
                    start = end + 1;
                    end = directory.indexOf("/", start);
                    // 检查所有目录是否创建完毕
                    if (end <= start) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("【切换工作目录失败】", e);
            throw new RuntimeException("切换工作目录失败");
        }
    }

    /** 
     * 上传文件/文件夹
     * @param file 上传的文件或文件夹 
     * @return 文件存放的路径以及文件名
     * @throws Exception 
     */
    public static void upload(File file,FTPClient ftpClient) throws Exception {
        if (file == null) {
            LOGGER.warn("【存储的文件为空】");
            throw new RuntimeException("上传文件为空");
        }
        LOGGER.debug("【上传文件/文件夹】file ： " + file.getName());

        // 是文件，直接上传
        if (!file.isDirectory()) {
            storeFile(new File(file.getPath()), ftpClient);
            return;
        }
        // 文件夹，递归上传所有文件
        for (File item : file.listFiles()) {
            if (!item.isDirectory()) {
                storeFile(item, ftpClient);
                continue;
            }
            upload(item, ftpClient);
            ftpClient.changeToParentDirectory();
        }
    }
    
    /** 
     * 上传文件
     * @param file 上传的文件或文件夹 
     * @return 文件存放的路径以及文件名
     * @gWidth 宽度
     * @throws Exception 
     */
    public static boolean uploadInputStream(InputStream input,String fileName, FTPClient ftpClient,Integer gWidth){
        try {
			if (input == null) {
			    LOGGER.warn("【存储的文件为空】");
			    return false;
			}

			// 是文件，直接上传
			InputStream inFile = input;
		    if(gWidth > 0) {
		        BufferedImage sourceImg = ImageIO.read(input);
		        // 在文本框内的图片宽度应比文本框本身宽度小100px
		        if(sourceImg.getWidth() > gWidth - 100) {
		        	inFile = getImageStream(zoomOutImage(sourceImg, gWidth - 100));
		        }
		    }
		    ftpClient.enterLocalPassiveMode();
		    ftpClient.storeFile(fileName, inFile);
		    inFile.close();
		    return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
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

    /**
     * 删除文件
     * @param fileName 要删除的文件地址
     * @return true/false
     * @throws IOException 
     */
    public static boolean delete(String fileName, FTPClient ftpClient) throws IOException {
        LOGGER.debug("【删除文件】fileName ： " + fileName);
        return ftpClient.deleteFile(fileName);
    }

    /**
     * 存储文件
     * @param file {@link File}
     * @throws Exception
     */
    public static void storeFile(File file, FTPClient ftpClient) throws Exception {
        if (file == null) {
            LOGGER.warn("【存储的文件为空】");
            throw new RuntimeException("存储的文件为空");
        }
        LOGGER.debug("【存储文件】file ： " + file.getName());

        FileInputStream input = new FileInputStream(file);
        ftpClient.enterLocalPassiveMode();
        ftpClient.storeFile(file.getName(), input);
        input.close();
    }

    /**
     * 下载文件到指定目录
     * @param ftpFile 文件服务器上的文件地址
     * @param dstFile 输出文件的路径和名称
     * @throws Exception 
     */
    public static void downLoad(String ftpFile, String dstFile, FTPClient ftpClient) throws Exception {
        LOGGER.debug("【下载文件到指定目录】ftpFile = " + ftpFile + " , dstFile = " + dstFile);
        if (StringUtils.isBlank(ftpFile)) {
            LOGGER.warn("【参数ftpFile为空】");
            throw new RuntimeException("【参数ftpFile为空】");
        }
        if (StringUtils.isBlank(dstFile)) {
            LOGGER.warn("【参数dstFile为空】");
            throw new RuntimeException("【参数dstFile为空】");
        }
        File file = new File(dstFile);
        FileOutputStream fos = new FileOutputStream(file);
        ftpClient.retrieveFile(ftpFile, fos);
        fos.flush();
        fos.close();
    }

    /**
     * 从文件服务器获取文件流
     * @param ftpFile 文件服务器上的文件地址
     * @return {@link InputStream}
     * @throws IOException
     */
    public static InputStream retrieveFileStream(String ftpFile, FTPClient ftpClient) throws IOException {
        LOGGER.debug("【从文件服务器获取文件流】ftpFile ： " + ftpFile);
        if (StringUtils.isBlank(ftpFile)) {
            LOGGER.warn("【参数ftpFile为空】");
            throw new RuntimeException("【参数ftpFile为空】");
        }
        return ftpClient.retrieveFileStream(ftpFile);
    }
    
    
    
    public static void main(String[] args) {
		try {
			FTPClient ftpClient = connect("172.168.22.103", 2134, "ftpuser-fb1", "Ps1-32!$",false, "/test2/ttt1/ttttt");
			File file = new File("d:/demo.log");
			upload(file, ftpClient);
			closeConnection(ftpClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}