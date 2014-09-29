package com.ascariandrea.afw.models;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by andreaascari on 22/01/14.
 */

public class ModelCollection<T extends BaseModel> {
    private String wrap;
    private String TAG = ModelCollection.class.getName();

    public ModelCollection(String wrap) {
        this.wrap = wrap;
    }

    public List<T> fromJSONList(String json, Class<T> klass) {
        try {
            if (wrap != null) {
                json = new JSONObject(json).getJSONArray(wrap).toString();
            } else {
                json = new JSONArray(json).toString();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper.readValue(json,
                    mapper.getTypeFactory().constructCollectionType(List.class, klass));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
