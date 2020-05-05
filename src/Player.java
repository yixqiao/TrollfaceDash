import java.util.ArrayList;

import processing.core.PImage;

public class Player extends MovingImage {

	private double velocityY;
	private boolean onPlatform;

	public Player(PImage img, int x, int y, int w, int h) {
		super(img, x, y, w, h);
		velocityY = 0;
		onPlatform = true;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public boolean isOnPlatform() {
		return onPlatform;
	}

	public void jump(double velocityY) {
		this.velocityY = velocityY;
		onPlatform = false;
	}

	public void land() {
		velocityY = 0;
		onPlatform = true;
	}

	public MovingImage act(ArrayList<MovingImage> list) {
		moveByAmount(0, velocityY);
		velocityY += 0.4;
		onPlatform = false;
		for (MovingImage m : list) {
			if (intersects(m)) {
				if (m instanceof NormalPlatform || m instanceof Goal) {
					if (getY() <= m.getY() - m.getHeight() + getVelocityY() + 1) {
						y = m.getY() - m.getHeight() + 1;
						land();
					} else {
						return m;
					}
				} else if (m instanceof DangerousPlatform) {
					return m;
				}

			}
		}
		return null;
	}

}
