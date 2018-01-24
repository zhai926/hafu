package com.hafu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * ftp工具类
 */
public class FtpUtil {
    private static final Logger LOGGER = Logger.getLogger(FtpUtil.class);
	private FTPClient ftp;

    /**
    * FTPClient构造函数,主要进行初始化配置连接FTP服务器。
    * 
    * @param host
    *            FTP服务器的IP地址
    * @param port
    *            FTP服务器的端口
    * @param userName
    *            FTP服务器的用户名
    * @param passWord
    *            FTP服务器的密码
    */
    public FtpUtil(String host, int port, String userName, String passWord) {
    	LOGGER.debug("【连接文件服务器】host = " + host + " , port : " + port + " , userName = " + userName + " , passWord = "
                + passWord);
        ftp = new FTPClient();
        try {
            ftp.connect(host, port);// 连接FTP服务器
            ftp.login(userName, passWord);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            	LOGGER.debug("未连接到FTP，用户名或密码错误。");
                ftp.disconnect();
            } else {
            	LOGGER.debug("FTP连接成功。");
            }
        } catch (SocketException e) {
            LOGGER.error("FTP的IP地址可能错误，请正确配置。",e);
        } catch (IOException e) {
            LOGGER.error("FTP的端口错误,请正确配置。",e);
        }
    }

    /**
    * 上传文件到FTP服务器
    * 
    * @param local
    *            本地文件名称，绝对路径* 
    * @param remote
    *            远程文件路径,支持多级目录嵌套，支持递归创建不存在的目录结构
    * @throws IOException
    */
    public void upload(String local, String remote) throws IOException {
        // 设置PassiveMode传输
        ftp.enterLocalPassiveMode();
        // 设置以二进制流的方式传输
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        // 对远程目录的处理
        String remoteFileName = remote;
        if (remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            // 创建服务器远程目录结构，创建失败直接返回
            if (!CreateDirecroty(remote)) {
                return;
            }
        }
        //FTPFile[] files = ftp.listFiles(new String(remoteFileName));
        File f = new File(local);
        uploadFile(remoteFileName, f);
    }

    public void uploadFile(String remoteFile, File localFile)
            throws IOException {
        InputStream in = new FileInputStream(localFile);
        ftp.enterLocalPassiveMode();
        boolean flag = ftp.storeFile(remoteFile, in);
        if(flag){
        	LOGGER.debug("上传成功!");
        }else{
        	LOGGER.debug("上传状态:"+ftp.getReplyString());
        	LOGGER.debug("上传失败!");
        }
        ftp.disconnect();
        in.close();
        
    }
    
    /**
     * 上传文件 以InputStream形式
     * @param remoteFile ftp文件路径 例如:test/1.jpg
     * @param in
     * @throws IOException
     */
    public void uploadFile(String remoteFile, InputStream in)
            throws IOException {
    	// 设置PassiveMode传输
        ftp.enterLocalPassiveMode();
        // 设置以二进制流的方式传输
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        // 对远程目录的处理
        if (remoteFile.contains("/")) {
            // 创建服务器远程目录结构，创建失败直接返回
            if (!CreateDirecroty(remoteFile)) {
                return;
            }
        }
        
        boolean flag = ftp.storeFile(remoteFile, in);
        if(flag){
        	LOGGER.debug("上传成功!");
        }else{
        	LOGGER.debug("上传状态:"+ftp.getReplyString());
        	LOGGER.debug("上传失败!");
        }
        ftp.disconnect();
        in.close();
        LOGGER.debug("上传成功!");
    }

    /**
    * 递归创建远程服务器目录
    * 
    * @param remote
    *            远程服务器文件绝对路径
    * 
    * @return 目录创建是否成功
    * @throws IOException
    */
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/")
                && !ftp.changeWorkingDirectory(new String(directory))) {
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
                if (!ftp.changeWorkingDirectory(subDirectory)) {
                    if (ftp.makeDirectory(subDirectory)) {
                        ftp.changeWorkingDirectory(subDirectory);
                    } else {
                    	LOGGER.debug("创建目录失败");
                        success = false;
                        return success;
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
        return success;
    }

    public boolean uploadAll(String filename, String uploadpath)
            throws Exception {
        boolean success = false;
        File file = new File(filename);
        // 要上传的是否存在
        if (!file.exists()) {
            return success;
        }
        // 要上传的是否是文件夹
        if (!file.isDirectory()) {
            return success;
        }
        File[] flles = file.listFiles();
        for (File files : flles) {
            if (files.exists()) {
                if (files.isDirectory()) {
                    this.uploadAll(files.getAbsoluteFile().toString(),
                            uploadpath);
                } else {
                    String local = files.getCanonicalPath().replaceAll("\\\\",
                            "/");
                    String remote = uploadpath
                            + local.substring(local.indexOf("/") + 1);
                    upload(local, remote);
                    ftp.changeWorkingDirectory("/");
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream(new File("D:/1.jpg"));
        //ftp.upload("D:/1.jpg", "test2/1.jpg");
        for(int i=0;i<50;i++){
            FtpUtil ftp = new FtpUtil("172.168.22.103", 2135, "ftpuser-fb1", "Ps1-32!$");
        	ftp.upload("D:/1.jpg", "test"+i+"/1.jpg");
        }
        //ftp.uploadAll("D:/ftp", "/bb/aa/");
        //ftp.uploadFile("/test/2.jpg", in);
    }
}
