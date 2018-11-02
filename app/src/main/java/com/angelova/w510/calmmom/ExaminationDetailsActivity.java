package com.angelova.w510.calmmom;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.angelova.w510.calmmom.adapters.MeasurementsAdapter;
import com.angelova.w510.calmmom.adapters.TestsAdapter;
import com.angelova.w510.calmmom.dialogs.ListDialog;
import com.angelova.w510.calmmom.dialogs.LoadingDialog;
import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationDocument;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.models.Measurement;
import com.angelova.w510.calmmom.models.Test;
import com.angelova.w510.calmmom.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
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
import com.google.gson.Gson;
import com.jsibbold.zoomage.ZoomageView;
import com.melnykov.fab.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private TextView mDocumentsCounter;
    private ImageView mDocumentsViewBtn;
    private TextView mDateView;

    private LinearLayout mImagesView;
    private ProgressBar mImagesLoader;

    private TestsAdapter mTestsAdapter;
    private MeasurementsAdapter mMeasurementsAdapter;

    private Examination mExamination;
    private User mUser;
    private String mUserEmail;

    private List<Test> mTests;
    private List<Measurement> mMeasurements;

    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    private boolean isDocumentsSelected = false;
    private List<String> selectedFilesNames = new ArrayList<>();
    private List<Uri> selectedUris = new ArrayList<>();

    private int mImagesHeight;

    private LoadingDialog mLoadingDialog;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

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
        mDocumentsCounter = (TextView) findViewById(R.id.documents_count);
        mDocumentsViewBtn = (ImageView) findViewById(R.id.view_documents_btn);
        mImagesSelector = (TextView) findViewById(R.id.images_selector);
        mImagesView = (LinearLayout) findViewById(R.id.img_layout);
        mImagesLoader = (ProgressBar) findViewById(R.id.images_loader);
        mDateView = (TextView) findViewById(R.id.date_text_view);

        mExamination = (Examination) getIntent().getSerializableExtra("examination");
        if (mExamination.getTests() != null && mExamination.getTests().size() > 0) {
            mTests = mExamination.getTests();
            mTestsAdapter = new TestsAdapter(this, mTests, mExamination.getStatus() == ExaminationStatus.COMPLETED);
        } else {
            Resources bgResources = getLocalizedResources(ExaminationDetailsActivity.this, new Locale("bg"));
            Resources enResources = getLocalizedResources(ExaminationDetailsActivity.this, new Locale("en"));
            mTests = Arrays.asList(new Test(bgResources.getString(R.string.examination_no_tests), enResources.getString(R.string.examination_no_tests)));
            mTestsAdapter = new TestsAdapter(this, mTests, mExamination.getStatus() == ExaminationStatus.COMPLETED);
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
        mMeasurementsAdapter = new MeasurementsAdapter(this, mMeasurements, mExamination.getStatus() == ExaminationStatus.COMPLETED);
        mLayoutManager = new LinearLayoutManager(this);
        mListMeasurements.setLayoutManager(mLayoutManager);
        mListMeasurements.setItemAnimator(new DefaultItemAnimator());
        mListMeasurements.setAdapter(mMeasurementsAdapter);

        mLoadingDialog = new LoadingDialog(this, getString(R.string.examination_uploading));

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
                if (!mNewItemInput.getText().toString().isEmpty()) {
                    if (mListTests.getVisibility() == View.VISIBLE) {
                        if (mTests == null) {
                            mTests = new ArrayList<>();
                        }
                        Resources bgResources = getLocalizedResources(ExaminationDetailsActivity.this, new Locale("bg"));
                        if (mTests.size() == 1 && mTests.get(0).getTitle().equalsIgnoreCase(bgResources.getString(R.string.examination_no_tests))) {
                            mTests = new ArrayList<>();
                        }
                        if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                            mTests.add(new Test("", mNewItemInput.getText().toString()));
                        } else {
                            mTests.add(new Test(mNewItemInput.getText().toString(), ""));
                        }
                        mExamination.setTests(mTests);
                        mTestsAdapter = new TestsAdapter(ExaminationDetailsActivity.this, mTests, mExamination.getStatus() == ExaminationStatus.COMPLETED);
                        mListTests.setAdapter(mTestsAdapter);
                    } else {
                        if (mMeasurements == null) {
                            mMeasurements = new ArrayList<Measurement>();
                        }
                        if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                            mMeasurements.add(new Measurement("", mNewItemInput.getText().toString()));
                        } else {
                            mMeasurements.add(new Measurement(mNewItemInput.getText().toString(), ""));
                        }
                        mExamination.setActivities(mMeasurements);
                        mMeasurementsAdapter.notifyDataSetChanged();
                    }
                    mNewItemInput.setText("");
                    mNewItemInputLayout.setVisibility(View.GONE);
                    updateExaminationInDb();
                } else {
                    if (mListTests.getVisibility() == View.VISIBLE) {
                        showAlertDialogNow(getString(R.string.examination_no_text_adding_test), getString(R.string.examination_no_text_title));
                    } else {
                        showAlertDialogNow(getString(R.string.examination_no_text_adding_mes), getString(R.string.examination_no_text_title));
                    }
                }
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewItemInput.setText("");
                mNewItemInputLayout.setVisibility(View.GONE);
            }
        });

        if (mExamination.getDocuments() == null) {
            mDocumentsCounter.setText(getString(R.string.examination_no_documents));
        } else if (mExamination.getDocuments().size() == 1) {
            mDocumentsCounter.setText(getString(R.string.examination_one_document));
        } else {
            mDocumentsCounter.setText(String.format(getString(R.string.examination_documents), mExamination.getDocuments().size()));
        }

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


        mDocumentsViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExamination.getDocuments() != null && mExamination.getDocuments().size() > 0) {
                    ListDialog dialog = new ListDialog(ExaminationDetailsActivity.this, mExamination.getDocuments(), new ListDialog.DialogClickListener() {
                        @Override
                        public void onItemClicked(ExaminationDocument document) {
                            openWebPage(document, document.getDownloadUri());
                        }
                    });
                    dialog.show();
                }
            }
        });

        if (mExamination.getDate() != null && !TextUtils.isEmpty(mExamination.getDate())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            try {
                Date date = sdf.parse(mExamination.getDate());
                mDateView.setText(dateFormat.format(date));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        } else {
            //mDateView.setText(getString(R.string.time_line_adapter_no_date));
            //mDateView.setText("dd MMM yyyy");
        }

        if (mExamination.getStatus() != ExaminationStatus.COMPLETED) {
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar currentDate = Calendar.getInstance();
                    final Calendar date = Calendar.getInstance();
                    int year = 0;
                    int month = 0;
                    int day = 0;
                    if (mExamination.getDate() != null && !TextUtils.isEmpty(mExamination.getDate())) {
                        try {
                            Date selectedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(mExamination.getDate());
                            Calendar selected = Calendar.getInstance();
                            selected.setTime(selectedDate);
                            year = selected.get(Calendar.YEAR);
                            month = selected.get(Calendar.MONTH);
                            day = selected.get(Calendar.DATE);
                        } catch (ParseException pe) {
                            pe.printStackTrace();
                        }
                    } else {
                        year = currentDate.get(Calendar.YEAR);
                        month = currentDate.get(Calendar.MONTH);
                        day = currentDate.get(Calendar.DATE);
                    }
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ExaminationDetailsActivity.this, R.style.AppTheme_DialogTheme, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                            date.set(year, monthOfYear, dayOfMonth);

                            try {
                                int hour = 0;
                                int minute = 0;
                                if (mExamination.getDate() != null && !TextUtils.isEmpty(mExamination.getDate())) {
                                    Date selectedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(mExamination.getDate());
                                    Calendar selected = Calendar.getInstance();
                                    selected.setTime(selectedDate);
                                    hour = selected.get(Calendar.HOUR_OF_DAY);
                                    minute = selected.get(Calendar.MINUTE);
                                } else {
                                    hour = currentDate.get(Calendar.HOUR_OF_DAY);
                                    minute = currentDate.get(Calendar.MINUTE);
                                }

                                new TimePickerDialog(ExaminationDetailsActivity.this, R.style.AppTheme_DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        date.set(Calendar.MINUTE, minute);
                                        mDateView.setText(new SimpleDateFormat("dd MMM yyyy").format(date.getTime()));
                                        mExamination.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date.getTime()));
                                        updateExaminationInDb();
                                    }
                                }, hour, minute, false).show();
                            } catch (ParseException pe) {
                                pe.printStackTrace();
                            }
                        }
                    }, year, month, day);
                    //datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
                    datePickerDialog.show();
                }
            });
        }
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

                    mLoadingDialog.show();
                    uploadDocument(uri, displayName);
