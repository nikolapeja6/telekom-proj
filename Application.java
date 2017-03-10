package nikolapeja6.Telekom;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;


public class Application extends JFrame {
	
	// File containing the source message
	private File sourceFile;
	
	// Flags for options: Burrows–Wheeler, Move to Front and AritheticCoder
	private boolean bw = false;
	private boolean mtf = false;
	private boolean ac = false;
	
	// FileChooser for choosing the source file
	JFileChooser fileChooser;


	public static void main(String [] args){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {	}

		new Application();
	}
	
	private Application(){
		
		// Initial window settings
		super("Stepen kompresije");
		setBounds(200, 100, 500, 175);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		// Adding Menus
		addMenu();
		
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select the file with the source code");
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
		fileChooser.setFileFilter(filter);
		
		// Adding  Components to the Window
		addComponents();
		
		
		setVisible(true);
		
	}
	
	
	// Addds the menus to the application
	private void addMenu(){
		
		// Creating the MenuBar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		
		//Main Menu
		JMenu mainMenu = new JMenu("Main");
		menuBar.add(mainMenu);
		
		// Select Source File
		JMenuItem selectSource = new JMenuItem("Select Source");
		mainMenu.add(selectSource);
		selectSource.setToolTipText("Select the file contating the source message");
		selectSource.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//
				// To add selectSource action
			}
		});
		
		// Exit 
		JMenuItem exitItem = new JMenuItem("Exit");
		mainMenu.add(exitItem);
		exitItem.setToolTipText("Exit the application");
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		
		
		// Help Menu
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		// About
		JMenuItem about = new JMenuItem("About");
		helpMenu.add(about);
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//
				// To add about popout
			}
		});
		
		// Help
		JMenuItem help = new JMenuItem("Help");
		helpMenu.add(help);
		help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//
				// To add help popout
			}
		});
		
	}
	
	
	// Adds the components to the window
	private void addComponents(){
		
		// Setting the general layout to the frame
		setLayout(new BorderLayout());
		
		
		// First panel containing the options for the output and the title
		JPanel firstPanel = new JPanel();
		add(firstPanel, BorderLayout.CENTER);
		firstPanel.setLayout(new GridLayout(2,1));
		
		// Title Label
		JLabel optionsTitle = new JLabel(" Select the options which you want to be displayed:");
		firstPanel.add(optionsTitle);
		
		// Panel for holding the options
		JPanel optionsPanel = new JPanel();
		firstPanel.add(optionsPanel);
		optionsPanel.setLayout(new GridLayout(1,3));
		
		// Adding the options
		JCheckBox burrowsWheeler = new JCheckBox("Burrows–Wheeler");
		JCheckBox moveToFront = new JCheckBox("Move to Front");
		JCheckBox arithemicCoder = new JCheckBox("Arithmetic Coder");
		optionsPanel.add(burrowsWheeler);
		optionsPanel.add(moveToFront);
		optionsPanel.add(arithemicCoder);
		burrowsWheeler.setFocusPainted(false);
		moveToFront.setFocusPainted(false);
		arithemicCoder.setFocusPainted(false);
		
		// Adding the listener for checking the options
		ActionListener optionsListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {

				Object source = e.getSource();
				
				if(source == burrowsWheeler){
					if(burrowsWheeler.isSelected())
						bw = true;
					else
						bw = false;
					
					return;
				}
				
				if(source == moveToFront){
					if(moveToFront.isSelected())
						mtf = true;
					else
						mtf = false;
					
					return;
				}
				
				if(source == arithemicCoder){
					if(arithemicCoder.isSelected())
						ac = true;
					else
						ac = false;
					
					return;
				}
				
				
			}
		};
		
		burrowsWheeler.addActionListener(optionsListener);
		moveToFront.addActionListener(optionsListener);
		arithemicCoder.addActionListener(optionsListener);
		
		
		
		// second Panel that contains the Browse panel and the Start Panel
		JPanel secondPanel = new JPanel();
		add(secondPanel, BorderLayout.SOUTH);
		secondPanel.setLayout(new GridLayout(2,1));
		
		// Browse Panel that contains the browse button and the display of the file name
		JPanel browsePanel = new JPanel();
		secondPanel.add(browsePanel);
		browsePanel.setLayout(new BorderLayout());
		
		JTextField fileName = new JTextField();
		browsePanel.add(fileName, BorderLayout.CENTER);
		
		JButton browse = new JButton("Browse");
		browsePanel.add(browse, BorderLayout.EAST);
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					sourceFile = fileChooser.getSelectedFile();
					fileName.setText(sourceFile.getName());
					fileName.setCaretPosition(0);
				} else {
					sourceFile = null;
					fileName.setText("");
				}		
			}
		});
		
		
		
		// The Start Panel with the button that starts the 
		JPanel startPanel = new JPanel();
		secondPanel.add(startPanel);
		JButton start = new JButton("Start");
		startPanel.add(start);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkSource())
					Compressor.compress(sourceFile, bw, mtf, ac);
				else
					JOptionPane.showMessageDialog(null, "The file with the source code is not valid. It should contain only english alphabet letters.", "Invalid source file",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		

	}
	
	// Checks if the source file is valid
	private boolean checkSource(){
		
		if(sourceFile == null || !sourceFile.exists() || !sourceFile.canRead())
			return false;
		
		try(BufferedReader in = new BufferedReader(new FileReader(sourceFile));){
			String data = in.readLine();
			
			for(int i=0; i<data.length(); i++)
				if(!Character.isLetter(data.charAt(i)))
					return false;
			
		}catch (Exception e){ return false;}
				
		return true;
		
	}
	
	
	
}
