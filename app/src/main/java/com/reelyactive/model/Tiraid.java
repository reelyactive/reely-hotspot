package com.reelyactive.model;

import java.util.ArrayList;
import java.util.List;

public class Tiraid {

    private Adva48Identifier identifier = new Adva48Identifier();
    private String timestamp = "";
    private List<RadioDecoding> radioDecodings = new ArrayList<RadioDecoding>();

    /**
     * @return The identifier
     */
    public Adva48Identifier getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier The identifier
     */
    public void setIdentifier(Adva48Identifier identifier) {
        this.identifier = identifier;
    }

    /**
     * @return The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return The radioDecodings
     */
    public List<RadioDecoding> getRadioDecodings() {
        return radioDecodings;
    }

    /**
     * @param radioDecodings The radioDecodings
     */
    public void setRadioDecodings(List<RadioDecoding> radioDecodings) {
        this.radioDecodings = radioDecodings;
    }

}
