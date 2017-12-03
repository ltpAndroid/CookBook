package com.tarena.cookbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tarena.cookbook.R;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class ShareFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_share,container,false);
        initialUI();
        return contentView;
    }

    @Override
    protected void initialUI() {
    actionBar = contentView.findViewById(R.id.acitonBar_share);
    initialActionBar(0,"邻里厨房");
    }
}
