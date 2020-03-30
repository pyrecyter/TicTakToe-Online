package lk.ticktactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lk.ticktactoe.Model.tictactoeRoom;

public class Matchmaking extends AppCompatActivity {

    DatabaseReference reference;
    DatabaseReference matchref;
    String key;
    Button cancel;
    TextView timmer;
    ValueEventListener listener;
    ValueEventListener listener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchmaking);

        cancel = findViewById(R.id.stop_btn);
        timmer = findViewById(R.id.time_txt);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CountUpTimer timer = new CountUpTimer(300000) {
            public void onTick(int seconds) {
                int minutes = seconds / 60;
                seconds     = seconds % 60;

                timmer.setText(String.format("%d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(Matchmaking.this,"No matches found !",Toast.LENGTH_LONG).show();
                finish();
            }
        };

        timer.start();

        reference = FirebaseDatabase.getInstance().getReference("Matchmaking");
        matchref = FirebaseDatabase.getInstance().getReference("TicTacToe");
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this,"User Auth Error",Toast.LENGTH_LONG).show();
            finish();
        }
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String roomid = snapshot.getKey();
                        if(roomid.matches(key)) continue;
                        tictactoeRoom room = new tictactoeRoom(5);
                        matchref.child(roomid).setValue(room);
                        Intent it = new Intent(Matchmaking.this,TicTacToe.class);
                        it.putExtra("user",key);
                        it.putExtra("roomid",roomid);
                        startActivity(it);
                        reference.child(roomid).removeValue();
                        finish();
                    }
                } else {
                    reference.child(key).setValue("searching...");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        matchref.addValueEventListener(listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(key)){
                        reference.removeEventListener(listener);
                        Intent it = new Intent(Matchmaking.this,TicTacToe.class);
                        it.putExtra("user",key);
                        it.putExtra("roomid",key);
                        startActivity(it);
                        finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        reference.removeEventListener(listener);
        matchref.removeEventListener(listener2);
        reference.child(key).removeValue();
        super.onDestroy();
    }
}
