package edu.heinz.ds.eventmasterandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InterestingEvent extends AppCompatActivity {

    InterestingEvent me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding a picture from Flickr, it
         * can callback to this object with the resulting picture Bitmap.  The "this" of the OnClick will be the OnClickListener, not
         * this InterestingPicture.
         */
        final InterestingEvent ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                System.out.println("onClick");
                System.out.println(Thread.currentThread().getThreadGroup().getName());
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                System.out.println("searchTerm = " + searchTerm);
                GetEvent gp = new GetEvent();
                gp.search(searchTerm, me, ma); // Done asynchronously in another thread.  It calls ip.pictureReady() in this thread when complete.
            }
        });
    }

    /*
     * This is called by the GetPicture object when the picture is ready.  This allows for passing back the Bitmap picture for updating the ImageView
     */
    public void eventReady(String event) {
        System.out.println("pictureReady");
        System.out.println(Thread.currentThread().getThreadGroup().getName());
        //ImageView pictureView = (ImageView)findViewById(R.id.interestingPicture);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        TextView descriptionView = (TextView) findViewById(R.id.textView2);
        String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
        if (event != null) {
            //pictureView.setImageBitmap(picture);
            System.out.println("event");
            //pictureView.setVisibility(View.VISIBLE);
            descriptionView.setText(event);

        } else {
            //pictureView.setImageResource(R.mipmap.ic_launcher);
            System.out.println("No event");
            //pictureView.setVisibility(View.INVISIBLE);
            descriptionView.setText("Sorry, I could not find a event for "+searchTerm);

        }
        searchView.setText("");
        //pictureView.invalidate();
    }
}
