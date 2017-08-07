package GUI;

import javax.swing.JPanel;

import org.json.simple.JSONObject;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class PropertyKey extends JPanel implements JsonGUI, JsonProp {
	private JComboBox<String> propertyKey;
	private JComboBox<String> propertyType;

	public PropertyKey() {
		this.setPreferredSize(new Dimension(500, 30));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		propertyKey = new JComboBox<String>();
		GridBagConstraints gbc_propertyKey = new GridBagConstraints();
		gbc_propertyKey.insets = new Insets(0, 0, 0, 5);
		gbc_propertyKey.fill = GridBagConstraints.HORIZONTAL;
		gbc_propertyKey.gridx = 0;
		gbc_propertyKey.gridy = 0;
		add(propertyKey, gbc_propertyKey);

		String[] m = new String[] { "Matching ID", "Not Matching ID" };
		propertyType = new JComboBox(m);
		GridBagConstraints gbc_propertyType = new GridBagConstraints();
		gbc_propertyType.insets = new Insets(0, 0, 0, 5);
		gbc_propertyType.fill = GridBagConstraints.HORIZONTAL;
		gbc_propertyType.gridx = 1;
		gbc_propertyType.gridy = 0;
		add(propertyType, gbc_propertyType);

		JButton btnDelete = new JButton("Delete");
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 0;
		add(btnDelete, gbc_btnDelete);

		btnDelete.addActionListener((ActionEvent E) -> {
			Container c = this.getParent();
			c.remove(this);
			c.validate();
			c.repaint();
		});
	}

	@Override
	public void insertCB(String[] headers) {
		for (int i = 0; i < headers.length; i++) {
			propertyKey.addItem(headers[i]);
		}
	}

	@Override
	public void testing() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCB() {
		propertyKey.removeAllItems();

	}

	@Override
	public JSONObject pullJson(ArrayList<String[]> allData, int row) {
		JSONObject prop = new JSONObject();

		for (int i = 0; i < allData.get(row).length; i++) {
			if (propertyKey.getSelectedItem().equals(allData.get(0)[i])) {
				prop.put(propertyKey.getSelectedItem().toString(), allData.get(row)[i]);
			}
		}

		return prop;
	}

	@Override
	public JSONObject pullMatching(ArrayList<String[]> allData, int row) {
		JSONObject prop = new JSONObject();

		for (int i = 0; i < allData.get(row).length; i++) {
			if (propertyKey.getSelectedItem().equals(allData.get(0)[i])) {
				if (propertyType.getSelectedItem().equals("Matching ID")) {
					prop.put(propertyKey.getSelectedItem().toString(), allData.get(row)[i]);
				}
			}
		}

		return prop;
	}
}
