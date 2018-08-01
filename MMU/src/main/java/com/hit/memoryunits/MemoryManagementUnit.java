package com.hit.memoryunits;

import java.util.logging.Level;

import com.hit.algorithm.IAlgoCache;
import com.hit.exceptions.HardDiskLoadFailException;
import com.hit.exceptions.PageNotFoundExcpetion;
import com.hit.exceptions.ProcessPagesCountOverflowExcpetion;
import com.hit.util.MMULogger;

public class MemoryManagementUnit {

	private RAM ram;
	private IAlgoCache<Long, Long> algo;

	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long,Long> algo) {
		this.ram = new RAM(ramCapacity);
		this.algo = algo;
	}

	public Page<byte[]>[] getPages(Long[] pageIds) throws HardDiskLoadFailException, PageNotFoundExcpetion, ProcessPagesCountOverflowExcpetion {
		HardDisk hardDisk;

		try{
			hardDisk = HardDisk.getInstance();
		}
		catch(HardDiskLoadFailException e) {
			MMULogger.getInstance().write("MMU can't get hard disk instance", Level.SEVERE);
			throw e;
		} 

		for (int i = 0; i < pageIds.length; i++) {
			if (algo.getElement(pageIds[i]) == null)
				//continue; //page was in ram
			{
				if (ram.getPages().size() < ram.getInitialCapacity()) { // The RAM is not full, calling page fault
					MMULogger.getInstance().write("PF:" + pageIds[i], Level.INFO);
					try {
						ram.addPage(hardDisk.pageFault(pageIds[i]));
						algo.putElement(pageIds[i], pageIds[i]);
					}
					catch(PageNotFoundExcpetion e) {
						MMULogger.getInstance().write("Page fault failed, there is no such page ID in the hard disk (" + pageIds[i] + ")", Level.SEVERE);
						throw e;
					}
				}
				else { // The RAM is full, calling page replacement
					Page<byte[]> pageToRemove = null;
					Long pageIdToRemove = algo.putElement(pageIds[i], pageIds[i]);

					MMULogger.getInstance().write("PR:MTH " + pageIdToRemove + " MTR " + pageIds[i], Level.INFO);

					try {
						pageToRemove = ram.getPage(pageIdToRemove);
					}
					catch(PageNotFoundExcpetion e) { 
						MMULogger.getInstance().write("CacheAlgo and RAM unsynchronized (algo has returned ID that the ram don't have)", Level.SEVERE);
						throw e;
					}

					try{
						ram.removePage(pageToRemove);
						ram.addPage(hardDisk.pageReplacement(pageToRemove, pageIds[i]));				
					}
					catch(PageNotFoundExcpetion e) {
						MMULogger.getInstance().write("Page replacement failed, there is no such page ID in the hard disk (" + pageIds[i] + ")", Level.SEVERE);
						throw e;
					}
				}
			} // end if (getElemet() == null)			
		}

		if (ram.getInitialCapacity() < pageIds.length)
			throw new ProcessPagesCountOverflowExcpetion();

		return ram.getPages(pageIds);
	}

	public void shutdown() {
		try{
			HardDisk.getInstance().writeHardDiskToFile();
		}
		catch(HardDiskLoadFailException e) {
			MMULogger.getInstance().write("MMU shutdown didn't saved RAM data to hard disk", Level.SEVERE);
			return;
		}
	}
}