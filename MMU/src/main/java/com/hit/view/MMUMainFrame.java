package com.hit.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.hit.util.GetPageCommand;


public class MMUMainFrame extends JFrame implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private MMUView mmuView;
	
	private JTable ramTable;
	private DefaultTableModel ramTableModel;
	
	private JTextField pageFaultField;
	private JTextField pageReplacementField;
	
	private JButton playButton;
	private JButton playAllButton;
	private JList<String> commandsList;
	
	private JList<String> processList;
	private Boolean[] selectedProcesses;
	
	public MMUMainFrame(MMUView parentView, int ramSize, int pagesSize, int processCount) {
		super("MMU Simulator");
		
		try { // set windows style
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		mmuView = parentView;
		selectedProcesses = new Boolean[processCount+1];
		for (int i = 0; i < selectedProcesses.length; i++)
			selectedProcesses[i] = new Boolean(false);

		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(createTablePanel(ramSize, pagesSize));
		upperPanel.add(createCoutnersPanel());
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		lowerPanel.add(createControlPanel());
		lowerPanel.add(createProcessesListPanel(processCount));
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(upperPanel);
		this.add(lowerPanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.pack();		
	}

	private JPanel createTablePanel(int columnCount, int rowCount) {
		JPanel tablePanel = new JPanel();
		/*String[] columnHeaders = new String[columnCount];
		for (int i = 0; i < columnCount; i++)
			columnHeaders[i] = new String("0");
		
		Object[][] tableData = new Object[rowCount][columnCount];
	
		for (int i = 0; i < rowCount; i++)
			for (int j = 0; j < columnCount; j++)
				tableData[i][j] = new String("0");*/
		
		ramTableModel = new DefaultTableModel(rowCount, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	
		ramTable = new JTable();
		ramTable.setModel(ramTableModel);
		ramTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JScrollPane sp = new JScrollPane(ramTable);
		sp.setPreferredSize(new Dimension(400, 125));

		tablePanel.add(sp);
		
		return tablePanel;
	}
	
	private JPanel createCoutnersPanel() {
		JPanel countersPanel = new JPanel();
		JLabel pageFaultLabel = new JLabel("Page fault amount");
		pageFaultField = new JTextField("0");
		pageFaultField.setPreferredSize(new Dimension(30, 20));
		JLabel pageReplacementLabel = new JLabel("Page replacement amount");
		pageReplacementField = new JTextField("0");
		pageReplacementField.setPreferredSize(new Dimension(30, 20));

		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new FlowLayout());
		upperPanel.add(pageFaultLabel);
		upperPanel.add(pageFaultField);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new FlowLayout());
		lowerPanel.add(pageReplacementLabel);
		lowerPanel.add(pageReplacementField);
		
		countersPanel.setLayout(new BoxLayout(countersPanel, BoxLayout.Y_AXIS));
		countersPanel.add(Box.createVerticalGlue());
		countersPanel.add(upperPanel);
		countersPanel.add(lowerPanel);
		
		return countersPanel;
	}
	
	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		
		playButton = new JButton("Play");
		playButton.setActionCommand("Play");
		playButton.addActionListener(this);
		
		playAllButton = new JButton("Play All");
		playAllButton.setActionCommand("Play All");
		playAllButton.addActionListener(this);
		
		JButton restartButton = new JButton("Restart");
		restartButton.setActionCommand("Restart");
		restartButton.addActionListener(this);
		
		buttonsPanel.add(playButton);
		buttonsPanel.add(playAllButton);
		buttonsPanel.add(restartButton);
			
		JPanel commandsPanel = new JPanel();
		commandsPanel.setLayout(new FlowLayout());

		commandsList = new JList<String>(mmuView.getCommands());	
		commandsList.setSelectedIndex(2);

		commandsPanel.add(new JScrollPane(commandsList));
		
		controlPanel.add(buttonsPanel);
		controlPanel.add(commandsPanel);
		
		return controlPanel;
	}
	
	private JPanel createProcessesListPanel(int processCount) {
		JPanel processesListPanel = new JPanel();
		String[] processesNames = new String[processCount];
			
		for (int i = 0; i < processCount; i++)
			processesNames[i] = new String("Process " + (i+1));
				
		processList = new JList<String>(processesNames);
		processList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		processList.setSelectedIndex(0);
		selectedProcesses[0] = true;
		processList.addListSelectionListener(this);
		
		processesListPanel.add(new JScrollPane(processList));
		
		return processesListPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Play"))
			mmuView.playSingle();
		else if (e.getActionCommand().equals("Play All"))
			mmuView.playAll();	
		else if(e.getActionCommand().equals("Restart")) {
			mmuView.restartScenario();
			ramTableModel.setColumnCount(0);
			pageFaultField.setText(Integer.toString(0));
			pageReplacementField.setText(Integer.toString(0));		
		}
	}
	
	@Override // Listener to JList processList
	public void valueChanged(ListSelectionEvent arg0) { 
		int[] selectedIndcies = processList.getSelectedIndices();
		
		for (int i = 0; i < selectedProcesses.length; i++)
			selectedProcesses[i] = false;
		
		for (int i = 0; i < selectedIndcies.length; i++)
			selectedProcesses[selectedIndcies[i]] = true;
	}
	
	public void showGUI() {
		this.setVisible(true);
	}

	public void pageFaultOccur(String pageNum) {
		int currPageFaultCounter = Integer.parseInt(pageFaultField.getText());
		currPageFaultCounter++;
		pageFaultField.setText(Integer.toString(currPageFaultCounter));
		
		ramTableModel.addColumn(pageNum);
		
		for (int i = 0; i < ramTableModel.getRowCount(); i++)
			ramTableModel.setValueAt("##", i, ramTableModel.getColumnCount()-1);
	}
	
	public void pageReplacementOccur(String removedPageNum, String addedPageNum) {
		int currPageReplacementCounter = Integer.parseInt(pageReplacementField.getText());
		currPageReplacementCounter++;
		pageReplacementField.setText(Integer.toString(currPageReplacementCounter));
		
		String[] columnsHeaders = new String[ramTableModel.getColumnCount()];
		
		for (int i=0; i < ramTableModel.getColumnCount(); i++){
			columnsHeaders[i] = ramTableModel.getColumnName(i);
			
			if (ramTableModel.getColumnName(i).equals(removedPageNum)) {
				columnsHeaders[i] = addedPageNum;
				
				for (int j = 0; j < ramTableModel.getRowCount(); j++)
					ramTableModel.setValueAt("##", j, i);
			}
		}
		
		ramTableModel.setColumnIdentifiers(columnsHeaders);	
	}

	public void getPageOccur(GetPageCommand currGPCommand) {
		//int[] selectedProcesses = processList.getSelectedIndices();
		int processNum = Integer.parseInt(currGPCommand.getProcessNum());
		
		//boolean found = false;
		
		//for(int i = 0; i < selectedProcesses.length; i++)
		//	if (selectedProcesses[i] + 1 == processNum)
		//		found = true;
		
		if (selectedProcesses[processNum - 1] == true) {
			int columnNum = 0;

			for (int i=0; i < ramTableModel.getColumnCount(); i++){
				if (ramTableModel.getColumnName(i).equals(currGPCommand.getPageNum())) {
					columnNum = i;
					break;
				}			
			}

			for (int i = 0; i < currGPCommand.getDataSize(); i++)
				ramTableModel.setValueAt(currGPCommand.getData(i), i, columnNum);
		}
	}
	
	public void setPlayButtonEnable(boolean flag) {
		playButton.setEnabled(flag);
		playAllButton.setEnabled(flag);
	}
	
	public void setCurrentCommand(int index) {
		commandsList.setSelectedIndex(index);
		commandsList.ensureIndexIsVisible(commandsList.getSelectedIndex());
	}
}
