package tech.npwd.msnth.toning;

//Note: Object for declaring notes to play

public class Note {

    //Properties of the note
    private final int key;
    private final int duration;

    public Note(int key, int duration){
        this.key = key;
        this.duration = duration;
    }

    public int getKey() {
        return key;
    }
    public int getDuration() {
        return duration;
    }
}
