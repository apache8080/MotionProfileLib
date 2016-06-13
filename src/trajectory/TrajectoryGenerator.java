package trajectory;

import trajectory.Segment.*;

public class TrajectoryGenerator{

    double maxV;
    double maxA;
    double controlLoop;

    public TrajectoryGenerator(double maxV, double maxA, double controlLoop){
        this.maxV = maxV;
        this.maxA = maxA;
        this.controlLoop = controlLoop;
    }

    /**
     *
     * Using the third kinematic equation we calculate the max velocity the robt can travel based 
     * off of the desired distance we want to travel. Then we compare this calculated velocity 
     * to the max velocity that the robot can actually do and returns the minimum value.
     *
     * @param   initialV    initial velocity of the robot
     * @param   totalDistance desired distance of travel for the robot
     * @return      the minimum between adjustedMaxV and maxV
     *
     */
    private double calcAdjustedMaxV(double initialV, double totalDistance){ 
        double adjustedMaxV = Math.sqrt((initialV*initialV)+(2*this.maxA*totalDistance));

        return Math.min(adjustedMaxV, this.maxV);
    }


    public Segment[] calcTrajectory(double initialV, double endingV, double totalDistance){
        double adjustedMaxV = calcAdjustedMaxV(initialV, totalDistance);
        double rampUpTime = (adjustedMaxV - initialV)/this.maxA;
        double rampUpDistance = adjustedMaxV*0.5*rampUpTime;
        double rampDownTime = (adjustedMaxV - endingV)/this.maxA;
        double rampDownDistance = adjustedMaxV*0.5*rampDownTime;
        double coastDistance = totalDistance-(rampUpDistance+rampUpDistance);
        double coastTime = coastDistance/adjustedMaxV;
        double totalTime = rampUpTime+coastTime+rampDownTime;
        int totalIterations = (int)(totalTime/(1/this.controlLoop));
        
        Segment[] trajectory = new Segment[totalIterations];
        double time=0.0;
        for(int i=0; i<=totalIterations; i++){
            double velocity, acceleration, position;
            if(time<=rampUpTime){
                velocity = initialV+(this.maxA*time);
                acceleration = this.maxA;
                position = acceleration*0.5*time*time;
            }else if(time>rampUpTime && time<=(coastTime+rampUpTime)){
                velocity = adjustedMaxV;
                acceleration = 0;
                position = velocity*time;
            }else{
                velocity = adjustedMaxV-(this.maxA*time);
                acceleration = -this.maxA;
                position = acceleration*0.5*time*time;
            }
            time += (1/controlLoop);
            Segment s = new Segment(velocity, acceleration, position);
            trajectory[i] = s;
        }
        return trajectory;
    }
}
