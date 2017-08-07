package JsonCreator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.geotools.swing.data.JFileDataStoreChooser;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.awt.*;
import java.util.*;

public class FileChooser {
	private ArrayList<ArrayList<String[]>> DATA;

	JFileChooser chooser;
	String choosertitle;
	File[] directory;

	Component c;

	public FileChooser(Component c) {
		this.c = c;
		DATA = new ArrayList<ArrayList<String[]>>();
	}

	public void openFile() {
		int result;

		chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Acceptable Files", "csv", "txt"));
		chooser.setCurrentDirectory(new File("/Users/azum288/Desktop/Locations"));
		chooser.setDialogTitle(choosertitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			chooser.setCurrentDirectory(new File(chooser.getCurrentDirectory().toString()));
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
			if (chooser.getSelectedFile().isFile()) {
				directory = new File[1];
				directory[0] = chooser.getSelectedFile();
				try {
					readFile(directory[0]);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (chooser.getSelectedFile().isDirectory()) {
				directory = chooser.getSelectedFile().listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".csv"))
							return true;
						return false;
					}
				});
				for (int i = 0; i < directory.length; i++) {
					try {
						readFile(directory[i]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("No Selection ");
		}
	}

	public void readFile(File file) throws FileNotFoundException, IOException {
		final String DELIMITER = ",";

		ArrayList<String[]> allData = new ArrayList<String[]>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			// Read the first line (Header)
			String line = reader.readLine();
			line = filter(line, "\"");

			String[] split = line.split(DELIMITER);
			String[] headers = new String[split.length + 1];
			headers[0] = "None";
			for (int i = 0; i < split.length; i++) {
				headers[i + 1] = split[i];
			}

			for (int i = 0; i < headers.length; i++) {
				headers[i] = headers[i].trim();
			}
			for (int i = 0; i < split.length; i++) {
				split[i] = split[i].trim();
			}
			allData.add(split);

			for (line = reader.readLine(); line != null; line = reader.readLine()) {
				String lineCheck = filter(line, DELIMITER);

				if (lineCheck.trim().length() > 0) { // skip blank lines
					String tokens[] = line.split("\\,");
					for (int i = 0; i < tokens.length; i++) {
						tokens[i] = tokens[i].trim();
					}
					allData.add(tokens);
				}
			}
		}

		DATA.add(allData);
	}

	public ArrayList<String[]> tempData() {
		return DATA.get(0);
	}

	public void reset() {
		DATA = new ArrayList<ArrayList<String[]>>();
	}

	public String[] getFile() {
		if (directory == null) {
			return new String[0];

		} else {

			String[] directoryNames = new String[directory.length];
			for (int i = 0; i < directoryNames.length; i++) {
				directoryNames[i] = directory[i].getName();
			}

			return directoryNames;
		}
	}

	private static String filter(String filter, String chars) {
		char[] ca = chars.toCharArray();
		for (char c : ca)
			filter = filter.replace("" + c, "");
		return filter;
	}

	public String[] getHeaders() {
		final String DELIMITER = ",";
		ArrayList<String> headers = new ArrayList<String>();
		for (int i = 0; i < DATA.size(); i++) {
			String[] firstRow = DATA.get(i).get(0);
			for (int j = 0; j < firstRow.length; j++) {
				headers.add(firstRow[j]);
			}
		}
		String[] headersArray = new String[headers.size()];
		for (int i = 0; i < headersArray.length; i++) {
			headersArray[i] = headers.get(i);
		}
		return headersArray;

	}

	public String[] getCBHeaders() {
		String[] headers = this.getHeaders();
		String[] CBheaders = new String[headers.length + 1];
		CBheaders[0] = "None";
		for (int i = 0; i < headers.length; i++) {
			CBheaders[i + 1] = headers[i];
		}

		return CBheaders;

	}

	public ArrayList<ArrayList<String[]>> getAllData() {
		return DATA;
	}

	public int getNumFiles() {
		return directory.length;
	}
	
	public String getDirectoryName() {
		return chooser.getCurrentDirectory().getName();
	}

	public ArrayList<String[]> getAllHeaders() {
		ArrayList<String[]> allHeaders = new ArrayList<String[]>();
		for (int i = 0; i < DATA.size(); i++) {
			String[] headers = DATA.get(i).get(0);
			String[] CBheaders = new String[headers.length + 1];
			CBheaders[0] = "None";
			for (int j = 0; j < headers.length; j++) {
				CBheaders[j + 1] = headers[j];
			}
			allHeaders.add(CBheaders);
		}
		return allHeaders;
	}
}