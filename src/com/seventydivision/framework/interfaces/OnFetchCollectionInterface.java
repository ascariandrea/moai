package com.seventydivision.framework.interfaces;


import com.seventydivision.framework.client.AsyncCollectionHandler;
import com.seventydivision.framework.models.BaseModel;

import java.util.List;

/**
 * Created by andreaascari on 03/02/14.
 */
public interface OnFetchCollectionInterface<T extends BaseModel> {
    //public void initHandler();

    public void fetchData(AsyncCollectionHandler<T> asyncCollectionHandler );

    public void fetchCompleted(boolean completed);

    public void populateView(List<T> list);
}
