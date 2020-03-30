package lk.ticktactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinRoom extends AppCompatActivity {

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);

        layout = findViewById(R.id.layout);
        ProgressBar progressBar = new ProgressBar(JoinRoom.this);
        layout.addView(progressBar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TicTacToe");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                layout.removeAllViews();
                layout.setPadding(10,30,10,30);
                for (final DataSnapshot ds : dataSnapshot.getChildren()){
                    if(!ds.child("ready").getValue(Boolean.class)){
                        Button button = new Button(JoinRoom.this);
                        button.setText(ds.child("roomName").getValue(String.class));
                        button.setBackgroundColor(Color.CYAN);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinearLayout linearLayout = new LinearLayout(JoinRoom.this);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                TextView textView2 = new TextView(JoinRoom.this);
                                textView2.setText("Enter Password");
                                final EditText password = new EditText(JoinRoom.this);
                                linearLayout.addView(textView2);
                                linearLayout.addView(password);
                                AlertDialog alertDialog = new AlertDialog.Builder(JoinRoom.this).create();
                                alertDialog.setTitle(ds.child("roomName").getValue(String.class));
                                alertDialog.setMessage("Enter Details");
                                alertDialog.setView(linearLayout);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String pass = password.getText().toString();
                                                if(pass.isEmpty()) {
                                                    Toast.makeText(JoinRoom.this,"Fill all",Toast.LENGTH_LONG).show();
                                                }
                                                if(pass.matches(ds.child("password").getValue(String.class))){
                                                    Intent it = new Intent(JoinRoom.this,TicTacToe.class);
                                                    it.putExtra("user","notplayer1");
                                                    it.putExtra("roomid",ds.getKey());
                                                    startActivity(it);
                                                }else
                                                Toast.makeText(JoinRoom.this,"Wrong password",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                        layout.addView(button);
                        layout.addView(new TextView(JoinRoom.this));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
