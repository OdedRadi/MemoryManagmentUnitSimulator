package com.hit.view;

import java.util.Observable;
import javax.swing.SwingUtilities;

import com.hit.util.GetPageCommand;
import com.hit.util.LogParser;

public class MMUView extends Observable implements View {

	private MMUMainFrame mainFrame;
	private String[] commands;
	private int currentCommandIndex;
	private GetPageCommand[] getPageCommands;
	private int currentGetPageCommandIndex;
	
	public MMUView() {
	}
	
	public void initView(LogParser lp) {
		this.commands = lp.getCommands();
		currentCommandIndex = 2;
		
		this.getPageCommands = lp.getGetPageCommands();
		currentGetPageCommandIndex = 0;
		
		mainFrame = new MMUMainFrame(this, lp.getRamSize(), lp.getPageSize(), lp.getProcessCount());
	}
	
	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainFrame.showGUI();
			}
		});	
	}
		
	public void playSingle() {
		String currCommand = commands[currentCommandIndex];
		
		if (currCommand.startsWith("PF")) {
			String pageNum = currCommand.substring(3, currCommand.length());
			mainFrame.pageFaultOccur(pageNum);	
		}
		
		else if(currCommand.startsWith("PR")) {
			String[] PRCommandString = currCommand.split(" ");
			String removedPageNum = PRCommandString[1];
			String addedPageNum = PRCommandString[3];
			mainFrame.pageReplacementOccur(removedPageNum, addedPageNum);
		}
		else if (currCommand.startsWith("GP")) {
			GetPageCommand currGPCommand = getPageCommands[currentGetPageCommandIndex];
			mainFrame.getPageOccur(currGPCommand);
			currentGetPageCommandIndex++;
		}
		
		currentCommandIndex++;
		mainFrame.setCurrentCommand(currentCommandIndex);
		
		if (currentCommandIndex == commands.length)
			mainFrame.setPlayButtonEnable(false);
	}
	
	public void playAll() {
		while (currentCommandIndex < commands.length) {
			playSingle();
		}
	}

	public void restartScenario() {
		currentCommandIndex = 2;
		currentGetPageCommandIndex = 0;
		mainFrame.setCurrentCommand(2);
		mainFrame.setPlayButtonEnable(true);
	}

	public String[] getCommands() {
		return commands;
	}

}
