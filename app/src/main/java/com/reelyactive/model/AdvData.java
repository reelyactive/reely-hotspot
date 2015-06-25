package com.reelyactive.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvData {

    private List<String> flags = new ArrayList<String>();
    private String complete128BitUUIDs = "";

    /**
     * @return The flags
     */
    public List<String> getFlags() {
        return flags;
    }

    /**
     * @param flags The flags
     */
    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

    /**
     * @return The complete128BitUUIDs
     */
    public String getComplete128BitUUIDs() {
        return complete128BitUUIDs;
    }

    /**
     * @param complete128BitUUIDs The complete128BitUUIDs
     */
    public void setComplete128BitUUIDs(String complete128BitUUIDs) {
        this.complete128BitUUIDs = complete128BitUUIDs;
    }

}
