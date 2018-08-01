package com.hit.processes;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import com.hit.exceptions.HardDiskLoadFailException;
import com.hit.exceptions.PageNotFoundExcpetion;
import com.hit.exceptions.ProcessPagesCountOverflowExcpetion;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;
import com.hit.util.MMULogger;

public class Process implements Callable<Boolean> {
	
	private int processId;
	private MemoryManagementUnit mmu;
	private ProcessCycles processCycles;

	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles)
	{
		setId(id);
		this.mmu = mmu;
		this.processCycles = processCycles;
	}

	public int getId(){
		return processId;
	}
	
	public void setId(int id){
		this.processId = id;
	}
	
	public Boolean call() throws Exception{
		
		for(ProcessCycle currentProcessCycle: processCycles.getProcessCycles()) {
			
			Long[] currentPagesIds = currentProcessCycle.getPages().toArray(new Long[currentProcessCycle.getPages().size()]);

			List<byte[]> currentData = currentProcessCycle.getData();

			Page<byte[]>[] currentPages = null;

			synchronized (mmu) {
				try {
					currentPages = mmu.getPages(currentPagesIds);
					
					for (int j = 0 ; j < currentPagesIds.length; j++) 
					{
						if (currentData.get(j).length != 0) {
							currentPages[j].setContent(currentData.get(j));
							MMULogger.getInstance().write("GP:P" + processId + " " + currentPages[j].toString(), Level.INFO);
						}
						else
							MMULogger.getInstance().write("GP:P" + processId + " " + currentPages[j].getPageId() + " []", Level.INFO);
					}					
				}
				catch(ProcessPagesCountOverflowExcpetion e) {
					MMULogger.getInstance().write("Process number " + processId + " requierd largest pages count then the ram can have", Level.SEVERE);
					return false;
				}
				catch(HardDiskLoadFailException e) {
					return false;
				}
				catch(PageNotFoundExcpetion e) {
					return false;
				}
			}
	
			delay(currentProcessCycle.getSleepMs());
		}
		
		return true;
	}
	
	private static void delay(long ms){
		try { 
			Thread.sleep(ms);
		}
		catch (InterruptedException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			return;
		}
	}
}