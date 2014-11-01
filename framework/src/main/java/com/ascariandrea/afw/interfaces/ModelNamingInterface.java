package com.ascariandrea.afw.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by andreaascari on 01/07/14.
 */

public interface ModelNamingInterface {
    public String SINGLE_NAME = null;

    @JsonIgnore public String getSingleName();
    @JsonIgnore public String getPluralName();

}
