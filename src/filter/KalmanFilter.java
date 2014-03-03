package filter;

import Jama.Matrix;

public class KalmanFilter {
    Matrix control;
    Matrix tran;
    Matrix tranT;
    
    Matrix procNoise;
    Matrix obsMat;
    Matrix obsMatT;
    Matrix obsNoise;
    
    Matrix errCo;   
    Matrix coRes;
    Matrix kalmanGain;
    Matrix state;
    Matrix mRes;
    Matrix input;
    int d; //dimention of state
    
    public KalmanFilter(Matrix tran, Matrix control, Matrix procNoise, Matrix obsMat, Matrix obsNoise,
            Matrix initState, Matrix initErrCovirance){
        this.tran = tran.copy();
        tranT = tran.transpose();
        
        this.control = control.copy();
        this.procNoise = procNoise.copy();
        this.obsMat = obsMat.copy();
        obsMatT = obsMat.transpose();
        this.obsNoise = obsNoise.copy();
        
        //initialize the other fields
        this.d = tran.getRowDimension();
        this.state = initState;
        this.errCo = initErrCovirance;
        this.input = new Matrix(d, 1);
    }
    
    public Matrix predict(Matrix input){
        this.input = input;       
        state = tran.times(state).plus(control.times(input));
        errCo = tran.times(errCo).times(tranT).plus(procNoise); 
        return state;
    }
    
    public Matrix getState(){
        return state;
    }
    
    public void update(Matrix obs){       
        mRes = obs.minus(obsMat.times(state));
        coRes = obsMat.times(errCo).times(obsMatT).plus(obsNoise);
        kalmanGain = errCo.times(obsMatT).times(coRes.inverse());
        state = state.plus(kalmanGain.times(mRes));
        errCo = Matrix.identity(d, d).minus(kalmanGain.times(obsMat)).times(errCo);
    }
    
    public void setTran(Matrix tran){
        this.tran = tran.copy();
        tranT = tran.transpose();
    }
    
    public void setControl(Matrix control){
        this.control = control.copy();
    }
    
    public void setNoise(Matrix procNoise, Matrix obsNoise){
        this.procNoise = procNoise.copy();       
        this.obsNoise = obsNoise.copy();
    }
}
    