package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public abstract class Troop extends Sprite{
	private int costProd, timeProd, speed, damage;
	
	public Troop(Pane layer, Image image, double x, double y, int health, int costProd, int timeProd, int speed, int damage) {
		super(layer,image,x,y,health);
		this.costProd = costProd;
		this.timeProd = timeProd;
		this.speed = speed;
		this.damage = damage;
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
	
	public boolean collidesWith(Castle castle) {
    	return getView().getBoundsInParent().intersects(castle.getView().getBoundsInParent());
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

	public int getDamage() {
		return damage;
	}

	public void setDamage(int dammage) {
		this.damage = dammage;
	}
	
}