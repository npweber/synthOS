package tech.npwd.msnth.key;

public class AutoKeySelect {

    private final String keysSelected;

    public AutoKeySelect(KeyRanges keyRange){
        this.keysSelected = selectKeys(keyRange);
    }

    private String selectKeys(KeyRanges keyRanges){
        String selected = "68 74 79";
        return selected;
    }

    public String getKeysSelected() {
        return keysSelected;
    }
}
