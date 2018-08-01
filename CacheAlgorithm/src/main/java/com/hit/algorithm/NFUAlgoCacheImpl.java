package com.hit.algorithm;

import java.util.HashMap;
import java.util.Iterator;

public class NFUAlgoCacheImpl<K, V> extends AbstractAlgoCache<K, V> {

	private HashMap<K,Integer> countMap;
	private K lowestKey;
	
	public NFUAlgoCacheImpl(int capacity) {	
		super(capacity);
		countMap = new HashMap<K, Integer>(capacity);
	}

	@Override
	public V getElement(K key) {
		if (hashMap.containsKey(key)){
			Integer counter;
			counter = countMap.get(key);
			counter++;
			countMap.replace(key, counter);
			return hashMap.get(key);
		}
		
		return null;		
	}

	@Override
	public V putElement(K key, V value) {

		if (capacity == 0)
			return null;
		
		if (hashMap.isEmpty()) {
			hashMap.put(key, value);
			countMap.put(key, 1);
			lowestKey = key;
			return value;
		}
		
		V element = getElement(key);
		V returnValue = hashMap.get(lowestKey);
		
		if (element == null) {	
			if (countMap.get(lowestKey) > 1) {
				lowestKey = key;
			}
			
			if (hashMap.size() == capacity){
				removeElement(lowestKey);
				lowestKey = key;
			}
			
			countMap.put(key, 1);
			hashMap.put(key, value);
		}
		
		else if (key == lowestKey) {
			Iterator<K> it = countMap.keySet().iterator();
			
			int lowestKeyCount = countMap.get(lowestKey);
			
			while (it.hasNext()) {
				K currentKey = it.next();
				int currentKeyCount = countMap.get(currentKey);
				
				if (currentKeyCount <= lowestKeyCount){
					lowestKey = currentKey;
					lowestKeyCount = currentKeyCount;
				}
			}
		}

		return returnValue;		
	}

	@Override
	public void removeElement(K key) {
		countMap.remove(key);
		hashMap.remove(key);
	}

}
