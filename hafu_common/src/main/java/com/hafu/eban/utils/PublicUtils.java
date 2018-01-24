package com.hafu.eban.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.hafu.eban.entity.response.DayInfoResponse_Location;
import com.hafu.eban.entity.response.DayInfoResponse_Mask1;

public class PublicUtils {
	
	private static Logger logger = Logger.getLogger(PublicUtils.class);
	
	/*public static void main(String[] args) {
		String str = "#Dot|121.515524|31.140411|永泰路金谊河畔|15|1|26166#Line|121.563912|31.20551|芳甸路永达国际大厦|550|1|49969#Dot|121.560132|31.209712|梅花路吸铁石短租公寓|15|1|53811#Line|121.541749|31.212397|花木路香梅花园精制家具|92|1|58144#Line|121.534981|31.213759|花木路陆家嘴世纪金融广场1号楼|550|1|58449#Dot|121.525184|31.232988|商城路福沈居委会|15|1|59371#Line|121.543663|31.235764|张杨路民生路|550|1|60637#Line|121.545674|31.229013|灵山路进才中学|550|1|60955#Dot|121.559431|31.226972|芳甸路巴黎贝甜|15|1|64080#Line|121.534981|31.213759|花木路陆家嘴世纪金融广场1号楼|550|1|70306#Line|121.539298|31.193279|锦绣路星河湾商业街|550|1|70644#Line|121.545441|31.169415|锦绣路老万县面|550|1|71053#Dot|121.507595|31.148492|联明路荆艺软膜天花工厂|550|1|71565#Line|121.515499|31.141626|池河路金谊河畔|550|1|71980#Dot|121.515524|31.140411|永泰路金谊河畔|15|1|72753";
		String str1 = "#Dot|0-709|在家,收拾,#none|709-1723|永泰路金谊河畔,未佩戴,#Dot|1723-47565|在家,收拾,#out|47565-47565|永泰路金谊河畔,上车,#out|47565-51387|,乘车中,#out|51387-51387|梅花路吸铁石短租公寓,下车,#none|51387-52122|梅花路吸铁石短租公寓,未佩戴,#Dot|52122-57414|在梅花路吸铁石短租公寓附近,坐着休息,#out|57414-57414|梅花路吸铁石短租公寓,出门,#out|57414-61698|,走路中,#out|61698-61698|芳甸路巴黎贝甜,进门,#Dot|61698-69421|在芳甸路巴黎贝甜附近,坐着休息,#out|69421-69421|芳甸路巴黎贝甜,上车,#out|69421-72274|,乘车中,#out|72274-72274|永泰路金谊河畔,下车,#Dot|72274-76481|在家,做家务,#none|76481-86196|永泰路金谊河畔,未佩戴,#Dot|86196-86399|在家,休息,";
		getDayInfoResponse_Mask1(str1);
	}*/

	public static List<DayInfoResponse_Location> getDayInfoResponse_Location(
			String location) {
		List<DayInfoResponse_Location> list = new ArrayList<DayInfoResponse_Location>();
		try{
			logger.debug("<<<<DayInfoResponse_Location:"+location);
			String locations[] = location.split("#");
			for (int i = 0; i < locations.length; i++) {
				String param = locations[i];
				if(StringUtils.isNotBlank(param)){
					String params[] = param.split("\\|");
					DayInfoResponse_Location l = new DayInfoResponse_Location();
					l.setAccuracy(params[4]);
					l.setAttribute(params[0]);
					l.setCoordinate(params[5]);
					l.setTime(secToTime(params[6]));
					l.setLatitude(params[2]);
					l.setLongitude(params[1]);
					l.setLocation(params[3]);
					list.add(l);
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return list;
	}
	
	public static List<DayInfoResponse_Mask1> getDayInfoResponse_Mask1(String mask1){
		List<DayInfoResponse_Mask1> list = new ArrayList<DayInfoResponse_Mask1>();
		String mask1s[] = mask1.split("#");
		for (int i = 0; i < mask1s.length; i++) {
			String param = mask1s[i];
			if(StringUtils.isNotBlank(param)){
				String params[] = param.split("\\|");
				DayInfoResponse_Mask1 l = new DayInfoResponse_Mask1();
				l.setAttribute(params[0]);
				String time  = params[1];
				String startTime = time.substring(0,time.indexOf("-"));
				String endTime = time.substring(time.indexOf("-")+1,time.length());
				l.setStartTime(secToTime(startTime));
				l.setEndTime(secToTime(endTime));
				try{
					l.setBehaviorType(params[2]);
				}catch(Exception e){
					l.setBehaviorType("");
				}
				
				list.add(l);
			}
		}
		return list;
	}
	
	
	
	public static String secToTime(String time) {  
		Integer times = Integer.valueOf(time);
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (times <= 0)  
            return "00:00:00";  
        else {  
            minute = times / 60;  
            if (minute < 60) {  
                second = times % 60;  
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = times - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
  
    public static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }  

}
