package com.example.testecarrefour.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import com.example.testecarrefour.R;
import com.example.testecarrefour.databinding.ActivitySignUpBinding;
import com.example.testecarrefour.model.Base64Custom;
import com.example.testecarrefour.model.Settings;
import com.example.testecarrefour.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private User user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up );

        getSupportActionBar().setTitle(getString(R.string.register));

        int blockVisibility = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
        binding.btnVisibilityPassword.setOnClickListener(view -> {
            if ( binding.editRegisterPasswordSignUp.getInputType() == blockVisibility){
                binding.editRegisterPasswordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.btnVisibilityPassword.setImageResource(R.drawable.baseline_visibility_24);
            }else{
                binding.editRegisterPasswordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                binding.btnVisibilityPassword.setImageResource(R.drawable.baseline_visibility_off_24);
            }
        });

        binding.btnRegisterSignUp.setOnClickListener(view -> {

            if (!binding.editRegisterNameSignUp.getText().toString().isEmpty()){
                if (!binding.editRegisterEmailSignUp.getText().toString().isEmpty()){
                    if (!binding.editRegisterPasswordSignUp.getText().toString().isEmpty()){

                        user = new User(binding.editRegisterNameSignUp.getText().toString(), binding.editRegisterEmailSignUp.getText().toString(), binding.editRegisterPasswordSignUp.getText().toString());
                        registerUser();

                    }else{
                        phrasesTost(getString(R.string.text_fill_password));
                    }
                }else{
                    phrasesTost(getString(R.string.text_fill_email));
                }
            }else{
                phrasesTost(getString(R.string.text_fill_name));
            }
        });

    }

    private void registerUser() {

        auth = Settings.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String idUser = Base64Custom.codeBase64(user.getEmail());
                    user.setIdUser(idUser);
                    user.salve();
                    finish();

                    phrasesTost(getString(R.string.text_register_sucess));

                }else{

                    String exceptionPhrases = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        exceptionPhrases = getString(R.string.text_strong_password);
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exceptionPhrases = getString(R.string.text_valid_email);
                    }catch (FirebaseAuthUserCollisionException e){
                        exceptionPhrases = getString(R.string.text_register_email);
                    }catch (Exception e){
                        exceptionPhrases = getString(R.string.text_wrong_register_user) + e.getMessage();
                        e.printStackTrace();
                    }
                    phrasesTost(exceptionPhrases);
                }
            }
        });
    }

    private void phrasesTost(String prhases){
        Toast.makeText(getApplicationContext(), prhases , Toast.LENGTH_SHORT).show();
    }
}