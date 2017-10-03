package com.silence.glidedome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.silence.glidedome.glide.GlideApp;
import com.silence.glidedome.model.ImageModel;

import butterknife.InjectView;
import butterknife.OnClick;

public class showPhotoActivity extends BaseActivity implements View.OnLongClickListener {

    @InjectView(R.id.photo_view)
    PhotoView mPhotoView;
    @InjectView(R.id.tv_name)
    TextView mTvName;
    private ImageModel mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.imagetransition);
            getWindow().setSharedElementEnterTransition(transition);

            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    mTvName.setVisibility(View.GONE);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        transition.removeListener(this);
                        animateRevealShow();
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }




        mImage = (ImageModel) getIntent().getSerializableExtra("image");
        GlideApp.with(this)
                .load(mImage.getUrl())
                .thumbnail(1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPhotoView);
    }

    private void animateRevealShow() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(mTvName, mTvName.getMeasuredWidth(), mTvName.getMeasuredHeight(), mTvName.getMeasuredWidth(), mTvName.getHeight());
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    mTvName.setVisibility(View.VISIBLE);
                    super.onAnimationStart(animation);
                }
            });
            animator.start();
        }
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
        mPhotoView.setOnLongClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_photo;
    }


    @OnClick(R.id.photo_view)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public boolean onLongClick(View view) {
        Snackbar.make(view, mImage.getUrl(), Snackbar.LENGTH_SHORT).show();
        Log.d("Url  :   ", mImage.getUrl());
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
            ActivityCompat.finishAfterTransition(this);
        }

    }
}
