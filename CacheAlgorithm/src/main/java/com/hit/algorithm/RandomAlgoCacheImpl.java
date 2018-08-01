package com.hit.algorithm;

import java.util.ArrayList;
import java.util.Set;

public class RandomAlgoCacheImpl<K, V> extends AbstractAlgoCache<K, V> {

	public RandomAlgoCacheImpl(int capacity) {
		super(capacity);
	}

	@Override
	public V getElement(K key) {
		// TODO Auto-generated method stub
		return hashMap.get(key);
	}

	@Override
	public V putElement(K key, V value) {
		// TODO Auto-generated method stub
		if (capacity == 0)
			return null;
		
		if (hashMap.isEmpty()) {
			hashMap.put(key, value);
			return value;
		}
			
		java.util.Random r = new java.util.Random();
					
		Set<K> set = hashMap.keySet();
		ArrayList<K> keys = new ArrayList<K>(set);
		
		K randomKey = keys.get(r.nextInt(keys.size()));
		V returnValue = hashMap.get(randomKey);
		
		if (!hashMap.containsKey(key)) {
			if (hashMap.size() == capacity) {
				removeElement(randomKey);
			}
			
			hashMap.put(key, value);
		}
			
		return returnValue;
	}

	@Override
	public void removeElement(K key) {
		// TODO Auto-generated method stub
		hashMap.remove(key);
	}

}
