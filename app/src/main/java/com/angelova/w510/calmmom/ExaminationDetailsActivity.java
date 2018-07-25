package com.angelova.w510.calmmom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.angelova.w510.calmmom.adapters.MeasurementsAdapter;
import com.angelova.w510.calmmom.adapters.TestsAdapter;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationDocument;
import com.angelova.w510.calmmom.models.User;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExaminationDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 2077;
    private static final int READ_REQUEST_CODE = 42;

    private RecyclerView mListTests;
    private RecyclerView mListMeasurements;
    private RadioButton mTestsBtn;
    private RadioButton mMeasurementsBtn;
    private FloatingActionButton mAddItemBtn;
    private ConstraintLayout mNewItemInputLayout;
    private EditText mNewItemInput;
    private TextView mOkayBtn;
    private TextView mCancelBtn;
    private TextView mDocumentsSelector;
    private TextView mImagesSelector;

    private LinearLayout mImagesView;
    private ProgressBar mImagesLoader;

    private TestsAdapter mTestsAdapter;
    private MeasurementsAdapter mMeasurementsAdapter;

    private Examination mExamination;
    private User mUser;
    private String mUserEmail;

    private List<String> mTests;
    private List<String> mMeasurements;

    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    private boolean isDocumentsSelected = false;
    private List<String> selectedFilesNames = new ArrayList<>();
    private List<Uri> selectedUris = new ArrayList<>();

    private int mImagesHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mListTests = (RecyclerView) findViewById(R.id.tests_list);
        mListMeasurements = (RecyclerView) findViewById(R.id.mes_list);
        mTestsBtn = (RadioButton) findViewById(R.id.tests);
        mMeasurementsBtn = (RadioButton) findViewById(R.id.measurements);
        mAddItemBtn = (FloatingActionButton) findViewById(R.id.add_new_item_btn);
        mNewItemInputLayout = (ConstraintLayout) findViewById(R.id.new_item_input_layout);
        mNewItemInput = (EditText) findViewById(R.id.new_item_input);
        mOkayBtn = (TextView) findViewById(R.id.okay_btn);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);
        mDocumentsSelector = (TextView) findViewById(R.id.documents_selector);
        mImagesSelector = (TextView) findViewById(R.id.images_selector);
        mImagesView = (LinearLayout) findViewById(R.id.img_layout);
        mImagesLoader = (ProgressBar) findViewById(R.id.images_loader);

        mExamination = (Examination) getIntent().getSerializableExtra("examination");
        if (mExamination.getTests().size() > 0) {
            mTests = mExamination.getTests();
            mTestsAdapter = new TestsAdapter(mTests);
        } else {
            mTestsAdapter = new TestsAdapter(Arrays.asList(getString(R.string.examination_no_tests)));
        }
        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");
        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mListTests.setLayoutManager(mLayoutManager);
        mListTests.setItemAnimator(new DefaultItemAnimator());
        mListTests.setAdapter(mTestsAdapter);

        mMeasurements = mExamination.getActivities();
        mMeasurementsAdapter = new MeasurementsAdapter(mMeasurements);
        mLayoutManager = new LinearLayoutManager(this);
        mListMeasurements.setLayoutManager(mLayoutManager);
        mListMeasurements.setItemAnimator(new DefaultItemAnimator());
        mListMeasurements.setAdapter(mMeasurementsAdapter);

        mTestsBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mListMeasurements.setVisibility(View.GONE);
                    mListTests.setVisibility(View.VISIBLE);
                } else {
                    mListTests.setVisibility(View.GONE);
                    mListMeasurements.setVisibility(View.VISIBLE);
                }
            }
        });

        mMeasurementsBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mListTests.setVisibility(View.GONE);
                    mListMeasurements.setVisibility(View.VISIBLE);
                } else {
                    mListMeasurements.setVisibility(View.GONE);
                    mListTests.setVisibility(View.VISIBLE);
                }
            }
        });

        mAddItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListTests.getVisibility() == View.VISIBLE) {
                    mListTests.scrollToPosition(mTestsAdapter.getItemCount()-1);
                } else {
                    mListMeasurements.scrollToPosition(mMeasurementsAdapter.getItemCount() - 1);
                }

                mNewItemInputLayout.setVisibility(View.VISIBLE);
            }
        });

        mOkayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListTests.getVisibility() == View.VISIBLE) {
                    mTests.add(mNewItemInput.getText().toString());
                    mExamination.setTests(mTests);
                    mTestsAdapter.notifyDataSetChanged();
                } else {
                    mMeasurements.add(mNewItemInput.getText().toString());
                    mExamination.setActivities(mMeasurements);
                    mMeasurementsAdapter.notifyDataSetChanged();
                }
                mNewItemInput.setText("");
                mNewItemInputLayout.setVisibility(View.GONE);
                updateExaminationInDb();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewItemInput.setText("");
                mNewItemInputLayout.setVisibility(View.GONE);
            }
        });

        mDocumentsSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(ExaminationDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ExaminationDetailsActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                } else  {
                    isDocumentsSelected = true;
                    getFilesFromStorage();
                }
            }
        });

        mImagesSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(ExaminationDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ExaminationDetailsActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                } else  {
                    mImagesLoader.setVisibility(View.VISIBLE);
                    getFilesFromStorage();
                }
            }
        });

        final ViewTreeObserver vto = mImagesView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImagesView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = mImagesView.getMeasuredWidth();
                mImagesHeight = mImagesView.getMeasuredHeight();
                if (mExamination.getImages() != null) {
                    showImages();
                }
            }
        });

        //Glide.with(this).load(mExamination.getImages().get(0).getDownloadUri()).into(mImageView);
    }

    private void getFilesFromStorage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        if (!isDocumentsSelected) {
            i.setType("image/*");
        } else {
            String[] mimeTypes = { "image/*", "application/pdf" };
            //i.setType("*/*");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                i.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    i.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                i.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
            }
        }
        startActivityForResult(i, READ_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getFilesFromStorage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            Uri uri = data.getData();
            // do somthing...
            try {
                Cursor cursor = getContentResolver()
                        .query(data.getData(), null, null, null, null, null);
                String displayName = "";

                try {
                    // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
                    // "if there's anything to look at, look at it" conditionals.
                    if (cursor != null && cursor.moveToFirst()) {

                        // Note it's called "Display Name".  This is
                        // provider-specific, and might not necessarily be the file name.
                        displayName = cursor.getString(
                                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }

                if (!selectedFilesNames.contains(displayName)) {
                    selectedFilesNames.add(displayName);
                    //update user documents for current examination

                    uploadDocument(uri, displayName);
//                    if (mSelectedFilesLabel.getVisibility() == View.GONE) {
//                        mSelectedFilesLabel.setVisibility(View.VISIBLE);
//                    }
                    selectedUris.add(uri);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateExaminationInDb() {
        ObjectMapper m = new ObjectMapper();
        List<Examination> examinations = new ArrayList<>();
        for (Examination ex : mUser.getExaminations()) {
            if (ex.getTitle().equals(mExamination.getTitle()) && ex.getDate().equals(mExamination.getDate())) {
                examinations.add(mExamination);
            } else {
                examinations.add(ex);
            }
        }
        mUser.setExaminations(examinations);
        Map<String,Object> user = m.convertValue(mUser, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document " + e);
                    }
                });
    }

    private void uploadDocument(Uri documentPath, final String fileName) {
        final StorageReference ref = mStorageReference.child("documents/" + documentPath.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(documentPath);

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
                    if (isDocumentsSelected) {
                        if (mExamination.getDocuments() == null) {
                            mExamination.setDocuments(new ArrayList<ExaminationDocument>());
                        }
                        mExamination.getDocuments().add(new ExaminationDocument(downloadUri.toString(), fileName));
                        isDocumentsSelected = false;
                    } else {
                        if (mExamination.getImages() == null) {
                            mExamination.setImages(new ArrayList<ExaminationDocument>());
                        }
                        mExamination.getImages().add(new ExaminationDocument(downloadUri.toString(), fileName));
                        updateImagesInList();
                    }

                    updateExaminationInDb();
                } else {
                    // Handle failures
                    // ...
//                    showAlertDialogNow("Failed to upload profile image. Please try again", "Warning");
//                    enableAllEditableFields();
                }
            }
        });
    }

    private void updateImagesInList() {
        mImagesView.removeAllViews();
        showImages();
    }

    private void showImages() {
        mImagesLoader.setVisibility(View.VISIBLE);
        List<ExaminationDocument> images = mExamination.getImages();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mImagesHeight, mImagesHeight);
            params.setMargins(0, 0, 15, 0);
            imageView.setLayoutParams(params);
            imageView.setId(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImagesView.addView(imageView);
            Glide.with(this).load(images.get(i).getDownloadUri()).into(imageView);
        }
        mImagesLoader.setVisibility(View.GONE);
    }
}
