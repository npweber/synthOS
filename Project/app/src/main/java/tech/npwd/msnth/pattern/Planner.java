package tech.npwd.msnth.pattern;

import tech.npwd.roots.*;

import static tech.npwd.msnth.main.MuSynth.argumenter;
import static tech.npwd.roots.Randomer.random;
import static tech.npwd.msnth.pattern.Durations.useDuration;

//Planner: Establisher and Holder of the Loop Pattern

public class Planner extends Grouper<Integer>{

    //Use the users choice to specify how many beats will be played
    private final int loopSize = Sizes.useSize(argumenter().getValue("-sz"));

    //Use the users choice of "noteDurations" to specify the tempo of the Loop Pattern
    private final int[] durationSelection = useDuration(argumenter().getValue("-spd"));

    //Create Loop Pattern
    public Planner(){
        //Establish a pattern by selecting the durations at random for the amount of notes there are to play
        for (int i = 1; i <= this.loopSize; i++)
            add(durationSelection[random(-1, durationSelection.length)]);
    }
}
