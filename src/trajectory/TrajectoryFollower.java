package trajectory;

import trajectory.Segment.*;
import trajectory.TrajectoryException.*;

public class TrajectoryFollower{

    Segment[] trajectory;
    int step = -1;
    double kV, kA, kP, kI, kD, dt;
    double prevError = 0.0;
    double sumError = 0.0;
    public TrajectoryFollower(){

    }

    public void setTrajectory(Segment[] trajectory){
        this.trajectory = trajectory;
        this.step = 0;
    }
    
    public void setConstants(double kV, double kA, double kP, double kI, double kD, double dt){
        this.kV = kV;
        this.kA = kA;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.dt = dt;
    }

    public double getFeedForward(double velocity, double acceleration){
        return (kV*velocity)+(kA*acceleration);
    }

    public double getFeedBack(double setpointPosition, double actualPosition){
        double error = setpointPosition-actualPosition;
        this.sumError += error;
        double errorDt = (error-this.prevError)/this.dt;
        this.prevError = error;
        //Integral term may not be necessary
        return (this.kP*error)+(this.kI*this.sumError)+(this.kD*errorDt);
    }
    public double calcMotorOutput(double currPosition){
        try{
            if(step==-1){
                throw new TrajectoryException("No Trajectory added");
            }else{
                Segment s = this.trajectory[step];
                double velocity = s.getVelocity();
                double acceleration = s.getAcceleration();
                double position = s.getPosition();
                return getFeedForward(velocity, acceleration)+getFeedBack(position, currPosition);
            }
        }catch(TrajectoryException e){
            System.out.println(e.getMessage());
            return 0.0;
        }
    };
}
