package com.example.hello;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.ext.AcknowledgedMessagesExtension;
import org.cometd.server.ext.TimesyncExtension;

public class CometDInitializer extends GenericServlet {
    public void init() throws ServletException {
        BayeuxServer bayeux = (BayeuxServer)getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);

        bayeux.addExtension(new AcknowledgedMessagesExtension());
        TimesyncExtension ts = new TimesyncExtension();
        ts.setAccuracyTarget(1000);
        bayeux.addExtension(ts);

        new HelloService(bayeux);
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
}
