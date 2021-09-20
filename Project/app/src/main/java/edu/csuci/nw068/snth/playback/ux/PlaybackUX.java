package edu.csuci.nw068.snth.playback.ux;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;

import edu.csuci.nw068.snth.R;
import edu.csuci.nw068.snth.generate.Generation;
import edu.csuci.nw068.snth.generate.ux.GenerationUX;

public class PlaybackUX extends Dialog {

    private final Generation generation;
    private final GenerationUX generationUX;

    public PlaybackUX(@NonNull GenerationUX generationUX, Generation generation) {
        super(generationUX);
        this.generation = generation;
        this.generationUX = generationUX;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.playback_ux);

        setupHandlers();
    }

    private void setupHandlers(){
        PlayTriggerHandler playTriggerHandler = new PlayTriggerHandler(generation);
        findViewById(R.id.playButton).setOnClickListener(playTriggerHandler);

        ShareTriggerHandler shareTriggerHandler = new ShareTriggerHandler();
        findViewById(R.id.shareButton).setOnClickListener(shareTriggerHandler);

        SaveTriggerHandler saveTriggerHandler = new SaveTriggerHandler(generation, generationUX);
        findViewById(R.id.saveButton).setOnClickListener(saveTriggerHandler);
    }

    private static class PlayTriggerHandler implements View.OnClickListener {
        private final Generation generation;

        private PlayTriggerHandler(Generation generation){
            this.generation = generation;
        }

        @Override
        public void onClick(View v){
            generation.playLoop();
        }
    }

    private static class ShareTriggerHandler implements View.OnClickListener {
        @Override
        public void onClick(View v){
            //TODO: Implement Loop Sharing
        }
    }

    private static class SaveTriggerHandler implements View.OnClickListener {
        private final Generation generation;
        private final GenerationUX generationUX;

        private SaveTriggerHandler(Generation generation, GenerationUX generationUX){
            this.generation = generation;
            this.generationUX = generationUX;
        }

        @Override
        public void onClick(View v){
            saveLoopToLibrary();

            int toastDuration = generationUX.getResources().getInteger(R.integer.toastDuration);
            Toast saveToast = Toast.makeText(generationUX, R.string.saveToLibraryToast, toastDuration);
            saveToast.show();
        }

        private void saveLoopToLibrary(){
            String tempPath = "androidFilePath";
            File loopMidiFile = new File(tempPath);
            //TODO: Enable when Library File Space Exists
            //generation.saveLoop(loopMidiFile);
        }
    }
}
