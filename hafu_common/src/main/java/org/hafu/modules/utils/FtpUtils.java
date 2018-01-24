package org.hafu.modules.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import com.jcraft.jsch.UserInfo;


public class FtpUtils {

	private static final Logger logger = Logger.getLogger(FtpUtils.class);

	private static String ftphost;// FTP IP地址
	private static String ftpuser;// FTP 用户名
	private static String ftppass;// FTP 密码
	private static String ftppath;// FTP 上传路径
	private static int ftpport;// FTP 端口

	public static String getFtphost() {
		return ftphost;
	}

	public static void setFtphost(String ftphost) {
		FtpUtils.ftphost = ftphost;
	}

	public static String getFtpuser() {
		return ftpuser;
	}

	public static void setFtpuser(String ftpuser) {
		FtpUtils.ftpuser = ftpuser;
	}

	public static String getFtppass() {
		return ftppass;
	}

	public static void setFtppass(String ftppass) {
		FtpUtils.ftppass = ftppass;
	}

	public static int getFtpport() {
		return ftpport;
	}

	public static void setFtpport(int ftpport) {
		FtpUtils.ftpport = ftpport;
	}

	public static void setFtppath(String ftppath) {
		FtpUtils.ftppath = ftppath;
	}

	public static String getFtppath() {
		return ftppath;
	}

	/**
	 * 
	 * 构造一个全局共享的内部类，以实现UserInfo接品方法
	 * 
	 */
	private static final UserInfo defaultUserInfo = new UserInfo() {
		
		public String getPassphrase() {
			return null;
		}

		public String getPassword() {
			return null;
		}

		public boolean promptPassword(String arg0) {
			return false;
		}

		public boolean promptPassphrase(String arg0) {
			return false;
		}

		public boolean promptYesNo(String arg0) {
			return true;
		}

		public void showMessage(String arg0) {
			
		}
		
	};
	
	
	/**
	 * 公共参数初始化的put 方法
	 * 使用默认的 hostname,username,password,port ,remoteFilePath
	 */
	public static boolean defaultPut(File file,String picName,String relativePath,int gwidth){
		ResourceBundle bundle=PropertyResourceBundle.getBundle("smsconfig");//ftp配置文件
		String hostname =BasicTypeUtils.notNull(bundle.getString("ftp.server.host"));//ftp IP地址
		String username =BasicTypeUtils.notNull(bundle.getString("ftp.server.user")); //ftp 用户名
		String password =BasicTypeUtils.notNull(bundle.getString("ftp.server.pwd"));//ftp 密码
		String remoteFilePath=BasicTypeUtils.notNull(bundle.getString("ftp.server.path"));//ftp上传路径
		String port = bundle.getString("ftp.server.port");//ftp端口号
		relativePath = BasicTypeUtils.notNull(bundle.getString(relativePath)); //相对路径
		FtpUtils.setFtphost(hostname);
		FtpUtils.setFtpuser(username);
		FtpUtils.setFtppass(password);
		FtpUtils.setFtpport(Integer.parseInt(port));
		FtpUtils.setFtppath(remoteFilePath);
		return put(file, picName, relativePath, gwidth);
	}

