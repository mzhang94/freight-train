package filter;

import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class MyProcessModel implements ProcessModel{

    @Override
    public RealMatrix getControlMatrix() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RealMatrix getInitialErrorCovariance() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RealVector getInitialStateEstimate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RealMatrix getProcessNoise() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RealMatrix getStateTransitionMatrix() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
