package catalog.filter_main;

import java.util.Set;

class FilterExpression<OperandT> { // Implicit AND.
  OperandT eq;
  OperandT ne;
  Set<OperandT> in;
  Set<OperandT> nin;
  OperandT gt;
  OperandT gte;
  OperandT lt;
  OperandT lte;
}