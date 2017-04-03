package com.developer.gavine.ebooks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class EbookAsyncTaskLoader extends AsyncTaskLoader<List<Ebooks>> {

    private String mUrl;

    public EbookAsyncTaskLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Ebooks> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        List<Ebooks> ebooks = QueryUtils.fetchEbooksData(mUrl);
        return ebooks;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
