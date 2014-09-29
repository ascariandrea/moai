package com.ascariandrea.afw.interfaces;

import com.ascariandrea.afw.models.BaseModel;

/**
 * Created by andreaascari on 09/07/14.
 */
public interface OnFetchManyResourceInterface {

    void addResourceToFetch(Class<? extends BaseModel> model);
}
