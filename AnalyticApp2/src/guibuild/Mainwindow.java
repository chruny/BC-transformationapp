package guibuild;


import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import editdata.Data;
import editdata.DataDatabase;
import javafx.scene.control.ComboBox;

import javax.swing.SwingConstants;

public class Mainwindow extends Data{

	private JFrame frame;
	private CustomDataModel customDataModel;
	private JTable table;
	private JLabel labelstrana;
	JComboBox comboBox ;
	//private DefaultComboBoxModel comboBoxModel;
	private Integer actualpocetriadkov=100;
	public int strana=0;
	public int workingrow=0;
	/*
	 *  0  not loaded data
	 *  1 loaded data from csv
	 *  2 loaded data from database
	 */
	public int flagodkialnacitavam=0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mainwindow window = new Mainwindow();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Mainwindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1129, 651);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, panel, 476, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		ScrollPane scrollPane = new ScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, panel, 6, SpringLayout.EAST, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -180, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, frame.getContentPane());
		
		JButton LoadData = new JButton("Nacitaj CSV");
		sl_panel.putConstraint(SpringLayout.WEST, LoadData, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, LoadData, -443, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, LoadData, 0, SpringLayout.EAST, panel);
		LoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				callLoadDataFromCSV();
			}
		});
		panel.add(LoadData);
		
		JButton checkPSC = new JButton("Skontroluj PSC");
		sl_panel.putConstraint(SpringLayout.WEST, checkPSC, 0, SpringLayout.WEST, LoadData);
		sl_panel.putConstraint(SpringLayout.EAST, checkPSC, 0, SpringLayout.EAST, panel);
		checkPSC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				callCheckPSC();
			}
		});
		panel.add(checkPSC);
		
		JButton deletespaces = new JButton("Vymaz Medzery");
		sl_panel.putConstraint(SpringLayout.WEST, deletespaces, 0, SpringLayout.WEST, LoadData);
		sl_panel.putConstraint(SpringLayout.EAST, deletespaces, 0, SpringLayout.EAST, panel);
		deletespaces.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callDeleteSpaces();
			}
		});
		panel.add(deletespaces);
		
		JButton loadDataFromDatabase = new JButton("Nacitaj Databazu");
		sl_panel.putConstraint(SpringLayout.SOUTH, deletespaces, -19, SpringLayout.NORTH, loadDataFromDatabase);
		sl_panel.putConstraint(SpringLayout.NORTH, loadDataFromDatabase, 97, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, loadDataFromDatabase, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, loadDataFromDatabase, -356, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, loadDataFromDatabase, 0, SpringLayout.EAST, panel);
		loadDataFromDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callLoadDataFromDatabase();
			}
		});
		panel.add(loadDataFromDatabase);
		
		
		
		comboBox = new JComboBox();
		sl_panel.putConstraint(SpringLayout.NORTH, checkPSC, 21, SpringLayout.SOUTH, comboBox);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Som tu"+comboBox.getSelectedIndex());
				workingrow=comboBox.getSelectedIndex();
				
			}
		});
		sl_panel.putConstraint(SpringLayout.EAST, comboBox, 0, SpringLayout.EAST, panel);
		panel.add(comboBox);
		
		JLabel lblVyberteStlpec = new JLabel("Vyberte stlpec");
		sl_panel.putConstraint(SpringLayout.NORTH, lblVyberteStlpec, 166, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblVyberteStlpec, 30, SpringLayout.WEST, panel);
		panel.add(lblVyberteStlpec);
		
		
		Integer[] pocetzobrazenychriadkov={10,100,500,1000};
		JComboBox<Integer> comboBox_2 = new JComboBox(pocetzobrazenychriadkov);
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox, 38, SpringLayout.SOUTH, comboBox_2);
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox_2, 6, SpringLayout.SOUTH, loadDataFromDatabase);
		sl_panel.putConstraint(SpringLayout.EAST, comboBox_2, 0, SpringLayout.EAST, LoadData);
		comboBox_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("combobox sa zmenil");
				JComboBox cb=(JComboBox)arg0.getSource();
				setActualpocetriadkov((Integer) cb.getSelectedItem());
				updateStranaLabel();
				//tableChanged();
			}
		});
		comboBox_2.setSelectedIndex(1);
		comboBox_2.setMaximumRowCount(20);
		panel.add(comboBox_2);
		
		JLabel lblPocetZobraz = new JLabel("Pocet zobraz.");
		sl_panel.putConstraint(SpringLayout.NORTH, lblPocetZobraz, 6, SpringLayout.SOUTH, loadDataFromDatabase);
		sl_panel.putConstraint(SpringLayout.WEST, lblPocetZobraz, 0, SpringLayout.WEST, lblVyberteStlpec);
		panel.add(lblPocetZobraz);
		
		JLabel lblStlpcov = new JLabel("riadkov");
		sl_panel.putConstraint(SpringLayout.WEST, lblStlpcov, 10, SpringLayout.WEST, lblPocetZobraz);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblStlpcov, -6, SpringLayout.NORTH, lblVyberteStlpec);
		panel.add(lblStlpcov);
		
		table=new JTable();
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		scrollPane.add(table);
		
		frame.getContentPane().add(scrollPane);
		
		JPanel panel_1 = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel_1, 6, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, panel_1, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, panel_1, 105, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.EAST, panel_1, 164, SpringLayout.WEST, panel);
		frame.getContentPane().add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		JButton prev = new JButton("<==");
		sl_panel_1.putConstraint(SpringLayout.WEST, prev, 0, SpringLayout.WEST, panel_1);
		sl_panel.putConstraint(SpringLayout.NORTH, prev, 10, SpringLayout.NORTH, panel_1);
		sl_panel.putConstraint(SpringLayout.WEST, prev, 0, SpringLayout.WEST, panel_1);
		panel_1.add(prev);
		
		JButton next = new JButton("==>");
		sl_panel_1.putConstraint(SpringLayout.NORTH, prev, 0, SpringLayout.NORTH, next);
		sl_panel_1.putConstraint(SpringLayout.NORTH, next, 44, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, next, 0, SpringLayout.EAST, panel_1);
		sl_panel.putConstraint(SpringLayout.NORTH, next, 22, SpringLayout.NORTH, panel_1);
		sl_panel.putConstraint(SpringLayout.EAST, next, -36, SpringLayout.EAST, panel_1);
		panel_1.add(next);
		
		labelstrana = new JLabel("1/1");
		sl_panel.putConstraint(SpringLayout.NORTH, labelstrana, 10, SpringLayout.NORTH, panel_1);
		sl_panel.putConstraint(SpringLayout.WEST, labelstrana, 69, SpringLayout.WEST, panel_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, labelstrana, 20, SpringLayout.SOUTH, panel_1);
		sl_panel.putConstraint(SpringLayout.EAST, labelstrana, 69, SpringLayout.EAST, panel_1);
		panel_1.add(labelstrana);
		labelstrana.setHorizontalAlignment(SwingConstants.CENTER);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callNext();
			}
		});
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				callPrev();
			}
		});
		
		JLabel lblPocetStran = new JLabel("Strana");
		sl_panel.putConstraint(SpringLayout.NORTH, lblPocetStran, 713, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblPocetStran, 7, SpringLayout.WEST, LoadData);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblPocetStran, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lblPocetStran, -59, SpringLayout.EAST, panel);
		panel.add(lblPocetStran);
		lblPocetStran.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton button = new JButton("Skontroluj Email");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, button, 17, SpringLayout.SOUTH, checkPSC);
		sl_panel.putConstraint(SpringLayout.WEST, button, 0, SpringLayout.WEST, LoadData);
		sl_panel.putConstraint(SpringLayout.EAST, button, 0, SpringLayout.EAST, LoadData);
		panel.add(button);
		
		JButton button_1 = new JButton("Skontroluj Rodne Cislo");
		sl_panel.putConstraint(SpringLayout.NORTH, button_1, 20, SpringLayout.SOUTH, button);
		sl_panel.putConstraint(SpringLayout.WEST, button_1, 0, SpringLayout.WEST, LoadData);
		sl_panel.putConstraint(SpringLayout.EAST, button_1, 0, SpringLayout.EAST, LoadData);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Spusti Diagnostiku");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spustiDiagnostiku();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, button_2, 23, SpringLayout.SOUTH, button_1);
		sl_panel.putConstraint(SpringLayout.WEST, button_2, 0, SpringLayout.WEST, LoadData);
		sl_panel.putConstraint(SpringLayout.EAST, button_2, 0, SpringLayout.EAST, LoadData);
		panel.add(button_2);
		
		
	}
	
	public void callCheckEmail() {
		System.out.println("checkEmail");
		
	}
	
	public void callCheckRodneCislo() {
		System.out.println("check");
	}
	
	public void spustiDiagnostiku() {
		switch(flagodkialnacitavam) {
		case 0:
			System.out.println("data este neboli nacitane");
			break;
		case 1:
			System.out.println("CSV Diagnostics");
			spustiDiagnostikuCSV();
			break;
		case 2:
			System.out.println("Database Diagnostics");
			spustiDiagnostikuDatabaza();
			break;
		}
		
	}
	
	public int getStrana() {
		return strana;
	}

	public void setStrana(int strana) {
		this.strana = strana;
	}

	public void updateComboBoxStlpec() {
		DefaultComboBoxModel comboBoxModel;
		Vector vector = null;
		switch(flagodkialnacitavam) {
		case 0:
			System.out.println("Data este neboli nacitane");
			System.out.println("Som tu"+flagodkialnacitavam);
			break;
		case 1:
			vector=new Vector(Arrays.asList(namesOfColumns));
			System.out.println("Som tu"+flagodkialnacitavam);
			break;
		case 2:
			vector=new Vector(Arrays.asList(getNamesOfColumnsFromDatabase()));
			System.out.println("Som tu"+flagodkialnacitavam);
			break;
		}
		comboBoxModel=new DefaultComboBoxModel(vector);
		comboBox.setModel(comboBoxModel);
	}
	
	private void callCheckPSC() {
		
		System.out.println("Press Check PSC");
	}
	
	private void callLoadDataFromCSV() {
		System.out.println("Press Load Data");
		flagodkialnacitavam=1;
		loadData();
		tableChanged();
		System.out.println("Loaded");
	}
		
	private void callDeleteSpaces() {
		System.out.println("Press Delete Spaces");
		eraseAllSpaces(rowData);
		tableChanged();
		
	}
	
	private void callNext() {
		strana++;
		switch(flagodkialnacitavam) {
		case 0:
			System.out.println("Doposial ste nenacitali ziadne data");
			break;
		case 1:
			System.out.println("Dalsia strana case 1");
			flagodkialnacitavam=1;
			loadData();
			tableChanged();
			break;
		case 2:
			System.out.println("Dalsia strana case 2");
			flagodkialnacitavam=2;
			callDatabaseLoadFromMain(strana,(Integer)actualpocetriadkov);
			tableChanged();
			break;
		}
		updateStranaLabel();
	}
	
	private void callPrev() {
		System.out.println("Predosla strana");		
		if(strana!=0) 
		strana--;
		switch(flagodkialnacitavam) {
		case 0:
			System.out.println("Doposial ste nenacitali ziadne data");
			break;
		case 1:
			System.out.println("Dalsia strana case 1");
			flagodkialnacitavam=1;
			loadData();
			tableChanged();
			break;
		case 2:
			System.out.println("Dalsia strana case 2");
			flagodkialnacitavam=2;
			callDatabaseLoadFromMain(strana,(Integer)actualpocetriadkov);
			tableChanged();
			break;
		}
		updateStranaLabel();
	}
	
	private void callLoadDataFromDatabase() {
		System.out.println("Press Call Data From Database");
		flagodkialnacitavam=2;
		callDatabaseLoadFromMain(strana,actualpocetriadkov);
		tableChanged();
		updateStranaLabel();
		System.out.println("Main:DatabaseLoad");
		
	}
	
	private void tableChanged() {
		customDataModel=new CustomDataModel(rowData,namesOfColumns);
		table.setModel(customDataModel);
		updateComboBoxStlpec();
		customDataModel.refresh();
	}

	public Integer getActualpocetriadkov() {
		return actualpocetriadkov;
	}
	
	public void setActualpocetriadkov(Integer actualpocetriadkov) {
		this.actualpocetriadkov = actualpocetriadkov;
	}
	
	public void updateStranaLabel() {

		switch(flagodkialnacitavam) {
		case 0:
			System.out.println("Data not Loaded");
			//labelstrana.setText("1/1");
			break;
		case 1:
			System.out.println("Dalsia strana case 1");
			flagodkialnacitavam=1;
			loadData();
			tableChanged();
			labelstrana.setText((strana+1)+"/"+getMaxpagenumber());
			break;
		case 2:
			System.out.println("Dalsia strana case 2");
			flagodkialnacitavam=2;
			callDatabaseLoadFromMain(strana,(Integer)actualpocetriadkov);
			tableChanged();
			labelstrana.setText((strana+1)+"/"+getMaxpagenumber());
			break;
		
		
		}
		
		
	}

//end of class
}
