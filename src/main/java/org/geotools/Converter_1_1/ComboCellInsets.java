package org.geotools.Converter_1_1;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.geotools.Converter_1_1.TableRenderDemo.MyTableModel;

public class ComboCellInsets {
	
	public JComponent makeUI(String[] headers) {
		String[] columnNames = { "Name", "Type", "Can be Null" };
		Object[][] data = new Object[headers.length][3];
		for (int i = 0; i < headers.length; i++)
		{
			data[i][0] = headers[i];
			data[i][1] = "String";
			data[i][2] = false;
		}
		
		
		//Object[][] data = { { "bbb","Int" ,  false}, { "aaa","Int" , true }};
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};
		JTable table = new JTable(model);
		table.setRowHeight(36);
		table.setAutoCreateRowSorter(true);
		
		TableColumn column = table.getColumnModel().getColumn(1);
		column.setCellRenderer(new ComboBoxCellRenderer());
		column.setCellEditor(new ComboBoxCellEditor());
		return new JScrollPane(table);
	}
	
	public JComponent submitBtn(final ArrayList<String[]> allData) {
		JButton submit = new JButton("submit");
		
		submit.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  TableModel model = mytable.getModel();
				  Object[][] data = new Object[model.getRowCount()][model.getColumnCount()];
				  for (int i = 0; i < model.getRowCount(); i++)
					  for(int j = 0; j < model.getColumnCount(); j++)
						  data[i][j] = model.getValueAt(i, j);
				  
				  PostgreSQL interfaces = new PostgreSQL();
				  interfaces.createDB(data);
				  interfaces.insertDB(allData);
				  
				  String something = (String) mytable.getModel().getValueAt(0, 1);
				  //System.out.println(something);
				  System.exit(0); 
			}} );
		return submit;
	}

	//public static void main(String[] args) {
	//	EventQueue.invokeLater(new Runnable() {
	//		@Override
	//		public void run() {
	//			createAndShowGUI();
	//		}
	//	});
	//}

	public static JTable mytable;
	
	public static void createAndShowGUI(String[] headers, ArrayList<String[]> allData) {
		JFrame f = new JFrame();
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JScrollPane pane = (JScrollPane) new ComboCellInsets().makeUI(headers);
		
		JViewport viewport = pane.getViewport(); 
		mytable = (JTable)viewport.getView();
		
		mainPanel.add(pane, BorderLayout.PAGE_START);
		mainPanel.add(new ComboCellInsets().submitBtn(allData), BorderLayout.PAGE_END);
		
		mainPanel.getComponent(1);
		
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.getContentPane().add(mainPanel);
		//f.add(submitBtn());
		f.setSize(500, 470);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}

//************************************************
class ComboBoxPanel extends JPanel {
	private String[] m = new String[] { "Int", "String", "Double", "ID" };
	protected JComboBox<String> comboBox = new JComboBox<String>(m) {
		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			return new Dimension(100, d.height);
		}
	};

	public ComboBoxPanel() {
		super();
		setOpaque(true);		
		comboBox.setEditable(true);
		add(comboBox);
	}
}

//************************************************
class ComboBoxCellRenderer extends ComboBoxPanel implements TableCellRenderer {
	public ComboBoxCellRenderer() {
		super();
		setName("Table.cellRenderer");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		if (value != null) {
			comboBox.setSelectedItem(value);
		}
		return this;
	}
}

//************************************************
class ComboBoxCellEditor extends ComboBoxPanel implements TableCellEditor {
	public ComboBoxCellEditor() {
		super();
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.setBackground(table.getSelectionBackground());
		comboBox.setSelectedItem(value);
		return this;
	}

	// Copid from DefaultCellEditor.EditorDelegate
	@Override
	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			MouseEvent e = (MouseEvent) anEvent;
			return e.getID() != MouseEvent.MOUSE_DRAGGED;
		}
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		if (comboBox.isEditable()) {
			comboBox.actionPerformed(new ActionEvent(this, 0, ""));
		}
		fireEditingStopped();
		return true;
	}

	// Copid from AbstractCellEditor
	// protected EventListenerList listenerList = new EventListenerList();
	transient protected ChangeEvent changeEvent = null;

	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	@Override
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}

	public CellEditorListener[] getCellEditorListeners() {
		return listenerList.getListeners(CellEditorListener.class);
	}

	protected void fireEditingStopped() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
			}
		}
	}

	protected void fireEditingCanceled() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
			}
		}
	}
}