//                    if (isDocumentsSelected) {
//                        if (mExamination.getDocuments() != null) {
//                            mDocumentsCounter.setText(String.format(getString(R.string.examination_documents), mExamination.getDocuments().size() + 1));
//                        } else {
//                            mDocumentsCounter.setText(getString(R.string.examination_no_documents));
//                        }
//                    }
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

    public void updateExaminationMeasurements(List<Measurement> measurements) {
        mExamination.setActivities(measurements);
        updateExaminationInDb();
    }

    public void updateExaminationTests(List<Test> tests) {
        mExamination.setTests(tests);
        updateExaminationInDb();
    }

    public void updateExaminationInDb() {
        ObjectMapper m = new ObjectMapper();
        List<Examination> examinations = new ArrayList<>();
        for (Examination ex : mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getExaminations()) {
            if (ex.getTitle().equals(mExamination.getTitle())) {
                examinations.add(mExamination);
            } else {
                examinations.add(ex);
            }
        }
        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setExaminations(examinations);
        Map<String,Object> user = m.convertValue(mUser, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                        Gson gson = new Gson();
                        String userJson = gson.toJson(mUser);
                        mEditor.putString("user", userJson);
                        mEditor.apply();
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

                        if (mExamination.getDocuments() != null) {
                            mDocumentsCounter.setText(String.format(getString(R.string.examination_documents), mExamination.getDocuments().size()));
                        } else {
                            mDocumentsCounter.setText(getString(R.string.examination_no_documents));
                        }
                    } else {
                        if (mExamination.getImages() == null) {
                            mExamination.setImages(new ArrayList<ExaminationDocument>());
                        }
                        mExamination.getImages().add(new ExaminationDocument(downloadUri.toString(), fileName));
                        updateImagesInList();
                    }

                    updateExaminationInDb();
                    mLoadingDialog.dismiss();
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
            final String imageUrl = images.get(i).getDownloadUri();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from(ExaminationDetailsActivity.this);
                    final View view = factory.inflate(R.layout.dialog_image, null);
                    final ZoomageView image = (ZoomageView) view.findViewById(R.id.image_view);
                    final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.image_progress);
                    TextView okBtn = (TextView) view.findViewById(R.id.ok_button);
                    GlideDrawableImageViewTarget imageViewMainTarget = new GlideDrawableImageViewTarget(image);
                    Glide.with(ExaminationDetailsActivity.this).load(imageUrl)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            }).into(imageViewMainTarget);
                    final AlertDialog.Builder share_dialog = new AlertDialog.Builder(ExaminationDetailsActivity.this);
                    share_dialog.setView(view);
                    final Dialog dialog = share_dialog.show();
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            mImagesView.addView(imageView);
            Glide.with(this).load(images.get(i).getDownloadUri()).into(imageView);
        }
        mImagesLoader.setVisibility(View.GONE);
    }

    public void openWebPage(ExaminationDocument document, String url) {

        LayoutInflater factory = LayoutInflater.from(ExaminationDetailsActivity.this);
        final View view = factory.inflate(R.layout.dialog_document_view, null);
        final WebView doc = (WebView) view.findViewById(R.id.web_view);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.image_progress);
        TextView okBtn = (TextView) view.findViewById(R.id.ok_button);
        doc.getSettings().setJavaScriptEnabled(true);
        if (document.getName().endsWith("pdf")) {
            try {
                url = URLEncoder.encode(url, "UTF-8");
                doc.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            doc.getSettings().setBuiltInZoomControls(true);
            doc.getSettings().setUseWideViewPort(true);
            doc.getSettings().setJavaScriptEnabled(true);
            doc.getSettings().setLoadWithOverviewMode(true);
            doc.loadUrl(url);
        }
        doc.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                doc.setVisibility(View.VISIBLE);
            }
        });

        final AlertDialog.Builder share_dialog = new AlertDialog.Builder(ExaminationDetailsActivity.this);
        share_dialog.setView(view);
        final Dialog dialog = share_dialog.show();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
    }

    @NonNull
    Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ExaminationDetailsActivity.this, ExaminationsActivity.class);
        intent.putExtra("email", mUserEmail);
        startActivity(intent);
    }
}
