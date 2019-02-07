import processing.core.PApplet;
import processing.core.PImage;

public class Barrier extends PApplet{

	private float x;
	private float y;
	private float speedX;
	private PApplet parent;
	private PImage[][] spritesheet;
	private String type;
	private int animCount;
	private int animStep;
	private PImage current;
	
	public Barrier(PApplet parent, float x, float y, String type, PImage[][] spritesheet) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		speedX = -5;
		this.type = type;
		this.spritesheet = spritesheet;
		animCount = 0;
		animStep = 0;
		current = spritesheet[0][0];
	}
	
	public void move() {
		x += speedX;
		if (animCount == 10) {
			if (animStep == 5) {
				animStep = 0;
			} else {
				animStep++;
			}
			animCount = 0;
		} else {
			animCount++;
		}
	}
	
	public void render() {	
		int typeIndex;
		switch (type) {
		case "fire":
			typeIndex = 0;
			break;
		case "earth":
			typeIndex = 1;
			break;
		case "air":
			typeIndex = 2;
			break;
		case "lightning":
			typeIndex = 3;
			break;
		default:
			typeIndex = 4;
			break;
		}
		current = spritesheet[typeIndex][animStep];
		parent.image(current, x, y);
	}
	
	public void update() {
		move();
		render();
	}
	
	public void speedUp() {
		speedX *= 1.05;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean checkOffScreen() {
		if (x <= -400) {
			return true;
		}
		return false;
	}
	
	public void refresh(String type) {
		x += 1600;
		this.type = type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
