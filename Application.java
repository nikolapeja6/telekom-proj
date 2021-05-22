package nikolapeja6.Telekom;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class Application extends JFrame {
	
	// File containing the source message
	private File sourceFile;
	
	// Flags for options: Burrows–Wheeler, Move to Front and AritheticCoder
	private boolean bw = false;
	private boolean mtf = false;
	private boolean ac = false;
	
	// FileChooser for choosing the source file
	JFileChooser fileChooser;
	
	private JDialog helpDialog;
	private JDialog aboutDialog;
	private JTextField fileName;


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
		
		// Create the help and about windows
		createHelpAndAbout();
		
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
	
	private void createHelpAndAbout(){
		
		// Help Dialog
		helpDialog = new JDialog();
		helpDialog.setDefaultCloseOperation(HIDE_ON_CLOSE);		
		helpDialog.setSize(800, 700);
		helpDialog.setTitle("Help");
		JTextPane helpLabel = new JTextPane();
		helpLabel.setContentType("text/html");
		helpLabel.setEditable(false);
		helpLabel.setFont(getFont());
		final String helpText = 	"<html><h1>Compression ratio - Help</h1>"+
				"<h2>How to Use</h2>"+
				"<p>The <i>Compression ratio</i> shows to compression ratio (at least I think that is the proper English term) of the compression using the 3 methods: <i>Burrows–Wheeler transformation</i>,"+
				" the <i>Move to Front</i> and the <i>Arithmetic Coder</i>.</p>"+					
				"<p>In order to do this, you need to:</p><ul style=\"list-style-type:circle\">"+
				"<li>Select the text file (.txt) that contains the source message by clicking the <b>Browse</b> button and then choosing the file. The name of the file you have chosen will appear next to the <i>Browse</i> button."+
				" Note: the file with the source message must contain <b>only English alphabet letters in a single line, no spaces, tabs, numbers, etc.</b>. "+
				"The program is <b>case insensitive</b>, as before the transformation it transforms all letters to upper case</li>"+
				"<li>Select the options (the checkboxes in the upper part of the screen) for displaying the step-by-step work of the appropriate algorithms. Note: the output produced by selecting these options will "+
				"be produced in a <b>separate file (output.txt)</b> in the same directory as the program.</li>"+
				"<li>Click the <b>Start</b> button to start the compression. If the compression was successful, you will see a message stating this and displaying what the compression ratio is.</li></ul>"+
				"<h2>Other</h2>"+
				"<h3>Error messages</h3>"+
				"<p>If you have not selected a valid source file, if the file does not meet the required format (see above), or if an error ocurred during the compression, an <i>error message</i> will appear.</p>"+
				"<h3>Warning</h3>"+
				"<p>The <i>Compression ratio</i> <b>does not warn you if the output file (<i>output.txt</i>) already exists. If it does, it will be overwritten with the new one and the old file will be lost</b>. The user is advised to check if there is a file with the name <i>output.txt</i> in the same folder as the program."+
				"If there is such a file (if you wish to keep the old file), you should rename it.</p></html>";

		helpLabel.setText(helpText);
		JScrollPane helpScroll = new JScrollPane(helpLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		helpDialog.add(helpScroll);
		
		
		
		aboutDialog = new JDialog();
		aboutDialog.setDefaultCloseOperation(HIDE_ON_CLOSE);		
		aboutDialog.setSize(500,330);
		aboutDialog.setTitle("About");
		JTextPane aboutLabel = new JTextPane();
		aboutLabel.setContentType("text/html");
		aboutLabel.setEditable(false);
		aboutLabel.setFont(getFont());
		final String aboutText = 	"<html><h1>About</h1>"+
				"<h2>Compression ratio (March 2017)</h2>"+
				"<p>The <i>Compression ratio</i> is a program written by Nikola Pejic as part of his homework assignment for the subject \"Osnovi Telekomunikacija\" at the <a href=\"http://www.etf.bg.ac.rs/\">School of Electrical Engineering</a> at the <a href=\"http://www.bg.ac.rs/en/\">University of Belgrade</a>.</p>"+
				"<p>This program shows to compression ratio (at least I think that is the proper English term) of the compression using the 3 methods: <i>Burrows–Wheeler transformation</i>,"+
				" the <i>Move to Front</i> and the <i>Arithmetic Coder</i>. Additionally, it can show a step-by-step view of the algorithm with a given input.</p></html>";

		aboutLabel.setText(aboutText);
		JScrollPane aboutScroll = new JScrollPane(aboutLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		aboutDialog.add(aboutScroll);
		aboutLabel.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    try {
                        Desktop.getDesktop().browse(hle.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
});
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
				aboutDialog.setLocationRelativeTo(menuBar.getParent());
				aboutDialog.setVisible(true);
			}
		});
		
		// Help
		JMenuItem help = new JMenuItem("Help");
		helpMenu.add(help);
		help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				helpDialog.setLocationRelativeTo(menuBar.getParent());
				helpDialog.setVisible(true);
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
		
		fileName = new JTextField();
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
				
					double val = -1;
					
					try{
						val = Compressor.compress(sourceFile, bw, mtf, ac);
					}catch(Exception ee){}
					
					if(val < 0)
					JOptionPane.showMessageDialog(null, "The file with the source code is not valid or an error occured while compressing.\nCheck is the file contains only English alphabet letters.", "Error",
							JOptionPane.ERROR_MESSAGE);
					else
						JOptionPane.showMessageDialog(null, "The conversion was successfully finished.\nThe compression ratio is "+String.format("%.4f",val), "Successful compression",	JOptionPane.INFORMATION_MESSAGE);
			}
		});
		

	}

}
