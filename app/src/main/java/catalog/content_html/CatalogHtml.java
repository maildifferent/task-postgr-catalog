package catalog.content_html;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import catalog.content_front.CatalogFilterFront;
import catalog.content_html.filters.FilterHtmlBrewDrink;
import catalog.content_html.filters.FilterHtmlCheese;
import catalog.content_main.ResourceObj;
import catalog.content_main.reports.ReportResource;
import catalog.filter_front.FilterFront;
import catalog.html.HtmlDocument;
import catalog.html.HtmlState;
import catalog.html.HtmlStringFromElemTree;
import catalog.html.elements.HtmlDetailsElement;
import catalog.html.elements.HtmlDivElement;
import catalog.html.elements.HtmlLIElement;
import catalog.html.elements.HtmlUListElement;
import catalog.other.JsonParser;
import catalog.other.JsonStringify;
import catalog.other.UtilFile;
import catalog.other.UtilUrl;
import catalog.report.ReportParams;
import catalog.report.ReportResult;

public class CatalogHtml {
  private static final int limit = 8;
  private static final String templateCatalog = UtilFile.readToString("./app/src/main/html/catalog.html");
  private static final Set<String> sections = new HashSet<>(Arrays.asList("filter", "items", "pages"));

  public static CatalogHtmlQueryParams parseQueryParams(Map<String, String> queryParams) {
    CatalogHtmlQueryParams result = new CatalogHtmlQueryParams();

    for (var entry : queryParams.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      // http://localhost:8000/catalog?filterState={"open":["cheese_details"],"checked":["cheese_blue"],"value":[]}
      if (key.equals("filterState")) {
        result.filterState = HtmlState.createFromJson(value);
      }

      // http://localhost:8000/catalog?page=5
      else if (key.equals("page")) {
        String pageQueryParam = queryParams.get("page");
        result.page = Integer.parseInt(pageQueryParam);
      }

      // http://localhost:8000/catalog?sections=["filter", "items", "pages"]
      else if (key.equals("sections")) {
        result.sections = new HashSet<>(3);
        Object parsedQueryParam = JsonParser.parse(value);
        if (parsedQueryParam instanceof List querySections) {
          for (Object item : querySections) {
            if (item instanceof String section) {
              if (!sections.contains(section)) {
                throw new IllegalArgumentException();
              }
              result.sections.add(section);
            } else {
              throw new IllegalArgumentException();
            }
          }
        } else {
          throw new IllegalArgumentException();
        }
      }

      else {
        throw new IllegalArgumentException();
      }
    }

    return result;
  }

  public static CatalogSectionsHtml renderNeededSections(CatalogHtmlQueryParams qParams) {
    int page = qParams.page;
    HtmlState filterState = qParams.filterState;
    Set<String> requestedSections = qParams.sections;

    CatalogSectionsHtml result = new CatalogSectionsHtml();

    // http://localhost:8000/catalog?filterState={"open":["cheese_details"],"checked":["cheese_blue"],"value":[]}
    Map<String, FilterFront<?, ?>> filterFrontsById = CatalogFilterFront.genFromHtmlState(filterState);

    // http://localhost:8000/catalog?page=5
    if (page < 1)
      throw new IllegalArgumentException();

    // http://localhost:8000/catalog?sections=["filter", "items", "pages"]
    if (requestedSections != null && requestedSections.isEmpty())
      throw new IllegalArgumentException();

    if (requestedSections == null || requestedSections.contains("filter")) {
      result.filter = CatalogHtml.renderFilterContainer(filterState);
    }

    if (requestedSections == null || requestedSections.contains("items") || requestedSections.contains("pages")) {
      ReportResult<ResourceObj> reportResult = ReportResource.run(filterFrontsById,
          new ReportParams().limit(limit).offset(((page - 1) * limit)));

      if (requestedSections == null || requestedSections.contains("items")) {
        result.items = CatalogHtml.renderItemsContainer(reportResult.items);
      }

      if (requestedSections == null || requestedSections.contains("pages")) {
        result.pages = CatalogHtml.renderPagesContainer(page, reportResult.totalCount, filterState);
      }
    }

    return result;
  }

