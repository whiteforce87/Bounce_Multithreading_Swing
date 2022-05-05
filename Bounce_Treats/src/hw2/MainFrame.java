package hw2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainFrame extends JFrame implements BounceListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	double updatedXPos;
	double updatedYPos;
	double releasedX;
	double releasedY;
	double pressedX;
	double pressedY;
	double changedX;
	double changedY;
	int condition;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		initGUI();
	}

	private void initGUI() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panel = new CustomPanel();
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				do_panel_mouseReleased(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				do_panel_mousePressed(e);
			}
		});
		contentPane.add(panel, BorderLayout.CENTER);

	}

	BounceThread bounceThread;

	boolean started = false;

	
	protected void do_panel_mouseReleased(MouseEvent e) {
		releasedX = e.getPoint().x;
		releasedY = e.getPoint().y;

		ExecutorService srv = Executors.newCachedThreadPool();
		if (bounceThread == null && started == false) {
			updatedXPos = e.getX();
			updatedYPos = e.getY();
			bounceThread = new BounceThread(this);
			srv.submit(bounceThread);
			started = true;

		} else {
			
			if (pressedX == releasedX && pressedY == releasedY) {
				updatedXPos = e.getX();
				updatedYPos = e.getY();
				started = true;
				bounceThread = new BounceThread(this);
				srv.submit(bounceThread);
				

			} else {
				
				updatedXPos = e.getX();
				updatedYPos = e.getY();
				condition = 0;
				bounceThread = new BounceThread(this);
				srv.submit(bounceThread);
				started = true;

			}

			srv.shutdown();

		}
	}

	protected void do_panel_mousePressed(MouseEvent e) {
		pressedX = e.getPoint().x;
		pressedY = e.getPoint().y;

		if (bounceThread != null) {
			bounceThread.pauseThread();

		}

	}

	@Override
	public void bounce(double x, double y) {

		((CustomPanel) panel).setPosX(x);
		((CustomPanel) panel).setPosY(y);

		repaint();

	}



	public class BounceThread implements Runnable {

		BounceListener listener;

		boolean canRun = true;
		boolean isPaused = false;
		boolean isResumed = true;
		double x = updatedXPos;
		double y = updatedYPos;
		int diameter = 50;
		int radius = diameter / 2;
		int width;
		int height;
		
		double dx = ((CustomPanel) panel).getDirectionX();
		double dy = ((CustomPanel) panel).getDirectionY();

		public BounceThread(BounceListener listener) {
			this.listener = listener;
		}

		@Override
		public void run() {

			while (true) {

				width = panel.getWidth() - radius;
				height = panel.getHeight() - radius;

				changedX = releasedX - pressedX;
				changedY = releasedY - pressedY;
				
				/*Ball can make 45 degrees movements according to positive or negative value of X and Y.
				Also by using condition, it can move to the previous direction after click on the screen */

				if (changedX > 0 && changedY > 0 || condition == 1) {
					x = x + dx;
					y = y + dy;
					condition = 1;
				} else if (changedX > 0 && changedY < 0 || condition == 2) {
					x = x + dx;
					y = y - dy;
					condition = 2;
				} else if (changedX < 0 && changedY < 0 || condition == 3) {
					x = x - dx;
					y = y - dy;
					condition = 3;
				} else if (changedX < 0 && changedY > 0 || condition == 4) {
					x = x - dx;
					y = y + dy;
					condition = 4;
				} else if (changedX == 0 && changedY > 0 || condition == 5) {
					y = y + dy;
					condition = 5;
				} else if (changedX == 0 && changedY < 0 || condition == 6) {
					y = y - dy;
					condition = 6;
				} else if (changedX < 0 && changedY == 0 || condition == 7) {
					x = x - dx;
					condition = 7;
				} else if (changedX > 0 && changedY == 0 || condition == 8) {
					x = x + dx;
					condition = 8;
				}


					if (!isPaused) {
						if (x < 0) {
							dx = -dx;
							x = radius;
						} else if (x + radius > width) {
							dx = -dx;
							x = width - radius;
						}

						if (y < 0) {
							dy = -dy;
							y = radius;
						} else if (y + radius > height) {
							dy = -dy;
							y = height - radius;
						}

						listener.bounce(x, y);
						try {
							Thread.sleep(40);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {

						try {
							synchronized (this) {
								wait();
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
			}
		}

		public void pauseThread() {
			isPaused = true;

		}

		public void resumeThread() {
		
			isPaused = false;

			synchronized (this) {
				notify();
			}
		}

		public void stopThread() {
			this.canRun = false;
		}
	}

}
