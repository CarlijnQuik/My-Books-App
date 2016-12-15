package com.example.carlijnquik.carlijnquik_pset6;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {

    Activity activity;
    Context context;
    ArrayList<Book> books;

    public BookAdapter(Activity activity, ArrayList<Book> books) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_item_list, null);
        }

        Book book = books.get(position);
        if (books != null) {

            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            ImageView ivCover = (ImageView) convertView.findViewById(R.id.ivCover);

            if (book.getTitle() != null) {
                tvTitle.setText(book.getTitle());
            }
            if (book.getAuthor() != null) {
                tvAuthor.setText(book.getAuthor());
            }
            Picasso.with(context).load(Uri.parse(book.getCoverUrl())).error(R.drawable.nocover).into(ivCover);


        }

        return convertView;
    }

    @Override
    public int getCount(){
        return books.size();
    }

    public Object getItem(int position){
        return books.get(position);
    }

    public long getItemId(int i){
        return books.indexOf(getItem(i));
    }

}
