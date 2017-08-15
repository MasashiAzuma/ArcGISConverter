package JsonCreator;

import java.util.ArrayList;
import java.util.Arrays;

import javax.measure.unit.SystemOfUnits;

import org.json.simple.*;

import GUI.JsonGUI;

//handles the JSON conversion using json.simple
public class Json {

	public ArrayList<JSONObject> getJson(JsonGUI[] allGUI, ArrayList<String[]> allData) {
		ArrayList<JSONObject> singularJson = new ArrayList<JSONObject>();

		for (int i = 1; i < allData.size(); i++) {
			singularJson.add(new JSONObject());
			for (int j = 0; j < allGUI.length; j++) {
				JSONObject ele = allGUI[j].pullJson(allData, i);
				Object[] keys = ele.keySet().toArray();
				for (int k = 0; k < keys.length; k++) {
					singularJson.get(i - 1).put(keys[k], ele.get(keys[k]));
				}

			}
		}
		return convergeJson(singularJson);
	}

	public ArrayList<JSONObject> convergeJson(ArrayList<JSONObject> data) {
		// first get the keys from properties
		// then compare their names
		
		ArrayList<JSONObject> finalized = new ArrayList<JSONObject>();

		if (data.size() == 1) {
			return data;
		}

		while (data.size() > 1) {

			ArrayList<JSONObject> matched = new ArrayList<JSONObject>();
			ArrayList<Object> propKey = new ArrayList<Object>();
			JSONObject temp = (JSONObject) data.get(0).get("properties");

			// add in implementation for different stuff
			for (Object o : temp.values()) {
				propKey.add(this.filter(o.toString(), "\""));
			}

			// matched.add(data.get(0));

			for (int i = data.size() - 1; i >= 0; i--) {
				JSONObject prop = (JSONObject) data.get(i).get("properties");
				for (int j = 0; j < propKey.size(); j++) {

					if (prop.containsValue(propKey.get(j))) {
						for (Object o : prop.values()) {
							propKey.add(this.filter(o.toString(), "\""));
						}
						matched.add(data.remove(i));
						break;
					}
				}
			}
			finalized.add(combine(matched));
			if (data.size() == 1) {
				finalized.add(data.get(0));
			}
		}
		for (int i = 0; i < finalized.size(); i++) {
			//System.out.println(finalized.get(i));
		}
		return finalized;
	}


	// given a matched array list of JSONobjects I want to combine the data and
	// properties fields
	public JSONObject combine(ArrayList<JSONObject> matched) {
		// combine the property fields

		// the combined properties of all matched fields (has to go through the matching
		// again)

		// combine the geometry field here
		
		JSONObject combinedGeo = new JSONObject();
		JSONArray coordArray = new JSONArray();
		Double latTotal = 0.0;
		Double lonTotal = 0.0;
		int size = 0;
		for (int i = 0; i < matched.size(); i++) {
			JSONObject geo = (JSONObject) matched.get(i).get("geometry");
			JSONArray coordinates = (JSONArray) geo.get("coordinates");

			if (coordinates.size() != 0 && !coordinates.get(0).toString().equals("")
					&& !coordinates.get(0).equals("NaN") && !coordinates.get(0).equals("NA")) {
				if (!coordinates.get(0).toString().isEmpty() && !coordinates.get(1).toString().isEmpty()) {
					latTotal += Double.parseDouble((String) coordinates.get(0));
					lonTotal += Double.parseDouble((String) coordinates.get(1));
					size++;
				}
			}
		}

		latTotal = latTotal / size;
		lonTotal = lonTotal / size;

		combinedGeo = (JSONObject) matched.get(0).get("geometry");
		coordArray.add(latTotal.toString());
		coordArray.add(lonTotal.toString());
		combinedGeo.put("coordinates", coordArray);

		/* this method is okay */
		JSONObject combinedProp = new JSONObject();

		for (int i = 0; i < matched.size(); i++) {
			JSONObject property = (JSONObject) matched.get(i).get("properties");
			Object[] propKey = property.keySet().toArray();
			for (int j = 0; j < propKey.length; j++) {
				combinedProp.put(propKey[j], property.get(propKey[j]));
			}
		}

		JSONObject consolidatedJSON = new JSONObject();
		consolidatedJSON.put("geometry", combinedGeo);
		consolidatedJSON.put("properties", combinedProp);
		consolidatedJSON.put("type", "Feature");

		return consolidatedJSON;
	}
	
	private static String filter(String filter, String chars) {
		char[] ca = chars.toCharArray();
		for (char c : ca)
			filter = filter.replace("" + c, "");
		return filter;
	}
}
