package catalog.content_main.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import catalog.content_front.CatalogFilterFront;
import catalog.content_main.ResourceObj;
import catalog.filter_front.FilterFront;
import catalog.filter_front.FilterFrontToFilterField;
import catalog.filter_main.FilterField;
import catalog.report.ReportParams;
import catalog.report.ReportResult;
import catalog.storage.DbAccess;
import catalog.storage.QueryProcessor;
import catalog.storage.QueryRead;
import catalog.storage.QueryWith;
import catalog.storage.field.DbField;
import catalog.storage.tables.Image;
import catalog.storage.tables.ImageTab;
import catalog.storage.tables.Measurelot;
import catalog.storage.tables.MeasurelotTab;
import catalog.storage.tables.Price;
import catalog.storage.tables.PriceTab;
import catalog.storage.tables.Resource;
import catalog.storage.tables.ResourceTab;

public class ReportResource {
  public static ReportResult<ResourceObj> run(Map<String, FilterFront<?, ?>> filterFrontsById, ReportParams params) {
    ResourceQueryProcessor qProcessor = new ResourceQueryProcessor();
    List<FilterFront<?, ?>> filteredFilterFronts = excludeRedundantFilters(filterFrontsById);
    Map<DbField<?>, FilterField<?>> filterFieldByDbField = FilterFrontToFilterField.convToMap(filteredFilterFronts);

    Integer offset = null;
    Integer limit = null;
    if (params != null) {
      offset = params.offset;
      limit = params.limit;
    }

    if (filterFieldByDbField == null || filterFieldByDbField.isEmpty()) {
      QueryRead readQuery = genMainQuery();
      QueryWith withQuery = genWithQuery(readQuery, offset, limit);
      try {
        DbAccess.getData(withQuery, qProcessor);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IllegalArgumentException();
      }
    } else {
      for (var entry : filterFieldByDbField.entrySet()) {
        DbField<?> dbField = entry.getKey();
        FilterField<?> filterField = entry.getValue();
        QueryRead readQuery = genMainQuery();
        if (dbField.table != ResourceTab.class) {
          readQuery.joinInner(dbField.table, Resource.gid);
        }
        readQuery.where(filterField);

        Integer newOffset = null;
        Integer newLimit = null;
        if (offset != null) {
          newOffset = offset - qProcessor.totalCount;
          newOffset = newOffset > 0 ? newOffset : null;
        }
        if (limit != null) {
          newLimit = limit - qProcessor.resources.size();
          newLimit = newLimit > 0 ? newLimit : 0;
        }

        QueryWith withQuery = genWithQuery(readQuery, newOffset, newLimit);
        try {
          DbAccess.getData(withQuery, qProcessor);
        } catch (SQLException e) {
          e.printStackTrace();
          throw new IllegalArgumentException();
        }
      }
    }

    ReportResult<ResourceObj> result = new ReportResult<>();
    result.items = qProcessor.resources;
    result.totalCount = qProcessor.totalCount;
    return result;
  }

