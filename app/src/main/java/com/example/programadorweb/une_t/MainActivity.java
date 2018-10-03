package com.example.programadorweb.une_t;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.programadorweb.une_t.Modal.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnRegister;
    RelativeLayout rootLayout;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                                          .setDefaultFontPath("fonts/Arkhip_font.ttf")
                                                           .setFontAttrId(R.attr.fontPath)
                                                           .build());
        setContentView(R.layout.activity_main);

        //init firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");


        //init view

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        //Event

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view){

                showRegisterDialog();

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view){

                showLoginDialog();

            }
        });
    }

    private void  showLoginDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("LOGIN");
        dialog.setMessage("Por Favor usa el correo electrónico para ingresar");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText edtEmail = (MaterialEditText) login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = (MaterialEditText) login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);

        //set button



        dialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //set disabled button sign in is processing

                btnSignIn.setEnabled(false);

                 //check validation
                if(TextUtils.isEmpty(edtEmail.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Por Favor Ingrese el Correo", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edtPassword.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Por Favor Ingrese el Password", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                if(edtPassword.getText().toString().length() < 6)
                {
                    Snackbar.make(rootLayout, "El Password debe mas largo", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();
                //Login

                auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>(){

                            @Override
                            public void onSuccess(AuthResult authResult) {
                                  waitingDialog.dismiss();
                                 startActivity(new Intent(MainActivity.this,Welcome.class));
                                 finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout,"Fallo"+e.getMessage(),Snackbar.LENGTH_SHORT).show();

                        //active button
                        btnSignIn.setEnabled(true);
                    }
                });

            }

                });

        dialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }

      private void  showRegisterDialog(){
          final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
          dialog.setTitle("REGISTRO");
          dialog.setMessage("Por Favor usa el correo electrónico para registrarte");

          LayoutInflater inflater = LayoutInflater.from(this);
          View register_layout = inflater.inflate(R.layout.layout_register,null);

          final MaterialEditText edtEmail = (MaterialEditText) register_layout.findViewById(R.id.edtEmail);
          final MaterialEditText edtPassword = (MaterialEditText) register_layout.findViewById(R.id.edtPassword);
          final MaterialEditText edtName = (MaterialEditText) register_layout.findViewById(R.id.edtName);
          final MaterialEditText edtPhone = (MaterialEditText) register_layout.findViewById(R.id.edtPhone);

          dialog.setView(register_layout);

          //set button

          dialog.setPositiveButton("REGISTRO", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();

                  //check validation
                  if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                      Snackbar.make(rootLayout, "Por Favor Ingrese el Correo", Snackbar.LENGTH_SHORT).show();
                      return;
                  }
                  if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                      Snackbar.make(rootLayout, "Por Favor Ingrese el Password", Snackbar.LENGTH_SHORT).show();
                      return;
                  }


                  if (edtPassword.getText().toString().length() < 6) {
                      Snackbar.make(rootLayout, "El Password debe mas largo", Snackbar.LENGTH_SHORT).show();
                      return;
                  }

                  if (TextUtils.isEmpty(edtName.getText().toString())) {
                      Snackbar.make(rootLayout, "Por Favor Ingrese el Nombre Completo", Snackbar.LENGTH_SHORT).show();
                      return;
                  }

                  if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                      Snackbar.make(rootLayout, "Por Favor Ingrese el Nùmero Telefonico", Snackbar.LENGTH_SHORT).show();
                      return;
                  }

                  //Registrar un nuevo usuario

                  auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                          .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                              @Override
                              public void onSuccess(AuthResult authResult) {
                                  //guardar usuarioen la base de datos

                                  User user = new User();
                                  user.setEmail(edtEmail.getText().toString());
                                  user.setPassword(edtPassword.getText().toString());
                                  user.setName(edtName.getText().toString());
                                  user.setPhone(edtPhone.getText().toString());

                                  //use email to key
                                  user.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                          .setValue(user)
                                          .addOnSuccessListener(new OnSuccessListener<Void>() {

                                              public void onSuccess(Void aVoid) {

                                                  Snackbar.make(rootLayout, "Registro Exitoso ", Snackbar.LENGTH_SHORT).show();

                                              }
                                          })
                                          .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {

                                                  Snackbar.make(rootLayout, "Fallo" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                                              }
                                          });
                              }
                          })
                          .addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Snackbar.make(rootLayout, "Fallo" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                              }
                          });
              }
          }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();
              }
          });

          dialog.show();
      }
}
