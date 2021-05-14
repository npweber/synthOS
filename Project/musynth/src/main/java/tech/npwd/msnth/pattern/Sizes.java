package tech.npwd.msnth.pattern;

//Sizes: Defined amounts of notes to play in the loop
//(Pre-defined values to account for ALL lengths of loops)

public enum Sizes {

    SMALL(3),
    MEDIUM_SMALL(4),
    MEDIUM(5),
    LARGE(6);

    private final int size;

    Sizes(int size){ this.size = size; }

    //Use the specified size
    public static int useSize(String sizeType){ return valueOf(sizeType).size; }
}
