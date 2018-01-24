package org.hafu.modules.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义 jstl 标签 
 * @author zc
 *
 */

public class CustomDefinedJstl {
	
	/**
	 * 过滤html标签
	 * 包含 script style html
	 */
	public static String filterHtmlTags(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		if(null == htmlStr || "".equals(htmlStr)){
			return "";
		}else{
			try {
				// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
				String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
				
				// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
				String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
				
				String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
				String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
				
				
				p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
				m_script = p_script.matcher(htmlStr);
				htmlStr = m_script.replaceAll(""); // 过滤script标签

				p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
				m_style = p_style.matcher(htmlStr);
				htmlStr = m_style.replaceAll(""); // 过滤style标签

				p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
				m_html = p_html.matcher(htmlStr);
				htmlStr = m_html.replaceAll(""); // 过滤html标签

				p_html = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
		        m_html = p_html.matcher(htmlStr);  
		        htmlStr = m_html.replaceAll(""); // 过滤空格回车标签  
				
				textStr = htmlStr.replaceAll("&nbsp;", "");

			} catch (Exception e) {
				System.err.println("Html2Text: " + e.getMessage());
			}
			return textStr.trim();// 返回文本字符串
		}
		
	}
	
	/**
	 * 过滤html标签 ，截取字符串
	 * @param inputString
	 * @return
	 */
	public static String filterHtmlTags(String inputString,int length) {
		String result = filterHtmlTags(inputString);
		if(result.length()<=length){
			return result;
		}else{
			String regEx = "[\\u4e00-\\u9fa5]"; 
			Pattern pattern = Pattern.compile(regEx);
			Matcher m = pattern.matcher(result.substring(length-2, length));
			int sub = 0;
			if(m.find()){
				sub = 2;
			}else{
				sub = 3;
			}
			int no = length - sub ;
			String subend = result.substring(0,no);
			return subend+"...";
		}	
	}
	
}
