package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.adapter.ViewPagerAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.Preferencias;
import com.example.whatsapp.model.Contato;
import com.example.whatsapp.model.Usuario;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth usuarioAutenticacao;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String identificadorContato;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //autenticação
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar( toolbar );

        // tabLayoult
        tabLayout = findViewById(R.id.tabs_layoult);
        viewPager2 = findViewById(R.id.view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);



        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                if ( position == 0 ){
                    tab.setText("Conversas");
                }
                if ( position == 1 ){
                    tab.setText("Status");
                }
                if ( position == 2 ){
                    tab.setText("Contatos");
                }

            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.item_sair:
                deslogarUsuario();
                return true;

            case R.id.item_configuracoes:
                return true;

            case R.id.item_adicionar:
                abrirCadastroUsusario();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadastroUsusario(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //configurar a dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuario");
        alertDialog.setCancelable(false);

        EditText editText = new EditText(MainActivity.this);
        alertDialog.setView( editText );

        //configura os botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();

                if ( emailContato.isEmpty() ){
                    Toast.makeText(MainActivity.this,"Preencha o e-mail", Toast.LENGTH_LONG).show();
                }else{

                    //verifica se o usuario ja esta cadastrado
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    //recupera a instancia
                    firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child( identificadorContato );

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null ){

                                // Recuperar dados do contato a ser adicionado
                                Usuario usuarioContato = snapshot.getValue( Usuario.class );

                                // recupera identificador do usuario logado
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                firebase = ConfiguracaoFirebase.getFirebase();
                                firebase = firebase
                                        .child("Contatos")
                                        .child(identificadorUsuarioLogado)
                                        .child( identificadorContato );

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario( identificadorContato );
                                contato.setEmail( usuarioContato.getEmail() );
                                contato.setNome( usuarioContato.getNome() );

                                firebase.setValue( contato );

                            }else{
                                Toast.makeText(MainActivity.this, "Usuario não possu cadastro", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    public void deslogarUsuario(){
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}