package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Onager extends Troop {
	
	public Onager(Pane layer, Image image, double x, double y, int health, int costProd, int timeProd, int speed, int damage) {
		super(layer,image,x,y,health, costProd, timeProd, speed, damage);
	}

}