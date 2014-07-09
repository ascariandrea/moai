package com.seventydivision.framework.interfaces;

import com.seventydivision.framework.models.BaseModel;

/**
 * Created by andreaascari on 09/07/14.
 */
public interface OnFetchManyResourceInterface {

    void addResourceToFetch(Class<? extends BaseModel> model);
}
