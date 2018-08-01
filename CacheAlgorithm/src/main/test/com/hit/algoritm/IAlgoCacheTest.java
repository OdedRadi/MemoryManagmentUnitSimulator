package com.hit.algoritm;

import org.junit.Test;
import static org.junit.Assert.*;

import com.hit.algorithm.AbstractAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NFUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;

public class IAlgoCacheTest {

	private int valuesCount = 10;
	private Integer[] keys = new Integer[valuesCount];
	private String[] values = new String[valuesCount];
	private AbstractAlgoCache<Integer, String> algo;
	
	@Test
	public void TestNFU() {
		algo = new NFUAlgoCacheImpl<Integer, String>(valuesCount);

		for (int i = 0; i < valuesCount; i++) { 
			keys[i] = i;
			values[i] = Integer.toString(i);
		}

		for (int i = 0; i < valuesCount; i++) { //put values[0] - 10 times, values[1] - 9 times...
			for(int j = i; j < valuesCount; j++) {
				algo.putElement(keys[i], values[i]);
			}
		}

		String returnedValue = null;

		for(int i = valuesCount - 1; i > 0; i--) { // make values[9] be 11 times, values[8] be 11 times, until values[1]
			for(int j = i; j >= 0 ; j--) {
				returnedValue = algo.putElement(keys[i], values[i]);	
			}
			returnedValue = algo.putElement(keys[i], values[i]);
			assertEquals(values[i-1], returnedValue);			
		}		
		
		Integer num = new Integer(20);
		String string = new String("S");
		returnedValue = algo.putElement(num, string); // now values[0] is the only 1 that didn't increased to 11		
		
		assertEquals(values[0], returnedValue);	
		
	}
	
	@Test
	public void TestLRU() {
		algo = new LRUAlgoCacheImpl<Integer, String>(valuesCount);
		
		for (int i = 0; i < valuesCount; i++) {
			keys[i] = i;
			values[i] = Integer.toString(i);
			algo.putElement(keys[i], values[i]);
		}

		String returnedValue = null;
		
		for (int i = 0; i < valuesCount-1; i++) {
			returnedValue = algo.putElement(keys[i], values[i]);		
			assertEquals(values[i+1], returnedValue);			
		}
		
		returnedValue = algo.putElement(new Integer(20), new String("S"));		
		assertEquals(values[9], returnedValue);	
	}
	
	@Test
	public void TestRandom() {
		algo = new RandomAlgoCacheImpl<Integer, String>(valuesCount);
		
		for (int i = 0; i < valuesCount; i++) {
			keys[i] = i;
			values[i] = Integer.toString(i);
		}
		
		for (int i = 0; i < valuesCount; i++) {
				algo.putElement(keys[i], values[i]);
		}
		
		Integer newKey = new Integer(valuesCount);
		String newValue = new String("Test");
		
		String returnedValue = algo.putElement(newKey, newValue);
		
		boolean found = false;
		
		for(int i = 0; i<valuesCount; i++){
			if (values[i].equals(returnedValue)) {
				found = true;
				break;
			}
		}
		assertTrue (found);
	}
}
