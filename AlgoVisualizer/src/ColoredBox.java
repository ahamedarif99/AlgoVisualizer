import java.awt.Color;
/**
 * DO NOT CHANGE THIS FILE
 * 
 * This class represents a single colored box. These
 * are the objects that we're sorting. Note that this
 * class implements Comparable.
 */
public class ColoredBox implements Comparable
{
	// INSTANCE VARIABLES
	private int num;
	private Color color;

	/**
	 * Our only constructor
	 */
	public ColoredBox(int initNum, Color initColor)
	{
		num = initNum;
		color = initColor;
	}

	// ACCESSOR METHODS
	public int getNum() 	{ return num; 	}
	public Color getColor() { return color; }

	/**
	 * This method provides a means for comparing
	 * two ColoredBox objects such that we may
	 * sort them.
	 */
	public int compareTo(Object obj)
	{
		ColoredBox otherBox = (ColoredBox)obj;
		if (num < otherBox.num)
			return -1;
		else if (num == otherBox.num)
			return 0;
		else
			return 1;
	}
}