package edu.it_diving.viewModel;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class CallMember {
    private String name;
    private int icon;
    private Bitmap iconBitmap;

    protected CallMember(@NonNull String name, int icon) {

        setName(name);
        setIcon(icon);
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        iconBitmap = null;
    }

    public void setIcon(Bitmap iconBitmap) { this.iconBitmap = iconBitmap; }

    public int getIcon() {
        return icon;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

}