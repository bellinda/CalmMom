package com.angelova.w510.calmmom;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.dialogs.AddIllnessDialog;
import com.angelova.w510.calmmom.models.Illness;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileText;
    private LinearLayout mIllnessesLayout;

    private static final int SELECT_FILE = 1023;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        initializeActivity();
    }

    private void initializeActivity() {
        mProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        mProfileText = (TextView) findViewById(R.id.profile_text);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(InfoActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InfoActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
                }
                else {
                    openGallery();
                }
            }
        });

        mIllnessesLayout = (LinearLayout) findViewById(R.id.illnesses_view);
        mIllnessesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Illness> illnesses = new ArrayList<>();
                //adding a default item
                illnesses.add(new Illness());

                AddIllnessDialog dialog = new AddIllnessDialog(InfoActivity.this, illnesses, new AddIllnessDialog.DialogClickListener() {
                    @Override
                    public void onSave(List<Illness> receivedIllnesses) {

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SELECT_FILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap d = BitmapFactory.decodeFile(picturePath);
            int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
            Bitmap scaledImage = Bitmap.createScaledBitmap(d, 512, nh, true);
            mProfileImage.setImageBitmap(scaledImage);
            mProfileText.setVisibility(View.GONE);
        }
    }
}
