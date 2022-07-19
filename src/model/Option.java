package model;

import controller.Action;

public class Option {
    final private String label;
    final private Action action;

    public Option(String label, Action action){
        this.label = label;
        this.action = action;
    }

    public String getLabel(){
        return this.label;
    }

    public Action getAction(){
        return this.action;
    }
}
