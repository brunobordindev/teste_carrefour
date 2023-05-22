package com.example.testecarrefour.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.example.testecarrefour.R;
import com.example.testecarrefour.databinding.ActivityRevenueBinding;
import com.example.testecarrefour.model.Base64Custom;
import com.example.testecarrefour.model.DateUtil;
import com.example.testecarrefour.model.Movement;
import com.example.testecarrefour.model.Settings;
import com.example.testecarrefour.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RevenueActivity extends AppCompatActivity {

    private ActivityRevenueBinding binding;
    private Movement movement;
    private DatabaseReference firebaseRef = Settings.getFirebaseReference();
    private FirebaseAuth auth = Settings.getFirebaseAuth();
    private Double totalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_revenue);

        getSupportActionBar().setTitle(getString(R.string.revenue));

        binding.editDateRevenue.setText(DateUtil.dateCurrent());

        recoverTotalRevenue();

        binding.fabSalveRevenue.setOnClickListener(view -> {

            if (validateRevenueField()){

                movement = new Movement();
                Double valueRecover = Double.parseDouble(binding.editValueRevenue.getText().toString());
                String date = binding.editDateRevenue.getText().toString();
                movement.setValue(valueRecover);
                movement.setDate(date);
                movement.setCategory(binding.editCategoryRevenue.getText().toString());
                movement.setDescription(binding.editDescriptionRevenue.getText().toString());
                movement.setType("R");

                Double revenueCurrent = totalRevenue + valueRecover;
                currentRevenue(revenueCurrent);

                movement.salve(date);

                finish();
            }

        });

    }

    public Boolean validateRevenueField(){
        if (!binding.editValueRevenue.getText().toString().isEmpty()){
            if (!binding.editDateRevenue.getText().toString().isEmpty()){
                if (!binding.editCategoryRevenue.getText().toString().isEmpty()){
                    if (!binding.editDescriptionRevenue.getText().toString().isEmpty()){

                        return true;

                    }else{
                        phrasesTost(getString(R.string.text_not_completed_description));
                        return false;
                    }
                }else{
                    phrasesTost(getString(R.string.text_not_completed_category));
                    return false;
                }
            }else{
                phrasesTost(getString(R.string.text_not_completed_date));
                return false;
            }
        }else{
            phrasesTost(getString(R.string.text_not_completed_value));
            return false;
        }
    }

    private void recoverTotalRevenue() {

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codeBase64(emailUser);

        DatabaseReference userRef = firebaseRef.child("users").child(idUser);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                totalRevenue = user.getRevenueTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void currentRevenue(Double revenueCurrent) {

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codeBase64(emailUser);

        DatabaseReference userRef = firebaseRef.child("users").child(idUser);
        userRef.child("revenueTotal").setValue(revenueCurrent);
    }

    private void phrasesTost(String prhases) {
        Toast.makeText(getApplicationContext(), prhases, Toast.LENGTH_SHORT).show();
    }
}