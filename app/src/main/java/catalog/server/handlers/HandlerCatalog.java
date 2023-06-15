package catalog.server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import catalog.content_html.CatalogHtml;
import catalog.other.UtilServer;

public class HandlerCatalog implements HttpHandler {
  // "Class HttpExchange"
  // https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpExchange.html
  // 1. getRequestMethod() to determine the command
  // 2. getRequestHeaders() to examine the request headers (if needed)
  // 3. getRequestBody() returns a InputStream for reading the request body. After
  // reading the request body, the stream is close.
  // 4. getResponseHeaders() to set any response headers, except content-length
  // 5. sendResponseHeaders(int,long) to send the response headers. Must be called
  // before next step.
  // 6. getResponseBody() to get a OutputStream to send the response body. When
  // the response body has been written, the stream must be closed to terminate
  // the exchange.
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      // UtilServer.printRequestInfo(exchange);

      Map<String, String> queryParams = UtilServer.queryToMap(exchange.getRequestURI().getQuery());
      String result = CatalogHtml.renderAllInTemplate(CatalogHtml.parseQueryParams(queryParams));

      UtilServer.sendStringResponse(exchange, result);
    } catch (Exception e) {
      e.printStackTrace();
      UtilServer.sendStatus(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
    }
  }
}
