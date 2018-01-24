package org.hafu.modules.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MailSender {
	private static final Logger logger=Logger.getLogger(MailSender.class);
	private String mailServerHost;
	private String mailServerPort="25";
	private String userName;
	private String password;
	private String fromAddress;
	private boolean validate=false;
	private Properties getProperties(){
		Properties p=new Properties();
		p.setProperty("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}
	
	/**
	 * 发送邮件
	 * @param subject
	 * @param content
	 * @param toAddressList
	 * @return
	 */
	public boolean sendHtmlEmail(String subject,String content,String email){
		MyAuthenticator authenticator=null;
		Properties pro=getProperties();
		if(validate){
			authenticator=new MyAuthenticator(userName, password);
		}
		Session session=Session.getDefaultInstance(pro, authenticator);
		try {
			Message message=new MimeMessage(session);
			Address from=new InternetAddress(fromAddress);
			message.setFrom(from);
			message.setSubject(subject);
			message.setSentDate(new Date());
			Multipart mainPart = new MimeMultipart();
			BodyPart html = new MimeBodyPart();
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			message.setContent(mainPart);
			Address to = new InternetAddress(email);
			message.setRecipient(Message.RecipientType.TO, to);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[subscribe : MailSender]: 发送邮件时出错。");
		}
		return true;
	}


	public String getMailServerHost() {
		return mailServerHost;
	}


	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}


	public String getMailServerPort() {
		return mailServerPort;
	}


	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFromAddress() {
		return fromAddress;
	}


	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}


	public boolean isValidate() {
		return validate;
	}


	public void setValidate(boolean validate) {
		this.validate = validate;
	}


	public static Logger getLogger() {
		return logger;
	}
	
	
}
