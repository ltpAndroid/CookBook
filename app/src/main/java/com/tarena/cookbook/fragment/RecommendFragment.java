package com.tarena.cookbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CategoryAdapter;
import com.tarena.cookbook.entity.Category;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class RecommendFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner banner;
    Unbinder unbinder;
    @BindView(R.id.gv_category)
    GridView gvCategory;

    private TextView tvShow;
    private ImageView ivAddMenu;
    List<String> imagePaths;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_recommend, container, false);
        unbinder = ButterKnife.bind(this, contentView);

        initData();
        initialUI();
        setListener();
        return contentView;
    }

    private void initData() {
        imagePaths = new ArrayList<>();
        imagePaths.add("http://bmob-cdn-14961.b0.upaiyun.com/2017/11/30/8586e90e53c1497ba53d69360465b127.jpeg");
        imagePaths.add("http://bmob-cdn-14961.b0.upaiyun.com/2017/11/29/62a7fe512dc04882b413d3c2921c5c40.png");

        categoryList.clear();
        categoryList.add(0, new Category(R.drawable.chinese_cooking, "家常菜"));
        categoryList.add(1, new Category(R.drawable.fast_cook, "快手菜"));
        categoryList.add(2, new Category(R.drawable.original_cook, "创意菜"));
        categoryList.add(3, new Category(R.drawable.vegetable_cook, "素菜"));
        categoryList.add(4, new Category(R.drawable.cold_cook, "凉菜"));
        categoryList.add(5, new Category(R.drawable.cookie_cook, "烘焙"));
        categoryList.add(6, new Category(R.drawable.noodles_cook, "面食"));
        categoryList.add(7, new Category(R.drawable.soup_cook, "汤"));
        categoryList.add(8, new Category(R.drawable.flavor_cook, "自制调味料"));

    }


    private void setListener() {


    }

    @Override
    protected void initialUI() {
        actionBar = contentView.findViewById(R.id.acitonBar_recommend);
        initialActionBar(R.drawable.add_menu, "美食推荐");
        ivAddMenu = contentView.findViewById(R.id.iv_actionBar_left);

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imagePaths);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        adapter = new CategoryAdapter(getActivity(), categoryList);
        gvCategory.setAdapter(adapter);

    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
