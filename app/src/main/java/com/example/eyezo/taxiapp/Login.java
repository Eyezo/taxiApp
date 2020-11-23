package com.example.eyezo.taxiapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etLogMail;
    EditText etLogPassword;

    Button btnLogin;
    Button btnRegister;
    Button btnTest;
    TextView tvReset;

   // public static final String MY_USER_INFO = "com.example.eyezo.taxiapp.Names";

    //SharedPreferences.Editor editor = getSharedPreferences(MY_USER_INFO, MODE_PRIVATE).edit();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etLogMail = findViewById(R.id.etLogMail);
        etLogPassword = findViewById(R.id.etLogPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvReset = findViewById(R.id.tvReset);
        btnTest = findViewById(R.id.btnTestList);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etLogMail.getText().toString().isEmpty() ||
                        etLogPassword.getText().toString().isEmpty())
                {

                    Toast.makeText(Login.this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    final String email = etLogMail.getText().toString().trim();
                    final String password = etLogPassword.getText().toString().trim();

                    showProgress(true);

                    String whereClause = "email = '" + email +"' and password = '" + password +"'";

                    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                    // set where clause
                    queryBuilder.setWhereClause(whereClause);

                    queryBuilder.setGroupBy("name");

                    showProgress(true);
                    tvLoad.setText("Loading....");

                    Backendless.Persistence.of(Person.class).find(queryBuilder, new AsyncCallback<List<Person>>() {
                        @Override
                        public void handleResponse(List<Person> response) {

                            String selector1 = "Driver";
                            String selector2 = "Admin";

                            Person temp = new Person();

                            String email = temp.getEmail();
                            String password = temp.getPassword();
                            String nme = temp.getName();
                            String amnt = temp.getAmount();
                            String passengers = temp.getNumOfPass();
                            String role = temp.getRole();

                            if(selector1.equalsIgnoreCase(temp.getRole()))
                            {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("email",email);
                                intent.putExtra("password", password);
                                intent.putExtra("name" , nme);
                                intent.putExtra("amount" , amnt);
                                intent.putExtra("passengers", passengers);
                                intent.putExtra("role", role);

                                startActivity(intent);
                                Login.this.finish();
                            }
                            else if(selector2.equalsIgnoreCase(temp.getRole()))
                            {

                                startActivity(new Intent(Login.this, DriverList.class));
                                Login.this.finish();

                            }
                            else {
                                Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                                showProgress(false);

                            }



                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "whats happening" , Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });




                 /**   Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {


                            TaxiBack.user = response;
                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();


                         BackendlessUser user = Backendless.UserService.CurrentUser();
                            if( user != null )
                            {

                                String name = (String) user.getProperty("name");
                                String amount = (String) user.getProperty("amount");
                                String numOfPass = (String) user.getProperty("numofpass");
                                String role = (String) user.getProperty("role");

                                String selector = "Driver";
                                TaxiBack.user = response;
                                startActivity(new Intent(Login.this, DriverList.class));
                                Login.this.finish();

                                if(selector.equalsIgnoreCase(role))
                                {
                                    TaxiBack.user = response;
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    Login.this.finish();
                                }
                                else
                                {
                                    TaxiBack.user = response;
                                    startActivity(new Intent(Login.this, DriverList.class));
                                    Login.this.finish();

                                }


                            }
                            else
                            {
                                Toast.makeText( Login.this,
                                        "User hasn't been logged",
                                        Toast.LENGTH_SHORT ).show();
                            }

                        }


                       @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    }, true);
                    **/

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));

            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, DriverList.class));
                Login.this.finish();
            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLogMail.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Please enter your email address in the email field", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email = etLogMail.getText().toString().trim();

                    showProgress(true);

                    Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {

                            Toast.makeText(Login.this, "Reset instructions sent to email address", Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });
                }

            }
        });
    }

    /**
     * Show the progress UI and the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
