package com.example.hello;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

import java.util.HashMap;
import java.util.Map;

public class HelloService extends AbstractService {

    HelloService(BayeuxServer bayeux) {
        super(bayeux, "hello");
        addService("/service/hello", "processHello");
    }

    public void processHello(ServerSession remote, ServerMessage message) {
        Map<String, Object> input = message.getDataAsMap();
        String name = (String)input.get("name");

        Map<String, Object> output = new HashMap<>();
        output.put("greeting", "Hello, " + name);

        // DEPRECATED?
        remote.deliver(getServerSession(), "/hello", output);
    }

}
