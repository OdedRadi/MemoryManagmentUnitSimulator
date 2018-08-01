package com.hit.factory;

import java.util.HashMap;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NFUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;

public class AlgoFactory {
	
	private HashMap<String, Creator> algoCreators;
	private static final String LRU_ALGO_NAME = "LRU";
	private static final String NRU_ALGO_NAME = "NFU";
	private static final String RANDOM_ALGO_NAME = "RANDOM";
	
	private interface Creator{
		public IAlgoCache<Long,Long> create(int capacity);
	}
	private class LruCreator implements Creator{
		public IAlgoCache<Long,Long> create(int capacity) {
			return new LRUAlgoCacheImpl<Long,Long>(capacity);
		}
	}
	private class NfuCreator implements Creator{
		public IAlgoCache<Long,Long> create(int capacity) {
			return new NFUAlgoCacheImpl<Long,Long>(capacity);
		}
	}
	private class RandomCreator implements Creator{
		public IAlgoCache<Long,Long> create(int capacity) {
			return new RandomAlgoCacheImpl<Long,Long>(capacity);
		}
	}
	
	public AlgoFactory() {
		algoCreators = new HashMap<String, Creator>();
		algoCreators.put(LRU_ALGO_NAME, new LruCreator());
		algoCreators.put(NRU_ALGO_NAME, new NfuCreator());
		algoCreators.put(RANDOM_ALGO_NAME, new RandomCreator());		
	}

	public IAlgoCache<Long,Long> createAlgo(String type, int capacity){
		Creator c = algoCreators.get(type);
		if (c == null)
			return null;
		return c.create(capacity);
	}
}