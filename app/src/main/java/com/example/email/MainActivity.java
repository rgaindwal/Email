package com.example.email;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

      String[] nameList = {"Ram Mohan", "Raja Das", "Manu"};
      String[] messageList = {"Hey! how are you? I was wondering if you'd be able to make it to the wedding?",
              "Sir, I have completed the task that you assigned to me last week.",
              "What are you tryfing to say with all the efforts you put in your work?"
      };
      String[] timeList = {"12:30 PM", "1:00 AM", "Yesterday"};

      ListView listView;
      MyAdapter adapter;

      private TextToSpeech myTTS;

      ImageButton btnCompose ;



      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            initilizeTextToSpeech();

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Inbox");
            setSupportActionBar(toolbar);

            listView = findViewById(R.id.listEmail);
            adapter = new MyAdapter(this, nameList, messageList, timeList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        speak(position);
                  }
            });


            btnCompose = findViewById(R.id.composeBtn);

            btnCompose.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ComposeActivity.class);
                        startActivity(intent);
                  }
            });

      }



      private void initilizeTextToSpeech() {
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                  @Override
                  public void onInit(int status) {
                        if(myTTS.getEngines().size() == 0){
                              Toast.makeText(MainActivity.this, "No TTS engine", Toast.LENGTH_SHORT).show();
                        } else {
                              myTTS.setLanguage(Locale.US);
                              speak("Welcome to your inbox");
                        }
                  }
            });
      }

      private void speak(String s) {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
      }

      private void speak(int position) {
            String s;
            s = "Message from" + nameList[position] + "at," + timeList[position] + " is." + messageList[position].substring(0, 60);
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
      }

      class MyAdapter extends ArrayAdapter<String>{
            Context context;
            String[] rname;
            String[] rmess;
            String[] rtime;

            MyAdapter(Context c, String[] name, String[] message, String[] time){
                  super(c, R.layout.layout_email_item, R.id.tvName, name);

                  this.context = c;
                  this.rname = name;
                  this.rmess = message;
                  this.rtime = time;
            }


            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                  LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  View row = layoutInflater.inflate(R.layout.layout_email_item, parent, false);
                  TextView tvname = row.findViewById(R.id.tvName);
                  TextView tvmessage = row.findViewById(R.id.tvMessage);
                  TextView tvtime = row.findViewById(R.id.tvTime);


                  tvmessage.setText(rmess[position]);
                  tvtime.setText(rtime[position]);
                 tvname.setText(rname[ position]);

                  return row;
            }
      }

      @Override
      protected void onResume() {
            speak("Welcome to your inbox");
            super.onResume();
      }


      @Override
      protected void onPause() {
            super.onPause();
//            myTTS.shutdown();
      }

      @Override
      protected void onDestroy() {
            super.onDestroy();
            myTTS.shutdown();
      }
}
