package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * one of the types of troops
 * @author Kadri Mehdi
 *@version 8.0
 */
public class Knight extends Troop{

	public Knight(Pane layer, Image image, double x, double y, int health, int costProd, int timeProd, int speed, int damage) {
		super(layer,image,x,y,health, costProd, timeProd, speed, damage);
	}

}