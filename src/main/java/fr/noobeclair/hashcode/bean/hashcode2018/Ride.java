package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.StringTokenizer;

import fr.noobeclair.hashcode.MainRunner;
import fr.noobeclair.hashcode.bean.Bean;

public class Ride extends Bean {
	
	public Integer id;
	public Point start;
	public Point end;
	public Integer points;
	public Integer tripSt;
	public Integer tripEnd;
	
	public Ride() {
		super();
	}
	
	public Ride(Integer id, Point start, Point end, Integer points, Integer tripSt, Integer tripEnd) {
		super();
		this.id = id;
		this.start = start;
		this.end = end;
		this.points = points;
		this.tripSt = tripSt;
		this.tripEnd = tripEnd;
	}
	
	public Ride(Ride r) {
		super();
		this.id = r.id;
		this.start = r.start;
		this.end = r.end;
		this.points = r.points;
		this.tripSt = r.tripSt;
		this.tripEnd = r.tripEnd;
	}
	
	public Ride(Integer id, Point start, Point end) {
		super();
		this.id = id;
		this.start = start;
		this.end = end;
		this.tripSt = 0;
		this.tripEnd = 0;
		this.points = null;
	}
	
	protected Point getStart() {
		return start;
	}
	
	protected Point getEnd() {
		return end;
	}
	
	@Override
	public double realdistance(Bean b) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	@Override
	public int realhashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((tripEnd == null) ? 0 : tripEnd.hashCode());
		result = prime * result + ((tripSt == null) ? 0 : tripSt.hashCode());
		return result;
	}
	
	@Override
	public boolean realequals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Ride other = (Ride) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (tripEnd == null) {
			if (other.tripEnd != null)
				return false;
		} else if (!tripEnd.equals(other.tripEnd))
			return false;
		if (tripSt == null) {
			if (other.tripSt != null)
				return false;
		} else if (!tripSt.equals(other.tripSt))
			return false;
		return true;
	}
	
	@Override
	public int realcompareTo(Bean b) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getTripSt() {
		return tripSt;
	}
	
	public void setTripSt(Integer tripSt) {
		this.tripSt = tripSt;
	}
	
	public Integer getTripEnd() {
		return tripEnd;
	}
	
	public void setTripEnd(Integer tripEnd) {
		this.tripEnd = tripEnd;
	}
	
	public void setStart(Point start) {
		this.start = start;
	}
	
	public void setEnd(Point end) {
		this.end = end;
	}
	
	public boolean isInQuarter(Quarter q) {
		if (this.start.x >= q.getStart().getX() && this.start.x <= q.getEnd().getX() && this.start.y >= q.getStart().getY() && this.start.y <= q.getEnd().getY()) {
			return true;
		} else if (this.start.x <= q.getStart().getX() && this.start.x >= q.getEnd().getX() && this.start.y >= q.getStart().getY() && this.start.y >= q.getEnd().getY()) {
			return true;
		} else if (this.start.x <= q.getStart().getX() && this.start.x >= q.getEnd().getX() && this.start.y >= q.getStart().getY() && this.start.y <= q.getEnd().getY()) {
			return true;
		} else if (this.start.x <= q.getStart().getX() && this.start.x >= q.getEnd().getX() && this.start.y <= q.getStart().getY() && this.start.y >= q.getEnd().getY()) {
			return true;
		}
		return false;
		/*
		 * x = -2, y = -2 , q1.s.x = -1, q1.s.y = -1s, q1.e.x = -3 q2.e.y = -2 --> x < x
		 * = -2, y = 2 , q1.s.x = -1, q1.s.y = 1, q1.e.x = -3 q2.e.y = 2
		 * 
		 */
	}
	
	public Integer getPoints() {
		if (points == null) {
			this.points = Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
		}
		return points;
	}
	
	public void setPoints(Integer points) {
		this.points = points;
	}
	
	public static Ride parseFromStr(String in, String sep) {
		Ride res = new Ride();
		StringTokenizer stk = new StringTokenizer(in, sep);
		
		int i = 0;
		while (stk.hasMoreElements()) {
			String s = stk.nextToken();
			int nbm1 = 0;
			int nb = 0;
			switch (i) {
			case 0:
				nbm1 = Integer.parseInt(s);
				break;
			case 1:
				nb = Integer.parseInt(s);
				res.start = new Point(nbm1, nb);
				break;
			case 2:
				nbm1 = Integer.parseInt(s);
				break;
			case 3:
				nb = Integer.parseInt(s);
				res.end = new Point(nbm1, nb);
				break;
			case 4:
				nb = Integer.parseInt(s);
				res.tripSt = nb;
				break;
			case 5:
				nb = Integer.parseInt(s);
				res.tripEnd = nb;
				break;
			default:
				String e = "Error creating slide, in " + in + ", delim " + sep + ", res " + res;
				throw new RuntimeException(e);
				
			}
			i++;
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "Ride [id=" + id + ", start=" + start + ", end=" + end + ", points=" + getPoints() + ", tripSt=" + tripSt + ", tripEnd=" + tripEnd + "]" + MainRunner.CR;
	}
	
}
