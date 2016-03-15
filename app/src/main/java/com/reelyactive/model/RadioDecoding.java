package com.reelyactive.model;

public class RadioDecoding {
    private long rssi = 0;
    private Identifier identifier = new Eui64Identifier();

    /**
     * @return The rssi
     */
    public Long getRssi() {
        return rssi;
    }

    /**
     * @param rssi The rssi
     */
    public void setRssi(Long rssi) {
        this.rssi = rssi;
    }

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

}
