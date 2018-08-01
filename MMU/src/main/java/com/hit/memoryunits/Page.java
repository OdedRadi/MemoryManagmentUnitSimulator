package com.hit.memoryunits;

import java.io.Serializable;
import java.util.Arrays;

public class Page<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private T content;

	public Page(Long id, T content) {
		setPageId(id);
		setContent(content);
	}

	public Page(Page<T> page) {
		setPageId(page.getPageId());
		setContent(page.getContent());
	}

	public boolean equals(Object obj) {
		
		if (this.hashCode() == obj.hashCode())
			return true;
		
		return false;
	}

	public T getContent() {
		return content;
	}

	public Long getPageId() {
		return id;
	}

	public int hashCode() {
		return id.hashCode();
	}

	public void setContent(T content) {
		this.content = content;
	}

	public void setPageId(Long pageId) {
		this.id = pageId;
	}

	public String toString() {
		byte[] array = (byte[])content;
		String s = new String(id + " " + Arrays.toString(array));
		return s;
	}
}
