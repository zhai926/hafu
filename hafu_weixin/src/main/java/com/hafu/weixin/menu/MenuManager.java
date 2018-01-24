package com.hafu.weixin.menu;

import com.hafu.weixin.WeixinCommon;
import com.hafu.weixin.menu.pojo.Button;
import com.hafu.weixin.menu.pojo.ComplexButton;
import com.hafu.weixin.menu.pojo.Menu;
import com.hafu.weixin.menu.pojo.ViewButton;

/**
 * 菜单管理器 类
 * @author Dong
 *
 */
public class MenuManager {
    /** 
     * 组装菜单数据 
     *  
     * @return 
     */  
    public static Menu getMenu() {
        // 第二个主菜单  view 类型子button    	
        ViewButton viewBtn11 = new ViewButton();
        viewBtn11.setName("首页");
        viewBtn11.setType("view");
        viewBtn11.setUrl(WeixinCommon.MENU_BASE + "/index/index");
        
        ViewButton viewBtn21 = new ViewButton();
        viewBtn21.setName("数据中心");
        viewBtn21.setType("view");
        viewBtn21.setUrl(WeixinCommon.MENU_BASE + "/health/dataCompare");
    	
    	
        ViewButton viewBtn31 = new ViewButton();
        viewBtn31.setName("设置");
        viewBtn31.setType("view");
        viewBtn31.setUrl(WeixinCommon.MENU_BASE + "/index/device_user_list");
         

        // 主菜单
        /*ComplexButton mainBtn1 = new ComplexButton();  
        mainBtn1.setName("我的账号");
        mainBtn1.setSub_button(new ViewButton[] { viewBtn11,viewBtn12 });*/        
  
        
        /** 
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br> 
         *  
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br> 
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br> 
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 }); 
         */  
        Menu menu = new Menu();  
        menu.setButton(new Button[] { viewBtn11, viewBtn21, viewBtn31 });  
  
        return menu;  
    }  
    
}
