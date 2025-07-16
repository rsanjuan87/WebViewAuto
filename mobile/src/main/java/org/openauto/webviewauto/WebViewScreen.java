package org.openauto.webviewauto;

import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.Template;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.ActionStrip;
import androidx.car.app.model.Row;
import androidx.car.app.model.ItemList;

public class WebViewScreen extends Screen {
    private final MenuManager menuManager;

    public WebViewScreen(CarContext carContext) {
        super(carContext);
        this.menuManager = new MenuManager(carContext, this);
    }

    @Override
    public Template onGetTemplate() {
        return menuManager.buildMainMenu();
    }
}