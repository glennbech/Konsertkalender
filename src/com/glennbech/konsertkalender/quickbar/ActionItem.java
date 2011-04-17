package com.glennbech.konsertkalender.quickbar;

import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;

/**
 * Action item, displayed as menu with icon and text.
 *
 * @author Lorensius. W. L. T
 */
class ActionItem {

    private Drawable icon;
    private String title;
    private OnClickListener listener;
    private Object argument;

    public Object getArgument() {
        return argument;
    }

    public void setArgument(Object argument) {
        this.argument = argument;
    }

    /**
     * Constructor
     */
    public ActionItem() {
    }


    /**
     * Constructor
     *
     * @param icon {@link android.graphics.drawable.Drawable} action icon
     */
    public ActionItem(Drawable icon) {
        this.icon = icon;
    }


    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }


    /**
     * Set action title
     *
     * @param title action title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get action title
     *
     * @return action title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Set action icon
     *
     * @param icon {@link android.graphics.drawable.Drawable} action icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * Get action icon
     *
     * @return {@link android.graphics.drawable.Drawable} action icon
     */
    public Drawable getIcon() {
        return this.icon;
    }

    /**
     * Set on click listener
     *
     * @param listener on click listener {@link }
     */
    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Get on click listener
     *
     * @return on click listener {@link }
     */
    public OnClickListener getListener() {
        return this.listener;
    }

}