package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityCreatePasswordBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreatePasswordActivity extends AppCompatActivity {
    private ActivityCreatePasswordBinding binding;
    private String email, sellerName, phoneNo;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getStringExtra("email");
        phoneNo = getIntent().getStringExtra("phoneNo");
        sellerName = getIntent().getStringExtra("sellerName");
        firebaseAuth = FirebaseAuth.getInstance();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.createPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        String password = binding.passwordEt.getText().toString().trim();
        String cPassword = binding.cPasswordEt.getText().toString().trim();

        if (password.length() < 8){
            Toast.makeText(this, "Password must be of 8 characters!!", Toast.LENGTH_SHORT).show();
        }else if (!cPassword.equals(password)){
            Toast.makeText(this, "Confirm password are not same!!!", Toast.LENGTH_SHORT).show();
        }else {
            createAccountWithEmailAndPassword(email, password);
        }
    }

    private void createAccountWithEmailAndPassword(String email, String password) {
        binding.progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(CreatePasswordActivity.this, "Password Created Successfully!!!", Toast.LENGTH_SHORT).show();
                        addDataToDataBase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreatePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDataToDataBase() {
        binding.progressBar.setVisibility(View.VISIBLE);
        long timestamp = System.currentTimeMillis();

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("uid", ""+firebaseAuth.getUid());
        hashMap.put("email", ""+email);
        hashMap.put("phoneNo", ""+phoneNo);
        hashMap.put("sellerName", ""+sellerName);
        hashMap.put("shopName", "");
        hashMap.put("profileImage", "");
        hashMap.put("userType", "seller");
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("shopId", ""+firebaseAuth.getUid());

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(firebaseAuth.getUid());
        documentReference.set(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreatePasswordActivity.this, "data Added to database!!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreatePasswordActivity.this, GSTINActivity.class));

                        }else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreatePasswordActivity.this, "Failed adding data to database!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreatePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}