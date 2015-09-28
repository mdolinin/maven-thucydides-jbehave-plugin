package net.thucydides.maven.plugin;

import java.util.ArrayList;
import java.util.List;

public class SimpleWithList {

    protected List<Simple> simpleList;

    public List<Simple> getSimpleList() {
        if (simpleList == null) {
            simpleList = new ArrayList<Simple>();
        }
        return this.simpleList;
    }

}
