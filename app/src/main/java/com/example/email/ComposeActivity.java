package com.example.email;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.util.List;
import java.util.Locale;

public class ComposeActivity extends AppCompatActivity implements View.OnClickListener {

      private TextToSpeech myTTS;
      private SpeechRecognizer mySpeechRecognizer;

      private EditText toEdt, ccEdt, subjectEdt, messageEdt;
      private ImageButton btnRepeatAction, btnVoice, btnDone;

      private int askNumber;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_compose);

            initilizeTextToSpeech();
            initializeSpeechRecognizer();

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Compose");
            setSupportActionBar(toolbar);

            toEdt = findViewById(R.id.edtTo);
            ccEdt = findViewById(R.id.edtCc);
            subjectEdt = findViewById(R.id.edtSubject);
            messageEdt = findViewById(R.id.edtMessage);

            btnRepeatAction = findViewById(R.id.btnRepeatAction);
            btnDone = findViewById(R.id.btnDone);
            btnVoice = findViewById(R.id.btnVoice);

            askNumber = 0;

            btnVoice.setOnClickListener(this);


      }

      private void startListening() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            mySpeechRecognizer.startListening(intent);
      }

      private void requestTo() {
            speak("Whom do you want to send the message");
            int secs = 2;
            Utils.delay(secs, new Utils.DelayCallback() {
                  @Override
                  public void afterDelay() {
                        // Do something after delay
                        startListening();
                  }
            });

      }

      private void requestCc() {
            speak("Whom do you want to cc the message");
            int secs = 2;
            Utils.delay(secs, new Utils.DelayCallback() {
                  @Override
                  public void afterDelay() {
                        // Do something after delay
                        startListening();
                  }
            });
      }

      private void requestSubject() {
            speak("What is the subject?");
            int secs = 2;
            Utils.delay(secs, new Utils.DelayCallback() {
                  @Override
                  public void afterDelay() {
                        // Do something after delay
                        startListening();
                  }
            });

      }

      private void requestMessage() {
            speak("What is the message?");
            int secs = 2;
            Utils.delay(secs, new Utils.DelayCallback() {
                  @Override
                  public void afterDelay() {
                        // Do something after delay
                        startListening();
                  }
            });

      }

      private void requestSend() {
            speak("Do you want to send the mail");
            int secs = 2;
            Utils.delay(secs, new Utils.DelayCallback() {
                  @Override
                  public void afterDelay() {
                        // Do something after delay
                        startListening();
                  }
            });

      }





      private void initilizeTextToSpeech() {
            myTTS = new TextToSpeech(ComposeActivity.this, new TextToSpeech.OnInitListener() {
                  @Override
                  public void onInit(int status) {
                        if(myTTS.getEngines().size() == 0){
                              Toast.makeText(ComposeActivity.this, "No TTS engine", Toast.LENGTH_SHORT).show();
                        } else {
                              myTTS.setLanguage(Locale.US);
                              speak("I'm in Compose");
                        }
                  }
            });
      }

      private void speak(String s) {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
      }


      private void initializeSpeechRecognizer() {
            if(SpeechRecognizer.isRecognitionAvailable(this) ){
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

            Toast.makeText(this, command, Toast.LENGTH_SHORT).show();

            if(command.contains("to") || command.contains("send")){
                  askNumber = 0;
            }


            if(askNumber %5== 0) {
                  speak(command);
                  String s = command.replace("\\s+","");
                  toEdt.setText(s);
                  askNumber++;
            } else if(askNumber%5 == 1) {
                  speak(command);
                  if(!command.contains("none"))
                        ccEdt.setText(command);
                  askNumber++;
            } else if(askNumber%5 == 2) {
                  speak(command);
                        if(!command.contains("none"))
                  subjectEdt.setText(command);
                  askNumber++;
            } else if(askNumber%5 == 3) {
                  speak(command);
                  messageEdt.setText(command);
                  askNumber++;
            } else if(askNumber%5 == 4) {
                  if(command.contains("yes")){
                        speak("Sending Message");
                        sendEmail();
                  } else if(command.contains("no")){
                        speak("What do you want to edit?");
                        int secs = 2;
                        Utils.delay(secs, new Utils.DelayCallback() {
                              @Override
                              public void afterDelay() {
                                    // Do something after delay
                                    startListening();
                              }
                        });
                  }
                  askNumber++;
            }
      }

      private void sendEmail() {
            //Implement the Gmail API call to email
      }

      @Override
      public void onClick(View v) {
            switch (v.getId()){
                  case R.id.btnVoice:
                        switch (askNumber%5){
                              case 0:
                                    requestTo();
                                    break;
                              case 1:
                                    requestCc();
                                    break;
                              case 2:
                                    requestSubject();
                                    break;
                              case 3:
                                    requestMessage();
                                    break;
                              case 4:
                                    requestSend();
                                    break;
                              default:
                                    askNumber = 0;
                                    break;

                        }
                        break;
            }
      }

      public static class Utils {

            // Delay mechanism

            public interface DelayCallback{
                  void afterDelay();
            }

            public static void delay(int secs, final DelayCallback delayCallback){
                  Handler handler = new Handler();
                  handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                              delayCallback.afterDelay();
                        }
                  }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
            }
      }

}
