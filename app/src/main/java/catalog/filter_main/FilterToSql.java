
// http://www.jooq.org/doc/3.19/manual/getting-started/use-cases/jooq-as-a-sql-builder-without-codegeneration/
// https://stackoverflow.com/questions/29428231/jooq-vs-hibernate
// https://blog.jooq.org/jooq-vs-hibernate-when-to-choose-which/

// ... Done:
// (
//   (id > 2 AND id < 5) OR (id > 12 AND id < 15)
// ) AND (name = sweets*)
// Implementation: eq, gt, or returns for example FilterFieldInteger.
// Product.id.gt(2).lt(5).or().gt(12).lt(15), Product.name.eq(sweets*)
// ... Not done:
// (id > 10 AND name = sweets*) OR (id > 20 AND name = coffee*)
// Implementation:
// FilterOr(
//   FilterAnd(Product.id.gt(10), Product.name.eq(sweets*)),
//   FilterAnd(Product.id.gt(20), Product.name.eq(coffee*))
// )
// ... Not done:
// (id > 10) AND (name = sweets*)
// Implementation:
// FilterAnd(Product.id.gt(10), Product.name.eq(sweets*))

package catalog.filter_main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// SQL from filter generator.
public class FilterToSql {
  public static String conv(List<Filter> filters) { // Implicit AND.
    var result = new ArrayList<String>(filters.size());

    // Currently only list of FilterField-s is implemented:
    for (var obj : filters) {
      if (obj instanceof FilterField<?> casted) {
        ArrayList<String> filterExpressionSqls;
        filterExpressionSqls = genFromFilterField(casted);
        String str = filterExpressionSqls.stream().collect(Collectors.joining(" OR "));
        result.add("(" + str + ")");
      } else {
        throw new IllegalArgumentException();
      }
    }

    String str = result.stream().collect(Collectors.joining(" AND "));
    return str;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util private.
  //////////////////////////////////////////////////////////////////////////////
  private static <OperandT> ArrayList<String> genFromFilterField(FilterField<OperandT> filterField) {
    String name = filterField.dbField.name;
    var result = new ArrayList<String>(filterField.filters.size());

    for (var filter : filterField.filters) {
      var list = new ArrayList<String>(8);
      if (filter.eq != null)
        list.add(name + " = " + filter.eq.toString());
      if (filter.ne != null)
        list.add(name + " != " + filter.ne.toString());
      if (filter.in != null) {
        String items = filter.in.stream().map(item -> item instanceof String ? ("'" + item + "'") : item.toString())
            .collect(Collectors.joining(", "));
        list.add(name + " IN (" + items + ") ");
      }
      if (filter.nin != null) {
        String items = filter.nin.stream().map(item -> item instanceof String ? ("'" + item + "'") : item.toString())
            .collect(Collectors.joining(", "));
        list.add(name + " NIN (" + items + ") ");
      }
      if (filter.gt != null)
        list.add(name + " > " + filter.gt.toString());
      if (filter.gte != null)
        list.add(name + " >= " + filter.gte.toString());
      if (filter.lt != null)
        list.add(name + " < " + filter.lt.toString());
      if (filter.lte != null)
        list.add(name + " <= " + filter.lte.toString());
      String str = list.stream().collect(Collectors.joining(" AND "));
      result.add("(" + str + ")");
    }

    if (result.isEmpty())
      throw new IllegalArgumentException();
    return result;
  }
}