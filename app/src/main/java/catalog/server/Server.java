package catalog.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import catalog.other.EnvMap;
import catalog.server.handlers.HandlerApiCatalog;
import catalog.server.handlers.HandlerCatalog;
import catalog.server.handlers.HandlerStatic;

public class Server {
  private static HttpServer instance;
  private static int port = 8080;

  static {
    if (EnvMap.PRODUCTION_FLAG) {
      port = Integer.parseInt(EnvMap.PORT);
    }
  }

  public static void start() {
    if (instance != null)
      throw new Error();

    try {
      instance = HttpServer.create(new InetSocketAddress(port), 0);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }

    instance.createContext("/api/catalog", new HandlerApiCatalog());
    instance.createContext("/catalog", new HandlerCatalog());
    instance.createContext("/pub/", new HandlerStatic("/pub/", "./pub/", ""));

    instance.setExecutor(null);
    instance.start();
    System.out.println("Server started on port " + port + ".");
  }
}
