package com.example.altaybook;

import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Book> bookList = new ArrayList<>();
    private OnLongClickListener onLongClickListener;
    MainActivity mainActivity;
    List<Book> selectedBooks;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kitap, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.book_name_tv.setText(bookList.get(position).getName());
        holder.book_image.setImageBitmap(bytesToBitmap(bookList.get(position).getImage()));

        if(bookList.get(position).isSelected()) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_name_tv;
        ImageView book_image;
        CardView cardView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            book_name_tv = itemView.findViewById(R.id.book_name_id);
            book_image = itemView.findViewById(R.id.book_image_id);
            cardView = itemView.findViewById(R.id.cardview_id);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if(bookList.get(position).isSelected()){
                        bookList.get(position).setSelected(false);
                        notifyDataSetChanged();
                        return false;
                    }
                    bookList.get(position).setSelected(true);
                    notifyDataSetChanged();

                    onLongClickListener.onLongClick();

                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public interface OnLongClickListener {
        void onLongClick();
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public List<Book> getSelectedBooks() {
        selectedBooks = new ArrayList<>();
        for(Book book : bookList) {
            if(book.isSelected()) {
                selectedBooks.add(book);
            }
        }
        return selectedBooks;
    }

    public void setSelectedBooks(List<Book> selectedBooks) {
        this.selectedBooks = selectedBooks;
        notifyDataSetChanged();
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public Bitmap bytesToBitmap(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
        return bitmap;
    }

}
