package catalog.storage;

import java.sql.ResultSet;

public abstract class QueryProcessor {
  public abstract void run(ResultSet rs);
}
