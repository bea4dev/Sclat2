package be4rjp.sclat2.data.progress;

public class ProgressData {

    private TutorialProgress tutorialProgress = TutorialProgress.PLAY_TUTORIAL;

    public TutorialProgress getTutorialProgress() {return tutorialProgress;}

    public int getCombinedID(){
        int data = 0;

        data |= (tutorialProgress.getSaveNumber() & 0xF);

        return data;
    }

    public void setByCombinedID(int data){
        tutorialProgress = TutorialProgress.getBySaveNumber(data & 0xF);
    }

}
