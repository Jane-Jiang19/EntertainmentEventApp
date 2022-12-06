/*
 * Author: Jingyan Jiang
 *
 * This class is the controller part of web application
 * Get HTTP request from the Android application
 * Send HTTP response to the Android application
 * Display the DashBoard
 */


package ds.eventmasterservice;

import java.io.*;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "EventServlet",
            urlPatterns = {"/submit", "/getResults"})
public class EventServlet extends HttpServlet {
    EventSearch es = null;
    MongoDB mongo = null;

    public void init() {
        es = new EventSearch();
        mongo = new MongoDB();
        mongo.create();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long receiveRequest = System.currentTimeMillis();
        long sendtoAPI = 0;
        long receivefromAPI = 0;
        long sendResponse = 0;
        String action =  request.getServletPath();
        PrintWriter out = response.getWriter();
        String ua = request.getHeader("User-Agent");
        boolean mobile;
        if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
            mobile = true;


        } else {
            mobile = false;
        }
        EventList el;
        String keyword = "";
        // if the request contains submit, search by the keyword
        if (action.equals("/submit")){
            keyword = request.getQueryString();
            System.out.println(keyword);
            try {
                JSONObject eventJson = es.search(keyword);
                sendtoAPI = es.start;
                receivefromAPI = es.end;
                el = es.processAPIData(eventJson);
                response.setContentType("text/json");

                if(el.eventlist.size()==0){
                    out.write("Not Found");
                }else{
                    System.out.println(el.toString());
                    out.write(el.toString());
                }

                System.out.println("finished");
                sendResponse = System.currentTimeMillis();

                //Mongodb

                StringBuilder eventname = new StringBuilder();
                for (int i = 0; i<el.eventlist.size();i++){
                    if (i == el.eventlist.size()-1){
                        eventname.append(((Event) el.eventlist.get(i)).name);

                    } else{
                        eventname.append(((Event) el.eventlist.get(i)).name+", ");
                    }
                }
                if (mobile){
                    storeSearch(keyword, eventname.toString(), receiveRequest, sendtoAPI, receivefromAPI, sendResponse);
                }


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }else{
            // if the request contains getResults, display the dashbaord
            DashBoard dashboard = new DashBoard();
            ArrayList<Document> docs = mongo.getAll();
            long apiTime = dashboard.calculateAPIResTime(docs);
            long appTime = dashboard.calculateAPPResTime(docs);
            String topkeyword = dashboard.findTopKeywords(docs);
            request.setAttribute("apiTime", apiTime);
            request.setAttribute("appTime", appTime);
            request.setAttribute("topkeyword",topkeyword);
            request.getSession().setAttribute("docs",docs);
            String nextView = "dashboard.jsp";
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            try {
                view.forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }

    }

    // store the data to MongoDB
    public void storeSearch(String keyword, String result, long getReqTime, long reqAPITime, long resAPITime, long sendResTime){
        Document d = new Document();
        d.append("keyword", keyword);
        d.append("result", result);
        d.append("getReqTime", getReqTime);
        d.append("reqAPITime", reqAPITime);
        d.append("resAPITime", resAPITime);
        d.append("sendResTime", sendResTime);
        mongo.insert(d);

    }

    public void destroy() {
    }
}