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

public class MenuFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_menu,container,false);
        initialUI();
        return contentView;
    }

    @Override
    protected void initialUI() {
    actionBar = contentView.findViewById(R.id.acitonBar_menu);
    initialActionBar(0,"菜谱大全");
    }
}
