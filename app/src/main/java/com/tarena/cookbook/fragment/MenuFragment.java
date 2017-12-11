package com.tarena.cookbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookListAdapter;
import com.tarena.cookbook.entity.SortTagInfo;
import com.tarena.cookbook.view.IndexView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class MenuFragment extends Fragment {

    @BindView(R.id.tv_classify_title)
    TextView tvTitle;
    @BindView(R.id.lv_classify)
    ListView lvClassify;
    @BindView(R.id.tv_classify_toast)
    TextView tvToast;
    @BindView(R.id.index_view)
    IndexView indexView;
    Unbinder unbinder;

    private SortTagInfo sortTagInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_menu, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        initData();
        setListener();
        return contentView;
    }

    private void setListener() {
        CookListAdapter adapter = new CookListAdapter(getActivity(), sortTagInfo);
        Log.i("CookBook","----------");
        lvClassify.setAdapter(adapter);
        indexView.setOnLetterChangeListener(new IndexView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(int selectedIndex) {
                //
                lvClassify.setSelection(selectedIndex - 1);
                tvToast.setText(sortTagInfo.getResult().get(selectedIndex - 1).getName());//设置中间显示的字
                tvToast.setVisibility(View.VISIBLE);//设置为可见
            }

            @Override
            public void onClickUp() {
                tvToast.setVisibility(View.GONE);//当放开时，设置为不可见
            }
        });
        lvClassify.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View v = view.getChildAt(0);
                if (v == null) {
                    return;
                }
                int dex = v.getBottom() - tvTitle.getHeight();
                //                if (dex <= 0) {
                //                    params.topMargin = dex;
                //                } else {
                //                    params.topMargin = 0;
                //                }
                //Log.i("myout","firstVisibleItem = " + firstVisibleItem);
                tvTitle.setText(sortTagInfo.getResult().get(firstVisibleItem).getName());
                //viewTop.setLayoutParams(params);
                indexView.setSelected(firstVisibleItem + 1);
            }
        });

    }

    private void initData() {
        Gson gson = new Gson();
        try {
            InputStream in = getActivity().getAssets().open("cooks_classify");
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String len;
            while ((len = reader.readLine()) != null) {
                builder.append(len);
            }
            reader.close();
            sortTagInfo = gson.fromJson(builder.toString(), SortTagInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
