package GUI;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;

import java.awt.FlowLayout;

public class DataSet extends JPanel implements JsonGUI{
	private JTextField textField;
	private JPanel panelMain;
	private JPanel panel;
	private String[] headers;
	private JComboBox<String> timeKeyCB;

	/**
	 * Create the panel.
	 */
	public DataSet() {
		this.setMaximumSize(new Dimension(580,220));
		panelMain = this;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{157, 213, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblData = new JLabel("Data Set Name");
		GridBagConstraints gbc_lblData = new GridBagConstraints();
		gbc_lblData.insets = new Insets(0, 0, 5, 5);
		gbc_lblData.anchor = GridBagConstraints.EAST;
		gbc_lblData.gridx = 0;
		gbc_lblData.gridy = 0;
		add(lblData, gbc_lblData);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnDeleteSet = new JButton("Delete Set");
		GridBagConstraints gbc_btnDeleteSet = new GridBagConstraints();
		gbc_btnDeleteSet.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteSet.gridx = 2;
		gbc_btnDeleteSet.gridy = 0;
		add(btnDeleteSet, gbc_btnDeleteSet);
		btnDeleteSet.addActionListener((ActionEvent e)->{
			Container c = this.getParent();
			c.remove(this);
			c.validate();
			c.repaint();
		});
		
		JCheckBox chckbxTimeDependent = new JCheckBox("Time Dependent");
		GridBagConstraints gbc_chckbxTimeDependent = new GridBagConstraints();
		gbc_chckbxTimeDependent.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTimeDependent.gridx = 0;
		gbc_chckbxTimeDependent.gridy = 1;
		add(chckbxTimeDependent, gbc_chckbxTimeDependent);
		
		JLabel lblTimeKey = new JLabel("Time Key");
		GridBagConstraints gbc_lblTimeKey = new GridBagConstraints();
		gbc_lblTimeKey.anchor = GridBagConstraints.EAST;
		gbc_lblTimeKey.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeKey.gridx = 1;
		gbc_lblTimeKey.gridy = 1;
		//add(lblTimeKey, gbc_lblTimeKey);
		
		timeKeyCB = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 1;
		//add(comboBox, gbc_comboBox);
		
		JLabel lblDataKeysAnd = new JLabel("Data Keys and Type");
		GridBagConstraints gbc_lblDataKeysAnd = new GridBagConstraints();
		gbc_lblDataKeysAnd.insets = new Insets(0, 0, 5, 5);
		gbc_lblDataKeysAnd.gridx = 0;
		gbc_lblDataKeysAnd.gridy = 2;
		add(lblDataKeysAnd, gbc_lblDataKeysAnd);
		
		JButton btnAddData = new JButton("Add Data");
		GridBagConstraints gbc_btnAddData = new GridBagConstraints();
		gbc_btnAddData.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddData.gridx = 1;
		gbc_btnAddData.gridy = 2;
		add(btnAddData, gbc_btnAddData);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setPreferredSize(new Dimension(400,200));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		add(scrollPane, gbc_scrollPane);
		
		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel.add(new DataKey());
		
		btnAddData.addActionListener((ActionEvent e)->{
			panel.add(new DataKey());
			if (headers != null) {
				JsonGUI g = (JsonGUI) panel.getComponent(panel.getComponentCount()-1);
				g.insertCB(headers);
			}
			panel.getParent().getParent().validate();
			panel.getParent().getParent().repaint();
		});

		chckbxTimeDependent.addActionListener((ActionEvent e)->{
			if(chckbxTimeDependent.isSelected()) {
				panelMain.add(lblTimeKey, gbc_lblTimeKey);
				panelMain.add(timeKeyCB, gbc_comboBox);
				panelMain.validate();
				panelMain.repaint();
			}
			else {
				panelMain.remove(lblTimeKey);
				panelMain.remove(timeKeyCB);
				panelMain.validate();
				panelMain.repaint();
			}
		});
	}

	@Override
	public void insertCB(String[] headers) {
		this.headers = headers;
		Container c = panel;
		for(int i = 0; i < c.getComponentCount(); i++) {
			JsonGUI g = (JsonGUI) c.getComponent(i);
			g.insertCB(headers);
		}
		
		for(int i = 0; i < headers.length; i++) {
			timeKeyCB.addItem(headers[i]);
		}
	}

	@Override
	public void testing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCB() {
		panel.removeAll();
		panel.add(new DataKey());
		timeKeyCB.removeAllItems();
		headers = null;
		
	}

	@Override
	public JSONObject pullJson(ArrayList<String[]> allData, int row) {
		JSONObject dataSetCollection = new JSONObject();
		
		for(int i = 0; i < panel.getComponentCount(); i++){
			JsonGUI g = (JsonGUI) panel.getComponent(i);
			JSONObject dkey = (JSONObject) g.pullJson(allData, row);
			Object[] keys = dkey.keySet().toArray();
			for (int j = 0; j < keys.length; j++) {
				dataSetCollection.put(keys[j], dkey.get(keys[j]));
			}
		}
		for (int i = 0; i < allData.get(0).length; i++) {
			if (timeKeyCB.getSelectedItem().equals(allData.get(0)[i])) {
				dataSetCollection.put(timeKeyCB.getSelectedItem().toString() + " TIME", allData.get(row)[i]);
			}
		}
		JSONObject dataSet = new JSONObject();
		dataSet.put(textField.getText(), dataSetCollection);
		
		return dataSet;
	}

}
