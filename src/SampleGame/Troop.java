package SampleGame;


public abstract class Troop {
	private int costProd, timeProd, speed, HP, dammage;
	
	public Troop(int costProd, int timeProd, int speed, int HP, int dammage) {
		this.costProd = costProd;
		this.timeProd = timeProd;
		this.speed = speed;
		this.HP = HP;
		this.dammage = dammage;
	}

	public int getCostProd() {
		return costProd;
	}

	public void setCostProd(int costProd) {
		this.costProd = costProd;
	}

	public int getTimeProd() {
		return timeProd;
	}

	public void setTimeProd(int timeProd) {
		this.timeProd = timeProd;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public int getDammage() {
		return dammage;
	}

	public void setDammage(int dammage) {
		this.dammage = dammage;
	}
	
}