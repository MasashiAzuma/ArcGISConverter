package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import net.miginfocom.swing.MigLayout;
import javax.swing.ScrollPaneConstants;

public class Properties extends JPanel implements JsonGUI {
	// private static JFrame frame;
	private JPanel panel;
	private String[] headers;

	// public static void main(String[] args) {
	// frame = new JFrame();
	// Properties property = new Properties();
	//
	// frame.getContentPane().add(property);
	//
	// frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	// frame.setSize(600,400);
	// frame.setResizable(true);
	// frame.setLocationRelativeTo(null);
	// frame.setVisible(true);
	// }

	public Properties() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblProperties = new JLabel("Properties");
		GridBagConstraints gbc_lblProperties = new GridBagConstraints();
		gbc_lblProperties.gridwidth = 2;
		gbc_lblProperties.anchor = GridBagConstraints.WEST;
		gbc_lblProperties.insets = new Insets(0, 0, 5, 5);
		gbc_lblProperties.gridx = 0;
		gbc_lblProperties.gridy = 0;

		Font labelFont = lblProperties.getFont();
		lblProperties.setFont(new Font(labelFont.getName(), Font.PLAIN, 30));

		add(lblProperties, gbc_lblProperties);

		JLabel lblPropertyKey = new JLabel("Property Key");
		GridBagConstraints gbc_lblPropertyKey = new GridBagConstraints();
		gbc_lblPropertyKey.insets = new Insets(0, 0, 5, 5);
		gbc_lblPropertyKey.gridx = 0;
		gbc_lblPropertyKey.gridy = 1;
		add(lblPropertyKey, gbc_lblPropertyKey);

		JButton btnAddProperty = new JButton("Add Property");
		GridBagConstraints gbc_btnAddProperty = new GridBagConstraints();
		gbc_btnAddProperty.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddProperty.gridx = 1;
		gbc_btnAddProperty.gridy = 1;
		add(btnAddProperty, gbc_btnAddProperty);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);

		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(new PropertyKey());

		// panel.add(propertyKey);

		btnAddProperty.addActionListener((ActionEvent e) -> {
			panel.add(new PropertyKey());
			if (headers != null) {
				JsonGUI g = (JsonGUI) panel.getComponent(panel.getComponentCount() - 1);
				g.insertCB(headers);
			}
			panel.getParent().validate();
			panel.getParent().repaint();
		});

	}

	@Override
	public void insertCB(Object[] headers) {

		this.headers = new String[headers.length];
		for (int i = 0; i < headers.length; i++) {
			this.headers[i] = headers[i].toString();
		}
		Container c = panel;
		for (int i = 0; i < c.getComponentCount(); i++) {
			JsonGUI g = (JsonGUI) c.getComponent(i);
			g.insertCB(headers);
		}
	}

	@Override
	public void deleteCB() {
		panel.removeAll();
		panel.add(new PropertyKey());
		headers = null;

	}

	@Override
	public String[] pullHeaders() {
		String[] propertyHeaders = new String[panel.getComponentCount()];
		for (int i = 0; i < propertyHeaders.length; i++) {
			propertyHeaders[i] = ((JsonGUI) panel.getComponent(i)).pullHeaders()[0];
		}
		return propertyHeaders;
	}
}
