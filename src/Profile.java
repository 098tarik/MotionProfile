import java.util.ArrayList;

// @author Tarik Brown
public class Profile {

	private double distance;
	private double max_velocity;
	private double acceleration;
	
	private double t0, t1, t2, t3;
	private double d1, d2, d3;

	public Profile(double v_max, double accel, double dist) {
		
		distance = dist;
		max_velocity = v_max;
		acceleration = accel;
		
		t0 = 0.0;
		double distToMaxVel = 2 * (((Math.abs(max_velocity)) / 2.0) * ((Math.abs(max_velocity)) / accel));
		
		if (distance < distToMaxVel) {
			// if triangle
			d1 = distance / 2.0;
			d3 = d1;
			double d1finVel = Math.pow((2 * accel * d1), 0.5);
			t1 = d1finVel / accel;
			t2 = t1;
			d2 = 0;
			t3 = 2.0 * t1;
		} else {
			// if trapezoid
			t1 = (Math.abs(max_velocity)) / accel;
			d1 = ((Math.abs(max_velocity)) / 2.0) * t1;
			d3 = d1;
			d2 = Math.abs(distance) - d1 - d3;
			t2 = Math.abs(d2 / max_velocity) + t1;
			t3 = t2 + t1;
		}
		
	}

	public double getFinalTime() {
		return t3;
	}

	public double getVelocityAtTime(double time) {
		double v_exp = 0;
		
		if (time >= t1 && time <= t2) {
			v_exp = Math.abs(max_velocity);
		} else if (time < t1 && time >= t0) {
			v_exp = acceleration * time;
		} else if (time > t2 && time <= t3) {
			v_exp = Math.abs(max_velocity) - acceleration * (time - t2);
		} else if (time > t3) {
			v_exp = 0;
		}
		
		if (distance >= 0.0) {
			return v_exp;
		} else if (distance < 0.0) {
			return -v_exp;
		} else {
			return 0.0;
		}
	}

	public double getDistAtTime(double time) {
		double dist = 0;
		if (time <= t1 && time >= t0) {
			dist = (0.5 * getVelocityAtTime(time) * time);
		} else if (time > t1 && time <= t2) {
			dist = d1 + (max_velocity * (time - t1));
		} else if (time > t2 && time <= t3) {
			dist = d1 + (max_velocity * (t2 - t1))
					+ ((Math.pow(getVelocityAtTime(time), 2.0) - Math.pow(max_velocity, 2.0)) / (2.0 * -acceleration));
		} else {
			dist = distance;
		}
		if (distance > 0.0) {
			return dist;
		} else if (distance < 0.0) {
			return -dist;
		} else {
			return 0.0;
		}
	}

	public double[] getVelocities() {

		int index = 0;
		int updates = ((int) (t3 * 50)) + 2;
		double[] velocities = new double[updates];
		for (double time = 0.0; time < t3; time += .02) {
			double vel_ftpersec = getVelocityAtTime(time);
			double vel_ticksper100ms = vel_ftpersec * (12.0 / (6.0 * Math.PI)) * 8.45 * 80.0 / 10.0;
			velocities[index] = vel_ticksper100ms;
			index++;
		}
		return velocities;
	}

	// per 100 msec for updates
	public ArrayList<Double> getDistances() {
		ArrayList<Double> distArr = new ArrayList<>();
		for (double i = 0.0; i < getFinalTime(); i += 0.05) {
			distArr.add(new Double(getDistAtTime(i)));
		}
		return distArr;
	}

	public double getDist() {
		return distance;
	}

	public double getMaxVel() {
		return max_velocity;
	}

	public double getAccel() {
		return acceleration;
	}

}
