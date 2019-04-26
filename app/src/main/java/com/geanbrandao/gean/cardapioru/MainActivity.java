package com.geanbrandao.gean.cardapioru;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

/**
 * aqui vai verificar se esta logado
 * caso esteja vai para a tela HOME
 * caso não esteja chama a tela de login
 */
public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 984;

    private FirebaseAuth auth;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.i("Login", "ja esta logado");
            Toast.makeText(context, "Já está logado", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("Login", "Nao tem usuario logado no momento");
            //signIn();
        }

    }

    private void signIn() {
        Log.i("Login", "chama tela de login");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        //.setLogo(R.drawable.logo)
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                //startActivity(SignedInActivity.createIntent(this, response));
                // ve se o usuario ja esta na base de dados
                Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                Log.i("Login", "Login realizado com sucesso");
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(context, "SignIn cancelado", Toast.LENGTH_SHORT).show();
                    Log.i("Login", "SignIn cancelado");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                    Log.i("Login", "Error no internet conection");
                    return;
                }

                Toast.makeText(context, "Erro desconhecido", Toast.LENGTH_SHORT).show();
                Log.i("Login", "Sign-in error: ", response.getError());
            }
        }
    }

}
