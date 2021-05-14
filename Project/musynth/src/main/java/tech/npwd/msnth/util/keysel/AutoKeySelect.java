package tech.npwd.msnth.util.keysel;

import tech.npwd.roots.Grouper;

public class AutoKeySelect {

    private final Grouper<Integer> keysSelected;

    public AutoKeySelect(KeyRanges keyRange){
        this.keysSelected = selectKeys(keyRange);
    }

    private Grouper<Integer> selectKeys(KeyRanges keyRanges){
        Grouper<Integer> selected = new Grouper<>();
        selected.addFromArray(new Integer[]{71, 73, 74});
        return selected;
    }

    public Grouper<Integer> getKeysSelected() {
        return keysSelected;
    }
}
