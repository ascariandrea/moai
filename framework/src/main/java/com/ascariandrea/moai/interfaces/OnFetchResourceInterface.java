package com.ascariandrea.moai.interfaces;


import com.ascariandrea.moai.client.AsyncResourceHandler;
import com.ascariandrea.moai.models.Model;


/**
 * Created by andreaascari on 03/02/14.
 */
public interface OnFetchResourceInterface<T extends Model> {

    public void fetchData(AsyncResourceHandler<T> asyncResourceHandler);

    public void fetchCompleted(boolean completed);

    public void populateView(T res);

    public boolean isFetching();
}
