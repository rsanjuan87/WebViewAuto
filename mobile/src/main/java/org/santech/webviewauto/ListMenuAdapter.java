package org.santech.webviewauto;

import androidx.car.app.model.ItemList;
import androidx.car.app.model.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMenuAdapter {
    private final List<Row> menuItems = new ArrayList<>();
    private final Map<String, Integer> menuItemsByNames = new HashMap<>();
    private MenuCallbacks callbacks;

    public interface MenuCallbacks {
        void onMenuItemClicked(String name);
        void onEnter();
        void onExit();
    }

    public Row getMenuItem(int i) {
        return menuItems.get(i);
    }

    public Row getMenuItem(String name) {
        Integer index = menuItemsByNames.get(name);
        return index != null ? menuItems.get(index) : null;
    }

    public int getMenuItemCount() {
        return menuItems.size();
    }

    public void addMenuItem(String name, Row menuItem) {
        menuItemsByNames.put(name, menuItems.size());
        menuItems.add(menuItem);
    }

    public void setCallbacks(MenuCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public ItemList.Builder buildItemList() {
        ItemList.Builder builder = new ItemList.Builder();
        for (Row item : menuItems) {
            builder.addItem(item);
        }
        return builder;
    }

    public void onMenuItemClicked(String name) {
        if (callbacks != null) {
            callbacks.onMenuItemClicked(name);
        }
    }

    public void onEnter() {
        if (callbacks != null) {
            callbacks.onEnter();
        }
    }

    public void onExit() {
        if (callbacks != null) {
            callbacks.onExit();
        }
    }
}
