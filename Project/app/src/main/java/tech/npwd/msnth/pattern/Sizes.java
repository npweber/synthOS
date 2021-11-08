package tech.npwd.msnth.pattern;

//Sizes: Defined amounts of notes to play in the loop
//(Pre-defined values to account for ALL lengths of loops)

public enum Sizes {

    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10);

    private final int size;

    Sizes(int size){ this.size = size; }

    //Use the specified size
    public static int useSize(String sizeType){ return valueOf(sizeType).size; }
}
