package JsonCreator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Builder {
	private final ArrayList<JSONObject> TEMPLATE;
	private ArrayList<ArrayList<String[]>> DATA;
	private ArrayList<JSONObject> features;
	private FileChooser fc;
	public Builder(ArrayList<JSONObject> template, ArrayList<ArrayList<String[]>> data, FileChooser fc) {
		this.TEMPLATE = template;
		this.DATA = data;
		this.fc = fc;
	}

	public void cycle() {
		Selector.displayTime("Before build cycle");
		
		features = new ArrayList<JSONObject>();
		for (int i = 0; i < DATA.size(); i++) {
			for (int j = 1; j < DATA.get(i).size(); j++) {
				Selector.displayTime("each build cycle" + j);
				features.add(this.buildCycle(DATA.get(i).get(j), i, j));
			}
		}
		this.writeJSON(features);
	}
	
	public JSONObject buildCycle(String[] row, int i, int j){
		for(int k = 0; k < row.length; k++){
			// cycles through all data sheets
			int templateNum = match(row[k]);
			if (templateNum != -1) {
				JSONObject feature = createFeature(TEMPLATE.get(templateNum), DATA.get(i).get(j),
						DATA.get(i).get(0));
				return feature;
			} else {
				// there is no template match
			}
		}
		return null;
		
	}

	// Returns the template that matches the DATA returns -1 if nothing matches
	public int match(String target) {
		for (int i = 0; i < TEMPLATE.size(); i++) {
			JSONObject properties = (JSONObject) TEMPLATE.get(i).get("properties");
			Object[] propVals = properties.values().toArray();
			for (int j = 0; j < propVals.length; j++) {
				if (propVals[j].toString().equalsIgnoreCase(target)) {
					return i;
				}
			}
		}
		return -1;
	}

	public JSONObject createFeature(JSONObject targetTemplate, String[] targetDATA, String[] header) {
		JSONObject properties = (JSONObject) targetTemplate.get("properties");
		JSONObject propertiesCopy = (JSONObject) properties.clone();

		for (int i = 0; i < header.length; i++) {
			propertiesCopy.put(header[i], targetDATA[i]);
		}

		JSONObject feature = new JSONObject();
		feature.put("type", "Feature");
		feature.put("geometry", targetTemplate.get("geometry"));
		feature.put("properties", propertiesCopy);

		return feature;
	}

	public void writeJSON(ArrayList<JSONObject> featuresArray) {
		JSONObject finalObj = new JSONObject();
		JSONArray features = new JSONArray();

		for (int i = 0; i < featuresArray.size(); i++) {
			features.add(featuresArray.get(i));
		}
		finalObj.put("type", "FeatureCollection");
		finalObj.put("features", features);
		
		Selector.displayTime("End time");

		try (FileWriter file = new FileWriter(fc.getDirectoryPath() + "/" + "test.json")) {
			finalObj.writeJSONString(file);
			Selector.log.append("JSON File created successfully" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
