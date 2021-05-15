package edu.csuci.nw068.snth.generate;

import java.io.IOException;
import java.io.InputStream;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;

import edu.csuci.nw068.snth.generate.ux.GenerationUX;
import tech.npwd.msnth.main.MuSynth;
import tech.npwd.msnth.key.AutoKeySelect;
import tech.npwd.roots.Grouper;

public class Generation {

    private static SF2Soundbank soundbank;

    private final GenerationUX.GenerationUXInput inputs;

    public Generation(GenerationUX generationUX){
        this.inputs = generationUX.getInputs();

        soundbank = selectSoundbank(generationUX);
    }

    private SF2Soundbank selectSoundbank(GenerationUX generationUX) {
        StringBuilder inputSynthName = new StringBuilder(inputs.getSynthesizer());
        inputSynthName.append(".sf2");

        SF2Soundbank soundbank;
        try {
            InputStream sf2File = generationUX.getAssets().open(inputSynthName.toString());
            soundbank = new SF2Soundbank(sf2File);
        } catch (IOException e){
            soundbank = null;
            e.printStackTrace();
        }

        return soundbank;
    }

    public void generateLoop(){
        Grouper<String> argsGrouper = new Grouper<>();
        argsGrouper.add("-keys");

        AutoKeySelect autoKeySelect = new AutoKeySelect(inputs.getKeyRange());
        argsGrouper.add(autoKeySelect.getKeysSelected());

        String otherArgs = "-spd " +
                inputs.getSpeed() +
                " -sz " + inputs.getSize() +
                " -ord " + inputs.getOrdering();

        argsGrouper.addFromArray(otherArgs.split(" "));

        String[] args = new String[argsGrouper.size()];
        for(int i = 0; i < args.length; i++)
            args[i] = argsGrouper.get(i);

        MuSynth.main(args);
    }

    public static SF2Soundbank getSoundbank() {
        return soundbank;
    }
}
