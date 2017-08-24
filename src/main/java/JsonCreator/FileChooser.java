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

	JFileChooser chooser;
	String choosertitle;
	File[] directory;

	Component c;

	public FileChooser(Component c) {
		this.c = c;
	}

	public void openFile() {
		int result;

		chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Acceptable Files", "csv", "txt"));
		chooser.setCurrentDirectory(new File("/Users/SerARK/Desktop/Patrick Data"));
		// chooser.setCurrentDirectory(new File("/Users/azum288/Desktop"));
		chooser.setDialogTitle(choosertitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(false);

		// reading one or two files
		if (chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			chooser.setCurrentDirectory(new File(chooser.getCurrentDirectory().toString()));
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
			if (chooser.getSelectedFile().isFile()) {
				directory = new File[2];
				directory[0] = chooser.getSelectedFile();
				directory[1] = chooser.getSelectedFile();
			} else if (chooser.getSelectedFile().isDirectory()) {
				directory = chooser.getSelectedFile().listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".csv"))
							return true;
						return false;
					}
				});
			}
		} else {
			System.out.println("No Selection ");
		}
	}

	public File getFile(String fileName) {
		for (int i = 0; i < directory.length; i++) {
			if (directory[i].getName().equals(fileName))
				return directory[i];
		}
		return null;
	}

	public String[][] getAllHeaders() {
		String[][] allHeaders = new String[directory.length][];
		for (int i = 0; i < directory.length; i++) {
			allHeaders[i] = this.readHeaders(directory[i].getName());
		}
		return allHeaders;
	}

	public String[] readHeaders(String fileName) {
		// returns the headers for the specific fileName
		for (int i = 0; i < directory.length; i++) {
			if (directory[i].getName().equals(fileName)) {
				try (BufferedReader reader = new BufferedReader(new FileReader(directory[i]))) {
					String firstLine = reader.readLine();
					firstLine = filter(firstLine, "\"");
					String[] split = firstLine.split(",", -1);
					for (int j = 0; j < split.length; j++)
						split[j] = split[j].trim().toLowerCase();
					return split;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public File[] getAllBut(String fileName) {
		File[] newDirectory = new File[directory.length - 1];
		int index = 0;
		for (int i = 0; i < newDirectory.length; i++) {
			if (!directory[index].getName().equals(fileName)) {
				newDirectory[i] = directory[index];
				index++;
			}
		}
		return newDirectory;
	}

	public String getFileName(int num) {
		return directory[num].getName();
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

	public int getNumFiles() {
		return directory.length;
	}

	public String getSelectedFileName() {
		return chooser.getSelectedFile().getName();
	}

	public String getDirectoryName() {
		return chooser.getCurrentDirectory().getName();
	}

	public String getDirectoryPath() {
		return chooser.getCurrentDirectory().getPath().toString();
	}

}