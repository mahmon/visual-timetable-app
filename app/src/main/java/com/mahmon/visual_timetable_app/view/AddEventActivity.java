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

// Class to manage AddEventActivity
public class AddEventActivity extends BaseActivity {

    // View variables
    private EditText mEnterEventHeading;
    private String mEventHeading;
    private ProgressBar mUploadProgressBar;
    private Button mBtnChooseImage;
    private ImageView mSelectedImage;
    // database variables
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    // Used to identify image request
    private static final int PICK_IMAGE_REQUEST = 1;
    // Variable to identify chosen image location
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this activity to the relevant XML layout
        setContentView(R.layout.activity_add_event);
        mUploadProgressBar = findViewById(R.id.upload_progress_bar);
        // Attach Button btn_choose_image to local variable mBtnChooseImage
        mBtnChooseImage = findViewById(R.id.btn_choose_image);
        // Attache imageView img_selected_image to mSelectedImage
        mSelectedImage = findViewById(R.id.img_selected_image);
        // Set a listener for the button mBtnChooseImage
        mBtnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call method defined below
                openFileChooser();
            }
        });
        // Instantiate database and storage eferences linked to VISUAL_EVENTS node
        mStorageRef = FirebaseStorage.getInstance().getReference(VISUAL_EVENTS);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_enter_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_exit_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_out);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_in);
        getToolBarBottom().getMenu().removeItem(R.id.btn_add_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
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

    // set method calls for toolBarMethodsBottom
    public void toolBarMethodsBottom(Toolbar bottomToolbar) {
        // Create listeners for all buttons on menu_tool_bar_bottom
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_save_event
                    case R.id.btn_save_event:
                        // Prevent multiple clicks of the save button
                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(AddEventActivity.this,
                                    "Upload in progress", Toast.LENGTH_SHORT).show();
                        } else {
                            // Call save event method
                            saveEvent();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // Animation override for the default back button:
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.grow_in, R.anim.slide_out);
    }

    // Method called from button mBtnChooseImage
    private void openFileChooser() {
        // Create new intent
        Intent intent = new Intent();
        // Set intent to image type
        intent.setType("image/*");
        // Set intent action to select content
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Start the activity and place call to onActivityResult method
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Method called when openFileChooser method is run
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that an image has been selected
        if(requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            // Get the Uri of the image and storein mImageUri
            mImageUri = data.getData();
            // Use mImageUri to pass image to image view mSelectedImage
            Picasso.with(this).load(mImageUri).into(mSelectedImage);
        }
    }

    // Method called to get file extension of chosen image
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // Method called after saving event to open DisplayEventsActivity
    public void openDisplayEventsActivity() {
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        startActivity(intent);
    }

    /* CREATE: Write to database */
    // Save event to database
    public void saveEvent() {
        // Attach Edit Text txt_enter_event_heading to mEnterEventHeading
        mEnterEventHeading = findViewById(R.id.txt_enter_event_heading);
        // Store Edit Text txt_enter_event_heading in mEventHeading
        mEventHeading = mEnterEventHeading.getText().toString().trim();
        // If EditText box is NOT blank...
        if (!TextUtils.isEmpty(mEventHeading)) {
            // If an Image HAS been selected...
            if (mImageUri != null) {
                // Create a storage reference of currentTimeMillis and the file extension
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(mImageUri));
                // Write the selected image into the database
                mUploadTask = fileReference.putFile(mImageUri)
                        // Success listener
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Create handler to delay reset of progress bar
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mUploadProgressBar.setProgress(0);
                                    }
                                    // Allows user to see bar before reset
                                }, 500);
                                Toast.makeText(AddEventActivity.this,
                                        "Upload successful", Toast.LENGTH_SHORT).show();
                                Event event = new Event(mEnterEventHeading.getText().toString().trim(),
                                        taskSnapshot.getDownloadUrl().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(event);
                                // Call openDisplayEventsActivity to show events
                                openDisplayEventsActivity();
                            }
                        })
                        // Failure listener
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            // Send error message to a toast
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddEventActivity.this,
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        // Progress listener
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            // Method to calculate the progress
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                // Store current progress into a double
                                double progress = (100.0 *
                                        taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                // Cast double into an int and use to set mUploadProgressbar
                                mUploadProgressBar.setProgress((int) progress);
                            }
                        });
            } else {
                // Prompt user to select an image
                Toast.makeText(this,
                        "Please choose an image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Prompt user to enter a heading
            Toast.makeText(this,
                    "Please enter a heading", Toast.LENGTH_SHORT).show();
        }
    }

}
