// CREATE TABLE measurelot (
//   gid INTEGER PRIMARY KEY,
//   measurelot_weight_mainuon TEXT,
//   measurelot_nweight_appr BOOLEAN,
//   measurelot_nweight_qty INTEGER,
//   measurelot_nweight_uon TEXT,
//   measurelot_weight_appr BOOLEAN,
//   measurelot_weight_qty INTEGER,
//   measurelot_weight_uon TEXT
// );

package catalog.storage.tables;

import catalog.storage.field.DbFieldBoolean;
import catalog.storage.field.DbFieldInteger;
import catalog.storage.field.DbFieldText;

public class Measurelot {
  static public final DbFieldInteger gid = new DbFieldInteger(MeasurelotTab.class, "gid");
  static public final DbFieldText measurelot_weight_mainuon = new DbFieldText(MeasurelotTab.class, "measurelot_weight_mainuon");
  static public final DbFieldBoolean measurelot_nweight_appr = new DbFieldBoolean(MeasurelotTab.class, "measurelot_nweight_appr");
  static public final DbFieldInteger measurelot_nweight_qty = new DbFieldInteger(MeasurelotTab.class, "measurelot_nweight_qty");
  static public final DbFieldText measurelot_nweight_uon = new DbFieldText(MeasurelotTab.class, "measurelot_nweight_uon");
  static public final DbFieldBoolean measurelot_weight_appr = new DbFieldBoolean(MeasurelotTab.class, "measurelot_weight_appr");
  static public final DbFieldInteger measurelot_weight_qty = new DbFieldInteger(MeasurelotTab.class, "measurelot_weight_qty");
  static public final DbFieldText measurelot_weight_uon = new DbFieldText(MeasurelotTab.class, "measurelot_weight_uon");
}
