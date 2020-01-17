package tcc;

import tcc.flight.FlightReservationDoc;
import tcc.hotel.HotelReservationDoc;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Simple non-transactional client. Can be used to populate the booking services
 * with some requests.
 */
public class TransactionalClient {
    public static void main(String[] args) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(TestServer.BASE_URI);

            GregorianCalendar tomorrow = new GregorianCalendar();
            tomorrow.setTime(new Date());
            tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 1);

            // book flight

            WebTarget webTargetFlight = target.path("flight");

            FlightReservationDoc docFlight = new FlightReservationDoc();
            docFlight.setName("Christian");
            docFlight.setFrom("Karlsruhe");
            docFlight.setTo("Berlin");
            docFlight.setAirline("airberlin");
            docFlight.setDate(tomorrow.getTimeInMillis());

            Response responseFlight = webTargetFlight.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docFlight));

            // book hotel

            WebTarget webTargetHotel = target.path("hotel");

            HotelReservationDoc docHotel = new HotelReservationDoc();
            docHotel.setName("Christian");
            docHotel.setHotel("Interconti");
            docHotel.setDate(tomorrow.getTimeInMillis());

            Response responseHotel = webTargetHotel.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docHotel));

            FlightReservationDoc outputFlight = responseFlight.readEntity(FlightReservationDoc.class);
            HotelReservationDoc outputHotel = responseHotel.readEntity(HotelReservationDoc.class);

            if (responseFlight.getStatus() == 200 && responseHotel.getStatus() == 200) {
                //String confirmFlightUrl = outputFlight.getUrl();
                //String[] splitURL = confirmFlightUrl.split("/");
                //String flightID = splitURL[splitURL.length - 1];

                String flightID = getFlightID(outputFlight);
                outputFlight.setConfirmed(true);
                webTargetFlight = target.path("flight/" + flightID);
                Response confirmedFlight;
				System.out.print("Confirm flight");
                do {
                	System.out.print(".");
                    confirmedFlight = webTargetFlight.request().accept(MediaType.TEXT_PLAIN)
                            .put(Entity.xml(outputFlight));
                    Thread.sleep(500);
                } while (confirmedFlight.getStatus() != 200);
                System.out.print("\n");


                String hotelID = getHotelID(outputHotel);
                outputHotel.setConfirmed(true);
                webTargetHotel = target.path("hotel/" + hotelID);
                Response confirmedHotel;

                System.out.print("Confirm hotel");
                do {
                	System.out.print(".");
                    confirmedHotel = webTargetHotel.request().accept(MediaType.TEXT_PLAIN)
                            .put(Entity.xml(outputHotel));
                    Thread.sleep(500);
                } while (confirmedHotel.getStatus() != 200);
				System.out.print("\n");
                System.out.println("Successfully booked both flight and hotel!");

            } else if (responseFlight.getStatus() == 200) {
            	System.out.println("There was a problem reserving the hotel...rolling back flight reservation");
                String flightID = getFlightID(outputFlight);
				webTargetFlight = target.path("flight/" + flightID);
				Response deletedFlight;
				do {
					deletedFlight = webTargetFlight.request().accept(MediaType.TEXT_PLAIN).delete();
					Thread.sleep(500);
				} while (deletedFlight.getStatus() != 200);
				System.out.println("Flight reservation successfully cancelled.");
            } else if (responseHotel.getStatus() == 200){
				System.out.println("There was a problem reserving the flight...rolling back hotel reservation");
				String hotelID = getHotelID(outputHotel);
				webTargetHotel = target.path("hotel/" + hotelID);
				Response deletedHotel;
				do {
					deletedHotel = webTargetHotel.request().accept(MediaType.TEXT_PLAIN).delete();
					Thread.sleep(500);
				} while (deletedHotel.getStatus() != 200);
				System.out.println("Hotel reservation successfully cancelled.");
			} else {
            	System.out.println("Nothing got reserved, no cancellation needed.");
			}


            //System.out.println("Output from Server: " + outputHotel);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFlightID(FlightReservationDoc flightReservationDoc) {
        String flightID = flightReservationDoc.getUrl().substring(flightReservationDoc.getUrl().lastIndexOf("/") + 1);
        System.out.println("FlightID: " + flightID);
        return flightID;
    }

    private static String getHotelID(HotelReservationDoc hotelReservationDoc) {
        String hotelID = hotelReservationDoc.getUrl().substring(hotelReservationDoc.getUrl().lastIndexOf("/") + 1);
        System.out.println("HotelID: " + hotelID);
        return hotelID;
    }
}
