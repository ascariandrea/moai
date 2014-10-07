package com.ascariandrea.afw.interfaces;

import com.ascariandrea.afw.models.Model;

/**
 * Created by andreaascari on 09/07/14.
 */
public interface OnFetchManyResourceInterface {

    void addResourceToFetch(Class<? extends Model> model);
}
