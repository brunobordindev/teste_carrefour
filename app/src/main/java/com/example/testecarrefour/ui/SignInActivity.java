package com.example.testecarrefour.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import com.example.testecarrefour.R;
import com.example.testecarrefour.databinding.ActivitySignInBinding;
import com.example.testecarrefour.model.Settings;
import com.example.testecarrefour.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private User user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        getSupportActionBar().setTitle(getString(R.string.login));

        int blockVisibility = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
        binding.btnVisibilityPasswordSignIn.setOnClickListener(view -> {
            if ( binding.editRegisterPasswordSignIn.getInputType() == blockVisibility){
                binding.editRegisterPasswordSignIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.btnVisibilityPasswordSignIn.setImageResource(R.drawable.baseline_visibility_24);
            }else{
                binding.editRegisterPasswordSignIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                binding.btnVisibilityPasswordSignIn.setImageResource(R.drawable.baseline_visibility_off_24);
            }
        });

        binding.btnEnterSignIn.setOnClickListener(view -> {

            if (!binding.editRegisterEmailSignIn.getText().toString().isEmpty()){
                if (!binding.editRegisterPasswordSignIn.getText().toString().isEmpty()){

                    user = new User(binding.editRegisterEmailSignIn.getText().toString(), binding.editRegisterPasswordSignIn.getText().toString());
                    validLogin();

                }else{
                    phrasesTost(getString(R.string.text_fill_password));
                }
            }else{
                phrasesTost(getString(R.string.text_fill_email));
            }
        });
    }

    private void validLogin() {

        auth = Settings.getFirebaseAuth();
        auth.signInWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    openScreenMain();

                }else{

                    String exceptionPhrases = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        exceptionPhrases = getString(R.string.text_not_register_user);
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exceptionPhrases = getString(R.string.text_not_equal_email_and_password);
                    }catch (Exception e){
                        exceptionPhrases = getString(R.string.text_wrong_login_user) + e.getMessage();
                    }
                    phrasesTost(exceptionPhrases);
                }
            }
        });
    }

    private void openScreenMain() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void phrasesTost(String prhases){
        Toast.makeText(getApplicationContext(), prhases , Toast.LENGTH_SHORT).show();
    }
}