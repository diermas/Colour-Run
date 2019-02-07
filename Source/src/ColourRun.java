import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class ColourRun extends PApplet{
	
	public Player player1;	
	public Barrier[] barriers;
	public int counter;
	public int score;
	public int scoreCounter;
	public boolean gamePaused;
	public PImage walls;
	public PImage[][] wallSheet;
	public int level;
	public Clip musicClip;
	public Clip jumpClip;
	
	public static void main(String[] args) {
		PApplet.main("ColourRun");
	}
	
	public void settings() {
		size (1200,600);
	}
	
	public void setup() {
		playMusic("data/PimPoy.wav", (float) 0.2);
		level = 0;
		walls = loadImage("textures.png");
		player1 = new Player(this, "fire");
		wallSheet = new PImage[5][6];
		for (int i = 0; i < wallSheet.length; i++) {
			for (int j = 0; j < wallSheet[i].length; j++) {
				wallSheet[i][j] = walls.get(j*50, i*160, 50, 160);
			}
		}
		score = 0;
		scoreCounter = 0;
		barriers = new Barrier[8];
		startLevel();
		counter = 0;
		gamePaused = false;
	}
	
	public void draw() {
		switch (level) {
		case 0:
			background(66,229,244);
			stroke(0);
			fill(36,242,104);
			rect(0,400,width,height-400);
			textSize(50);
			fill(0);
			text("COLOUR RUN!!!", 400, 70);
			textSize(30);
			text("Use the number keys 1-4 to change your colour", 250, 150);
			text("Press Spacebar to jump, and Escape to pause/unpause", 200, 200);
			text("Match your colour to the wall to pass through", 275, 250);
			fill(255);
			rect(200,450,250,100);
			rect(750,450,250,100);
			fill(0);
			text("Play", 290, 510);
			text("Quit", 840, 510);
			break;
		case 1:
			// Draw the background features and button highlights
			background(66, 229, 244);
			textSize(32);
			fill(0);
			stroke(0);
			text("Score: "+score, 50, 50);
			fill(36, 242, 104);
			rect(0,400,width,height-400);
			fill(255,0,0);
			rect(50, 450, 200, 100);
			fill(0);
			text("1", 140, 510);
			fill(0,255,0);
			rect(350, 450, 200, 100);
			fill(0);
			text("2", 440, 510);
			fill(255,255,0);
			rect(650, 450, 200, 100);
			fill(0);
			text("3", 740, 510);
			fill(255,0,255);
			rect(950, 450, 200, 100);
			fill(0);
			text("4", 1040, 510);
			textSize(10);
			
			if (!gamePaused) {
				player1.update();
				for (int i = 0; i < barriers.length; i++) {
					barriers[i].update();
				}
				if (counter == 100) {
					for (int i = 0; i < barriers.length; i++) {
						barriers[i].speedUp();
					}
					counter = 0;
				} else {
					counter++;
				}
				if (player1.checkCollision(barriers)) {
					level = 2;
				}
				for (int i = 0; i < barriers.length; i++) {
					if (barriers[i].checkOffScreen()) {
						String newType = randomType();
						barriers[i].refresh(newType);
						if (i % 2 != 0) {
							while(barriers[i-1].getType() == barriers[i].getType()) {
								newType = randomType();
								barriers[i].setType(newType);
							}
						}
					}
				}
				if (scoreCounter == 10) {
					score++;
					scoreCounter = 0;
				} else {
					scoreCounter++;
				}
				if (jumpClip != null) {
					if (!jumpClip.isRunning()) {
						jumpClip.close();
					}
				}
			} else {
				player1.render();
				for (int i = 0; i < barriers.length; i++) {
					barriers[i].render();
				}
			}
			break;
		case 2:
			background(66,229,244);
			stroke(0);
			fill(36,242,104);
			rect(0,400,width,height-400);
			textSize(50);
			fill(255,0,0);
			text("GAME OVER", 450, 200);
			text("Your Score: " + score, 420, 300);
			fill(255);
			rect(200,450,250,100);
			rect(750,450,250,100);
			fill(0);
			textSize(30);
			text("Restart", 270, 510);
			text("Quit", 840, 510);
			break;
		default:
			break;
		}
		
	}
	
	public String randomType() {
		Random random = new Random();
		int index = random.nextInt(5);
		switch (index) {
		case 0: return "fire";
		case 1: return "earth";
		case 2: return "lightning";
		case 3: return "air";
		case 4: return "dark";
		default: return "dark";
		}
	}
	
	public void keyPressed() {
		if (key == ESC) {
			key = '0';
		}
		if (level == 1) {
			switch (key) {
			case ' ':
				if (!gamePaused) {
					if (!player1.isJumping()) {
						player1.jump();
						playSound("data/JumpSound.wav");
					}
				}
				break;
			case '1':
				if (!gamePaused) {
					player1.setType("fire");
				}
				break;
			case '2':
				if (!gamePaused) {
					player1.setType("earth");
				}
				break;
			case '3':
				if (!gamePaused) {
					player1.setType("lightning");
				}
				break;
			case '4':
				if (!gamePaused) {
					player1.setType("air");
				}
				break;
			case '0':
				gamePaused = !gamePaused;
				break;
			default:
				break;
			}
		}
	}
	
	public void mousePressed() {
		if (level == 0) {
			if (mouseY > 450 && mouseY < 550) {
				if (mouseX > 200 && mouseX < 450) {
					changeLevel(1);
				} else if (mouseX > 750 && mouseX < 1000) {
					exit();
				}
			}
		}
		if (level == 2) {
			if (mouseY > 450 && mouseY < 550) {
				if (mouseX > 200 && mouseX < 450) {
					changeLevel(1);
				} else if (mouseX > 750 && mouseX < 1000) {
					exit();
				}
			}
		}
	}
	
	public void playSound(String fileName) {
		try (InputStream in = getClass().getResourceAsStream(fileName)){
			InputStream bufferedIn = new BufferedInputStream(in);
			try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn)){
				jumpClip = AudioSystem.getClip();
				jumpClip.open(audioIn);
				jumpClip.start();
			}
			
		} catch (Exception e) {
			
		}
	}
	
	// Code for volume taken from Stack Overflow https://stackoverflow.com/questions/40514910/set-volume-of-java-clip
	public void playMusic(String fileName, float newVolume) {
		if (musicClip != null) {
			musicClip.stop();
			musicClip.close();
		}
		try (InputStream in = getClass().getResourceAsStream(fileName)){
			InputStream bufferedIn = new BufferedInputStream(in);
			try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn)){
				musicClip = AudioSystem.getClip();
				musicClip.open(audioIn);
				musicClip.loop(1000);
				FloatControl volume = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(20f * (float) Math.log10(newVolume));
				musicClip.start();
				System.out.println("music:"+musicClip.getLevel());
			}
			
		} catch (Exception e) {
			
		}
	}
	
	public void changeLevel(int level) {
		this.level = level;
		switch(level) {
		case 0:
			playMusic("data/PimPoy.wav", (float) 0.2);
			break;
		case 1:
			startLevel();
			playMusic("data/PlatformerAudio.wav", (float) 0.3);
			break;
		default:
			break;
		}
	}
	
	public void startLevel() {
		score = 0;
		for (int i = 0; i < barriers.length; i++) {
			String type = randomType();
			if (i % 2 == 0) {
				barriers[i] = new Barrier(this, 1200+((i/2)*400), 240, type, wallSheet);
			} else {
				String newType = randomType();
				barriers[i] = new Barrier(this, 1200+(((i-1)/2)*400), 80, type, wallSheet);
				while(barriers[i-1].getType() == newType) {
					newType = randomType();
				}
				barriers[i].setType(newType);
			}
		}
	}

}
