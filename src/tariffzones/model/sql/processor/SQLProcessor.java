package tariffzones.model.sql.processor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

import tariffzones.model.sql.interfaces.DatabaseConnectionParametres;

public class SQLProcessor {
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	public void connectDatabase(String username, String password) {
		try {
			Class.forName(DatabaseConnectionParametres.JDBC_DRIVER);
			connection = DriverManager.getConnection(DatabaseConnectionParametres.DB_URL, username, password);
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private void buildStatement(PreparedStatement preparedStatement, Object[] values) {
		if (values == null) {
			return;
		}
		Object value = null;
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (value != null) {
				try {
					if (value instanceof Integer) {
						preparedStatement.setInt(i, (int) value);
					}
					else if (value instanceof String) {
						preparedStatement.setString(i, (String) value);
					}
					else if (value instanceof Double) {
						preparedStatement.setDouble(i, (double) value);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void select(String sqlStatement, Object[] values) {
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement);
			buildStatement(preparedStatement, values);
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			System.err.println("Select error: " + e);
			e.printStackTrace();
		}
		
	}
	
	public void insert(String sqlStatement, Object[] values) {
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement);
			buildStatement(preparedStatement, values);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Insert error: " + e);
			e.printStackTrace();
		}
	}

	public void delete(String sqlStatement, Object[] values) {
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement);
			buildStatement(preparedStatement, values);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Delete error: " + e);
			e.printStackTrace();
		}
	}
	
	public ResultSet getResultSet() {
		return this.resultSet;
	}
}