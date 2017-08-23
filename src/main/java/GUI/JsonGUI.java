package GUI;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public interface JsonGUI{
	abstract public void insertCB(Object[] objects);
	
	abstract public void deleteCB();
	
	abstract public String[] pullHeaders();
	
}
