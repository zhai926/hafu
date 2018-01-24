package org.hafu.modules.utils;

import java.util.List;

public class MyPageBean<T> {
	public static final int DEFAULT_PAGESIZE = 5;
	private int totalPageNum;
	private int totalResultNum;
	private int currentPage;
	private int pageSize;
	private List<T> currentResults;
	private int fromPos = 0;
	private int toPos = 0;
	private String jsFunction;
	
	
	public MyPageBean(int totalResultNum, int pageIndex,List<T> resultList){
		this(totalResultNum, pageIndex, DEFAULT_PAGESIZE,resultList);
	}
	
	//url 提交
	public MyPageBean(int totalResultNum, int pageIndex, int pageSize,List<T> resultList){
		this.totalResultNum = totalResultNum;
		this.pageSize = pageSize;
		this.totalPageNum = (int) Math.ceil(totalResultNum / (double) pageSize); 
		if(pageIndex > totalPageNum ){
			pageIndex = totalPageNum;
		}
		if(pageIndex < 1 ){
			pageIndex = 1;
		}
		this.currentPage = pageIndex;
		getPageHtml();//获得分页 效果
		this.currentResults = resultList;
	}
	
	//表单提交
	public MyPageBean(int totalResultNum, int pageIndex, int pageSize,List<T> resultList,String jsFunction){
		this.jsFunction = jsFunction;
		this.totalResultNum = totalResultNum;
		this.pageSize = pageSize;
		this.totalPageNum = (int) Math.ceil(totalResultNum / (double) pageSize); 
		if(pageIndex > totalPageNum ){
			pageIndex = totalPageNum;
		}
		if(pageIndex < 1 ){
			pageIndex = 1;
		}
		this.currentPage = pageIndex;
		getJsPageHtml();//获得分页 效果
		this.currentResults = resultList;
	}
	
	
	
	
	/**
	 * 获得分页 html
	 * @return
	 */
	public String getPageHtml(){
		int totalPageNum = this.totalPageNum;
		StringBuffer strb = new StringBuffer();
		strb.append("<div class=\"pagenum\">");
		
		if(getCurrentPage() > 1){
			appendPrevious(strb);
		} else {
			appendNoPrevious(strb);
		}
		if(totalPageNum <= 5){
			appendPage(strb, totalPageNum);
		}else{
			if(getCurrentPage() < 5){
				appendPage(strb, 5);
			}
			else{
				strb.append(String.format(getPageNumPattern(), getToPageHref(getCurrentPage() - 3), getCssClass1(), getCurrentPage() - 3));
				strb.append(String.format(getPageNumPattern(), getToPageHref(getCurrentPage() - 2), getCssClass1(), getCurrentPage() - 2));
				strb.append(String.format(getPageNumPattern(), getToPageHref(getCurrentPage() - 1), getCssClass1(), getCurrentPage() - 1));
				strb.append(String.format(getPageNumPattern(), getToPageHref(getCurrentPage()), getCssClass2(), getCurrentPage()));
				if(getCurrentPage() < totalPageNum){
					strb.append(String.format(getPageNumPattern(), getToPageHref(getCurrentPage() + 1), getCssClass1(), getCurrentPage() + 1));
				}
			}
		}
		if(getCurrentPage() < totalPageNum){
			appendNext(strb);
		} else {
			appendNoNext(strb);
		}
		
		//append总共多少页
		appendTotalPage(strb);	
		strb.append("</div>");//pagination div end
		return strb.toString();
	}
	
	
	/**
	 * 获得javascipt 提交 分页 html
	 * @return
	 */
	public String getJsPageHtml(){
		int totalPageNum = this.totalPageNum;
		StringBuffer strb = new StringBuffer();
		strb.append("<div class=\"pagenum\">");
		
		if(getCurrentPage() > 1){
			//appendPrevious(strb);
			strb.append(String.format("<span><a class='prev on' href='%s'>上一页</a></span>",this.getJSToPageHref(getCurrentPage() - 1)));
		} else {
			appendNoPrevious(strb);
		}
		if(totalPageNum <= 5){
			//appendPage(strb, totalPageNum);
			appendJsPage(strb,totalPageNum);
		}else{
			if(getCurrentPage() < 5){
				//appendPage(strb, 5);
				appendJsPage(strb,5);
			}
			else{
				strb.append(String.format(getPageNumPattern(), getJSToPageHref(getCurrentPage() - 3), getCssClass1(), getCurrentPage() - 3));
				strb.append(String.format(getPageNumPattern(), getJSToPageHref(getCurrentPage() - 2), getCssClass1(), getCurrentPage() - 2));
				strb.append(String.format(getPageNumPattern(), getJSToPageHref(getCurrentPage() - 1), getCssClass1(), getCurrentPage() - 1));
				strb.append(String.format(getPageNumPattern(), getJSToPageHref(getCurrentPage()), getCssClass2(), getCurrentPage()));
				if(getCurrentPage() < totalPageNum){
					strb.append(String.format(getPageNumPattern(), getJSToPageHref(getCurrentPage() + 1), getCssClass1(), getCurrentPage() + 1));
				}
			}
		}
		if(getCurrentPage() < totalPageNum){
			//appendNext(strb);
			strb.append(String.format("<span><a class='next on' href='%s'>下一页</a></span>",this.getJSToPageHref(getCurrentPage() + 1)));
		} else {
			appendNoNext(strb);
		}
		
		//append总共多少页
		appendTotalPage(strb);	
		strb.append("</div>");//pagination div end
		return strb.toString();
	}
	
