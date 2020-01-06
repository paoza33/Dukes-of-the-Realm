package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * The Troop class represent a troop belonging to a castle
 * There are three types of troops: 
 * 		Lancer
 * 		Knight
 * 		Onager
 * 
 * @author Kadri Mehdi
 * @version 8.0
 */

public abstract class  Troop extends Sprite{
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
	
	/**
	 * check if the troop is in collision with the castle
	 * @param castle
	 * 			the castle with which the troop may collide
	 * @return
	 */
	public boolean collidesWith(Castle castle) {
		if(this.x >= castle.getX() && (this.x<= castle.getX() + castle.getW() )) {
			if(this.y >= castle.getY() && (this.y<= castle.getY() + castle.getH() )) {
				return true;
			}
		}
    	return false;
    }
	
	/**
	 * moving the ost to the target castle
	 * @param castle
	 * 			the target of the ost
	 * @param speed
	 * 			the speed of the ost
	 */
	public void attack(Castle castle, double speed) {		//deplacements des troupes
		if(this.y < castle.getY()) {
			this.y += speed;
			
		}
		else if(this.y > (castle.getY() + castle.getH())) {
			this.y -= speed;
		}
		if(this.x < castle.getX()) {
			this.x += speed;
		}
		else if(this.x > castle.getX() + castle.getW()) {
			this.x -= speed;
		}
	}
	
	/**
	 * bypassing a castle that's not the target
	 * @param castleNeutral
	 * 			the castle that is not the target
	 * @param castleEnemy
	 * 			the castle that's the target
	 * @param speed
	 * 			troop speed
	 */
	public void bypass(Castle castleNeutral, Castle castleEnemy, double speed) {
		if(x > castleNeutral.getX() && x < (castleNeutral.getX() + castleNeutral.getW())) {
			if(castleEnemy.getX() > (castleNeutral.getX() + castleNeutral.getW())) {
				x += speed;
			}
			else if(castleEnemy.getX() > castleNeutral.getX() && castleEnemy.getX() < (castleNeutral.getX()+ castleNeutral.getW())){
				if(x < castleNeutral.getX() + (castleNeutral.getW() /2 )) {
					x -= speed;
				}
				else {
					x += speed;
				}
			}
			else {
				x -= speed;
			}
		}
		else {
			if(castleEnemy.getY()< castleNeutral.getY()) {
				y -= speed;
			}
			else if(castleEnemy.getY() > (castleNeutral.getY() + castleNeutral.getH())) {
				y += speed;
			}
			else {
				if(y < castleNeutral.getY() + (castleNeutral.getY() /2 )) {
					y -= speed;
				}
				else {
					y += speed;
				}
			}
		}
		
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