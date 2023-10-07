# **Reset**



#### **描述**

完全不接触索引文件或工作树（但会像所有模式一样，将头部重置为）。这使所有更改的文件更改为“要提交的更改”

回退你已提交的 commit，并将 commit 的修改内容放回到暂存区。

一般在使用 reset 命令时，`git reset --hard` 会被提及的比较多，它能让 commit 记录强制回溯到某一个节点。而 `git reset --soft` 的作用正如其名，`--soft` (柔软的) 除了回溯节点外，还会保留节点的修改内容。



#### **命令使用**

```bash
# 恢复最近一次 commit
git reset --soft HEAD^
```

`reset --soft` 相当于后悔药，给重新改过的机会。对于上面的场景，就可以再次修改重新提交，保持干净的 commit 记录。

以上说的是还未 push 的 commit。对于已经 push 的 commit，也可以使用该命令，不过再次 push 时，由于远程分支和本地分支有差异，需要强制推送 `git push -f` 来覆盖被 reset 的 commit

还有一点需要注意，在 `reset --soft` 指定 commit 号时，会将该 commit 到最近一次 commit 的所有修改内容全部恢复，而不是只针对该 commit

举个例子：

commit 记录有 c、b、a

```bash
git reset --soft 1a900ac29eba73ce817bf959f82ffcb0bfa38f75
```

此时的 HEAD 到了 a，而 b、c 的修改内容都回到了暂存区。





