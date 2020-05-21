import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
/**
 * DO NOT CHANGE THIS FILE
 * 
 * This application provides a Graphical User Interface
 * to help one visualize various sorting algorithms. Note
 * that in order to properly use this application one must
 * provide two command-line arguments. The first one being
 * the name of the class to perform the sorting and the
 * second one being the size of the array to be sorted.
 */
public class BoxSortingDemo extends JFrame
{
	// THIS CLASS IS A SINGLETON
	private static BoxSortingDemo singleton = null;
	
	// THIS OBJECT WILL DO ALL OF OUR SORTING FOR US
	// NOTE THAT BoxSorter IS AN INTERFACE, SO THIS
	// IS SIMPLY THE APPARENT TYPE
	private BoxSorter boxSorter;
	private static int size;

	// GUI COMPONENTS
	private JPanel northPanel;
	private JButton resetButton;
	private JButton sortButton;
	
	private JPanel southPanel;
	private JLabel sortingSpeedLabel;
	private JPanel speedPanel;
	private JButton speedUpButton;
	private JButton slowDownButton;
	
	// EVENT HANDLERS
	private ResetHandler resetHandler;
	private SortHandler sortHandler;
	private SpeedUpHandler speedUpHandler;
	private SlowDownHandler slowDownHandler;
	
	// OUR DRAWING SURFACE
	private ColoredBoxPanel coloredBoxPanel;
	
	// HERE'S THE ARRAY WE'LL BE SORTING
	private ColoredBox[] coloredBoxes;
	
	// SWAPS PER SECOND FOR SHOWING SORTING
	private static int swapsPerSecond = 5;
	private static long sleepTime = 200;
	
	// FOR PRESENTING REAL NUMBERS NEATLY
	private NumberFormat numberFormatter;
	
