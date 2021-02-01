package com.example.whatsapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.whatsapp.fragment.ContatosFragment;
import com.example.whatsapp.fragment.ConversasFragment;
import com.example.whatsapp.fragment.StatusFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {super(fragmentActivity);}

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch ( position ){
            case 0:
                return new ConversasFragment();
            case 1:
                return new StatusFragment();
            default:
                return new ContatosFragment();
        }
    }


    @Override
    public int getItemCount(){return 3;}



}
