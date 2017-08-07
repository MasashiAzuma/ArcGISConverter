package GUI;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Container;

public class Data extends JPanel implements JsonGUI{
		private String[] headers;
		private JPanel panel;
		
//		private static JFrame frame;
		
//		public static void main(String[] args) {
//			frame = new JFrame();
//			Data data = new Data();
//			
//			frame.getContentPane().add(data);
//			
//			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//			frame.setSize(600,400);
//			frame.setResizable(true);
//			frame.setLocationRelativeTo(null);
//			frame.setVisible(true);
//		}
		
		public Data() {
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{0, 0, 0};
			gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
			gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
			setLayout(gridBagLayout);
			
			JLabel lblData = new JLabel("Data");
			GridBagConstraints gbc_lblData = new GridBagConstraints();
			gbc_lblData.gridwidth = 2;
			gbc_lblData.anchor = GridBagConstraints.WEST;
			gbc_lblData.insets = new Insets(0, 0, 5, 5);
			gbc_lblData.gridx = 0;
			gbc_lblData.gridy = 0;
			
			Font labelFont = lblData.getFont();
			lblData.setFont(new Font(labelFont.getName(), Font.PLAIN, 30));
			
			add(lblData, gbc_lblData);
			
			JLabel lblDataSets = new JLabel("Data Sets");
			GridBagConstraints gbc_lblDataSets = new GridBagConstraints();
			gbc_lblDataSets.insets = new Insets(0, 0, 5, 5);
			gbc_lblDataSets.gridx = 0;
			gbc_lblDataSets.gridy = 1;
			add(lblDataSets, gbc_lblDataSets);
			
			JButton btnAddSet = new JButton("Add Set");
			GridBagConstraints gbc_btnAddSet = new GridBagConstraints();
			gbc_btnAddSet.insets = new Insets(0, 0, 5, 0);
			gbc_btnAddSet.gridx = 1;
			gbc_btnAddSet.gridy = 1;
			add(btnAddSet, gbc_btnAddSet);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBorder(new EmptyBorder(0,0,0,0));
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.gridwidth = 2;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 2;
			add(scrollPane, gbc_scrollPane);
			
			panel = new JPanel();
			scrollPane.setViewportView(panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(new DataSet());
			
			btnAddSet.addActionListener((ActionEvent e)->{
				panel.add(new DataSet());
				if (headers != null) {
					JsonGUI g = (JsonGUI) panel.getComponent(panel.getComponentCount()-1);
					g.insertCB(headers);
				}
				panel.getParent().validate();
				panel.getParent().repaint();
			});
					
		}
		
		@Override
		public void insertCB(String[] headers) {
			this.headers = headers;
			Container c = panel;
			for (int i = 0; i < c.getComponentCount(); i++) {
				JsonGUI g = (JsonGUI) c.getComponent(i);
				g.insertCB(headers);
			}
			
		}
		@Override
		public void testing() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void deleteCB() {
			panel.removeAll();
			panel.add(new DataSet());
			headers = null;
			
		}

		@SuppressWarnings("unchecked")
		@Override
		public JSONObject pullJson(ArrayList<String[]> allData, int row) {
			JSONObject dataCollection = new JSONObject();
			
			for(int i = 0; i < panel.getComponentCount(); i++){
				JsonGUI g = (JsonGUI) panel.getComponent(i);
				JSONObject dkey = (JSONObject) g.pullJson(allData, row);
				Object[] keys = dkey.keySet().toArray();
				for (int j = 0; j < keys.length; j++) {
					dataCollection.put(keys[j], dkey.get(keys[j]));
				}
			}
			JSONObject dataSet = new JSONObject();
			dataSet.put("data", dataCollection);
			
			return dataSet;
		}
}
