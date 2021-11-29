package tech.npwd.msnth.key;

import static tech.npwd.roots.Randomer.random;

import edu.csuci.nw068.snth.generate.ux.GenerationUX;
import tech.npwd.msnth.pattern.Sizes;

public class AutoKeySelect {

    private static final int[] keyNumbersBase = {65, 70, 75, 80, 85};
    private static final int randomOffsetMax = 5;

    private final String keysSelected;

    public AutoKeySelect(GenerationUX.GenerationUXInput input){
        this.keysSelected = selectKeys(input);
    }

    private String selectKeys(GenerationUX.GenerationUXInput input){
        StringBuilder selected = new StringBuilder();

        int keyRangeClass = input.getKeyRange().ordinal();
        int keyNumberBase = keyNumbersBase[keyRangeClass];

        final int numberOfNotes = Sizes.useSize(input.getSize().name());
        int numberOfKeys = Integer.MAX_VALUE;
        while(numberOfKeys > numberOfNotes)
            numberOfKeys = random(-1, randomOffsetMax) + 1;

        int key;
        boolean duplicate;
        for(int i = 0; i < numberOfKeys; i++) {
            key = keyNumberBase + random(-1, randomOffsetMax);
            duplicate = selected.toString().contains(String.valueOf(key));

            if(!duplicate)
                selected.append(key).append(" ");
            else
                i--;
        }

        return selected.toString();
    }

    public String getKeysSelected() {
        return keysSelected;
    }
}
