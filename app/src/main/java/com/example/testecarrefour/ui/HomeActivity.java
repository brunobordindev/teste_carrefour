package com.example.testecarrefour.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testecarrefour.R;
import com.example.testecarrefour.databinding.ActivityHomeBinding;
import com.example.testecarrefour.model.Base64Custom;
import com.example.testecarrefour.model.Movement;
import com.example.testecarrefour.model.Settings;
import com.example.testecarrefour.model.User;
import com.example.testecarrefour.viewModel.AdapterMovement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private List<Integer> imageList = new ArrayList<>();
    private CarouselView carouselView;
    private TextView btnScheduleMonth, textHello, textBalanceValue;
    private LinearLayout linearMonth;
    private MaterialCalendarView calendarView;
    private String monthYearSelected;
    private DatabaseReference movementRef;
    private ValueEventListener valueEventListenerMovement;
    private ValueEventListener valueEventListenerUser;
    private FirebaseAuth auth = Settings.getFirebaseAuth();
    private DatabaseReference firebaseRef = Settings.getFirebaseReference();
    private List<Movement> movements = new ArrayList<>();
    private AdapterMovement adapterMovement;
    private RecyclerView recyclerView;
    private Movement movement;
    private DatabaseReference userRef = Settings.getFirebaseReference();
    private Double revenueTotal = 0.00;
    private Double expenseTotal = 0.00;
    private Double totalValueUser = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initialize();
        visibilityScheduleMonth();
        
        binding.fabExpense.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
        });

        binding.fabRevenue.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), RevenueActivity.class));
        });
        
        SettingsCalendarView();
        swipe();

        adapterMovement = new AdapterMovement(movements, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterMovement);

        imageList.add(R.drawable.prop_1);
        imageList.add(R.drawable.prop_2);
        imageList.add(R.drawable.prob_3);

        carouselView.setPageCount(imageList.size());
        carouselView.setImageListener(imageListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemExit:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                phrasesTost(getString(R.string.exit));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int draFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(draFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteMovement(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    private void deleteMovement(RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.delete_movement_account));
        alertDialog.setMessage(R.string.are_you_sure_delete_movement_account);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                movement = movements.get(position);

                String emailUser = auth.getCurrentUser().getEmail();
                String idUser = Base64Custom.codeBase64(emailUser);

                movementRef = firebaseRef.child("movement").child(idUser).child(monthYearSelected);
                movementRef.child(movement.getKey()).removeValue();
                adapterMovement.notifyItemRemoved(position);

                updateBalanceAfterDeletingTransaction();
            }
        });

        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                phrasesTost(getString(R.string.canceling));
                adapterMovement.notifyDataSetChanged();
            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void updateBalanceAfterDeletingTransaction() {

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codeBase64(emailUser);
        userRef = firebaseRef.child("users").child(idUser);

        if (movement.getType().equals("R")){
            revenueTotal = revenueTotal - movement.getValue();
            userRef.child("revenueTotal").setValue(revenueTotal);
        }


        if (movement.getType().equals("E")){
            expenseTotal = expenseTotal - movement.getValue();
            userRef.child("expenseTotal").setValue(expenseTotal);
        }
    }

    private void recoverMovement() {

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codeBase64(emailUser);

        movementRef = firebaseRef.child("movement").child(idUser).child(monthYearSelected);
        valueEventListenerMovement = movementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                movements.clear();
                for (DataSnapshot datas: snapshot.getChildren()){
                    Movement movement = datas.getValue(Movement.class);
                    movement.setKey(datas.getKey());
                    movements.add(movement);
                }

                adapterMovement.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recoverResume(){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codeBase64(emailUser);
        userRef = firebaseRef.child("users").child(idUser);

        valueEventListenerUser = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                textHello.setText("Olá, " + user.getName());

                revenueTotal = user.getRevenueTotal();
                expenseTotal = user.getExpenseTotal();
                totalValueUser = revenueTotal - expenseTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String resultFormat = decimalFormat.format(totalValueUser);

                if (totalValueUser < 0){
                    textBalanceValue.setText("R$ " + resultFormat);
                    textBalanceValue.setTextColor(getResources().getColor(R.color.red_carrefour));
                }else{
                    textBalanceValue.setText("R$ " + resultFormat);
                    textBalanceValue.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SettingsCalendarView() {

        CharSequence months[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(months);

        CalendarDay dateCurrent = calendarView.getCurrentDate();
        String monthSelected = String.format("%02d", (dateCurrent.getMonth() + 1));
        monthYearSelected = String.valueOf(monthSelected + "" + dateCurrent.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String monthSelected = String.format("%02", (date.getMonth() + 1));
                monthYearSelected = String.valueOf(monthSelected + "" + date.getYear());

                movementRef.removeEventListener(valueEventListenerMovement);
                recoverMovement();
            }
        });

    }

    public void initialize(){
        carouselView = findViewById(R.id.carouselView);
        btnScheduleMonth = findViewById(R.id.btn_schedule_month);
        linearMonth = findViewById(R.id.linear_month);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recycler_movimentation);
        textHello = findViewById(R.id.text_hello_user);
        textBalanceValue = findViewById(R.id.text_balance_value);


        getSupportActionBar().setElevation(0);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imageList.get(position));
        }
    };

    public void visibilityScheduleMonth(){
        btnScheduleMonth.setOnClickListener(view -> {
            if (linearMonth.getVisibility() == View.GONE){
                linearMonth.setVisibility(View.VISIBLE);
            }else{
                linearMonth.setVisibility(View.GONE);
            }
        });
    }

    private void phrasesTost(String prhases) {
        Toast.makeText(getApplicationContext(), prhases, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recoverResume();
        recoverMovement();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerUser);
        movementRef.removeEventListener(valueEventListenerMovement);
    }
}