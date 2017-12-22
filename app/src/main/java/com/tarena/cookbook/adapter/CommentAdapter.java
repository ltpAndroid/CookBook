package com.tarena.cookbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.Comment;
import com.tarena.cookbook.util.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class CommentAdapter extends MyAdapter<Comment> {
    public CommentAdapter(Context context, List<Comment> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler holder = null;
        if (null == convertView) {
            holder = new ViewHoler();
            convertView = getLayoutInflater().inflate(R.layout.item_comment, null);
            holder.ivHead = convertView.findViewById(R.id.comment_iv_head);
            holder.tvNickname = convertView.findViewById(R.id.comment_tv_nickname);
            holder.tvContent = convertView.findViewById(R.id.comment_tv_content);
            holder.tvTime = convertView.findViewById(R.id.comment_tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHoler) convertView.getTag();
        }

        Comment comment = getItem(position);
        Log.i("comment",""+comment);
        Picasso.with(getContext()).load(comment.getUser().getHeadPath()).into(holder.ivHead);
        holder.tvNickname.setText(comment.getUser().getNickname());
        holder.tvContent.setText(comment.getContent());
        //显示发送时间
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comment.getCreatedAt()));
            long millis = calendar.getTimeInMillis();

            holder.tvTime.setText(DateTimeUtils.formatDate(millis));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHoler {
        CircleImageView ivHead;
        TextView tvNickname;
        TextView tvContent;
        TextView tvTime;
    }
}
