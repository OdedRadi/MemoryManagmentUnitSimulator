package com.hit.algorithm;

import java.util.ArrayList;

public class LRUAlgoCacheImpl<K, V> extends AbstractAlgoCache<K, V> {

	private ArrayList<K> que;
	
	public LRUAlgoCacheImpl(int capacity) {
		super(capacity);
		que = new ArrayList<K>(capacity);
	}

	@Override
	public V getElement(K key) {	
		// TODO Auto-generated method stub
		if (hashMap.containsKey(key)){
			que.remove(key);
			que.add(key);
			return hashMap.get(key);
		}
		
		return null;
	}

	@Override
	public V putElement(K key, V value) {
		// TODO Auto-generated method stub
		
		if (capacity == 0)
			return null;
		
		if (hashMap.isEmpty()) {
			hashMap.put(key, value);
			que.add(key);
			return value;
		}		
		
		V element = getElement(key);
		V returnValue = hashMap.get(que.get(0));
		
		if (element == null) {
			if (hashMap.size() == capacity){
				removeElement(que.get(0));
			}
			
			hashMap.put(key, value);
			que.add(key);
		}
		
		return returnValue;
	}

	@Override
	public void removeElement(K key) {
		// TODO Auto-generated method stub
		que.remove(key);
		hashMap.remove(key);
	}
}
