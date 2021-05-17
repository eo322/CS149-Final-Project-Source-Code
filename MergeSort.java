import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSort extends RecursiveAction {
	private static int THRESHOLD = 100;
	private final int[] array;
	private final int[] tmp;
	private int left;
	private int right;
	
	public MergeSort(int[] array, int[] tmp, int left, int right) {
		this.array = array;
		this.tmp = tmp;
		this.left = left;
		this.right = right;
	}
	
	static void execute(int[] array, int left, int right) {
		ForkJoinPool pool = new ForkJoinPool();
		int[] tmp = new int[array.length];
		MergeSort task = new MergeSort(array, tmp, 0, array.length-1);
		pool.invoke(task);
	}
	
	public static void selectionSort(int[] array)
    {
        int n = array.length;
  
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
			{
				if (array[j] < array[min_idx])
				{
					min_idx = j;
				}
			}
            int temp = array[min_idx];
            array[min_idx] = array[i];
            array[i] = temp;
        }
	}

  public static void mSort(int[] array, int[] tmp, int left, int right) {
		if (left >= right) 
			return;

		int middle = left + (right-left) /2;//divides itself into two havles
		//calls itself on the two halves
		mSort(array, tmp, left, middle);
		mSort(array, tmp, middle + 1, right);
		//merges the two sorted halves
		merge(array, tmp, left, right);
	}

  public static void merge(int[] array, int[] tmp, int left, int right) {
		/* copy active part of array to tmp */
		for (int i = left; i <= right; i++)
			tmp[i] = array[i];

		/* merge lists */
		int k = left;
		int mid = left + (right-left) / 2;
		int j = mid+1;
		while (k <= right) {
			if (left > mid)
				array[k++] = tmp[j++];
			else if (j > right)
				array[k++] = tmp[left++];
			else if (tmp[left] < tmp[j])
				array[k++] = tmp[left++];
			else
				array[k++] = tmp[j++];
		}
	}

	protected void compute() {
		if (left >= right) { 
			return;
		} else if (right - left < THRESHOLD) {
			MergeSort.selectionSort(array);
		} else {
			int middle = left + (right-left) /2;

			MergeSort worker1 = new MergeSort(array, tmp, left, middle);
			MergeSort worker2 = new MergeSort(array, tmp, middle + 1, right);
			invokeAll(worker1, worker2);
			
			MergeSort.merge(array, tmp, left, right);
		}
	}

	public static void main(String[] args)
	{
	  int[] unsorted = {7,12,19,3,18,4,2,6,15,8 };
	  System.out.print("original list:");
	  for (int i =0; i <10; i++)
	  {
		System.out.print(unsorted[i] + " ");
	  }
	  System.out.print("\n");
	  System.out.print("\n");
	  execute(unsorted, 0, unsorted.length-1);
	  System.out.print("sorted list:");
	  for (int i =0; i <10; i++)
	  {
		System.out.print(unsorted[i] + " ");
	  }
	}

}