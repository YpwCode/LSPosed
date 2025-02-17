

package org.mliboot.mlsp.manager.ui.activity.base;

import android.app.ActivityManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.mliboot.mlsp.manager.App;
import org.mliboot.mlsp.manager.R;
import org.mliboot.mlsp.manager.util.Telemetry;
import org.mliboot.mlsp.manager.util.ThemeUtil;

import rikka.material.app.MaterialActivity;

public class BaseActivity extends MaterialActivity {
    private static Bitmap icon = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!App.isParasitic) return;
        for (var task : getSystemService(ActivityManager.class).getAppTasks()) {
            task.setExcludeFromRecents(false);
        }
        if (icon == null) {
            var drawable = getApplicationInfo().loadIcon(getPackageManager());
            if (drawable instanceof BitmapDrawable) {
                icon = ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof AdaptiveIconDrawable) {
                icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                final Canvas canvas = new Canvas(icon);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }
        }
        setTaskDescription(new ActivityManager.TaskDescription(getTitle().toString(), icon, getColor(R.color.ic_launcher_background)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Telemetry.trackEvent("BaseActivity stop", null);
    }

    @Override
    public void onApplyUserThemeResource(@NonNull Resources.Theme theme, boolean isDecorView) {
        if (!ThemeUtil.isSystemAccent()) {
            theme.applyStyle(ThemeUtil.getColorThemeStyleRes(), true);
        }
        theme.applyStyle(ThemeUtil.getNightThemeStyleRes(this), true);
        theme.applyStyle(rikka.material.preference.R.style.ThemeOverlay_Rikka_Material3_Preference, true);
    }

    @Override
    public String computeUserThemeKey() {
        return ThemeUtil.getColorTheme() + ThemeUtil.getNightTheme(this);
    }

    @Override
    public void onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars();
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }
}
