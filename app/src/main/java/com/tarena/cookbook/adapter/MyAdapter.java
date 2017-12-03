package com.tarena.cookbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;


public abstract class MyAdapter<T> extends android.widget.BaseAdapter {

    private Context context;
    private List<T> data;
    private LayoutInflater layoutInflater;


    public MyAdapter(Context context, List<T> data) {
        super();
        setContext(context);
        setData(data);
        this.layoutInflater = LayoutInflater.from(context);
    }


    protected final Context getContext() {
        return context;
    }


    protected final void setContext(Context context) {
        if (null == context) {
            throw new IllegalArgumentException("context cannot be null");
        }
        this.context = context;
    }


    protected final List<T> getData() {

        return data;
    }


    protected final void setData(List<T> data) {
        if (null == data) {
            data = new ArrayList<T>();
        }
        this.data = data;
    }


    protected final LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
