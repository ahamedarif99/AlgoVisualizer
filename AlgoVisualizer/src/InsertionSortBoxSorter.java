
public class InsertionSortBoxSorter implements BoxSorter
{

	
	public void sortBoxes(ColoredBox[] boxes) 
	{
		for(int i = boxes.length - 1; i >= 0; i--)
		{
			ColoredBox key = boxes[i];
			int j = i + 1;
			
			while((j < boxes.length) && (boxes[j].compareTo(key) == -1))
			{
				boxes[j - 1] = boxes[j];
				j = j + 1;
			}
			boxes[j - 1] = key;
			BoxSortingDemo.updateDisplay();
		}
		
	}

}