	/**
	 * Ftp文件上传方法，在远程Ftp服务器上创建所需的目录及文件
	 * @param String 最终在VSFtp服务器上的文件名
	 * @param File   待写入远程服务器的本地文件对象
	 * @param String 最终在VSFtp服务器上的文件目录，相对于remoteFilePath
	 * @return boolean
	 * 
	 */
	@SuppressWarnings("unused")
	public static boolean put(File localFile, String targetFileName, 
			String remoteFileDirectory, int gWidth) {		
		String hostname =BasicTypeUtils.notNull(getFtphost());
		String username =BasicTypeUtils.notNull(getFtpuser());  
		String password =BasicTypeUtils.notNull(getFtppass());
		String remoteFilePath=BasicTypeUtils.notNull(getFtppath());	
		//检测VSFTP参数是否初使化正确
		if(hostname.length() == 0 || username.length() == 0 || 
		   password.length() == 0 || remoteFilePath.length() == 0) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<< VSFtp system paras initialization was failed.>>>>>>>>>>>>>>");
			return false;
		}
		String[] targetFileCatalog = remoteFileDirectory.split("/");
		StringBuffer tempRemoteFileCatalog = new StringBuffer(remoteFilePath);
		Session session = null;
		Channel channel = null;		
		try{
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<< targetFileName:" + targetFileName + " remoteFileDirectory:" + remoteFileDirectory + ">>>>>>>>>>>>>");
	        JSch jsch = new JSch(); 
	        session=jsch.getSession(username,hostname,getFtpport());
	        session.setPassword(password);  
	        session.setUserInfo(defaultUserInfo);
	        session.connect();
	        channel = session.openChannel("sftp");;   
	        channel.connect();  
	        ChannelSftp cs = (ChannelSftp)channel;	        
			for (String catalog : targetFileCatalog) {
				tempRemoteFileCatalog.append(catalog).append("/");
		        try {
		        	cs.mkdir(tempRemoteFileCatalog.toString());
		        } catch(Exception ex){ }
			}
	        remoteFilePath = tempRemoteFileCatalog.append(targetFileName).toString();
	        @SuppressWarnings("resource")
			InputStream inFile = new FileInputStream(localFile);
	        if(gWidth > 0) {
		        BufferedImage sourceImg = ImageIO.read(localFile);
		        // 在文本框内的图片宽度应比文本框本身宽度小100px
		        if(sourceImg.getWidth() > gWidth - 100) {
		        	inFile = getImageStream(zoomOutImage(sourceImg, gWidth - 100));
		        }
	        }
	        SftpProgressMonitor spm = new SftpProgressMonitor() {        	
				
				public boolean count(long arg0) {
					return false;
				}
				
				public void end() {		
				}
				
				public void init(int arg0, String arg1, String arg2, long arg3) {
				}
	        };
	        //以IO流的方式将文件写入服务器
	        //cs.put(inFile, remoteFilePath, spm, cs.RESUME);
	        //将本地指定文件上传到服务器
	        cs.put(inFile, remoteFilePath);
	        return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if(null != channel) {
					channel.disconnect();
				}
				if(null != session) {
					session.disconnect();
				}
				logger.info("<<<<<<<<<<<<<<<<<<<<<<< VSFtp disconnection was success.>>>>>>>>>>>");
			} catch(Exception er) {
				logger.error("<<<<<<<<<<<<<<<<<<<<<< " + er.getMessage() + ">>>>>>>>>>>>>>");
				return false;
			} finally {
				channel = null;
				session = null;
			}
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
	 * 远程创建目录 eg: remoteDir="test1/test2"
	 * @param remoteDir
	 *            (相对ftp根目录的相对路径)
	 * @return
	 */
	public static boolean makeDir(String remoteDir) {
		String hostname = BasicTypeUtils.notNull(getFtphost());
		String username = BasicTypeUtils.notNull(getFtpuser());
		String password = BasicTypeUtils.notNull(getFtppass());
		String remoteFilePath = BasicTypeUtils.notNull(getFtppath());
		// 检测VSFTP参数是否初使化正确
		if (hostname.length() == 0 || username.length() == 0 || 
			password.length() == 0 || remoteFilePath.length() == 0) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<< VSFtp system paras initialization was failed.>>>>>>>>>>>>>>");
			return false;
		}
		String[] targetFileCatalog = remoteDir.split("/");
		StringBuffer tempRemoteFileCatalog = 
				new StringBuffer(remoteFilePath.lastIndexOf("/") != -1 ? (remoteFilePath + "/") : remoteFilePath);
		Session session = null;
		Channel channel = null;
		try {
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<   remoteFileDirectory:" + remoteDir + ">>>>>>>>>>>>>");
			JSch jsch = new JSch();
			session = jsch.getSession(username, hostname, getFtpport());
			session.setPassword(password);
			session.setUserInfo(defaultUserInfo);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp cs = (ChannelSftp) channel;
			for (String catalog : targetFileCatalog) {
				if (catalog == null || "".equals(catalog)) {
					continue;
				}
				tempRemoteFileCatalog.append(catalog).append("/");
				try {
					cs.mkdir(tempRemoteFileCatalog.toString());
					logger.info("<<<<<<<<<<<<<<<<<<<<<<<<  create remote file directory:" + tempRemoteFileCatalog.toString() + ">>>>>>>>>>>>>");
				} catch (SftpException ex) {
					if (ex.id != 4) {
						throw ex;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if (null != channel) {
					channel.disconnect();
				}
				if (null != session) {
					session.disconnect();
				}
				logger.info("<<<<<<<<<<<<<<<<<<<<<<< VSFtp disconnection was success.>>>>>>>>>>>");
			} catch (Exception er) {
				logger.error("<<<<<<<<<<<<<<<<<<<<<< " + er.getMessage() + ">>>>>>>>>>>>>>");
				return false;
			} finally {
				channel = null;
				session = null;
			}
		}
	}

	/**
	 * 获取远程ftp目录下的子元素
	 * @param remoteDir
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Vector<LsEntry> list(String remoteDir) {
		String hostname = BasicTypeUtils.notNull(getFtphost());
		String username = BasicTypeUtils.notNull(getFtpuser());
		String password = BasicTypeUtils.notNull(getFtppass());
		String remoteFilePath = BasicTypeUtils.notNull(getFtppath());
		// 检测VSFTP参数是否初使化正确
		if (hostname.length() == 0 || username.length() == 0 || 
			password.length() == 0 || remoteFilePath.length() == 0) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<< VSFtp system paras initialization was failed.>>>>>>>>>>>>>>");
			return null;
		}
		Session session = null;
		Channel channel = null;
		try {
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<< remote file directory:" + remoteDir + ">>>>>>>>>>>>>");
			JSch jsch = new JSch();
			session = jsch.getSession(username, hostname, getFtpport());
			session.setPassword(password);
			session.setUserInfo(defaultUserInfo);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp cs = (ChannelSftp) channel;
			return cs.ls(remoteDir);
		} catch (SftpException ex) {
			if (ex.id != 2) {
				ex.printStackTrace();
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (null != channel) {
					channel.disconnect();
				}
				if (null != session) {
					session.disconnect();
				}
				logger.info("<<<<<<<<<<<<<<<<<<<<<<< VSFtp disconnection was success.>>>>>>>>>>>");
			} catch (Exception er) {
				logger.error("<<<<<<<<<<<<<<<<<<<<<< " + er.getMessage() + ">>>>>>>>>>>>>>");
				return null;
			} finally {
				channel = null;
				session = null;
			}
		}
	}

	public static void main(String[] args) {
		/*VSFtpUtils.setFtphost("210.192.111.101");
		VSFtpUtils.setFtpuser("fisuser");
		VSFtpUtils.setFtppass("1234uintec");
		VSFtpUtils.setFtpport(22);
		VSFtpUtils.setFtppath("/home/fisuser/deploy/related");*/	
		ResourceBundle bundle=PropertyResourceBundle.getBundle("smsconfig");
		String hostname =BasicTypeUtils.notNull(bundle.getString("ftp.server.host"));
		String username =BasicTypeUtils.notNull(bundle.getString("ftp.server.user"));  
		String password =BasicTypeUtils.notNull(bundle.getString("ftp.server.pwd"));
		String remoteFilePath=BasicTypeUtils.notNull(bundle.getString("ftp.server.path"));
		String port = bundle.getString("ftp.server.port");
		FtpUtils.setFtphost(hostname);
		FtpUtils.setFtpuser(username);
		FtpUtils.setFtppass(password);
		FtpUtils.setFtpport(Integer.parseInt(port));
		FtpUtils.setFtppath(remoteFilePath);			
		FtpUtils.put(new File("c:\\1.jpg"), "test" + new Date().getTime()+".jpg", "/test", 0);
		/*VSFtpUtils.makeDir("/finanacial/fanxiaodong");
		Vector<LsEntry> list = VSFtpUtils
				.list("/home/fisuser/deploy/related");
	 	for (LsEntry e : list) {
			System.out.println(e.getFilename());
		}*/
	}
	public static boolean upLoadByInputSream(String url, String targetFileName, 
			String remoteFileDirectory, int gWidth){
		
		String hostname =BasicTypeUtils.notNull(getFtphost());
		String username =BasicTypeUtils.notNull(getFtpuser());  
		String password =BasicTypeUtils.notNull(getFtppass());
		String remoteFilePath=BasicTypeUtils.notNull(getFtppath());	
		//检测VSFTP参数是否初使化正确
		if(hostname.length() == 0 || username.length() == 0 || 
		   password.length() == 0 || remoteFilePath.length() == 0) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<< VSFtp system paras initialization was failed.>>>>>>>>>>>>>>");
			return false;
		}
		String[] targetFileCatalog = remoteFileDirectory.split("/");
		StringBuffer tempRemoteFileCatalog = new StringBuffer(remoteFilePath);
		Session session = null;
		Channel channel = null;		
		try{
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<< targetFileName:" + targetFileName + " remoteFileDirectory:" + remoteFileDirectory + ">>>>>>>>>>>>>");
	        JSch jsch = new JSch(); 
	        session=jsch.getSession(username,hostname,getFtpport());
	        session.setPassword(password);  
	        session.setUserInfo(defaultUserInfo);
	        session.connect();
	        channel = session.openChannel("sftp");;   
	        channel.connect();  
	        ChannelSftp cs = (ChannelSftp)channel;	        
			for (String catalog : targetFileCatalog) {
				tempRemoteFileCatalog.append(catalog).append("/");
		        try {
		        	cs.mkdir(tempRemoteFileCatalog.toString());
		        } catch(Exception ex){ }
			}
	        remoteFilePath = tempRemoteFileCatalog.append(targetFileName).toString();
	        //InputStream inFile = new FileInputStream(localFile);
	        URL uri=new URL(url);
	        InputStream inFile=uri.openStream();
	        /**
	         * 
			byte[] buff = new byte[1024];
			while(true) {
				int readed = in.read(buff);
				if(readed == -1) {
					break;
				}
				byte[] tep = new byte[readed];
				System.arraycopy(buff, 0, tep, 0, readed);
				os.write(tep);
			}
	         */
	        if(gWidth > 0) {
		        BufferedImage sourceImg = ImageIO.read(uri);
		        		//ImageIO.read(localFile);
		        // 在文本框内的图片宽度应比文本框本身宽度小100px
		        if(sourceImg.getWidth() > gWidth - 100) {
		        	inFile = getImageStream(zoomOutImage(sourceImg, gWidth - 100));
		        }
	        }
	       
	        //以IO流的方式将文件写入服务器
	        //cs.put(inFile, remoteFilePath, spm, cs.RESUME);
	        //将本地指定文件上传到服务器
	        cs.put(inFile, remoteFilePath);
	        return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if(null != channel) {
					channel.disconnect();
				}
				if(null != session) {
					session.disconnect();
				}
				logger.info("<<<<<<<<<<<<<<<<<<<<<<< VSFtp disconnection was success.>>>>>>>>>>>");
			} catch(Exception er) {
				logger.error("<<<<<<<<<<<<<<<<<<<<<< " + er.getMessage() + ">>>>>>>>>>>>>>");
				return false;
			} finally {
				channel = null;
				session = null;
			}
		}
	}
	/**
	 * 公共参数初始化的put 方法
	 * 使用默认的 hostname,username,password,port ,remoteFilePath
	 */
	public static boolean defaultPut(InputStream is,String picName,String relativePath,int gwidth){
		ResourceBundle bundle=PropertyResourceBundle.getBundle("smsconfig");//ftp配置文件
		String hostname =BasicTypeUtils.notNull(bundle.getString("ftp.server.host"));//ftp IP地址
		String username =BasicTypeUtils.notNull(bundle.getString("ftp.server.user")); //ftp 用户名
		String password =BasicTypeUtils.notNull(bundle.getString("ftp.server.pwd"));//ftp 密码
		String remoteFilePath=BasicTypeUtils.notNull(bundle.getString("ftp.server.path"));//ftp上传路径
		String port = bundle.getString("ftp.server.port");//ftp端口号
		relativePath = BasicTypeUtils.notNull(bundle.getString(relativePath)); //相对路径
		FtpUtils.setFtphost(hostname);
		FtpUtils.setFtpuser(username);
		FtpUtils.setFtppass(password);
		FtpUtils.setFtpport(Integer.parseInt(port));
		FtpUtils.setFtppath(remoteFilePath);
		return put(is, picName, relativePath, gwidth);
	}
	@SuppressWarnings("unused")
	public static boolean put(InputStream in, String targetFileName, 
			String remoteFileDirectory, int gWidth) {		
		String hostname =BasicTypeUtils.notNull(getFtphost());
		String username =BasicTypeUtils.notNull(getFtpuser());  
		String password =BasicTypeUtils.notNull(getFtppass());
		String remoteFilePath=BasicTypeUtils.notNull(getFtppath());	
		//检测VSFTP参数是否初使化正确
		if(hostname.length() == 0 || username.length() == 0 || 
		   password.length() == 0 || remoteFilePath.length() == 0) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<< VSFtp system paras initialization was failed.>>>>>>>>>>>>>>");
			return false;
		}
		String[] targetFileCatalog = remoteFileDirectory.split("/");
		StringBuffer tempRemoteFileCatalog = new StringBuffer(remoteFilePath);
		Session session = null;
		Channel channel = null;		
		try{
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<< targetFileName:" + targetFileName + " remoteFileDirectory:" + remoteFileDirectory + ">>>>>>>>>>>>>");
	        JSch jsch = new JSch(); 
	        session=jsch.getSession(username,hostname,getFtpport());
	        session.setPassword(password);  
	        session.setUserInfo(defaultUserInfo);
	        session.connect();
	        channel = session.openChannel("sftp");;   
	        channel.connect();  
	        ChannelSftp cs = (ChannelSftp)channel;	        
			for (String catalog : targetFileCatalog) {
				tempRemoteFileCatalog.append(catalog).append("/");
		        try {
		        	cs.mkdir(tempRemoteFileCatalog.toString());
		        } catch(Exception ex){ }
			}
	        remoteFilePath = tempRemoteFileCatalog.append(targetFileName).toString();
	        @SuppressWarnings("resource")
			InputStream inFile = in;//new FileInputStream(localFile);
	        if(gWidth > 0) {
		        BufferedImage sourceImg = ImageIO.read(in);
		        // 在文本框内的图片宽度应比文本框本身宽度小100px
		        if(sourceImg.getWidth() > gWidth - 100) {
		        	inFile = getImageStream(zoomOutImage(sourceImg, gWidth - 100));
		        }
	        }
	        SftpProgressMonitor spm = new SftpProgressMonitor() {	        	
				
				public boolean count(long arg0) {
					return false;
				}				
				public void end() {		
				}				
				public void init(int arg0, String arg1, String arg2, long arg3) {
				}
	        };
	        //以IO流的方式将文件写入服务器
	        //cs.put(inFile, remoteFilePath, spm, cs.RESUME);
	        //将本地指定文件上传到服务器
	        cs.put(inFile, remoteFilePath);
	        return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if(null != channel) {
					channel.disconnect();
				}
				if(null != session) {
					session.disconnect();
				}
				logger.info("<<<<<<<<<<<<<<<<<<<<<<< VSFtp disconnection was success.>>>>>>>>>>>");
			} catch(Exception er) {
				logger.error("<<<<<<<<<<<<<<<<<<<<<< " + er.getMessage() + ">>>>>>>>>>>>>>");
				return false;
			} finally {
				channel = null;
				session = null;
			}
		}
	}

	/**
	 * 删除 图片
	 */
	public static boolean removeFile(String fileName, 
			String relativeFilePath) {		
		String hostname =BasicTypeUtils.notNull(getFtphost());
		String username =BasicTypeUtils.notNull(getFtpuser());  
		String password =BasicTypeUtils.notNull(getFtppass());
		String remoteFilePath=BasicTypeUtils.notNull(getFtppath());	
		//检测VSFTP参数是否初使化正确
		if(hostname.length() == 0 || username.length() == 0 || 
		   password.length() == 0 || remoteFilePath.length() == 0) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<< VSFtp system paras initialization was failed.>>>>>>>>>>>>>>");
			return false;
		}
		StringBuffer tempRemoteFileCatalog = new StringBuffer(remoteFilePath);
		Session session = null;
		Channel channel = null;		
		try{
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<< targetFileName:" + fileName + " remoteFileDirectory:" + fileName + ">>>>>>>>>>>>>");
	        JSch jsch = new JSch(); 
	        session=jsch.getSession(username,hostname,getFtpport());
	        session.setPassword(password);  
	        session.setUserInfo(defaultUserInfo);
	        session.connect();
	        channel = session.openChannel("sftp");;   
	        channel.connect();  
	        ChannelSftp cs = (ChannelSftp)channel;	        
	        remoteFilePath = tempRemoteFileCatalog.append(relativeFilePath).toString();
	        cs.cd(remoteFilePath);
	        cs.rm(fileName);
	        return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if(null != channel) {
					channel.disconnect();
				}
				if(null != session) {
					session.disconnect();
				}
				logger.info("<<<<<<<<<<<<<<<<<<<<<<< VSFtp disconnection was success.>>>>>>>>>>>");
			} catch(Exception er) {
				logger.error("<<<<<<<<<<<<<<<<<<<<<< " + er.getMessage() + ">>>>>>>>>>>>>>");
				return false;
			} finally {
				channel = null;
				session = null;
			}
		}
	}
	
}
