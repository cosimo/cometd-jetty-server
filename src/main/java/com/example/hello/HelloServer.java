package com.example.hello;

import org.cometd.server.CometDServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class HelloServer {

  private static final String ROOT = "/";
  private static final int PORT = 8008;

  public static void main(String[] args) throws Exception {
    Server server;

    int maxThreads = 100;
    int minThreads = 10;
    int idleTimeout = 600;

    QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

    server = new Server(threadPool);
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(PORT);
    server.setConnectors(new Connector[] { connector });

    // The context where the application is deployed
    ServletContextHandler context = new ServletContextHandler(server, ROOT);
    WebSocketServerContainerInitializer.configureContext(context);

    // Setup the CometD servlet.
    String cometdPath = "/cometd/*";

    ServletHolder cometdServletHolder = new ServletHolder(CometDServlet.class);
    cometdServletHolder.setInitParameter("ws.cometdURLMapping", cometdPath);
    cometdServletHolder.setInitParameter("timeout", String.valueOf(30000));
    cometdServletHolder.setInitParameter("maxInterval", String.valueOf(20000));
    cometdServletHolder.setInitParameter("maxConnectDelay", String.valueOf(30000));
    cometdServletHolder.setInitParameter("multiFrameInterval", String.valueOf(-1));
    cometdServletHolder.setInitParameter("logLevel", String.valueOf(2));
    cometdServletHolder.setInitParameter("ws.bufferSize", String.valueOf(200000));
    cometdServletHolder.setAsyncSupported(true);
    cometdServletHolder.setInitOrder(1);

    ServletHolder cometdInitializer = new ServletHolder(CometDInitializer.class);
    cometdInitializer.setInitOrder(2);

    FilterHolder corsFilter = new FilterHolder(CrossOriginFilter.class);
    corsFilter.setAsyncSupported(true);

    context.addServlet(cometdServletHolder, cometdPath);
    context.addServlet(cometdInitializer, null);

    /*EnumSet<DispatcherType> allTypes = EnumSet.of(DispatcherType.ASYNC,
                                                  DispatcherType.INCLUDE,
                                                  DispatcherType.ERROR,
                                                  DispatcherType.REQUEST,
                                                  DispatcherType.FORWARD);
    context.addFilter(corsFilter, cometdPath, allTypes);
    */

    server.start();
    server.dumpStdErr();
  }

}
