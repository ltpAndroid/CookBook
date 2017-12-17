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

import java.util.List;


public class CookStepListAdapter extends BaseAdapter {
    private List<ShowCookersInfo.Result.Data.Steps> cookSteps;
    private Context context;

    public CookStepListAdapter(List<ShowCookersInfo.Result.Data.Steps> cookSteps, Context context) {
        this.cookSteps = cookSteps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cookSteps.size();
    }

    @Override
    public ShowCookersInfo.Result.Data.Steps getItem(int position) {
        return cookSteps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cook_step, null);
            holder = new ViewHolder();
            holder.stepNum = convertView.findViewById(R.id.step_num);
            holder.img = convertView.findViewById(R.id.img);
            holder.content = convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShowCookersInfo.Result.Data.Steps cookStep = getItem(position);
        holder.stepNum.setText(position + 1 + "");
        holder.content.setText(cookStep.getStep().substring(2));
        Picasso.with(context).load(cookStep.getImg()).placeholder(R.drawable.ic_placeholder).into(holder.img);
        return convertView;
    }

    private class ViewHolder {
        TextView stepNum;
        ImageView img;
        TextView content;
    }
}
