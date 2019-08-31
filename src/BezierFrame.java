import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

/**
 * The BezierFrame class contains method to establish the GUI and interact with
 * users. example usage: new BezierFrame();
 * 
 * @author 180007800
 *
 */
public class BezierFrame extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

	private static int SIZE = 10;
	private JLabel sample;
	private JTextField samNum;
	private JButton showSam;
	private JButton clearCanvas;
	private JButton addPts;
	private JButton drawDash;
	private JCheckBox showTan;
	private JCheckBox showCurva;

	float x;
	float y;
	private Point clicked;

	private int frameWidth;
	private int frameHeight;

	private boolean drawSam;
	private boolean drawTan;
	private boolean drawCurva;
	private boolean hasTan;
	private boolean hasCurva;
	private boolean isEdit;
	private boolean hasDash;
	private boolean isDraw;

	BezierCurve bezierCurve;

	/**
	 * Constructor. set size of the image, setup all components.
	 */
	public BezierFrame() {
		frameWidth = 800;
		frameHeight = 800;

		drawSam = true;
		drawTan = false;
		drawCurva = false;
		hasTan = false;
		hasCurva = false;
		hasDash = false;
		isDraw = true;
		isEdit = true;

		this.setPreferredSize(new Dimension(frameWidth, frameHeight));

		JPanel actionPanel = new JPanel();
		clearCanvas = new JButton("Clear the curve");
		clearCanvas.addActionListener(this);
		
		addPts = new JButton("Add points");
		addPts.addActionListener(this);

		drawDash = new JButton("Draw dash outside the curve");
		drawDash.addActionListener(this);

		JPanel samplePanel = new JPanel();
		showSam = new JButton("Show the sample points");
		showSam.addActionListener(this);

		sample = new JLabel("Input the Numbers of samples");
		samNum = new JTextField(5);

		showTan = new JCheckBox("Show the tangent");
		showTan.addActionListener(this);

		showCurva = new JCheckBox("Show the curva");
		showCurva.addActionListener(this);

		/**
		 * Setup all components of the main frame: the box, the button, the text, the
		 * label, mouse listeners, and the main canvas of BezierFrame
		 */
		actionPanel.add(clearCanvas);
		actionPanel.add(addPts);
		actionPanel.add(drawDash);
		actionPanel.setVisible(true);

		samplePanel.add(showSam);
		samplePanel.add(showSam);
		samplePanel.add(sample);
		samplePanel.add(samNum);
		samplePanel.add(showTan);
		samplePanel.add(showCurva);
		samplePanel.setVisible(true);

		JFrame frame = new JFrame("CS4102-p1-BezierCurve");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.add(actionPanel, BorderLayout.SOUTH);
		frame.add(samplePanel, BorderLayout.NORTH);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);

		this.bezierCurve = new BezierCurve(frameWidth, frameHeight);

		/**
		 * invoke dtawBasic()method of BezierDraw to set the background
		 */
		bezierCurve.drawBasic();
		repaint();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(bezierCurve.getImg(), 0, 0, null);
	}

	/**
	 * Setup the frame with all buttons: movePoints, addPoints, darwDash,
	 * clearCanvas, showSam, showTan, showCurva a text field to change max
	 * iteration, and a button to update with the given max iteration.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == clearCanvas) {
			bezierCurve.update();
			repaint();
		} else if (e.getSource() == addPts) {
			isDraw = true;
			isEdit = false;
		} 
		else if (e.getSource() == drawDash) {
			if (hasDash == true) {
				hasDash = false;
				bezierCurve.clearDash();
				repaint();
			} else {
				hasDash = true;
				bezierCurve.drawDash();
				repaint();
			}

		} else if (e.getSource() == showSam) {
			/**
			 * showSample is implemented after getting the value of textField.
			 */
			if (samNum.getText().length() == 0) {
				alertSample();
			} else {
				bezierCurve.showSam(Integer.parseInt(samNum.getText()));
				repaint();
			}
		} else if (e.getSource() == showTan) {
			/**
			 * show the tangent
			 */
			if (samNum.getText().length() == 0 || drawSam == false) {
				alertSample();
			}

			if (hasTan == true) {
				hasTan = false;
				bezierCurve.clearTan();
				repaint();
			} else {
				hasTan = true;
				bezierCurve.drawTan();
				repaint();
			}
		} else if (e.getSource() == showCurva) {
			/**
			 * show the curvature
			 */
			if (samNum.getText().length() == 0 || drawSam == false) {
				alertSample();
			}
			if (hasCurva == true) {
				hasCurva = false;
				bezierCurve.clearCurva();
				repaint();
			} else {
				hasCurva = true;
				bezierCurve.drawCurva();
				repaint();
			}
		}
	}

	/**
	 * all the operations about mouse, the curve will be draw when the mouse is
	 * dragged the cursor will be changed to hand_cursor when the mouse iw moved the
	 * control points will be draw when the mouse is pressed
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (isEdit && clicked != null) {
			if (e.getX() - SIZE / 2 < frameWidth && e.getY() - SIZE / 2 < frameHeight) {
				bezierCurve.clear();
				clicked.x = e.getX() - SIZE / 2;
				clicked.y = e.getY() - SIZE / 2;
				bezierCurve.drawCurve();
				bezierCurve.drawControlPts();
				repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (!isDraw
				&& bezierCurve.checkPoint(e.getX() - SIZE / 2, e.getY() - SIZE / 2, bezierCurve.controlPts) != null) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * give the coordinates of the first point when clicked.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (isDraw && bezierCurve.allowAddPts) {
			if (e.getX() - SIZE / 2 < frameWidth && e.getY() - SIZE / 2 < frameHeight) {
				bezierCurve.addPts(e.getX() - SIZE / 2, e.getY() - SIZE / 2);
				repaint();
			}
		} else if (!isDraw && isEdit) {
			clicked = bezierCurve.checkPoint(e.getX() - SIZE / 2, e.getY() - SIZE / 2, bezierCurve.controlPts);
		} else if (!isDraw && !isEdit) {
			clicked = bezierCurve.checkPoint(e.getX() - SIZE / 2, e.getY() - SIZE / 2, bezierCurve.controlPts);
			bezierCurve.removePoint(clicked);
			repaint();
		} else
			alertPoints();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * in case of mistakes
	 */
	private void alertPoints() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "please clear the surface");
	}

	private void alertSample() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "please enter the number of sample points");
	}
}
