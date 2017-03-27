package constellation;

public class Constellation {
	private Satellite[] satellites;
	
	private static final double[] semiMajorAxis = { 7800000.0, 2800000.0, 3300000.0,
	2800000.0, 2800000.0, 3300000.0, 2800000.0, 5000000, 5000000,
	5000000, 5000000, 5000000, 5000000, 5000000, 5000000, 5000000,
	5000000, 5000000, 5000000, 5000000, 5000000 };
	
	private static final double[] eccentricity = { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	private static final double[] inclination = { 0.5, 1.4, 0.8, 1.9, 1.4, 0.8, 1.9,
	1.771509191, 1.771509191, 1.804775166, 1.804775166, 1.812925854,
	1.812925854, 1.789695522, 1.789695522, 1.752851621, 1.752851621,
	1.730089037, 1.730089037, 1.738353171, 1.738353171 };
	
	private static final double[] argOfPerigree = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	private static final double[] RAAN = { 0, 3.5, 5.5, 0.2, 3.5, 5.5, 0.2, 0, 0,
	0.900396162, 0.900396162, 1.80601435, 1.80601435, 2.709623664,
	2.709623664, 3.606426193, 3.606426193, 4.498324348, 4.498324348,
	5.389297477, 5.389297477 };
	
	private static final double[] trueAnomaly = { 0, 5.5, 6.2, 4.0, 4.7, 5.4, 3.1, 0,
	3.141592654, 1.362641379, 4.504241014, 2.746031232, 5.887623886,
	4.121821921, 0.980234504, 5.467802387, 2.326209734, 0.501590174,
	3.643182827, 1.81158195, 4.953174604 };
	
	public Constellation(int num) {
		if (num > 21) {
			num = 21;
		} else if (num < 0) {
			num = 0;
		}
		satellites = new Satellite[num];
		for (int i = 0; i < num && i < 21; i++) {
			satellites[i] = new Satellite(semiMajorAxis[i], eccentricity[i], inclination[i], argOfPerigree[i], RAAN[i], trueAnomaly[i]);
		}
	}
	
	public void Propagate(double timeDiff) {
		for (int i = 0; i < satellites.length; i++) {
			satellites[i].Propagate(timeDiff);
			System.out.println(String.format("Time: %1$d", timeDiff));
			System.out.println(String.format("Satellite: %d radius: %2$fm true anomaly: %3$f�", i, satellites[i].getRadius(), satellites[i].getTrueAnomaly()));
		}
	}
	
	public void PrintDebug(double time) {
		for (int i = 0; i < satellites.length; i++) {
			System.out.println(String.format("Satellite: %d radius: %2$fm true anomaly: %3$f�", i, satellites[i].getRadius(), satellites[i].getTrueAnomaly()));
		}
	}
}