package lk.ticktactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import lk.ticktactoe.Model.tick;
import lk.ticktactoe.Model.tictactoeRoom;

public class TicTacToe extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn;


    private int player1Points;
    private int player2Points;
    private String roomid;
    private String user;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private String ticType="";

    private  ValueEventListener listener;

    private tictactoeRoom room;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe);
        Intent it = getIntent();
        roomid = it.getStringExtra("roomid");
        user = it.getStringExtra("user");
        reference = FirebaseDatabase.getInstance().getReference("TicTacToe").child(roomid);
        reference.addValueEventListener(listener =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                room = dataSnapshot.getValue(tictactoeRoom.class);

                if(room.getPlayer1Score() == room.getTotScore()){
                    reference.removeEventListener(listener);
                    reference.removeValue();
                    new AlertDialog.Builder(TicTacToe.this)
                            .setTitle("Game Over")
                            .setMessage("Player 1 won the game !")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent it = new Intent(TicTacToe.this,HomePage.class);
                                    startActivity(it);
                                    finish();

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                if(room.getPlayer2Score() == room.getTotScore()){
                    reference.removeEventListener(listener);
                    reference.removeValue();
                    new AlertDialog.Builder(TicTacToe.this)
                            .setTitle("Game Over")
                            .setMessage("Player 2 won the game !")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent it = new Intent(TicTacToe.this,HomePage.class);
                                    startActivity(it);
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                player1Points = room.getPlayer1Score();
                player2Points = room.getPlayer2Score();
                updatePointsText();
                setBtnText();
                if(!roomid.matches(user)){
                    player1Turn = room.isP2chance();
                    ticType = "O";
                    room.joinRoom();
                }else{
                    ticType = "X";
                    player1Turn = room.isP1chance();
                }
                if(!room.isReady()) {
                    Toast.makeText(TicTacToe.this,"Waiting for player 2 !",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText(ticType);

            switch (v.getId()){
                case R.id.button_00: room.setTiles(1,ticType);
                    break;
                case R.id.button_01: room.setTiles(2,ticType);
                    break;
                case R.id.button_02: room.setTiles(3,ticType);
                    break;
                case R.id.button_10: room.setTiles(4,ticType);
                    break;
                case R.id.button_11: room.setTiles(5,ticType);
                    break;
                case R.id.button_12: room.setTiles(6,ticType);
                    break;
                case R.id.button_20: room.setTiles(7,ticType);
                    break;
                case R.id.button_21: room.setTiles(8,ticType);
                    break;
                case R.id.button_22: room.setTiles(9,ticType);
                    break;
            }

            if (checkForWin()) {
                if (roomid.matches(user)) {
                    player1Wins();
                } else {
                    player2Wins();
                }
            }else if(checkdraw()){
                draw();
            }
        }

        updateDB();
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private boolean checkdraw(){
       List<tick> ticks =  room.getT();
       int i =0;
       for(tick t : ticks){
           if(t.isId())
               i++;
       }
       return i>=9;
    }

    private void player1Wins() {
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
        room.p1Scored();
    }

    private void player2Wins() {
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
        room.p2Scored();
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        if(roomid.matches(user)) {
            textViewPlayer1.setText("You : " + player1Points);
            textViewPlayer2.setText("Player 2: " + player2Points);
        }else{
            textViewPlayer1.setText("Player 1: " + player1Points);
            textViewPlayer2.setText("You : " + player2Points);
        }
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");

            }
        }
        room.ResetBoad();
        updateDB();
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    private void updateDB(){
        reference.setValue(room);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    private void setBtnText(){
        List<tick> list = room.getT();
        for (tick t : list){
                int i = t.getI();
                int j = t.getJ();
                buttons[i][j].setText(t.getVal());
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        reference.removeEventListener(listener);
        reference.removeValue();
        super.onDestroy();
    }
}
