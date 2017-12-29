package com.tarena.cookbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CommentAdapter;
import com.tarena.cookbook.entity.Comment;
import com.tarena.cookbook.entity.Message;
import com.tarena.cookbook.entity.MyUser;
import com.tarena.cookbook.util.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.lv_comment)
    ListView lvComment;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.iv_send)
    ImageView ivSend;

    TextView tvContent;
    CircleImageView ivHead;
    TextView tvNickname;
    TextView tvTime;
    RelativeLayout layout_images;
    TextView tvDelete;

    private Message message;
    private CommentAdapter adapter;
    private MyUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);

        currentUser = BmobUser.getCurrentUser(MyUser.class);
        initHeaderView();
    }

    //**********************头部View******************//
    private void initHeaderView() {
        View messageView = getLayoutInflater().inflate(R.layout.view_message, null);

        tvNickname = messageView.findViewById(R.id.item_msg_nickname);
        tvContent = messageView.findViewById(R.id.item_msg_content);
        tvTime = messageView.findViewById(R.id.item_msg_time);
        layout_images = messageView.findViewById(R.id.item_msg_imagesLayout);
        ivHead = messageView.findViewById(R.id.item_msg_head);
        tvDelete = messageView.findViewById(R.id.tv_msg_delete);

        //得到传递过来的消息对象
        message = (Message) getIntent().getExtras().get("msg");

        tvContent.setText(message.getDetail());
        tvNickname.setText(message.getUser().getNickname());
        Picasso.with(this).load(message.getUser().getHeadPath()).into(ivHead);
        //显示发送时间
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.getCreatedAt()));
            long millis = calendar.getTimeInMillis();
            tvTime.setText(DateTimeUtils.formatDate(millis));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //删除信息:tvDelete
        MyUser user = message.getUser();
        if (currentUser != null) {
            if (!user.getNickname().equals(currentUser.getNickname())) {
                tvDelete.setVisibility(View.INVISIBLE);
            } else {
                tvDelete.setVisibility(View.VISIBLE);
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MessageDetailActivity.this);
                        dialog.setTitle("注意!!!");
                        dialog.setMessage("是否确认删除当前信息?");
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                message.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(MessageDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(MessageDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        dialog.create().show();
                    }
                });
            }
        } else {
            tvDelete.setVisibility(View.INVISIBLE);
        }

        //判断是否有图片
        if (null != message.getImagePaths()) {
            //显示图片
            loadImages(layout_images, message.getImagePaths());
        }

        //给添加到listview里面的空间添加ListView的layoutparams
        ListView.LayoutParams lp = new ListView.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageView.setLayoutParams(lp);

        //添加listVIew的headerVIew  为 messageView
        lvComment.addHeaderView(messageView);
    }

    private void loadImages(RelativeLayout layout_images, List<String> imagePaths) {

        //删除之前显示所有图片
        layout_images.removeAllViews();

        //获取屏幕宽度
        int scrrenWidth = this.getResources().getDisplayMetrics().widthPixels - 300;
        // int scrrenWidth = layout_images.getWidth();
        int size = (scrrenWidth - 2 * 8) / 3;


        if (imagePaths.size() == 1) {
            ImageView iv = new ImageView(this);
            layout_images.addView(iv, new RelativeLayout.LayoutParams(600, 600));
            Picasso.with(this).load(imagePaths.get(0)).into(iv);

        } else {

            for (int i = 0; i < imagePaths.size(); i++) {
                ImageView iv = new ImageView(this);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(this).load(imagePaths.get(i)).into(iv);

                iv.setX(i % 3 * (size + 8));
                iv.setY(i / 3 * (size + 8));
                layout_images.addView(iv, new RelativeLayout.LayoutParams(size, size));
            }
        }

        //动态改变 当复用控件中用到动态改变的控件时自动适应高度可能会出问题
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layout_images
                .getLayoutParams();

        int line = imagePaths.size() % 3 == 0 ? imagePaths.size() / 3 : imagePaths.size() / 3 + 1;
        lp.height = line * (size + 8);
    }

    @OnClick({R.id.iv_left, R.id.iv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_send:
                sendAction();
                break;
        }
    }

    private void sendAction() {
        Comment comment = new Comment();
        comment.setContent(etComment.getText().toString());
        comment.setUser(BmobUser.getCurrentUser(MyUser.class));
        comment.setSourceMesId(message.getObjectId());
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    etComment.setText("");
                    Toast.makeText(MessageDetailActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessageDetailActivity.this, "发送失败：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                //发送完成之后需要查询
                loadComments();
            }
        });
    }

    private void loadComments() {
        BmobQuery<Comment> bq = new BmobQuery<>();
        //去Comment表中 查询和当前message相关的评论
        bq.addWhereEqualTo("sourceMesId", message.getObjectId());
        bq.order("-createdAt");
        bq.include("user");//包括user
        bq.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                adapter = new CommentAdapter(MessageDetailActivity.this, list);
                lvComment.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadComments();
    }
}
