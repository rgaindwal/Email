package com.example.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

      private TextToSpeech myTTS;
      private SpeechRecognizer mySpeechRecognizer;

      private TextInputLayout txtEmail, txtPass;
      private ImageView imgVoice;
      private MaterialButton btnLogin;
      private int currentCtr = 0;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            initilizeTextToSpeech();
            initializeSpeechRecognizer();

            txtEmail = findViewById(R.id.txtEmail);
            txtPass = findViewById(R.id.txtPass);
            imgVoice = findViewById(R.id.imgVoice);
            btnLogin = findViewById(R.id.btnLogin);

            imgVoice.setOnClickListener(this);
            btnLogin.setOnClickListener(this);

      }


      private void initilizeTextToSpeech() {
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                  @Override
                  public void onInit(int status) {
                        if (myTTS.getEngines().size() == 0) {
                              Toast.makeText(LoginActivity.this, "No TTS engine", Toast.LENGTH_SHORT).show();
                        } else {
                              myTTS.setLanguage(Locale.US);
                              speak("Welcome to Viz Email");
                        }
                  }
            });
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

            if (command.contains("gmail") || command.contains("yahoo") ||
                    command.contains("in") || command.contains("com") ||
                    command.contains("email")) {
                  Objects.requireNonNull(txtEmail.getEditText()).setText(command);
            } else if (command.contains("yes")) {
                  if(Objects.requireNonNull(txtEmail.getEditText()).getText().toString().toLowerCase().equals("hello @ gmail.com") &&
                          Objects.requireNonNull(txtPass.getEditText()).getText().toString().toLowerCase().equals("1234")){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                  }
            } else if (command.contains("no")) {
                  currentCtr = 3;
            } else {
                  Objects.requireNonNull(txtPass.getEditText()).setText(command);
            }


      }

      private void speak(String s) {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
      }

      private void startListening() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            mySpeechRecognizer.startListening(intent);
      }

      @Override
      public void onClick(View v) {
            switch (v.getId()) {
                  case R.id.txtEmail:

                        break;
                  case R.id.txtPass:

                        break;
                  case R.id.btnLogin:
                        Objects.requireNonNull(txtEmail.getEditText()).setText("hello@gmail.com");
                        Objects.requireNonNull(txtPass.getEditText()).setText("1234");

                        int secs = 2;
                        ComposeActivity.Utils.delay(secs, new ComposeActivity.Utils.DelayCallback() {
                              @Override
                              public void afterDelay() {
                                    // Do something after delay
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                              }
                        });

                        break;
                  case R.id.imgVoice:
                        if (currentCtr % 4 == 0) {
                              speak("Speak Email");
                               secs = 1;
                              ComposeActivity.Utils.delay(secs, new ComposeActivity.Utils.DelayCallback() {
                                    @Override
                                    public void afterDelay() {
                                          // Do something after delay
                                          startListening();
                                    }
                              });
                              currentCtr++;
                        } else if (currentCtr % 4 == 1) {
                              speak("Speak Password");
                               secs = 1;
                              ComposeActivity.Utils.delay(secs, new ComposeActivity.Utils.DelayCallback() {
                                    @Override
                                    public void afterDelay() {
                                          // Do something after delay
                                          startListening();
                                    }
                              });
                              currentCtr++;
                        } else if (currentCtr % 4 == 2) {
                              speak("Shall we login?");
                               secs = 1;
                              ComposeActivity.Utils.delay(secs, new ComposeActivity.Utils.DelayCallback() {
                                    @Override
                                    public void afterDelay() {
                                          // Do something after delay
                                          startListening();
                                    }
                              });
                              currentCtr++;
                        } else {
                              speak("Retry");
                              currentCtr = 0;
                        }
                        break;
            }
      }
}
