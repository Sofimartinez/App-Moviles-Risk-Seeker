package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IniciarSesionActivity extends AppCompatActivity {

    private EditText correo;
    private EditText contraseña;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static final String TAG = "InicioSesionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        mAuth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.idcorreologin);
        contraseña = findViewById(R.id.idcontraseñalogin);

    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void IniciarSession(View view){

        mAuth.signInWithEmailAndPassword(correo.getText().toString(), contraseña.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();


                            mDatabase = FirebaseDatabase.getInstance().getReference("Usuario").child(uid);

                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String nombre = snapshot.child("nombre").getValue().toString();
                                        String apellido = snapshot.child("apellido").getValue().toString();

                                        Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso",
                                                Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                        i.putExtra("invitado",false);
                                        i.putExtra("nombre",nombre+ " " + apellido);
                                        startActivity(i);
                                        finish();

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Usuario no encontrado",
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                        i.putExtra("invitado",true);
                                        i.putExtra("nombre","");
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w(TAG, "Error al iniciar sesion", error.toException());
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(getApplicationContext(), "Fallo al inicial sesión",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}