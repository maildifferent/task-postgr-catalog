package catalog.filter_main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import catalog.storage.field.DbField;

// Filter field = field data (name, type...) + filter expression.
public class FilterField<OperandT> extends Filter {
  public final Class<OperandT> typeClass;
  public final DbField<OperandT> dbField;
  public final List<FilterExpression<OperandT>> filters = new ArrayList<>( // Implicit OR.
      Arrays.asList(new FilterExpression<OperandT>()));

  public FilterField(DbField<OperandT> dbField) {
    this.typeClass = dbField.typeClass;
    this.dbField = dbField;
  }

  public FilterField<OperandT> or() {
    filters.add(new FilterExpression<OperandT>());
    return this;
  };

  // Comparison methods (eq, ne, in, nin, gt, gte, lt, lte).
  public FilterField<OperandT> eq(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.eq = filter.eq == null ? val : notNullErr(val);
    return this;
  }

  public FilterField<OperandT> ne(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.ne = filter.ne == null ? val : notNullErr(val);
    return this;
  }

  public FilterField<OperandT> in(Set<OperandT> val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.in = filter.in == null ? val : notNullErr(val);
    return this;
  }

  public FilterField<OperandT> in(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    if (filter.in != null)
      notNullErr(val);
    filter.in = new HashSet<>(Arrays.asList(val));
    return this;
  }

  public FilterField<OperandT> inAdd(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    if (filter.in == null) {
      filter.in = new HashSet<OperandT>();
    }
    filter.in.add(val);
    return this;
  }

  public FilterField<OperandT> inAdd(Set<OperandT> val) {
    var filter = this.filters.get(filters.size() - 1);
    if (filter.in == null) {
      filter.in = val;
    } else {
      filter.in.addAll(val);
    }
    return this;
  }

  public FilterField<OperandT> nin(Set<OperandT> val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.nin = filter.nin == null ? val : notNullErr(val);
    return this;
  }

  public FilterField<OperandT> nin(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    if (filter.nin != null)
      notNullErr(val);
    filter.nin = new HashSet<>(Arrays.asList(val));
    return this;
  }

  public FilterField<OperandT> ninAdd(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    if (filter.nin == null) {
      filter.nin = new HashSet<OperandT>();
    }
    filter.nin.add(val);
    return this;
  }

  public FilterField<OperandT> ninAdd(Set<OperandT> val) {
    var filter = this.filters.get(filters.size() - 1);
    if (filter.nin == null) {
      filter.nin = val;
    } else {
      filter.nin.addAll(val);
    }
    return this;
  }

  public FilterField<OperandT> gt(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.gt = filter.gt == null ? val : notNullErr(val);
    return this;
  }

  public FilterField<OperandT> gte(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.gte = filter.gte == null ? val : notNullErr(val);
    return this;
  }

  public FilterField<OperandT> lt(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.lt = filter.lt == null ? val : notNullErr(val);
    return this;
  }

  public FilterField<OperandT> lte(OperandT val) {
    var filter = this.filters.get(filters.size() - 1);
    filter.lte = filter.lte == null ? val : notNullErr(val);
    return this;
  }

  private <T> T notNullErr(T val) {
    throw new IllegalArgumentException();
  }
}