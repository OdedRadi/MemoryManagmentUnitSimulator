package com.hit.driver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

public class CLI extends Observable implements Runnable {

	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String LRU = "LRU";
	public static final String NFU = "NFU";
	public static final String RANDOM = "RANDOM";

	private InputStream in;
	private OutputStream out;
	
	public CLI(InputStream in, OutputStream out){
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void run() {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(new BufferedInputStream(in));	
		String command;

		while (true) {
			System.out.println("Please enter 'start' or 'stop'");
			
			command = s.nextLine();

			if (!command.equals(START) && !command.equals(STOP)) {
				System.out.println("Not a valid command");
				continue;
			}

			else if (command.equals(START)) {
				System.out.println("Please enter required algorithm and RAM capacity");
				String algo = s.next();
				algo = algo.toUpperCase();
				
				if (!algo.equals(LRU) && !algo.equals(NFU) && !algo.equals(RANDOM)) {
					System.out.println("Not a valid command");
					s.nextLine();
					continue;
				}
				
				String capacityString = s.next();
			
				try { 		
					Integer.valueOf(capacityString);
				}
				catch (Exception e) {
					System.out.println("Not a valid command");
					s.nextLine();
					continue;
				}
				
				ArrayList<String> fullCommand = new ArrayList<String>();
				fullCommand.add(algo);
				fullCommand.add(capacityString);
				
				setChanged();
				notifyObservers(fullCommand);
				
				s.nextLine();
				continue;
			}
			
			else if (command.equals(STOP)) {
				System.out.println("Thank you");
				return;
			}
		}	
	}

	
	public void write(String string) throws IOException{
		
		PrintWriter writer = new PrintWriter(new BufferedOutputStream(out));
		writer.write(string);
	}

	

}
