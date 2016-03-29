package com.example.pasmarla.pruebainterfazdadm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class record extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;
    private Button back;
    private String s_back;
    private int position;
    //Controles
    ListView lvEntradas;

    //Total de entradas de cada idioma
    int totalEntradas = 0;
    //Arrays con los términos en inglés / español
    String[] usuario;
    //String[] puntuacion;

    //Nombre del fichero con el diccionario
    String nomFichero;


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
            //puntuacion = new String[totalEntradas];
            try {
                String s;
                int linea = 0;
                InputStreamReader is = new InputStreamReader(openFileInput(nomFichero));
                BufferedReader fich = new BufferedReader(is);
                while ((s = fich.readLine()) != null) {// Leer hasta fin de fichero



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

        setContentView(R.layout.activity_record);
//Escritura lectura fichero preferencias.
        final SharedPreferences preferencias = getSharedPreferences("idioma",MODE_PRIVATE);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        back=(Button) findViewById(R.id.back);

        lvEntradas = (ListView) findViewById(R.id.lvEntradas);

        nomFichero = "records.txt";


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

        lvEntradas.setAdapter(adapter);





        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.back).setOnTouchListener(mDelayHideTouchListener);



        // IDIOMAS

        int idiom=preferencias.getInt("id",position);
        if(idiom==0){

            s_back="Volver";

        }
        if(idiom==1){

            s_back="かえる";}

        if(idiom==2){

            s_back="Back";}


        back.setText(s_back);





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi_intent = new Intent(record.this, FullscreenActivity.class);


                finish();
                startActivity(mi_intent);

            }

        });




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
