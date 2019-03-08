package com.example.hello;

import org.cometd.bayeux.Promise;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HelloService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    HelloService(BayeuxServer bayeux) {
        super(bayeux, "hello");
        addService("/service/hello", "processHello");
    }

    public void processHello(ServerSession remote, ServerMessage message) {
        Map<String, Object> input = message.getDataAsMap();
        LOGGER.info("Received message: {}", message);

        String name = "unknown";
        if (input != null) {
            LOGGER.info("    input: {}", input);
            name = (String) input.get("name");
        }

        Map<String, Object> output = new HashMap<>();
        output.put("greeting", "Hello, " + name);
        output.put("type", "message");

        LOGGER.info("clientId: {}", remote.getId());

        final ServerMessage.Mutable reply = getBayeux().newMessage();
        reply.setChannel("/hello");
        reply.setData(output);

        LOGGER.info("reply before send: {}", reply);

        remote.deliver(getServerSession(), reply, Promise.noop());
    }
}
