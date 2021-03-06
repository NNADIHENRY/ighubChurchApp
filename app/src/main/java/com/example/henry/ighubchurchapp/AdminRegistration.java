package com.example.henry.ighubchurchapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class AdminRegistration extends AppCompatActivity {
    private EditText etsurname, etfirstname, etemail, etpassword, etphone;
    private Button btnsubmit, tvhomepage;
    double id;

    Button showMenu;

    private FirebaseAuth firebaseAuth;



    private String surname, firstname, email, password, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        id = Math.random();

        //linking the xml and java
        etsurname = findViewById(R.id.etSurname);
        etfirstname = findViewById(R.id.etFirstname);
        etemail = findViewById(R.id.etEmail);
        etpassword = findViewById(R.id.etPassword);
        etphone = findViewById(R.id.etPhone);
        tvhomepage = findViewById(R.id.tvHomePage);
        btnsubmit = findViewById(R.id.btnSubmit);

        tvhomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminRegistration.this,
                        HomePage.class));
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();


            }
        });
        Toast.makeText(this, "your admin id isv "+id, Toast.LENGTH_LONG).show();
        showMenu = findViewById(R.id.show_dropdown_menu);
        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), showMenu);
                dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_menu, dropDownMenu.getMenu());
                showMenu.setText("...");
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        boolean me;
                        if(menuItem.getTitle().toString().matches("Registration") ){

                            showDialog();
                            me = true;

                        }
                        else if(menuItem.getTitle().toString().matches("Login") ){
                            me = true;

                        }
                        else if(menuItem.getTitle().toString().matches("Pay Tithe/Offering")){
                            me = true;


                        }
                        else if(menuItem.getTitle().toString().matches( "View Pastors")){
                            me = true;

                        }
                        else if(menuItem.getTitle().toString().matches( "Weekly Activity")){
                            weeklyActivity();
                            me = true;

                        }
                        else {
                            me = false;
                        }
                        return me;



                    }
                });
                dropDownMenu.show();
            }
        });

    }


    private void validate() {
        //validation here
        surname = etsurname.getText().toString().trim();
        firstname = etfirstname.getText().toString().trim();
        email = etemail.getText().toString().trim();
        password = etpassword.getText().toString().trim();
        phone = etphone.getText().toString().trim();



        if(Pattern.matches("[0-9]+", surname)){
            etsurname.setError("Not a valid name");
            if(surname.isEmpty()){
                etsurname.setError("this field cannot be empty");
            }
            return;
        }

        if(Pattern.matches("[0-9]+", firstname)){
            etfirstname.setError("Not a valid name");
            if(firstname.isEmpty()){
                etfirstname.setError("this field cannot be empty");
            }
            return;
        }
        if(email.isEmpty()){
            etemail.setError("Please input a valid email address");
            return;
        }
        if(password.isEmpty() || password.length() < 6){
            etpassword.setError("Please input your password");
            return;
        }
        if(!Pattern.matches("[0-9]+", phone) || phone.length() != 11) {
            etphone.setError("Please enter a valid phone number");
           
        }else{

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(AdminRegistration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(AdminRegistration.this,
                                    "createUserWithEmail:onComplete" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                            if(!task.isSuccessful()){
                                Toast.makeText(AdminRegistration.this,
                                        "Authentication faild." + task.getException(), Toast.LENGTH_SHORT ).show();
                            }else{
                                FirebaseDatabase database =  FirebaseDatabase.getInstance();
                                DatabaseReference mRef =  database.getReference().child("Users").push();
                                FirebaseUser user =  firebaseAuth.getCurrentUser();
                                String userId = user.getUid();
                                mRef.child(firstname).child("sur-name").setValue(surname);
                                mRef.child(firstname).child("first name").setValue(firstname);
                                mRef.child(firstname).child("email").setValue(email);
                                mRef.child(firstname).child("password").setValue(password);
                                mRef.child(firstname).child("phone").setValue(phone);
                                mRef.child(firstname).child("userId").setValue(userId);
                                Intent intent = new Intent(AdminRegistration.this, LoginPage.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }

    }
    private void weeklyActivity() {
        // Override active layout
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.weekly_activity, null);
        // AlertDialog used for pop-Ups
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminRegistration.this);
        builder.setView(view);

        builder.setCancelable(true)
//                positive button is used to indicate whether to save or update
                .setPositiveButton( "Continue",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogBox, int id) {
                            }
                        })
                // Used to set Negative button to cancel
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });


    }
    private void showDialog() {
        // Override active layout
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.selectregistration, null);
        // AlertDialog used for pop-Ups
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminRegistration.this);
        builder.setView(view);

        // Used to link or get views in the dialogBox
        TextView text = view.findViewById(R.id.reg);
        final RadioButton pastor = view.findViewById(R.id.btnpastor);
        final RadioButton member = view.findViewById(R.id.btnmember);

        builder.setCancelable(false)
//                positive button is used to indicate whether to save or update
                .setPositiveButton( "Continue",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogBox, int id) {
                            }
                        })
                // Used to set Negative button to cancel
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show toast message when no text is entered

                        if (pastor.isChecked()==false && member.isChecked()==false){

                            Toast.makeText(AdminRegistration.this, " select registration type ",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(member.isChecked()==true) {
                            startActivity(new Intent(AdminRegistration.this, MembersRegistration.class));
                        }else if(pastor.isChecked()==true) {
                            startActivity(new Intent(AdminRegistration.this, PastorRegistration.class));
                        }
                        else {
                            alertDialog.dismiss();
                        }

                        // check if user updating note


                    }
                });


    }
}
