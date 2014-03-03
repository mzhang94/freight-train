package train;

public class Data {
    public double x;
    public double y;
    public double time;
    
    public Data(double x, double y, double time){
        this.x = x;
        this.y = y;
        this.time = time;
    }
    
    public Data clone(){
        return new Data(x, y, time);
    }
    
    public String toString(){
        return String.format("%s %s %s", time, x, y);
    }
}
