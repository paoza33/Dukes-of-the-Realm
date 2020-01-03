package SampleGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Sprite {

    private ImageView imageView;

    protected Pane layer;

    protected double x;
    protected double y;

    protected double dx;
    protected double dy;

    protected int health;

    private boolean removable = false;

    private double w;
    private double h;

    public Sprite(Pane layer, Image image, double x, double y, int health) {

        this.layer = layer;
        this.x = x;
        this.y = y;

        this.health = health;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y);

        this.w = image.getWidth(); 
        this.h = image.getHeight(); 

        addToLayer();

    }

    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }

    public void removeFromLayer() {
        this.layer.getChildren().remove(this.imageView);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
		this.health = health;
	}

	public boolean isRemovable() {
        return removable;
    }
	
	public void remove() {
        this.removable = true;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public boolean isAlive() {
        return health > 0;
    }

    protected ImageView getView() {
        return imageView;
    }

    public void updateUI() {
        imageView.relocate(x, y);
    }

    public double getWidth() {
        return w;
    }

    public double getHeight() {
        return h;
    }

    public double getCenterX() {
        return x + w * 0.5;
    }

    public double getCenterY() {
        return y + h * 0.5;
    }

}