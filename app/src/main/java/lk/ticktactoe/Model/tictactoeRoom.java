package lk.ticktactoe.Model;

import java.util.ArrayList;
import java.util.List;

public class tictactoeRoom {

    String password;
    String roomName;
    int player1Score;
    int player2Score;
    int totScore;
    boolean p1chance;
    boolean p2chance;
    boolean ready;
    List <tick> t;

    public tictactoeRoom(){}

    public tictactoeRoom(String roomName,String password, int totScore){
        this.roomName = roomName;
        this.password = password;
        this.totScore = totScore;
        player1Score = 0;
        player2Score = 0;
        p1chance = true;
        p2chance = false;
        ready = false;
        t = new ArrayList<>(9);
        for (int i=0; i<9;i++){
            tick nt = new tick();
            nt.val = "";
            nt.id = false;
            nt.i = i/3;
            nt.j = i%3;
            t.add(nt);
        }
    }
    public tictactoeRoom(int totScore){
        this.roomName = "matchmaking";
        this.password = "password";
        this.totScore = totScore;
        ready = true;
        player1Score = 0;
        player2Score = 0;
        p1chance = true;
        p2chance = false;
        t = new ArrayList<>(9);
        for (int i=0; i<9;i++){
            tick nt = new tick();
            nt.val = "";
            nt.id = false;
            nt.i = i/3;
            nt.j = i%3;
            t.add(nt);
        }
    }

    public void setTiles(int tile,String val){
        switch (tile){
            case 1:
                t.get(0).id = true;
                t.get(0).val = val;
                break;
            case 2:
                t.get(1).id = true;
                t.get(1).val = val;
                break;
            case 3:
                t.get(2).id = true;
                t.get(2).val = val;
                break;
            case 4:
                t.get(3).id = true;
                t.get(3).val = val;
                break;
            case 5:
                t.get(4).id = true;
                t.get(4).val = val;
                break;
            case 6:
                t.get(5).id = true;
                t.get(5).val = val;
                break;
            case 7:
                t.get(6).id = true;
                t.get(6).val = val;
                break;
            case 8:
                t.get(7).id = true;
                t.get(7).val = val;
                break;
            case 9:
                t.get(8).id = true;
                t.get(8).val = val;
                break;
                default : return;
        }
        switchChance();
    }

    public void joinRoom(){
        ready = true;
    }

    public void ResetBoad(){
        List<tick> ticks = new ArrayList<>(9);
        for (int i=0; i<9;i++){
            tick ts = new tick();
            ts.i = t.get(i).i;
            ts.j = t.get(i).j;
            ts.id = false;
            ts.val = "";
            ticks.add(ts);
        }
        t = ticks;
    }
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void switchChance(){
        p1chance = !p1chance;
        p2chance = !p2chance;
    }

    public void p1Scored(){
        player1Score++;
    }

    public void p2Scored(){
        player2Score++;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int plater1Score) {
        this.player1Score = plater1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public int getTotScore() {
        return totScore;
    }

    public void setTotScore(int totScore) {
        this.totScore = totScore;
    }

    public boolean isP1chance() {
        return p1chance;
    }

    public void setP1chance(boolean p1chance) {
        this.p1chance = p1chance;
    }

    public boolean isP2chance() {
        return p2chance;
    }

    public void setP2chance(boolean p2chance) {
        this.p2chance = p2chance;
    }

    public List<tick> getT() {
        return t;
    }

    public void setT(List<tick> t) {
        this.t = t;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}

