package lk.ticktactoe.Model;

public class tick {
    String val;
    boolean id;
    int i,j;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public tick(){}

    public tick(String s,boolean v,int ii,int jj){
        val = s;
        id = v;
        i=ii;
        j=jj;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
