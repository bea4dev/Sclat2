package be4rjp.sclat2.language;

public enum Lang {
    ja_JP(0),
    en_US(1);
    
    //SQLに保存する時の識別番号
    private final int saveNumber;
    
    Lang(int saveNumber){
        this.saveNumber = saveNumber;
    }
    
    public int getSaveNumber() {return saveNumber;}
    
    
    public static Lang getLangByID(int id){
        for(Lang lang : Lang.values()){
            if(lang.getSaveNumber() == id) return lang;
        }
        return null;
    }
}
