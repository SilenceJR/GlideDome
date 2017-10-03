package com.silence.glidedome;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.silence.glidedome.glide.GlideApp;
import com.silence.glidedome.model.ImageModel;

import java.util.List;

/**
 * Created by Silence
 *
 * @time 2017/9/23 1:17
 * @des ${TODO}
 */

class GlideAdapter extends BaseQuickAdapter<ImageModel, BaseViewHolder> {

    public GlideAdapter(@Nullable List<ImageModel> data) {
        super(R.layout.item_main_glide, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ImageModel item) {

        final ImageView imageView = (ImageView) helper.getView(R.id.item_iv);

        final TextView textView = (TextView) helper.getView(R.id.item_tv);

        final ViewGroup.LayoutParams params = imageView.getLayoutParams();
        final CardView cardView = (CardView) helper.getView(R.id.item_card);
        final ViewGroup.LayoutParams cardParams = cardView.getLayoutParams();

        //        GlideApp.with(mContext)
        //                .load(item.getUrl())
        //                .placeholder()

        GlideApp.with(mContext)
                .asBitmap()
                .load(item.getUrl())

                .thumbnail(0.1f)

                .placeholder(R.drawable.icon_tese)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        int width = resource.getWidth();
                        int height = resource.getHeight();

//                        int max = DensityUtil.dip2px(imageView.getContext(), 240);
                        int max = DensityUtil.dip2px(imageView.getContext(), BaseActivity.getWidth() / 2);
                        helper.setText(R.id.item_tv, width + "x" + height);

                        if (width > max || height > max) {
                            if (height > width) {
                                params.height = max;
                                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                cardParams.height = max + textView.getLayoutParams().height;
                                cardParams.width = params.width;
                            } else {
                                params.width = max;
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                cardParams.height = params.height + textView.getLayoutParams().height;
                                cardParams.width = params.width;
                            }
                        }
                        imageView.setLayoutParams(params);
                        cardView.setLayoutParams(cardParams);


                        //
                        //                                                ViewGroup.LayoutParams tvParams = textView.getLayoutParams();
                        //
                        //                                                params.height = BaseActivity.getWidth() * height / width;
                        //
                        //
                        //                        cardParams.height = params.height + tvParams.height;
                        //
                        //                        cardView.setLayoutParams(cardParams);

                        //                        float i = width / height;
                        //                        if (width > 300 || height > 300) {
                        //                            params.height = imageView.getWidth() * height / width;
                        //                            imageView.setLayoutParams(params);
                        //                        }
                        //
                        //                        imageView.setLayoutParams(params);
//                        cardView.setLayoutParams(cardParams);
                        imageView.setImageBitmap(resource);
                    }
                });

    }

}
