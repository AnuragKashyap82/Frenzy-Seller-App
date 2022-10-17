package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        }, 500);
    }

    private void checkUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            checkAccountVerified(firebaseAuth.getUid());
        }else {
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            finishAffinity();
        }
    }

    private void checkAccountVerified(String uid) {
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                boolean isVerified = (boolean) snapshot.get("isVerified");
                if (isVerified){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finishAffinity();
                }else {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(SplashActivity.this, SellerVerificationActivity.class));
                    finishAffinity();
                }
            }
        });
    }
}