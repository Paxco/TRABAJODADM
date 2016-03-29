package com.example.pasmarla.pruebainterfazdadm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Set_records extends AppCompatActivity {
    //Nombre del fichero
    String nomFichero;
    private Button atras;
    private Button play;
    private Button insertar;
    private String s_play;
    private String s_insertar;
    private String s_back;
    private TextView dif;
    private String dif_s;
    int totalEntradas = 0;
    String[] usuario=new String[4];
    String[] nombre=new String[4];
    String[] puntuacion=new String[4];
    int aux;
    int posicion;
    private int position;



    //Si nombreFichero no existe lo crea escribiendo dentro contenido.
    //devuelve true si existía o se creó sin problemas. false en caso contrario
    protected boolean crearSiNoExiste(String nombreFichero, String contenido){
        File file = new File(getFilesDir(), nombreFichero);
//Si el Fichero no existe se crea con una entrada inicial
        if (!file.exists()) {
            try{
                FileOutputStream out = new FileOutputStream(file);
                out.write(contenido.getBytes());
                out.close();
                Toast.makeText(this, "Creado fichero " + nombreFichero, Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                Toast.makeText(this, "Error creando fichero", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }



    //lee el fichero devolviendo el número de entradas leídas
//Reserva el espacio que corresponda en los arrays de entradas
    protected int leerFichero ()
    {
        totalEntradas=contarLineasFichero();
        if (totalEntradas>0) {
            usuario = new String[totalEntradas];
            try {
                String s;
                int linea = 0;
                aux=0;
                posicion=0;
                InputStreamReader is = new InputStreamReader(openFileInput(nomFichero));
                BufferedReader fich = new BufferedReader(is);
                while ((s = fich.readLine()) != null) {// Leer hasta fin de fichero

                    if(aux==0){
                        Log.d("aux",Integer.toString(posicion));
                        nombre[posicion]=s;
                        aux++;
                        posicion++;

                    }
                    else if(aux==1){
                        Log.d("aux",Integer.toString(posicion));
                        puntuacion[posicion]= s;
                        aux++;
                        posicion++;

                    }
                    else if(aux==2){aux=0;}


                    usuario[linea] = s;

                    linea++;



                }
                is.close(); // Cerrar el fichero






            } catch (IOException ex){
                ex.printStackTrace();
                Toast.makeText(this, "Error leyendo fichero", Toast.LENGTH_SHORT).show();
            }
        }
        return totalEntradas;
    }

    protected int contarLineasFichero ()
    {
        int lineas = 0;
        try {
            InputStreamReader is = new InputStreamReader(openFileInput(nomFichero));
            BufferedReader fich = new BufferedReader(is);
            String s;
// Leer hasta fin de fichero
            while ((s = fich.readLine()) != null)
                lineas++;
            Toast.makeText(this, "Líneas leídas: "+lineas, Toast.LENGTH_SHORT).show();
// Cerrar el fichero
            is.close();
        } catch (IOException ex){
            ex.printStackTrace();
            Toast.makeText(this, "Error contando líneas de "+nomFichero, Toast.LENGTH_SHORT).show();
        }
        return lineas;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_records);
        nomFichero="records.txt";

        //¿Existe el fichero? Si no existe crear con una entrada inicial
        String a1 = "Dididalda            400"  + System.getProperty("line.separator") +"________________"+ System.getProperty("line.separator") + "Batman                200"
                + System.getProperty("line.separator")+"________________"+ System.getProperty("line.separator")+ "Paxco                 300"
                + System.getProperty("line.separator")+"________________"+ System.getProperty("line.separator");
        if (!crearSiNoExiste(nomFichero, a1)) { //Volver a la actividad inicial tras indicar el error.
            Toast.makeText(this, "Problemas creando el fichero de puntuaciones", Toast.LENGTH_LONG).show();
            finish();
        }
        if (leerFichero() == 0) { //Volver a la actividad inicial tras indicar el error.
            Toast.makeText(this, "Problemas con el fichero de puntuaciones", Toast.LENGTH_LONG).show();
            finish();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usuario);

//Escritura lectura fichero preferencias.
        final SharedPreferences preferencias = getSharedPreferences("idioma",MODE_PRIVATE);



        atras=(Button) findViewById(R.id.back);
        final EditText usuario =
                (EditText)findViewById(R.id.usuario);
        final String ganador = usuario.getText().toString();
        play= (Button) findViewById(R.id.play);
        insertar= (Button) findViewById(R.id.insertar);
        dif=(TextView)findViewById(R.id.texto);

        // IDIOMAS

        final int idiom=preferencias.getInt("id",position);
        if(idiom==0){
            s_play="Jugar de nuevo";
            s_insertar="introducir record";
            s_back="Volver";
            dif_s="Selecciona dificultad:";

        }
        if(idiom==1){
            s_play="かいし";
            s_insertar="とくてん";
            s_back="かえる";
            dif_s= "めんどうをえらぶ:";
        }
        if(idiom==2){
            s_play="Play again";
            s_insertar="insert high-score";
            s_back="Back";
            dif_s="Select difficulty:";
        }

        play.setText(s_play);
        insertar.setText(s_insertar);
        atras.setText(s_back);
        dif.setText(dif_s);

        final  int score = 0;
        final SharedPreferences preferencias2 = getSharedPreferences("scr",MODE_PRIVATE);
        final int scr=preferencias2.getInt("id3", score);
        Log.d("score:", Integer.toString(scr));

        //comparador
        //si la punuacion es maxima
      /* if(Integer.parseInt(puntuacion[0])>scr){
           puntuacion[0]=Integer.toString(scr);
            aux=0;
            insertar.setVisibility(View.VISIBLE);}
        //no es maxima pero es mayor que 2
        else if(Integer.parseInt(puntuacion[1])>scr){
           puntuacion[1]=Integer.toString(scr);
            aux=2;
            insertar.setVisibility(View.VISIBLE);}

        else if(Integer.parseInt(puntuacion[2])>scr){
           puntuacion[2]=Integer.toString(scr);
            aux=3;
            insertar.setVisibility(View.VISIBLE);}

        else{aux=4;
            insertar.setVisibility(View.INVISIBLE);}*/



        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi_intent = new Intent(Set_records.this, FullscreenActivity.class);



                finish();
                startActivity(mi_intent);

            }

        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi_intent = new Intent(Set_records.this, Juego_breakout.class);


                finish();
                startActivity(mi_intent);

            }

        });

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* INTRODUCIR ESCRITURA DE RECORD*/
                //nombre[aux]=ganador;

                /*                               */


                Intent mi_intent = new Intent(Set_records.this, FullscreenActivity.class);

                finish();
                startActivity(mi_intent);

            }

        });

    }

}
