package com.ascariandrea.moai.interfaces;

import com.ascariandrea.moai.models.Model;

/**
 * Created by andreaascari on 09/07/14.
 */
public interface OnFetchManyResourceInterface {

    void addResourceToFetch(Class<? extends Model> model);
}
