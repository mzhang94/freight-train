package train;

public class Data {
    public final double x;
    public final double y;
    public final double time;
    public final boolean valid;
    
    public Data(double x, double y, double time, boolean valid){
        this.x = x;
        this.y = y;
        this.time = time;
        this.valid = valid;
    }
    
    public Data clone(){
        return new Data(x, y, time, valid);
    }
    
    public String toString(){
        return String.format("%s %s %s %b", time, x, y, valid);
    }
    
    //return average of these two data, with valid true
    public static Data average(Data a, Data b){
        return new Data(0.5*a.x+0.5*b.x, 0.5*a.y+0.5*b.y, 0.5*a.time+0.5*b.time, true);
    }
}
