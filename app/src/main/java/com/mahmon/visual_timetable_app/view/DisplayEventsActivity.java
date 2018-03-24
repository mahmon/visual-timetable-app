package com.mahmon.visual_timetable_app.view;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;
import com.mahmon.visual_timetable_app.model.Event;
import com.mahmon.visual_timetable_app.model.EventAdapter;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

// Class to display events, implements the adapter and a listener
public class DisplayEventsActivity extends BaseActivity
        implements EventAdapter.OnItemClickListener {

    // Constant used to assign arbitrary value to image pick
    private static final int PICK_IMAGE_REQUEST = 1;
    // Variables for RecyclerView and Adapter
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManger;
    private EventAdapter mAdapter;
    // Progress bars shown while adapter loads or image updates
    private ProgressBar mProgressCircle;
    private ProgressBar mProgressBar;
    // Variables for Firebase connections
    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    // Listener variable used to kill listener
    private ValueEventListener mDBListener;
    // List used to hold events for display
    private List<Event> mEvents;
    // AlertDialog
    private AlertDialog alertDialog;
    // Variable for imageView in dialog view
    private ImageView editImageView;
    // Variable to store image URI
    private Uri mImageUri;
    // Storage task variable
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_enter_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_save_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_cancel_save);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
        // Attach mRecyclerView to recycler_view
        mRecyclerView = findViewById(R.id.recycler_view);
        // Instantiate LinearLayoutManager for mRecyclerView
        mLayoutManger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManger);
        // Attach mProgressCircle to progress_circle
        mProgressCircle = findViewById(R.id.progress_circle);
        // Instantiate mEvents as ArrayList
        mEvents = new ArrayList<>();
        // Instantiate new EventAdapter
        mAdapter = new EventAdapter(DisplayEventsActivity.this, mEvents);
        // Connect mRecyclerView to  mAdapter
        mRecyclerView.setAdapter(mAdapter);
        // Attach Click listener to EventAdapter
        mAdapter.setOnItemClickListener(DisplayEventsActivity.this);
        // Get Firebase database reference for VISUAL_EVENTS node
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
        // Get Firebase storageRef
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference(VISUAL_EVENTS);
        // Instantiate database listener
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            // Method called on activity load and on any data changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear local list every time to prevent duplicate entry
                mEvents.clear();
                // For loop to iterate through database
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Store each Event in a local Event Object
                    Event event = postSnapshot.getValue(Event.class);
                    // Get event node key from database and set it to local event
                    event.setKey(postSnapshot.getKey());
                    // Add the local Event to local list
                    mEvents.add(event);
                }
                // Update Adapter every time
                mAdapter.notifyDataSetChanged();
                // Hide the progress bar
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
            // Called if database cannot be reached
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display the error message
                Toast.makeText(DisplayEventsActivity.this,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // Hide progress bard
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
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

    // Method call when any list item is clicked, pass list position
    @Override
    public void onItemClick(int position) {
        // Call showUpdateDeleteDialog method below
        showUpdateDeleteDialog(position);
    }

    // Method to Inflate dialog box for updating and deleting events
    private void showUpdateDeleteDialog(final int position) {
        // Instantiate an event object from the list position
        final Event selectedEvent = mEvents.get(position);
        // Instantiate dialogBuilder object
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // Instantiate LayoutInflater object
        LayoutInflater inflater = getLayoutInflater();
        // Instantiate a dialogView and use inflater to inflate XML file to it
        View dialogView = inflater.inflate(R.layout.dialog_update_delete_event, null);
        // Use dialogBuilder setView method passing in dialogView instantiated above
        dialogBuilder.setView(dialogView);
        // Create local variable and link to dialog_title
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        // Create local variable and link to txt_enter_event_heading
        final EditText editTextName = dialogView.findViewById(R.id.txt_enter_event_heading);
        // Progress bar
        mProgressBar = dialogView.findViewById(R.id.progress_bar_edit);
        // Create local variable and link to image_view_edit_upload
        editImageView = dialogView.findViewById(R.id.image_view_current_image);
        // Load the current event image into the edit dialog
        Picasso.with(this)
                .load(selectedEvent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(editImageView);
        // Add a click listener to the image view
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Method call to select new image
                openFileChooser();
            }
        });
        // Create local variable and link to btn_save_edited_event
        final ImageButton buttonUpdate = dialogView.findViewById(R.id.btn_save_edited_event);
        // Create local variable and link to btn_delete_event
        final ImageButton buttonDelete = dialogView.findViewById(R.id.btn_delete_event);
        // Use eventHeading to setText for dialogTitle
        String title = getResources().getString(R.string.txt_update_event_dialog_title)
                + " '" + selectedEvent.getName() + "'";
        dialogTitle.setText(title);
        // Instantiate an AlertDialog object, used dialogBuilder to create it
        alertDialog = dialogBuilder.create();
        // Show the AlertDialog
        alertDialog.show();
        // Attach an onClickListener to buttonUpdate
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If upload task is NOT null and Up load is in progress
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    // Prevents multiple clicks and uploads
                    Toast.makeText(getApplicationContext(),
                            "Event in progress", Toast.LENGTH_SHORT).show();
                } else {
                    // Get text from editTextName, store in local variable
                    String newName = editTextName.getText().toString().trim();
                    // If user has typed in a value...
                    if (!TextUtils.isEmpty(newName)) {
                        // Call the updateEvent method
                        updateEvent(selectedEvent, newName);
                        // If the user has NOT typed in a value...
                    } else {
                        // Get existing name value and overwrite itself
                        updateEvent(selectedEvent, selectedEvent.getName());
                    }
                }
            }
        });
        // Attach an onClickListener to buttonDelete
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the deleteEvent Method
                deleteEvent(selectedEvent);
            }
        });
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
            Picasso.with(this).load(mImageUri).fit().centerCrop().into(editImageView);
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
    private void updateEvent(Event selectedEvent, String newName) {
        // Get event key, generated when dialog inflates
        final String selectedKey = selectedEvent.getKey();
        // Get current file name
        final String uploadId = selectedEvent.getKey();
        /* Update cancelled */
        // If NO event heading added and NO image selected
        if (newName == selectedEvent.getName() && mImageUri == null) {
            Toast.makeText(getApplicationContext(),
                    "No updates entered", Toast.LENGTH_SHORT).show();
            // Close dialog
            closeDialog();
        /* Update event TITLE */
        // If event heading is added and NO image selected
        } else if (newName != selectedEvent.getName() && mImageUri == null) {
            // Use selectedKey to change event heading value
            mDatabaseRef.child(selectedKey).child("name").setValue(newName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Confirm update
                    Toast.makeText(getApplicationContext(),
                    "Event Heading Updated", Toast.LENGTH_SHORT).show();
                    }
            });
            // Close dialog
            closeDialog();
        /* Update event IMAGE */
        // If NO event heading is added and image selected
        } else if (newName == selectedEvent.getName() && mImageUri != null) {
            // Create file name plus image file extension
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
                    }, 500);
                    // Prompt user that upload successful
                    Toast.makeText(getApplicationContext(),
                            "New Image Uploaded", Toast.LENGTH_LONG).show();
                    // Run short delay before closing dialog
                    Handler showHandler = new Handler();
                    showHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Call close dialog method
                            closeDialog();
                        }
                    }, 250);
                }
            }).addOnFailureListener(new OnFailureListener() {
                // Show error message if database write fails
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Call close dialog method
                    closeDialog();
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
        // If event heading is added and image selected
        } else if (newName != selectedEvent.getName() && mImageUri != null) {
            // Use selectedKey to change event heading value
            mDatabaseRef.child(selectedKey).child("name").setValue(newName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Confirm update
                    Toast.makeText(getApplicationContext(),
                            "Event Heading Updated", Toast.LENGTH_SHORT).show();
                }
            });
            // Create file name plus image file extension
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
                    }, 500);
                    // Prompt user that upload successful
                    Toast.makeText(getApplicationContext(),
                            "New Image Uploaded", Toast.LENGTH_LONG).show();
                    // Run short delay before closing dialog
                    Handler showHandler = new Handler();
                    showHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Call close dialog method
                            closeDialog();
                        }
                    }, 250);
                }
            }).addOnFailureListener(new OnFailureListener() {
                // Show error message if database write fails
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Call close dialog method
                    closeDialog();
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
    private void deleteEvent(Event selectedEvent) {
        // Get event key, generated when dialog inflates
        final String selectedKey = selectedEvent.getKey();
        // Generate storageRef from selected event
        StorageReference eventRef = mStorage.getReferenceFromUrl(selectedEvent.getImageUrl());
        // Use storageRef to delete image
        eventRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Following image deletion, delete database entry
                mDatabaseRef.child(selectedKey).removeValue();
                // Confirm deletion
                Toast.makeText(DisplayEventsActivity.this,
                        "Event Deleted", Toast.LENGTH_SHORT).show();
                // Close dialog
                closeDialog();
            }
        });
    }

    // Method called to close dialog
    private void closeDialog() {
        // Dismiss the dialog
        alertDialog.dismiss();
    }

    // Destroy EventListener when Activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}