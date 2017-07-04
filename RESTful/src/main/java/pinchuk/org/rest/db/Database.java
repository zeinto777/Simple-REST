package pinchuk.org.rest.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Andrii Pinchuk on 27.02.2017.
 */
public class Database {
    private static final BasicDataSource dataSource = new BasicDataSource();

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mydb?useLegacyDatetimeCode=false" +
            "&serverTimezone=UTC" +
            "&allowMultiQueries=true" +
            "&rewriteBatchedStatements=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    static {
        dataSource.setDriverClassName(JDBC_DRIVER);
        dataSource.setUrl(JDBC_URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
    }

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
