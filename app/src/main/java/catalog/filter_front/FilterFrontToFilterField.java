package catalog.filter_front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catalog.filter_main.FilterField;
import catalog.storage.field.DbField;

public class FilterFrontToFilterField {
  public static Map<DbField<?>, FilterField<?>> convToMap(List<FilterFront<?, ?>> filterFronts) {
    if (filterFronts == null)
      return null;

    Map<DbField<?>, FilterField<?>> result = new HashMap<>();

    for (var filterFront : filterFronts) {
      var filterField = result.get(filterFront.dbField);
      if (filterField == null) {
        filterField = new FilterField<>(filterFront.dbField);
        result.put(filterField.dbField, filterField);
      }
      mergeFilterFrontIntoFilterField(filterField, filterFront);
    }

    return result;
  }

  private static <T> void mergeFilterFrontIntoFilterField(FilterField<T> filterField, FilterFront<?, ?> filterFront) {
    if (filterFront.dbField != filterField.dbField)
      throw new IllegalArgumentException();

    if (filterFront instanceof FilterFrontCheckEq casted) {
      filterField.eq(filterField.typeClass.cast(casted.optionValue));
    }

    else if (filterFront instanceof FilterFrontCheckIn casted) {
      for (var value : casted.optionValues) {
        filterField.inAdd(filterField.typeClass.cast(value));
      }
    }

    else if (filterFront instanceof FilterFrontInterval casted) {
      switch (casted.intervalType) {
        // @formatter:off
        case "gt" : { filterField.gt(filterField.typeClass.cast(casted.value));   break; }
        case "gte": { filterField.gte(filterField.typeClass.cast(casted.value));  break; }
        case "lt" : { filterField.lt(filterField.typeClass.cast(casted.value));   break; }
        case "lte": { filterField.lte(filterField.typeClass.cast(casted.value));  break; }
        default: throw new IllegalArgumentException();
        // @formatter:on
      }
    }

    else {
      throw new IllegalArgumentException();
    }
  }
}
