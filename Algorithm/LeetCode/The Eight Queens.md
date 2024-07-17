# 八皇后



​	八皇后问题，是一个古老而著名的问题，是回溯算法的典型例题。该问题是十九世纪著名的数学家高斯1850年提出：在8×8格的国际象棋上摆放八个皇后，使其不能互相攻击，即任意两个皇后都不能处于同一行、同一列或同一斜线上，问有多少种摆法并输出每一种摆法。

![29035e22acd844fe8ab6217fe6aaa173.gif](images/29035e22acd844fe8ab6217fe6aaa173.gif)

```C++
#include<bits/stdc++.h>
using namespace std;

int a[100],b[100],c[100],d[100];
//a数组表示的是行；
//b数组表示的是列；
//c表示的是左下到右上的对角线；
//d表示的是左上到右下的对角线；
int n,sum;

int print() {
    if(sum<=2) {
        for(int k=1;k<=n;k++) {	cout<<a[k]<<" "; }
        cout<<endl;
    }
    sum++;
}
void queen(int i) {
    if(i>n) {
        print();
        return;
    } else {
        for(int j=1;j<=n;j++) {
            if((!b[j])&&(!c[i+j])&&(!d[i-j+n])) {
                a[i]=j;
                b[j]=1;
                c[i+j]=1;
                d[i-j+n]=1;
                queen(i+1);
                b[j]=0;
                c[i+j]=0;
                d[i-j+n]=0;
            }
        }
    }
}
int main() {    
    cin >> n;
    queen(1);
    cout << sum;
    return 0;
}
```

