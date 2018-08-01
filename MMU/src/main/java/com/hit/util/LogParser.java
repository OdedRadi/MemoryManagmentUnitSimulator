package com.hit.util;

import java.util.ArrayList;

public class LogParser {

	private String[] commands;
	private int ramSize;
	private int pageSize;
	private int processesCount;
	
	private GetPageCommand[] getPageCommands;
	
	public LogParser() {
		ramSize = 0;
		pageSize = 0;
		processesCount = 0;
	}
	
	public void parseLog(ArrayList<String> arg) {	
		commands = arg.toArray(new String[arg.size()]);
		
		String ramSizeString = commands[0].substring(3); // "RC:x" = "x"
		ramSize = Integer.parseInt(ramSizeString);
		
		String processCountString = commands[1].substring(3); // "PN:x" = "x"
		processesCount = Integer.parseInt(processCountString);
		
		int getPageCommandCounter = 0;
		for(String curr:commands) {
			if (curr.startsWith("GP")) {
				getPageCommandCounter++;
			}
		}
		
		getPageCommands = new GetPageCommand[getPageCommandCounter];
		
		getPageCommandCounter = 0;
		for(String curr:commands) {
			if (curr.startsWith("GP")) {
				
				String[] currGPStrings = curr.split(" "); // "GP:Px y [a, b, c]" = { "GP:Px" , "y" , "[a," , "b," , "c]" }
				
				String currProcessNum = currGPStrings[0].substring(4); // "GP:Px" = "x" 
				String currPageNum = currGPStrings[1];

				int currGPPageSize = currGPStrings.length - 2; 
				
				if (currGPPageSize > pageSize)
					pageSize = currGPPageSize;

				String[] currDataStrings = new String[currGPPageSize];

				if (currGPStrings[2].equals("[]"))
					currDataStrings[0] = "0";
				else
					currDataStrings[0] = currGPStrings[2].substring(1, currGPStrings[2].length() - 1); // "[a," = "a"

				for (int i = 1; i < currGPPageSize; i++) {
					currDataStrings[i] = currGPStrings[i+2].substring(0, currGPStrings[i+2].length() - 1); // "a," = "a"
				}

				getPageCommands[getPageCommandCounter] = new GetPageCommand(currProcessNum, currPageNum, currDataStrings);				
				getPageCommandCounter++;
			}
		}
	}
	
	public String[] getCommands() {
		return commands;
	}
	
	public int getRamSize() {
		return ramSize;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public int getProcessCount() {
		return processesCount;
	}
	
	public GetPageCommand[] getGetPageCommands() {
		return getPageCommands;
	}
	
	
}
