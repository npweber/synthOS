package tech.npwd.msnth.key;

import java.util.Random;

import edu.csuci.nw068.snth.generate.ux.GenerationUX;

import tech.npwd.msnth.pattern.Sizes;

public class AutoKeySelect {

    private final String keysSelected;

    private final Random random;

    public AutoKeySelect(GenerationUX.GenerationUXInput input){
        this.random = new Random();
        this.keysSelected = selectKeys(input);
    }

    private String selectKeys(GenerationUX.GenerationUXInput input){
        StringBuilder selected = new StringBuilder();

        String sizeName = input.getSize().name();
        int maxNumberOfKeys = Sizes.useSize(sizeName);
        int numberOfKeys = random.nextInt(maxNumberOfKeys) + 1;

        final int[] keyNumbersBase = {65, 70, 75, 80, 85};
        int keyRangeClass = input.getKeyRange().ordinal();
        int randMax = 5;
        int keyNumberBase = keyNumbersBase[keyRangeClass];

        int rand;
        for(int i = 0; i < numberOfKeys; i++) {
            rand = random.nextInt(randMax);
            selected.append(keyNumberBase + rand).append(" ");
        }

        return selected.toString();
    }

    public String getKeysSelected() {
        return keysSelected;
    }
}
