package kashyap.anurag.frenzyseller.Activities;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void validateData() {
        String email = binding.emailEt.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(ForgotPasswordActivity.this, "Enter Valid Email!!", Toast.LENGTH_SHORT).show();
        }else {
            sendVerificationEmail(email);
        }
    }

    private void sendVerificationEmail(String email) {
        binding.resetPassBtn.setEnabled(false);

        TransitionManager.beginDelayedTransition(binding.ll);
        binding.ll.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            binding.errorTv.setVisibility(View.VISIBLE);
                            binding.errorTv.setText("Recovery email sent successfully! Check your Inbox");
                            binding.errorTv.setTextColor(getResources().getColor(R.color.successGreen));
                            TransitionManager.beginDelayedTransition(binding.ll);

                        }else {

                            binding.errorTv.setText(task.getException().getMessage());
                            binding.errorTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            TransitionManager.beginDelayedTransition(binding.ll);
                            binding.resetPassBtn.setEnabled(true);

                        }
                        binding.progressBar.setVisibility(View.GONE);

                    }
                });
    }
}