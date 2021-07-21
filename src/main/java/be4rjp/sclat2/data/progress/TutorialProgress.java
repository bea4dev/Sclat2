package be4rjp.sclat2.data.progress;

public enum TutorialProgress {
    PLAY_TUTORIAL(0),
    FINISHED_TUTORIAL(1);

    private final int saveNumber;

    TutorialProgress(int saveNumber){
        this.saveNumber = saveNumber;
    }

    public int getSaveNumber() {return saveNumber;}

    public static TutorialProgress getBySaveNumber(int saveNumber){
        for(TutorialProgress tutorialProgress : TutorialProgress.values()){
            if(tutorialProgress.getSaveNumber() == saveNumber) return tutorialProgress;
        }
        return null;
    }
}
