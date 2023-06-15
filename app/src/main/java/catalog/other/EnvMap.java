package catalog.other;

import java.util.Map;

// https://docs.oracle.com/javase/tutorial/essential/environment/env.html
public class EnvMap {
  public static final Map<String, String> env = System.getenv();
  public static boolean PRODUCTION_FLAG = false;

  public static String JDBC_DATABASE_PASSWORD;
  public static String JDBC_DATABASE_URL;
  public static String JDBC_DATABASE_USERNAME;

  public static String PORT;

  static {
    if (env.containsKey("PRODUCTION_FLAG")) {
      PRODUCTION_FLAG = true;

      JDBC_DATABASE_PASSWORD = env.get("JDBC_DATABASE_PASSWORD");
      JDBC_DATABASE_URL = env.get("JDBC_DATABASE_URL");
      JDBC_DATABASE_USERNAME = env.get("JDBC_DATABASE_USERNAME");

      PORT = env.get("PORT");
    }
  }
}
