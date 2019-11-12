package de.hska.iwi.vislab.lab2.example;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class FibonacciTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and
        // Main.startServer())
        // --
        // c.configuration().enable(new
        // org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
        target.path("fibonacci/reset").request().post(null);
    }

    @After
    public void tearDown() throws Exception {
        target.path("fibonacci/reset").request().post(null);
        server.shutdown();
    }

    @Test
    public void getZero() {
        String responseMsg = target.path("fibonacci").request().accept(MediaType.TEXT_PLAIN).get(String.class);
        assertEquals("0", responseMsg);
    }

    @Test
    public void getFirstNumber(){
        String responseMsg = target.path("fibonacci/next").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
        assertEquals("1", responseMsg);
    }

    @Test
    public void getThirdNumber(){
        target.path("fibonacci/next").request().post(null);
        target.path("fibonacci/next").request().post(null);
        String responseMsg = target.path("fibonacci/next").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
        assertEquals("2", responseMsg);
    }

    @Test
    public void getFifthNumber(){
        target.path("fibonacci/next").request().post(null);
        target.path("fibonacci/next").request().post(null);
        target.path("fibonacci/next").request().post(null);
        target.path("fibonacci/next").request().post(null);
        String responseMsg = target.path("fibonacci/next").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
        assertEquals("5", responseMsg);
    }

    @Test
    public void checkReset(){
        String responseMsg = target.path("fibonacci").request().accept(MediaType.TEXT_PLAIN).get(String.class);
        assertEquals("0", responseMsg);
        responseMsg = target.path("fibonacci/next").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
        assertEquals("1", responseMsg);
        responseMsg = target.path("fibonacci/reset").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
        assertEquals("Reset successful", responseMsg);
        responseMsg = target.path("fibonacci").request().accept(MediaType.TEXT_PLAIN).get(String.class);
        assertEquals("0", responseMsg);
        responseMsg = target.path("fibonacci/next").request().accept(MediaType.TEXT_PLAIN).post(null, String.class);
        assertEquals("1", responseMsg);
    }
}
