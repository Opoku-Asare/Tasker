package fi.oulu.mobisocial.tasker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText username = (EditText) findViewById(R.id.usernameText);
        final EditText password = (EditText) findViewById(R.id.passwordText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String un=username.getText().toString();
                String pw=password.getText().toString();

                //Non Empty Username and password entered
                if(!un.isEmpty() && !pw.isEmpty()) {


                    String savedUsername=MainActivity.retrieveFromSharePreference(MainActivity.USERNAME_KEY);
                    String savedPassword=MainActivity.retrieveFromSharePreference(MainActivity.PASSWORD_KEY);

                    // Username and/or password exists... user is registered
                    if(!savedUsername.isEmpty() ||!savedPassword.isEmpty()){

                        //existing username and password mismatch
                        if(!un.trim().equals(savedUsername)||!pw.trim().equals(savedPassword)){
                            Toast.makeText(LoginActivity.this, "Username or Password combination does not match.. Please try again",Toast.LENGTH_LONG).show();
                        }else{

                            MainActivity.USER_LOGGED_IN=true;
                            setContentView(R.layout.activity_main);

                            finish();
                        }
                    }else{// user is not registered
                        MainActivity.saveToSharedPreference(MainActivity.USERNAME_KEY, un);
                        MainActivity.saveToSharedPreference(MainActivity.PASSWORD_KEY,pw);
                        Toast.makeText(LoginActivity.this, "User registered",Toast.LENGTH_LONG).show();
                        MainActivity.USER_LOGGED_IN=true;
                        setContentView(R.layout.activity_main);
                        finish();
                    }


                }

                else {
                    Toast.makeText(LoginActivity.this, "Username or Password cannot be empty",Toast.LENGTH_LONG).show();
                }



            }
        });



    }
}
