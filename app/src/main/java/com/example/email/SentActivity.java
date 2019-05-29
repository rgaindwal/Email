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
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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

import java.util.List;
import java.util.Locale;
public class SentActivity extends AppCompatActivity implements View.OnClickListener{

      String[] nameList = {"Raja Das", "Manu","Ram Mohan", "Rohan", "Parth", "Prakhar"};
      String[] messageList = { "Sir, I have completed the task that you assigned to me last week.",
              "What are you tryfing to say with all the efforts you put in your work?",
              "Hey! how are you? I was wondering if you'd be able to make it to the wedding?",
              "Sir, I have completed the task that you assigned to me last week.",
              "Sir, I have completed the task that you assigned to me last week.",
              "What are you tryfing to say with all the efforts you put in your work?",
      };
      String[] timeList = {"12:30 PM", "1:00 AM", "Yesterday", "Yesterday", "Yesterday", "Yesterday"};

      ListView listView;
      MyAdapter adapter;

      private TextToSpeech myTTS;

      ImageButton btnBackFlow;
      ImageButton btnRepeatLast;

      private String lastCommand;


      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sent);

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Inbox");
            setSupportActionBar(toolbar);

            initilizeTextToSpeech();

            btnBackFlow = findViewById(R.id.backFlowBtn);
            btnRepeatLast = findViewById(R.id.btnRepeatAction);

            listView = findViewById(R.id.listEmail);
            adapter = new MyAdapter(this, nameList, messageList, timeList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        speak(position);
                  }
            });


            btnBackFlow.setOnClickListener(this);
            btnRepeatLast.setOnClickListener(this);
      }

      private void initilizeTextToSpeech() {
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                  @Override
                  public void onInit(int status) {
                        if(myTTS.getEngines().size() == 0){
                              Toast.makeText(SentActivity.this, "No TTS engine", Toast.LENGTH_SHORT).show();
                        } else {
                              myTTS.setLanguage(Locale.US);
                              speak("Welcome to your Outbox");
                        }
                  }
            });
      }

      private void speak(String s) {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
            lastCommand = s;
      }

      private void speak(int position) {
            String s;
            s = "Message from" + nameList[position] + "at," + timeList[position] + " is." + messageList[position].substring(0, 60);
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
            lastCommand = s;
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
      public void onClick(View v) {
            switch (v.getId()){
                  case R.id.backFlowBtn:
                        Intent intent = new Intent(SentActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                  case R.id.btnRepeatAction:
                        speak(lastCommand);
                        break;
            }
      }
}
