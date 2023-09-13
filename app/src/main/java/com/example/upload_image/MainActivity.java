package com.example.upload_image;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import java.io.ByteArrayOutputStream;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button select, previous, next, capture, clear, submit;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    TextView total, imageCountText;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    private final int CAMERA_REQ_CODE = 100;
    private final int targetWidth = 600; // Adjust this width as needed
    private final int targetHeight = 800; // Adjust this height as needed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        select = findViewById(R.id.select);
        // Initialize the TextView
        imageCountText = findViewById(R.id.imageCountText);
        total = findViewById(R.id.text);
        imageView = findViewById(R.id.image);
//        previous = findViewById(R.id.previous);
//        capture = findViewById(R.id.capture);
        clear = findViewById(R.id.clear);
        submit =findViewById(R.id.submit);
        mArrayUri = new ArrayList<Uri>();
        ImageButton cameraButton = findViewById(R.id.cameraButton);
        ImageButton galleryButton = findViewById(R.id.galleryButton);
        ImageButton previousButton = findViewById(R.id.previousButton);
        ImageButton nextButton = findViewById(R.id.nextButton);

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });

//        next = findViewById(R.id.next);

        // click here to select next image
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position < mArrayUri.size() - 1) {
//                    // increase the position by 1
//                    position++;
//                    imageView.setImageURI(mArrayUri.get(position));
//                } else {
//                    Toast.makeText(MainActivity.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        // click here to select next image
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                    // Update the TextView with the current image number and total count
                    updateImageCountText();
                } else {
                    Toast.makeText(MainActivity.this, "Last Image Already Shown!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // click here to view previous image
//        previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position > 0) {
//                    // decrease the position by 1
//                    position--;
//                    imageView.setImageURI(mArrayUri.get(position));
//                }
//            }
//        });
        // click here to view previous image
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));
                    // Update the TextView with the current image number and total count
                    updateImageCountText();
                }else {
                    Toast.makeText(MainActivity.this, "This is the First Image!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // click here to select image
//        select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // initialising intent
//                Intent intent = new Intent();
//                // setting type to select to be image
//                intent.setType("image/*");
//                // allowing multiple image to be selected
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
//            }
//        });
        // click here to select images from gallery
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialising intent
                Intent intent = new Intent();
                // setting type to select to be image
                intent.setType("image/*");
                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
        // click here to capture image from camera
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera, CAMERA_REQ_CODE);
            }
        });
        // click here to capture image from camera
//        capture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(iCamera, CAMERA_REQ_CODE);
//            }
//        });

        // click here to clear the selected image
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArrayUri.size() > 0) {
                    mArrayUri.remove(position);
                    if (position > 0) {
                        position--;
                    }
                    if (mArrayUri.size() > 0) {
                        imageView.setImageURI(mArrayUri.get(position));
                        // Update the TextView with the current image number and total count
                        updateImageCountText();
                    } else {
                        imageView.setImageResource(0); // No images left, clear the ImageSwitcher.
                        // Update the TextView with the current image number and total count
                        updateImageCountText();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No images to clear!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity
                Intent intent = new Intent(MainActivity.this, Output_Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    // adding imageuri in array
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageUri);
                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
                // Update the TextView with the current image number and total count
                updateImageCountText();
            } else {
                Uri imageUri = data.getData();
                mArrayUri.add(imageUri);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
                // Update the TextView with the current image number and total count
                updateImageCountText();
            }
        } else if (requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            // Resize the captured image to a specific width and height
            img = resizeBitmap(img, targetWidth, targetHeight);
            Uri imageUri = getImageUri(img);
            mArrayUri.add(imageUri);
            imageView.setImageURI(mArrayUri.get(position));
            // Update the TextView with the current image number and total count
            updateImageCountText();
        } else {
            // show this if no image is selected or captured
            Toast.makeText(this, "No image selected or captured", Toast.LENGTH_LONG).show();
        }
    }
    private Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
    private Uri getImageUri(Bitmap inImage) {
        // Convert Bitmap to Uri
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    // Add this method to update the image count TextView
    private void updateImageCountText() {
        int totalImages = mArrayUri.size(); // Replace this with the actual count of images
        int currentImageNumber = position + 1; // Current image number (1-based index)
        if(totalImages==0){
            currentImageNumber=0;
        }
        String countText = "Image " + currentImageNumber + "/" + totalImages;
        imageCountText.setText(countText);
    }
}
//
//import static com.example.upload_image.R.id.imgReceived;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.example.upload_image.databinding.ActivityMainBinding;
//
//public class MainActivity extends AppCompatActivity {
//    private final int CAMERA_REQ_CODE = 100;
//    private final int GALLERY_REQ_CODE = 200;
//    ImageView imgReceived;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        imgReceived = findViewById(R.id.imgReceived);
//        Button btnCamera = findViewById(R.id.btnCamera);
//        Button btnGallery = findViewById(R.id.btnGallery);
//
//        btnCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(iCamera,CAMERA_REQ_CODE);
//            }
//        });
//        btnGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent iGallery = new Intent(Intent.ACTION_PICK);
//                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(iGallery,GALLERY_REQ_CODE);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK){
//            if(requestCode==CAMERA_REQ_CODE){
//                //for camera
//                Bitmap img = (Bitmap)data.getExtras().get("data");
//                imgReceived.setImageBitmap(img);
//            }
//            else if(requestCode==GALLERY_REQ_CODE){
//                imgReceived.setImageURI(data.getData());
//            }
//        }
//    }
//}