package org.geotools.Converter_1_1;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class PostgreSQL {
	Object[][] data;

	public PostgreSQL() {
	}

	public void createDB(Object[][] data_) {
		data = data_;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TestDB", "postgres", "Wyc12345");
			System.out.println("Opened database successfully");

			String command = "CREATE TABLE TESTING(";
			for (int i = 0; i < data.length; i++) {
				command += (String) data[i][0] + "  ";
				String type = (String) data[i][1];

				if (type.equalsIgnoreCase("String")) {
					command += "TEXT  ";
				} else if (type.equalsIgnoreCase("Int")) {
					command += "INT  ";
				} else if (type.equalsIgnoreCase("ID")) {
					command += "TEXT  ";
				} else if (type.equalsIgnoreCase("Double")) {
					command += "REAL  ";
				} else {
					command += "STRING  ";
				}

				Boolean ifNull = (Boolean) data[i][2];
				if (!ifNull && i == data.length - 1) {
					command += "NOT NULL";
				} else if (!ifNull) {
					command += "NOT NULL,";
				}
			}
			command += ")";

			stmt = c.createStatement();
			stmt.executeUpdate(command);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	public void insertDB(ArrayList<String[]> allData) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TestDB", "postgres", "Wyc12345");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			for (int j = 0; j < allData.size(); j++) {
				String command = "INSERT INTO TESTING(";

				for (int i = 0; i < data.length; i++) {
					command += data[i][0];
					if (i != data.length - 1) {
						command += ",";
					}
				}

				command += ") VALUES(";

				for (int i = 0; i < data.length; i++) {
					String type = (String) data[i][1];
					if (type.equalsIgnoreCase("String")) {
						command += "'" + allData.get(j)[i] + "'";
						if (i != data.length - 1) {
							command += ",";
						}
					} else {
						command += allData.get(j)[i];
						if (i != data.length - 1) {
							command += ",";
						}
					}
				}

				command += ");";
				stmt.executeUpdate(command);
			}
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public void viewDB() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TestDB", "postgres", "Wyc12345");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM TESTING;");
			while (rs.next()) {
				Double lat = rs.getDouble("LAT");
				Double lon = rs.getDouble("LON");
				String city = rs.getString("city");
				int number = rs.getInt("number");
				System.out.println("LAT= " + lat);
				System.out.println("LONG = " + lon);
				System.out.println("CITY = " + city);
				System.out.println("NUMBER = " + number);
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully");
	}
}