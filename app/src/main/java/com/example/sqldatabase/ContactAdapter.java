package com.example.sqldatabase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    Context context;
    ArrayList<contactModal> data;

    int lastIndex = -1;

    public ContactAdapter(Context context, ArrayList<contactModal> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        contactModal modal = data.get(position);
        holder.tv_name.setText(modal.getName());
        holder.tv_phone.setText(modal.getPhone());

        //Deleting Logic

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setMessage("You Sure ????")
                        .setTitle("Delete Contact")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyDbHandler dbHandler = new MyDbHandler(context);
                                dbHandler.deleteContact(modal);

                                data.remove(position);
                                notifyDataSetChanged();


                            }
                        })
                        .setNegativeButton("No",null);
                    builder.create().show();



                return false;
            }
        });

        //Editing Logic

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactModal Old = data.get(position);

                Dialog editDialog = new Dialog(context);
                editDialog.setContentView(R.layout.new_contact_dialog);
                TextView tv_Update = editDialog.findViewById(R.id.id_tv_btnAdd);
                TextView tv_newName = editDialog.findViewById(R.id.id_tv_newName);
                TextView tv_newPhone = editDialog.findViewById(R.id.id_tv_newPhone);

                tv_newPhone.setText(Old.getPhone());
                tv_newName.setText(Old.getName());

                tv_Update.setText("Update");
                MyDbHandler dbHandler = new MyDbHandler(context);

                tv_Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactModal New = new contactModal();
                        New.setPhone(tv_newPhone.getText().toString());
                        New.setName(tv_newName.getText().toString());
                        New.setId(Old.getId());

                        dbHandler.updateContact(Old,New);
                        data.remove(position);
                        data.add(position,New);

                        notifyDataSetChanged();
                        editDialog.dismiss();


                    }
                });

                editDialog.show();


            }
        });

        Animation animation ;
        if (lastIndex<position)

            if (position%2==0) {
                animation = AnimationUtils.loadAnimation(context, R.anim.slid_in);
                holder.cardView.setAnimation(animation);

            }
            else {
                animation =  AnimationUtils.loadAnimation(context, R.anim.slide_right);
                holder.cardView.setAnimation(animation);
            }

        lastIndex = position;





    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_phone;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.id_tv_cv_name);
            tv_phone = itemView.findViewById(R.id.id_tv_cv_phone);
            cardView = itemView.findViewById(R.id.id_cv_cardView);


        }
    }
}
