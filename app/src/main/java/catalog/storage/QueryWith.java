package catalog.storage;

import java.util.ArrayList;
import java.util.Map;

/**
 * https://www.postgresql.org/docs/current/queries-with.html
 * https://stackoverflow.com/questions/28888375/run-a-query-with-a-limit-offset-and-also-get-the-total-number-of-rows
 */
public class QueryWith extends QueryAbstract {
  public Map<String, QueryRead> readQueries;
  public String querySuffix = "";

  @Override
  public String genSql() {
    if (readQueries == null || readQueries.isEmpty())
      return "";

    ArrayList<String> strings = new ArrayList<>(readQueries.size());
    for (var entry : readQueries.entrySet()) {
      String cte = entry.getKey();
      String readQuerySql = entry.getValue().genSql();
      strings.add(cte + " AS (" + readQuerySql + ")");
    }

    String result = "WITH " + String.join(", ", strings);
    result += querySuffix;
    return result;
  }
}
