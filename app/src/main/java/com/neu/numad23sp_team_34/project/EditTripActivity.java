//package com.neu.numad23sp_team_34.project;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.RatingBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.AutocompleteActivity;
//import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
//import com.google.android.material.button.MaterialButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.neu.numad23sp_team_34.R;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class EditTripActivity extends AppCompatActivity {
//
//    private EditText updateEditTextStoryTitle;
//    private EditText updateEditTextStoryDescription;
//    private EditText updateKeywords;
//    private RecyclerView editImageRecyclerView;
//    private MaterialButton buttonAddImage;
//    private RecyclerView editItineraryRecyclerView;
//    private EditText editTextReview;
//    private RatingBar editRatingBar;
//    private MaterialButton buttonPreview;
//    private MaterialButton buttonSubmit;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_trip);
//
//        // Initialize views
//        updateEditTextStoryTitle = findViewById(R.id.updateeditTextStoryTitle);
//        updateEditTextStoryDescription = findViewById(R.id.updateeditTextStoryDescription);
//        updateKeywords = findViewById(R.id.updatekeywords);
//        editImageRecyclerView = findViewById(R.id.editimageRecyclerView);
//        buttonAddImage = findViewById(R.id.buttonAddImage);
//        editItineraryRecyclerView = findViewById(R.id.edititineraryRecyclerView);
//        editTextReview = findViewById(R.id.editTextReview);
//        editRatingBar = findViewById(R.id.editratingBar);
//        buttonPreview = findViewById(R.id.buttonPreview);
//        buttonSubmit = findViewById(R.id.buttonSubmit);
//
//        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchPlacesAutocomplete();
//            }
//        });
//
//        buttonPreview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPreviewDialog();
//            }
//        });
//
//        buttonAddImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseImage();
//            }
//        });
//
//        buttonSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Do Validation for all the fields here
//                submitStory();
//            }
//        });
//    }
//
//    private void launchPlacesAutocomplete() {
//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
//        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                .build(this);
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//    }
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAPTURE_IMAGE_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                captureImage();
//            } else {
//                Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    private void showPreviewDialog() {
//        // Get the data from input fields
//        String title = editTextStoryTitle.getText().toString();
//        String description = editTextStoryDescription.getText().toString();
//        String review = editTextReview.getText().toString();
//        float rating = ratingBar.getRating();
//
//
//
//        // Create a custom dialog
//        Dialog previewDialog = new Dialog(this);
//        previewDialog.setContentView(R.layout.dialog_story_preview);
//
//        // Set the data for the TextViews in the dialog
//        TextView textViewTitle = previewDialog.findViewById(R.id.textViewTitle);
//        TextView textViewDescription = previewDialog.findViewById(R.id.textViewDescription);
//        textViewTitle.setText(title);
//        textViewDescription.setText(description);
//
//        RatingBar previewRatingBar = previewDialog.findViewById(R.id.previewRatingBar);
//        previewRatingBar.setRating(rating);
//
//        // Set the location for the TextView in the dialog
//        RecyclerView dialogImageRecyclerView = previewDialog.findViewById(R.id.dialogImageRecyclerView);
//        ImageAdapter dialogImageAdapter = new ImageAdapter(this, images);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        dialogImageRecyclerView.setLayoutManager(layoutManager);
//        dialogImageRecyclerView.setAdapter(dialogImageAdapter);
//
//        // Set the image for the ImageView in the dialog
//        // Replace the line below with the actual image data
//
//
//
//        // Show the dialog
//        previewDialog.show();
//    }
//
//
//    private void submitStory() {
//        String storyTitle = editTextStoryTitle.getText().toString();
//        String storyDescription = editTextStoryDescription.getText().toString();
//        String review = editTextReview.getText().toString();
//        List<String> keywords = Arrays.asList(editKeywords.getText().toString().split(","));
//        float rating = ratingBar.getRating();
//
//        if(storyTitle.isEmpty()){
//            editTextStoryTitle.setError("Title cannot be empty");
//            editTextStoryTitle.requestFocus();
//            Toast.makeText(getApplicationContext(),"Please add a title",Toast.LENGTH_SHORT);
//        }else if(storyTitle.length()>50){
//            editTextStoryTitle.setError("Only 50 characters");
//            editTextStoryTitle.requestFocus();
//            Toast.makeText(getApplicationContext(),"Title cannot be more than 50 characters",Toast.LENGTH_SHORT);
//        }else if(storyDescription.isEmpty()){
//            editTextStoryDescription.setError("Description cannot be empty");
//            editTextStoryDescription.requestFocus();
//            Toast.makeText(getApplicationContext(),"Please add a description",Toast.LENGTH_SHORT);
//        }if(storyDescription.length()>1000){
//            editTextStoryDescription.setError("Only 1000 characters");
//            editTextStoryDescription.requestFocus();
//            Toast.makeText(getApplicationContext(),"Description cannot be more than 1000 characters",Toast.LENGTH_SHORT);
//        } else if(imageAdapter.getItemCount()==0){
//            Toast.makeText(CreateStory.this,"Please add an image... " +
//                    "",Toast.LENGTH_SHORT).show();
//        } else if(itineraryAdapter.getItemCount()==0){
//            Toast.makeText(CreateStory.this,"Please add location..." +
//                    "",Toast.LENGTH_SHORT).show();
//        } else if(itineraryAdapter.getItemCount()==1){
//            Toast.makeText(CreateStory.this,"Trip needs two locations..." +
//                    "",Toast.LENGTH_SHORT).show();
//        } else if (rating==0.0){
//            Toast.makeText(CreateStory.this,"Rating can not be empty..." +
//                    "",Toast.LENGTH_SHORT).show();
//        } else {
//
//            // Create a unique ID for the story
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stories");
//            String storyId = databaseReference.push().getKey();
//
//            // Upload images to Firebase Storage
//            uploadImagesAndCreateStory(storyId, storyTitle, storyDescription, review, rating, keywords);
//
//            Toast.makeText(this, "Story submitted successfully!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//    }
//
//    private void uploadImagesAndCreateStory(String storyId, String storyTitle, String storyDescription, String review, float rating, List<String> keywords) {
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("stories/images").child(storyId);
//
//        List<String> imageUrls = new ArrayList<>();
//
//        for (int i = 0; i < images.size(); i++) {
//            Bitmap image = images.get(i);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageData = baos.toByteArray();
//
//            StorageReference imageRef = storageReference.child("image_" + i + ".jpg");
//            UploadTask uploadTask = imageRef.putBytes(imageData);
//
//            uploadTask
//                    .addOnFailureListener(e ->
//                            Toast.makeText(CreateStory.this, "Failed to upload image: " + e.getMessage() + "\n Try again", Toast.LENGTH_SHORT).show()
//                    )
//                    .addOnSuccessListener(taskSnapshot -> {
//                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                imageUrls.add(uri.toString());
//                                if (images.size() == imageUrls.size()) {
//                                    // Create a Story object with the input data
//                                    Story story = new Story(storyId, storyTitle, storyDescription, review, rating, itineraryItems, imageUrls, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),  keywords, new ArrayList<>());
//
//                                    // Save the story object to the Realtime Database
//                                    FirebaseDatabase.getInstance().getReference("stories").child(storyId).setValue(story);
//                                }
//                            }
//                        });
//                        Toast.makeText(CreateStory.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }
//
//
//
//
//    private void chooseImage() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Choose an option")
//                .setItems(new CharSequence[]{"Take a photo", "Choose from gallery"},
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which) {
//                                    case 0: // Take a photo
//                                        captureImage();
//                                        break;
//                                    case 1: // Choose from gallery
//                                        selectImageFromGallery();
//                                        break;
//                                }
//                            }
//                        })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }
//
//    private void selectImageFromGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    private void captureImage() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAPTURE_IMAGE_REQUEST);
//        } else {
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                images.add(bitmap);
//                imageAdapter.notifyDataSetChanged();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            images.add(imageBitmap);
//            imageAdapter.notifyDataSetChanged();
//        }
//
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                String placeName = place.getName();
//                String placeAddress = place.getAddress();
//                addLocationToItinerary(placeName, placeAddress);
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i("MainActivity", status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }
//
//    private void addLocationToItinerary(String placeName, String placeAddress) {
//        String locationEntry = "📍 " + placeName + " (" + placeAddress + ")";
//        itineraryItems.add(locationEntry);
//        itineraryAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onRemoveLocation(int position) {
//        itineraryItems.remove(position);
//        itineraryAdapter.notifyDataSetChanged();
//
//    }
//
//
//}
//
//
//
//
