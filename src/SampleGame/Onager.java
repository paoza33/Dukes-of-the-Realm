package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Onager extends Troop {
	
	public Onager(Pane layer, Image image, double x, double y, int health, int costProd, int timeProd, int speed, int damage) {
		super(layer,image,x,y,health, costProd, timeProd, speed, damage);
	}
	
	public void attack(Castle castle) {
		if(this.x < castle.getX()) {
			this.x += getSpeed();
		}
		else if(this.x > castle.getX() + castle.getW()) {
			this.x -= getSpeed();
		}
		if(this.y < castle.getY()) {
			this.y += getSpeed();
		}
		else if(this.y > castle.getX() + castle.getH()) {
			this.y -= getSpeed();
		}
	}

}