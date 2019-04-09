package com.mancel.yann.topquiz.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mancel.yann.topquiz.R;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Yann MANCEL on 01/04/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.controller
 */
public class ItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mItemList;
    private LayoutInflater mLayoutInflater;

    //----------------------------------------------------------------------------------------------

    /**
     *  Initializes a ItemAdapter object with 2 arguments
     *
     * @param context a Context object that initializes the context of the ItemAdapter object
     * @param list a List<String> object that contains all items
     */
    public ItemAdapter(Context context, List<String> list) {
        // Initializes the context of the ItemAdapter object
        this.mContext = context;

        // Retrieves the list of items
        this.mItemList = list;

        // Initializes the LayoutInflater object
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public int getCount() {
        return this.mItemList.size();
    }

    @Override
    public String getItem(int position) {
        return this.mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Updates the item view thanks to the XML file
        convertView = this.mLayoutInflater.inflate(R.layout.adapter_item, null);

        // Retrieves the item to the list: [name: score]
        StringTokenizer stringTokenizer = new StringTokenizer(getItem(position), ": ");

        // Updates the name
        String name = stringTokenizer.nextToken();
        TextView nameTextView = (TextView) convertView.findViewById(R.id.ItemAdapter_mNameItem);
        nameTextView.setText(name);

        // Updates the score
        String score = stringTokenizer.nextToken();
        TextView scoreTextView = (TextView) convertView.findViewById(R.id.ItemAdapter_mScoreItem);
        scoreTextView.setText(score);

        return convertView;
    }
}
