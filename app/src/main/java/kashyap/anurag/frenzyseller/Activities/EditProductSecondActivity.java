package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityEditProductSecondBinding;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class EditProductSecondActivity extends AppCompatActivity {
    private ActivityEditProductSecondBinding binding;
    private FirebaseAuth firebaseAuth;
    private String productTitle, productDescription, allDetails, timestamp, deliveryWithin;
    private boolean isTabLayout, isCod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductSecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        timestamp = getIntent().getStringExtra("timestamp");

        loadProductDetails(timestamp);

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
        binding.codEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog codDialog = new Dialog(EditProductSecondActivity.this);
                codDialog.setContentView(R.layout.cod_dialog);
                codDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView yesBtn = codDialog.findViewById(R.id.yesBtn);
                TextView noBtn = codDialog.findViewById(R.id.noBtn);
                codDialog.setCancelable(true);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        codDialog.dismiss();
                        isCod = true;
                        binding.codEt.setText("COD available");
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        codDialog.dismiss();
                        isCod = false;
                        binding.codEt.setText("COD not available");
                    }
                });
                codDialog.show();
            }
        });
        binding.tabSelectorEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog tabSelectorDialog = new Dialog(EditProductSecondActivity.this);
                tabSelectorDialog.setContentView(R.layout.tab_selector_dialog);
                tabSelectorDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView tabLayoutBtn = tabSelectorDialog.findViewById(R.id.tabLayoutBtn);
                TextView allDetailsBtn = tabSelectorDialog.findViewById(R.id.allDetailsBtn);
                tabSelectorDialog.setCancelable(true);


                tabLayoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tabSelectorDialog.dismiss();
                        isTabLayout = true;
                        binding.tabSelectorEt.setText("Tab Layout");
                    }
                });
                allDetailsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tabSelectorDialog.dismiss();
                        isTabLayout = false;
                        binding.tabSelectorEt.setText("All Details Layout");
                    }
                });
                tabSelectorDialog.show();
            }
        });

    }

    private void loadProductDetails(String timestamp) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(timestamp);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                productTitle = snapshot.getString("productTitle");
                productDescription = snapshot.getString("productDescription");
                deliveryWithin = snapshot.getString("deliveryWithin");
                allDetails = snapshot.getString("allDetails");
                productTitle = snapshot.getString("productTitle");

                isTabLayout = (boolean) snapshot.get("isTabSelected");
                isCod = (boolean) snapshot.get("isCod");

                binding.productTitleEt.setText(productTitle);
                binding.productDescEt.setText(productDescription);
                binding.deliveryWithinEt.setText(deliveryWithin);

                if (isTabLayout){
                    binding.tabSelectorEt.setText("Tab Layout");
                }else {
                    binding.allDetailsEt.setText(allDetails);
                    binding.tabSelectorEt.setText("All Details Layout");
                }

                if (isCod){
                    binding.codEt.setText("COD available");
                }else {
                    binding.codEt.setText("COD not available");
                }

            }
        });
    }
    private void validateData() {
        productTitle = binding.productTitleEt.getText().toString().trim();
        productDescription = binding.productDescEt.getText().toString().trim();
        String tabSelected = binding.tabSelectorEt.getText().toString().trim();
        String cod = binding.codEt.getText().toString().trim();
        allDetails = binding.allDetailsEt.getText().toString().trim();
        deliveryWithin = binding.deliveryWithinEt.getText().toString().trim();

        if (productTitle.isEmpty()) {
            Toast.makeText(this, "Enter Product Name!!!", Toast.LENGTH_SHORT).show();
        } else if (productDescription.isEmpty()) {
            Toast.makeText(this, "Enter Product Description!!!", Toast.LENGTH_SHORT).show();
        }else if (deliveryWithin.isEmpty()) {
            Toast.makeText(this, "Enter Delivery within!!!", Toast.LENGTH_SHORT).show();
        }else if (tabSelected.isEmpty()) {
            Toast.makeText(this, "Select Layout!!!", Toast.LENGTH_SHORT).show();
        } else if (cod.isEmpty()) {
            Toast.makeText(this, "Is COD available!!!", Toast.LENGTH_SHORT).show();
        } else if (allDetails.isEmpty()) {
            Toast.makeText(this, "Select No Oof Available Products Quantity!!!", Toast.LENGTH_SHORT).show();
        }else {
            uploadDataToDb();
        }
    }
    private void uploadDataToDb() {
        binding.progressBar.setVisibility(View.VISIBLE);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("productTitle", "" + productTitle);
        hashMap.put("productDescription", "" + productDescription);
        hashMap.put("isTabSelected", isTabLayout);
        hashMap.put("deliveryWithin", ""+deliveryWithin);
        hashMap.put("isCod", isCod);
        hashMap.put("isDetailsComplete", false);
        hashMap.put("allDetails", ""+allDetails);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document("" + timestamp);
        documentReference
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProductSecondActivity.this, "Product Updated", Toast.LENGTH_SHORT).show();
                            addInStock(timestamp);

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditProductSecondActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProductSecondActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void addInStock(String timestamp) {
        binding.progressBar.setVisibility(View.VISIBLE);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("productTitle",""+ productTitle);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products").child(""+timestamp);
        databaseReference
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(EditProductSecondActivity.this, EditProductThirdActivity.class);
                        intent.putExtra("timestamp", ""+timestamp);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProductSecondActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}