package com.example.main.rendimiento;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class palets_activity extends AppCompatActivity {

    EditText Npalet;
    Button Bingresa;
    Toolbar toolbar;
    Statement st;
    ResultSet rs;
    boolean ban;
    boolean otraban;
    String usuario;
    ListView ListEstibas;
    String c_codigo_prc;
    String c_codigo_env;

    BD clasebd=new BD();
    boolean bvalida=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palets_activity);

        Npalet=(EditText)findViewById(R.id.Npalet);
        Bingresa=(Button)findViewById(R.id.Bingresa);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ListEstibas=(ListView) findViewById(R.id.ListEstibas);

        usuario=getIntent().getExtras().getString("ParUsuario");

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(null);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(Npalet.getWindowToken(), 0);


        otraban=false;
        Bingresa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //try{
                    //st=claseConecta.conexionBD().createStatement();
                    //rs=st.executeQuery("select count(c_codigo_pal) as sino from nra.dbo.t_palet");
                    //rs=st.executeQuery("select count(c_consec_scc) as sino from nra.dbo.t_palet where c_consec_scc='"+Npalet.getText().toString().trim().substring(Npalet.getText().toString().trim().length() - 11,Npalet.getText().toString().trim().length() -1)+"'");
                    //Toast.makeText(getApplicationContext(),"select count(c_codigo_usu) as sino from RendimientoEmpaque.dbo.usuarios where c_codigo_usu='"+username.getText().toString().trim()+"' and v_pass_usu='"+password.getText().toString().trim()+"'", Toast.LENGTH_SHORT).show();
                    //while (rs.next()){
                        //if (Integer.parseInt(rs.getString("sino"))>0){
                            //ban=true;
                        //}else{
                            //ban=false;
                        //}
                        //break;
                    //}
                //}
                //catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                //}
                validar();
            }
        });

        llenarEstibas();

        Npalet.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int tlist;
                tlist=ListEstibas.getCount();
                bvalida=false;
                if (ListEstibas.getCount()>0){
                    for(i=0;i<tlist;i++ ){
                        try {
                            if (ListEstibas.getItemAtPosition(i).toString().trim().equals(Npalet.getText().toString().trim())) {
                                bvalida = true;
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                if (bvalida==false){
                    if (Npalet.getText().toString().trim().length()>=6){
                        if (otraban==false){
                            otraban=true;
                            //Npalet.setText(Npalet.getText().toString().trim().substring(Npalet.getText().toString().trim().length() - 10,Npalet.getText().toString().trim().length() -1));
                            validar();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Ya se ingreso esta estiba, no se puede agregar otra vez.", Toast.LENGTH_SHORT).show();
                    Npalet.setText(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    protected void validar (){

        if (  (Npalet.getText().toString().trim().length()>0 && selPalet()==true)){ //
            Intent intent = new Intent(this, Cajas_Activity.class);
            intent.putExtra("parametro", Npalet.getText()+"");
            intent.putExtra("ParUsuario", usuario);
            intent.putExtra("Parenv", c_codigo_env);
            intent.putExtra("Parprc", c_codigo_prc);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"No se encontro el palet.", Toast.LENGTH_SHORT).show();
        }
    }
    protected void cerrarSesion(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void llenarEstibas(){
        ArrayList listado1 = new ArrayList();

        try{
            Statement st=clasebd.conexionBD().createStatement();
            ResultSet rs=st.executeQuery("select distinct c_codigo_pal from RendimientoEmpaque.dbo.rendimiento where  convert(date,d_creacion_ren)=CONVERT(date,getdate())");
            while (rs.next()){
                listado1.add(rs.getString("c_codigo_pal"));
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter adaptador1 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listado1);

        ListEstibas.setAdapter(adaptador1);
        ListEstibas.setSelection(ListEstibas.getCount() - 1);
    }

    protected boolean selPalet(){
        boolean siPalet;
        siPalet=false;
        try{
            Statement st=clasebd.conexionBD().createStatement();
            ResultSet rs=st.executeQuery("select distinct pro.c_codigo_env,pro.c_codigo_prc from nra.dbo.t_palet as pal inner join nra.dbo.t_producto as pro on pal.c_codigo_pro=pro.c_codigo_pro\n" +
                    "where c_codigo_pal='"+Npalet.getText().toString().trim()+"' or c_codigo_est='"+Npalet.getText().toString().trim()+"' and c_codigo_tem='08'");
            while (rs.next()){
                c_codigo_env=rs.getString("c_codigo_env");
                c_codigo_prc=rs.getString("c_codigo_prc");
                siPalet=true;
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (Npalet.getText().toString().trim().substring(0,3).toUpperCase().equals( "LOT".toString().trim())){
            siPalet=true;
        }
        return siPalet;
    }



}
