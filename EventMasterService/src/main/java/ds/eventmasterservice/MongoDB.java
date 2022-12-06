/*
 * Author: Jingyan Jiang
 *
 * This class is the controller part of web application
 * Get HTTP request from the Android application
 * Send HTTP response to the Android application
 * This class is used to connect to the MongoDB, store data in it, get data from it.
 */

package ds.eventmasterservice;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;

public class MongoDB {

    MongoClient mongoClient;
    MongoDatabase database;

    // connect to the MongoDB
    //Source: https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/#insert-operations
    public void create(){
        // Replace the uri string with your MongoDB deployment's connection string
        ConnectionString connectionString = new ConnectionString("mongodb+srv://shawnj:melodyr@cluster0.ncxj0a2.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("Cluster0");
        database.getCollection("EventDB");
        if (database == null){
            database.createCollection("EventDB");
        }

    }

    // used to store data in the MongoDB
    public void insert(Document d){
        database.getCollection("EventDB").insertOne(d);
        System.out.println("Document inserted successfully");

    }

    // Get all data from MongoDB
    public ArrayList getAll(){
        //Retrieving the documents
        ArrayList documents = new ArrayList();
        FindIterable<Document> iterDoc = database.getCollection("EventDB").find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            documents.add((Document) it.next());

        }
        return documents;
    }




}
