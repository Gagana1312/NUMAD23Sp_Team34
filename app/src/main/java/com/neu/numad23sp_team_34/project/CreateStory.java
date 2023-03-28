package com.neu.numad23sp_team_34.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.neu.numad23sp_team_34.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CreateStory extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextStoryTitle, editTextStoryDescription, editTextItinerary, editTextReview;
    private ImageView storyImageView;
    private Button buttonAddImage, buttonSubmit;
    private RatingBar ratingBar;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;

    private static final int CAPTURE_IMAGE_REQUEST = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createstory);

        Places.initialize(getApplicationContext(), "AIzaSyC0YKtZG9Gq0bA8slXRbBbvRlaw3IxsI8c");


        editTextStoryTitle = findViewById(R.id.editTextStoryTitle);
        editTextStoryDescription = findViewById(R.id.editTextStoryDescription);
        editTextItinerary = findViewById(R.id.editTextItinerary);
        editTextReview = findViewById(R.id.editTextReview);
        storyImageView = findViewById(R.id.storyImageView);
        buttonAddImage = findViewById(R.id.buttonAddImage);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        ratingBar = findViewById(R.id.ratingBar);

        Button buttonAddLocation = findViewById(R.id.buttonAddLocation);

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlacesAutocomplete();
            }
        });




        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitStory();
            }
        });


    }

    private void launchPlacesAutocomplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAPTURE_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void chooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setItems(new CharSequence[]{"Take a photo", "Choose from gallery"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: // Take a photo
                                        captureImage();
                                        break;
                                    case 1: // Choose from gallery
                                        selectImageFromGallery();
                                        break;
                                }
                            }
                        })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAPTURE_IMAGE_REQUEST);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                storyImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            storyImageView.setImageBitmap(imageBitmap);
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String placeName = place.getName();
                String placeAddress = place.getAddress();
                addLocationToItinerary(placeName, placeAddress);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("MainActivity", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void addLocationToItinerary(String placeName, String placeAddress) {
        String currentItinerary = editTextItinerary.getText().toString();
        String locationEntry = placeName + " - " + placeAddress + "\n";
        String updatedItinerary = currentItinerary + locationEntry;
        editTextItinerary.setText(updatedItinerary);
    }

    private void submitStory() {
        String storyTitle = editTextStoryTitle.getText().toString();
        String storyDescription = editTextStoryDescription.getText().toString();
        String itinerary = editTextItinerary.getText().toString();
        String review = editTextReview.getText().toString();
        float rating = ratingBar.getRating();

        // Perform validation checks and store the data in your preferred way (e.g., local database, remote server, etc.)
        // ...

        Toast.makeText(this, "Story submitted successfully!", Toast.LENGTH_SHORT).show();
    }
}