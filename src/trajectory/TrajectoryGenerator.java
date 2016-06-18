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
    private double calcAdjustedMaxV(double initialV, double endingV, double totalDistance){
        double startDiscount = (0.5*initialV*initialV)/this.maxA;
        double endDiscount = (0.5*endingV*endingV)/this.maxA;
        double adjustedMaxV = Math.sqrt((this.maxA*totalDistance)-startDiscount-endDiscount);

        return Math.min(adjustedMaxV, this.maxV);
    }


    public Segment[] calcTrajectory(double initialV, double endingV, double totalDistance){
        double adjustedMaxV = calcAdjustedMaxV(initialV, endingV,  totalDistance);
        double rampUpTime = (adjustedMaxV - initialV)/this.maxA;
        double rampUpDistance = initialV*rampUpTime + 0.5*this.maxA*rampUpTime*rampUpTime;
        double rampDownTime = (adjustedMaxV - endingV)/this.maxA;
        double rampDownDistance = adjustedMaxV*rampDownTime-0.5*this.maxA*rampDownTime*rampDownTime;
        double coastDistance = totalDistance-(rampUpDistance+rampUpDistance);
        double coastTime = coastDistance/adjustedMaxV;
        double totalTime = rampUpTime+coastTime+rampDownTime;
        int totalIterations = (int)(totalTime/(1/this.controlLoop));
        //double[] trajectoryInfo = {adjustedMaxV, rampUpTime, rampUpDistance, rampDownTime, rampDownDistance, coastDistance, coastTime, totalTime};
        System.out.println(adjustedMaxV);
        Segment[] trajectory = new Segment[totalIterations];
        double time=0.0;
        for(int i=0; i<totalIterations; i++){
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
                velocity = adjustedMaxV-(this.maxA*(time-(rampUpTime+coastTime)));
                //System.out.println(this.maxA*time);
                acceleration = -this.maxA;
                position = (coastDistance+rampUpDistance)+this.maxA*0.5*time*time;
            }
            time += (1/controlLoop);
            Segment s = new Segment(velocity, acceleration, position);
            trajectory[i] = s;
        }
        return trajectory;
    }
}
