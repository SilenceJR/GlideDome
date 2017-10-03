package com.silence.glidedome;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.silence.glidedome.inter.GankApi;
import com.silence.glidedome.model.ImageModel;
import com.silence.glidedome.model.ReqMsg;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {


    @InjectView(R.id.recycler)
    RecyclerView mRecycler;
    @InjectView(R.id.fab)
    FloatingActionButton mFab;
    @InjectView(R.id.swipelayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GlideAdapter mAdapter;
    private int page = 1;
    private List<ImageModel> mResults = new ArrayList<>();
    private GankApi mGankApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobclickAgent.setDebugMode(true);
        mGankApi = RetrofitFactory.getInstance();

        initData();

    }

    private void initData() {
        mGankApi.getGank(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReqMsg>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ReqMsg reqMsg) {
                        updataAdatper(reqMsg);

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (mSwipeRefreshLayout.isEnabled()) {
                            mSwipeRefreshLayout.setEnabled(false);
                        }
                        Log.e("e            :", e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private boolean flag = true;

    private void updataAdatper(ReqMsg reqMsg) {
        if (!reqMsg.isError() && reqMsg.getResults().size() != 0) {
            page++;
            mResults = reqMsg.getResults();
            if (flag) {
                mAdapter.setNewData(mResults);
                flag = false;
            } else {
                mAdapter.addData(mResults);
            }

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            if (!mSwipeRefreshLayout.isEnabled()) {
                mSwipeRefreshLayout.setEnabled(true);
            }
            if (mAdapter.isLoading()) {
                mAdapter.loadMoreComplete();
            }
            if (!mAdapter.isLoadMoreEnable()) {
                mAdapter.setEnableLoadMore(true);
            }
        } else {
            if (mAdapter.isLoading()) {
                Toast.makeText(this, "加载出错啦", Toast.LENGTH_SHORT).show();
                mAdapter.loadMoreFail();
            } else {
                //                mAdapter.setEmptyView(notDataView);
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (mSwipeRefreshLayout.isEnabled()) {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        mToolbarTitle.setText("Glide");
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initListener() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(GAP_HANDLING_NONE);
        mRecycler.setLayoutManager(staggeredGridLayoutManager);
//        mRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        mAdapter = new GlideAdapter(mResults);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.openLoadAnimation();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    @OnClick({R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mRecycler.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        page = 1;
        flag = true;
        initData();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        initData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ImageModel imageModel = (ImageModel) adapter.getData().get(position);
        Intent intent = new Intent(this, showPhotoActivity.class);
        intent.putExtra("image", imageModel);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

//            Pair<View, String> imagePair = Pair.create(adapter.getViewByPosition(position, R.id.item_iv), "main_iv");
//            Pair<View, String> tvPair = Pair.create(adapter.getViewByPosition(position, R.id.item_tv), "main_tv");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, adapter.getViewByPosition(position, R.id.item_iv), adapter.getViewByPosition(position, R.id.item_iv).getTransitionName());
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imagePair, tvPair);
//            View imageView = adapter.getViewByPosition(position, R.id.item_iv);
//            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(imageView, 0, 0 ,imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
//            ActivityCompat.startActivity(intent ,options.toBundle());
            ActivityCompat.startActivity(this, intent, options.toBundle());
        } else {
            startActivity(intent);
        }
//        goActivity(showPhotoActivity.class);


    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        Snackbar.make(view, ((ImageModel) adapter.getData().get(position)).getUrl(), Snackbar.LENGTH_SHORT).show();
        return false;
    }
}
