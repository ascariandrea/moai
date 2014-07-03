package com.seventydivision.framework.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by andreaascari on 01/07/14.
 */

public interface ModelNamingInterface {
    public String SINGLE_NAME = null;

    @JsonIgnore public String getSingleName();
    @JsonIgnore public String getPluralName();

}
