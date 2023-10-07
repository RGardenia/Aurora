# Cherry-pick



#### 描述

给定一个或多个现有提交，应用每个提交引入的更改，为每个提交记录一个新的提交。这需要工作树清洁（没有从头提交的修改）

将已经提交的 commit，复制出新的 commit 应用到分支里



#### **应用场景**

应用场景1：有时候版本的一些优化需求开发到一半，可能其中某一个开发完的需求要临时上，或者某些原因导致待开发的需求卡住了已开发完成的需求上线。这时候就需要把 commit 抽出来，单独处理。

应用场景2：有时候开发分支中的代码记录被污染了，导致开发分支合到线上分支有问题，这时就需要拉一条干净的开发分支，再从旧的开发分支中，把 commit 复制到新分支。



#### **命令使用**

##### **复制单个**

现在有一条 `feature` 分支，commit 记录如下：

需要把 b 复制到另一个分支，首先把 commitHash 复制下来，然后切到 master 分支。

当前 master 最新的记录是 a，使用cherry-pick把 b 应用到当前分支。

完成后看下最新的 log，b 已经应用到 master，作为最新的 commit 了。可以看到 commitHash 和之前的不一样，但是提交时间还是保留之前的。

##### **复制多个**

以上是单个 commit 的复制，下面再来看看 cherry-pick 多个 commit 要如何操作。

一次转移多个提交：

```bash
git cherry-pick commit1 commit2
```

上面的命令将 commit1 和 commit2 两个提交应用到当前分支。

多个连续的 commit，也可区间复制：

```bash
git cherry-pick commit1^..commit2
```

上面的命令将 commit1 到 commit2 这个区间的 commit 都应用到当前分支（包含commit1、commit2），commit1 是最早的提交。

**放弃 cherry-pick**

```bash
git cherry-pick --abort
```

**退出 cherry-pick**

```bash
git cherry-pick --quit
```