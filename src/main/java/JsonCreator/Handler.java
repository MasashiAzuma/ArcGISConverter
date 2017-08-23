package JsonCreator;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTabbedPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import GUI.Geometry;
import GUI.JsonGUI;
import GUI.Properties;

public class Handler {
	private JTabbedPane tabbedPane;
	private FileChooser fc;
	private ArrayList<JsonGUI> allUI;
	private JsonGUI[][] AllGUI;

	private Geometry geometry;
	private Properties property;

	private int[] selectedTab;
	public int iteration;
	private boolean allFileForamt;

	public Handler(JTabbedPane tabbedPane, FileChooser fc) {
		this.tabbedPane = tabbedPane;
		this.fc = fc;

		geometry = new Geometry();
		property = new Properties();
		tabbedPane.addTab("Geometry", geometry);
		tabbedPane.addTab("Properties", property);
	}

	public void insertHeaders() {
		geometry.deleteCB();
		property.deleteCB();

		String[][] allHeaders = fc.getAllHeaders();

		HashSet<String> singleHeaders = new HashSet<String>();
		for (int i = 0; i < allHeaders.length; i++) {
			for (int j = 0; j < allHeaders[i].length; j++) {
				singleHeaders.add(allHeaders[i][j]);
			}
		}
		Object[] singleHeaderArray = singleHeaders.toArray();
		Set<String> ordered = new TreeSet<String>();
		for (int i = 0; i < singleHeaderArray.length; i++) {
			ordered.add(singleHeaderArray[i].toString());
		}
		geometry.insertCB(ordered.toArray());
		property.insertCB(ordered.toArray());
	}

	public void createJSON(String keyFileName) {
		JSONObject keyJson = processKey(keyFileName);
		File[] directory = fc.getAllBut(keyFileName);
		JSONArray features = new JSONArray();
		for (int i = 0; i < directory.length; i++) {
			this.processData(directory[i], keyJson, features);
		}

		JSONObject finalObj = new JSONObject();
		finalObj.put("features", features);
		finalObj.put("type", "FeatureCollection");

		Selector.displayTime("Done creating Features");

		this.write(finalObj);
		// need to read in directory files given keyJSON (you also have headersIndexes
		// to consider)

	}

	public void processData(File file, JSONObject keyJson, JSONArray features) {
		System.out.println(file.getName());
		JSONArray coordinates = this.fileNameCheck(file, keyJson);
		boolean matchedName = false;
		if (coordinates != null)
			matchedName = true;
		System.out.println(matchedName);

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String firstLine = reader.readLine();
			firstLine = filter(firstLine, "\"");
			String[] header = firstLine.split(",", -1);
			for (int j = 0; j < header.length; j++)
				header[j] = header[j].trim().toLowerCase();

			int[][] headerIndexs = this.getHeaderIndex(header);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				JSONObject feature = new JSONObject();
				JSONObject geometry = new JSONObject();
				JSONObject property = new JSONObject();

				line = this.filter(line, "\"");
				String[] split = line.split(",", -1);

				// processes the geometry
				if (!matchedName) {
					if (headerIndexs[0].length != 0) {
						for (int i = 0; i < headerIndexs[0].length; i++) {
							coordinates.add(split[headerIndexs[0][i]]);
						}
					} else if (headerIndexs[1].length != 0) {
						for (int i = 0; i < headerIndexs[1].length; i++) {
							JSONArray targGeo = (JSONArray) keyJson.get(split[headerIndexs[1][i]]);
							if (targGeo != null)
								coordinates = targGeo;
						}
					} else {
						for (int i = 0; i < split.length; i++) {
							JSONArray targGeo = (JSONArray) keyJson.get(split[i]);
							if (targGeo != null)
								coordinates = targGeo;
						}
					}
				}
				geometry.put("type", "Point");
				geometry.put("coordinates", coordinates);

				// process the properties (super easy haha)
				for (int i = 0; i < split.length; i++) {
					property.put(header[i], split[i]);
				}

				feature.put("geometry", geometry);
				feature.put("properties", property);
				feature.put("type", "Feature");
				features.add(feature);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONArray fileNameCheck(File file, JSONObject keyJson) {
		Object[] proNames = keyJson.keySet().toArray();
		String fileName = file.getName();
		for (int i = 0; i < proNames.length; i++) {
			if (proNames[i].toString().length() != 0
					&& fileName.toLowerCase().contains(proNames[i].toString().toLowerCase())) {
				return (JSONArray) keyJson.get(proNames[i]);
			}
		}
		return null;
	}

	public JSONObject processKey(String fileName) {
		File file = fc.getFile(fileName);
		JSONObject keyJson = new JSONObject();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

			String firstLine = reader.readLine();
			firstLine = filter(firstLine, "\"");
			String[] split = firstLine.split(",", -1);
			for (int j = 0; j < split.length; j++)
				split[j] = split[j].trim().toLowerCase();

			int[][] headerIndexs = this.getHeaderIndex(split);

			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				line = this.filter(line, "\"");
				split = line.split(",", -1);
				JSONArray geometry = new JSONArray();

				for (int i = 0; i < headerIndexs[0].length; i++) {
					try {
						geometry.add(Double.parseDouble(split[headerIndexs[0][i]]));
					} catch (NumberFormatException e) {

					}

				}
				for (int i = 0; i < headerIndexs[1].length; i++) {
					keyJson.put(split[headerIndexs[1][i]], geometry);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Selector.displayTime("Done creating key");
		return keyJson;
	}

	public int[][] getHeaderIndex(String[] split) {
		String[] geoHead = geometry.pullHeaders();
		String[] proHead = property.pullHeaders();

		int[] geoIndex = new int[geoHead.length];
		int[] proIndex = new int[proHead.length];

		for (int i = 0; i < geoHead.length; i++) {
			for (int j = 0; j < split.length; j++) {
				if (geoHead[i].equalsIgnoreCase(split[j])) {
					geoIndex[i] = j + 1;
					break;
				}
			}
		}

		for (int i = 0; i < proHead.length; i++) {
			for (int j = 0; j < split.length; j++) {
				if (proHead[i].equalsIgnoreCase(split[j])) {
					proIndex[i] = j + 1;
					break;
				}
			}
		}

		int[][] newestArray = new int[2][];

		int j = 0;
		for (int i = 0; i < geoIndex.length; i++) {
			if (geoIndex[i] != 0)
				geoIndex[j++] = geoIndex[i];
		}
		int[] newArray = new int[j];
		System.arraycopy(geoIndex, 0, newArray, 0, j);
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = newArray[i] - 1;
		}

		newestArray[0] = newArray;

		j = 0;
		for (int i = 0; i < proIndex.length; i++) {
			if (proIndex[i] != 0)
				proIndex[j++] = proIndex[i];
		}
		newArray = new int[j];
		System.arraycopy(proIndex, 0, newArray, 0, j);
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = newArray[i] - 1;
		}

		newestArray[1] = newArray;

		return newestArray;
	}

	public void write(JSONObject target) {
		System.out.println(fc.getDirectoryPath());
		System.out.println(fc.getSelectedFileName());
		try (FileWriter file = new FileWriter(fc.getDirectoryPath() + "/" + fc.getSelectedFileName() + ".json")) {
			target.writeJSONString(file);
			Selector.log.append(fc.getSelectedFileName() + ".json File created successfully" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String filter(String filter, String chars) {
		char[] ca = chars.toCharArray();
		for (char c : ca)
			filter = filter.replace("" + c, "");
		return filter;
	}

}
