package com.example.recognizingapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.recognizingapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.opencv.videoio.VideoCapture;

public class FromSavedFragment extends Fragment {

    VideoView vid;
    MediaController mediaController;
    String path;

    public FromSavedFragment() {
        // Required empty public constructor
    }


    public void openGallery(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 101);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == Activity.RESULT_OK && data != null){
            Uri vidUri = data.getData();

            path = getPath(vidUri);


            mediaController = new MediaController(this.getContext());
            mediaController.setAnchorView(vid);
            vid.setMediaController(mediaController);
            if(path != null){
                vid.setVideoURI(Uri.parse(path));
                vid.start();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_from_saved, container, false);
        vid = view.findViewById(R.id.video_view);

        final FloatingActionButton button = view.findViewById(R.id.load_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openGallery(v);
            }
        });


        return view;
    }



    private String getPath(Uri uri){
        if(uri == null){
            return null;
        } else {
            String[] projection = { MediaStore.Video.VideoColumns.DATA };
            Cursor cursor = this.getContext().getContentResolver().query(uri, projection,
                    null, null, null);

            if (cursor != null){
                int col_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();

                return cursor.getString(col_index);
            }
        }
        return null;
    }
}