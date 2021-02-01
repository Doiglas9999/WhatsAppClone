package com.example.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static Boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes ){

        if (Build.VERSION.SDK_INT >= 23){

            List<String> listaPermissoes = new ArrayList<String>();

            /*percorre as permissoes necessarias,
            verificando uma a uma se ja tem permissao liberada */
            for (String permissao : permissoes){
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if ( !validaPermissao ) listaPermissoes.add(permissao);
            }

            // caso a lista esteja vazia, não é necessario solicitar permissao
            if ( listaPermissoes.isEmpty() ) return true;

            //converte lista em array
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicita permissao
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode );

        }

        return true;

    }

}
