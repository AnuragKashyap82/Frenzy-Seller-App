package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivitySellerVerificationBinding;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SellerVerificationActivity extends AppCompatActivity {
    private ActivitySellerVerificationBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        loadMyDetails();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

    }

    private void loadMyDetails() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                String name  = snapshot.getString("sellerName");
                String shopName  = snapshot.getString("shopName");

                binding.nameTv.setText(name);
                binding.shopNameTv.setText("Shop Name: "+shopName);
            }
        });
    }
    private void showLogoutDialog() {
        Dialog logoutDialog = new Dialog(SellerVerificationActivity.this);
        logoutDialog.setContentView(R.layout.logout_dialog);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView cancelBtn = logoutDialog.findViewById(R.id.cancelBtn);
        TextView logoutBtn = logoutDialog.findViewById(R.id.logoutBtn);
        logoutDialog.setCancelable(true);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
                firebaseAuth.signOut();
                startActivity(new Intent(SellerVerificationActivity.this, SignInActivity.class));
                finishAffinity();
            }
        });
        logoutDialog.show();
    }
}