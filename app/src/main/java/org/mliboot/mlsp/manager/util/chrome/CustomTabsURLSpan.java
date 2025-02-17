

package org.mliboot.mlsp.manager.util.chrome;

import android.app.Activity;
import android.text.style.URLSpan;
import android.view.View;

import org.mliboot.mlsp.manager.util.NavUtil;

public class CustomTabsURLSpan extends URLSpan {

    private final Activity activity;

    public CustomTabsURLSpan(Activity activity, String url) {
        super(url);
        this.activity = activity;
    }

    @Override
    public void onClick(View widget) {
        String url = getURL();
        NavUtil.startURL(activity, url);
    }
}
