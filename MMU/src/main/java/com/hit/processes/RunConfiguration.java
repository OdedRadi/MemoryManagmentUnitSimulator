package com.hit.processes;

import java.util.List;

public class RunConfiguration {
	
	private List<ProcessCycles> processesCycles;
	
	public RunConfiguration(java.util.List<ProcessCycles> processesCycles){
		setProcessesCycles(processesCycles);
	}

	public List<ProcessCycles> getProcessesCycles(){
		return processesCycles;
	}
	
	public void setProcessesCycles(List<ProcessCycles> processesCycles){
		this.processesCycles = processesCycles;
	}
	
	public java.lang.String toString(){
		return null;
	}
}
