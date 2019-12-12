package com.example.mob_lab1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Profile extends AppCompatActivity {
    EditText editName;
    EditText editEmail;
    Button button;
    ImageView user_photo;
    public static final int PICK_IMAGE = 1;
    FirebaseUser firebaseUser;
    Uri newPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user_photo = findViewById(R.id.user_photo);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        button = findViewById(R.id.save_bt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = editName.getText().toString();
                String updatedEmail = editEmail.getText().toString();
                UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder()
                        .setDisplayName(updatedName);
                if (newPhotoUrl != null) {
                    builder.setPhotoUri(newPhotoUrl);
                }
                firebaseUser.updateProfile(builder.build());
                firebaseUser.updateEmail(updatedEmail);
            }
        });

        editEmail.setText(firebaseUser.getEmail());
        editName.setText(firebaseUser.getDisplayName());
        if (firebaseUser.getPhotoUrl() != null) {
            Picasso.get().load(firebaseUser.getPhotoUrl()).into(user_photo);
        }
        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // special intent for Samsung file manager
                Intent sIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                // if you want any file type, you can skip next line
                sIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sIntent.addCategory(Intent.CATEGORY_DEFAULT);

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            InputStream inputStream = null;
            Bitmap newProfilePic = null;
            try {
                inputStream = getContentResolver().openInputStream(data.getData());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                newProfilePic = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final StorageReference photoRef = FirebaseStorage.getInstance().getReference(firebaseUser.getUid() + ".jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newProfilePic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteData = baos.toByteArray();

            photoRef.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newPhotoUrl = uri;
                            Picasso.get().load(newPhotoUrl).into(user_photo);
                        }
                    });
                }
            });
        }
    }
}
