package edu.csuci.nw068.snth.generate;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.csuci.nw068.snth.R;

import npwd.tech.msnth.pattern.Durations;
import npwd.tech.msnth.pattern.Sizes;
import npwd.tech.msnth.toning.Ordering;

public class GenerationUX extends AppCompatActivity {

    protected String synthesizerSpinnerState;
    protected int sizeSliderState;
    protected int speedSliderState;
    protected int keysSliderState;
    protected String orderingSpinnerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generation_ux);

        getInputs();
    }

    UXInput getInputs(){
        UXInput inputs;

        Spinner synthesizerSpinner = findViewById(R.id.synthesizerSelect);
        synthesizerSpinnerState = (String) synthesizerSpinner.getSelectedItem();
        SeekBar sizeSlider = findViewById(R.id.sizeSlider);
        sizeSliderState = sizeSlider.getProgress();
        SeekBar speedSlider = findViewById(R.id.speedSlider);
        speedSliderState = speedSlider.getProgress();
        SeekBar keysSlider = findViewById(R.id.keySlider);
        keysSliderState = keysSlider.getProgress();
        Spinner orderingSpinner = findViewById(R.id.orderingSelect);
        orderingSpinnerState = (String) orderingSpinner.getSelectedItem();

        inputs = new UXInput();
        System.out.println(inputs.ordering);
        return inputs;
    }

    class UXInput {
        final String synthesizer;
        final Sizes size;
        final Durations speed;
        final int keyRangeNumber;
        final Ordering ordering;

        UXInput(){
            this.synthesizer = synthesizerSpinnerState;

            this.size = Sizes.values()[sizeSliderState];

            int durationsIndex = indexAlignSpeedNumber(speedSliderState);
            this.speed = Durations.values()[durationsIndex];

            this.keyRangeNumber = keysSliderState;
            this.ordering = Ordering.valueOf(orderingSpinnerState.toUpperCase());
        }

        private int indexAlignSpeedNumber(int speedNumber){
            int lastIndex = Durations.values().length - 1;
            return lastIndex - speedNumber * 2;
        }
    }
}