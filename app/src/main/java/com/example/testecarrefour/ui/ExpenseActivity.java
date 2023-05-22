package com.example.testecarrefour.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.example.testecarrefour.R;
import com.example.testecarrefour.databinding.ActivityExpenseBinding;
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

public class ExpenseActivity extends AppCompatActivity {

    private ActivityExpenseBinding binding;
    private Movement movement;
    private DatabaseReference firebaseRef = Settings.getFirebaseReference();
    private FirebaseAuth auth = Settings.getFirebaseAuth();
    private Double totalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense);

        getSupportActionBar().setTitle("Despesa");

        binding.editDateExpense.setText(DateUtil.dateCurrent());

        recoverTotalExpense();

        binding.fabSalveExpense.setOnClickListener(view -> {

            if (validateExpenseField()) {
                movement = new Movement();
                String date = binding.editDateExpense.getText().toString();
                Double recoverValue = Double.parseDouble(binding.editValueExpense.getText().toString());
                movement.setValue(recoverValue);
                movement.setDate(date);
                movement.setCategory(binding.editCategoryExpense.getText().toString());
                movement.setDescription(binding.editDescriptionExpense.getText().toString());
                movement.setType("E");

                Double updatedExpense = totalExpense + recoverValue;
                updateExpense(updatedExpense);

                movement.salve(date);
                finish();

            }

        });

    }

    private boolean validateExpenseField() {

        if (!binding.editValueExpense.getText().toString().isEmpty()) {
            if (!binding.editDateExpense.getText().toString().isEmpty()) {
                if (!binding.editCategoryExpense.getText().toString().isEmpty()) {
                    if (!binding.editDescriptionExpense.getText().toString().isEmpty()) {

                        return true;

                    } else {
                        phrasesTost(getString(R.string.text_not_completed_description));
                        return false;
                    }
                } else {
                    phrasesTost(getString(R.string.text_not_completed_category));
                    return false;
                }
            } else {
                phrasesTost(getString(R.string.text_not_completed_date));
                return false;
            }
        } else {
            phrasesTost(getString(R.string.text_not_completed_value));
            return false;
        }
    }

    private void recoverTotalExpense() {
        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codeBase64(emailUser);

        DatabaseReference userRef = firebaseRef.child("users").child(idUser);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                totalExpense = user.getExpenseTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateExpense(Double expense) {
        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codeBase64(emailUser);

        DatabaseReference userRef = firebaseRef.child("users").child(idUser);
        userRef.child("expenseTotal").setValue(expense);
    }
    private void phrasesTost(String prhases) {
        Toast.makeText(getApplicationContext(), prhases, Toast.LENGTH_SHORT).show();
    }
}