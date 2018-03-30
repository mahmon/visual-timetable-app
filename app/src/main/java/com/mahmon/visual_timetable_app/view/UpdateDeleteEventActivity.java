package com.mahmon.visual_timetable_app.view;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import java.util.Date;

import static com.mahmon.visual_timetable_app.view.AddEventActivity.parseDate;

public class UpdateDeleteEventActivity extends BaseActivity {

    // Variable to store edited date
    private int mDate;
    // Broadcast receiver used to get values from date picker
    private BroadcastReceiver localBroadcastReceiverEditDate;
    // Constant used to assign arbitrary value to image pick
    private static final int PICK_IMAGE_REQUEST = 1;
    // Variables to store receive data
    private int selectedEventDate;
    private String selectedEventName;
    private String selectedEventImageUrl;
    private String selectedEventDescription;
    private String selectedEventKey;
    // Variable to store Uri of new image
    private Uri mImageUri;
    // View variables
    private Button mButtonDate;
    private EditText mEditTextName;
    private ImageView mEditImageView;
    private EditText mEditTextDescription;
    // Variables for Firebase connections
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private ProgressBar mProgressBar;
    // Storage task variable
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_event);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_enter_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_exit_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_out);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_in);
        getToolBarBottom().getMenu().removeItem(R.id.btn_add_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        // Get Firebase database reference for VISUAL_EVENTS node
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
        // Get Firebase storageRef
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference(VISUAL_EVENTS);
        // Receive intent data from DisplayEventsActivity
        Intent intent = getIntent();
        // Copy bundle to local bundle
        Bundle data = intent.getExtras();
        // Write bundle data to local variables
        selectedEventDate = data.getInt("EXTRA_EVENT_DATE");
        selectedEventName = data.getString("EXTRA_EVENT_NAME");
        selectedEventImageUrl = data.getString("EXTRA_EVENT_IMAGE_URL");
        selectedEventDescription = data.getString("EXTRA_EVENT_DESCRIPTION");
        selectedEventKey = data.getString("EXTRA_EVENT_KEY");
        // Used to check if new date picked
        mDate = selectedEventDate;
        // Create local variable and link to mButtonDate
        mButtonDate = findViewById(R.id.btn_edit_date);
        // Set button text to show current date (formatted)
        mButtonDate.setText(formatDateIntToDateString(selectedEventDate));
        // Set button text color to dim
        mButtonDate.setTextColor(getResources().getColor(R.color.snowmanColorAccent));
        // Set onClick listener for date editor button
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call picker dialog defined below
                showDatePickerDialog();
            }
        });
        // Create local variable and link to edit_txt_enter_event_heading
        mEditTextName = findViewById(R.id.edit_txt_enter_event_heading);
        // Set hint text to selectedEventName
        mEditTextName.setHint(selectedEventName);
        // Create local variable and link to image_view_edit_upload
        mEditImageView = findViewById(R.id.image_view_current_image);
        // Load the current event image into the edit dialog
        Picasso.with(this)
                .load(selectedEventImageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(mEditImageView);
        // Add a click listener to the image view
        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Method call to select new image
                openFileChooser();
            }
        });
        // Create local variable and link to edit_txt_update_description
        mEditTextDescription = findViewById(R.id.edit_txt_update_description);
        // Set hint text to selectedEventName
        mEditTextDescription.setHint(selectedEventDescription);
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
                        // If upload task is NOT null and Up load is in progress
                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            // Prevents multiple clicks and uploads
                            Toast.makeText(getApplicationContext(),
                                    "Event in progress", Toast.LENGTH_SHORT).show();
                        } else {
                            // Get text from editTextName, store in local variable
                            String newEventName = mEditTextName.getText().toString().trim();
                            // Get text from editTextDescription, store in local variable
                            String newEventDescription = mEditTextDescription.getText().toString().trim();
                            // If user entered NAME only
                            if (!TextUtils.isEmpty(newEventName)
                                    && TextUtils.isEmpty(newEventDescription)) {
                                // Call updateEvent, pass new name and existing description
                                updateEvent(mDate, newEventName, selectedEventDescription);
                            // If user entered DESCRIPTION only
                            } else if (TextUtils.isEmpty(newEventName)
                                    && !TextUtils.isEmpty(newEventDescription)) {
                                // Call updateEvent, pass existing name and new description
                                updateEvent(mDate, selectedEventName, newEventDescription);
                            // If user entered NAME and DESCRIPTION
                            } else if (!TextUtils.isEmpty(newEventName)
                                    && !TextUtils.isEmpty(newEventDescription)) {
                                // Call updateEvent, pass new name and new description
                                updateEvent(mDate, newEventName, newEventDescription);
                            // If user entered no text
                            } else {
                                // Call updateEvent, pass existing name and existing description
                                updateEvent(mDate, selectedEventName, selectedEventDescription);
                            }
                        }
                        return true;
                    // User clicked btn_delete_event
                    case R.id.btn_delete_event:
                        // Call delete event method
                        deleteEvent();
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
        // Instantiate local broadcast receiver for dates
        localBroadcastReceiverEditDate = new LocalBroadcastReceiverEditDate();
        // Progress bar
        mProgressBar = findViewById(R.id.progress_bar_edit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register broadcast receiver to get date from dialog on resume
        LocalBroadcastManager.getInstance(this).registerReceiver(
                localBroadcastReceiverEditDate,
                new IntentFilter("GET_DATE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister broadcast receiver on pause
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                localBroadcastReceiverEditDate);
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
            Picasso.with(this).load(mImageUri).fit().centerCrop().into(mEditImageView);
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
    private class LocalBroadcastReceiverEditDate extends BroadcastReceiver {
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
                // Save dateAsInt into mDate
                mDate = dateBundle.getInt("dateAsInt");
                // Update text on button
                mButtonDate.setText(formatDateIntToDateString(mDate));
                // Set button text color to bright
                mButtonDate.setTextColor(getResources().getColor(R.color.snowmanColorFont));

            }
        }
    }

    // Method to create formatted date text for button text
    public String formatDateIntToDateString (int date) {
        // Create String from int
        String dateAsString = "" + date;
        // Create Date object from dateAsString
        Date mDateAsDate = parseDate(dateAsString);
        // Convert date object back to String in date format
        String formattedDate = String.format("%1$s %2$tB %2$td, %2$tY", "", mDateAsDate);
        // Return String formattedDate
        return formattedDate;
    }

    /* Method - Update date */
    private void updateDate(int date) {
        // If user has picked a new date
        if (date != selectedEventDate) {
            // Use selectedKey to change event heading value
            mDatabaseRef.child(selectedEventKey).child("date").setValue(date);
        }
    }

    /* Method - Update name */
    private void updateName(String name) {
        // If user has entered a new name
        if (!name.equals(selectedEventName)) {
            // Use selectedKey to change event heading value
            mDatabaseRef.child(selectedEventKey).child("name").setValue(name);
        }
    }

    /* Method - Update Description */
    private void updateDescription(String description) {
        // If user has entered a new description
        if (!description.equals(selectedEventDescription)) {
            // Use selectedKey to change event heading value
            mDatabaseRef.child(selectedEventKey).child("description").setValue(description);
        }
    }

    /* Method - Update Image */
    private void updateImage() {
        // If user has entered a new image
        if (mImageUri != null) {
            // Delete existing image
            // Generate storageRef from selected event
            StorageReference deleteRef = mStorage.getReferenceFromUrl(selectedEventImageUrl);
            // Use storageRef to delete image
            deleteRef.delete();
            // Write new image to storage
            // Create a file name for the image using the unique key
            StorageReference fileReference = mStorageRef.child(selectedEventKey
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
                    // Get new URL
                    String newImageURL = taskSnapshot.getDownloadUrl().toString();
                    // Write new URL to existing event
                    // Use selectedKey to change event heading value
                    mDatabaseRef.child(selectedEventKey)
                            .child("imageUrl").setValue(newImageURL)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Prompt that image has uploaded
                            Toast.makeText(UpdateDeleteEventActivity.this,
                                    "Image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                // Show error message if database write fails
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateDeleteEventActivity.this,
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
    }

    /* UPDATE: Update event in database */
    // Method called to Update Events
    private void updateEvent(int date, String name, String description) {
        // Check if Nothing entered
        if (    name.equals(selectedEventName)
                && mImageUri == null
                && description.equals(selectedEventDescription)
                && mDate == selectedEventDate) {
            // Prompt user to enter some update
            Toast.makeText(getApplicationContext(),
                    "No updates entered", Toast.LENGTH_SHORT).show();
        } else {
            // Call update methods
            updateImage();
            updateDate(date);
            updateName(name);
            updateDescription(description);
            // Confirm update
            Toast.makeText(getApplicationContext(),
                    "Event Updated", Toast.LENGTH_SHORT).show();
            // Finish Activity and return to DisplayEvents
            openDisplayEventsActivity();
        }
    }

    /* DELETE: Delete events from database */
    // Method called to Delete Events
    private void deleteEvent() {
        // Generate storageRef from selected event
        StorageReference deleteRef = mStorage.getReferenceFromUrl(selectedEventImageUrl);
        // Use storageRef to delete image
        deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Following image deletion, delete database entry
                mDatabaseRef.child(selectedEventKey).removeValue();
                // Confirm deletion
                Toast.makeText(getBaseContext(),
                        "Event Deleted", Toast.LENGTH_SHORT).show();
                // Finish Activity and return to DisplayEvents
                openDisplayEventsActivity();
            }
        });
    }

    // Method called to close activity and openDisplayEventsActivity
    public void openDisplayEventsActivity() {
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        startActivity(intent);
    }

}
