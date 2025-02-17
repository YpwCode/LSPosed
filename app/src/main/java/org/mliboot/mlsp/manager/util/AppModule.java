

package org.mliboot.mlsp.manager.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.mliboot.mlsp.manager.R;

@GlideModule
public class AppModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void registerComponents(Context context, @NonNull Glide glide, Registry registry) {
        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.app_icon_size);
        var factory = new AppIconModelLoader.Factory(iconSize, false, context);
        registry.prepend(PackageInfo.class, Bitmap.class, factory);
    }
}
