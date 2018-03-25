package com.mahmon.visual_timetable_app.view;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
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

public class UpdateDeleteEventActivity extends BaseActivity {

    // Constant used to assign arbitrary value to image pick
    private static final int PICK_IMAGE_REQUEST = 1;

    // Variables to store receive data
    private String selectedEventName;
    private String selectedEventImageUrl;
    private String selectedEventKey;
    // Variable to store Uri of new image
    private Uri mImageUri;
    // View variables
    private ImageView mEditImageView;
    private EditText mEditText;
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
        selectedEventName = data.getString("EXTRA_EVENT_NAME");
        selectedEventImageUrl = data.getString("EXTRA_EVENT_IMAGE_URL");
        selectedEventKey = data.getString("EXTRA_EVENT_KEY");
        // Create local variable and link to txt_enter_event_heading
        mEditText = findViewById(R.id.txt_enter_event_heading);
        // Set hint text to selectedEventName
        mEditText.setHint(selectedEventName);
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
                            String newEventName = mEditText.getText().toString().trim();
                            // If user has typed in a value...
                            if (!TextUtils.isEmpty(newEventName)) {
                                // Call the updateEvent method
                                updateEvent(newEventName);
                                // If the user has NOT typed in a value...
                            } else {
                                // Get existing name value and overwrite itself
                                updateEvent(selectedEventName);
                            }
                        }
                        return true;
                    // User clicked btn_delete_event
                    case R.id.btn_delete_event:
                        // TODO
                        deleteEvent();
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);

        // Progress bar
        mProgressBar = findViewById(R.id.progress_bar_edit);

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

    /* UPDATE: Update event in database */
    // Method called to Update Events
    private void updateEvent(String name) {
        /* Update cancelled */
        // If NO event heading added and NO image selected
        if (name.equals(selectedEventName) && mImageUri == null) {
            Toast.makeText(getApplicationContext(),
                    "No updates entered", Toast.LENGTH_SHORT).show();
        /* Update event TITLE */
            // If event heading is added and NO image selected
        } else if (!name.equals(selectedEventName) && mImageUri == null) {
            // Use selectedKey to change event heading value
            mDatabaseRef.child(selectedEventKey).child("name").setValue(name)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Confirm update
                    Toast.makeText(getApplicationContext(),
                            "Event Heading Updated", Toast.LENGTH_SHORT).show();
                }
            });
            // Finish Activity and return to DisplayEvents
            openDisplayEventsActivity();
        /* Update event IMAGE */
        // If NO event heading is added and image selected
        } else if (name.equals(selectedEventName) && mImageUri != null) {
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
                    mDatabaseRef.child(selectedEventKey).child("imageUrl")
                            .setValue(newImageURL).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Confirm update
                            Toast.makeText(getApplicationContext(),
                                    "Event Image Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Finish Activity and return to DisplayEvents
                    openDisplayEventsActivity();
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
        /* Update event TITLE and IMAGE */
        // If NO event heading is added and image selected
        } else if (!name.equals(selectedEventName) && mImageUri != null) {
            // Use selectedKey to change event heading value
            mDatabaseRef.child(selectedEventKey).child("name").setValue(name);
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
            mUploadTask = fileReference.putFile(mImageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                    mDatabaseRef.child(selectedEventKey).child("imageUrl")
                            .setValue(newImageURL).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        // Confirm update
                        Toast.makeText(getApplicationContext(),
                                "Event Heading and Image Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Finish Activity and return to DisplayEvents
                    openDisplayEventsActivity();
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
