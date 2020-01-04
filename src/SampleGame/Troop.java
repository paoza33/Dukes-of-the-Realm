package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public abstract class  Troop extends Sprite{
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private int costProd, timeProd, speed, damage;
	
	public Troop(Pane layer, Image image, double x, double y, int health, int costProd, int timeProd, int speed, int damage) {
		super(layer,image,x,y,health);
		this.costProd = costProd;
		this.timeProd = timeProd;
		this.speed = speed;
		this.damage = damage;
		init();
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
		if(this.x >= castle.getX() && (this.x<= castle.getX() + castle.getW() )) {
			if(this.y >= castle.getY() && (this.y<= castle.getY() + castle.getH() )) {
				return true;
			}
		}
    	return false;
    }
	
	public void attack(Castle castle, double speed) {		//deplacements des troupes
		if(this.x < castle.getX()) {
			this.x += speed;
		}
		else if(this.x > castle.getX() + castle.getW()) {
			this.x -= speed;
		}
		if(this.y < castle.getY()) {
			this.y += speed;
			
		}
		else if(this.y > castle.getX() + castle.getH()) {
			this.y -= speed;
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
	
	private void init() {
		// calculate movement bounds of the player ship
		// allow half of the player to be outside of the screen
		minX = 0 - getWidth() / 2.0;
		maxX = Settings.SCENE_WIDTH - getWidth() / 2.0;
		minY = 0 - getHeight() / 2.0;
		maxY = Settings.SCENE_HEIGHT - getHeight();
	}

	public void move() {
		super.move();
		// ensure the player can't move outside of the screen
		checkBounds();
	}

	private void checkBounds() {
		// vertical
		y = y < minY ? minY : y;
		y = y > maxY ? maxY : y;

		// horizontal
		x = x < minX ? minX : x;
		x = x > maxX ? maxX : x;
	}

	
}