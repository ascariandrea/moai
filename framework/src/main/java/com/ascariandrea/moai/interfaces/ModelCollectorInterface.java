package com.ascariandrea.moai.interfaces;

import com.ascariandrea.moai.models.Model;

import java.util.List;

/**
 * Created by andreaascari on 04/07/14.
 */
public interface ModelCollectorInterface<T extends Model> {

    List<T> getAll();
}
