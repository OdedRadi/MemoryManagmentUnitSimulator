package com.hit.driver;

import com.hit.model.MMUModel;
import com.hit.view.MMUView;
import com.hit.controller.MMUController;

public class MMUDriver {
	
	public MMUDriver(){

	}

	public static void main(String[] args){
		CLI cli = new CLI(System.in, System.out);
		MMUModel model = new MMUModel();
		MMUView view = new MMUView();
		MMUController conrtoller = new MMUController(view, model);
		
		cli.addObserver(conrtoller);
		model.addObserver(conrtoller);
		view.addObserver(conrtoller);
		
		new Thread(cli).start();
	}
}
