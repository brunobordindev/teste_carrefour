package com.example.testecarrefour.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.testecarrefour.R;
import com.example.testecarrefour.model.Settings;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.introduction_one)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.introduction_two)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.introduction_three)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.introduction_register)
                .canGoBackward(false)
                .canGoForward(false)
                .build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        validUser();
    }

    private void validUser() {
        auth = Settings.getFirebaseAuth();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }

    public void haveRegister(View view) {
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }
}