package train;

import Jama.Matrix;
import filter.KalmanFilter;

//uses nonlinear Kalman Filter to take changes of direction into account
public class Computation4 {
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
    private double theta;
    
    
    public Computation4(Model model){
        this.model = model;
        Matrix procNoise = new Matrix(dim,dim);
        procNoise.set(dim-1,dim-1,0.05);
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
        
        if(count >= 1 && (Math.pow(state.get(0,0)-data.x,2) > 10*coRes.get(0,0) || 
                        Math.pow(state.get(1,0)-data.y,2) > 10*coRes.get(1,1))){           
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
        Matrix err = filter.getErr();
        dt = data.time - lastTime;
        a = state.get(5,0);
        theta = Math.atan2(sin, cos);
        cos = state.get(3,0);
        sin = state.get(4,0);
        v = state.get(2,0);
        if(count >= 1){
            filter.setTran(new Matrix(new double[][]{
                    new double[]{1, 0, 0.5*dt*Math.cos(theta), 0.5*dt*v, 0, -0.5*v*sin*dt*dt}, 
                    //x' = x + dt * v*cos - 0.5*a*v*sin*(dt)^2
                    new double[]{0, 1, 0.5*dt*Math.sin(theta), 0, 0.5*dt*v, 0.5*v*cos*dt*dt},  
                    //y' = y + dt*v*sin + 0.5*v*cos*a*(dt)^2
                    new double[]{0, 0, 1, 0, 0, 0},  
                    //v' = v
                    new double[]{0, 0, 0, 0, -1*a*dt, -0.*sin*dt},      
                    //cos' = cos(theta) - sin*a*dt 
                    new double[]{0, 0, 0, 1*a*dt, 0, 0.*cos*dt},
                    //sin' = sin(theta) + cos*a*dt                                 
                    new double[]{0, 0, 0, 0, 0, 0.9}}));            
                    //a' = 0.9  * a + noise this is to keep angular accelaration small                
                   
            Matrix procNoise = new Matrix(dim,dim);
            //set process noise due to linearization of non-linear model
            //all noise are estimation
            procNoise.set(0, 0, v*v*dt*dt);
            procNoise.set(1, 1, v*v*dt*dt);
            procNoise.set(0, 5, v*v*err.get(5,5)*dt*dt);
            procNoise.set(5, 0, v*v*err.get(5,5)*dt*dt);
            procNoise.set(1, 5, v*v*err.get(5,5)*dt*dt);
            procNoise.set(5, 1, v*v*err.get(5,5)*dt*dt);
            
            procNoise.set(3, 3, err.get(5,5)*dt*dt);
            procNoise.set(4, 4, err.get(5,5)*dt*dt);
           
            procNoise.set(3, 5, err.get(5,5)*dt*dt);
            procNoise.set(5, 3, err.get(5,5)*dt*dt);
            procNoise.set(4, 5, err.get(5,5)*dt*dt);
            procNoise.set(5, 4, err.get(5,5)*dt*dt);
            procNoise.set(dim-1,dim-1,0.2*0.2);
            filter.setProcNoise(procNoise);
        }
        
        System.out.println(String.format("%f %f, %f, %f, %f", a, v, theta, cos, sin));
        //update estimation and a
        state = filter.getState();
        
        model.addEstimate(new Data(state.get(0,0), state.get(1,0), data.time, true));
        model.addEstimateVel(new Data(v*cos, v*sin, data.time, true));
        a = state.get(4,0);
                
        //predict
        Matrix control = new Matrix(dim, 2);
        control.set(3,0,1);
        control.set(4,1,1);
       
        filter.setControl(control);
        
       
        filter.predict(new Matrix(new double[][]{new double[]{Math.cos(theta)}, new double[]{Math.sin(theta)}}));             
        model.addRawData(data);
        count++;
        lastTime = data.time;
        return;
    }
}
