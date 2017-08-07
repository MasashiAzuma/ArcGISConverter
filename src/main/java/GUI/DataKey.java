package GUI;

import javax.swing.JPanel;

import org.json.simple.JSONObject;

import java.awt.GridBagLayout;
import javax.swing.JComboBox;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;

public class DataKey extends JPanel implements JsonGUI {
	private JComboBox<String> keyCB;
	private JComboBox<String> typeCB;

	/**
	 * Create the panel.
	 */
	public DataKey() {
		this.setPreferredSize(new Dimension(150, 100)); 

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		keyCB = new JComboBox<String>();
		GridBagConstraints gbc_keyCB = new GridBagConstraints();
		gbc_keyCB.insets = new Insets(0, 0, 5, 0);
		gbc_keyCB.fill = GridBagConstraints.HORIZONTAL;
		gbc_keyCB.gridx = 0;
		gbc_keyCB.gridy = 0;
		add(keyCB, gbc_keyCB);

		String[] m = new String[] { "String", "Unit", "Desciption" };
		typeCB = new JComboBox<String>(m);
		GridBagConstraints gbc_typeCB = new GridBagConstraints();
		gbc_typeCB.insets = new Insets(0, 0, 5, 0);
		gbc_typeCB.fill = GridBagConstraints.HORIZONTAL;
		gbc_typeCB.gridx = 0;
		gbc_typeCB.gridy = 1;
		add(typeCB, gbc_typeCB);

		JButton btnNewButton = new JButton("Delete");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		btnNewButton.addActionListener((ActionEvent e) -> {
			Container c = this.getParent();
			c.remove(this);
			c.validate();
			c.repaint();
		});
		add(btnNewButton, gbc_btnNewButton);

	}

	@Override
	public void insertCB(String[] headers) {
		for (int i = 0; i < headers.length; i++) {
			keyCB.addItem(headers[i]);
		}

	}

	@Override
	public void testing() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCB() {
		keyCB.removeAllItems();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject pullJson(ArrayList<String[]> allData, int row) {
		JSONObject dataKey = new JSONObject();
		for (int i = 0; i < allData.get(0).length; i++) {
			if (keyCB.getSelectedItem().equals(allData.get(0)[i])) {
				dataKey.put(keyCB.getSelectedItem().toString(), allData.get(row)[i]);
			}
		}
		return dataKey;
	}

}
