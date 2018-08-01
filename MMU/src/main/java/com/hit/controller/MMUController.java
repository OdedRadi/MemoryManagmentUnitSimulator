package com.hit.controller;

import java.util.List;
import java.util.Observable;

import com.hit.driver.CLI;
import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.util.LogParser;
import com.hit.view.MMUView;
import com.hit.view.View;

public class MMUController implements Controller{
	private View view;
	private Model model;
	
	public MMUController(View v, Model m) {
		view = v;
		model = m;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof CLI) {
			((MMUModel)model).setConfiguration((List<String>)arg);
			((MMUModel)model).start();
		}
		else if (o instanceof Model) {
			((MMUView)view).initView((LogParser)arg);
			((MMUView)view).start();

		}
	}
}