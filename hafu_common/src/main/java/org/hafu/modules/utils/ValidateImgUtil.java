package org.hafu.modules.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 生成验证码信息
 * @author Administrator
 *
 */

public class ValidateImgUtil {
	public static Map<String,ResponseEntity<byte[]>> generate() throws IOException{
		int width = 75;
		int height = 25;
		Random random = new Random();
		Color bcolor = new Color(random.nextInt(50)+205,240,240); //背景色
		Color fcolor = getReverseColor(new Color(random.nextInt(25)+100,random.nextInt(25)+100,random.nextInt(25)+100));//字体色
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//访问数据缓存区
		Graphics2D g = bimage.createGraphics();//创建一个 Graphics2D，可以讲它绘制到BufferedImage中
		g.setFont(new Font("宋体", Font.BOLD, 25));
		g.setColor(bcolor);//使用此图形上下文的所有后续图形操作均使用这个指定的颜色
		g.fillRect(0, 0, width, height);//画完背景后设置新的颜色，用于写后面的验证码的颜色
		g.setColor(fcolor);
		StringBuffer sb=new StringBuffer();
		g.rotate(6,23,12);//将当前的 Graphics2D Transform 与平移后的旋转转换连接。
		for(int i=1;i<=4;i++){//写入数据
			int ram=random.nextInt(10);
			sb.append(ram+"");
			g.drawString(ram+"",14*(5-i),30+random.nextInt(4)*2);
			g.translate(-1,-4);
		}
		String vcode = sb.reverse().toString();
		for (int i = 0, n = 80+random.nextInt(100); i < n; i++) { //绘制扰点，大小为1*1 的方形
			g.drawRect(random.nextInt(width+12), random.nextInt(height+15), 1, 1);
		}
		g.dispose();//绘制完毕，关闭资源
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageOutputStream imageOut = ImageIO.createImageOutputStream(output); //生成输出流
		ImageIO.write(bimage, "JPEG", imageOut); //写入输出流
		imageOut.close();
		Map<String,ResponseEntity<byte[]>> map = new HashMap<String, ResponseEntity<byte[]>>();
		ResponseEntity<byte[]> re = null;
    	if(output!=null){
    		HttpHeaders httpHeaders = new HttpHeaders();
    		httpHeaders.set("Pragma", "No-cache");
    		httpHeaders.set("Cache-Control", "No-cache");
    		httpHeaders.setDate(0l);
    		httpHeaders.setContentType(MediaType.IMAGE_JPEG);
    		re = new ResponseEntity<byte[]>(output.toByteArray(),httpHeaders,HttpStatus.OK);
    	}
		map.put(vcode, re);
		return map;
	}
	public static Color getReverseColor(Color c) { //使用反转颜色
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}
	
	
	
	
	
	
}
