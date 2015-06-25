package com.reelyactive.model;

public class AdvHeader {

    private String type = "";
    private long length = 0;
    private String txAdd = "random";
    private String rxAdd = "public";

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
     * @return The length
     */
    public Long getLength() {
        return length;
    }

    /**
     * @param length The length
     */
    public void setLength(Long length) {
        this.length = length;
    }

    /**
     * @return The txAdd
     */
    public String getTxAdd() {
        return txAdd;
    }

    /**
     * @param txAdd The txAdd
     */
    public void setTxAdd(String txAdd) {
        this.txAdd = txAdd;
    }

    /**
     * @return The rxAdd
     */
    public String getRxAdd() {
        return rxAdd;
    }

    /**
     * @param rxAdd The rxAdd
     */
    public void setRxAdd(String rxAdd) {
        this.rxAdd = rxAdd;
    }


}
