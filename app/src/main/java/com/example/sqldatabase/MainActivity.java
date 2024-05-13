package com.example.sqldatabase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<contactModal> data = new ArrayList<>();

    Button btn_new;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MyDbHandler dbHandler = new MyDbHandler(this);
        recyclerView = findViewById(R.id.id_recView);
        btn_new = findViewById(R.id.id_btn_new);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        data = dbHandler.fetchData();
        ContactAdapter adapter = new ContactAdapter(MainActivity.this,data);

        recyclerView.setAdapter(adapter);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.new_contact_dialog);

                dialog.show();

                TextView btn_add = dialog.findViewById(R.id.id_tv_btnAdd);
                EditText tv_newName = dialog.findViewById(R.id.id_tv_newName);
                EditText tv_newPhone = dialog.findViewById(R.id.id_tv_newPhone);

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {
                        contactModal modal = new contactModal(tv_newName.getText().toString(),tv_newPhone.getText().toString());
                        data.add(modal);
                        dialog.dismiss();
                        dbHandler.addContact(modal);
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(data.size()-1);

                    }
                });


            }
        });






    }
}