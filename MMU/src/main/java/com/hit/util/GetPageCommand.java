package com.hit.util;

import java.util.ArrayList;

public class GetPageCommand {
	private String processNum;
	private String pageNum;
	private ArrayList<Integer> data;
	
	public GetPageCommand(String processNum, String pageNum, String[] data) {
		this.data = new ArrayList<Integer>();
		
		setProcessNum(processNum);
		setPageNum(pageNum);
		setData(data);
	}
	
	public void setProcessNum(String processNum) {
		this.processNum = processNum;
	}
	
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	
	public void setData(String[] newData) {
		for(int i=0; i < newData.length; i++)
			data.add(Integer.parseInt(newData[i]));	
	}
	
	public String getProcessNum() {
		return processNum;
	}
	
	public String getPageNum() {
		return pageNum;
	}
	
	public int getDataSize() {
		return data.size();
	}
	
	public int getData(int index) {
		return data.get(index).intValue();
	}
}
