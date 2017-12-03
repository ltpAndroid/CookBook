package com.tarena.cookbook.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2 0002.
 */

public class CategoryAdapter extends MyAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_gv_category, null);
            holder.ivIcon = convertView.findViewById(R.id.iv_icon);
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Category category = getData().get(position);
        holder.ivIcon.setImageResource(category.getImgId());
        holder.tvTitle.setText(category.getTitle());

        return convertView;
    }

    class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }
}
