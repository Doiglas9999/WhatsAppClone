package com.example.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Preferencias;
import com.example.whatsapp.model.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(@NonNull Context c, @NonNull ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //verifica sa a lista esta preenchida
        if (mensagens != null){

            //recupera dados do usuario remetente
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            //inicializa objeto para montagem do layoult
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // recupera a mensagem
            Mensagem mensagem = mensagens.get( position );

            //monta layoult apartir do xml

            if(idUsuarioRemetente.equals( mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem__direita, parent,false);
            } else{
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent,false);
            }



            //recupera elemento para a exibicao
            TextView textoMensagem = (TextView) view.findViewById(R.id.text_mensagem);
            textoMensagem.setText( mensagem.getMensagem() );

        }


        return view;
    }
}
