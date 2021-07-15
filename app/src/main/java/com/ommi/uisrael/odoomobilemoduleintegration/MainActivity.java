package com.ommi.uisrael.odoomobilemoduleintegration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ommi.uisrael.odoomobilemoduleintegration.datos.OdooUtility;
import com.ommi.uisrael.odoomobilemoduleintegration.datos.SharedData;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

import static android.os.Looper.loop;
import static android.os.Looper.prepare;

public class MainActivity extends AppCompatActivity {

    public OdooUtility odoo;
    private long loguinTaskId;

    EditText editUsername;
    EditText editPassword;
    EditText editDatabase;
    EditText editServerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String database = SharedData.getKey(MainActivity.this, "database");
        String serverAddress = SharedData.getKey(MainActivity.this, "serverAddress");
        String username = SharedData.getKey(MainActivity.this, "username");
        String password = SharedData.getKey(MainActivity.this, "password");

        editServerAddress = (EditText) findViewById(R.id.editServerAddress);
        editDatabase = (EditText) findViewById(R.id.editDatabase);
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);

        editServerAddress.setText(serverAddress);
        editDatabase.setText(database);
        editUsername.setText(username);
        editPassword.setText(password);


    }






    public void onClickLogin(View view) {
        switch (view.getId()) {
            case R.id.buttonLoguin2:

                String password = editPassword.getText().toString();
                String username = editUsername.getText().toString();
                String database = editDatabase.getText().toString();
                String serverAddress = editServerAddress.getText().toString();

                odoo = new OdooUtility(serverAddress, "common");
                loguinTaskId = odoo.login(listener, database, username, password);

                SharedData.setKey(MainActivity.this, "password", password);
                SharedData.setKey(MainActivity.this, "username", username);
                SharedData.setKey(MainActivity.this, "database", database);
                SharedData.setKey(MainActivity.this, "serverAddress", serverAddress);

                break;


        }
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        public void onResponse(long id, Object result) {

            prepare();
            if (id == loguinTaskId) {
                if (result instanceof Boolean && (Boolean) result == false) {
                    odoo.MessageDialog(MainActivity.this, "Error de Ingreso revise si el usuario y contraseña estan correctos ");

                } else {
                    String uid = result.toString();
                    SharedData.setKey(MainActivity.this, "uid", uid);
                    //odoo.MessageDialog(MainActivity.this, "Ingreso exitoso");
                    Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
                    MainActivity.this.startActivity(myIntent);

                }
            }

            loop();
        }


        public void onError(long id, XMLRPCException error) {

            prepare();
            Log.e("LOGUIN", error.getMessage());
            odoo.MessageDialog(MainActivity.this, "LOGIN ERRROR " + error.getMessage());

            loop();

        }

        public void onServerError(long id, XMLRPCServerException error) {

            prepare();
            Log.e("LOGIN", error.getMessage());
            odoo.MessageDialog(MainActivity.this, "Login Error " + error.getMessage());


            loop();
        }
    };


}
