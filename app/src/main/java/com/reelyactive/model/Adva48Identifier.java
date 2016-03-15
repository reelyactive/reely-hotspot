package com.reelyactive.model;

/**
 * Created by saiimons on 16-03-03.
 */
public class Adva48Identifier extends Identifier {

    private AdvHeader advHeader = new AdvHeader();
    private AdvData advData = new AdvData();

    public Adva48Identifier(){
        setType("ADVA-48");
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
