package search;

public class BinarySearch {
    // :NOTE: контракт? (*fixed*)
    // Pred:  x = args[0] && arr = args[1..args.length - 1] && forall i in 0..arr.length - 2 arr[i+1] >= arr[i]
    // Post:  out -> i: i = min(0..arr.length - 1) that arr[i] <= x
    public static void main(String[] args) {
        int[] arr = new int[args.length - 1];
        int x = Integer.parseInt(args[0]);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(args[i + 1]);
        }
        // Pred: arr sorted by descending(forall i = 0..n - 2: arr[i] >= a[i - 1])
        int rIterative = iterativeBinarySearch(arr, x);
        int rRecursive = recursiveBinarySearch(arr, x);
        System.out.println(rRecursive == rIterative ? rRecursive : "No.");
        // Post: 0 <= R <= arr.length && R == min(i): arr[i] <= x
    }

    // Pred: arr sorted by descending(forall i = 0..n - 2: arr[i] >= a[i - 1])
    // Post: 0 <= R <= arr.length && R == min(i): arr[i] <= x
    public static int recursiveBinarySearch(int[] arr, int x) {
        //return binarySearch on all array [0, arr.length)
        return recursiveBinarySearch(arr, -1, arr.length, x);
    }

    // :NOTE: R надо определить перед использованием, l r могут быть любыми?(*fixed*)
    // Pred: arr sorted by descending(forall i = 0..n - 2: arr[i] >= a[i - 1) && l <= l' < R <= r' <= r
    //       && -1 <= l && r <= arr.length
    // Post: 0 <= R <= arr.length && R == min(i): arr[i] <= x
    private static int recursiveBinarySearch(int[] arr, int l, int r, int x) {
        // arr sorted by descending && l <= l' < R <= r' <= r
        if (l < r - 1) {
            // arr sorted by descending && l <= l' < R <= r' <= r && r - l > 1
            int m = l + (r - l) / 2;
            // m == (l + r) / 2 -> m in [l', r']
            if (arr[m] > x) {
                // (arr sorted by descending && l <= l' < R <= r' <= r && r - l > 1 && arr[m] > x) -> R in (m, r'], l' = m
                return recursiveBinarySearch(arr, m, r, x);
                // R in (m, r'] -> recursive find answer in this range
            } else {
                // (arr sorted by descending && l <= l' < R <= r' <= r && r - l > 1 && arr[m] <= x) -> R in (l', m], r' = m
                return recursiveBinarySearch(arr, l, m, x);
                // R in (l', m] -> recursive find answer in this range
            }
        }
        // (r - l == 1 && arr sorted by descending && l <= l' < R <= r' <= r) -> R == r'
        return r;
    }

    // Pred: arr sorted by descending(forall i = 0..n - 2: arr[i] >= a[i - 1])
    // Post: 0 <= R <= arr.length && R == min(i): arr[i] <= x
    public static int iterativeBinarySearch(int[] arr, int x) {

        int l = -1;
        int r = arr.length;
        int m;

        // l == -1 && r == arr.length && arr sorted by descending
        // inv: l <= l' && r' <= r && l' < R <= r' && R in (l', r']
        while (l < r - 1) {
            // r - l > 1 && l <= l' && r' <= r
            m = l + (r - l) / 2;
            // m == (r' + l') / 2 -> m in (l', r']

            // :NOTE: m = l' (*fixed*)

            //P': arr sorted by descending

            if (arr[m] <= x) {
                // P' && arr[m] <= x -> R in (l', m]
                r = m;
                // :NOTE: r < l -> [l', r') из инварианта не существует (*fixed*)
                // r'' = m
                // r'' < r'
                // r' = r''
            } else {
                // P' && arr[m] > x -> R in (m, r']
                l = m;
                // l'' = m
                // l'' > l'
                // l' = l''
            }
        }
        // (r - l == 1 && l <= l' && r' <= r && R in (l', r']) -> R == r'
        return r;
    }
}