	/*
	 * This constructor initializes our application.
	 */
	private BoxSortingDemo(String title, BoxSorter initBoxSorter)
	{
		super(title + " Sort");
		boxSorter = initBoxSorter;
		setSize(1200,200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		layoutGUI();
		singleton = this;
	}
	
	/**
	 * Accessor method for our singleton.
	 */
	public static BoxSortingDemo getBubbleSortDemo()
	{
		return singleton;
	}
	
	/**
	 * This method lays out all GUI components and sets
	 * up all event handlers.
	 */
	public void layoutGUI()
	{
		// CONSTRUCT GUI COMPONENTS
		northPanel = new JPanel();
		resetButton = new JButton("Reset");
		sortButton = new JButton("Sort");
		
		southPanel = new JPanel();
		sortingSpeedLabel = new JLabel();
		numberFormatter = NumberFormat.getNumberInstance();
		numberFormatter.setMaximumFractionDigits(1);
		numberFormatter.setMinimumFractionDigits(0);
		updateSortingSpeedLabel();
		speedPanel = new JPanel();
		speedUpButton = new JButton("+");
		slowDownButton = new JButton("-");
		
		// MAKE OUR RENDERING SURFACE
		coloredBoxPanel = new ColoredBoxPanel();

		// CONSTRUCT OUR EVENT HANDLERS
		resetHandler = new ResetHandler();
		sortHandler = new SortHandler();
		speedUpHandler = new SpeedUpHandler();
		slowDownHandler = new SlowDownHandler();
		
		// ARRANGE THE COMPONENTS IN THE NORTH PANEL
		northPanel.add(resetButton);
		northPanel.add(sortButton);
		GridLayout gridLayout = new GridLayout(2,1);
		speedPanel.setLayout(gridLayout);
		speedPanel.add(speedUpButton, 0, 0);
		speedPanel.add(slowDownButton, 0, 1);
		southPanel.add(sortingSpeedLabel);
		southPanel.add(speedPanel);

		// ARRANGE THE PANELS IN THSI FRAME
		this.add(northPanel, BorderLayout.NORTH);
		this.add(coloredBoxPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		
		// REGISTER THE EVENT HANDLERS
		resetButton.addActionListener(resetHandler);
		sortButton.addActionListener(sortHandler);
		speedUpButton.addActionListener(speedUpHandler);
		slowDownButton.addActionListener(slowDownHandler);
	}

	/**
	 * This thread allows rendering to happen in
	 * a separate thread so that the GUI continues
	 * to work for the user.
	 */
	class RenderThread implements Runnable
	{
		Thread proxyThread = null;
		public void start()
		{
			if (proxyThread == null)
				proxyThread = new Thread(this);
			proxyThread.start();
		}
		
		public void run()
		{
			singleton.coloredBoxPanel.repaint();
		}
	}

	/**
	 * This method is for setting up the thread
	 * used for rendering.
	 */
	public void initNewRenderThread()
	{
		RenderThread rt = new RenderThread();
		rt.start();
	}

	/**
	 * This method is called when the user wishes to
	 * speed up or slow down the animation speed.
	 */
	public void updateSwapsPerSecond(int initSwapsPerSecond)
	{
		if ((initSwapsPerSecond > 0) && (initSwapsPerSecond <= 10))
		{
			swapsPerSecond = initSwapsPerSecond;
			sleepTime = (long)(1000.0/initSwapsPerSecond);
		}
	}

	/**
	 * Speed up sorting.
	 */
	public void incSortingSpeed()
	{
		updateSwapsPerSecond(swapsPerSecond + 1);
	}

	/**
	 * Slow down sorting.
	 */
	public void decSortingSpeed()
	{
		updateSwapsPerSecond(swapsPerSecond - 1);
	}

	/**
	 * After each sorting change, this method should
	 * be called to update the GUI.
	 */
	public static void updateDisplay()
	{
		singleton.initNewRenderThread();
		try
		{
			System.out.println(sleepTime);
			Thread.sleep(sleepTime);
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}

	/**
	 * This is for redrawing the speed label.
	 */
	public void updateSortingSpeedLabel()
	{
		sortingSpeedLabel.setText("Current Swap Speed: " 
								+ swapsPerSecond 
								+ " per Second");		
	}

	/**
	 * This method makes a new random array and
	 * then forces its rendering.
	 */
	public void resetData()
	{
		coloredBoxes = new ColoredBox[size];
		int randomRed;
		for (int i = 0; i < size; i++)
		{
			randomRed = (int)(Math.random() * 256);
			Color c = new Color(randomRed, 0, 0);
			coloredBoxes[i] = new ColoredBox(randomRed, c);
		}
		updateDisplay();
	}
	
	/**
	 * This panel provides all of the rendering
	 * of our boxes.
	 */
	class ColoredBoxPanel extends JPanel
	{
		static final int MARGIN = 25;
		static final int INDENT = 10;
		Font f = new Font("Serif", Font.BOLD, 16);

		/**
		 * Here's where all rendering is done.
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			int panelWidth = getWidth();
			setFont(f);
			if ((panelWidth > 0) && (BoxSortingDemo.this.coloredBoxes != null))
			{
				int inc = (panelWidth - (MARGIN * 2))/BoxSortingDemo.this.coloredBoxes.length;
				int x = MARGIN;
				int y = MARGIN;
				for (int i = 0; i < BoxSortingDemo.this.coloredBoxes.length; i++)
				{
					g.setColor(BoxSortingDemo.this.coloredBoxes[i].getColor());
					g.fill3DRect(x, y, inc, 100, true);
					g.setColor(Color.WHITE);
					g.drawString("" + BoxSortingDemo.this.coloredBoxes[i].getNum(), x + (inc/2)-10, y + 20);
					x += inc;
				}
			}
		}
	}

	/**
	 * This handles running the desired
	 * sorting algorithm in a separate thread.
	 */
	class SortingThread implements Runnable
	{
		private Thread proxyThread;
		
		public SortingThread()
		{
			proxyThread = new Thread(this);
		}
		
		public void start()
		{
			proxyThread.start();
		}
		
		public void run()
		{
			boxSorter.sortBoxes(coloredBoxes);
		}
	}

	/**
	 * This handler resets the array.
	 */
	class ResetHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			BoxSortingDemo.this.resetData();
		}
	}

	/**
	 * This handler starts sorting.
	 */
	class SortHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			SortingThread st = new SortingThread();
			st.start();
		}
	}

	/**
	 * This handler speeds up sorting.
	 */
	class SpeedUpHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			BoxSortingDemo.this.incSortingSpeed();
			BoxSortingDemo.this.updateSortingSpeedLabel();
		}
	}

	/**
	 * This handler slows down sorting.
	 */
	class SlowDownHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			BoxSortingDemo.this.decSortingSpeed();
			BoxSortingDemo.this.updateSortingSpeedLabel();
		}
	}

	/**
	 * Here's where our application starts. Note that
	 * it uses java reflection for dynamically constructing
	 * objects from class name.
	 */
	public static void main(String[] args)
	{
		BoxSorter sorter = null;
		if (args.length < 2)
		{
			JOptionPane.showMessageDialog(null, "Command Line Argument Missing - You Need to Specify the Sorting Algorithm and Array Size");
			System.exit(-1);
		}
		else
		{
			try
			{
				size = Integer.parseInt(args[1]);
				String className = args[0];
				
				// HERE GOES JAVA REFLECTION
				Class sortClass = Class.forName(className);
				sorter = (BoxSorter)sortClass.newInstance();
				BoxSortingDemo frame = new BoxSortingDemo(args[0], sorter);
				frame.setVisible(true);
			}
			catch(Exception ie)
			{
				JOptionPane.showMessageDialog(null, "Invalid Command Line Arguments\n\nMake sure you properly specify a valid sorting class name and number of boxes\n");
				System.exit(-1);				
			}
		}
	}
}