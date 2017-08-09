package JsonCreator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.geotools.swing.data.JFileDataStoreChooser;

import GUI.Geometry;
import GUI.JsonGUI;
import GUI.Properties;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class Selector extends JFrame {
	public static JTextArea log;
	public static long startTime;
	public static long lapTime;
	
	private JPanel contentPane;
	private String[] headers;
	private ArrayList<String[]> allData;
	private ArrayList<JsonGUI> allUI;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Selector frame = new Selector();
					frame.setVisible(true);
					frame.pack();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void displayTime(String thing) {
		Selector.lapTime = System.nanoTime();
		System.out.println((lapTime - startTime)/1000000000 + " sec: " + thing);
	}

	public Selector() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 200, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 357, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnAddFile = new JButton("Add File");
		GridBagConstraints gbc_btnAddFile = new GridBagConstraints();
		gbc_btnAddFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddFile.gridx = 1;
		gbc_btnAddFile.gridy = 0;
		contentPane.add(btnAddFile, gbc_btnAddFile);

		JTabbedPane tabbedPane = new JTabbedPane();

		Component c = contentPane;
		FileChooser fc = new FileChooser(c);
		Handler hd = new Handler(tabbedPane, fc);

		JLabel lblFiles = new JLabel("Files");
		GridBagConstraints gbc_lblFiles = new GridBagConstraints();
		gbc_lblFiles.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiles.anchor = GridBagConstraints.EAST;
		gbc_lblFiles.gridx = 0;
		gbc_lblFiles.gridy = 1;
		contentPane.add(lblFiles, gbc_lblFiles);

		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		contentPane.add(comboBox, gbc_comboBox);

		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 3;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 2;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		JButton btnPrev = new JButton("Previous");
		GridBagConstraints gbc_btnPrev = new GridBagConstraints();
		gbc_btnPrev.insets = new Insets(0, 0, 5, 5);
		gbc_btnPrev.gridx = 0;
		gbc_btnPrev.gridy = 3;
		contentPane.add(btnPrev, gbc_btnPrev);

		JButton btnSubmit = new JButton("Submit");
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 5);
		gbc_btnSubmit.gridx = 1;
		gbc_btnSubmit.gridy = 3;
		contentPane.add(btnSubmit, gbc_btnSubmit);

		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnNext = new GridBagConstraints();
		gbc_btnNext.insets = new Insets(0, 0, 5, 0);
		gbc_btnNext.gridx = 2;
		gbc_btnNext.gridy = 3;
		contentPane.add(btnNext, gbc_btnNext);
		
		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(log);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		contentPane.add(scrollPane, gbc_scrollPane);

		btnAddFile.addActionListener((ActionEvent e) -> {
			fc.reset();
			fc.openFile();
			hd.makeTabs();
			hd.insertHeaders();
			hd.insertTabs();

			comboBox.setModel(new DefaultComboBoxModel(fc.getFile()));

		});
		
		btnNext.addActionListener((ActionEvent e) -> {
			hd.next();
			comboBox.setSelectedIndex(hd.iteration);
			contentPane.validate();
			contentPane.repaint();
			
		});
		
		btnPrev.addActionListener((ActionEvent e) -> {
			hd.prev();
			comboBox.setSelectedIndex(hd.iteration);
			contentPane.validate();
			contentPane.repaint();
		});

		btnSubmit.addActionListener((ActionEvent e) -> {
				//new Thread(()->MyProgressBar.gen(this)).start();
				new Thread(()->hd.getJson()).start();
				//MyProgessBar.get().update(updateContext);
		});
		
		comboBox.addItemListener((ItemEvent e) -> {
			hd.select(comboBox.getSelectedIndex());
			contentPane.validate();
			contentPane.repaint();
		});

	}
}
//class MyProgressBar extends JDialog{
//	private static final long serialVersionUID = 1L;
//	private static Optional<MyProgressBar> mpb = Optional.empty();
//	public static void gen(JFrame jf) {
//		if(!mpb.isPresent()) {
//			mpb=Optional.of(new MyProgressBar(jf));						
//		}					
//	}
//	MyProgressBar(JFrame jf){
//		super(jf, true);
//		make();
//	}
//	private void make() {
//		// TODO Auto-generated method stub
//		
//	}
//	public void update(UpdateContext ux) {
//		//update code
//		if(ux.isKill) {
//			end();
//		}
//	}
//	private void end() {
//		mpb=Optional.empty();
//		this.dispose();
//	}
//	
//}
