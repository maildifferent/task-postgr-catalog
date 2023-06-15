package catalog.filter_front;

import java.util.Set;

import catalog.storage.field.DbField;

public class FilterFrontCheckIn<ValT> extends FilterFrontCheck<ValT> {
  public final Set<ValT> optionValues;

  public FilterFrontCheckIn(DbField<ValT> dbField, Set<ValT> optionValues) {
    super(dbField);
    this.optionValues = optionValues;
  }
}