package com.example.main.rendimiento;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    Button login;

    Statement st;
    ResultSet rs;

    boolean ban;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);


    }

    public void goPallet(View view)
    {
        ban=false;
        try{
            st=conexionBD().createStatement();
            rs=st.executeQuery("select count(c_codigo_usu) as sino from RendimientoEmpaque.dbo.usuarios where c_codigo_usu='"+username.getText().toString().trim()+"' and v_pass_usu='"+password.getText().toString().trim()+"'");
            //Toast.makeText(getApplicationContext(),"select count(c_codigo_usu) as sino from RendimientoEmpaque.dbo.usuarios where c_codigo_usu='"+username.getText().toString().trim()+"' and v_pass_usu='"+password.getText().toString().trim()+"'", Toast.LENGTH_SHORT).show();
            while (rs.next()){
                if (Integer.parseInt(rs.getString("sino"))>0){
                    ban=true;
                }else{
                    ban=false;
                }
                break;
            }
        }catch (IllegalStateException ile){
            Toast.makeText(getApplicationContext(),ile.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (ban==true){
            Intent intent = new Intent(this, palets_activity.class);
            intent.putExtra("ParUsuario", username.getText().toString().trim());
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Usuaio o contraeña incorrectos. Verifique por favor", Toast.LENGTH_SHORT).show();
        }
    }

    public Connection conexionBD(){
        String servidor,usuario,pass;
        servidor="192.168.3.254";
        String basedatos="RendimientoEmpaque";
        usuario="sa";
        pass="inventumc762$";

        Connection conexion=null;
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://"+servidor+";databaseName="+basedatos+";user="+usuario+";password="+pass+";");
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
        return conexion;
    }




}
