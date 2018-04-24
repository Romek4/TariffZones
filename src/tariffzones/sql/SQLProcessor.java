package tariffzones.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

import tariffzones.sql.interfaces.DatabaseConnectionParametres;

public class SQLProcessor {
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	public boolean connectDatabase(String dbUrl, String username, String password) {
		try {
//			Class.forName(DatabaseConnectionParametres.JDBC_DRIVER);
			connection = DriverManager.getConnection(dbUrl, username, password);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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
			e.printStackTrace();
		}
		
	}
	
	public void insert(String sqlStatement, Object[] values) {
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement);
			buildStatement(preparedStatement, values);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(String sqlStatement, Object[] values) {
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement);
			buildStatement(preparedStatement, values);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(String sqlStatement, Object[] values) {
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement);
			buildStatement(preparedStatement, values);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getResultSet() {
		return this.resultSet;
	}
}
