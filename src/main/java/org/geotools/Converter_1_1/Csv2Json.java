package org.geotools.Converter_1_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Note: to keep things simple in the code below the input file should not have
 * additional spaces or tabs between fields.
 */
public class Csv2Json {

	public static void main(String[] args) throws Exception {
		final String DELIMITER = ",";
		
		// Set cross-platform look & feel for compatability
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

		// filter file chooser
		String[] files = { "csv", "txt" };
		File file = JFileDataStoreChooser.showOpenFile(files, null);
		if (file == null) {
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			/* First line of the data file is the header */
			String line = reader.readLine();
			String[] headers = line.split(DELIMITER);
			for (int i = 0; i < headers.length; i++)
				headers[i] = headers[i].trim();
			
			ArrayList<String[]> allData = new ArrayList<String[]>();
			
			for (line = reader.readLine(); line != null; line = reader.readLine()) {
				if (line.trim().length() > 0) { // skip blank lines
					String tokens[] = line.split("\\,");
					for (int i = 0; i < tokens.length; i++) {
						tokens[i] = tokens[i].trim();
					}
					allData.add(tokens);
				}
			}
			
			ComboCellInsets combo = new ComboCellInsets();
			ComboCellInsets.createAndShowGUI(headers, allData);
		}

	}

}
