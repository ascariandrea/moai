package com.seventydivision.framework.interfaces;

import com.seventydivision.framework.models.BaseModel;

import java.util.List;

/**
 * Created by andreaascari on 04/07/14.
 */
public interface ModelCollectorInterface<T extends BaseModel> {

    List<T> getAll();
}
