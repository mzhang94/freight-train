package train;

import java.util.ArrayList;

public class Computation {
    Double[][] data;
    Double[][] velocity;
    public Computation(Double[][] data){
        this.data = data;
        this.velocity = Parser.getVelocity(data);
    }
   
    public ArrayList<Double> calculateTurnTime(){
        return null;
    }
    
}
