/*
 * Copyright (C) 2014 Brian Antonelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.brianantonelli.glass.helloglass;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Main activity.
 */
public class HelloGlassActivity extends Activity {
    private Card _card;
    private View _cardView;
    private TextView _statusTextView;

    private TextToSpeech _speech;
    private Context _context = this;
    private final SimpleDateFormat TF = new SimpleDateFormat("h:m a");

    private String getTime(){
        return TF.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String time = getTime();

        // Even though the text-to-speech engine is only used in response to a menu action, we
        // initialize it when the application starts so that we avoid delays that could occur
        // if we waited until it was needed to start it up.
        _speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speak("Hello Glass! The time is: " + time);
            }
        });

        setContentView(R.layout.layout_helloworld);
        _statusTextView = (TextView)findViewById(R.id.status);
        setText(time);
    }

    /**
    * Handle the tap event from the touchpad.
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            // swipes
            case KeyEvent.KEYCODE_TAB:
                if(event.isShiftPressed()){
                    speak("Swipe left");
                    setText("Swipe left");
                    setText(getTime(), 5);
                }
                else{
                    speak("Swipe right");
                    setText("Swipe right");
                    setText(getTime(), 5);
                }
                return true;
            // taps
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                audio.playSoundEffect(Sounds.TAP);

                _statusTextView.setText(R.string.touchpad_touched);
                speak("Touched");
                setText("Touched");
                setText(getTime(), 5);

                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private void speak(String text){
        _speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void setText(String text){
        _statusTextView.setText(text);
    }

    private void setText(final String text, int delay){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _statusTextView.setText(text);
            }
        }, delay * 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}