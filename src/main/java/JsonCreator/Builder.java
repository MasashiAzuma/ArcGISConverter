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
			Selector.myProgressBar.update();
			Selector.label.setText("Converge Cycle: " + (i + 1) + "/" + DATA.size() + " - " + fc.getFileName(i));
			for (int j = 1; j < DATA.get(i).size(); j++) {
				//Selector.displayTime("each build cycle" + j + "file: " + fc.getFileName(i));
				features.add(this.buildCycle(DATA.get(i).get(j), i, j));
			}
		}
		this.missingGeometry(features);
		this.writeJSON(features);

	}

	public JSONObject buildCycle(String[] row, int i, int j) {
//		int templateNum = match(fc.getFileName(i));
//		if (templateNum != -1) {
//			JSONObject feature = createFeature(TEMPLATE.get(templateNum), DATA.get(i).get(j), DATA.get(i).get(0));
//			return feature;
//		}
		
		for (int k = 0; k < row.length; k++) {
			// cycles through all data sheets
			int templateNum = match(row[k]);
			if (templateNum != -1) {
				JSONObject feature = createFeature(TEMPLATE.get(templateNum), DATA.get(i).get(j), DATA.get(i).get(0));
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
			Object[] propKeys = properties.keySet().toArray();
			Object[] propVals = properties.values().toArray();
			for (int j = 0; j < propVals.length; j++) {
//				if (target.toLowerCase().contains(propVals[j].toString().toLowerCase())) {
//					return i;
//				}
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
			features.add(this.translate(featuresArray.get(i)));
		}
		finalObj.put("type", "FeatureCollection");
		finalObj.put("features", features);

		Selector.label.setText("");
		Selector.displayTime("End time");
		Selector.myProgressBar.kill();

		try (FileWriter file = new FileWriter(fc.getDirectoryPath() + "/" + fc.getSelectedFileName() + ".json")) {
			finalObj.writeJSONString(file);
			Selector.log.append(fc.getSelectedFileName() + ".json File created successfully" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONObject translate(JSONObject feature) {
		JSONObject geo = (JSONObject) feature.get("geometry");
		JSONArray coordinates = (JSONArray) geo.get("coordinates");
		JSONArray newCoordinates = new JSONArray();

		if (coordinates.get(0) instanceof String && !coordinates.get(0).equals("NA")
				&& !coordinates.get(0).equals("NaN") && !coordinates.get(0).equals("")) {
			newCoordinates.add(Double.parseDouble((String) coordinates.get(0)));
			newCoordinates.add(Double.parseDouble((String) coordinates.get(1)));
			geo.put("coordinates", newCoordinates);
			feature.put("geometry", geo);
		}

		return feature;

	}

	public void missingGeometry(ArrayList<JSONObject> data) {
		JSONObject finalObj = new JSONObject();
		JSONArray features = new JSONArray();
		int total = 0;
		for (int i = 0; i < data.size(); i++) {
			JSONObject geo = (JSONObject) data.get(i).get("geometry");
			JSONArray coordinates = (JSONArray) geo.get("coordinates");
			if (coordinates.size() == 0) {
				total++;
				features.add(data.get(i));
			} else if (coordinates.get(0).equals("NaN") || coordinates.get(0).equals("NA")) {
				total++;
				features.add(data.get(i));
			}
		}

		finalObj.put("type", "FeatureCollection");
		finalObj.put("features", features);

		try (FileWriter file = new FileWriter(fc.getDirectoryPath() + "/" + "MissingGeo.json")) {
			finalObj.writeJSONString(file);
			Selector.log.append("Missing Geo JSON File created successfully" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Selector.log.append(total + "/" + data.size() + " Points without Geometry" + "\n");
	}

}
