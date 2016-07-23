package test;

import trajectory.TrajectoryGenerator;
import trajectory.Segment;
import trajectory.Serialize;
import java.io.*;

public class TestTrajectory {

    public static void main(String[] args) throws IOException {
        TrajectoryGenerator	testGenerator	= new TrajectoryGenerator(10.0, 15.0, 200.0);
        Segment[]               trajectory	= testGenerator.calcTrajectory(0.0, 0.0, 25.0);
        /*for(int i=0; i<trajectoryInfo.length; i++){
            System.out.println(trajectoryInfo[i]+"\n");
        }*/

        for (int i = 0; i < trajectory.length; i++){
            double	velocity     = trajectory[i].getVelocity();
            double	acceleration = trajectory[i].getAcceleration();
            double	position     = trajectory[i].getPosition();

            //System.out.println("Velocity: "+velocity+" --- Acceleration: "+acceleration+" --- Position: "+position+" \n");
        }

	Serialize.serializeArray(trajectory, "serialized.dat");
	Segment[] deserialized = Serialize.toArray("serialized.dat");

	if (assertSegmentsEqual(trajectory, deserialized))
	    System.out.println("Array serialization to file works as expected");
	else
	    System.out.println("Error in array serialization");
    }

    private static boolean assertSegmentsEqual(Segment[] expected, Segment[] actual) {
	if (expected.length != actual.length) return false;

	for (int i = 0; i < expected.length; i++) {
	    Segment	segExpected = expected[i];
	    Segment	segActual   = actual[i];

	    if (!segExpected.toString().equals(segActual.toString())) return false;
	}

	return true;
    }
}
