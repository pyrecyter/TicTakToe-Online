package lk.ticktactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lk.ticktactoe.Model.tictactoeRoom;

public class CreateRoom extends AppCompatActivity {

    EditText roomname,password,total;
    Button create,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        create = findViewById(R.id.createroom_btn);
        cancel = findViewById(R.id.cancel_btn);
        roomname = findViewById(R.id.roomName);
        password = findViewById(R.id.roomPassword);
        total = findViewById(R.id.totalScore);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String roomName = roomname.getText().toString();
                    String Password = password.getText().toString();
                    int Total = Integer.parseInt(total.getText().toString());
                    tictactoeRoom room = new tictactoeRoom(roomName,Password,Total);
                    DatabaseReference ref =FirebaseDatabase.getInstance().getReference("TicTacToe");
                    String key = ref.push().getKey();
                    ref.child(key).setValue(room);
                    Intent it = new Intent(CreateRoom.this,TicTacToe.class);
                    it.putExtra("user",key);
                    it.putExtra("roomid",key);
                    startActivity(it);
                    finish();
                }
            }
        });
    }

    boolean validate(){
        if(roomname.getText().toString().isEmpty() || password.getText().toString().isEmpty() || total.getText().toString().isEmpty()){
            Toast.makeText(this,"Please fill all !",Toast.LENGTH_LONG).show();
            return  false;
        }else{
            return  true;
        }
    }
}
