package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class MemoryManagementUnitTest {
	@SuppressWarnings("unchecked")
	@Test
	public void testMMU() throws Exception {
		
		int size = 50; // hard disk is 1000
		IAlgoCache<Long, Long> algo = new LRUAlgoCacheImpl<Long, Long>(size);	
		MemoryManagementUnit mmu = new MemoryManagementUnit(size, algo);
		
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("src/main/resources/com/hit/config/HardDisk.txt")));
		
		
		Page<byte[]> p;
		Long[] pageIds = new Long[size];
		Page<byte[]>[] pagesFromHardDisk = (Page<byte[]>[]) new Page[size];
		
		
		for(int i = 0; i < size * 2; i++) {
			if (i < 50)
				in.readObject(); 
			else {
				p = (Page<byte[]>)in.readObject();
				pageIds[i-50] = p.getPageId();
				pagesFromHardDisk[i-50] = p;
			}
		}
		
		in.close();
		// now we got the pages from 51 - 100
		
		Page<byte[]>[] pagesFromMMU = mmu.getPages(pageIds);
		
		for (int i = 0; i < size; i++)
			assertEquals(pagesFromHardDisk[i], pagesFromMMU[i]);
		
		System.out.println("---------------- Second round ----------------");
		

		in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("src/main/resources/com/hit/config/HardDisk.txt")));
		
		for (int i = 0; i < size*3; i++) {
			if (i < 100)
				in.readObject(); 
			else {
				p = (Page<byte[]>)in.readObject();
				pageIds[i-100] = p.getPageId();
				pagesFromHardDisk[i-100] = p;
			}
		}
		in.close();
		
		pagesFromMMU = mmu.getPages(pageIds);
		
		for (int i = 0; i < size; i++)
			assertEquals(pagesFromHardDisk[i], pagesFromMMU[i]);
	}
}
