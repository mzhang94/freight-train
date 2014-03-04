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
}
