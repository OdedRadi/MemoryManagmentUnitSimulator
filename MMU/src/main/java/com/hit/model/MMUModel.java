package com.hit.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.factory.AlgoFactory;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.LogParser;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model {

	private final static String CONFIG_FILE_NAME = "src/main/resources/com/hit/config/Configuration.json";
	private final static String LOG_FILE_NAME = "logs/log.txt";
	private int ramCapacity;
	private MemoryManagementUnit mmu;
	public MMUModel() {
		
	}
	
	public void setConfiguration(List<String> configuration) {
		String[] command = configuration.toArray(new String[2]);
		
		IAlgoCache<Long,Long> algo = null;
		ramCapacity = Integer.valueOf(command[1]).intValue();
	
		AlgoFactory algoFac = new AlgoFactory();	
		algo = algoFac.createAlgo(command[0], ramCapacity);

		mmu = new MemoryManagementUnit(ramCapacity, algo);	
	}
	
	public void start() {
		RunConfiguration runConfig = null;
		
		runConfig = readConfigurationFile();
			
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		
		List<Process> processes = createProcesses(processCycles, mmu);

		MMULogger.getInstance().write("RC:" + ramCapacity, Level.INFO);
		MMULogger.getInstance().write("PN:" + processes.size(), Level.INFO);
		
		runProcesses(processes);	
		
		mmu.shutdown();	
		
		readLogFileAndStartView();
	}

	private void readLogFileAndStartView() {
		ArrayList<String> logLines = new ArrayList<>();
		try {
			Scanner logScanner = new Scanner(new BufferedReader(new FileReader(LOG_FILE_NAME)));
			
			while (logScanner.hasNext())
				logLines.add(logScanner.nextLine());
			
			logScanner.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Log file didn't found... the view will not start");
			return;
		}
		
		LogParser lp = new LogParser();
		lp.parseLog(logLines);
				
		setChanged();
		notifyObservers(lp);
	}

	private static void runProcesses(List<Process> applications){
		ExecutorService executor = Executors.newCachedThreadPool();
		
		@SuppressWarnings("unchecked")
		Future<Boolean> futures[] = new Future[applications.size()];
		
		for(int i = 0; i < applications.size(); i++)
			futures[i] = executor.submit(applications.get(i));
		
		executor.shutdown();
		try {
			executor.awaitTermination(10L, TimeUnit.MINUTES);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < applications.size(); i++) {
			try {
				if (futures[i].get() == false) {
					MMULogger.getInstance().write("Process number " + i+1 + "falied", Level.SEVERE);
				}
			} 
			catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<Process> createProcesses(List<ProcessCycles> appliocationsScenarios, MemoryManagementUnit mmu){
		Process[] processes = new Process[appliocationsScenarios.size()];

		for (int i=0; i<appliocationsScenarios.size(); i++)
			processes[i] = new Process(i+1, mmu, appliocationsScenarios.get(i));
		
		return Arrays.asList(processes);
	}

	private static RunConfiguration readConfigurationFile() {
		RunConfiguration runConfiguration = null;
		
		try {
			runConfiguration = new Gson().fromJson(new JsonReader(new FileReader(CONFIG_FILE_NAME)), RunConfiguration.class);
		}
		catch(FileNotFoundException e) {
			MMULogger.getInstance().write("Json configuration file didn't found", Level.SEVERE);
			return null;
		}
		catch(JsonSyntaxException e) {
			MMULogger.getInstance().write("Json file syntax error", Level.SEVERE);
			return null;
		}
		catch(JsonIOException e) {
			MMULogger.getInstance().write("Json IO exception", Level.SEVERE);
			return null;
		}
		
		return runConfiguration; 
	}

}
