/**
 * YOU MUST DEFINE THIS CLASS
 * 
 * This class provides a reverse implementation
 * of the selection sort algorithm.
 * 
 * @author ____________
 */
public class SelectionSortBoxSorter implements BoxSorter
{
	public void sortBoxes(ColoredBox[] boxes) 
	{
		for (int i = boxes.length - 1; i > 0; i--)
		{
			int maxidx = i;
			for (int j = i - 1; j >= 0; j--)
			{
				if(boxes[j].compareTo(boxes[maxidx]) == 1)
				{
					maxidx = j;
				}
			}
			
			ColoredBox temp = boxes[maxidx];
			boxes[maxidx] = boxes[i];
			boxes[i] = temp;
			BoxSortingDemo.updateDisplay();
		}
	}
}

