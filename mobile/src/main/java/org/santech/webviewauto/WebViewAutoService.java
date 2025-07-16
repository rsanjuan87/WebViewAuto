package org.santech.webviewauto;

import androidx.car.app.CarAppService;
import androidx.car.app.Session;
import androidx.car.app.validation.HostValidator;

public class WebViewAutoService extends CarAppService {
    @Override
    public Session onCreateSession() {
        return new WebViewAutoSession();
    }

    @Override
    public HostValidator createHostValidator() {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR;
    }
}
