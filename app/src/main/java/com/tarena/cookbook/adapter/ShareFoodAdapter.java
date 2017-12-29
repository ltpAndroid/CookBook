package com.tarena.cookbook.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.Message;
import com.tarena.cookbook.entity.MyUser;
import com.tarena.cookbook.entity.ShowCookersInfo;
import com.tarena.cookbook.util.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2017/12/21
 */

public class ShareFoodAdapter extends MyAdapter<Message> {


    private String TAG = "CookBook";
    private MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);

    public ShareFoodAdapter(Context context, List<Message> data) {
        super(context, data);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Message msg = getData().get(i);
        Log.i(TAG, "msg:" + msg);

        ViewHoler holder = null;
        if (null == view) {
            holder = new ViewHoler();
            view = getLayoutInflater().inflate(R.layout.item_share_food, null);
            holder.iv_head = view.findViewById(R.id.item_iv_head);
            holder.tv_nickname = view.findViewById(R.id.item_tv_nickname);
            holder.tv_content = view.findViewById(R.id.item_tv_content);
            holder.tv_time = view.findViewById(R.id.item_tv_time);
            holder.layout_images = view.findViewById(R.id.imagesLayout);
            view.setTag(holder);
        } else {
            holder = (ViewHoler) view.getTag();
        }

        MyUser user = msg.getUser();
        //异步加载用户头像
        Picasso.with(getContext()).load(user.getHeadPath()).into(holder.iv_head);

        holder.tv_nickname.setText(user.getNickname());
        holder.tv_content.setText(msg.getDetail());

        //显示发送时间
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msg.getCreatedAt()));
            long millis = calendar.getTimeInMillis();

            holder.tv_time.setText(DateTimeUtils.formatDate(millis));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //判断是否有图片
        if (null != msg.getImagePaths()) {
            //显示图片
            loadImages(holder.layout_images, msg.getImagePaths());
        }

        return view;
    }

    private void loadImages(RelativeLayout layout_images, List<String> imagePaths) {

        //删除之前显示所有图片
        layout_images.removeAllViews();

        //获取屏幕宽度
        int scrrenWidth = getContext().getResources().getDisplayMetrics().widthPixels - 300;
        //int scrrenWidth = layout_images.getWidth();
        int size = (scrrenWidth - 2 * 10) / 3;

        if (imagePaths.size() == 1) {
            ImageView iv = new ImageView(getContext());
            layout_images.addView(iv, new RelativeLayout.LayoutParams(600, 600));
            Picasso.with(getContext()).load(imagePaths.get(0)).into(iv);

        } else {

            for (int i = 0; i < imagePaths.size(); i++) {
                ImageView iv = new ImageView(getContext());
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(getContext()).load(imagePaths.get(i)).into(iv);

                iv.setX(i % 3 * (size + 10));
                iv.setY(i / 3 * (size + 10));
                layout_images.addView(iv, new RelativeLayout.LayoutParams(size, size));
            }
        }

        //动态改变 当复用控件中用到动态改变的控件时自动适应高度可能会出问题

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layout_images.getLayoutParams();

        int line = imagePaths.size() % 3 == 0 ? imagePaths.size() / 3 : imagePaths.size() / 3 + 1;
        lp.height = line * (size + 10);
    }

    class ViewHoler {
        CircleImageView iv_head;
        TextView tv_nickname;
        TextView tv_content;
        TextView tv_time;
        RelativeLayout layout_images;
    }
}
