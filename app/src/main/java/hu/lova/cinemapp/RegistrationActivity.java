package hu.lova.cinemapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG=RegistrationActivity.class.getName();
    private static final String PREFERENCE_KEY=RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    EditText phoneEditText;
    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth=FirebaseAuth.getInstance();

        int secretKey=getIntent().getIntExtra("SECRET_KEY", 0);
        if(secretKey!=SECRET_KEY) finish();

        usernameEditText=findViewById(R.id.usernameEditText);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        passwordAgainEditText=findViewById(R.id.passwordAgainEditText);
        phoneEditText=findViewById(R.id.phoneEditText);

        preferences=getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        String email=preferences.getString("email", "");
        emailEditText.setText(email);

        Log.i(LOG_TAG,"onCreate");
    }

    public void registration(View view){
        String username=usernameEditText.getText().toString();
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String passwordAgain=passwordAgainEditText.getText().toString();
        if(!password.equals(passwordAgain)){
            Log.e(LOG_TAG, "Nem egyeznek a jelszavak");
            return;
        }
        String phone=phoneEditText.getText().toString();
        Log.i(LOG_TAG, "Sikeres regisztráció!");
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(LOG_TAG, "Új felhasználó léterhozva!");
                startShopping();
            }else{
                Log.d(LOG_TAG, "Felhasználó létrehozása sikertelen!");
                Toast.makeText(RegistrationActivity.this, "Felhasználó létrehozása sikertelen!"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedItem=adapterView.getItemAtPosition(position).toString();
        Log.i(LOG_TAG,selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void cancel(View view){
        finish();
    }

    private void startShopping(){
        Intent intent=new Intent(this, TicketActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }
}
