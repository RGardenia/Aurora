# 全排列问题



要求：

按照字典序输出自然数 1 到 n 所有不重复的排列，即 nn 的全排列，要求所产生的任一数字序列中不允许出现重复的数字。

```c++
#include<bits/stdc++.h>
using namespace std;

int n,pd[100],a[100];

void dfs(int k) {
    if(k==n) {
        for(int i=1;i<=n;i++) {
            printf("%5d",a[i]);
        }
        printf("\n");
        return ;
    }
    for(int i=1;i<=n;i++) {
        if(!pd[i]) {
            pd[i]=1;
            a[k+1]=i;
            dfs(k+1);
            pd[i]=0;
        }
    }
}
int main() {
    cin>>n;
    dfs(0);
    return 0;
}
```

