

public class RandomSortBoxSorter implements BoxSorter
{

	public void sortBoxes(ColoredBox[] boxes) 
	{
			while(Sorted(boxes) == false)
			{
				int one = (int) (Math.random() * boxes.length);
				int two = (int) (Math.random() * boxes.length);
				ColoredBox temp = boxes[one];
				boxes[one] = boxes[two];
				boxes[two] = temp;
				BoxSortingDemo.updateDisplay();
			}
		
		
	}
	
	public boolean Sorted(ColoredBox[] boxes)
	{
		boolean sorted = true;
		for(int ii = 0; ii < boxes.length - 1; ii++)
		{
			if(boxes[ii].compareTo(boxes[ii+1]) == 1)
			{
				return sorted = false;
			}
		}
		return sorted;
	}
}
