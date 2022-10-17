package kashyap.anurag.frenzyseller.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityEditProductFirstBinding;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProductFirstActivity extends AppCompatActivity {
    private ActivityEditProductFirstBinding binding;
    private String category, productId;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri image_uri = null;

    private String uploadedImageUrl;

    private String  productPrice, productCuttedPrice, fulfilmentBy, procurementType, noOfQuantity, deliveryFee,
            countryOrigin, manufacturer, packer, zonalDeliveryFee, nationalDeliveryFee;

    private boolean deliveryFeeStatus = false;
    private boolean isDetailsComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        category = getIntent().getStringExtra("category");
        productId = getIntent().getStringExtra("productId");

        progressDialog = new ProgressDialog(this);

        loadProductDetails(productId);
        loadStockQuantity(productId);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        binding.uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.freeDeliverySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    binding.localDeliveryEt.setVisibility(View.VISIBLE);
                    binding.localDeliveryTv.setVisibility(View.VISIBLE);
                } else {
                    binding.localDeliveryEt.setVisibility(View.GONE);
                    binding.localDeliveryTv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadProductDetails(String productId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                 uploadedImageUrl = snapshot.getString("productImage");
                 productPrice = snapshot.getString("productPrice");
                 deliveryFee = snapshot.getString("deliveryFee");
                 productCuttedPrice = snapshot.getString("productCuttedPrice");

                binding.mrpEt.setText(productCuttedPrice);
                binding.sellingPriceEt.setText(productPrice);

                try {
                    Picasso.get().load(uploadedImageUrl).fit().centerCrop().placeholder(R.drawable.ic_cart_black).into(binding.productIv);
                } catch (Exception e) {
                    binding.productIv.setImageResource(R.drawable.ic_cart_black);
                }
                if (deliveryFee.equals("FREE Delivery")){
                    deliveryFeeStatus = false;
                    binding.freeDeliverySwitch.setChecked(false);
                    binding.localDeliveryEt.setVisibility(View.GONE);
                    binding.localDeliveryTv.setVisibility(View.GONE);
                }else {
                    deliveryFeeStatus = true;
                    binding.localDeliveryEt.setText(deliveryFee);
                    binding.freeDeliverySwitch.setChecked(true);
                }
            }
        });
    }
    private void loadStockQuantity(String productId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products").child(productId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String noOfQuantity = snapshot.child("noOfQuantity").getValue().toString();
                binding.stockEt.setText(noOfQuantity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void validateData() {
        productCuttedPrice = binding.mrpEt.getText().toString().trim();
        productPrice = binding.sellingPriceEt.getText().toString().trim();
        fulfilmentBy = binding.fullfilmentTv.getText().toString().trim();
        procurementType = binding.procurementEt.getText().toString().trim();
        noOfQuantity = binding.stockEt.getText().toString().trim();
        deliveryFeeStatus = binding.freeDeliverySwitch.isChecked();
        deliveryFee = binding.localDeliveryEt.getText().toString().trim();
        countryOrigin = binding.countryOriginEt.getText().toString().trim();
        manufacturer = binding.manufacturerEt.getText().toString().trim();
        packer = binding.packerEt.getText().toString().trim();
        zonalDeliveryFee = binding.zonalDeliveryEt.getText().toString().trim();
        nationalDeliveryFee = binding.nationalDeliveryEt.getText().toString().trim();

        if (productCuttedPrice.isEmpty()){
            Toast.makeText(this, "Enter product MRP", Toast.LENGTH_SHORT).show();
        }else if (productPrice.isEmpty()){
            Toast.makeText(this, "Enter product Selling Price", Toast.LENGTH_SHORT).show();
        }else if (fulfilmentBy.isEmpty()){
            Toast.makeText(this, "Select Fulfilment By", Toast.LENGTH_SHORT).show();
        }else if (procurementType.isEmpty()){
            Toast.makeText(this, "Enter procurementType", Toast.LENGTH_SHORT).show();
        }else if (noOfQuantity.isEmpty()){
            Toast.makeText(this, "Enter Stock Available", Toast.LENGTH_SHORT).show();
        }else if (zonalDeliveryFee.isEmpty()){
            Toast.makeText(this, "Enter Zonal Delivery Fee", Toast.LENGTH_SHORT).show();
        }else if (nationalDeliveryFee.isEmpty()){
            Toast.makeText(this, "Enter National Delivery Fee", Toast.LENGTH_SHORT).show();
        }else if (countryOrigin.isEmpty()){
            Toast.makeText(this, "Enter countryOrigin", Toast.LENGTH_SHORT).show();
        }else if (manufacturer.isEmpty()){
            Toast.makeText(this, "Enter manufacturer Details", Toast.LENGTH_SHORT).show();
        }else if (packer.isEmpty()){
            Toast.makeText(this, "Enter packer Details", Toast.LENGTH_SHORT).show();
        }else if (deliveryFeeStatus) {
            deliveryFee = binding.localDeliveryEt.getText().toString().trim();

            if (TextUtils.isEmpty(deliveryFee)) {
                Toast.makeText(this, "Delivery Price is required....!", Toast.LENGTH_SHORT).show();
            }else {
                updateDataToDb();
            }

        } else {
            deliveryFee = "FREE Delivery";
            updateDataToDb();
        }
    }
    private void updateDataToDb() {
        binding.progressBar.setVisibility(View.VISIBLE);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("productPrice", "" + productPrice);
        hashMap.put("deliveryFee", "" + deliveryFee);
        hashMap.put("productImage", "" + uploadedImageUrl);
        hashMap.put("productCuttedPrice",""+ productCuttedPrice);
        hashMap.put("category",""+ category);
        hashMap.put("active", false);
        hashMap.put("isDetailsComplete", isDetailsComplete);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document("" + productId);
        documentReference
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProductFirstActivity.this, "Product Updated", Toast.LENGTH_SHORT).show();
                            addInStock(productId);

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditProductFirstActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductFirstActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addInStock(String productId) {
        binding.progressBar.setVisibility(View.VISIBLE);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("inStock", true);
        hashMap.put("noOfQuantity",""+ noOfQuantity);
        hashMap.put("active", false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products").child(""+productId);
        databaseReference.updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(EditProductFirstActivity.this, EditProductSecondActivity.class);
                        intent.putExtra("timestamp", ""+productId);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProductFirstActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (checkCameraPermission()) {
                                pickImageCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            if (checkStoragePermission()) {
                                pickImageGallery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private void pickImageCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        cameraActivityResultLauncher.launch(intent);

    }

    private void pickImageGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        binding.productIv.setImageURI(image_uri);
                        uploadImageToStorage(image_uri);
                    } else {
                        Toast.makeText(EditProductFirstActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri = data.getData();
                        binding.productIv.setImageURI(image_uri);
                        uploadImageToStorage(image_uri);
                    } else {
                        Toast.makeText(EditProductFirstActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void uploadImageToStorage(Uri image_uri) {
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String filePathAndName = "ProductsImages/" + productId;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        uploadedImageUrl = "" + uriTask.getResult();
                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProductFirstActivity.this, "Failed to upload image due to\"+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage((int) p + "% uploading");
                    }
                });
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        Toast.makeText(this, "Storage and Camera permission granted", Toast.LENGTH_SHORT).show();
                        pickImageCamera();
                    } else {
                        Toast.makeText(this, "Camera permission are necessary...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        Toast.makeText(this, "Storage Permission granted", Toast.LENGTH_SHORT).show();
                        pickImageGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is necessary...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}