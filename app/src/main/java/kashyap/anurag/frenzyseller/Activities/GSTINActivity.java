package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityGstinactivityBinding;

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

public class GSTINActivity extends AppCompatActivity {
    private ActivityGstinactivityBinding binding;
    private FirebaseAuth  firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGstinactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.gstCardView.setVisibility(View.GONE);
                binding.pinCodeCard.setVisibility(View.VISIBLE);
            }
        });
        binding.continuePinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        String pinCode = binding.pinCodeEt.getText().toString().trim();
        String address = binding.addressEt.getText().toString().trim();
        String city = binding.cityEt.getText().toString().trim();
        String state = binding.stateEt.getText().toString().trim();

        if (pinCode.isEmpty()){
            Toast.makeText(this, "Enter pincode!!", Toast.LENGTH_SHORT).show();
        }else if (address.isEmpty()){
            Toast.makeText(this, "Enter address!!", Toast.LENGTH_SHORT).show();
        }else if (city.isEmpty()){
            Toast.makeText(this, "Enter city!!", Toast.LENGTH_SHORT).show();
        }else if (state.isEmpty()){
            Toast.makeText(this, "Enter state!!", Toast.LENGTH_SHORT).show();
        }else {
            long timestamp = System.currentTimeMillis();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("pinCode", ""+pinCode);
            hashMap.put("address", ""+address);
            hashMap.put("city", ""+city);
            hashMap.put("state", ""+state);
            hashMap.put("addressId", ""+timestamp);

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(firebaseAuth.getUid());
            documentReference
                    .update(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(GSTINActivity.this, "Completed!!!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(GSTINActivity.this, SellerAllDetailActivity.class));
                            }else {
                                Toast.makeText(GSTINActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GSTINActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
}