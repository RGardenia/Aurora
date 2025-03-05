# Binary Search









```go
// > ==> (>= x + 1)
lower_bound(nums, target + 1)

// < ==> (>= x) - 1
lower_bound(nums, target) - 1

// <= ==> (> x) - 1
lower_bound(nums, target + 1) - 1
```



```go
// 要求 nums 是非递减的，即 nums[i] <= nums[i+1]
// 返回最小的满足 nums[i] >= target 的 i
// 如果不存在，返回 len(nums)
func lower_bound(nums []int, target int) (int) {
    left, right := 0, len(nums) - 1   // 左闭右闭区间
    for left <= right {
        mid := left + (right - left) / 2
        if nums[mid] < target {
            left = mid + 1	// [mid + 1, right]
        } else {
            right = mid - 1   // [left, mid - 1]
        }
    }
    return left
}

func lower_bound(nums []int, target int) (int) {
    left, right := 0, len(nums)   // 左闭右开区间
    for left < right {
        mid := left + (right - left) / 2
        if nums[mid] < target {
          left = mid + 1	// [mid + 1, right)
        } else {
          right = mid   // [left, mid)
        }
    }
    return left   // right
}

func lower_bound(nums []int, target int) (int) {
    left, right := -1, len(nums) - 1   // 左开右开区间
    for left + 1 < right {
        mid := left + (right - left) / 2
        if nums[mid] < target {
            left = mid	// (mid + 1, right)
        } else {
            right = mid   // (left, mid)
        }
    }
    return right
}
```

