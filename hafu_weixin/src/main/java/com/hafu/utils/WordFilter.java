package com.hafu.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordFilter {
	private static final String SEARCH_ONE="wordCensor.properties";
	private static Pattern pattern = null;
	private static Matcher m = null;
	static{
		StringBuffer strs = new StringBuffer("");
		  //String str = "SEARCH_";
		  try {
			   InputStream in = WordFilter.class.getClassLoader().getResourceAsStream(SEARCH_ONE);	//获取敏感词条
			   BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			   String str="";
			   int j=0;
			   Set<String> set=new HashSet<String>();
			   while((str=reader.readLine())!=null){
				   j++;
				   //将逗号，空格隔开的词分成两个词
				   String[] tempStr=str.split("[\\,\\|\\s]");
				   if(tempStr!=null){
					   for(int i=0;i<tempStr.length;i++){
						   if(tempStr[i]!=null&&!tempStr[i].trim().equals("")){
							   set.add(tempStr[i].replaceAll("[^a-zA-Z\\d\\u4e00-\\u9fa5]", ""));   
						   }
					   }
				   }
			   }
			   StringBuffer buffer=new StringBuffer();
			   if(set!=null&&!set.isEmpty()){
				   buffer.append("(");
				   for(String s:set){
					   if(s.equals("*")){
						   continue;
					   }else if(s.trim().equals("")){
						   continue;
					   }
					   buffer.append(s+"|");
				   }
				   buffer.deleteCharAt(buffer.length()-1);
				   buffer.append(")");
			   }
			   pattern=Pattern.compile(buffer.toString());
		  } catch(IOException ioEx) {
			  ioEx.printStackTrace();
		  }
	}
	
	public static String filter(String content){
			m = pattern.matcher(content);
			while(m.find()){
				String str=m.group();
				int length=str.length();
				StringBuffer buffer=new StringBuffer();
				for(int i=0;i<length;i++){
					buffer.append("*");
				}
				content=content.replaceAll(str,buffer.toString());
				m = pattern.matcher(content);
			}
			return content;
	}
	public static void main(String[] args) {
		String content="商务信息网";
		content=WordFilter.filter(content);
		System.out.println(content);
	}
}
