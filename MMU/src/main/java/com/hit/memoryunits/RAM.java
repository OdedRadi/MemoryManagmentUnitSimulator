package com.hit.memoryunits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hit.exceptions.PageNotFoundExcpetion;

public class RAM {

	private HashMap<Long, Page<byte[]>> pagesMap;
	private int capacity; 
	
	RAM(int initialCapacity){
		pagesMap = new HashMap<Long, Page<byte[]>>(initialCapacity);
		capacity = initialCapacity;
	}
	
	public void addPage(Page<byte[]> addPage) {
		if (!pagesMap.containsKey(addPage.getPageId()))
			pagesMap.put(addPage.getPageId(), addPage);
	}
	
	public void addPages(Page<byte[]>[] addPages) {
		for (int i = 0; i < addPages.length; i++) 
			addPage(addPages[i]);		
	}
	
	public int getInitialCapacity() {
		return capacity;		
	}
	
	public Page<byte[]> getPage(Long pageId) throws PageNotFoundExcpetion{

		if (!pagesMap.containsKey(pageId))
			throw new PageNotFoundExcpetion();
		
		return pagesMap.get(pageId);
	}
	
	public Map<Long, Page<byte[]>> getPages(){
		return pagesMap;	
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds) throws PageNotFoundExcpetion{
		@SuppressWarnings("unchecked")
		Page<byte[]>[] returnArray = (Page<byte[]>[]) new Page[pageIds.length];
				
		for (int i = 0; i < pageIds.length; i++) 
				returnArray[i] = getPage(pageIds[i]);
		
		return returnArray;
	}
	
	public void removePage(Page<byte[]> removePage) {
		if (pagesMap.containsKey(removePage.getPageId()))
			pagesMap.remove(removePage.getPageId());
	}
	
	public void removePages(Page<byte[]>[] removePages) {
		for (int i=0; i<removePages.length; i++)
			removePage(removePages[i]);
	}
	
	public void setInitialCapacity(int initialCapacity) {
		ArrayList<Page<byte[]>> pagesArray = (ArrayList<Page<byte[]>>) pagesMap.values();
		
		pagesMap = new HashMap<Long, Page<byte[]>>(initialCapacity);
		
		for (int i = 0; i < initialCapacity; i++) {
			if (i > capacity) 
				pagesMap.put(null, null);
			else 
				addPage(pagesArray.get(i));
		}
		
		capacity = initialCapacity;
	}
	
	public void setPages(Map<Long, Page<byte[]>> pages) {
		pagesMap = (HashMap<Long, Page<byte[]>>) pages;	
	}	
}
