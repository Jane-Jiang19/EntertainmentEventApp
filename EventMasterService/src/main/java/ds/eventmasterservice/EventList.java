/*
 * Author: Jingyan Jiang
 *
 * This class is used to store all the events this service finds
 */

package ds.eventmasterservice;

import com.google.gson.Gson;

import java.util.ArrayList;

public class EventList {
    ArrayList eventlist = new ArrayList<Event>();

    public void addEvent(Event e){
        eventlist.add(e);
    }

    // override the toString method to make it a json formatted string
    public String toString(){
        Gson gson = new Gson();
        // Serialize to JSON
        String messageToSend = gson.toJson(this);
        return messageToSend;
    }
}
