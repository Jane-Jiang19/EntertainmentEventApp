/*
 * Author: Jingyan Jiang
 *
 * This class is the Model part of the web application
 * Used to send request and get response from 3rd party API
 * Also parse the data from 3rd party API
 */

package ds.eventmasterservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class EventSearch {
    private String keyword;
    long start;
    long end;




    // send HTTP request to 3rd Party API
    // get HTTP response from 3rd Party API
    public JSONObject search(String keyword) throws IOException, JSONException {
        start = System.currentTimeMillis();
        // Source: https://curlconverter.com/
        URL url = new URL("https://app.ticketmaster.com/discovery/v2/events.json?countryCode=US&size=3&sort=date,asc&keyword="+keyword+"&apikey=XArlsm83JN81i5JCAB6ORlArJtRhOUq2");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();

        end = System.currentTimeMillis();
        // System.out.println(httpConn.getResponseCode());
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        JSONObject obj = new JSONObject(response);
        return obj;
    }

    // Parse the data returned from the erd party API to get the information needed fo each event
    // return a EventList Object which stored all the events
    public EventList processAPIData(JSONObject obj) throws JSONException {
        int i = 0;
        EventList el = new EventList();
        // check if the return from 3rd party is valid for our usage
        // if not, don't update the information of the event.
        if (!obj.isNull("_embedded")){
            int len = obj.getJSONObject("_embedded").getJSONArray("events").length();
            while (i<len){
                String eventname = obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getString("name");
                Event e = new Event(eventname);
                if (!obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).isNull("city")){
                    String city = obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name");
                    e.city = city;
                }
                if (!obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).isNull("state")){
                    String state = obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("name");
                    e.state = state;
                }
                if (!obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).isNull("country")){
                    String country = obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("country").getString("name");
                    e.country = country;
                }
               if (!obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("sales").getJSONObject("public").isNull("startDateTime")){
                    String saledate = obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("sales").getJSONObject("public").getString("startDateTime").substring(0,19);
                    String tillsale = String.valueOf(calculateDays(saledate));
                    e.tillsale = tillsale;
                }
                if (!obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("dates").getJSONObject("start").isNull("dateTime")){
                    String eventdate = obj.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getJSONObject("dates").getJSONObject("start").getString("dateTime").substring(0,19);
                    String tillevent = String.valueOf(calculateDays(eventdate));
                    e.tillevent = tillevent;
                }

                el.addEvent(e);
                i+=1;
            }
        } else{
            return el;
        }
        return el;
    }




    // calculate the number of days from now till the input date
    //https://mkyong.com/java8/java-8-difference-between-two-localdate-or-localdatetime/
    private int calculateDays(String startDatestr){
        LocalDateTime startDate = LocalDateTime.parse(startDatestr);
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(currentDate, startDate);
        int days = (int) duration.toDays();
        return days;
    }



}
