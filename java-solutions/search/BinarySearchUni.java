package search;

import base.Asserts;

public class BinarySearchUni {
    // :NOTE: нет контракта
    // n = arr.length
    // Pred: forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted by descending
    // && Integer.Min_Value <= sum of arr <= Integer.Max_Value
    // Post: out -> Z
    public static void main(String[] args) {
        //creating of array by args length
        int[] arr = new int[args.length];
        // reading of array
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }
        // Pred: forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted by descending
        // && Integer.Min_Value <= sum of arr <= Integer.Max_Value
        int res = Uni(arr);
        System.out.println(res);
        // Post: 0 <= R < arr.length && arr[0..Z].length == R && calc it recursive if sum of arr % 2 == 0 else iterative
    }

    // Pred: Integer.Min_Value <= sum of arr <= Integer.Max_Value
    // Post: forall i = 0..n-1 sum(a[i])
    private static int sum(int[] arr) {
        // true
        int sum = 0;
        // sum all elements
        for (int i : arr) {
            sum += i;
        }
        return sum;
    }

    // Pred: forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted
    // Post: 0 <= R <= arr.length && arr[0..Z].length == R && calc it recursive if sum of arr % 2 == 0 else iterative
    public static int Uni(int[] arr) {
        //forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted
        if (sum(arr) % 2 == 0) {
            // forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted && sum(arr) % 2 == 0 -> calc it
            // recursive
            return recUni(arr, 0, arr.length - 1);
        } else {
            // forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted && sum(arr) % 2 == 1 -> calc it
            // iterative
            return ittUni(arr);
        }
    }

    // :NOTE: нет контракта для l, r (*fixed*)
    // Pred: forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted && 0 <= l && r < arr.length
    // Post: 0 <= R < arr.length && arr[0..Z].length == R
    private static int recUni(int[] arr, int l, int r) {
        // l < l' <= R <= r' < r
        if (r - l <= 3) {
            for (int i = l; i < r && i < arr.length - 1; i++) {
                // r - l <= 3 && l < l' <= i <= R <= r' < r
                if (arr[i] > arr[i + 1]) {
                    // r - l <= 3 && l < l' <= i <= R <= r' < r && arr[i] > arr[i + 1] -> Z == i -> R == i
                    return i;
                }
            }
            // R not in [l', r) && (l < l' <= R <= r' < r && r - l == 0) -> R == r'
            return r;
        }
        int a = l + (r - l) / 3;
        int b = r - (r - l) / 3;
        // forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted && a == l + (r - l) / 3
        // && b == r - (r - l) / 3 && r - l > 0
        if (arr[a] > arr[b]) {
            // (forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted && a == l' + (r' - l') / 3
            // && b == r' - (r' - l') / 3 && r' - l' >= 0 && arr[a] > arr[b]) -> R in [l', b]
            // r'' = b
            // r' > r''
            // r' = r''
            return recUni(arr, l, b);
        } else {
            // (forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted && a == l' + (r' - l') / 3
            // && b == r' - (r' - l') / 3 && r' - l' >= 0 && arr[a] > arr[b]) -> R in [a, r']
            return recUni(arr, a, r);
            // l'' = a
            // l' < l''
            // l' = l''
        }
    }

    // Post: forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted
    // Pred: 0 <= R < arr.length && arr[0..Z].length == R
    private static int ittUni(int[] arr) {
        // forall i = 0 .. Z - 1 arr sorted by increasing and j = Z .. n - 1 sorted
        int l = 0;
        int r = arr.length - 1;
        int a, b;
        // l < l' <= R <= r' < r
        while (r - l > 3) {
            // l < l' <= R <= r' < r && r - l > 3
            a = l + (r - l) / 3; // (r + 2l) / 3
            b = r - (r - l) / 3; // (2r + l) / 3 = l -> r = l
            // a == l' + (r' - l') / 3 -> a in [l', r']
            // b == r' - (r' - l') / 3 -> b in [l', r']

            // :NOTE: b = l'
            if (arr[a] > arr[b]) {
                // (l < l' <= R <= r' < r && r - l > 3 && arr[a] > arr[b]) -> R in [l', b)
                r = b;
                // :NOTE: r = b - 1 = l - 1 -> r < l
                // r'' = b
                // r' > r''
                // r' = r''
            } else {
                // (l < l' <= R <= r' < r && r - l > 3 && arr[a] <= arr[b]) -> R in [a, r')
                l = a;
                // l'' = a
                // l' < l''
                // l' = l''
            }
        }
        for (int i = l; i < r && i < arr.length - 1; i++) {
            // r - l <= 3 && l < l' <= i <= R <= r' < r
            if (arr[i] > arr[i + 1]) {
                // r - l <= 3 && l < l' <= i <= R <= r' < r && arr[i] > arr[i + 1] -> Z == i -> R == i
                return i;
            }
        }
        // R not in [l', r) && (l < l' <= R <= r' < r && r - l == 0) -> R == r'
        return r;
    }

}