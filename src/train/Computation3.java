package train;

import Jama.Matrix;
import filter.KalmanFilter;

//uses nonlinear Kalman Filter to take changes of direction into account
public class Computation3 implements Computation{
    private Model model;
    private KalmanFilter filter;
    private int count = 0;
    private double lastTime = 0;
    
    private double dt;
    private double a;
    private double v;
    private double cos;
    private double sin;
    
    private int dim = 6;
    
    
    public Computation3(Model model){
        this.model = model;
        Matrix procNoise = new Matrix(dim,dim);
        procNoise.set(5,5,0.1);
        Matrix init = new Matrix(dim,1);
        init.set(3,0,1);
        
        filter = new KalmanFilter(
                //initial values for process model does not matter, we will change it when we add data
                Matrix.identity(dim,dim), //transition 
                new Matrix(dim,dim), //control
                procNoise,//process noise
                Matrix.identity(2,dim),//observation matrix obs(x) = x + noise, obs(y) = y + noise
                Matrix.identity(2,2).times(100), //observation noise 
                init,//initial estimate
                new Matrix(new double[][]{
                        new double[]{10000000,0,0,0,0,0},               
                        new double[]{0,10000000,0,0,0,0},
                        new double[]{0,0,1000,0,0,0},
                        new double[]{0,0,0,1,0,0},
                        new double[]{0,0,0,0,1,0},
                        new double[]{0,0,0,0,0,1}})//initial estimate diviation
                );
    }
                      
     
    public void addData(Data data){
        
        Matrix state = filter.getState();      
        Matrix coRes = filter.getCoRes();
        
        
        if(count >= 1 && (Math.pow(state.get(0,0)-data.x,2) > 4*coRes.get(0,0) || 
                        Math.pow(state.get(1,0)-data.y,2) > 4*coRes.get(1,1))){           
            model.addEstimate(new Data(state.get(0,0), state.get(1,0), data.time, false));
            model.addEstimateVel(new Data(v*cos, v*sin, data.time, true));
            model.addRawData(new Data(data.x, data.y, data.time, false));
            count++;
            lastTime = data.time;
            return;
        }
        
        //update
        filter.update(new Matrix(new double[][]{new double[]{data.x}, new double[]{data.y}}));       
        
        //update transitional model
        state = filter.getState();
        dt = data.time - lastTime;
        a = state.get(5,0);
        cos = state.get(3,0);
        sin = state.get(4,0);
        v = state.get(2,0);
        if(count >= 1){
            filter.setTran(new Matrix(new double[][]{
                    new double[]{1, 0, 0.5*dt*cos, 0.*v*dt, -0.5*v*a*dt*dt, -0.5*v*sin*dt*dt}, 
                    //x' = x + dt * v*cos - 0.5*a*v*sin*(dt)^2
                    new double[]{0, 1, .5*dt*sin, 0.*v*a*dt*dt, 0.5*v*dt, 0.5*v*cos*dt*dt},  
                    //y' = y + dt*v*sin + 0.5*v*cos*a*(dt)^2
                    new double[]{0, 0, 1, 0, 0, 0},  
                    //v' = v
                    new double[]{0, 0, 0, 1-a*dt*dt, 0, -1*sin*dt},      
                    //cos' = cos - sin*a*dt 
                    new double[]{0, 0, 0, 0, 1-a*dt*dt, 1*cos*dt},
                    //sin' = sin + cos*a*dt
                    new double[]{0, 0, 0, 0, 0, 1}}));            
                    //a' = a                
        }
        
        System.out.println(String.format("%f %f, %f, %f", a, v, sin, cos));
        //update estimation and a
        state = filter.getState();
        
        model.addEstimate(new Data(state.get(0,0), state.get(1,0), data.time, true));
        model.addEstimateVel(new Data(v*cos, v*sin, data.time, true));
        a = state.get(4,0);
                
        //predict
        filter.predict(new Matrix(dim,1));     
        
        model.addRawData(data);
        count++;
        lastTime = data.time;
        return;
    }
}
