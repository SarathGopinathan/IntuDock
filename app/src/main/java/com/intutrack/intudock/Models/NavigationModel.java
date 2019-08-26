package com.intutrack.intudock.Models;

public class NavigationModel {

    private String title, text;
    private int icon;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }

}
