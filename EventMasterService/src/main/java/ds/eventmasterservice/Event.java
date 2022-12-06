/*
 * Author: Jingyan Jiang
 *
 * This class is used to store the info of one event
 */

package ds.eventmasterservice;

import com.google.gson.Gson;

public class Event {
    String name = "";
    String city ="N/A";
    String state = "N/A";
    String country = "N/A";
    String tillevent = "N/A";
    String tillsale = "N/A";

    public Event(String name){
        this.name = name;

    }

    public String toString(){
        Gson gson = new Gson();
        // Serialize to JSON
        String messageToSend = gson.toJson(this);
        return messageToSend;
    }

}
