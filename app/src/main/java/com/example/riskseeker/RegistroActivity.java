package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riskseeker.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText correo;
    private EditText contrasena;
    private EditText contrasenaConfirmacion;
    private EditText nombre;
    private EditText apellido;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.idcorreo);
        contrasena = findViewById(R.id.idcontraseña);
        contrasenaConfirmacion = findViewById(R.id.idcontraseñaconfirmacion);
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Compruebe si el usuario ha iniciado sesión
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void registrarUsuario(View view){

        String contra = contrasena.getText().toString();
        if(contra.length()<6) {
            contrasena.setError(getString(R.string.contraminimo));
            Toast.makeText(this,"La contraseña debe tener mínimo 6 caracteres",Toast.LENGTH_LONG).show();
        }else{
            registro();
        }
    }


    private void registro() {
        if(contrasena.getText().toString().equals(contrasenaConfirmacion.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(), contrasena.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Registro del usuario exitoso
                                FirebaseUser user = mAuth.getCurrentUser();

                                inicializarFirebase();

                                Usuario usuario = new Usuario(nombre.getText().toString(), apellido.getText().toString());
                                databaseReference.child("Usuario").child(user.getUid()).setValue(usuario).addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> taskRegistro) {
                                                if(taskRegistro.isSuccessful()){
                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    i.putExtra("invitado", false);
                                                    i.putExtra("nombre", nombre.getText().toString() + " " + apellido.getText().toString());
                                                    startActivity(i);
                                                    finish();

                                                    Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    user.delete();
                                                    Toast.makeText(getApplicationContext(), "Registro fallido", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        }
                                );

                            } else {
                                // Fallo al crear el usuario
                                Toast.makeText(getApplicationContext(), "Registro fallido", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.putExtra("invitado", true);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }
}