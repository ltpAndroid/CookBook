package com.tarena.cookbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.ShowCookersInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class CookItemAdapter extends BaseAdapter {

    private List<ShowCookersInfo.Result.Data> adapterInfo = new ArrayList<>();
    private Context context;

    public CookItemAdapter(Context context,List<ShowCookersInfo.Result.Data> adapterInfo) {
        this.context = context;
        this.adapterInfo = adapterInfo;
    }

    @Override
    public int getCount() {
        return adapterInfo.size();
    }

    @Override
    public ShowCookersInfo.Result.Data getItem(int position) {
        return adapterInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cooks_list, parent, false);
            holder.cookImg = convertView.findViewById(R.id.cook_img);
            holder.cookName = convertView.findViewById(R.id.cook_name);
            holder.cookTags = convertView.findViewById(R.id.cook_tags);
            holder.cookIngredients = convertView.findViewById(R.id.cook_ingredients);
            holder.cookBurden = convertView.findViewById(R.id.cook_burden);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShowCookersInfo.Result.Data info = getItem(position);
        Picasso.with(context).load(info.getAlbums().get(0)).placeholder(R.drawable.ic_placeholder).into(holder.cookImg);
        holder.cookName.setText(info.getTitle());
        holder.cookTags.setText(info.getTags());
        holder.cookIngredients.setText(info.getIngredients());
        holder.cookBurden.setText(info.getBurden());


        return convertView;
    }

    class ViewHolder {
        ImageView cookImg;
        TextView cookName;
        TextView cookTags;
        TextView cookIngredients;
        TextView cookBurden;
    }
}
