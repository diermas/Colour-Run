import processing.core.PApplet;

public class Player extends PApplet{
	
	private int x;
	private int y;
	private int speedY;
	private PApplet parent;
	private String type;
	private boolean flying;
	
	public Player(PApplet parent, String type) {
		x = 100;
		y = 375;
		this.parent = parent;
		this.type = type;
		speedY = 0;
		flying = false;
	}
	
	public void move() {
		y += speedY;
		if (flying) {			
			speedY--;
		} else {
			speedY++;
		}
		if (flying && y <= 160) {
			speedY = 0;
			y = 160;
		} else if (!flying && y >= 375) {
			speedY = 0;
			y = 375;
		}
	}
	
	public void render() {
		switch (type) {
		case "fire":
			parent.fill(255,0,0);
			break;
		case "lightning":
			parent.fill(255,255,0);
			break;
		case "air":
			parent.fill(255,0,255);
			break;
		case "earth":
			parent.fill(0,255,0);
			break;
		default:
			parent.fill(255,0,0);
			break;
		}
		parent.stroke(0);
		parent.ellipse(x,y,50,50);
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
			if (barX <= 100 && barX+50 >= 100) {
				if (barY <= y-25 && barY+160 >= y+25) {
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
		y = 375;
		flying = false;
		speedY = 0;
	}
	
}
