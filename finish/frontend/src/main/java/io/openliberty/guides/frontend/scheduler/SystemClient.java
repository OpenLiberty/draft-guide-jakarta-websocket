package io.openliberty.guides.frontend.scheduler;
// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]

import java.io.IOException;
import java.net.URI;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

// tag::clientEndpoint[]
@ClientEndpoint()
// end::clientEndpoint[]
public class SystemClient {

    private Session session;

    // tag::constructor[]
    public SystemClient(URI endpoint) {
        try {
            // tag::webSocketAPI[]
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpoint);
            // end::webSocketAPI[]
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // end::constructor[]

    // tag::onOpen[]
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.print("Scheduler connected to the server.");
    }
    // end::onOpen[]

    // tag::onMessage[]
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.print("Scheduler received message from the server: " + message);
    }
    // end::onMessage[]

    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
        System.out.print("Scheduler sent message \"" + message + "\" to the server.");
    }

    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Scheduler closed the session.");
    }

}
