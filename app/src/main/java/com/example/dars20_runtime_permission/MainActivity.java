package com.example.dars20_runtime_permission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity
        extends AppCompatActivity
        implements OnItemClickListener {
    private final int READ_STORAGE_REQ_CODE = 1001;
    private FileAdapter adapter;
    private LinkedList<File> queue;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = new LinkedList<>();
        rootView = findViewById(R.id.root_view);
        initRv();
    }

    private void initRv() {
        RecyclerView rv = findViewById(R.id.rv);
        File[] fileArray = loadFiles();
        ArrayList<File> fileList = new ArrayList<>();
        Collections.addAll(fileList, fileArray);
        adapter = new FileAdapter(this, fileList);
        rv.setAdapter(adapter);
    }

    private File[] loadFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                File root = Environment.getExternalStorageDirectory();
                queue.add(root);
                return root.listFiles();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    alertWithDialog();
                    alertWithSnackbar();
                } else
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_REQ_CODE);
                return new File[0];
            }
        } else {
            return Environment.getExternalStorageDirectory().listFiles();
        }
    }


    @SuppressLint("NewApi")
    private void alertWithSnackbar() {
        Snackbar snackbar = Snackbar.make(rootView, "Please give permission", Snackbar.LENGTH_SHORT);
        snackbar.setAction("GIVE", v -> requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_REQ_CODE));
        snackbar.setActionTextColor(Color.BLUE);
        snackbar.show();
    }

    @SuppressLint("NewApi")
    private void alertWithDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Permission Explanation")
                .setMessage("Manga ruhsat ber bla bla bla ish uchun.")
                .setPositiveButton("OK", (dialog1, which) -> requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_REQ_CODE))
                .setNegativeButton("NO", (dialog12, which) -> dialog12.dismiss())
                .setCancelable(false)
                .create();
        //todo snackbar

        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_REQ_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        File[] files = Environment.getExternalStorageDirectory().listFiles();
                        adapter.updateData(files);
                    } else {
                        Toast.makeText(this, "you denied read from storage", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

    @Override
    public void onItemClicked(File file) {
        queue.add(file);
        Toast.makeText(this, file.getName(), Toast.LENGTH_SHORT).show();
        if (file.isDirectory()) {
            adapter.updateData(file.listFiles());
        }
    }

    @Override
    public void onBackPressed() {
        if (queue.isEmpty()) super.onBackPressed();
        else {
            File file = queue.pollLast();
            Log.d("onBackPressed", file.getName());
            adapter.updateData(file.listFiles());
        }
    }
}
