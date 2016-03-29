package com.example.pasmarla.pruebainterfazdadm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter adaptador;
    private ImageView banderita;
    private TextView msg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Escritura lectura fichero preferencias.
        final SharedPreferences preferencias = getSharedPreferences("idioma",MODE_PRIVATE);

         banderita=(ImageView) findViewById(R.id.banderita);
         msg = (TextView) findViewById(R.id.msg);

        final Button arka = (Button) findViewById(R.id.arka);
        arka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mi_intent = new Intent(MainActivity.this, FullscreenActivity.class);

                startActivity(mi_intent);


                /*Intent mi_intent = new Intent(MainActivity.this, Set_records.class);

                startActivity(mi_intent);*/

            }
        });
        spinner= (Spinner) findViewById(R.id.spinner);
        String[] Idiomas= {"Español","にほんご","English"};
        adaptador= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Idiomas);
        spinner.setAdapter(adaptador);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
        public void onItemSelected(AdapterView<?> parent, View view,  int position, long id){
                Log.d("Dwe","Elegiste:"+Integer.toString(position)+ (String) parent.getItemAtPosition(position));
                if(position==0){banderita.setImageResource(R.mipmap.spain_flag_pin_64);
                    Context context = getApplicationContext();
                    CharSequence text = "Bienvenido!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                            msg.setText("Bienvenido a Retrodroid, elige un juego para continuar!");
                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putInt("id",position );
                    editor.commit();
                }

                if(position==1){banderita.setImageResource(R.mipmap.japan_flag_pin_64);
                    Context context = getApplicationContext();
                    CharSequence text = "かんげい!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();



                            msg.setText("Retrodroid はかんげいです, 同人ゲームをえさぶ ");
                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putInt("id", position);
                    editor.commit();





                }

                if(position==2){banderita.setImageResource(R.mipmap.united_kingdom_flag_pin_64);
                    Context context = getApplicationContext();
                    CharSequence text = "Welcome!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();



                            msg.setText("Welcome to Retrodroid, choose a game to continue!");
                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putInt("id", position);
                    editor.commit();



                }

            }





            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });











        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();





        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {



                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
