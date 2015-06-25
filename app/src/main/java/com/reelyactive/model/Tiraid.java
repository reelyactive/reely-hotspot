package com.reelyactive.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tiraid {

    private Identifier identifier = new Identifier();
    private String timestamp = "";
    private List<RadioDecoding> radioDecodings = new ArrayList<RadioDecoding>();

    /**
     * @return The identifier
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier The identifier
     */
    public void setIdentifier(Identifier identifier) {
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