	/**
	 * 获得合法的 当前页 
	 */
	public int lawCurrentPage(int pageNum) {
		if(pageNum > this.getTotalPageNum() ){
			pageNum = this.getTotalPageNum();
		}
		if(pageNum < 1 ){
			pageNum = 1;
		}
		return pageNum;
	}
	
	/**
	 * 没有选中的样式
	 * @return
	 */
	protected String getCssClass1(){
		return "";
	}
	
	/**
	 * 选中后的样式
	 * @return
	 */
	protected String getCssClass2(){
		return "on";
	}
	
	protected void appendPage(StringBuffer strb, int count){
		String cssClass = getCssClass1();
		for(int i = 1; i <= count; i++){
			if(i == getCurrentPage()){
				cssClass = getCssClass2();
			}else{
				cssClass = getCssClass1();
			}
			strb.append(String.format(getPageNumPattern(), getToPageHref(i), cssClass, i));
		}
	}
	protected String pageNumPattern = "<span><a href='%s' class='%s'>%s</a></span>";
	private String pageTotalPattern = "<span>共 <i>%d</i> 页 </span>";
	private String pageToPattern = "到第<span class=\"ipt\"><input id='idToPage' type=\"text\"> 页</span><span><input onclick='toPage();' type=\"button\" value=\"确定\"></span>";
	protected String getToPageHref(int i) {
		//TODO
		String url ="";
		return url + i;
	}
	protected String getJSToPageHref(int i) {
		//TODO
		String url = jsFunction;
		return String.format(url, i);
	}
	protected void appendJsPage(StringBuffer strb, int count){
		String cssClass = getCssClass1();
		for(int i = 1; i <= count; i++){
			if(i == getCurrentPage()){
				cssClass = getCssClass2();
			}else{
				cssClass = getCssClass1();
			}
			strb.append(String.format(getPageNumPattern(), getJSToPageHref(i), cssClass, i));
		}
	}
	protected void appendPrevious(StringBuffer strb){
		strb.append(String.format("<span><a class='prev on' href='%s'>上一页</a></span>",this.getToPageHref(getCurrentPage() - 1)));
	}
	
	protected void appendNoPrevious(StringBuffer strb){
		strb.append("<span><a class='prev' href='javascript:void(0);'>上一页</a></span>");
	}
	protected void appendNext(StringBuffer strb){
		strb.append(String.format("<span><a class='next on' href='%s'>下一页</a></span>",this.getToPageHref(getCurrentPage() + 1)));
	}
	protected void appendNoNext(StringBuffer strb){
		strb.append("<span><a class='next' href='javascript:void(0);'>下一页</a></span>");
	}
	protected void appendTotalPage(StringBuffer strb) {
		strb.append(String.format(pageTotalPattern, totalPageNum));
	}
	
	
	protected String getPageNumPattern(){
		return pageNumPattern;
	}
	public int getTotalPageNum() {
		return totalPageNum;
	}
	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public int getTotalResultNum() {
		return totalResultNum;
	}
	public void setTotalResultNum(int totalResultNum) {
		this.totalResultNum = totalResultNum;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getCurrentResults() {
		return currentResults;
	}

	public void setCurrentResults(List<T> currentResults) {
		this.currentResults = currentResults;
	}
}
