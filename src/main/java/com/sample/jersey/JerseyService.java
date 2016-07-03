package com.sample.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("jersey")
public class JerseyService {

	@GET
	@Path("data")
	@Produces(MediaType.TEXT_PLAIN)
	public String getData() {

		return "Hello This Simple Service";
	}

}
