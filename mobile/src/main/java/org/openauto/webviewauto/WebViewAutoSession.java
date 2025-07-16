package org.openauto.webviewauto;

import android.content.Intent;
import androidx.car.app.Screen;
import androidx.car.app.Session;

public class WebViewAutoSession extends Session {
    @Override
    public Screen onCreateScreen(Intent intent) {
        return new WebViewScreen(getCarContext());
    }
}