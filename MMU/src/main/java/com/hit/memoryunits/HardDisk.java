package com.hit.memoryunits;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import com.hit.exceptions.HardDiskLoadFailException;
import com.hit.exceptions.PageNotFoundExcpetion;
import com.hit.util.MMULogger;

public class HardDisk {

	private static HardDisk instance = null;
	private final int _SIZE = 1000;
	private final String DEFUALT_FILE_NAME = "src/main/resources/com/hit/config/HardDisk.txt";
	private HashMap<Long, Page<byte[]>> allPages;

	@SuppressWarnings({ "unchecked", "resource" })
	private HardDisk() throws HardDiskLoadFailException {
		
		allPages = new HashMap<Long, Page<byte[]>>(_SIZE);
		ObjectInputStream in = null;
		Page<byte[]> page = null;
		
		try {
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(DEFUALT_FILE_NAME)));
			
			while(allPages.size() < _SIZE+1) {
				page = (Page<byte[]>) in.readObject();
				allPages.put(page.getPageId(), page);	
			}
		}
		catch (EOFException e) {
			try {
				in.close();
			} catch (IOException e1) {
				throw new HardDiskLoadFailException();
			}
		}
		catch (ClassNotFoundException e) {
			MMULogger.getInstance().write("Hard disk file have incorrect class", Level.SEVERE);
			throw new HardDiskLoadFailException();
		}		
		catch (IOException e) {
			MMULogger.getInstance().write("Hard disk file didn't found", Level.SEVERE);
			throw new HardDiskLoadFailException();
		}
	}

	synchronized static HardDisk getInstance() throws HardDiskLoadFailException {
		if (instance == null) {
			try {
				instance = new HardDisk();
			}
			catch (HardDiskLoadFailException e) {
				throw e ;
			}
		}
		return instance;
	}

	public Page<byte[]> pageFault(Long pageId) throws PageNotFoundExcpetion {

		if (!allPages.containsKey(pageId))
			throw new PageNotFoundExcpetion();

		return new Page<byte[]>(allPages.get(pageId));	
	}

	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws PageNotFoundExcpetion {

		Page<byte[]> origPage = allPages.get(moveToHdPage.getPageId());
		
		if (!origPage.getContent().equals(moveToHdPage.getContent())) {
			allPages.replace(origPage.getPageId(), moveToHdPage);
		}
		
		return pageFault(moveToRamId);
	}
	
	public void writeHardDiskToFile() throws HardDiskLoadFailException {
		ObjectOutputStream out = null;
		try{			
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(DEFUALT_FILE_NAME)));
			
			Iterator<Long> it = allPages.keySet().iterator();
			while (it.hasNext())
				out.writeObject(allPages.get(it.next()));
			
			out.close();
		}
		catch (IOException e) {
			MMULogger.getInstance().write("Hard disk file didn't found", Level.SEVERE);			
			new HardDiskLoadFailException();
		}
	}
}
