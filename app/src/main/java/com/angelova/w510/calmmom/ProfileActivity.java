package com.angelova.w510.calmmom;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.models.User;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Calendar.DATE;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mImageView;
    private TextView mAddImageText;
    private TextView mNameTextView;
    private TextView mPregnancyIndex;
    private Button mChoosePregnancyBtn;
    private Button mForumBtn;
    private LinearLayout mDDLayout;
    private TextView mDeliveryDate;

    private User mUser;
    private String mUserEmail;

    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    private Uri mFilePath;
    private static final int SELECT_FILE = 1023;

    final Calendar mExpectedDeliveryDate = Calendar.getInstance();

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");

        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        mImageView = (CircleImageView) findViewById(R.id.profile_image);
        mAddImageText = (TextView) findViewById(R.id.profile_text);
        mNameTextView = (TextView) findViewById(R.id.name_field);
        mPregnancyIndex = (TextView) findViewById(R.id.pregnancy_count);
        mChoosePregnancyBtn = (Button) findViewById(R.id.other_pregnancies_btn);
        mForumBtn = (Button) findViewById(R.id.forum_btn);
        mDDLayout = (LinearLayout) findViewById(R.id.dd_view);
        mDeliveryDate = (TextView) findViewById(R.id.delivery_date);

        if (mUser.getProfileImage() != null && !mUser.getProfileImage().isEmpty()) {
            mAddImageText.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(mUser.getProfileImage()).into(mImageView);
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
                }
                else {
                    openGallery();
                }
            }
        });

        mNameTextView.setText(mUser.getName());
        if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).isFirstPregnancy()) {
            mPregnancyIndex.setText(getString(R.string.activity_profile_first_pregnancy));
        } else {
            int pregnanciesCount = mUser.getPregnancyCount();
            if (pregnanciesCount == 2) {
                mPregnancyIndex.setText(getString(R.string.activity_profile_second_pregnancy));
            } else {
                mPregnancyIndex.setText(String.format(Locale.getDefault(), getString(R.string.activity_profile_other_pregnancy), pregnanciesCount));
            }
        }

        mChoosePregnancyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).isFirstPregnancy()) {
                    WarnDialog dialog = new WarnDialog(ProfileActivity.this, "Warning", "This is your first pregnancy, so there is no other information available to be shown", new WarnDialog.DialogClickListener() {
                        @Override
                        public void onClick() {

                        }
                    });
                    dialog.show();
                } else {
                    //TODO: add logic for switching between pregnancies (1st - dialog to select to which pregnancy they want to switch)
                }
            }
        });

        mForumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ForumActivity.class);
                intent.putExtra("user", mUser);
                intent.putExtra("email", mUserEmail);
                startActivity(intent);
            }
        });

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

            if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate() == null || mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate().isEmpty()) {
                Date firstDayOfLM = sdf.parse(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getFirstDayOfLastMenstruation());
                Date deliveryDate = new Date(firstDayOfLM.getTime() + 280 * 24 * 3600 * 1000l);
                mDeliveryDate.setText(sdf.format(deliveryDate));
                mExpectedDeliveryDate.setTime(deliveryDate);
            } else {
                mDeliveryDate.setText(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate());
                mExpectedDeliveryDate.setTime(sdf.parse(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate()));
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        mDDLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mExpectedDeliveryDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                        mDeliveryDate.setText(sdf.format(mExpectedDeliveryDate.getTime()));
                        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setEstimatedDeliveryDate(mDeliveryDate.getText().toString());
                        String firstDayOfLastMenstruation = getFirstDayOfLastMenstruation();
                        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setFirstDayOfLastMenstruation(firstDayOfLastMenstruation);
                        updateUser();
                        mEditor.putBoolean("shouldReload", true).commit();
                    }
                }, mExpectedDeliveryDate.get(Calendar.YEAR), mExpectedDeliveryDate.get(Calendar.MONTH), mExpectedDeliveryDate.get(DATE));
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
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
                mFilePath = data.getData();
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
            mImageView.setImageBitmap(scaledImage);
            mAddImageText.setVisibility(View.GONE);

            uploadUserPhoto();
        }
    }

    private void uploadUserPhoto() {
        final StorageReference ref = mStorageReference.child("images/" + mFilePath.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(mFilePath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    mUser.setProfileImage(downloadUri.toString());

                    updateUser();
                } else {
                    // Handle failures
                    // ...
                    showAlertDialogNow("Failed to upload profile image. Please try again", "Warning");
                }
            }
        });
    }

    private void updateUser() {
        ObjectMapper m = new ObjectMapper();
        Map<String,Object> user = m.convertValue(mUser, Map.class);

        mDb.collection("users").document(mUserEmail).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document " + e.getMessage());
                    }
                });
    }

    private String getFirstDayOfLastMenstruation() {
        Date expectedDeliveryDate = mExpectedDeliveryDate.getTime();
        Date firstDayOfLM = new Date(expectedDeliveryDate.getTime() - 280 * 24 * 3600 * 1000l);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(firstDayOfLM);
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
    }
}
