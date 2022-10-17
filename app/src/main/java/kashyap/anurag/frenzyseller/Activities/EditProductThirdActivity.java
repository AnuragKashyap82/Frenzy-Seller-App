package kashyap.anurag.frenzyseller.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityEditProductThirdBinding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditProductThirdActivity extends AppCompatActivity {
    private ActivityEditProductThirdBinding binding;

    private String timestamp;

    private Uri image_uri1 = null;
    private Uri image_uri2 = null;
    private Uri image_uri3 = null;
    private Uri image_uri4 = null;
    private Uri image_uri5 = null;
    private Uri image_uri6 = null;

    private ProgressDialog progressDialog;
    private String uploadedImageUrl, uploadedImageUrl1, uploadedImageUrl2, uploadedImageUrl3, uploadedImageUrl4, uploadedImageUrl5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductThirdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        timestamp = getIntent().getStringExtra("timestamp");
        progressDialog = new ProgressDialog(this);

        loadAllImages1();
        loadAllImages2();
        loadAllImages3();
        loadAllImages4();
        loadAllImages5();
        loadAllImages6();

        binding.sendToQcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFireBase();
            }
        });
        binding.uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageGallery("bannerO");
            }
        });
        binding.uploadBackViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageGallery1("banner1");
            }
        });
        binding.uploadSideView1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageGallery2("banner2");
            }
        });
        binding.uploadSideView2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageGallery3("banner3");
            }
        });
        binding.uploadMore1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageGallery4("banner4");
            }
        });
        binding.uploadMore2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageGallery5("banner5");
            }
        });
    }
    private void pickImageGallery(String bannerO) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher0.launch(intent);

    }
    private void pickImageGallery1(String banner1) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher1.launch(intent);

    }

    private void pickImageGallery2(String banner2) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher2.launch(intent);

    }
    private void pickImageGallery3(String banner3) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher3.launch(intent);

    }
    private void pickImageGallery4(String banner4) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher4.launch(intent);

    }
    private void pickImageGallery5(String banner5) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher5.launch(intent);

    }
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher0 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri1 = data.getData();
                        binding.productIv.setImageURI(image_uri1);
                        uploadImages(timestamp);

                    } else {
                        Toast.makeText(EditProductThirdActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );private ActivityResultLauncher<Intent> galleryActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri2 = data.getData();
                        binding.backProductIv.setImageURI(image_uri2);
                        uplodImage1(timestamp);

                    } else {
                        Toast.makeText(EditProductThirdActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );private ActivityResultLauncher<Intent> galleryActivityResultLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri3 = data.getData();
                        binding.side1ProductIv.setImageURI(image_uri3);
                        uplodImage2(timestamp);
                    } else {
                        Toast.makeText(EditProductThirdActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );private ActivityResultLauncher<Intent> galleryActivityResultLauncher3 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri4 = data.getData();
                        binding.side2ProductIv.setImageURI(image_uri4);
                        uplodImage3(timestamp);
                    } else {
                        Toast.makeText(EditProductThirdActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher4 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri5 = data.getData();
                        binding.moreIv.setImageURI(image_uri5);
                        uplodImage4(timestamp);
                    } else {
                        Toast.makeText(EditProductThirdActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher5 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri6 = data.getData();
                        binding.moreIv2.setImageURI(image_uri6);
                        uplodImage5(timestamp);
                    } else {
                        Toast.makeText(EditProductThirdActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private void uploadImages(String timestamp) {
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String filePathAndName = "ProductsImages/" + timestamp + 0;
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(image_uri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        uploadedImageUrl = "" + uriTask.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("productImage", "" + uploadedImageUrl);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("0");
                        databaseReference.updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProductThirdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProductThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductThirdActivity.this, "Failed to upload image due to\"+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage((int) p + "% uploading");
                    }
                });
    }
    private void uplodImage1(String timestamp) {
        progressDialog.setTitle("Uploading Product");
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String filePathAndName = "ProductsImages/" +timestamp+ 1;
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(image_uri2)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        uploadedImageUrl1 = "" + uriTask.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("productImage", "" + uploadedImageUrl1);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("1");
                        databaseReference.updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            progressDialog.dismiss();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProductThirdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProductThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductThirdActivity.this, "Failed to upload image due to\"+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage((int) p + "% uploading");
                    }
                });
    }
    private void uplodImage2(String timestamp) {
        progressDialog.setTitle("Uploading Product");
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String filePathAndName = "ProductsImages/" +timestamp+ 2;
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(image_uri3)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        uploadedImageUrl2 = "" + uriTask.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("productImage", "" + uploadedImageUrl2);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("2");
                        databaseReference.updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            progressDialog.dismiss();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProductThirdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProductThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductThirdActivity.this, "Failed to upload image due to\"+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage((int) p + "% uploading");
                    }
                });
    }
    private void uplodImage3(String timestamp) {
        progressDialog.setTitle("Uploading Product");
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String filePathAndName = "ProductsImages/" +timestamp+ 3;
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(image_uri4)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        uploadedImageUrl3 = "" + uriTask.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("productImage", "" + uploadedImageUrl3);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("3");
                        databaseReference.updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            progressDialog.dismiss();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProductThirdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProductThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductThirdActivity.this, "Failed to upload image due to\"+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage((int) p + "% uploading");
                    }
                });
    }
    private void uplodImage4(String timestamp) {
        progressDialog.setTitle("Uploading Product");
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String filePathAndName = "ProductsImages/" +timestamp+ 4;
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(image_uri5)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        uploadedImageUrl4 = "" + uriTask.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("productImage", "" + uploadedImageUrl4);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("4");
                        databaseReference.updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            progressDialog.dismiss();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProductThirdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProductThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductThirdActivity.this, "Failed to upload image due to\"+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage((int) p + "% uploading");
                    }
                });
    }
    private void uplodImage5(String timestamp) {
        progressDialog.setTitle("Uploading Product");
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String filePathAndName = "ProductsImages/" +timestamp+ 5;
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(image_uri6)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        uploadedImageUrl5 = "" + uriTask.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("productImage", "" + uploadedImageUrl5);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("5");
                        databaseReference.updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProductThirdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProductThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductThirdActivity.this, "Failed to upload image due to\"+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage((int) p + "% uploading");
                    }
                });
    }
    private void addToFireBase() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isDetailsComplete", true);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document("" + timestamp);
        documentReference
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProductThirdActivity.this, "Product Updated", Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(EditProductThirdActivity.this, ProductAddedActivity.class));

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditProductThirdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProductThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void loadAllImages1() {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    uploadedImageUrl = snapshot.child("productImage").getValue().toString();
                    try {
                        Picasso.get().load(uploadedImageUrl).placeholder(R.drawable.ic_cart_black).into(binding.productIv);
                    } catch (Exception e) {
                        binding.productIv.setImageResource(R.drawable.ic_cart_black);
                    }
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadAllImages2() {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    uploadedImageUrl1 = snapshot.child("productImage").getValue().toString();
                    try {
                        Picasso.get().load(uploadedImageUrl1).placeholder(R.drawable.ic_cart_black).into(binding.backProductIv);
                    } catch (Exception e) {
                        binding.backProductIv.setImageResource(R.drawable.ic_cart_black);
                    }
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAllImages3() {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("2");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    uploadedImageUrl2 = snapshot.child("productImage").getValue().toString();
                    try {
                        Picasso.get().load(uploadedImageUrl2).placeholder(R.drawable.ic_cart_black).into(binding.side1ProductIv);
                    } catch (Exception e) {
                        binding.side1ProductIv.setImageResource(R.drawable.ic_cart_black);
                    }
                   progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadAllImages4() {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("3");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    uploadedImageUrl3 = snapshot.child("productImage").getValue().toString();
                    try {
                        Picasso.get().load(uploadedImageUrl3).placeholder(R.drawable.ic_cart_black).into(binding.side2ProductIv);
                    } catch (Exception e) {
                        binding.side2ProductIv.setImageResource(R.drawable.ic_cart_black);
                    }
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadAllImages5() {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("4");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    uploadedImageUrl4 = snapshot.child("productImage").getValue().toString();
                    try {
                        Picasso.get().load(uploadedImageUrl4).placeholder(R.drawable.ic_cart_black).into(binding.moreIv);
                    } catch (Exception e) {
                        binding.moreIv.setImageResource(R.drawable.ic_cart_black);
                    }
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAllImages6() {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productImages").child(timestamp).child("BannerImages").child("5");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    uploadedImageUrl5 = snapshot.child("productImage").getValue().toString();
                    try {
                        Picasso.get().load(uploadedImageUrl5).placeholder(R.drawable.ic_cart_black).into(binding.moreIv2);
                    } catch (Exception e) {
                        binding.moreIv2.setImageResource(R.drawable.ic_cart_black);
                    }
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}