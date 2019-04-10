package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.MainRunner;
import fr.noobeclair.hashcode.bean.BeanContainer;

public class H2018BeanContainer extends BeanContainer {

	public Set<Car> inputCars;
	public Set<Car> cars;
	public List<Ride> inputRides;
	public List<Ride> availableRides;
	public List<Ride> doneRides;
	public List<Ride> selectedRides;
	public CityMap map;
	public Integer bonus;
	public Integer maxTurn;
	public Integer score;

	protected void reset() {
		this.inputRides = new ArrayList<>();
		this.cars = new TreeSet<>();
		this.availableRides = new ArrayList<>();
		this.doneRides = new ArrayList<>();
		this.selectedRides = new ArrayList<>();
		this.inputCars = new TreeSet<>();
		this.score = 0;
	}

	@Override
	public Object getNew() {
		// Add custom properties copy
		// this.mycustom properties = o.custom;
		H2018BeanContainer h = new H2018BeanContainer(inName, inputCars, inputRides, null);
		return this;
	}

	public H2018BeanContainer(Object o) {
		super(o);
		H2018BeanContainer c = (H2018BeanContainer) o;
		reset();
	}

	public H2018BeanContainer(String inName) {
		super(inName);
		reset();
	}

	public H2018BeanContainer(String inName, Set<Car> cars, List<Ride> rides, CityMap map) {
		super(inName);
		reset();
		this.inputCars = cars;
		this.cars = cars;
		this.inputRides = rides;
		this.availableRides = rides;
		this.map = map;
	}

	public Set<Car> getCars() {
		return cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}

	public List<Car> getAvailableCars() {
		return this.cars.stream().filter(c -> org.apache.commons.lang3.BooleanUtils.isTrue(c.available))
				.collect(Collectors.toList());
	}

	public List<Ride> getAvailableRides() {
		return availableRides;
	}

	public void setAvailableRides(List<Ride> availableRides) {
		this.availableRides = availableRides;
	}

	public List<Ride> getDoneRides() {
		return doneRides;
	}

	public void setDoneRides(List<Ride> doneRides) {
		this.doneRides = doneRides;
	}

	public List<Ride> getSelectedRides() {
		return selectedRides;
	}

	public void setSelectedRides(List<Ride> selectedRides) {
		this.selectedRides = selectedRides;
	}

	public CityMap getMap() {
		return map;
	}

	public void setMap(CityMap map) {
		this.map = map;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public Integer getMaxTurn() {
		return maxTurn;
	}

	public void setMaxTurn(Integer maxTurn) {
		this.maxTurn = maxTurn;
	}

	public static H2018BeanContainer parseFromStr(String inName, String in, String sep) {
		H2018BeanContainer res = new H2018BeanContainer(inName);
		CityMap city = new CityMap();

		StringTokenizer stk = new StringTokenizer(in, sep);

		int i = 0;
		while (stk.hasMoreElements()) {
			String s = stk.nextToken();
			int nb = 0;
			switch (i) {
			case 0:
				city.setMaxX(Long.parseLong(s));
				break;
			case 1:
				city.setMaxY(Long.parseLong(s));
				res.setMap(city);
				break;
			case 2:
				nb = Integer.parseInt(s);
				res.cars = new TreeSet<>();
				for (int j = 0; j < i; j++) {
					Car car = new Car(j, new Point(0, 0));
					res.cars.add(car);
				}
				break;
			case 3:
				nb = Integer.parseInt(s);
				res.availableRides = new ArrayList<>(nb);
				res.selectedRides = new ArrayList<>(nb);
				res.doneRides = new ArrayList<>(nb);
				break;
			case 4:
				nb = Integer.parseInt(s);
				res.bonus = nb;
				break;
			case 5:
				nb = Integer.parseInt(s);
				res.maxTurn = nb;
				break;
			default:
				String e = "Error creating beancontainer, in " + in + ", delim " + sep + ", res " + res;
				throw new RuntimeException(e);

			}
			i++;
		}
		return res;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		List<Ride> arides = inputRides.stream().map(Ride::new).collect(Collectors.toList());
		List<Ride> drides = doneRides.stream().map(Ride::new).collect(Collectors.toList());
		List<Ride> srides = selectedRides.stream().map(Ride::new).collect(Collectors.toList());
		Set<Car> car = cars.stream().map(Car::new).collect(Collectors.toSet());
		H2018BeanContainer res = new H2018BeanContainer(this.inName, car, arides, map);
		res.doneRides = drides;
		res.selectedRides = srides;
		res.bonus = this.bonus;
		res.maxTurn = this.maxTurn;
		return res;
	}

	@Override
	public String toString() {
		return "Container[arides=[" + availableRides + "]," + MainRunner.CR + " drides=[" + doneRides + "],"
				+ MainRunner.CR + " srides=[" + selectedRides + "]," + MainRunner.CR + "cars=[" + cars + "], map=" + map
				+ ", bonus=" + bonus + ", maxTurn=" + maxTurn + "]";
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public List<Ride> getInputRides() {
		return inputRides;
	}

	public void setInputRides(List<Ride> inputRides) {
		this.inputRides = inputRides;
	}

}
