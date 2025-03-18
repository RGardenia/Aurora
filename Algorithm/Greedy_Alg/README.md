# 贪心算法

## 分发饼干

### 来源

leetcode.455 [分发饼干](https://leetcode-cn.com/problems/assign-cookies/)

### 描述

假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。

对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；并且每块饼干 j，都有一个尺寸 s[j] 。如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i ，这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。


示例 1:

```bash
输入: g = [1,2,3], s = [1,1]
输出: 1
解释: 
你有三个孩子和两块小饼干，3个孩子的胃口值分别是：1,2,3。
虽然你有两块小饼干，由于他们的尺寸都是1，你只能让胃口值是1的孩子满足。
所以你应该输出 1
```

示例 2:

```bash
输入: g = [1,2], s = [1,2,3]
输出: 2
解释: 
你有两个孩子和三块小饼干，2个孩子的胃口值分别是1,2。
你拥有的饼干数量和尺寸都足以让所有孩子满足。
所以你应该输出2.
```

### 思考

使用贪心算法，把最大的饼干给最贪心的小朋友，让他开心

![image-20201017221724239](images/image-20201017221724239.png)

然后剩下的最大的，在继续分配

![image-20201017221742375](images/image-20201017221742375.png)

因为贪心算法总是涉及到最大值和最小值，所以贪心算法和排序是分不开的

### 代码

```bash
class Solution(object):
    def findContentChildren(self, g, s):
        """
        :type g: List[int]
        :type s: List[int]
        :rtype: int
        """
        g = sorted(g, reverse=True)
        s = sorted(s, reverse=True)

        # 饼干索引
        si = 0
        # 贪心索引
        gi = 0
        # 几个小朋友开心
        res = 0
        while gi < len(g) and si < len(s):
            if s[si] >= g[gi]:
                # 如果当前的饼干能够满足最贪心的小朋友，那么就进行分配
                res = res + 1
                si = si + 1
                gi = gi + 1
            else:
                gi = gi + 1
        return res

if __name__ == '__main__':
    print(Solution().findContentChildren([1,5,4,2], [2,1,7,6]))
```

这里的时间复杂度就是 `O(n*logn)`，最要体现在排序方面，也就是使用快排进行排序





## 小Ａ的糖果

题目要求：

小 A 有 n 个糖果盒，第 i 个盒中有 ai 颗糖果。

小 A 每次可以从其中一盒糖果中吃掉一颗，他想知道，要让任意两个相邻的盒子中糖的个数之和都不大于 x，至少得吃掉几颗糖。

```c++
#include<bits/stdc++.h>
using namespace std;
long long sum;
int n,x,a[100010];
int main()
{
	cin>>n>>x;
	for(int i=1;i<=n;i++)
	{
		cin>>a[i];	
	}
	for(int i=1;i<n;i++)
	{
	    if(a[i]+a[i+1]>x)
		{
        	sum += a[i+1] - x+a[i];
        	a[i+1] = x-a[i];
    	}
	}
	cout<<sum;
	return 0;   
}
```





## [跳跃游戏](https://leetcode.cn/problems/jump-game/)

给你一个非负整数数组 `nums` ，你最初位于数组的 **第一个下标** 。数组中的每个元素代表你在该位置可以跳跃的最大长度。

判断你是否能够到达最后一个下标，如果可以，返回 `true` ；否则，返回 `false` 。

**示例 1：**

```
输入：nums = [2,3,1,1,4]
输出：true
解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。
```

**示例 2：**

```
输入：nums = [3,2,1,0,4]
输出：false
解释：无论怎样，总会到达下标为 3 的位置。但该下标的最大跳跃长度是 0 ， 所以永远不可能到达最后一个下标。
```

**提示：**

- `1 <= nums.length <= 104`
- `0 <= nums[i] <= 105`



```python
class Solution:
    def canJump(self, nums: List[int]) -> bool:
        max_reach = 0
        for i in range(len(nums)):
            if i > max_reach:
                return False
            max_reach = max(max_reach, i + nums[i]) # 贪
            if max_reach >= len(nums) - 1:
                return True
        return True
```







