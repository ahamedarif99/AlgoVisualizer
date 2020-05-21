
public class BubbleSortBoxSorter implements BoxSorter {
	
	public void sortBoxes(ColoredBox[] boxes) 
	{
		for (int i = boxes.length-1; i > 0 ; i--) 
		{
            for (int j = boxes.length-i-1; j >= 0; j--) 
            {
				if(boxes[j].compareTo(boxes[j + 1]) == 1)
				{
					ColoredBox temp = boxes[j];
					boxes[j] = boxes[j + 1];
					boxes[j + 1] = temp;
					BoxSortingDemo.updateDisplay();
				}
			}
		}
	}
}
