package com.hit.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MMULogger {

	private final static String DEFUALT_FILE_NAME = "logs/log.txt";
	private FileHandler handler;
	private static MMULogger instance = null;
	
	private MMULogger() {		
		try {
			handler = new FileHandler(DEFUALT_FILE_NAME);
		} 
		catch (IOException e) {
			System.out.println("Failed to create log file");
			handler = null;
		}
		
		if (handler != null) {
			handler.setFormatter(new Formatter() {
				@Override
				public String format(LogRecord arg0) {
					return arg0.getMessage() + "\r\n";
				}				
			});			
		}
	}
	
	public synchronized void write(String command, Level level) {
		if (handler != null) {
			if (level == Level.INFO)
				handler.publish(new LogRecord(level, command));
			else if (level == Level.SEVERE)
				handler.publish(new LogRecord(level, "ERR: " + command));
		}
		else
			System.out.println("No log file to write");
	}
		
	synchronized public static MMULogger getInstance() {
		if (instance == null)
			instance = new MMULogger();
		
		return instance;
	}	
}
