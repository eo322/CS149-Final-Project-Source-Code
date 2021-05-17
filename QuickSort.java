import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class QuickSort extends RecursiveAction {
	private static int THRESHOLD = 100;
	private int[] array;
	private int left;
	private int right;
	private int pivotIndex;
	
	public QuickSort(int[] array, int left, int right) {
		super();
		this.array = array;
		this.left = left;
		this.right = right;
		this.pivotIndex = left + (right-left)/2;
	}
	
	static void execute(int[] array, int left, int right) {
		ForkJoinPool pool = new ForkJoinPool();
		QuickSort task = new QuickSort(array, 0, array.length-1);
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

    public static void sort(int[] array, int left, int right) {
		if (left < right) {
			int pivotIndex = left + (right-left)/2;//pivot generation
			int pivotNewIndex = partition(array, left, right, pivotIndex);//partitioning
			//recursively sorting sublists
			sort(array, left, pivotNewIndex - 1);
			sort(array, pivotNewIndex + 1, right);
		}
	}
	
	public void compute() {
		if (right - left < THRESHOLD) {
			QuickSort.selectionSort(array);
		} else if (left < right) {
			int pivotNewIndex = partition(array, left, right, pivotIndex);
			
			QuickSort worker1 = new QuickSort(array, left, pivotNewIndex - 1);
			QuickSort worker2 = new QuickSort(array, pivotNewIndex + 1, right);
			invokeAll(worker1, worker2);
		}
	}
	
	static int partition(int[] array, int left, int right, int pivotIndex) {
		int pivotValue = array[pivotIndex];
		
		swap(array, pivotIndex, right);
		int storeIndex = left;
		
		for (int i = left; i < right; i += 1) {
			if (array[i] < pivotValue) {
				swap(array, i, storeIndex);
				storeIndex += 1;
			}
		}
		swap(array, storeIndex, right);
		return storeIndex;
	}
	
	static void swap(int[] array, final int a, final int b) {
		final int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
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