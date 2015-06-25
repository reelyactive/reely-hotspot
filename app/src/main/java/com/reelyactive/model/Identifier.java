package com.reelyactive.model;

public class Identifier {

    private String type = "";
    private String value = "";
    private AdvHeader advHeader = new AdvHeader();
    private AdvData advData = new AdvData();

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return The advHeader
     */
    public AdvHeader getAdvHeader() {
        return advHeader;
    }

    /**
     * @param advHeader The advHeader
     */
    public void setAdvHeader(AdvHeader advHeader) {
        this.advHeader = advHeader;
    }

    /**
     * @return The advData
     */
    public AdvData getAdvData() {
        return advData;
    }

    /**
     * @param advData The advData
     */
    public void setAdvData(AdvData advData) {
        this.advData = advData;
    }

}
