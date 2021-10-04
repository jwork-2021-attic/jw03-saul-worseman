package example;

public class QuickSorter implements Sorter {

    private int[] a;

    public void load(int[] a) {
        this.a = a;
    }


    private void swap(int i, int j) {
        int temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        plan += "" + a[i] + "<->" + a[j] + "\n";
    }

    private String plan = "";

    private int partition(int pivot, int first, int last){
        int i = first;
        int j = last;
        while(i < j){
            while(a[j] > pivot)
                j--;
            swap(i, j);
            while(a[i] < pivot)
                i++;
            swap(i, j);
        }
        return i;
    }

    private void quickSort(int first, int last){
        if(first < last){
            int pivot = a[first];
            int splitPoint = partition(pivot, first, last);
            quickSort(first, splitPoint - 1);
            quickSort(splitPoint + 1, last); 
        }
    }

    @Override
    public void sort() {
        quickSort(0, a.length - 1);
    }

    @Override
    public String getPlan() {
        //System.out.println(plan);
        return this.plan;
    }

    public static void main(String[] args){

    }
}
 