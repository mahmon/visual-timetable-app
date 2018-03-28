package com.mahmon.visual_timetable_app.view;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;
import com.mahmon.visual_timetable_app.model.Event;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Class to Add events to database
public class AddEventActivity extends BaseActivity {

    // Constant used for formatting selected date
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    // Variable to catch the selected date
    private int mDate;
    // Boolean to check date has been picked
    private boolean mDatePicked;
    // Broadcast receiver used to get values from date picker
    private BroadcastReceiver localBroadcastReceiverDate;
    // Constant used to assign arbitrary value to image pick
    private static final int PICK_IMAGE_REQUEST = 1;
    // Variables for view elements
    private Button mButtonDate;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private EditText mEditTextDescription;
    // Variable to store image URI
    private Uri mImageUri;
    // Database and Storage references
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    // Storage task variable
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_enter_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_exit_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_out);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_in);
        getToolBarBottom().getMenu().removeItem(R.id.btn_add_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_delete_event);
        // Button listener for bottom menu
        getToolBarBottom().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_cancel_save
                    case R.id.btn_cancel_save:
                        // Calls method from BaseActivity
                        cancelAddUpdateDeleteEvent();
                        return true;
                    // User clicked btn_save_event
                    case R.id.btn_save_event:
                        // If upload task is NOT and and Up load is in progress
                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            // Prevents multiple clicks and uploads
                            Toast.makeText(AddEventActivity.this,
                                    "Event in progress", Toast.LENGTH_SHORT).show();
                        } else {
                            // Method cal to save event and image to database
                            checkEventEntered();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
        // Instantiate local broadcast receiver for dates
        localBroadcastReceiverDate = new LocalBroadcastReceiverDate();
        // Set mDatePicked to false
        mDatePicked = false;
        // Attach local view btn_pick_date
        mButtonDate = findViewById(R.id.btn_pick_date);
        // Set button text color to dim
        mButtonDate.setTextColor(getResources().getColor(R.color.colorAccent));
        // Attach local view variables to XML elements
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar_upload);
        mEditTextDescription = findViewById(R.id.edit_text_file_description);
        // Set onClick listener for date picker button
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call picker dialog defined below
                showDatePickerDialog();
            }
        });
        // Instantiate database and storage references
        mStorageRef = FirebaseStorage.getInstance().getReference(VISUAL_EVENTS);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
        // Set click listener for choose image view
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On click open file chooser method
                openFileChooser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register broadcast receiver to get date from dialog on resume
        LocalBroadcastManager.getInstance(this).registerReceiver(
                localBroadcastReceiverDate,
                new IntentFilter("GET_DATE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister broadcast receiver on pause
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                localBroadcastReceiverDate);
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Instantiate menu inflater object
        MenuInflater inflater = getMenuInflater();
        // Set menu_tool_bar_top as default options menu
        inflater.inflate(R.menu.menu_tool_bar_top, topMenu);
        return true;
    }

    // Set method calls for default option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Attach topToolBarMethods to default menu
        toolBarMethodsTop(item);
        // Invoke the superclass to handle unrecognised user action.
        return super.onOptionsItemSelected(item);
    }

    // Method to open file chooser for selecting images from phone
    private void openFileChooser() {
        Intent intent = new Intent();
        // Limit to image files
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Start activity and pass arbitrary image request value
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Method called when file chooser is used
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that image request is valid and an image has been selected
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            // Store the Uri of the image
            mImageUri = data.getData();
            // Use Picasso to pass the image into the view
            Picasso.with(this).load(mImageUri).fit().centerCrop().into(mImageView);
        }
    }

    // Get file extension of the image
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // Inflate date picker, called from XML for btn_pick_date
    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Nested class called to construct local broadcast receiver
    private class LocalBroadcastReceiverDate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check for null entries
            if (intent == null || intent.getAction() == null) {
                return;
            }
            // If intents match...
            if (intent.getAction().equals("GET_DATE")) {
                // Get bundled data and store locally
                Bundle dateBundle = intent.getExtras();
                /* SAVE SELECTED DATE: int mDate used in checkEventEntered method */
                // Save dateAsInt into mDate
                mDate = dateBundle.getInt("dateAsInt");
                /* SET Button text to selected date */
                setButtonTextToDate(mButtonDate);
                // Set button text color to bright
                mButtonDate.setTextColor(getResources().getColor(R.color.colorFont));
                // Set mDatePicked to true
                mDatePicked = true;
            }
        }
    }

    // Method used to parse int received from date picker back into date format
    public static Date parseDate(String dateStr) {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    // Method to create formatted date text for button text
    public void setButtonTextToDate (Button btn) {
        /* SET Button text to selected date */
        // Create String from int mDate
        String mDateAsString = "" + mDate;
        // Create Date object from mDateAsString
        Date mDateAsDate = parseDate(mDateAsString);
        // Convert date object back to String to display on button
        String btnDateText = String.format("%1$s %2$tB %2$td, %2$tY", "" , mDateAsDate);
        // Write selected date onto the button
        btn.setText(btnDateText);
    }

    // Method called when save button is clicked, check user entered required details
    private void checkEventEntered() {
        // If button text has not been set to a date...
        if (mDatePicked) {
            // If EditText box is NOT blank...
            if (!TextUtils.isEmpty(mEditTextFileName.getText().toString().trim())) {
                // If an image has been selected...
                if (mImageUri != null) {
                    uploadEvent();
                    // Reset mDatePicked to false
                    mDatePicked = false;
                } else {
                    // If image has NOT been selected prompt user
                    Toast.makeText(this,
                            "Please select an image", Toast.LENGTH_SHORT).show();
                }
            } else {
                // If user has NOT entered a name, prompt the user
                Toast.makeText(this,
                        "Please enter a heading", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If user has NOT picked a date, prompt the user
            Toast.makeText(this,
                    "Please select a date", Toast.LENGTH_SHORT).show();
        }
    }

    /* WRITE: Write to database */
    private void uploadEvent() {
        // Get a unique key from the database
        final String uploadId = mDatabaseRef.push().getKey();
        // Create a file name for the image using the unique key
        StorageReference fileReference = mStorageRef.child(uploadId
                + "." + getFileExtension(mImageUri));
        // Create storage task, load image to cloud with listener
        mUploadTask = fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            // If successful...
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Handler progHandler = new Handler();
                progHandler.postDelayed(new Runnable() {
                    // Set a short delay to allow progress bar to be viewable
                    @Override
                    public void run() {
                        mProgressBar.setProgress(0);
                    }
                }, 250);
                // Gather details to create object
                int date = mDate;
                String name = mEditTextFileName.getText().toString().trim();
                String description = mEditTextDescription.getText().toString().trim();
                // Create a new event object, pass event name, description and image URL
                Event event = new Event(date, name, taskSnapshot.getDownloadUrl()
                        .toString(), description);
                // Write event to database using key from line above
                mDatabaseRef.child(uploadId).setValue(event);
                // Prompt user that upload successful
                Toast.makeText(AddEventActivity.this,
                        "Event created", Toast.LENGTH_LONG).show();
                // Run short delay before switching activities
                Handler showHandler = new Handler();
                showHandler.postDelayed(new Runnable() {
                @Override
                    public void run() {
                        // Call openDisplayImagesActivity
                        openDisplayImagesActivity();
                    }
                }, 250);
            }
        }).addOnFailureListener(new OnFailureListener() {
            // Show error message if database write fails
            @Override
            public void onFailure(@NonNull Exception e) {
            Toast.makeText(AddEventActivity.this,
                    e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            // Calculate upload percentage and use to animate progress bar
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()
                        / taskSnapshot.getTotalByteCount());
                mProgressBar.setProgress((int) progress);
            }
        });
    }

    // Method called to launch DisplayImagesActivity
    private void openDisplayImagesActivity() {
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        startActivity(intent);
    }

}