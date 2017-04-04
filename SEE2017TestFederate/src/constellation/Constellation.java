package constellation;

import java.util.ArrayList;

import AStar.AStar;
import federate.TestFederate;

public class Constellation {
	private ArrayList<Satellite> satellites;
	private ArrayList<Entity> entities;
	private AStar pathfinder;
	private int IDs;
	
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
	
	private TestFederate federate;
	
	public Constellation(TestFederate federate, int num) {
		IDs = 0;
		this.federate = federate;
		if (num > 21) {
			num = 21;
		} else if (num < 0) {
			num = 0;
		}
		satellites = new ArrayList<Satellite>();
		entities = new ArrayList<Entity>();
		String name;
		for (int i = 0; i < num && i < 21; i++) {
			name = "mwsu_satellite_" + 0;
			satellites.add(new Satellite(federate, semiMajorAxis[i], eccentricity[i], inclination[i], argOfPerigree[i], RAAN[i], trueAnomaly[i], name, IDs));
			satellites.get(i).setHlaAttributes();
			federate.reserveObjectInstanceName(name);
			System.out.println("after reserve");
			satellites.get(i).registerObjectInstance();
			entities.add(satellites.get(i));
			IDs++;
		}
		
		pathfinder = new AStar();
	}
	
	public void Propagate(double timeDiff) {
		for (int i = 0; i < satellites.size(); i++) {
			satellites.get(i).Propagate(timeDiff);
			//System.out.println(String.format("Time: %1$f", timeDiff));
			//System.out.println(String.format("Satellite: %d radius: %2$fm true anomaly: %3$f", i, satellites.get(i).getRadius(), satellites.get(i).getTrueAnomaly()));
			satellites.get(i).postAttributes();
		}
//		Vector3 loc = satellites.get(0).getLocation();
//		System.out.println(loc.getX() + " " + loc.getY() + " " + loc.getZ());
		System.out.println("tick");
		int sender = pathFind("tower1", "tower2");
		System.out.println("sent from " + sender);
	}
	
	public void addEntity(String name, double x, double y, double z) {
		entities.add(new Entity(null, name, x, y, z, IDs));
		IDs++;
	}
	
	public int getEntityID(String name) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).name == name) {
				return entities.get(i).getID();
			}
		}
		return -1;
	}
	
	public int pathFind(String from, String to) {
		int fromID = getEntityID(from);
		int toID = getEntityID(to);
		//if either are -1 then that entity doesn't exist
		if (fromID == -1) {
			System.out.println("no such entity " + from);
			return -1;
		}
		if (toID == -1) {
			System.out.println("no such entity " + to);
			return -1;
		}
		//0, 0, -1.738E6
		Entity sender = entities.get(fromID);
		Entity receiver = entities.get(toID);
		int start = -1;
		int end = -1;
		
		//find satellite closest to 'from' entity
		double dist = Double.MAX_VALUE;
		for (int i = 0; i < satellites.size(); i++) {
			if (distance(sender, satellites.get(i)) < dist) {
				start = i;
				dist = distance(sender, satellites.get(i));
			}
		}

		//find satellite closest to 'to' entity
		dist = Double.MAX_VALUE;
		for (int i = 0; i < satellites.size(); i++) {
			if (distance(receiver, satellites.get(i)) < dist) {
				end = i;
				dist = distance(receiver, satellites.get(i));
			}
		}
		
		//they're close enough to use one satellite.
		if (start == end) {
			return start;
		}
		
		//find a path between the satellites closest to the from and to entities
		ArrayList<Satellite> path = pathfinder.findPath(satellites, start, end);
		
		//we succeeded in finding a path
		if (path != null) {
			return path.get(path.size() - 1).getID();
		}
		//we failed to find a path
		return -1;
	}
	
	public double distance(Entity a, Entity b) {
		Vector3 locA = a.getLocation();
		Vector3 locB = b.getLocation();
		return Math.sqrt(Math.pow(locA.getX() - locB.getX(), 2) + Math.pow(locA.getY() - locB.getY(), 2) + Math.pow(locA.getZ() - locB.getZ(), 2));
	}
	
	public Satellite getSatellite(int i) {
		return satellites.get(i);
	}
	
	public Entity getEntity(String name) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getName() == name) {
				return entities.get(i);
			}
		}
		return null;
	}
	
//	public void PrintDebug(double time) {
//		for (int i = 0; i < satellites.size(); i++) {
//			System.out.println(String.format("Satellite: %d radius: %2$fm true anomaly: %3$f", i, satellites.get(i).getRadius(), satellites.get(i).getTrueAnomaly()));
//		}
//	}
}