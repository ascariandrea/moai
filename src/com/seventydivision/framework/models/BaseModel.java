package com.seventydivision.framework.models;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


import java.io.IOException;

/**
 * Created by andreaascari on 22/01/14.
 */
public abstract class BaseModel {
    private static final String TAG = BaseModel.class.getSimpleName();
    protected static ObjectMapper mapper;

    public static String SINGLE_NAME = "baseModel";
    public static String PLURAL_NAME = "baseModels";

    protected static void checkInitMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        }
    }

    public static ObjectMapper getMapper() {
        checkInitMapper();
        return mapper;
    }


    public String toJSON() {
        checkInitMapper();
        String ret = "{}";

        try {
            ret = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static <T> T fromJSON(String json, Class<T> model) {
        checkInitMapper();
        T instance = null;

        try {
            instance = model.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            instance = mapper.readValue(json, model);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public static String getSingleName() {
        return SINGLE_NAME;
    }

    public static String getPluralName() {
        return PLURAL_NAME;
    }

}
