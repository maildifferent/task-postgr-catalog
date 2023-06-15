// INSERT INTO tagstab (creator, name, sortorder) VALUES ($1, $2, $3), ($4, $5, $6) RETURNING id, creator...
// SELECT id, creator FROM tagstab WHERE id=$1 AND creator IN ($2, $3, ...)
// SELECT product_id, p.name, (sum(s.units) * (p.price - p.cost)) AS profit
//    FROM products p 
//    LEFT JOIN sales s USING (product_id)
//    WHERE s.date > CURRENT_DATE - INTERVAL '4 weeks'
//    GROUP BY product_id, p.name, p.price, p.cost
//    HAVING sum(p.price * s.units) > 5000;
// SELECT a + b AS sum, c FROM table1 ORDER BY sum, c DESC NULLS LAST LIMIT 20 OFFSET 10
// SELECT * FROM (VALUES (1, 'one'), (2, 'two'), (3, 'three')) AS t (num,letter);
// UPDATE tagstab SET creator=$1 WHERE id=$2 AND ... RETURNING *;
// DELETE FROM tagstab WHERE id=$1 AND creator=$2 AND ... RETURNING *;

package catalog.storage;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import catalog.filter_main.Filter;
import catalog.filter_main.FilterToSql;
import catalog.storage.field.DbField;

public class QueryRead extends QueryAbstract {
  public List<DbField<?>> selectFields;
  public Class<?> table;
  public List<Joins> joinInner = new LinkedList<>();
  public List<Joins> joinLeft = new LinkedList<>();
  public List<Joins> joinRight = new LinkedList<>();
  public List<Filter> whereFilters;
  public List<DbField<?>> orderBy = new LinkedList<>();
  public Integer offset;
  public Integer limit;

  //////////////////////////////////////////////////////////////////////////////
  // SQL statements.
  //////////////////////////////////////////////////////////////////////////////
  public QueryRead select(DbField<?>... fields) {
    this.selectFields = new ArrayList<DbField<?>>(Arrays.asList(fields));
    return this;
  }

  public QueryRead from(Class<?> table) {
    this.table = table;
    return this;
  }

  public QueryRead joinInner(Class<?> table, DbField<?>... using) {
    this.joinInner.add(new Joins(table, new ArrayList<DbField<?>>(Arrays.asList(using))));
    return this;
  }

  public QueryRead joinLeft(Class<?> table, DbField<?>... using) {
    this.joinLeft.add(new Joins(table, new ArrayList<DbField<?>>(Arrays.asList(using))));
    return this;
  }

  public QueryRead joinRight(Class<?> table, DbField<?>... using) {
    this.joinRight.add(new Joins(table, new ArrayList<DbField<?>>(Arrays.asList(using))));
    return this;
  }

  public QueryRead where(Filter... filters) {
    this.whereFilters = new ArrayList<Filter>(Arrays.asList(filters));
    return this;
  }

  public QueryRead orderBy(DbField<?>... fields) {
    this.orderBy.addAll(Arrays.asList(fields));
    return this;
  }

  public QueryRead offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public QueryRead limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Main.
  //////////////////////////////////////////////////////////////////////////////
  @Override
  public String genSql() {
    String selectFields = this.selectFields.stream().map(field -> field.name).collect(Collectors.joining(", "));
    String fromTable = getTableName(this.table);
    String joinInner = genJoinSql(this.joinInner, " JOIN ");
    String joinLeft = genJoinSql(this.joinLeft, " LEFT JOIN ");
    String joinRight = genJoinSql(this.joinRight, " RIGHT JOIN ");
    String whereFilters = this.whereFilters == null ? null : FilterToSql.conv(this.whereFilters);
    String orderBy = genOrderBySql(this.orderBy);
    String offset = this.offset == null ? "" : (" OFFSET " + this.offset);
    String limit = this.limit == null ? "" : (" LIMIT " + this.limit);
        
    String result = "SELECT " + selectFields
        + " FROM " + fromTable
        + joinInner
        + joinLeft
        + joinRight
        + (whereFilters == null ? "" : (" WHERE " + whereFilters))
        + orderBy
        + offset
        + limit;
        
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util private.
  //////////////////////////////////////////////////////////////////////////////
  private String genJoinSql(List<Joins> joins, String prefix) {
    if (joins == null || joins.isEmpty())
      return "";
    String result = joins.stream()
        .map(join -> getTableName(join.table)
            + " USING ("
            + join.fields.stream().map(field -> field.name).collect(Collectors.joining(", "))
            + ")")
        .map(str -> (prefix + str))
        .collect(Collectors.joining(""));
    return result;
  }

  private String genOrderBySql(List<DbField<?>> fields) {
    if (fields == null || fields.isEmpty())
      return "";
    String result = "ORDER BY " + fields.stream()
        .map(field -> getTableName(field.table) + "." + field.name)
        .collect(Collectors.joining(", "));
    return result;
  }

  private String getTableName(Class<?> table) {
    String str;
    try {
      Field tableNameField = table.getDeclaredField("name");
      str = (String) tableNameField.get(null);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException();
    }
    return str;
  }

  private static class Joins {
    public final Class<?> table;
    public final List<DbField<?>> fields;

    public Joins(Class<?> table, List<DbField<?>> fields) {
      this.table = table;
      this.fields = fields;
    }
  }
}
