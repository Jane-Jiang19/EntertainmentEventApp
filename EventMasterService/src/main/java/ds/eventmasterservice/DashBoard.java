/*
 * Author: Jingyan Jiang
 *
 * This class is used to do analytics for log data.
 */


package ds.eventmasterservice;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashBoard {

    Map keywordCount = new HashMap<String, Integer>();
    long apiResTime;
    long appResTime;


    // calculate the average response time of the 3rd party API
    public long calculateAPIResTime(ArrayList<Document> docs){
        long totaltime = 0;
        for (Document d: docs){
            totaltime += d.getLong("resAPITime")-d.getLong("reqAPITime");
        }
        apiResTime = totaltime/docs.size();
        return apiResTime;
    }

    // calculate the average response time of this web service
    public long calculateAPPResTime(ArrayList<Document> docs){
        long totaltime = 0;
        for (Document d: docs){
            totaltime += d.getLong("sendResTime")-d.getLong("getReqTime");
        }
        appResTime = totaltime/docs.size();
        return appResTime;
    }

    // find the top seach word from user inputs
    public String findTopKeywords(ArrayList<Document> docs){
        for (Document d: docs){
            String keyword = d.getString("keyword");
            if (keywordCount.containsKey(keyword)){
                int tmpcount = (int) keywordCount.get(keyword)+1;
                keywordCount.put(keyword,tmpcount);

            }else{
                keywordCount.put(keyword,1);
            }
        }
        int max = 0;
        String topword="";
        for (Object o:keywordCount.keySet()){
            if ((int)keywordCount.get(o)>max){
                max = (int)keywordCount.get(o);
                topword = (String) o;
            }
        }
        return topword;
    }



}
