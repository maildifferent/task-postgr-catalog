// CREATE TABLE price (
//   gid INTEGER,
//   price_datefr DATE,
//   price_dateto DATE,
//   price INTEGER,
//   price_cur TEXT,
//   price_qty INTEGER,
//   price_uon TEXT,
//   PRIMARY KEY (gid, price_datefr)
// );

package catalog.storage.tables;

import catalog.storage.field.DbFieldDate;
import catalog.storage.field.DbFieldInteger;
import catalog.storage.field.DbFieldText;

public class Price {
  static public final DbFieldInteger gid = new DbFieldInteger(PriceTab.class, "gid");
  static public final DbFieldDate price_datefr = new DbFieldDate(PriceTab.class, "price_datefr");
  static public final DbFieldDate price_dateto = new DbFieldDate(PriceTab.class, "price_dateto");
  static public final DbFieldInteger price = new DbFieldInteger(PriceTab.class, "price");
  static public final DbFieldText price_cur = new DbFieldText(PriceTab.class, "price_cur");
  static public final DbFieldInteger price_qty = new DbFieldInteger(PriceTab.class, "price_qty");
  static public final DbFieldText price_uon = new DbFieldText(PriceTab.class, "price_uon");
}
