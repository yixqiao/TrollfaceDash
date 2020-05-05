import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

public class DrawingSurface extends PApplet implements ActionListener {
	public static int WIDTH = 1080;
	public static int HEIGHT = 720;

	public static EasySound music;

	public static PImage background;
	public static PImage rageBackground;
	public static PImage winBackground;

	public static PImage player1;

	public static PImage goalImage;
	public static PImage platform;
	public static PImage fakePlatform;
	public static PImage dangerousPlatform;

	private boolean gameStarted = false;
	private boolean dead = false;
	private boolean won;
	private int deathTime;

	private boolean willJump = false;
	private double jumpPressedTime;

	public static double platformSpeed = -5;

	public static int totalLevels = 0;
	private int currentLevel = 0;

	private Player player;
	private Goal goal;

	private Timer timer;

	private ArrayList<MovingImage> MIs;

	public DrawingSurface() {
		music = new EasySound("assets/Hello.wav");
		music.play();

		background = loadImage("assets/Background.jpg");
		rageBackground = loadImage("assets/ragebackground.jpg");
		winBackground = loadImage("assets/winbackground.jpg");

		player1 = loadImage("assets/Player.png");

		goalImage = loadImage("assets/Goal.png");
		platform = loadImage("assets/Dirt.png");
		fakePlatform = loadImage("assets/TransparentDirt.png");
		dangerousPlatform = loadImage("assets/RedDirt.png");

		background.resize(DrawingSurface.WIDTH, DrawingSurface.HEIGHT);
		rageBackground.resize(DrawingSurface.WIDTH, DrawingSurface.HEIGHT);
		winBackground.resize(DrawingSurface.WIDTH, DrawingSurface.HEIGHT);

		player1.resize(64, 64);

		platform.resize(64, 64);
		goalImage.resize(128, 128);

		MIs = new ArrayList<MovingImage>();

		timer = new Timer(225000, this);
		timer.start();

		FileIO io = new FileIO();
		ArrayList<String> lines = io.readFile("assets/levels/totalLevels.txt");
		totalLevels = Integer.parseInt(lines.get(0));
	}

	NormalPlatform np;
	FakePlatform fp;
	DangerousPlatform dp;

	public void spawnPlatforms() {
		FileIO io = new FileIO();
		ArrayList<String> lines = io.readFile("assets/levels/level" + currentLevel + ".txt");
		String[] level = lines.toArray(new String[lines.size()]);

		for (int i = 0; i < level.length; i++) {

			for (int j = 0; j < level[i].length(); j++) {
				if (level[i].charAt(j) == '0') {
					player = new Player(player1, 100 + j * 64, HEIGHT / 2 - 64 * level.length / 2 + i * 64, 64, 64);
					MIs.add(player);
				} else if (level[i].charAt(j) == '1') {
					goal = new Goal(goalImage, 100 + j * 64 - 64, HEIGHT / 2 - 64 * level.length / 2 + i * 64 - 64, 128,
							128);
					MIs.add(goal);
				} else if (level[i].charAt(j) == '2') {
					np = new NormalPlatform(platform, 100 + j * 64, HEIGHT / 2 - 64 * level.length / 2 + i * 64, 64,
							64);
					MIs.add(np);
				} else if (level[i].charAt(j) == '3') {
					fp = new FakePlatform(fakePlatform, 100 + j * 64, HEIGHT / 2 - 64 * level.length / 2 + i * 64, 64,
							64);
					MIs.add(fp);
				} else if (level[i].charAt(j) == '4') {
					dp = new DangerousPlatform(dangerousPlatform, 100 + j * 64,
							HEIGHT / 2 - 64 * level.length / 2 + i * 64, 64, 64);
					MIs.add(dp);
				}
			}
		}
	}

	public void draw() {
		if (dead && millis() - deathTime >= 500 && !won) {
			reset();
		}
		if (!gameStarted) {

			image(background, 0, 0);
			textSize(50);
			fill(0, 0, 0, 130);
			text("Level " + currentLevel + " (Mouse wheel to change)", WIDTH / 2 - 380, 610);

			text("Press ENTER to Start", WIDTH / 2 - 250, 650);

		} else if (!dead && gameStarted) {

			runGame();
			image(background, 0, 0);
			for (int i = 0; i < MIs.size(); i++) {
				MIs.get(i).draw(this);
			}

		} else if (dead && won) {

			image(winBackground, 0, 0);
			if (currentLevel < totalLevels) {
				text("ENTER to advance to next level", WIDTH / 2 - 380, 610);
			} else {
				text("Congratulations! You beat the game!", WIDTH / 2 - 450, 610);
			}

		} else if (dead && !won) {

			image(rageBackground, 0, 0);

		}

		if (keyPressed) {
			keyDown();
		}
	}

	public void setup() {
		size(DrawingSurface.WIDTH, DrawingSurface.HEIGHT);
		frameRate(60);
		background(255);
	}

	public void reset() {
		dead = false;
		MIs = new ArrayList<MovingImage>();

		spawnPlatforms();
	}

	public void runGame() {
		for (int i = 0; i < MIs.size(); i++) {
			if (!(MIs.get(i) instanceof Player)) {
				(MIs.get(i)).moveByAmount(platformSpeed, 0);
				if (MIs.get(i).getX() <= -MIs.get(i).getWidth() - 10) {
					MIs.remove(i);
					i--;
				}
			}
		}

		MovingImage collided = player.act(MIs);
		if (collided != null) {
			if (collided instanceof Goal) {
				dead = true;
				won = true;
				deathTime = millis();
			} else if (collided instanceof NormalPlatform) {
				dead = true;
				won = false;
				deathTime = millis();
			} else if (collided instanceof DangerousPlatform) {
				dead = true;
				won = false;
				deathTime = millis();
			}
		}
		if (player.y >= HEIGHT) {
			dead = true;
			won = false;
			deathTime = millis();
		}
		if (player.isOnPlatform() && willJump && millis() - jumpPressedTime < 150) {
			willJump = false;
			player.jump(-10.2);
		}

	}

	public void keyDown() {
		if (keyCode == KeyEvent.VK_UP) {
			if (!dead) {
				willJump = true;
				jumpPressedTime = millis();
			}
		}
	}

	public void mousePressed() {

	}

	public void mouseReleased() {

	}

	public void keyPressed() {
		if (keyCode == KeyEvent.VK_ENTER) {
			if (!gameStarted) {
				spawnPlatforms();
				gameStarted = true;
			} else if (dead && !won) {
				reset();
			} else if (dead && won) {
				if (currentLevel < totalLevels) {
					currentLevel++;
					reset();
				}
			}
		}
	}

	public void keyReleased() {
	}

	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
		currentLevel += (int) e;
		if (currentLevel < 0) {
			currentLevel = 0;
		} else if (currentLevel > totalLevels) {
			currentLevel = totalLevels;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		music.play();

	}
}
