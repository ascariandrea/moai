package com.ascariandrea.afw.interfaces;

import com.ascariandrea.afw.models.Model;

import java.util.List;

/**
 * Created by andreaascari on 04/07/14.
 */
public interface ModelCollectorInterface<T extends Model> {

    List<T> getAll();
}
