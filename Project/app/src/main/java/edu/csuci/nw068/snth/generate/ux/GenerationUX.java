package edu.csuci.nw068.snth.generate.ux;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.csuci.nw068.snth.R;

import edu.csuci.nw068.snth.generate.Generation;
import edu.csuci.nw068.snth.playback.ux.PlaybackUX;
import tech.npwd.msnth.pattern.Durations;
import tech.npwd.msnth.pattern.Sizes;
import tech.npwd.msnth.toning.Ordering;
import tech.npwd.msnth.key.KeyRanges;

public class GenerationUX extends AppCompatActivity {

    private SeekBar sizeSlider;
    private SeekBar speedSlider;
    private SeekBar keysSlider;
    private Spinner orderingSpinner;

    private String synthesizerSpinnerState;
    private int sizeSliderState;
    private int speedSliderState;
    private int keysSliderState;
    private String orderingSpinnerState;

    private boolean userIsInteracting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generation_ux);

        this.sizeSlider = findViewById(R.id.sizeSlider);
        this.speedSlider = findViewById(R.id.speedSlider);
        this.keysSlider = findViewById(R.id.keySlider);
        this.orderingSpinner = findViewById(R.id.orderingSelect);

        setupHandlers();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void setupHandlers(){
        SliderToastHandler sliderToastHandler = new SliderToastHandler();
        this.sizeSlider.setOnSeekBarChangeListener(sliderToastHandler);
        this.speedSlider.setOnSeekBarChangeListener(sliderToastHandler);
        this.keysSlider.setOnSeekBarChangeListener(sliderToastHandler);

        SpinnerToastHandler orderSpinnerHandler = new SpinnerToastHandler();
        this.orderingSpinner.setOnItemSelectedListener(orderSpinnerHandler);
        this.userIsInteracting = false;

        GenerationTriggerHandler generationTriggerHandler = new GenerationTriggerHandler(this);
        findViewById(R.id.generateTrigger).setOnClickListener(generationTriggerHandler);
    }

    public GenerationUXInput getInputs(){
        Spinner synthesizerSpinner = findViewById(R.id.synthesizerSelect);
        synthesizerSpinnerState = (String) synthesizerSpinner.getSelectedItem();

        sizeSliderState = sizeSlider.getProgress();
        speedSliderState = speedSlider.getProgress();
        keysSliderState = keysSlider.getProgress();
        orderingSpinnerState = (String) orderingSpinner.getSelectedItem();

        return new GenerationUXInput();
    }

    public class GenerationUXInput {
        private final String synthesizer;
        private final Sizes size;
        private final Durations speed;
        private final KeyRanges keyRange;
        private final Ordering ordering;

        private GenerationUXInput(){
            this.synthesizer = synthesizerSpinnerState;
            this.size = Sizes.values()[sizeSliderState];
            this.speed = Durations.values()[speedSliderState];
            this.keyRange = KeyRanges.values()[keysSliderState];
            this.ordering = Ordering.valueOf(orderingSpinnerState.toUpperCase());
        }

        public String getSynthesizer() {
            return synthesizer;
        }

        public Sizes getSize() {
            return size;
        }

        public Durations getSpeed() {
            return speed;
        }

        public Ordering getOrdering() {
            return ordering;
        }

        public KeyRanges getKeyRange() {
            return keyRange;
        }
    }

    private class SliderToastHandler implements SeekBar.OnSeekBarChangeListener {

        private static final String sizeToast = "%s Notes in Loop";
        private static final String speedToast = "%s Tempo";
        private static final String keysToast = "%s Key Range to Generate From";

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            GenerationUXInput currentInputs = getInputs();
            String toastString;
            switch (seekBar.getId()){
                case R.id.sizeSlider: {
                    toastString = sizeToast;
                    String sizeName = currentInputs.size.name();
                    toastString = String.format(toastString, sizeName);
                    break;
                }
                case R.id.speedSlider: {
                    toastString = speedToast;
                    toastString = String.format(toastString, currentInputs.speed);
                    break;
                }
                case R.id.keySlider: {
                    toastString = keysToast;
                    toastString = String.format(toastString, currentInputs.keyRange);
                    break;
                }
                default: toastString = "";
            }
            toastString = toastString.replaceAll("_", " ");

            Toast toast = Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private class SpinnerToastHandler implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(userIsInteracting) {
                GenerationUXInput currentInputs = getInputs();
                final String toastString = "Notes played in " + currentInputs.ordering.name() + " order";
                Toast toast = Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT);

                toast.show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private static class GenerationTriggerHandler implements View.OnClickListener {

        private final GenerationUX generationUX;

        private GenerationTriggerHandler(GenerationUX generationUX){
            this.generationUX = generationUX;
        }

        @Override
        public void onClick(View v) {
            Generation generation = new Generation(generationUX);
            generation.generateLoop();

            PlaybackUX playbackDialog = new PlaybackUX(generationUX, generation);
            playbackDialog.show();
        }
    }
}