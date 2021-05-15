package tech.npwd.msnth.generation;

import jp.kshoji.javax.sound.midi.Synthesizer;

import static tech.npwd.roots.Printer.print;
import static tech.npwd.roots.Sleeper.sleep;

//Generator: Generates notes on demand

public class Generator {

    //Generates notes given a synthesizer, note, duration, and a note index
    public Generator(Synthesizer midiSynthesizer, int note, int duration, int noteIndex){
        //Log the note for verbose
        print("Note #" + noteIndex + ": Key (" + note + "), Duration (" + duration + ")");
        //Generate the note with the given synthesizer
        midiSynthesizer.getChannels()[0].noteOn(note, 100);
        //Measure the notes's play duration by letting it play while sleeping the time it should play
        sleep(duration);
        //Turn the note off after its play duration is over
        midiSynthesizer.getChannels()[0].noteOff(note, 100);
    }
}
