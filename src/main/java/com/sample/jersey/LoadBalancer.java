package com.sample.jersey;

import java.net.InetSocketAddress;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.proxy.BalancerServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.AbstractSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class LoadBalancer {

	private Server server1;
	private Server server2;
	private Server balancer;
	private boolean stickeySession;

	public static void main(String[] args) throws Exception {
		LoadBalancer balancer = new LoadBalancer();
		balancer.roundRobin();// for round robin 
//		balancer.stickeySession(); // for stickey session
		balancer.startBalancer(ServletContainer.class);
	}

	public void roundRobin() {
		this.stickeySession = false;
	}

	public void stickeySession() {
		this.stickeySession = true;
	}

	protected void startBalancer(Class<? extends HttpServlet> servletClass) throws Exception {
		server1 = startServer(new ServletHolder(servletClass), "node1", 8081);
		server1.start();

		server2 = startServer(new ServletHolder(servletClass), "node2", 8091);
		server2.start();
		ServletHolder holder = new ServletHolder(BalancerServlet.class);
		holder.setInitParameter("stickySessions", String.valueOf(stickeySession));
		holder.setInitParameter("proxyPassReverse", "true");
		holder.setInitParameter("balancerMember." + "node1" + ".proxyTo", "http://localhost:" + 8081);
		holder.setInitParameter("balancerMember." + "node2" + ".proxyTo", "http://localhost:" + 8091);
		balancer = startServer(holder, null, 8071);
		balancer.start();
		System.out.println(balancer.getURI());
	}

	private Server startServer(ServletHolder servletHolder, String node, int port) {
		InetSocketAddress address = new InetSocketAddress("localhost", port);
		Server server = new Server(address);
		ServerConnector connector = new ServerConnector(server);
		server.addConnector(connector);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		server.setHandler(context);
		servletHolder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.sample.jersey");
		context.addServlet(servletHolder, "/*");
		if (node != null) {
			AbstractSessionIdManager sessionIdManager = new HashSessionIdManager();
			sessionIdManager.setWorkerName(node);
			server.setSessionIdManager(sessionIdManager);
		}

		return server;
	}
}
