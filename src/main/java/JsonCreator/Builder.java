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

	public Builder(ArrayList<JSONObject> template, ArrayList<ArrayList<String[]>> data) {
		this.TEMPLATE = template;
		this.DATA = data;

	}

	public void cycle() {
		features = new ArrayList<JSONObject>();
		for (int i = 0; i < DATA.size(); i++) {
			for (int j = 1; j < DATA.get(i).size(); j++) {
				for (int k = 0; k < DATA.get(i).get(j).length; k++) {
					// cycles through all data sheets
					if (match(DATA.get(i).get(j)[k]) != -1) {
						int templateNum = match(DATA.get(i).get(j)[k]);
						JSONObject feature = createFeature(TEMPLATE.get(templateNum), DATA.get(i).get(j),
								DATA.get(i).get(0));
						features.add(feature);
					} else {
						// there is no template match
					}
				}
			}
		}

		this.writeJSON(features);
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

		for (int i = 0; i < targetDATA.length; i++) {
			propertiesCopy.put(header[i], targetDATA[i]);
		}

		JSONObject feature = new JSONObject();
		feature.put("type", "Feature");
		feature.put("geometry", targetTemplate.get("geometry"));
		feature.put("properties", propertiesCopy);

		// System.out.println(feature);

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

		try (FileWriter file = new FileWriter("/Users/azum288/Desktop/Locations/file1.json")) {
			file.write(finalObj.toJSONString());
			Selector.log.append("JSON File created successfully" + "\n");
			System.out.println("\nJSON Object: " + finalObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
