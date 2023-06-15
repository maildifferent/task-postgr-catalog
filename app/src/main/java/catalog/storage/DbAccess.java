package catalog.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import catalog.other.EnvMap;

public class DbAccess {
  // private static String jdbcPassword = "12345678";
  // private static String jdbcUrl = "jdbc:postgresql://localhost:5432/eshop";
  // private static String jdbcUsername = "postgres";
  
  // private static String jdbcHostname = "dpg-chvcfjorddlbpl3ok4ig-a";
  // private static String jdbcPort = "5432";
  // private static String jdbcDatabase = "task_postgr_catalog_db";
  private static String jdbcUsername = "task_postgr_catalog_db_user";
  private static String jdbcPassword = "eZFROjUR2J3En9mPrLk7U5RBFz7TAoXC";
  // private static String jdbcUrlInt = "postgres://task_postgr_catalog_db_user:eZFROjUR2J3En9mPrLk7U5RBFz7TAoXC@dpg-chvcfjorddlbpl3ok4ig-a/task_postgr_catalog_db";
  // private static String jdbcUrlExt = "postgres://task_postgr_catalog_db_user:eZFROjUR2J3En9mPrLk7U5RBFz7TAoXC@dpg-chvcfjorddlbpl3ok4ig-a.oregon-postgres.render.com/task_postgr_catalog_db";
  private static String jdbcUrl = "jdbc:postgresql://dpg-chvcfjorddlbpl3ok4ig-a.oregon-postgres.render.com:5432/task_postgr_catalog_db";
  // private static String jdbcPSQLCommand = "PGPASSWORD=eZFROjUR2J3En9mPrLk7U5RBFz7TAoXC psql -h dpg-chvcfjorddlbpl3ok4ig-a.oregon-postgres.render.com -U task_postgr_catalog_db_user task_postgr_catalog_db";

  static {
    if (EnvMap.PRODUCTION_FLAG) {
      jdbcPassword = EnvMap.JDBC_DATABASE_PASSWORD;
      jdbcUrl = EnvMap.JDBC_DATABASE_URL;
      jdbcUsername = EnvMap.JDBC_DATABASE_USERNAME;
    }
  }

  public static void getData(QueryAbstract query, QueryProcessor qProc) throws SQLException {
    try (
        Connection con = getConnection();
        Statement stmt = con.createStatement();) {
      String queryStr = query.genSql();
      ResultSet rs = stmt.executeQuery(queryStr);
      qProc.run(rs);
    }
  }

  private static Connection getConnection() throws SQLException {
    Connection con = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
    return con;
  }
}
