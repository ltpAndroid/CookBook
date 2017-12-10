package com.tarena.cookbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tarena.cookbook.R;
import com.tarena.cookbook.view.IndexView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class MenuFragment extends BaseFragment {

    @BindView(R.id.tv_classify_title)
    TextView tvTitle;
    @BindView(R.id.lv_classify)
    ListView lvClassify;
    @BindView(R.id.tv_classify_toast)
    TextView tvToast;
    @BindView(R.id.index_view)
    IndexView indexView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_menu, container, false);
        initialUI();
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    protected void initialUI() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
