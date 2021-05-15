package tech.npwd.msnth.toning;

import tech.npwd.roots.*;

import java.util.*;

import static tech.npwd.msnth.main.MuSynth.argumenter;
import static tech.npwd.roots.Printer.print;
import static tech.npwd.roots.Randomer.random;
import static tech.npwd.msnth.toning.Ordering.*;

//Placer: Takes generated beat/pattern and places randomly the notes onto the set of notes to play in the beat

public class Placer {

    //Key variables in Placing procedure
    private final Grouper<Integer> noteInput;
    private final Grouper<Integer> patternInput;

    private final HashMap<Integer, Grouper<Integer>> formattedPattern;

    public Placer(Grouper<Integer> pattern){
        //Grab input
        //Notes from program 'args[]'
        this.noteInput = new Grouper<>();
        argumenter().getArrayValue("-keys").forEach(note -> noteInput.add(Integer.parseInt(note)));
        //Already exported pattern
        this.patternInput = pattern;

        //Start Placing Process
        this.formattedPattern = new HashMap<>();
        patternToNote();
    }

    //Decide where to place each note
    private void patternToNote(){
        //Declare weights
        int[] weights = distributeWeights();

        //Now that the weights have been distributed, randomize the duration of each beat for the note with the weights specified
        noteInput.forEach(note ->
            //For each inputted note, put the note and a set of randomly picked durations, for as many times as its weight specifies
            formattedPattern.put(note, new Grouper<Integer>(){{
                for(int i = 0; i < weights[noteInput.indexOf(note)]; i++)
                    add(patternInput.get(random(-1, patternInput.size())));
            }})
        );
    }

    //Take formattedPattern and create a Note Sequence that is ordered by Ordering Profile
    public Grouper<Note> createNoteSequence() {
        //Establish Note Grouper
        Grouper<Note> noteSequence = new Grouper<>();

        //Only use one note if only given such
        if(noteInput.size() == 1)
            formattedPattern.get(noteInput.get(0)).forEach(duration -> noteSequence.add(new Note(noteInput.get(0), duration)));
        //Else, order and create sequence
        else {
            //Order given input to musically pattern (Ascending or Descending)
            orderNotes(valueOf(argumenter().getValue("-ord")), noteSequence);

            //Integrate Variance into the Sequence, effectively making it realistically synthetic
            integrateVariance(noteSequence);
        }
        return noteSequence;
    }

    //Start the weight distributing process
    //(Decide how many notes to place on each beat in the established pattern)
    private int[] distributeWeights(){
        //If only one note is inputted, make only a single full weight
        if(noteInput.size() == 1)
            return new int[]{ patternInput.size() };
        //Otherwise randomize the distribution
        else {
            //Control variable in while loop
            int distributionSum = 0;

            //Declare weight variable
            int[] distributionWeights = new int[noteInput.size()];

            //Use a while loop to generate distribution weights (ranging from 1 to the maximum) for each note
            int controlCount = 0;
            while (distributionSum == 0) {
                //Generate weights & add them up to the distribution sum
                for (int i = 0; i < noteInput.size(); i++) {
                    distributionWeights[i] = random(0, patternInput.size() + 1);
                    distributionSum += distributionWeights[i];
                }

                //If the sum doesn't match the pattern size, invalidate it so the loop runs again
                if (distributionSum != patternInput.size())
                    distributionSum = 0;

                //Forcibly exit the program if the loop stack overflows and cannot stop
                controlCount++;
                if(controlCount > 1000000) {
                    print("Looping in note formatting process exceeded control loop count. Rerun the program.");
                    System.exit(2);
                }
            }
            return distributionWeights;
        }
    }

    private void orderNotes(Ordering ordering, Grouper<Note> noteSequence){
        //Sort the keys Ascending or Descending
        //Declare order int
        int order = 0;
        if(ordering == DESCENDING)
            order = 1;
        //Sort by order
        noteInput.sort(order);

        //Add notes of each key and their specified duration to the note sequence, from the key order just defined, omitting randomization
        noteInput.forEach(key -> formattedPattern.entrySet().forEach(pair -> {
            if (pair.getKey().equals(key))
                pair.getValue().forEach(duration -> noteSequence.add(new Note(key, duration)));
        }));
    }

    //Integrate variance to the musically ordered sequence in order for all the different possibilities of loops to be calculated
    private void integrateVariance(Grouper<Note> noteSequence){
        int controlCount = 0;
        while (true) {
            //Perform a "note swap": Swap two notes with each other in the loop
            //(This will continue until the boolean check is valid)
            int randomIndex = random(-1, noteSequence.size());
            int swapIndex = random(-1, noteSequence.size());
            Note randomNote = noteSequence.get(randomIndex);
            noteSequence.set(randomIndex, noteSequence.get(swapIndex));
            noteSequence.set(swapIndex, randomNote);

            //Boolean check to see if the swap is swapping duplicate keys
            boolean duplicate = noteSequence.get(randomIndex).getKey() == noteSequence.get(swapIndex).getKey();
            //Boolean check the loop for no sense of musical "direction"
            //(The last note has to be the last key in "keysOrdered" Grouper)
            boolean notDirected = noteSequence.get(noteSequence.size() - 1).getKey() != noteInput.get(noteInput.size() - 1);

            //Forcibly exit the program if the loop stack overflows and cannot stop
            controlCount++;
            if(controlCount > 1000000) {
                print("Looping in note sequencing process exceeded control loop count. Rerun the program.");
                System.exit(2);
            }

            //IF these conditions do NOT exist, the variance is added and END While Loop
            if (!(duplicate || notDirected))
                break;
        }
    }
}
