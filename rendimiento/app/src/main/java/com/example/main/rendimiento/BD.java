package com.example.main.rendimiento;


import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Created by jorge on 25/10/2017.
 */

public class BD {


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
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    public void InsertaBD (String qery) {
        try{
            Statement st=conexionBD().createStatement();
            ResultSet rs=st.executeQuery(qery);
        }catch(Exception ex){
            //Toast.makeText(getApplicationContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
