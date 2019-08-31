import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;

/**
 * This class contains the formula of drawing the Bezier curve, some functions
 * about points and implementation of buttons
 * 
 * @author 180007800
 */
@SuppressWarnings("serial")
public class BezierCurve extends JLabel {

	private static final int RANGE = 7;
	private static final int HALFRANGE = 3;
	private static final int MAX_CONTROL_POINTS = 13;
	private Graphics g;

	private Color curveColor;
	ArrayList<Point> controlPts;
	private BufferedImage frameBuffer;

	private int width;
	private int height;
	private int colorGray;
	private int samNum;

	public boolean allowAddPts;
	private boolean showDash;
	private boolean showTan;
	private boolean showCurva;
	private boolean showSam;

	/**
	 * The constructor.
	 * 
	 * @param width
	 * @param height
	 */
	public BezierCurve(int width, int height) {
		this.width = width;
		this.height = height;
		frameBuffer = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		colorGray = Color.DARK_GRAY.getRGB();

		g = frameBuffer.createGraphics();
		allowAddPts = true;
		curveColor = Color.CYAN;
		controlPts = new ArrayList<Point>();
		samNum = 0;
		showDash = false;
		showTan = false;
		showCurva = false;
	}

	/**
	 * draw the basic background, it is set to be gray
	 * 
	 * @return
	 */
	public void drawBasic() {
		// TODO Auto-generated method stub
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				frameBuffer.setRGB(i, j, colorGray);
			}
		}
	}

	public Image getImg() {
		// TODO Auto-generated method stub
		return frameBuffer;
	}

	public void clear() {
		// TODO Auto-generated method stub
		g.setColor(Color.white);
		g.fillOval(0, 0, width, height);
	}

	/**
	 * the method includes process of drawing Bezier curve, tangent, curvature of
	 * sample points and the dash line outside the curve every time drawing the
	 * tangent, the curvature of sample points and the dash line, the Bezier curve
	 * is draw again
	 * 
	 * @return
	 */
	public void drawCurve() {
		// TODO Auto-generated method stub
		int sampleIndex = 1;
		double pX1;
		double pY1;
		double pX2 = 0;
		double pY2 = 0;
		pX1 = controlPts.get(0).x;
		pY1 = controlPts.get(0).y;
		clear();
		for (double u = 0.0002; u < 1.0; u += 0.0002) {
			pX2 = 0;
			pY2 = 0;
			for (int i = 0; i < controlPts.size(); i++) {
				pX2 += controlPts.get(i).x * parameterBezier(u, controlPts.size() - 1, i);
				pY2 += controlPts.get(i).y * parameterBezier(u, controlPts.size() - 1, i);
			}
			g.setColor(curveColor);
			g.drawLine((int) pX1 + HALFRANGE, (int) pY1 + HALFRANGE, (int) pX2 + HALFRANGE, (int) pY2 + HALFRANGE);
			if ((samNum > 0) && (u > (1.0 / (samNum + 1)) * sampleIndex)) {
				sampleIndex++;
				g.setColor(Color.red);
				g.fillOval((int) pX1, (int) pY1, RANGE, RANGE);
				if (showTan == true) {
					double x1x2Combine = 150 * (pX2 - pX1) / 0.0002
							/ Math.sqrt(Math.pow((pX2 - pX1) / 0.0002, 2) + Math.pow((pY2 - pY1) / 0.0002, 2));
					double y1y2Combine = 150 * (pY2 - pY1) / 0.0002
							/ Math.sqrt(Math.pow((pX2 - pX1) / 0.0002, 2) + Math.pow((pY2 - pY1) / 0.0002, 2));
					g.setColor(Color.BLUE);
					g.drawLine((int) pX1 + HALFRANGE, (int) pY1 + HALFRANGE, (int) (pX1 + x1x2Combine) + HALFRANGE,
							(int) (pY1 + y1y2Combine) + HALFRANGE);
				}
				if (showCurva == true) {
					u += 0.0002;
					double x0 = pX1;
					double y0 = pY1;
					pX1 = pX2;
					pY1 = pY2;
					pX2 = 0;
					pY2 = 0;
					for (int i = 0; i <= controlPts.size() - 1; i++) {
						pX2 += controlPts.get(i).x * parameterBezier(u, controlPts.size() - 1, i);
						pY2 += controlPts.get(i).y * parameterBezier(u, controlPts.size() - 1, i);
					}
					double x0x1Combine = 150 * (pX1 - x0) / 0.0002
							/ Math.sqrt(Math.pow((pX1 - x0) / 0.0002, 2) + Math.pow((pY1 - y0) / 0.0002, 2));
					double y0y1Combine = 150 * (pY1 - y0) / 0.0002
							/ Math.sqrt(Math.pow((pX1 - x0) / 0.0002, 2) + Math.pow((pY1 - y0) / 0.0002, 2));
					double x1x2Combine = 150 * (pX2 - pX1) / 0.0002
							/ Math.sqrt(Math.pow((pX2 - pX1) / 0.0002, 2) + Math.pow((pY2 - pY1) / 0.0002, 2));
					double y1y2Combine = 150 * (pY2 - pY1) / 0.0002
							/ Math.sqrt(Math.pow((pX2 - pX1) / 0.0002, 2) + Math.pow((pY2 - pY1) / 0.0002, 2));
					g.setColor(curveColor);
					g.drawLine((int) pX1 + HALFRANGE, (int) pY1 + HALFRANGE, (int) pX2 + HALFRANGE,
							(int) pY2 + HALFRANGE);
					g.setColor(Color.GREEN);
					g.drawLine((int) x0 + HALFRANGE, (int) y0 + HALFRANGE,
							(int) (x0 - (x0x1Combine - x1x2Combine) / 0.0002) + HALFRANGE,
							(int) (y0 - (y0y1Combine - y1y2Combine) / 0.0002) + HALFRANGE);
				}
			}
			pX1 = pX2;
			pY1 = pY2;
		}
		if (showDash == true) {
			g.setColor(Color.BLACK);
			for (int i = 0; i < controlPts.size() - 1; i++) {
				drawDashLine(g, controlPts.get(i).x + HALFRANGE, controlPts.get(i).y + HALFRANGE,
						controlPts.get(i + 1).x + HALFRANGE, controlPts.get(i + 1).y + HALFRANGE);
			}
		}
	}

	/**
	 * drawing dash line
	 * 
	 * @param g
	 * @param pX1
	 * @param pY1
	 * @param pX2
	 * @param pY2
	 */
	protected void drawDashLine(Graphics g, int pX1, int pY1, int pX2, int pY2) {
		// TODO Auto-generated method stub
		final float dash = 8;
		double x, y;
		if (pX1 == pX2) {
			if (pY1 > pY2) {
				int tmp = pY1;
				pY1 = pY2;
				pY2 = tmp;
			}
			y = (double) pY1;
			while (y < pY2) {
				double y0 = Math.min(y + dash, (double) pY2);
				g.drawLine(pX1, (int) y, pX2, (int) y0);
				y = y0 + dash;
			}
			return;
		} else if (pX1 > pX2) {
			int tmp = pX1;
			pX1 = pX2;
			pX2 = tmp;
			tmp = pY1;
			pY1 = pY2;
			pY2 = tmp;
		}
		double ratio = 1.0 * (pY2 - pY1) / (pX2 - pX1);
		double ang = Math.atan(ratio);
		double xinc = dash * Math.cos(ang);
		double yinc = dash * Math.sin(ang);
		x = (double) pX1;
		y = (double) pY1;
		while (x <= pX2) {
			double x0 = x + xinc;
			double y0 = y + yinc;
			if (x0 > pX2) {
				x0 = pX2;
				y0 = y + ratio * (pX2 - x);
			}
			g.drawLine((int) x, (int) y, (int) x0, (int) y0);
			x = x0 + xinc;
			y = y0 + yinc;
		}
	}

	/**
	 * the method is invoked by drawCurve() to get power of each control point
	 * 
	 * @param u
	 * @param pNum
	 * @param i
	 * @return
	 */
	public double parameterBezier(double u, int pNum, int i) {
		return getBinomial(pNum, i) * Math.pow(1 - u, pNum - i) * Math.pow(u, i);
	}

	/**
	 * the method is to get the binomial
	 * 
	 * @param pNum
	 * @param i
	 * @return
	 */
	private double getBinomial(int pNum, int i) {
		// TODO Auto-generated method stub
		if (i < 0)
			return 0;
		else if (i > pNum)
			return 0;
		else
			return (getFactorial(pNum) / (getFactorial(i) * getFactorial(pNum - i)));
	}

	/**
	 * the method is to calculate the factorial
	 * 
	 * @param pNum
	 * @return
	 */
	private int getFactorial(int pNum) {
		// TODO Auto-generated method stub
		if (pNum == 0) {
			return 1;
		}
		if (pNum == 1) {
			return 1;
		} else
			return pNum * getFactorial(pNum - 1);
	}

	/**
	 * show the sample points by invoke drawCurve() to draw the curve again
	 * 
	 * @param n
	 */
	public void showSam(int n) {
		// TODO Auto-generated method stub
		samNum = n;
		clear();
		if (controlPts.size() >= 2) {
			drawCurve();
		}
		drawControlPts();
	}

	private double distance(int pX1, int pX2, int pY1, int pY2) {
		// TODO Auto-generated method stub
		double result = Math.sqrt((Math.pow((pX2 - pX1), 2)) + (Math.pow(pY1 - pY2, 2)));
		return result;
	}

	public void drawControlPts() {
		// TODO Auto-generated method stub
		for (Point p : controlPts) {
			drawPts(p);
		}
	}

	public void drawPts(Point p) {
		g.setColor(Color.black);
		g.fillOval(p.x, p.y, RANGE, RANGE);
	}

	public void addPts(int x, int y) {
		// TODO Auto-generated method stub
		if (allowAddPts) {
			clear();
			controlPts.add(new Point(x, y));
			if (controlPts.size() >= 2) {
				drawCurve();
			}
			if (controlPts.size() >= MAX_CONTROL_POINTS) {
				allowAddPts = false;
			}
			drawControlPts();
		}
	}

	/**
	 * remove the control points to change the curve
	 * 
	 * @param p
	 */
	public void removePoint(Point p) {
		// TODO Auto-generated method stub
		clear();
		controlPts.remove(p);
		controlPts.trimToSize();
		allowAddPts = true;
		drawCurve();
		drawControlPts();
	}

	/**
	 * check the number of added control points, it must be less than 13
	 * 
	 * @param x
	 * @param y
	 * @param theList
	 * @return
	 */
	public Point checkPoint(int x, int y, ArrayList<Point> theList) {
		// TODO Auto-generated method stub
		for (Point p : theList) {
			if (distance(x, p.x, y, p.y) <= RANGE) {
				return p;
			}
		}
		return null;
	}

	/**
	 * reset the canvas,clear all
	 * 
	 * @return
	 */
	public void update() {
		// TODO Auto-generated method stub
		controlPts.clear();
		allowAddPts = true;
		clear();
	}

	public void drawDash() {
		// TODO Auto-generated method stub
		showDash = true;
		drawCurve();
		drawControlPts();
	}

	public void drawTan() {
		// TODO Auto-generated method stub
		showTan = true;
		drawCurve();
		drawControlPts();
	}

	public void drawCurva() {
		// TODO Auto-generated method stub
		showCurva = true;
		drawCurve();
		drawControlPts();
	}

	public void clearTan() {
		// TODO Auto-generated method stub
		showTan = false;
		clear();
		drawCurve();
		drawControlPts();
	}

	public void clearCurva() {
		// TODO Auto-generated method stub
		showCurva = false;
		clear();
		drawCurve();
		drawControlPts();
	}

	public void clearDash() {
		// TODO Auto-generated method stub
		showDash = false;
		clear();
		drawCurve();
		drawControlPts();
	}

}
