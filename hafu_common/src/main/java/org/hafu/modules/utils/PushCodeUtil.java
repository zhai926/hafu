package org.hafu.modules.utils;

import java.util.Random;

public class PushCodeUtil {
	public static final String getPushCode(){
		StringBuffer genP = new StringBuffer();
		//48-57 数字0-9 65～90 字母A-Z 97～122 字母a-z 共62
		Random r = new Random();
		Integer ri = null;
		for(int i = 0;i < 6;i ++) {
			ri = Math.abs(r.nextInt()) % 62; //62
			ri = ri <= 9 ? 48 + ri : ri <= 35 ? 55 + ri : 61 + ri;
			genP.append((char) ri.intValue());
		}
		return genP.toString().toLowerCase();
	}
}
