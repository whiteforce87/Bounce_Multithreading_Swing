package hw2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;

public class CustomPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double posX = 50;
	double posY = 50;
	//Instead of moving 1 pixel, I changed the value and increase the speed. I also change the sleep value in the thread.
	double directionX = 8;
	double directionY = 8;

	private BounceListener listener;

	public CustomPanel() {

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				posX = e.getX() - 25;
				posY = e.getY() - 25;

				repaint();

			}

		});

		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {

				posX = e.getX() - 25;
				posY = e.getY() - 25;

				repaint();

			}

		});

	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D pen = (Graphics2D) g;

		pen.setColor(Color.red);

		pen.fillArc((int) posX, (int) posY, 50, 50, 0, 360);

	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;

	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;

	}

	public BounceListener getListener() {
		return listener;
	}

	public void setListener(BounceListener listener) {
		this.listener = listener;
	}

	public double getDirectionX() {
		return directionX;
	}

	public void setDirectionX(int directionX) {
		this.directionX = directionX;
	}

	public double getDirectionY() {
		return directionY;
	}

	public void setDirectionY(int directionY) {
		this.directionY = directionY;
	}

}
