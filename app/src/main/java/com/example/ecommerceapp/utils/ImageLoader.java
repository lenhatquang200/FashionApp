package com.example.ecommerceapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerceapp.R;

public class ImageLoader {

    /**
     * Load an image from URL into an ImageView
     *
     * @param context Context
     * @param imageUrl URL of the image
     * @param imageView Target ImageView
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (context == null || imageView == null) {
            return;
        }

        // Use Glide to load images
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image))
                .into(imageView);
    }

    /**
     * Load an image from URL into an ImageView with custom placeholder
     *
     * @param context Context
     * @param imageUrl URL of the image
     * @param imageView Target ImageView
     * @param placeholderResId Resource ID for placeholder
     * @param errorResId Resource ID for error image
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView, 
                                int placeholderResId, int errorResId) {
        if (context == null || imageView == null) {
            return;
        }

        // Use Glide to load images
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .into(imageView);
    }
}
