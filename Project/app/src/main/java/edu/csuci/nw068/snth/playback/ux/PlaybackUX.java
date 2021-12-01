package edu.csuci.nw068.snth.playback.ux;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import edu.csuci.nw068.snth.R;
import edu.csuci.nw068.snth.generate.Generation;
import edu.csuci.nw068.snth.generate.ux.GenerationUX;

public class PlaybackUX extends Dialog {

    private final Generation generation;

    public PlaybackUX(@NonNull GenerationUX generationUX, Generation generation) {
        super(generationUX);
        this.generation = generation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.playback_ux);

        setupHandlers();
    }

    private void setupHandlers(){
        PlayLoopTriggerHandler playLoopTriggerHandler = new PlayLoopTriggerHandler();
        findViewById(R.id.loopPlayButton).setOnClickListener(playLoopTriggerHandler);

        PlayTriggerHandler playTriggerHandler = new PlayTriggerHandler(generation, playLoopTriggerHandler);
        findViewById(R.id.playButton).setOnClickListener(playTriggerHandler);
    }

    private static class PlayTriggerHandler implements View.OnClickListener {
        private final Generation generation;
        private final PlayLoopTriggerHandler playLoopTriggerHandler;

        private boolean threadLoopActive;

        private PlayTriggerHandler(Generation generation, PlayLoopTriggerHandler playLoopTriggerHandler){
            this.generation = generation;
            this.playLoopTriggerHandler = playLoopTriggerHandler;

            this.threadLoopActive = false;
        }

        @Override
        public void onClick(View v){
            if(!playLoopTriggerHandler.looped) {
                generation.playLoop();
            }
            else {
                if(!threadLoopActive) {
                    Thread loopingThread = new Thread(() -> {
                        while(playLoopTriggerHandler.looped)
                            generation.playLoop();
                        threadLoopActive = false;
                    });
                    loopingThread.start();
                    threadLoopActive = true;
                }
            }
        }
    }

    private static class PlayLoopTriggerHandler implements View.OnClickListener {

        private boolean looped;

        public PlayLoopTriggerHandler(){
            this.looped = false;
        }

        @Override
        public void onClick(View v){
            ImageButton loopButton = (ImageButton) v;
            if(!looped) {
                looped = true;
                loopButton.setColorFilter(Color.GREEN);
            }
            else {
                looped = false;
                loopButton.setColorFilter(Color.WHITE);
            }
        }
    }
}
