package com.glennbech.events;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */


abstract public class SectionedAdapter extends BaseAdapter {

    abstract protected View getHeaderView(String caption,
                                          int index,
                                          View convertView,
                                          ViewGroup parent);

    private List<Section> sections = new ArrayList<Section>();


    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        for (Section s : sections) {
            s.adapter.notifyDataSetInvalidated();
        }
    }

    public void clear() {
        sections = new ArrayList<Section>();
    }

    public SectionedAdapter() {
        super();
    }

    public void addSection(String caption, BaseAdapter adapter) {
        sections.add(new Section(caption, adapter));
    }

    public Object getItem(int position) {
        for (Section section : this.sections) {
            if (position == 0) {
                return (section);
            }

            int size = section.adapter.getCount() + 1;

            if (position < size) {
                return (section.adapter.getItem(position - 1));
            }
            position -= size;
        }
        return (null);
    }

    public int getCount() {
        int total = 0;

        for (Section section : this.sections) {
            total += section.adapter.getCount() + 1; // add one for header
        }
        return (total);
    }

    public int getViewTypeCount() {
        int total = 1;    // one for the header, plus those from groups

        for (Section section : this.sections) {
            total += section.adapter.getViewTypeCount();
        }
        return (total);
    }

    public int getItemViewType(int position) {
        int typeOffset = 1;    // start counting from here

        for (Section section : this.sections) {
            if (position == 0) {
                return 0;
            }

            int size = section.adapter.getCount() + 1;

            if (position < size) {
                return (typeOffset + section.adapter.getItemViewType(position - 1));
            }

            position -= size;
            typeOffset += section.adapter.getViewTypeCount();
        }
        return (-1);
    }

    public boolean areAllItemsSelectable() {
        return (false);
    }

    public boolean isEnabled(int position) {
        return (getItemViewType(position) != 0);
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        int sectionIndex = 0;

        for (Section section : this.sections) {
            if (position == 0) {
                return (getHeaderView(section.caption, sectionIndex,
                        convertView, parent));
            }

            int size = section.adapter.getCount() + 1;

            if (position < size) {
                return (section.adapter.getView(position - 1,
                        convertView,
                        parent));
            }

            position -= size;
            sectionIndex++;
        }
        return (null);
    }

    public long getItemId(int position) {
        return (position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (Section s : sections) {
            s.adapter.notifyDataSetChanged();
        }
    }

    class Section {
        String caption;
        BaseAdapter adapter;

        Section(String caption, BaseAdapter adapter) {
            this.caption = caption;
            this.adapter = adapter;
        }
    }
}

