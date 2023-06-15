package catalog.filter_front;

import catalog.storage.field.DbField;

public class FilterFrontInterval<ValT> extends FilterFront<ValT, FilterFrontInterval<ValT>> {
  public final String intervalType; // Gte/lte/ge/le.
  public ValT value;

  public FilterFrontInterval(DbField<ValT> dbField, String intervalType) {
    super(FilterFrontInterval.class, dbField);
    switch (intervalType) {
      // @formatter:off
      case "gte": break;
      case "lte": break;
      case "gt" : break;
      case "lt" : break;
      default: throw new IllegalArgumentException();
      // @formatter:on
    }
    this.intervalType = intervalType;
  }

  public FilterFrontInterval<ValT> value(ValT value) {
    this.value = value;
    return this;
  }

  public FilterFrontInterval<ValT> valueFromObj(Object value) {
    if (typeClass == Integer.class) {
      this.value = this.typeClass.cast(Integer.valueOf(value.toString()));
    } else if (typeClass == String.class) {
      this.value = this.typeClass.cast(value.toString());
    } else {
      throw new IllegalArgumentException();
    }
    return this;
  }
}
