package de.hska.iwi.vislab.lab2.example;

import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.prefs.Preferences;

@Path("fibonacci")
public class FibonacciService {

    private int fibonacciOld = 0;
    private int fibonacciNew = -1;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int currentValue(){
        loadValues();
        if(fibonacciNew == -1) {
            return 0;
        } else {
            return fibonacciNew;
        }
    }

    @POST
    @Path("next")
    @Produces(MediaType.TEXT_PLAIN)
    public int next(){
        loadValues();
        if (fibonacciNew == -1){
            fibonacciNew = 1;
            writeValues();
            return 1;
        } else {
            int temp = fibonacciOld + fibonacciNew;
            fibonacciOld = fibonacciNew;
            fibonacciNew = temp;
            writeValues();
            return temp;
        }
    }

    @POST
    @Path("reset")
    @Produces(MediaType.TEXT_PLAIN)
    public String reset(){
        fibonacciOld = 0;
        fibonacciNew = -1;
        writeValues();
        return "Reset successful";
    }


    private void writeValues(){
       /* URL resourceUrl = getClass().getResource("fibonacci.properties");
        try (OutputStream output = new FileOutputStream(new File(resourceUrl.toURI()))){
            Properties prop  =new Properties();

            prop.setProperty("fibonacciOld", Integer.toString(this.fibonacciOld));
            prop.setProperty("fibonacciNew", Integer.toString(this.fibonacciNew));

            prop.store(output, null);

            System.out.println("Written properties: " + prop);
        } catch (IOException io){
            io.printStackTrace();
        } catch (URISyntaxException uri){
            uri.printStackTrace();
        }*/
       Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
       prefs.putInt("fibonacciOld", fibonacciOld);
       prefs.putInt("fibonacciNew", fibonacciNew);
    }

    private void loadValues(){
        /*try (InputStream input = getClass().getClassLoader().getResourceAsStream("fibonacci.properties")){
            Properties prop  =new Properties();

            prop.load(input);

            this.fibonacciOld = Integer.getInteger(prop.getProperty("fibonacciOld"));
            this.fibonacciNew = Integer.getInteger(prop.getProperty("fibonacciNew"));


            System.out.println("Read properties: " + fibonacciOld + ", " + fibonacciNew);
        } catch (IOException io){
            io.printStackTrace();
        }*/
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        this.fibonacciOld = prefs.getInt("fibonacciOld", 0);
        this.fibonacciNew = prefs.getInt("fibonacciNew", -1);
    }

}
