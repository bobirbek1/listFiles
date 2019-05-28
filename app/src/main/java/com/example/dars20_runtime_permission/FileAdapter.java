package com.example.dars20_runtime_permission;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileVH> {
    private ArrayList<File> files;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public FileAdapter(MainActivity activity,
                       ArrayList<File> files) {
        this.files = files;
        inflater = LayoutInflater.from(activity);
        listener = activity;
    }

    @NonNull
    @Override
    public FileVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.file_item, parent, false);
        return new FileVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FileVH holder,
                                 int position) {
        File file = files.get(position);
        holder.onBind(file);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void updateData(File[] fileArray) {
        files.clear();
        Collections.addAll(files, fileArray);
//        notifyItemRangeInserted(0, fileArray.length);
        notifyDataSetChanged();
    }


    class FileVH extends RecyclerView.ViewHolder {
        private TextView tvName, tvSize, tvDate;
        private ImageView iv;
        private File file;

        public FileVH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvSize = itemView.findViewById(R.id.tv_size);
            iv = itemView.findViewById(R.id.iv);
            itemView.setOnClickListener(v -> listener.onItemClicked(file));
        }

        void onBind(File file) {
            this.file = file;
            tvName.setText(file.getName());
            if (file.isDirectory()) {
                iv.setImageResource(R.drawable.folder);
            } else {
                iv.setImageResource(R.drawable.file);
            }
        }
    }
}
