package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Lancer extends Troop{
	
	public Lancer(Pane layer, Image image, double x, double y, int health, int costProd, int timeProd, int speed, int damage) {
		super(layer,image,x,y,health, costProd, timeProd, speed, damage);
	}
	
	public void attack(Castle castle) {
		if(x < castle.getX()) {
			x += getSpeed();
		}
		else if(x > castle.getX() + castle.getW()) {
			x -= getSpeed();
		}
		if(y < castle.getY()) {
			y += getSpeed();
			
		}
		else if(y > castle.getX() + castle.getH()) {
			y -= getSpeed();
		}
	}

}