  public static String renderAllInTemplate(CatalogHtmlQueryParams qParams) {
    CatalogSectionsHtml sections = renderNeededSections(qParams);

    String catalogFilterContainerHtml = HtmlStringFromElemTree.conv(sections.filter);
    String result = templateCatalog.replace("{{catalog_filter_container}}", catalogFilterContainerHtml);

    String catalogItemsContainerHtml = HtmlStringFromElemTree.conv(sections.items);
    result = result.replace("{{catalog_items_container}}", catalogItemsContainerHtml);

    String catalogPagesContainerHtml = HtmlStringFromElemTree.conv(sections.pages);
    result = result.replace("{{catalog_pages_container}}", catalogPagesContainerHtml);

    return result;
  }

  private static HtmlDivElement renderItemsContainer(List<ResourceObj> resources) {
    HtmlUListElement catalogItemsListUL = HtmlDocument.createElement(HtmlUListElement.class)
        .addToClassList(ContentHtmlClasses.catalog_items_list);

    for (var resource : resources) {
      HtmlDivElement itemDiv = CatalogItemHtml.render(resource);
      HtmlLIElement itemLI = HtmlDocument.createElement(HtmlLIElement.class)
          .addToClassList(ContentHtmlClasses.catalog_item_card);
      itemLI.addLastChild(itemDiv);
      catalogItemsListUL.addLastChild(itemLI);
    }

    HtmlDivElement catalogItemsContainerDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.catalog_items_container)
        .addLastChild(catalogItemsListUL);
    return catalogItemsContainerDiv;
  }

  private static HtmlDivElement renderPagesContainer(int page, Integer totalCount, HtmlState filterState) {
    HtmlUListElement catalogPagesListUL = HtmlDocument.createElement(HtmlUListElement.class)
        .addToClassList(ContentHtmlClasses.catalog_pages_list);

    int totalPages = totalCount / limit + 1;
    int firstPage = Math.max(page - 5, 1);
    int lastPage = Math.min(firstPage + 9, totalPages);

    String url = "/catalog";
    List<String> queryParams = new LinkedList<>();

    if (filterState != null) {
      String filterStateStr = JsonStringify.stringify(filterState.toMap());
      queryParams.add("filterState=" + UtilUrl.encode(filterStateStr));
    }

    queryParams.add("page={{link_param_page}}");
    url = url + "?" + String.join("&", queryParams);

    for (int i = firstPage; i <= lastPage; i++) {
      HtmlLIElement pageLi = HtmlDocument.createElement(HtmlLIElement.class)
          .addToClassList(ContentHtmlClasses.catalog_page_link);

      pageLi.innerHTML = """
          <a class=\"catalog_page_link\" href=\"{{catalog_page_href}}\">{{catalog_page_text}}</a>
          """;

      pageLi.innerHTML = pageLi.innerHTML.replace("{{catalog_page_text}}", Integer.toString(i));
      pageLi.innerHTML = pageLi.innerHTML.replace("{{catalog_page_href}}", url);
      pageLi.innerHTML = pageLi.innerHTML.replace("{{link_param_page}}", Integer.toString(i));

      if (i == page) {
        pageLi.innerHTML = pageLi.innerHTML
            .replace("catalog_page_link", "catalog_page_link catalog_page_active");
      }

      catalogPagesListUL.addLastChild(pageLi);
    }

    HtmlDivElement catalogPagesContainerDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.catalog_pages_container)
        .addLastChild(catalogPagesListUL);
    return catalogPagesContainerDiv;
  }

  private static HtmlDivElement renderFilterContainer(HtmlState filterState) {
    HtmlDivElement catalogFilterContainerDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.catalog_filter_container)
        // .onchange("handleCatalogFilterChange(event, this)")
        .innerHTML("<div class=\"catalog_filter_header\">Products</div>");

    HtmlDetailsElement brewDrinkSection = FilterHtmlBrewDrink.render();
    HtmlDetailsElement cheeseSection = FilterHtmlCheese.render();

    HtmlUListElement ul = HtmlDocument.createElement(HtmlUListElement.class)
        .addToClassList(ContentHtmlClasses.catalog_filter_list)
        .addLastChild(HtmlDocument.createElement(HtmlLIElement.class).addLastChild(brewDrinkSection))
        .addLastChild(HtmlDocument.createElement(HtmlLIElement.class).addLastChild(cheeseSection));
    catalogFilterContainerDiv.addLastChild(ul);

    if (filterState != null) {
      filterState.applyToElem(catalogFilterContainerDiv);
    }

    return catalogFilterContainerDiv;
  }
}

class CatalogHtmlQueryParams {
  public int page = 1;
  public HtmlState filterState;
  public HashSet<String> sections;
}
