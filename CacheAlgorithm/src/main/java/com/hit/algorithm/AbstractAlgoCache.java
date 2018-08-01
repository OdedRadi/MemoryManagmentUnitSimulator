package com.hit.algorithm;

import java.util.HashMap;


public abstract class AbstractAlgoCache<K,V> implements IAlgoCache<K, V>{

	public AbstractAlgoCache(int capacity){
		this.capacity = capacity;
		hashMap = new HashMap<K, V>(capacity);
	}

	protected HashMap<K, V> hashMap;
	protected int capacity;
}