package org.hafu.modules.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/** 
 * MD5 加密 
 */  
public class MD5 {
	
	public static final Map<String, Integer> companysToQxs = new HashMap<String, Integer>();

	public static String getMD5Str (String str) { 
        MessageDigest messageDigest = null;  
  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            e.getMessage();  
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    } 
	
	public static void main(String[] args)
	{
		//http://service.cjdao.com/p2p/saveproduct?acceptinvestmentmoney=100000000.0&companyname=%E9%87%91%E9%93%B6%E7%8C%AB&creditrating=a&expectedrate=6.4&incomeway=4&loandeadline=90&md5_value=%F0%8AJ%0D%EF%5C%21%CC%BC%B1%ECN%E8+a%87%3E%BD%CF%D3%95%91yt%C8%C7%5C1%DC%0Av%BC&productname=%E9%93%B6%E4%BC%81%E4%BC%97%E8%B5%A2&risktype=1&startinvestmentmoney=1000.0&thirdproductid=1
		System.out.println(MD5.getMD5Str("uintec123456"));
		/*String md5 = MD5.getMD5Str("xiaoxiao"+"15801932811"+"2005"+"1385"+"38"+"50.0"+"0"+"1234");
		System.out.println("md5="+md5);*/
		//expectedrate=0.11, buytime=2014-01-22, phone=15223371861, idcard=, usertype=0, transactionstatus=0, companyid=2016, uaccount=cjd_xn, productid=183, money=100, ordercode=362720605151170560, md5_value=89951cec9372aba3efb031d81d00c212, productname=小牛在线测试
		//buytime=2014-02-25&companyid=2015&expectedrate=0.075&md5_value=74d959a44c4950c6dce9868dd1814db2&money=1.0&ordercode=140225156639&phone=15800911854&productid=844&productname=“银企众盈”融资项目第xxxxxxxxxxxxx期&transactionstatus=2&uaccount=15800911854&usertype=1
		/*String md51 = MD5.getMD5Str("15800911854"+"15800911854"+"1"+"2015"+"844"+"“银企众盈”融资项目第xxxxxxxxxxxxx期"+"2014-02-25"+"1.0"+"0.075"+"2"+"140225156639"+"1234");
		System.out.println("md51="+md51);*/
		//thirdproductid=322&productname=test001&companyname=积木盒子&startinvestmentmoney=100&acceptinvestmentmoney=500000&loandeadline=12&expectedrate=0.12&risktype=1&incomeWay=2&handlingcharge=0.0&creditrating=a
		//StringBuilder sb = new StringBuilder();
		//thirdproductid=3333&productname=定存宝C130224&companyname=有利网&startinvestmentmoney=1000.00&acceptinvestmentmoney=30&loandeadline=12.5&deadlineunit=1&expectedrate=11.00&risktype=1&incomeway=2&handlingcharge=0&creditrating=a
		/*sb.append("3333").append("定存宝C130224").append("有利网").append("1000.00").append("30").append("12.51").append("11.00").append("1").append("2").append("0").append("a").append("1234");
		String md52 = MD5.getMD5Str(sb.toString());
		System.out.println("md52="+md52);*/
		//phone=18103850081uaccount=peterhallpid=FB0000004524productname=招银进宝之点贷成金步步生金1号理财计划amount=1.00ordercode=PAY2014050500000802
		//investAmount=1.00payTime=3.70tranStatus=2companyid=2060realincome=expectedrate=md5_value=1BE0CF97BBC506C618126BB16E061BBC+"2060"
		/*md51 = MD5.getMD5Str("peterhall"+"18103850081"+""+"FB0000004524"+"招银进宝之点贷成金步步生金1号理财计划"+"PAY20140505000008021.003.70"+"220601234");
		System.out.println("md51="+md51);
		
		sb.setLength(0);
		sb.append("18103850081peterhallFB0000004524招银进宝之点贷成金步步生金1号理财计划1.00PAY20140505000008021.0020140514161423220603.701234");
		String md53 = MD5.getMD5Str(sb.toString());
		System.out.println("md53="+md53);
		
		String md511 = MD5.getMD5Str("银企众赢金银猫1234");
		System.out.println("md511="+md511);
		
		String md533 = MD5.getMD5Str("15801932811"+"fanxiaodong"+"1"+""+"xiaoxiao"+"1234");
		System.out.println("md533="+md533);
		
		System.out.println("md544="+(int)Math.floor(Double.valueOf("2.00")));*/
		/*String expectedrate= 0.5002+"";
		
		if(null!=expectedrate&&expectedrate.length()>0)
		{
			String temp = expectedrate.substring(expectedrate.indexOf("."));
			if(temp.length()>4)
			{
				String rateTemp = expectedrate.substring(0,expectedrate.indexOf(".")+5);
				System.out.println("rateTemp="+rateTemp);
			}
			else{
				System.out.println("expectedrate="+expectedrate);
			}
				
		}	*/
		//System.out.println(getWebContent("http://test.cjdao.com/p2p/saveproduct?thirdproductid=3&productname=定存宝C140224&companyname=有利网&startinvestmentmoney=1000.00&acceptinvestmentmoney=0.00&loandeadline=12&deadlineunit=1&expectedrate=11.00&risktype=1&incomeway=2&handlingcharge=0&creditrating=a&md5_value=3b885a0c432a39cf0647ccf33461ca42"));
	}
	
	public static String getWebContent(String urlString) {
		try {
			URLEncoder.encode(urlString, "UTF-8");
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(3000);
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.connect();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line=bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			bufferedReader.close();
			return sb.toString();
		} catch (Exception e) {
			System.err.println("获取API返回内容异常:" + urlString);
			e.printStackTrace();
		}
	
		return null;
	}
	

	static {
		companysToQxs.put("ICBC", 210);
		companysToQxs.put("CCB", 213);
		companysToQxs.put("CMB", 215);
		companysToQxs.put("BOCOM", 214);
		companysToQxs.put("BOC", 212);
		companysToQxs.put("ABC", 211);
		companysToQxs.put("HXB", 220);
		companysToQxs.put("CGB", 222);
		companysToQxs.put("CMBC", 218);
		companysToQxs.put("CIB", 217);
		companysToQxs.put("CNCB", 441);
		companysToQxs.put("BOB", 224);
		companysToQxs.put("CEB", 219);
		companysToQxs.put("BOS", 226);
		companysToQxs.put("SPDB", 216);
		companysToQxs.put("PAB", 221);
		companysToQxs.put("PSBC", 232);
		companysToQxs.put("SRCB", 231);
		companysToQxs.put("XIB", 415);
		companysToQxs.put("JSB", 229);
		companysToQxs.put("BSB", 247);
		companysToQxs.put("CBHB", 228);
		companysToQxs.put("NBCB", 369);
		companysToQxs.put("NJCB", 227);
		companysToQxs.put("HZB", 312);
		companysToQxs.put("CZB", 223);
		companysToQxs.put("TIANJIN", 225);
		companysToQxs.put("BUDL", 263);
		companysToQxs.put("WZCB", 408);
		companysToQxs.put("CZCB", 259);
		companysToQxs.put("ZJTLCB", 398);
		companysToQxs.put("GZCB", 305);
		companysToQxs.put("PINGAN", 221);
	}
    
}
