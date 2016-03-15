package com.reelyactive.model;

/**
 * Created by saiimons on 16-03-03.
 */
public class Event {
    private String event = "appearance";
    private Tiraid tiraid = new Tiraid();

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Tiraid getTiraid() {
        return tiraid;
    }

    public void setTiraid(Tiraid tiraid) {
        this.tiraid = tiraid;
    }
}
