package catalog.server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import catalog.content_html.CatalogHtml;
import catalog.html.HtmlStringFromElemTree;
import catalog.other.JsonStringify;
import catalog.other.UtilServer;

public class HandlerApiCatalog implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      // UtilServer.printRequestInfo(exchange);

      Map<String, String> queryParams = UtilServer.queryToMap(exchange.getRequestURI().getQuery());
      var sections = CatalogHtml.renderNeededSections(CatalogHtml.parseQueryParams(queryParams));

      var resultObj = new Result();
      if (sections.filter != null)
        resultObj.filter = HtmlStringFromElemTree.conv(sections.filter);
      if (sections.items != null)
        resultObj.items = HtmlStringFromElemTree.conv(sections.items);
      if (sections.pages != null)
        resultObj.pages = HtmlStringFromElemTree.conv(sections.pages);

      String resultStr = JsonStringify.stringify(resultObj.toMap());

      UtilServer.sendStringResponse(exchange, resultStr);
    } catch (Exception e) {
      e.printStackTrace();
      UtilServer.sendStatus(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
    }
  }
}

class Result {
  public String filter;
  public String items;
  public String pages;

  public Map<String, String> toMap() {
    if (filter == null && items == null && pages == null)
      return null;

    Map<String, String> result = new HashMap<>(3);

    // @formatter:off
    if (filter  != null)  result.put("filter",  filter);
    if (items   != null)  result.put("items",   items);
    if (pages   != null)  result.put("pages",   pages);
    // @formatter:on

    return result;
  }
}