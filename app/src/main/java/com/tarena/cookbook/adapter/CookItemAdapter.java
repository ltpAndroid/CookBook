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

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class CookItemAdapter extends BaseAdapter {

    private ShowCookersInfo adapterInfo;
    private Context context;

    public CookItemAdapter(Context context,ShowCookersInfo adapterInfo) {
        this.context = context;
        this.adapterInfo = adapterInfo;
    }

    @Override
    public int getCount() {
        if (adapterInfo.getResult() == null) {
            return 0;
        }
        return adapterInfo.getResult().getData().size();
    }

    @Override
    public Object getItem(int position) {
        return adapterInfo.getResult().getData().get(position);
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

        //ShowCookersInfo info = (ShowCookersInfo) getItem(position);
        Picasso.with(context).load(adapterInfo.getResult().getData().get(position).getAlbums().get(0)).placeholder(R.drawable.ic_placeholder).into(holder.cookImg);
        holder.cookName.setText(adapterInfo.getResult().getData().get(position).getTitle());
        holder.cookTags.setText(adapterInfo.getResult().getData().get(position).getTags());
        holder.cookIngredients.setText(adapterInfo.getResult().getData().get(position).getIngredients());
        holder.cookBurden.setText(adapterInfo.getResult().getData().get(position).getBurden());

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
