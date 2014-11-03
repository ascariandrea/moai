package com.ascariandrea.moai.interfaces;


import com.ascariandrea.moai.client.AsyncCollectionHandler;
import com.ascariandrea.moai.models.Model;

import java.util.List;

/**
 * Created by andreaascari on 03/02/14.
 */
public interface OnFetchCollectionInterface<T extends Model> {

    public void fetchData(AsyncCollectionHandler<T> asyncCollectionHandler);

    public void fetchCompleted(boolean completed);

    public void populateView(List<T> list);
}
