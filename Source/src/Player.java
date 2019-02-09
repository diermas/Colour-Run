import processing.core.PApplet;
import processing.core.PImage;

public class Player extends PApplet{
	
	private int x;
	private int y;
	private int speedY;
	private PApplet parent;
	private String type;
	private boolean flying;
	private PImage[][] spritesheet;
	private int animCount;
	private int animStep;
	private PImage current;
	
	public Player(PApplet parent, String type, PImage[][] spritesheet) {
		x = 100;
		y = 317;
		this.parent = parent;
		this.type = type;
		speedY = 0;
		flying = false;
		this.spritesheet = spritesheet;
		animCount = 0;
		animStep = 0;
		current = spritesheet[0][0];
	}
	
	public void move() {
		y += speedY;
		if (flying) {			
			speedY--;
		} else {
			speedY++;
		}
		if (flying && y <= 90) {
			speedY = 0;
			y = 90;
		} else if (!flying && y >= 317) {
			speedY = 0;
			y = 317;
		}
		if (animCount == 20) {
			if (animStep == 3) {
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
			typeIndex = 0;
			break;
		}
		if (!flying) {
			current = spritesheet[typeIndex][animStep];
		} else {
			current = spritesheet[typeIndex][animStep+4];
		}
		parent.image(current, x, y);
	}
	
	public void update() {
		this.move();
		this.render();
	}
	
	public void jump() {
		flying = !flying;
		if (!flying) {
			speedY = 15;
		} else {
			speedY = -15;
		}
		
	}
	
	public boolean checkCollision(Barrier[] barriers) {
		for (int i = 0; i < barriers.length; i++) {
			float barX = barriers[i].getX();
			float barY = barriers[i].getY();
			String barType = barriers[i].getType();
			if (barX <= 170 && barX+70 >= 170) {
				if (barY-3 <= y && barY+147 >= y+86) {
					if (barType != type) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void reset() {
		y = 320;
		flying = false;
		speedY = 0;
	}
	
}
