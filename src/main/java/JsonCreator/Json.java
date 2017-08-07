package JsonCreator;

import java.util.ArrayList;
import java.util.Arrays;

import javax.measure.unit.SystemOfUnits;

import org.json.simple.*;

import GUI.JsonGUI;

//handles the JSON conversion using json.simple
public class Json {
	// private ArrayList<String[]> allData;
	// private ArrayList<JSONObject> compiled;

	public Json() {
		// this.allData = allData;

	}

	public void reset() {
		// allData = new ArrayList<String[]>();
	}

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
		
		if(data.size()==1) {
			return data;
		}

		while (data.size() > 1) {

			ArrayList<JSONObject> matched = new ArrayList<JSONObject>();
			ArrayList<Object> propKey = new ArrayList<Object>();
			JSONObject temp = (JSONObject) data.get(0).get("matching properties");

			// add in implementation for different stuff
			for (Object o : temp.values()) {
				propKey.add(o);
			}

			// matched.add(data.get(0));

			for (int i = data.size() - 1; i >= 0; i--) {
				JSONObject prop = (JSONObject) data.get(i).get("matching properties");
				for (int j = 0; j < propKey.size(); j++) {

					if (prop.containsValue(propKey.get(j))) {
						for (Object o : prop.values()) {
							propKey.add(o);
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
			System.out.println(finalized.get(i));
		}
		return finalized;

	}

	public ArrayList<JSONObject> matchJSON(JSONObject target, ArrayList<JSONObject> data) {
		// given a single target we have to match the keys then within the matched keys
		// find the matched values
		// if a value is matched add the JSON into a matched ArrayList

		// json objects within data that match (property wise) with target (includes
		// target)
		ArrayList<JSONObject> matched = new ArrayList<JSONObject>();
		matched.add(target);
		JSONObject targProp = (JSONObject) target.get("matching Properties");
		Object[] targKeys = targProp.keySet().toArray();

		for (int i = data.size() - 1; i <= 0; i--) {
			JSONObject compProp = (JSONObject) data.get(i).get("matching Properties");
			Object[] compKeys = compProp.keySet().toArray();
			for (int j = 0; j < targKeys.length; j++) {
				for (int k = 0; k < compKeys.length; k++) {
					// keys are matched then we have to check if the values are matched or not
					if (targProp.get(targKeys[j]).equals(compProp.get(compKeys[k]))) {
						matched.add(data.remove(i));
					}
				}
			}
		}

		return null;
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

			if (coordinates.size() != 0 && !coordinates.get(0).equals("") && !coordinates.get(0).equals("NaN")) {
				latTotal += Double.parseDouble((String) coordinates.get(0));
				lonTotal += Double.parseDouble((String) coordinates.get(1));
				size++;
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
			JSONObject property = (JSONObject) matched.get(i).get("matching properties");
			Object[] propKey = property.keySet().toArray();
			for (int j = 0; j < propKey.length; j++) {
				combinedProp.put(propKey[j], property.get(propKey[j]));
			}
		}

		// combine the data fields

		// the combined data fields needs to make sure it works for multiple datasets
		JSONObject combinedData = new JSONObject();

		ArrayList<Object> uniqueDataKey = new ArrayList<Object>();
		ArrayList<JSONArray> dataSetArray = new ArrayList<JSONArray>();

		for (int i = 0; i < matched.size(); i++) {
			JSONObject data = (JSONObject) matched.get(i).get("data");
			Object[] dataKey = data.keySet().toArray();
			for (int j = 0; j < dataKey.length; j++) {
				if (!uniqueDataKey.contains(dataKey[j])) {
					uniqueDataKey.add(dataKey[j]);
					dataSetArray.add(new JSONArray());
				}
				if (data.get(dataKey[j]) instanceof JSONObject) {
					if (!dataSetArray.get(uniqueDataKey.indexOf(dataKey[j])).contains(data.get(dataKey[j])))
						dataSetArray.get(uniqueDataKey.indexOf(dataKey[j])).add(data.get(dataKey[j]));
				} else if (data.get(dataKey[j]) instanceof JSONArray) {
					JSONArray temp = (JSONArray) data.get(dataKey[j]);
					for (int k = 0; k < temp.size(); k++) {
						if (!dataSetArray.get(uniqueDataKey.indexOf(dataKey[j])).contains(temp.get(k))) {
							dataSetArray.get(uniqueDataKey.indexOf(dataKey[j])).add(temp.get(k));
						}
					}
				}

			}
		}

		for (int i = 0; i < dataSetArray.size(); i++) {
			combinedData.put(uniqueDataKey.get(i), dataSetArray.get(i));
		}

		JSONObject consolidatedJSON = new JSONObject();
		consolidatedJSON.put("geometry", combinedGeo);
		consolidatedJSON.put("data", combinedData);
		consolidatedJSON.put("properties", combinedProp);
		consolidatedJSON.put("matching properties", (JSONObject) matched.get(0).get("matching properties"));
		consolidatedJSON.put("type", "Feature");

		// have to check the keys that match and then append the data

		return consolidatedJSON;
	}

	public boolean compObj(JSONObject obj1, JSONObject obj2) {
		char[] first = obj1.toJSONString().toCharArray();
		char[] second = obj2.toJSONString().toCharArray();
		Arrays.sort(first);
		Arrays.sort(second);
		return Arrays.equals(first, second);
	}

	public boolean compObj(JSONArray array, JSONObject obj) {
		for (int i = 0; i < array.size(); i++) {
			char[] first = array.get(i).toString().toCharArray();
			char[] second = obj.toString().toCharArray();
			Arrays.sort(first);
			Arrays.sort(second);
			if (Arrays.equals(first, second))
				return true;
		}
		return false;
	}
}
