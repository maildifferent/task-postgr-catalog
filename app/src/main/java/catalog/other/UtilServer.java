package catalog.other;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class UtilServer {
  // "Java - HttpServer Example"
  // https://www.logicbig.com/tutorials/core-java-tutorial/http-server/http-server-basic.html
  static public void printRequestInfo(HttpExchange exchange) {
    System.out.println("-- Headers. --");
    Headers requestHeaders = exchange.getRequestHeaders();
    requestHeaders.entrySet().forEach(System.out::println);

    System.out.println("-- Principle. --");
    HttpPrincipal principal = exchange.getPrincipal();
    System.out.println(principal);

    System.out.println("-- HTTP method. --");
    String requestMethod = exchange.getRequestMethod();
    System.out.println(requestMethod);

    System.out.println("-- Query. --");
    URI requestURI = exchange.getRequestURI();
    String query = requestURI.getQuery();
    System.out.println(query);

    System.out.println();
  }

  // "Get URL parameters using JDK HTTP serve"
  // https://www.rgagnon.com/javadetails/java-get-url-parameters-using-jdk-http-server.html
  // Map<String,String>parms=queryToMap(httpExchange.getRequestURI().getQuery());
  /**
   * Returns the url parameters in a map.
   * 
   * @param query
   * @return map
   */
  static public Map<String, String> queryToMap(String query) {
    Map<String, String> result = new HashMap<String, String>();
    if (query == null)
      return result;
    for (String param : query.split("&")) {
      String pair[] = param.split("=");
      if (pair.length > 1) {
        result.put(pair[0], pair[1]);
      } else {
        result.put(pair[0], "");
      }
    }
    return result;
  }

  static public void sendStatus(HttpExchange httpExchange, int statusCode) throws IOException {
    httpExchange.sendResponseHeaders(statusCode, 0);
    httpExchange.getResponseBody().close();
  }

  static public void sendStringResponse(HttpExchange httpExchange, String response) throws IOException {
    // https://medium.com/@sayan-paul/types-of-http-request-methods-9a515aecbe8c
    Headers responseHeaders = httpExchange.getResponseHeaders();
    responseHeaders.add("Access-Control-Allow-Origin", "*");
    responseHeaders.add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    responseHeaders.add("Access-Control-Allow-Credentials", "true");
    responseHeaders.add("Access-Control-Allow-Methods", "GET, POST");

    byte[] bytes = response.getBytes("UTF-8");
    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK /* 200 */, bytes.length);
    OutputStream os = httpExchange.getResponseBody();
    os.write(bytes);
    os.close();
  }
}
