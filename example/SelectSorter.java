package example;

public class SelectSorter implements Sorter {

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

    @Override
    public void sort() {
        for(int i = 0; i < a.length; i++){
            int idx = i;
            for(int j = i; j < a.length; j++){
                if(a[idx] > a[j]){
                    idx = j;
                    }
                }
            swap(idx,i);
            }
        }

    @Override
    public String getPlan() {
        //System.out.println(plan);
        return this.plan;
    }

    public static void main(String[] args){

    }
}
 