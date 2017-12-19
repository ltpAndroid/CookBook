package com.tarena.cookbook.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class MainCookAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {
    public MainCookAdapter(int layoutResId, @Nullable List<Category> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Category item) {
        holder.setText(R.id.tv_cook_name, item.getTitle());
        Picasso.with(mContext).load(item.getImgId()).into((ImageView) holder.getView(R.id.iv_cook_image));
    }
}
