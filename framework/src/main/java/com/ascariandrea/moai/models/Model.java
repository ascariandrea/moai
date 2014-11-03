package com.ascariandrea.moai.models;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by andreaascari on 22/01/14.
 */
public abstract class Model {
    private static final String TAG = Model.class.getSimpleName();
    public static String SINGLE_NAME = "Model";
    public static String PLURAL_NAME = "Models";
    protected static ObjectMapper mapper;

    protected static <T extends Model> void checkInitMapper(T model) {
        if (mapper == null) {
            mapper = new ObjectMapper();
            for (DeserializationFeature ds : model.getDisabledDeserializationFeatures())
                mapper.disable(ds);

            for (DeserializationFeature ds : model.getEnabledDeserializationFeatures())
                mapper.enable(ds);

            for (SerializationFeature sf : model.getDisabledSerializationFeatures())
                mapper.disable(sf);

            for (SerializationFeature sf : model.getEnabledSerializationFeatures())
                mapper.enable(sf);
        }
    }

    public static <T extends Model> ObjectMapper getMapper(T model) {
        checkInitMapper(model);
        return mapper;
    }

    public static <T extends Model> T fromJSON(String json, Class<T> model) {
        T instance = null;

        try {
            instance = model.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        checkInitMapper(instance);

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

    protected java.util.List<SerializationFeature> getDisabledSerializationFeatures() {
        return Arrays.asList();
    }

    protected java.util.List<SerializationFeature> getEnabledSerializationFeatures() {
        return Arrays.asList(SerializationFeature.WRAP_ROOT_VALUE);
    }

    protected java.util.List<DeserializationFeature> getDisabledDeserializationFeatures() {
        return Arrays.asList();
    }

    protected java.util.List<DeserializationFeature> getEnabledDeserializationFeatures() {
        return Arrays.asList(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public String toJSON() {
        checkInitMapper(this);
        String ret = "{}";

        try {
            ret = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ret;
    }

}
