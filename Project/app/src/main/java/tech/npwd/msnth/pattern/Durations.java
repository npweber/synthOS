package tech.npwd.msnth.pattern;

//Durations: Defined intervals of note durations to be used when specifying how long a note should play
//(Pre-defined to generate ALL types of patterns)

public enum Durations {

    SLOW(new int[]{ 300, 350, 400 }),
    MODERATE(new int[]{ 250, 275, 300 }),
    MEDIUM_FAST(new int[]{ 175, 200, 225 }),
    FAST(new int[]{ 100, 150, 175 });

    private final int[] durationSelection;

    Durations(int[] durationSelection){
        this.durationSelection = durationSelection;
    }

    //Use the specified duration
    public static int[] useDuration(String durationType){ return valueOf(durationType).durationSelection; }
}
