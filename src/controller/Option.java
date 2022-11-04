package controller;

public class Option {
    final private String label;
    final private Handler handler;

    public Option(String label, Handler handler){
        this.label = label;
        this.handler = handler;
    }

    public String getLabel(){
        return this.label;
    }

    public Handler getAction(){
        return this.handler;
    }
}
