package com.example.mob_lab1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {
    EditText editName;
    EditText editEmail;
    Button button;
    ImageView user_photo;
    public static final int PICK_IMAGE = 1;
    User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user_photo = findViewById(R.id.user_photo);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        button = findViewById(R.id.save_bt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editN = editName.getText().toString();
                String editE = editEmail.getText().toString();
                user.email = editE;
                user.name = editN;
                myRef.setValue(user);

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.photoUrl != null && !user.photoUrl.isEmpty()) {
                    Picasso.get().load(user.photoUrl).into(user_photo);
                }
                editName.setText(user.name);
                editEmail.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("tag", "Failed to read valueN.", databaseError.toException());
            }
        });
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
                newProfilePic = BitmapFactory.decodeStream(inputStream,null,options);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final StorageReference photoRef = FirebaseStorage.getInstance().getReference("user.jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newProfilePic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteData = baos.toByteArray();

            photoRef.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            user.photoUrl = uri.toString();
                            Picasso.get().load(user.photoUrl).into(user_photo);
                        }
                    });
                }
            });
        }
    }
}
