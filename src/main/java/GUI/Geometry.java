package GUI;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class Geometry extends JPanel implements JsonGUI {
	// private static JFrame frame;
	private JComboBox<String> GeoCB;
	private JComboBox<String> lonCB;
	private JComboBox<String> latCB;

	// public static void main(String[] args) {
	// frame = new JFrame();
	// Geometry geometry = new Geometry();
	//
	// frame.getContentPane().add(geometry);
	//
	// frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	// frame.setSize(600,400);
	// frame.setResizable(true);
	// frame.setLocationRelativeTo(null);
	// frame.setVisible(true);
	// }

	public Geometry() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblGeometry = new JLabel("Geometry");
		GridBagConstraints gbc_lblGeometry = new GridBagConstraints();
		gbc_lblGeometry.gridwidth = 2;
		gbc_lblGeometry.anchor = GridBagConstraints.WEST;
		gbc_lblGeometry.insets = new Insets(0, 0, 5, 5);
		gbc_lblGeometry.gridx = 0;
		gbc_lblGeometry.gridy = 0;

		Font labelFont = lblGeometry.getFont();
		lblGeometry.setFont(new Font(labelFont.getName(), Font.PLAIN, 30));
		add(lblGeometry, gbc_lblGeometry);

		JLabel lblType = new JLabel("Type");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 1;
		add(lblType, gbc_lblType);

		JLabel lblLongitude = new JLabel("Longitude");
		GridBagConstraints gbc_lblLongitude = new GridBagConstraints();
		gbc_lblLongitude.insets = new Insets(0, 0, 5, 5);
		gbc_lblLongitude.gridx = 1;
		gbc_lblLongitude.gridy = 1;
		add(lblLongitude, gbc_lblLongitude);

		JLabel lblLatitude = new JLabel("Latitude");
		GridBagConstraints gbc_lblLatitude = new GridBagConstraints();
		gbc_lblLatitude.insets = new Insets(0, 0, 5, 0);
		gbc_lblLatitude.gridx = 2;
		gbc_lblLatitude.gridy = 1;
		add(lblLatitude, gbc_lblLatitude);

		String[] m = new String[] { "Point" };
		GeoCB = new JComboBox<String>(m);
		GridBagConstraints gbc_GeoCB = new GridBagConstraints();
		gbc_GeoCB.insets = new Insets(0, 0, 0, 5);
		gbc_GeoCB.fill = GridBagConstraints.HORIZONTAL;
		gbc_GeoCB.gridx = 0;
		gbc_GeoCB.gridy = 2;
		add(GeoCB, gbc_GeoCB);

		lonCB = new JComboBox<String>();
		GridBagConstraints gbc_LonCB = new GridBagConstraints();
		gbc_LonCB.insets = new Insets(0, 0, 0, 5);
		gbc_LonCB.fill = GridBagConstraints.HORIZONTAL;
		gbc_LonCB.gridx = 1;
		gbc_LonCB.gridy = 2;
		add(lonCB, gbc_LonCB);

		latCB = new JComboBox<String>();
		GridBagConstraints gbc_LatCB = new GridBagConstraints();
		gbc_LatCB.fill = GridBagConstraints.HORIZONTAL;
		gbc_LatCB.gridx = 2;
		gbc_LatCB.gridy = 2;
		add(latCB, gbc_LatCB);
	}

	@Override
	public void insertCB(Object[] headers) {
		for (int i = 0; i < headers.length; i++) {
			lonCB.addItem(headers[i].toString());
			latCB.addItem(headers[i].toString());
		}

	}

	@Override
	public void testing() {
		System.out.println("Geometry test");
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCB() {
		latCB.removeAllItems();
		lonCB.removeAllItems();
	}

	@Override
	public JSONObject pullJson(ArrayList<String[]> allData, int row) {
		JSONObject geo = new JSONObject();
		geo.put("type", GeoCB.getSelectedItem());

		JSONArray cor = new JSONArray();
		for (int i = 0; i < allData.get(0).length; i++) {
			if (latCB.getSelectedItem().equals(allData.get(0)[i])) {
				cor.add(allData.get(row)[i]);
			}
			if (lonCB.getSelectedItem().equals(allData.get(0)[i])) {
				cor.add(allData.get(row)[i]);
			}
		}
		geo.put("coordinates", cor);
		
		JSONObject geometry = new JSONObject();
		geometry.put("geometry", geo);

		return geometry;
	}
}
