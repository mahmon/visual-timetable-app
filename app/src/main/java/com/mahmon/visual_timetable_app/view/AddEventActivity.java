package com.mahmon.visual_timetable_app.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;
import com.squareup.picasso.Picasso;

// Class to manage AddEventActivity
public class AddEventActivity extends BaseActivity {

    // Used to identify image request
    private static final int PICK_IMAGE_REQUEST = 1;
    // Variable to identify chosen image location
    private Uri mImageUri;
    // Local variable for btn_choose_image
    private Button mBtnChooseImage;
    // Local variable for img_selected_image
    private ImageView mSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this activity to the relevant XML layout
        setContentView(R.layout.activity_add_event);
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

}
