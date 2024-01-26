import java.util.Arrays;

public class SegmentTree {
    int length;
    int[] segmentTree;
    int[] nums;
    int n;

    public SegmentTree(int[] nums) {
        this.nums = nums;
        n = nums.length;
        if ((n != 1) && ((n & (n - 1)) == 0)) {
            length = n * 2 - 1;
        } else {
            int power = 1;
            while (power < n) {
                power *= 2;
            }
            length = power * 2 - 1;
        }
        segmentTree = new int[length];
        Arrays.fill(segmentTree, Integer.MAX_VALUE);
        buildTree(0, n - 1, 0);
    }

    public void buildTree(int low, int high, int position) {
        if (low == high) {
            segmentTree[position] = nums[low];
            return;
        }
        int mid = low + (high - low) / 2;

        buildTree(low, mid, 2 * position + 1);
        buildTree(mid + 1, high, 2 * position + 2);
        segmentTree[position] = Math.min(segmentTree[2 * position + 1], segmentTree[2 * position + 2]);
    }

    public void update(int index, int val) {
        updateTree(0, n - 1, 0, index, val);
    }

    public void updateTree(int low, int high, int position, int index, int val) {
        // If the index is outside the range of this segment
        if (index < low || index > high) {
            return;
        }

        // If the current segment is a leaf node
        if (low == high) {
            nums[index] = val;
            segmentTree[position] = val;
            return;
        }

        // If the index is within the range of this segment, update both left and right subtrees
        int mid = low + (high - low) / 2;
        updateTree(low, mid, 2 * position + 1, index, val);
        updateTree(mid + 1, high, 2 * position + 2, index, val);

        // Update the current segment value after updating the subtrees
        segmentTree[position] = Math.min(segmentTree[2 * position + 1], segmentTree[2 * position + 2]);
    }

    public int getMinValue() {
        return segmentTree[0];
    }

    public int getMinInRange(int queryLow, int queryHigh) {
        return getMinInRangeHelper(0, n - 1, 0, queryLow, queryHigh);
    }

    private int getMinInRangeHelper(int low, int high, int position, int queryLow, int queryHigh) {
        // If the current segment is completely outside the query range
        if (low > queryHigh || high < queryLow) {
            return Integer.MAX_VALUE; // Assuming positive integers in the array
        }
        // If the current segment is completely inside the query range
        if (low >= queryLow && high <= queryHigh) {
            return segmentTree[position];
        }

        // If the current segment partially overlaps with the query range
        int mid = low + (high - low) / 2;
        int leftMin = getMinInRangeHelper(low, mid, 2 * position + 1, queryLow, queryHigh);
        int rightMin = getMinInRangeHelper(mid + 1, high, 2 * position + 2, queryLow, queryHigh);

        return Math.min(leftMin, rightMin);
    }

}
