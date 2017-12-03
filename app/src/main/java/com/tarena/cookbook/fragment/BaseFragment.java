package com.tarena.cookbook.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarena.cookbook.activity.MessageActivity;
import com.tarena.cookbook.R;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public abstract  class BaseFragment extends Fragment {
    protected LinearLayout actionBar;
    protected View contentView;

    protected void initialActionBar(int leftId,String title){
        //如果actionBar为null,说明该fragment中没有actionBar
        if (actionBar == null) {
            return;
        }

        ImageView ivLeft = actionBar.findViewById(R.id.iv_actionBar_left);
        TextView tvTitle = actionBar.findViewById(R.id.tv_actionBar_title);
        ImageView ivRight = actionBar.findViewById(R.id.iv_actionBar_right);

        if (leftId<=0){
            ivLeft.setVisibility(View.INVISIBLE);
        }else{
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setImageResource(leftId);
        }

        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.INVISIBLE);
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        ivRight.setImageResource(R.drawable.message_menu);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
    }

    protected abstract void initialUI();

}
