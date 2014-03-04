package train;

import Jama.Matrix;
import filter.KalmanFilter;

//uses nonlinear Kalman Filter to take changes of direction into account
public class Computation2 {
    private Model model;
    private KalmanFilter filter;
    private int count = 0;
    private double lastTime = 0;
    
    
    public Computation2(Model model){
        this.model = model;
        Matrix procNoise = new Matrix(5,5);
        procNoise.set(4,4,0.01);
//        Matrix init = new Matrix(5,1);
//        init.set(4,0,1);
        filter = new KalmanFilter(
                //initial values for process model does not matter, we will change it when we add data
                Matrix.identity(5,5), //transition 
                new Matrix(5,5), //control
                procNoise,//process noise
                Matrix.identity(2,5),//observation matrix obs(x) = x + noise, obs(y) = y + noise
                Matrix.identity(2,2).times(100), //observation noise 
                new Matrix(5,1),//initial estimate
                new Matrix(new double[][]{
                        new double[]{10000000,0,0,0,0},               
                        new double[]{0,10000000,0,0,0},
                        new double[]{0,0,1000,0,0},
                        new double[]{0,0,0, 1000,0},
                        new double[]{0,0,0,0, 100}})//initial estimate diviation
                );
    }
                      
     
    public void addData(Data data){
        
        Matrix state = filter.getState();      
        Matrix coRes = filter.getCoRes();
        
        
        if(count >= 1 && (Math.pow(state.get(0,0)-data.x,2) > 5*coRes.get(0,0) || 
                        Math.pow(state.get(1,0)-data.y,2) > 5*coRes.get(1,1))){           
            model.addEstimate(new Data(state.get(0,0), state.get(1,0), data.time, false));
            model.addEstimateVel(new Data(state.get(2,0), state.get(3,0), data.time, false));
            model.addRawData(new Data(data.x, data.y, data.time, false));
            count++;
            lastTime = data.time;
            return;
        }
        
        //update
        filter.update(new Matrix(new double[][]{new double[]{data.x}, new double[]{data.y}}));       
        
        //update transitional model
        state = filter.getState();
        double dt = data.time - lastTime;
        double a = state.get(4,0);
        double vx = state.get(2,0);
        double vy = state.get(3,0);
        if(count >= 1){
            filter.setTran(new Matrix(new double[][]{
                    new double[]{1, 0, dt, -0.*a*dt*dt, -0.5*vy*dt*dt}, //x' = x + dt * vx - 0.5*a*vy*(dt)^2
                    new double[]{0, 1, 0.*a*dt*dt, dt, 0.5*vx*dt*dt},  //y' = y + dt*vy + 0.5*vx*a*(dt)^2
                    new double[]{0, 0, 1, -0.5*a*dt, -0.5*a*vy},  //vx' = vx - a*dt*vy
                    new double[]{0, 0, 0.5*a*dt, 1, 0.5*a*vx},      //vy' = vy + a*dt*vx   
                    new double[]{0, 0, 0, 0, 1}}));            //a' = a     
           
        }
        System.out.println(String.format("%f %f, %f, %f", a, vx, vy, vx*vx+vy*vy));
        //update estimation and a
        state = filter.getState();
        
        model.addEstimate(new Data(state.get(0,0), state.get(1,0), data.time, true));
        model.addEstimateVel(new Data(state.get(2,0), state.get(3,0), data.time, true));
        a = state.get(4,0);
                
        //predict
        filter.predict(new Matrix(5,1));     
        
        model.addRawData(data);
        count++;
        lastTime = data.time;
        return;
    }
}