  public static ReportResult<ResourceObj> run(Map<String, FilterFront<?, ?>> filterFrontsById) {
    return run(filterFrontsById, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util. Private.
  //////////////////////////////////////////////////////////////////////////////
  private static List<FilterFront<?, ?>> excludeRedundantFilters(Map<String, FilterFront<?, ?>> filterFrontsById) {
    if (filterFrontsById == null)
      return null;

    List<FilterFront<?, ?>> result = new ArrayList<>(filterFrontsById.size());
    FilterFront<?, ?> resourceFilterFront = CatalogFilterFront.renderTree();

    for (var entry : filterFrontsById.entrySet()) {
      String key = entry.getKey();
      FilterFront<?, ?> filterFront = resourceFilterFront.idMap.get(key);
      FilterFront<?, ?> parent = filterFront.parent;
      boolean isRelevant = true;
      while (parent != null) {
        if (filterFrontsById.containsKey(parent.id)) {
          isRelevant = false;
          break;
        }
        parent = parent.parent;
      }
      if (isRelevant) {
        result.add(entry.getValue());
      }
    }

    return result;
  }

  private static QueryWith genWithQuery(QueryRead readQuery, Integer offset, Integer limit) {
    QueryWith withQuery = new QueryWith();
    withQuery.readQueries = new HashMap<>(1);
    withQuery.readQueries.put("cte", readQuery);

    withQuery.querySuffix = """
        SELECT * FROM (
          TABLE cte {{OFFSET}} {{LIMIT}}
        ) sub
        RIGHT JOIN (SELECT count(*) FROM cte) AS c(total_count) ON true;
        """;

    if (offset != null) {
      withQuery.querySuffix = withQuery.querySuffix.replace("{{OFFSET}}", "OFFSET " + offset);
    } else {
      withQuery.querySuffix = withQuery.querySuffix.replace("{{OFFSET}}", "");
    }

    if (limit != null) {
      withQuery.querySuffix = withQuery.querySuffix.replace("{{LIMIT}}", "LIMIT " + limit);
    } else {
      withQuery.querySuffix = withQuery.querySuffix.replace("{{LIMIT}}", "");
    }

    return withQuery;
  }

  private static QueryRead genMainQuery() {
    QueryRead result = new QueryRead()
        .select(
            Resource.gid, Resource.resource_name, Resource.resource_category,
            Image.image_src,
            Price.price_datefr, Price.price_dateto, Price.price, Price.price_cur, Price.price_qty, Price.price_uon,
            Measurelot.measurelot_weight_mainuon,
            Measurelot.measurelot_nweight_appr,
            Measurelot.measurelot_nweight_qty,
            Measurelot.measurelot_nweight_uon,
            Measurelot.measurelot_weight_appr,
            Measurelot.measurelot_weight_qty,
            Measurelot.measurelot_weight_uon)
        .from(ResourceTab.class)
        .joinLeft(ImageTab.class, Image.gid)
        .joinLeft(PriceTab.class, Price.gid)
        .joinLeft(MeasurelotTab.class, Measurelot.gid)
        .where(
            Resource.gid.gt(0).lt(1_000).or().gt(3_000).lt(9_000))
        .orderBy(Resource.gid);
    return result;
  }
}

class ResourceQueryProcessor extends QueryProcessor {
  public int totalCount = 0;
  public List<ResourceObj> resources = new LinkedList<>();

  @Override
  public void run(ResultSet rs) {
    boolean newRequestFlag = true;
    try {
      while (rs.next()) {
        Object gid = rs.getObject(Resource.gid.name);

        if (newRequestFlag) {
          newRequestFlag = false;
          Long longNum = rs.getLong("total_count");
          int curTotalCount = longNum.intValue();
          totalCount += curTotalCount;
          if (gid == null)
            break;
        }

        ResourceObj obj = new ResourceObj();
        obj.gid = (Integer) gid;
        obj.resource_name = rs.getString(Resource.resource_name.name);
        obj.resource_category = rs.getString(Resource.resource_category.name);

        obj.image_src = rs.getString(Image.image_src.name);

        obj.price_datefr = rs.getObject(Price.price_datefr.name, LocalDate.class);
        obj.price_dateto = rs.getObject(Price.price_dateto.name, LocalDate.class);
        obj.price = rs.getInt(Price.price.name);
        obj.price_cur = rs.getString(Price.price_cur.name);
        obj.price_qty = rs.getInt(Price.price_qty.name);
        obj.price_uon = rs.getString(Price.price_uon.name);

        obj.measurelot_weight_mainuon = rs.getString(Measurelot.measurelot_weight_mainuon.name);
        obj.measurelot_nweight_appr = rs.getBoolean(Measurelot.measurelot_nweight_appr.name);
        obj.measurelot_nweight_qty = rs.getInt(Measurelot.measurelot_nweight_qty.name);
        obj.measurelot_nweight_uon = rs.getString(Measurelot.measurelot_nweight_uon.name);
        obj.measurelot_weight_appr = rs.getBoolean(Measurelot.measurelot_weight_appr.name);
        obj.measurelot_weight_qty = rs.getInt(Measurelot.measurelot_weight_qty.name);
        obj.measurelot_weight_uon = rs.getString(Measurelot.measurelot_weight_uon.name);
        resources.add(obj);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException();
    }
  }
}
