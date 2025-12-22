package com.lqh.jspproject.Controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/")
public class TestController
{
    @GET
    @Path("/Test")
    @Produces("text/plain")
    public String Test()
    {
        return "This is a Test";
    }
}
