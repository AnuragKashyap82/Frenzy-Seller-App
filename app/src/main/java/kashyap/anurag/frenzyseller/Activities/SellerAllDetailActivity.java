package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.databinding.ActivitySellerAllDetailBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SellerAllDetailActivity extends AppCompatActivity {
    private ActivitySellerAllDetailBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerAllDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        String shopName = binding.shopNameEt.getText().toString().trim();
        if (shopName.isEmpty()){
            Toast.makeText(this, "Enter shop Name", Toast.LENGTH_SHORT).show();
        }else if (!binding.checkbox.isChecked()){
            Toast.makeText(this, "Check back is not checked!!!", Toast.LENGTH_SHORT).show();
        }else {
            addDataToDatabase(shopName);
        }
    }

    private void addDataToDatabase(String shopName) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("shopName", ""+shopName);
        hashMap.put("isVerified", false);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(firebaseAuth.getUid());
        documentReference
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SellerAllDetailActivity.this, "Shop Name Added !!!!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SellerAllDetailActivity.this, BankDetailsActivity.class));
                        }else {
                            Toast.makeText(SellerAllDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SellerAllDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}