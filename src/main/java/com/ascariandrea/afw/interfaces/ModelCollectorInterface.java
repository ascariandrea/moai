package com.ascariandrea.afw.interfaces;

import com.ascariandrea.afw.models.BaseModel;

import java.util.List;

/**
 * Created by andreaascari on 04/07/14.
 */
public interface ModelCollectorInterface<T extends BaseModel> {

    List<T> getAll();
}
