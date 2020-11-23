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
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private View mProgressView;
    private View driverView;
    private TextView tvLoad;

    TextView tvamount;
    TextView tvnumOfPassengers;

    Button btnminus;
    Button btnadd;
    Button btnsave;

    double amount;
    int numOfPass;

    BackendlessUser user = Backendless.UserService.CurrentUser();
    Intent in = getIntent();
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvamount = findViewById(R.id.tvAmount);
        tvnumOfPassengers = findViewById(R.id.tvNum);
        btnminus = findViewById(R.id.btnMinus);
        btnadd = findViewById(R.id.btnAdd);
        btnsave = findViewById(R.id.btnSave);

        driverView = findViewById(R.id.driver_screen);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);


        String name = in.getExtras().getString("name");
        email = in.getExtras().getString("email");
         password = in.getExtras().getString("password");
        String money = in.getExtras().getString("amount");
        String passengers = in.getExtras().getString("passengers");
        String role = in.getExtras().getString("role");


        Toast.makeText(MainActivity.this, "welcome: " +name, Toast.LENGTH_SHORT).show();

        amount = Double.parseDouble(money);
        numOfPass = Integer.parseInt(passengers);

        tvamount.setText(String.valueOf(amount));
        tvnumOfPassengers.setText(String.valueOf(numOfPass));

        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(numOfPass != 0) {
                    numOfPass = numOfPass - 1;
                    amount = numOfPass * 8;
                    tvnumOfPassengers.setText(String.valueOf(numOfPass));
                    tvamount.setText(String.valueOf(amount));

                }
                else
                {
                    Toast.makeText(MainActivity.this, "number of passengers cannot be lesser than 0", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                numOfPass = numOfPass + 1;
                amount = numOfPass * 8;
                tvnumOfPassengers.setText(String.valueOf(numOfPass));
                tvamount.setText(String.valueOf(amount));

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        Person temp = new Person();
                        temp.setAmount( String.valueOf(amount));
                        temp.setNumOfPass(String.valueOf(numOfPass));
                        Backendless.Persistence.save(temp, new AsyncCallback<Person>() {
                            @Override
                            public void handleResponse(Person response) {


                                Toast.makeText( MainActivity.this,
                                        "Driver info has been updated!",
                                        Toast.LENGTH_SHORT ).show();
                                showProgress(false);


                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                                Toast.makeText( MainActivity.this,
                                        "User not found",
                                        Toast.LENGTH_SHORT ).show();
                                showProgress(false);

                            }
                        });





                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(MainActivity.this, "Error: invalid user "  , Toast.LENGTH_SHORT).show();
                        showProgress(false);

                    }
                });

            }
        });




        /**   if( user != null )
           {

               Toast.makeText(MainActivity.this, "welcome: " +(String) user.getProperty("name"), Toast.LENGTH_SHORT).show();

               String userAmount = (String) user.getProperty( "amount" );
               String userNumOfPass = (String) user.getProperty("numofpass");


               amount = Double.parseDouble(userAmount);
               numOfPass = Integer.parseInt(userNumOfPass);

               tvamount.setText(String.valueOf(amount));
               tvnumOfPassengers.setText(String.valueOf(numOfPass));

               btnminus.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {


                       if(numOfPass != 0) {
                           numOfPass = numOfPass - 1;
                           amount = numOfPass * 8;
                           tvnumOfPassengers.setText(String.valueOf(numOfPass));
                           tvamount.setText(String.valueOf(amount));

                       }
                       else
                       {
                           Toast.makeText(MainActivity.this, "number of passengers cannot be lesser than 0", Toast.LENGTH_SHORT).show();

                       }
                   }
               });

               btnadd.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {


                       numOfPass = numOfPass + 1;
                       amount = numOfPass * 8;
                       tvnumOfPassengers.setText(String.valueOf(numOfPass));
                       tvamount.setText(String.valueOf(amount));

                   }
               });

               btnsave.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       showProgress(true);
                       // update user properties
                       user.setProperty( "amount", String.valueOf(amount));
                       user.setProperty("numofpass" , String.valueOf(numOfPass));
                       Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                           @Override
                           public void handleResponse(BackendlessUser response) {
                               Toast.makeText( MainActivity.this,
                                       "Driver info has been updated!",
                                       Toast.LENGTH_SHORT ).show();
                               showProgress(false);

                           }

                           @Override
                           public void handleFault(BackendlessFault fault) {
                               Toast.makeText( MainActivity.this,
                                       "User not found",
                                       Toast.LENGTH_SHORT ).show();
                               showProgress(false);

                           }
                       });

                   }
               });

           }
           else
           {
               Toast.makeText( MainActivity.this,
                       "User hasn't been logged",
                       Toast.LENGTH_SHORT ).show();
           }

           **/




    }
    /**
     * Show the progress UI and the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            driverView.setVisibility(show ? View.GONE : View.VISIBLE);
            driverView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    driverView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            driverView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    }

