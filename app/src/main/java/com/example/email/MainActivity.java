package com.example.email;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

      String[] nameList = {"Ram Mohan", "Raja Das", "Manu", "Rohan", "Parth", "Prakhar"};
      String[] messageList = {"Hey! how are you? I was wondering if you'd be able to make it to the wedding?",
              "Sir, I have completed the task that you assigned to me last week.",
              "What are you tryfing to say with all the efforts you put in your work?",
              "Sir, I have completed the task that you assigned to me last week.",
              "What are you tryfing to say with all the efforts you put in your work?",
              "Sir, I have completed the task that you assigned to me last week.",
      };
      String[] timeList = {"12:30 PM", "1:00 AM", "Yesterday", "Yesterday", "Yesterday", "Yesterday"};

      ListView listView;
      MyAdapter adapter;

      private TextToSpeech myTTS;
      private SpeechRecognizer mySpeechRecognizer;

      private int currentIndex;
      private String lastCommand;

      ImageButton btnCompose;
      ImageButton btnVoice;
      ImageButton btnforwardFlow;
      ImageButton btnLeftFocus, btnRightFocus;
      ImageButton btnRepeatLast;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            currentIndex = 0;
            lastCommand = "";

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Inbox");
            setSupportActionBar(toolbar);


            initilizeTextToSpeech();
            initializeSpeechRecognizer();


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
            btnVoice = findViewById(R.id.btnVoice);
            btnforwardFlow = findViewById(R.id.forwardFlowBtn);
            btnLeftFocus = findViewById(R.id.leftFocusBtn);
            btnRightFocus = findViewById(R.id.rightFocusBtn);
            btnRepeatLast = findViewById(R.id.btnRepeatAction);

            btnCompose.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ComposeActivity.class);
                        startActivity(intent);
                  }
            });

            btnVoice.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        startListening();
                  }
            });
            btnforwardFlow.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SentActivity.class);
                        startActivity(intent);
                  }
            });

            btnRightFocus.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if (currentIndex < nameList.length) {
                              speak(currentIndex);
                              currentIndex++;
                        }
                  }
            });

            btnLeftFocus.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if (currentIndex > 0) {
                              currentIndex--;
                              speak(currentIndex);
                        }
                  }
            });

            btnRepeatLast.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if (!lastCommand.equals(""))
                              speak(lastCommand);
                  }
            });

      }

      private void startListening() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            mySpeechRecognizer.startListening(intent);
      }

      private void initializeSpeechRecognizer() {
            if (SpeechRecognizer.isRecognitionAvailable(this)) {
                  mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                  mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                        @Override
                        public void onReadyForSpeech(Bundle params) {

                        }

                        @Override
                        public void onBeginningOfSpeech() {

                        }

                        @Override
                        public void onRmsChanged(float rmsdB) {

                        }

                        @Override
                        public void onBufferReceived(byte[] buffer) {

                        }

                        @Override
                        public void onEndOfSpeech() {

                        }

                        @Override
                        public void onError(int error) {

                        }

                        @Override
                        public void onResults(Bundle bundle) {
                              List<String> results = bundle.getStringArrayList(
                                      SpeechRecognizer.RESULTS_RECOGNITION
                              );
                              assert results != null;
                              processResult(results.get(0));
                        }

                        @Override
                        public void onPartialResults(Bundle partialResults) {

                        }

                        @Override
                        public void onEvent(int eventType, Bundle params) {

                        }
                  });
            }
      }

      private void processResult(String command) {
            command = command.toLowerCase();

            if (command.contains("compose") || (command.contains("new") && command.contains("email"))) {
                  Intent intent = new Intent(MainActivity.this, ComposeActivity.class);
                  startActivity(intent);
            }
            if (command.contains("outbox") || command.contains("sent") ||
                    (command.contains("past") && command.contains("email"))) {
                  Intent intent = new Intent(MainActivity.this, SentActivity.class);
                  startActivity(intent);
            }

      }


      private void initilizeTextToSpeech() {
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                  @Override
                  public void onInit(int status) {
                        if (myTTS.getEngines().size() == 0) {
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
            lastCommand = s;
      }

      private void speak(int position) {
            String s;
            s = "Message from" + nameList[position] + "at," + timeList[position] + " is." + messageList[position].substring(0, 60);
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
            lastCommand = s;
      }

      class MyAdapter extends ArrayAdapter<String> {
            Context context;
            String[] rname;
            String[] rmess;
            String[] rtime;

            MyAdapter(Context c, String[] name, String[] message, String[] time) {
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
                  tvname.setText(rname[position]);

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
