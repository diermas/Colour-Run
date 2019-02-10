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
	public PImage texSheet;
	public PImage[][] wallSheet;
	public PImage[][] charSheet;
	public int level;
	public Clip musicClip;
	
	public static void main(String[] args) {
		PApplet.main("ColourRun");
	}
	
	public void settings() {
		size (1200,600);
	}
	
	public void setup() {
		// Give all variables values as needed, and load the texture file into two separate arrays
		playMusic("data/PimPoy.wav", (float) 0.2);
		level = 0;
		texSheet = loadImage("spritesheet.png");
		wallSheet = new PImage[5][4];
		for (int i = 0; i < wallSheet.length; i++) {
			for (int j = 0; j < wallSheet[i].length; j++) {
				wallSheet[i][j] = texSheet.get(j*53, i*147, 53, 147);
			}
		}
		charSheet = new PImage[4][8];
		for (int i = 0; i < charSheet.length; i++) {
			for (int j = 0; j < charSheet[i].length; j++) {
				charSheet[i][j] = texSheet.get(j*70, (i*86)+735, 70, 86);
			}
		}
		player1 = new Player(this, "fire", charSheet);
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
			// Display the Main Menu screen and buttons
			background(173, 159, 145);
			stroke(0);
			fill(91, 73, 55);
			rect(0,400,width,height-400);
			rect(0,0,width,93);
			textSize(50);
			fill(0);
			text("COLOUR RUN!!!", 400, 70);
			textSize(30);
			text("Use the number keys 1-4 to change your colour", 250, 150);
			text("Press Spacebar to toggle gravity, and Escape to pause/unpause", 160, 200);
			text("Match your colour to the wall to pass through", 275, 250);
			fill(255);
			rect(100,450,250,100);
			rect(475,450,250,100);
			rect(850,450,250,100);
			fill(0);
			text("Play", 190, 510);
			text("Credits", 550,510);
			text("Quit", 940, 510);
			break;
		case 1:
			// Draw the background features and button highlights
			background(173, 159, 145);
			textSize(32);
			fill(91, 73, 55);
			rect(0,400,width,height-400);
			rect(0,0,width,93);
			fill(0);
			stroke(0);
			text("Score: "+score, 50, 50);
			fill(231,99,18);
			rect(50, 450, 200, 100);
			fill(0);
			text("1", 140, 510);
			fill(11,229,18);
			rect(350, 450, 200, 100);
			fill(0);
			text("2", 440, 510);
			fill(255,247,0);
			rect(650, 450, 200, 100);
			fill(0);
			text("3", 740, 510);
			fill(212,92,222);
			rect(950, 450, 200, 100);
			fill(0);
			text("4", 1040, 510);
			
			// Update all game entities if the game isn't paused, otherwise just render them
			if (!gamePaused) {
				player1.update();
				for (int i = 0; i < barriers.length; i++) {
					barriers[i].update();
				}
				// Slowly increment the speed of the walls every 100 ticks
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
					player1.reset();
				}
				// If barriers move off screen, refresh them with a new type to the right
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
			} else {
				player1.render();
				for (int i = 0; i < barriers.length; i++) {
					barriers[i].render();
				}
			}
			break;
		case 2:
			// Display the Game Over screen and menu
			background(173, 159, 145);
			stroke(0);
			fill(91, 73, 55);
			rect(0,400,width,height-400);
			rect(0,0,width,93);
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
		case 3:
			background(173, 159, 145);
			stroke(0);
			fill(91, 73, 55);
			rect(0,400,width,height-400);
			rect(0,0,width,93);
			textSize(50);
			fill(0);
			text("CREDITS", 525, 75);
			textSize(30);
			text("Title Audio: Pim Poy, DL Sounds, dl-sounds.com", 50, 150);
			text("Game BGM: Platformer2, DL Sounds, dl-sounds.com", 50, 200);
			text("Sprites used: Platformer Art Complete Pack, Kenney, kenney.nl", 50, 250);
			fill(255);
			rect(475,450,250,100);
			fill(0);
			text("Back", 565, 510);
			break;
		default:
			break;
		}
		
	}
	
	// Return a new random type when called
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
	
	// 
	public void keyPressed() {
		System.out.println(key);
		// Reset ESC key to use as pause
		if (key == ESC) {
			key = '0';
		}
		// Only register keyboard controls if the game is being played
		if (level == 1) {
			switch (key) {
			case ' ':
				if (!gamePaused) {
					player1.jump();
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
	
	// Track mousePress events on the Main Menu and Game Over screens for button clicks
	public void mousePressed() {
		if (level == 0) {
			if (mouseY > 450 && mouseY < 550) {
				if (mouseX > 100 && mouseX < 350) {
					changeLevel(1);
				} else if (mouseX > 850 && mouseX < 1100) {
					exit();
				} else if (mouseX > 475 && mouseX < 725) {
					changeLevel(3);
				}
			}
		} else if (level == 2) {
			if (mouseY > 450 && mouseY < 550) {
				if (mouseX > 200 && mouseX < 450) {
					changeLevel(1);
				} else if (mouseX > 750 && mouseX < 1000) {
					exit();
				}
			}
		} else if (level == 3) {
			if (mouseY > 450 && mouseY < 550) {
				if (mouseX > 475 && mouseX < 725) {
					changeLevel(0);
				}
			}
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
			}
			
		} catch (Exception e) {
			
		}
	}
	
	// Method to change the level value and play the appropriate music as needed
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
	
	// Method to generate new barriers, including the code that stops two black barriers spawning vertical
	public void startLevel() {
		score = 0;
		for (int i = 0; i < barriers.length; i++) {
			String type = randomType();
			if (i % 2 == 0) {
				barriers[i] = new Barrier(this, 1200+((i/2)*400), 253, type, wallSheet);
			} else {
				String newType = randomType();
				barriers[i] = new Barrier(this, 1200+(((i-1)/2)*400), 93, type, wallSheet);
				while(barriers[i-1].getType() == newType) {
					newType = randomType();
				}
				barriers[i].setType(newType);
			}
		}
	}

}
