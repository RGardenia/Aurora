# Stash



#### **描述**

官方解释：当想记录工作目录和索引的当前状态，但又想返回一个干净的工作目录时，请使用 `git stash` 。该命令将保存本地修改，并恢复工作目录以匹配头部提交。

stash 命令能够将还未 commit 的代码存起来，让工作目录变得干净。

#### **命令使用**

`git stash` 

当修复完线上问题，切回 `feature` 分支，想恢复代码也只需要：

```bash
git stash apply
```

相关命令

```bash
# 保存当前未commit的代码
git stash

# 保存当前未commit的代码并添加备注
git stash save "备注的内容"

# 列出stash的所有记录
git stash list

# 删除stash的所有记录
git stash clear

# 应用最近一次的stash
git stash apply

# 应用最近一次的stash，随后删除该记录
git stash pop

# 删除最近的一次stash
git stash drop
```

当有多条 stash，可以指定操作stash，首先使用 `stash list`  列出所有记录：

```bash
$ git stash list
stash@{0}: WIP on ...
stash@{1}: WIP on ...
stash@{2}: On ...

# 应用第二条记录	pop，drop 同理
$ git stash apply stash@{1}
```

