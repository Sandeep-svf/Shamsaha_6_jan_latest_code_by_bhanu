package com.shamsaha.app.preferences;

public class Singleton {

    boolean dialogDisplay = true;

    private  Singleton(){

    }

    public boolean isDialogDisplay() {
        return dialogDisplay;
    }

    public void setDialogDisplay(boolean dialogDisplay) {
        this.dialogDisplay = dialogDisplay;
    }

    private static Singleton singleton;

    public static Singleton get() {
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }
}
