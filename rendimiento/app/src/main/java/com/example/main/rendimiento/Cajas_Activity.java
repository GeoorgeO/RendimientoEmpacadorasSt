package com.example.main.rendimiento;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Vector;

public class Cajas_Activity extends AppCompatActivity {

    TextView labelPalet;
    EditText codEmp;
    ListView listEmp;
    Button bGuardar;
    Button bLimpiar;
    Button bSalir;
    TextView totalCajas;

    Button bc1,bc2,bc3,bc4;
    TextView ttn1,ttn2,ttn3,ttn4,Gcontador;
    int contaCajas,BFoco,tn1,tn2,tn3,tn4;
    String usuario;
    Vector<String> vCara1 = new Vector<String>();
    Vector<String> vCara2 = new Vector<String>();
    Vector<String> vCara3 = new Vector<String>();
    Vector<String> vCara4 = new Vector<String>();

    String c_codigo_env,c_codigo_prc;

    ArrayList listado1 = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cajas_activity);

        labelPalet =(TextView) findViewById(R.id.labelPalet);
        codEmp=(EditText)findViewById(R.id.codEmp);
        listEmp=(ListView) findViewById(R.id.listEmp);
        bGuardar=(Button)findViewById(R.id.bGuardar);
        bLimpiar=(Button)findViewById(R.id.bLimpiar);
        bSalir=(Button) findViewById(R.id.bSalir);


        totalCajas=(TextView)findViewById(R.id.totalCajas);
        contaCajas=0;

        labelPalet.setText(getIntent().getStringExtra("parametro"));
        usuario=getIntent().getExtras().getString("ParUsuario");
        c_codigo_env=getIntent().getExtras().getString("Parenv");
        c_codigo_prc=getIntent().getExtras().getString("Parprc");



        bc1=(Button) findViewById(R.id.bc1);
        bc2=(Button) findViewById(R.id.bc2);
        bc3=(Button) findViewById(R.id.bc3);
        bc4=(Button) findViewById(R.id.bc4);

        ttn1 =(TextView) findViewById(R.id.tn1);
        ttn2 =(TextView) findViewById(R.id.tn2);
        ttn3 =(TextView) findViewById(R.id.tn3);
        ttn4 =(TextView) findViewById(R.id.tn4);
        Gcontador=(TextView) findViewById(R.id.Gcontador);

         codEmp.setInputType(InputType.TYPE_NULL);

        tn1=0;
        tn2=0;
        tn3=0;
        tn4=0;

        BFoco=1;
        bc1.setBackgroundColor(Color.BLUE);
        bc2.setBackgroundColor(Color.DKGRAY);
        bc3.setBackgroundColor(Color.DKGRAY);
        bc4.setBackgroundColor(Color.DKGRAY);

        codEmp.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (codEmp.getText().toString().trim().length()>0 && codEmp.getText().toString().trim().length()<=6){
                    LlenarLista();

                    switch(BFoco){
                        case 1:
                            vCara1.add(codEmp.getText().toString().trim());
                            tn1=vCara1.size();
                            ttn1.setText(Integer.toString(tn1));
                            Gcontador.setText(Integer.toString(vCara1.size()));
                            break;
                        case 2:
                            vCara2.add(codEmp.getText().toString().trim());
                            tn2=vCara2.size();
                            ttn2.setText(Integer.toString(tn2));
                            Gcontador.setText(Integer.toString(vCara2.size()));
                            break;
                        case 3:
                            vCara3.add(codEmp.getText().toString().trim());
                            tn3=vCara3.size();
                            ttn3.setText(Integer.toString(tn3));
                            Gcontador.setText(Integer.toString(vCara3.size()));
                            break;
                        case 4:
                            vCara4.add(codEmp.getText().toString().trim());
                            tn4=vCara4.size();
                            ttn4.setText(Integer.toString(tn4));
                            Gcontador.setText(Integer.toString(vCara4.size()));
                            break;
                    }

                    contaCajas=vCara1.size()+vCara2.size()+vCara3.size()+vCara4.size();
                    totalCajas.setText("Total capturadas: "+Integer.toString(contaCajas));

                    codEmp.setText("");
                }else{
                    if (codEmp.getText().toString().trim().length()>0){
                        Toast.makeText(getApplicationContext(),"Codigo no valido, Favor de verificarlo." , Toast.LENGTH_SHORT).show();
                        codEmp.setText("");
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        bGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (vCara1.size()+vCara2.size()+vCara3.size()+vCara4.size()>0){
                    confirmar();
                }else{
                    Toast.makeText(getApplicationContext(),"No hay datos ingresados." , Toast.LENGTH_SHORT).show();
                }

            }
        });

        bLimpiar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {



                limpiarLista();
            }
        });

        bSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cerrarCajas();
            }
        });

        bc1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BFoco=1;
                bc1.setBackgroundColor(Color.BLUE);
                bc2.setBackgroundColor(Color.DKGRAY);
                bc3.setBackgroundColor(Color.DKGRAY);
                bc4.setBackgroundColor(Color.DKGRAY);
                Gcontador.setText(Integer.toString(vCara1.size()));
            }
        });
        bc2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BFoco=2;
                bc1.setBackgroundColor(Color.DKGRAY);
                bc2.setBackgroundColor(Color.BLUE);
                bc3.setBackgroundColor(Color.DKGRAY);
                bc4.setBackgroundColor(Color.DKGRAY);
                Gcontador.setText(Integer.toString(vCara2.size()));
            }
        });
        bc3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BFoco=3;
                bc1.setBackgroundColor(Color.DKGRAY);
                bc2.setBackgroundColor(Color.DKGRAY);
                bc3.setBackgroundColor(Color.BLUE);
                bc4.setBackgroundColor(Color.DKGRAY);
                Gcontador.setText(Integer.toString(vCara3.size()));
            }
        });
        bc4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BFoco=4;
                bc1.setBackgroundColor(Color.DKGRAY);
                bc2.setBackgroundColor(Color.DKGRAY);
                bc3.setBackgroundColor(Color.DKGRAY);
                bc4.setBackgroundColor(Color.BLUE);
                Gcontador.setText(Integer.toString(vCara4.size()));
            }
        });

    }

        public void LlenarLista (){
            listado1.add(codEmp.getText().toString().trim() );

            ArrayAdapter adaptador1 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listado1);

            listEmp.setAdapter(adaptador1);
            //contaCajas++;

            listEmp.setSelection(listEmp.getCount() - 1);

            //totalCajas.setText("Total capturadas: "+Integer.toString(contaCajas));
        }

        public void limpiarLista(){
            listado1.clear();
            ArrayAdapter adaptador1 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listado1);
            listEmp.setAdapter(adaptador1);
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("BORRAR");
            dialogo1.setMessage("¿Estas seguro que quieres LIMPIAR esta cara?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    switch (BFoco){
                        case 1:
                            vCara1.removeAllElements();
                            Gcontador.setText(Integer.toString(vCara1.size()));
                            ttn1.setText(Integer.toString(vCara1.size()));
                            break;
                        case 2:
                            vCara2.removeAllElements();
                            Gcontador.setText(Integer.toString(vCara2.size()));
                            ttn2.setText(Integer.toString(vCara2.size()));
                            break;
                        case 3:
                            vCara3.removeAllElements();
                            Gcontador.setText(Integer.toString(vCara3.size()));
                            ttn3.setText(Integer.toString(vCara3.size()));
                            break;
                        case 4:
                            vCara4.removeAllElements();
                            Gcontador.setText(Integer.toString(vCara4.size()));
                            ttn4.setText(Integer.toString(vCara4.size()));
                            break;
                    }
                    contaCajas=vCara1.size()+vCara2.size()+vCara3.size()+vCara4.size();
                    totalCajas.setText("Total capturadas: "+Integer.toString(contaCajas));
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();


            //contaCajas=0;

        }

        public void confirmar (){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("CONFIRMAR");
            dialogo1.setMessage("Has capturado "+Integer.toString(contaCajas)+" cajas , ¿Deseas continuar?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    insertaDatos();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }

        public void cerrarCajas(){
            Intent intent = new Intent(this, palets_activity.class);
            intent.putExtra("ParUsuario", usuario);
            startActivity(intent);
            finish();
        }

        public void insertaDatos (){
            int i;
            String cadena;


            cadena="";

            if (vCara1.size()>0){
                for(i=0;i<vCara1.size();i++ ){
                    cadena=cadena+ "insert into RendimientoEmpaque.dbo.rendimiento values ('"+labelPalet.getText().toString().trim()+"','08','"+vCara1.get(i).toString().trim()+"','"+usuario.toString().trim()+"',getdate()) ";
                }
            }
            if (vCara2.size()>0){
                for(i=0;i<vCara2.size();i++ ){
                    cadena=cadena+ "insert into RendimientoEmpaque.dbo.rendimiento values ('"+labelPalet.getText().toString().trim()+"','08','"+vCara2.get(i).toString().trim()+"','"+usuario.toString().trim()+"',getdate()) ";
                }
            }
            if (vCara3.size()>0){
                for(i=0;i<vCara3.size();i++ ){
                    cadena=cadena+ "insert into RendimientoEmpaque.dbo.rendimiento values ('"+labelPalet.getText().toString().trim()+"','08','"+vCara3.get(i).toString().trim()+"','"+usuario.toString().trim()+"',getdate()) ";
                }
            }
            if (vCara4.size()>0){
                for(i=0;i<vCara4.size();i++ ){
                    cadena=cadena+ "insert into RendimientoEmpaque.dbo.rendimiento values ('"+labelPalet.getText().toString().trim()+"','08','"+vCara4.get(i).toString().trim()+"','"+usuario.toString().trim()+"',getdate()) ";
                }
            }
            if (c_codigo_env!=null) {
                cadena = cadena + "insert into RendimientoEmpaque.dbo.Rendimiento_Palet values ('08','" + labelPalet.getText().toString().trim() + "','" + c_codigo_env.toString().trim() + "','" + c_codigo_prc.toString().trim() + "') ";
            }
            if (InsertaBD(cadena)==true){
                cerrarCajas();
            }

            /*if (listEmp.getCount()>0){
                for(i=0;i<listEmp.getCount();i++ ){
                    cadena=cadena+ "insert into RendimientoEmpaque.dbo.rendimiento values ('"+labelPalet.getText().toString().trim()+"','08','"+listEmp.getItemAtPosition(i).toString().trim()+"','"+usuario.toString().trim()+"',getdate()) ";
                    //Toast.makeText(getApplicationContext(),listEmp.getItemAtPosition(i).toString().trim() , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),cadena , Toast.LENGTH_SHORT).show();
                }
                if (InsertaBD(cadena)==true){
                    cerrarCajas();
                }

            }*/
        }

    public Connection conexionBD( ){
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

    public boolean InsertaBD (String qery) {
        try{
            Statement st=conexionBD().createStatement();
            ResultSet rs=st.executeQuery(qery);

            return true;
        }catch(Exception ex){
            //Toast.makeText(getApplicationContext(),"ocurrio algo", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
            if (ex.getMessage().length()!=49){
                Toast.makeText(getApplicationContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
            else{
                Toast.makeText(getApplicationContext(),"Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                return true;
            }

        }
    }
}
