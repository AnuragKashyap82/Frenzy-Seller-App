package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.databinding.ActivityCreateSellerAccountBinding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CreateSellerAccountActivity extends AppCompatActivity {
    private ActivityCreateSellerAccountBinding binding;
    private String phoneNo, sellerName, email;
    FirebaseAuth firebaseAuth;

    String verificationId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSellerAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        binding.emailEt.setEnabled(false);
        binding.nameEt.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData1();
            }
        });
    }

    private void validateData1() {
        email = binding.emailEt.getText().toString().trim();
        sellerName = binding.nameEt.getText().toString().trim();
        phoneNo = binding.phoneNoEt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid Email!!!", Toast.LENGTH_SHORT).show();
        }else if (sellerName.isEmpty()){
            Toast.makeText(this, "Enter your name!!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(CreateSellerAccountActivity.this, CreatePasswordActivity.class);
            intent.putExtra("email", ""+email);
            intent.putExtra("phoneNo", ""+phoneNo);
            intent.putExtra("sellerName", ""+sellerName);
            startActivity(intent);
        }
    }

    private void validateData() {
        phoneNo = binding.phoneNoEt.getText().toString().trim();
        if (phoneNo.length() != 10) {
            Toast.makeText(this, "Enter 10 digit phone No.", Toast.LENGTH_SHORT).show();
        }else {
            sendOtp(phoneNo);
        }

    }

    private void sendOtp(String phoneNo) {
        progressDialog.setMessage("Sending OTP...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+91" + phoneNo)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(CreateSellerAccountActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateSellerAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyId, forceResendingToken);
                        progressDialog.dismiss();
                        verificationId = verifyId;
                        binding.sendOtpBtn.setVisibility(View.GONE);
                        binding.verifyOtpBtn.setVisibility(View.VISIBLE);

                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);

        binding.verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                String otp = binding.otpEt.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.emailEt.setEnabled(true);
                            binding.nameEt.setEnabled(true);
                            binding.signUpBtn.setVisibility(View.VISIBLE);
                            binding.phoneNoEt.setEnabled(false);
                            Toast.makeText(CreateSellerAccountActivity.this, "Otp Verified.", Toast.LENGTH_SHORT).show();

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateSellerAccountActivity.this, "Otp Doesn't matches", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}