package org.openauto.webviewauto;

import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.ActionStrip;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.car.app.model.MessageTemplate;

import org.openauto.webviewauto.favorites.FavoriteEnt;
import org.openauto.webviewauto.favorites.FavoriteManager;

public class MenuManager {
    private final CarContext carContext;
    private final WebViewScreen screen;

    public MenuManager(CarContext carContext, WebViewScreen screen) {
        this.carContext = carContext;
        this.screen = screen;
    }

    public Template buildMainMenu() {
        ItemList.Builder listBuilder = new ItemList.Builder();

        // Add home button
        listBuilder.addItem(new Row.Builder()
                .setTitle(carContext.getString(R.string.menu_home))
                .setOnClickListener(() -> navigateToHome())
                .build());

        // Add back button
        listBuilder.addItem(new Row.Builder()
                .setTitle(carContext.getString(R.string.menu_back))
                .setOnClickListener(() -> navigateBack())
                .build());

        // Add favorites section
        FavoriteManager favoriteManager = new FavoriteManager(carContext);
        for (FavoriteEnt favorite : favoriteManager.favorites) {
            listBuilder.addItem(new Row.Builder()
                    .setTitle(favorite.getTitle())
                    .setOnClickListener(() -> navigateToUrl(favorite.getUrl()))
                    .build());
        }

        return new MessageTemplate.Builder(carContext.getString(R.string.app_name))
                .setHeaderAction(Action.APP_ICON)
                .setActionStrip(new ActionStrip.Builder()
                        .addAction(new Action.Builder()
                                .setTitle("Close")
                                .setOnClickListener(() -> screen.finish())
                                .build())
                        .build())
                .build();
    }

    private void navigateToHome() {
        // Implement home navigation
    }

    private void navigateBack() {
        // Implement back navigation
    }

    private void navigateToUrl(String url) {
        // Implement URL navigation
    }
}