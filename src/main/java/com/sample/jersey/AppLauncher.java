package com.sample.jersey;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.AbstractSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class AppLauncher {

	public static void main(String[] args) {

		URI uri = UriBuilder.fromUri("http://localhost").port(8080).build();
		ResourceConfig config = new ResourceConfig().packages("com.sample.jersey");
		Server server = JettyHttpContainerFactory.createServer(uri, config);
		ServerConnector connector = new ServerConnector(server);
		server.addConnector(connector);
		AbstractSessionIdManager sessionIdManager = new HashSessionIdManager();
		sessionIdManager.setWorkerName("node1");
		server.setSessionIdManager(sessionIdManager);
		System.out.println("Stated Server At - Host" + server.getURI());

	}

}
