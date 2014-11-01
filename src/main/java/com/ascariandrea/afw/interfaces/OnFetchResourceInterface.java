package com.ascariandrea.afw.interfaces;


import com.ascariandrea.afw.client.AsyncResourceHandler;
import com.ascariandrea.afw.models.Model;


/**
 * Created by andreaascari on 03/02/14.
 */
public interface OnFetchResourceInterface<T extends Model> {

    public void fetchData(AsyncResourceHandler<T> asyncResourceHandler);

    public void fetchCompleted(boolean completed);

    public void populateView(T res);

    public boolean isFetching();
}
