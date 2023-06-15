package catalog.content_main;

import java.time.LocalDate;

public class ResourceObj {
// Table "resource".
public int gid;
public String resource_name;
public String resource_category;
// Table "image".
public String image_src;
// Table "price".
public LocalDate price_datefr;
public LocalDate price_dateto;
public int price;
public String price_cur;
public int price_qty;
public String price_uon;
// Table "measurelot".
public String measurelot_weight_mainuon;
public boolean measurelot_nweight_appr;
public int measurelot_nweight_qty;
public String measurelot_nweight_uon;
public boolean measurelot_weight_appr;
public int measurelot_weight_qty;
public String measurelot_weight_uon;
}
