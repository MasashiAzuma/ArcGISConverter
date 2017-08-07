package JsonCreator;

import java.awt.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTabbedPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import GUI.Data;
import GUI.Geometry;
import GUI.JsonGUI;
import GUI.Properties;

public class Handler {
	private JTabbedPane tabbedPane;
	private FileChooser fc;
	private ArrayList<JsonGUI> allUI;
	private JsonGUI[][] AllGUI;
	private	int[] selectedTab;
	public int iteration;

	public Handler(JTabbedPane tabbedPane, FileChooser fc) {
		this.tabbedPane = tabbedPane;
		this.fc = fc;

		Geometry geometry = new Geometry();
		Properties properties = new Properties();
		Data data = new Data();
		tabbedPane.addTab("Geometry", geometry);
		tabbedPane.addTab("Properties", properties);
		tabbedPane.addTab("Data", data);
	}

	public void insertTabs() {
		tabbedPane.setComponentAt(0, (Component) AllGUI[0][0]);
		tabbedPane.setComponentAt(1, (Component) AllGUI[0][1]);
		tabbedPane.setComponentAt(2, (Component) AllGUI[0][2]);
		iteration = 0;
		
	}
	
	public void insertHeaders() {
		ArrayList<String[]> allHeaders = fc.getAllHeaders();
		
		for(int i = 0; i < allHeaders.size(); i++){
			for(int j = 0; j < 3; j++) {
				AllGUI[i][j].deleteCB();
				AllGUI[i][j].insertCB(allHeaders.get(i));
			}
		}
		
	}

	public void makeTabs() {
		selectedTab = new int[fc.getNumFiles()];
		AllGUI = new JsonGUI[fc.getNumFiles()][3];
		for (int i = 0; i < AllGUI.length; i++) {
			AllGUI[i][0] = new Geometry();
			AllGUI[i][1] = new Properties();
			AllGUI[i][2] = new Data();
		}
	}
	
	public void next() {
		selectedTab[iteration] = tabbedPane.getSelectedIndex();
		
		iteration++;
		if (iteration >= AllGUI.length) {
			iteration = 0;
		}
		tabbedPane.setComponentAt(0, (Component) AllGUI[iteration][0]);
		tabbedPane.setComponentAt(1, (Component) AllGUI[iteration][1]);
		tabbedPane.setComponentAt(2, (Component) AllGUI[iteration][2]);
		tabbedPane.setSelectedIndex(selectedTab[iteration]);
	}
	
	public void prev() {
		selectedTab[iteration] = tabbedPane.getSelectedIndex();
		
		iteration--;
		if (iteration < 0) {
			iteration = AllGUI.length-1;
		}
		tabbedPane.setComponentAt(0, (Component) AllGUI[iteration][0]);
		tabbedPane.setComponentAt(1, (Component) AllGUI[iteration][1]);
		tabbedPane.setComponentAt(2, (Component) AllGUI[iteration][2]);
		tabbedPane.setSelectedIndex(selectedTab[iteration]);
	}
	
	public void select(int i) {
		selectedTab[iteration] = tabbedPane.getSelectedIndex();
		
		iteration = i;
		tabbedPane.setComponentAt(0, (Component) AllGUI[iteration][0]);
		tabbedPane.setComponentAt(1, (Component) AllGUI[iteration][1]);
		tabbedPane.setComponentAt(2, (Component) AllGUI[iteration][2]);
		tabbedPane.setSelectedIndex(selectedTab[iteration]);
		
	}

	public void getJson() throws IOException {
	
		
		
		Json[] jsonCreator = new Json[fc.getNumFiles()];
		ArrayList<ArrayList<String[]>> DATA = fc.getAllData();
		ArrayList<JSONObject> directoryConverge = new ArrayList<JSONObject>();
		
		
		for(int i = 0; i < jsonCreator.length; i++) {
			jsonCreator[i] = new Json();
			ArrayList<JSONObject> temp = jsonCreator[i].getJson(AllGUI[i], DATA.get(i));
			for(int j = 0; j < temp.size(); j++) {
				directoryConverge.add(temp.get(j));
			}
		}
		
		System.out.println("-------------------------------------");
		System.out.println(directoryConverge.size());
		
		Json finalJSON = new Json();
		ArrayList<JSONObject> finalArray = finalJSON.convergeJson(directoryConverge);
		//final array is the template
		missingGeometry(finalArray);
		Builder builder = new Builder(finalArray, fc.getAllData());
		builder.cycle();
		
		//pack(finalArray);
		
	}
	
	public void pack(ArrayList<JSONObject> pack) throws IOException {
		JSONObject endProduct = new JSONObject();
		endProduct.put("type", "FeatureCollection");
		JSONArray features = new JSONArray();
		for(int i = 0; i < pack.size(); i++) {
			features.add(pack.get(i));
		}
		endProduct.put("features", features);
		endProduct.put("name", fc.getDirectoryName());
		
		System.out.println(endProduct);
		
//		try (FileWriter file = new FileWriter("/Users/azum288/Desktop/Locations/file1.json")) {
//			file.write(endProduct.toJSONString());
//			System.out.println("Successfully Copied JSON Object to File...");
//			System.out.println("\nJSON Object: " + endProduct);
//		}
		
		
		
	
	}
	
	public void missingGeometry(ArrayList<JSONObject> data) {
		int total = 0;
		for(int i = 0; i < data.size(); i++) {
			JSONObject geo = (JSONObject) data.get(i).get("geometry");
			JSONArray coordinates = (JSONArray) geo.get("coordinates");
			if (coordinates.get(0).equals("NaN")) {
				total++;
			}
		}
		Selector.log.append(total + " Points without Geometry" + "\n");
	}
}
