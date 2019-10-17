package com.google.learndemo.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.learndemo.R;
import com.google.learndemo.widget.transform.CircleRoundTransform;
import com.google.learndemo.widget.transform.CircleTransform;

/**
 * Glide 图片工具类
 */
public class GlideUtil {

    /**
     * 加载原生图片
     * @param context
     * @param res
     * @param iv
     */
    public static void loadImage(Context context, Object res, ImageView iv) {

        RequestOptions options = new RequestOptions()
//                .skipMemoryCache(false)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // 磁盘缓存
//                .dontAnimate()
//                .signature(new ObjectKey(res))
                .error(R.mipmap.ic_launcher) // 请求永久性失败时展示
                .fallback(R.mipmap.ic_launcher); // 在请求的url/model为null时展示

        Glide.with(context).load(res).apply(options).into(iv);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param res
     * @param iv
     */
    public static void loadCircleImage(Context context, Object res, ImageView iv) {

        RequestOptions options = new RequestOptions()
//                .skipMemoryCache(false)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // 磁盘缓存
//                .dontAnimate()
//                .signature(new ObjectKey(res))
                .error(R.mipmap.ic_launcher) // 请求永久性失败时展示
                .fallback(R.mipmap.ic_launcher) // 在请求的url/model为null时展示
                .transform(new CircleTransform()); // 设置圆形转换样式

        Glide.with(context).load(res).apply(options).into(iv);
    }

    /**
     * 加载圆角图片
     * @param context
     * @param res
     * @param iv
     */
    public static void loadCircleRoundImage(Context context, Object res, int radius, ImageView iv) {

        RequestOptions options = new RequestOptions()
//                .skipMemoryCache(false) // 禁止内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // 磁盘缓存
//                .dontAnimate()
//                .signature(new ObjectKey(res))
                .error(R.mipmap.ic_launcher) // 请求永久性失败时展示
                .fallback(R.mipmap.ic_launcher) // 在请求的url/model为null时展示
                .transform(new CircleRoundTransform(radius)); // 设置圆角、centerCrop转换样式

        Glide.with(context).load(res).apply(options).into(iv);
    }
}
