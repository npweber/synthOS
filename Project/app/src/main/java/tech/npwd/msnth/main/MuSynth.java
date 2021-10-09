package tech.npwd.msnth.main;

import cn.sherlock.com.sun.media.sound.SoftSynthesizer;

import edu.csuci.nw068.snth.generate.Generation;
import tech.npwd.roots.*;

import tech.npwd.msnth.generation.Generator;
import tech.npwd.msnth.pattern.Planner;
import tech.npwd.msnth.toning.*;

import jp.kshoji.javax.sound.midi.*;

import static tech.npwd.roots.Printer.*;
import static tech.npwd.roots.Sleeper.sleep;

import java.io.File;

//MuSynth: Main of MuSynth

public class MuSynth {

    private final static String version = "1_1";

    private static Argumenter argumenter;
    private final static String[] parameters = { "-keys", "-spd", "-sz", "-ord" };

    private final Grouper<Note> notes;

    //Program init
    public static void main(String[] args) {
        //Intake arguments
        intakeArgs(args);

        //Greet the user with the initialization msg
        greet();
    }

    //Main Constructor
    public MuSynth(){
        //Create pattern
        Planner pattern = new Planner();

        //Place notes onto pattern
        Placer formattedPattern = new Placer(pattern);

        //Get Note Sequence from Formatted Pattern
        notes = formattedPattern.createNoteSequence();
    }

    //Play the sequence until the user wants to continue
    public void play(){
        try {
            //Init custom synthesizer
            SoftSynthesizer synthesizer = new SoftSynthesizer();
            synthesizer.open();
            customizeSynthesizer(synthesizer);

            //Generate the note sequence
            print("BEGIN LOOP:\n-----------------------------------------");
            notes.forEach(note -> {
                //Generate the note
                new Generator(synthesizer, note.getKey(), note.getDuration(), notes.indexOf(note) + 1);
                //FIX: Sleep extra on the last note to prevent chop off
                if(notes.indexOf(note) == (notes.size() - 1)) {
                    sleep(notes.get(notes.size() - 1).getDuration());
                }
            });

        } catch (MidiUnavailableException e){
            printError("Error in MidiGeneration. Troubleshooting is necessary.", e);
        }
    }

    public void save(File file){
        Writer writer = new Writer(file);
        notes.forEach(note ->
            writer.write(note.getKey() + " " + note.getDuration() + "\n")
        );
        writer.finalizeFile();
    }

    //Customize Synthesizer to play a custom instrument
    private void customizeSynthesizer(SoftSynthesizer synthesizer){
        //Load Instrument
        synthesizer.loadAllInstruments(Generation.getSoundbank());

        //Log Instrument In-Use Readably
        print("Using Instrument \"" + synthesizer.getLoadedInstruments()[0].getName() + "\"\n");
        sleep(600);

        //Change to the instrument
        synthesizer.getChannels()[0].programChange(0);
        synthesizer.getChannels()[1].programChange(1);
    }

    //Greets the user upon the program init
    private static void greet() {
        print("Starting MuSynth v" + version + "...");
        sleep(600);
        print("Inputted Keys: " + argumenter.getValue("-keys"));
        sleep(600);
        print("Going by parameters: \n---------------------------------");
        sleep(400);
        print("Size: " + argumenter.getValue("-sz"));
        sleep(400);
        print("Speed: " + argumenter.getValue("-spd"));
        sleep(400);
        print("Order: " + argumenter.getValue("-ord") + "\n");
        sleep(600);
    }

    //Intake of program arguments
    private static void intakeArgs(String[] args) {
        //Intake from Argumenter
        argumenter = new Argumenter(new Grouper<String>() {{
            importFromArray(parameters);
        }});
        argumenter.intake(args);
    }

    public static Argumenter argumenter(){ return argumenter; }
}