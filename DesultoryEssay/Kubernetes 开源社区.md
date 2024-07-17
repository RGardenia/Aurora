

# Kubernetes 开源社区

> Kubernetes 中文社区 ：https://www.kubernetes.org.cn/

## **1. 了解 Kubernetes 开源社区**

这⼀章节，你将了解整个 Kubernetes 社区是如何治理的：

### **1.1. 分布式协作**

与公司内部集中式的项⽬开发模式不同，⼏乎所有的开源社区都是⼀个分布式、松散的组织，为此 ，Kubernetes 建⽴了⼀套完备的社区治理制度。协作上，社区⼤多数的讨论和交流主要围绕 issue 和 PR 展开。由于 Kubernetes ⽣态⼗分繁荣，因此所有对 Kubernetes 的修改都⼗分谨慎，每个提交的 PR 都需要通过两个以上成员的 Review 以及经过⼏千个单元测试、集成测试、端到端测试以及扩展性测试，所有这些举措共同保证了项⽬的稳定。

### **1.2. Committees**

委员会由多人组成，主要负责制定组织的行为规范和章程，处理一些敏感的话题。常见的委员会包括行为准则委员会，安全委员会，指导委员会。

### **1.3. SIG**

SIG 的全称是 Special Interest Group，即特别兴趣⼩组，它们是 Kubernetes 社区中关注特定模块的永久组织，Kubernetes 作为⼀个拥有⼏⼗万⾏源代码的项⽬，单⼀的⼩组是⽆法了解其实现的全貌的。Kubernetes ⽬前包含 20 多个 SIG，它们分别负责了 Kubernetes 项⽬中的不同模块，这是我们参与 Kubernetes 社区时关注最多的⼩组。作为刚刚参与社区的开发者，可以选择从某个 SIG 入手，逐步了解社区的⼯作流程。

### **1.4. KEP**

KEP 的全称是 Kubernetes Enhancement Proposal，因为 Kubernetes ⽬前已经是⽐较成熟的项⽬了，所有的变更都会影响下游的使⽤者，因此，对于功能和  API 的修改都需要先在 kubernetes/enhancements 仓库对应 SIG 的⽬录下提交提案才能实施，所有的提案都必须经过讨论、通过社区 SIG Leader 的批准。

### **1.5. Working Group**

这是由社区贡献者⾃由组织的兴趣⼩组，对现阶段的⼀些⽅案和社区未来发展⽅向进⾏讨论，并且会周期性的举⾏会议。会议⼤家都可以参加，⼤多是在国内的午夜时分。以 scheduling 为例，你可以查看文档 Kubernetes Scheduling Interest Group 了解例次会议纪要。会议使⽤ Zoom 进⾏录制并且会上传到 Youtube, 过程中会有主持⼈主持，如果你是新⼈，可能需要你进行自我介绍。

🔗https://docs.google.com/document/d/13mwye7nvrmV11q9_Eg77z-1w3X7Q1GTbslpml4J7F3A/edit%23heading%25253Dh.ukbaidczvy3r

### **1.6. MemberShip**

| 角色       | 职责                                         | 要求                                           |
| :--------- | :------------------------------------------- | :--------------------------------------------- |
| Member     | 社区积极贡献者                               | 对社区有多次贡献并得到两名reviewer的赞同       |
| Reviewer   | 对其他成员贡献的代码积极的review             | 在某个子项目中长期review和贡献代码             |
| Approver   | 对提交的代码进行最后的把关，有合并代码的权限 | 属于某一个子项目经验丰富的reviewer和代码贡献者 |
| Maintainer | 制定项目的优先级并引领项目发展方向           | 证明自己在这个项目中有很强的责任感和技术能力   |

每种⻆⾊承担不同的职责，同时也拥有不同的权限。⻆⾊晋升主要参考你对社区的贡献，具体内容可参考 KubernetesMemberShip。 

🔗https://github.com/kubernetes/community/blob/master/community-membership.md

### **1.7. Issue 分类**

## Kubernetes Issues 有很多 label，如 bug, feature, sig 等，有时候你需要对这些 issue 进⾏⼿动分类，关于如何分类可以参考以下文章链接。

🔗文章链接：https://hackmd.io/O_gw_sXGRLC_F0cNr3Ev1Q

### **1.8. 其他关注项**

## Slack：

## 🔗https://slack.k8s.io

## StackOverFlow：

## 🔗https://stackoverflow.com/questions/tagged/kubernetes

## Discussion：

## 🔗https://groups.google.com/g/kubernetes-dev

## 

## **2. 开始你的 first-good-issue**

更多详情可参见官⽅⽂档，⽂档详细描述了该如何提交 PR，以及应该遵循什么样的原则，并给到了⼀些最佳实践。

🔗官方文档：https://www.kubernetes.dev/docs/guide/contributing/

代码语言：javascript

复制

```javascript
2.1. 申请 CLA
```

当你提交 PR 时，Kubernetes 代码仓库 CI 流程会检查是否有 CLA 证书，如何申请证书可以参考官⽅⽂档。

🔗官方文档：https://github.com/kubernetes/community/blob/master/CLA.md

代码语言：javascript

复制

```javascript
2.2. 搜索 first-good-issue 「你可以选择你感兴趣的或你所熟悉的 SIG」
```

first-good-issue 是 Kubernetes 社区为培养新参与社区贡献的开发⼈员⽽准备的 issue，⽐较容易上⼿。

以 sig/scheduling 为例，在 Issues 中输⼊：

代码语言：javascript

复制

```javascript
is:issue is:open label:sig/scheduling label:"good first issue" no:assignee
```

该 Filters 表示筛选出没有被关闭的，属于 sig/scheduling，没有 assign 给别⼈的 good first issue。

如果没有相关的 good-first-issue，你也可以选择 kind/documentation 或者kind/cleanup 类型 issue。

## **2.3.了解 issue 上下⽂，确定⾃⼰能够完成就通过 /assign 命令标记给⾃⼰。这⾥涉及到⼀些常⻅命令，如下所示；更多命令，⻅ Command-Help。**

🔗Command-Hlep：https://prow.k8s.io/command-help?repo=kubernetes%25252Fkubernetes

| /retitle               | 重命名标题                           |
| :--------------------- | :----------------------------------- |
| /close                 | 关闭 issue                           |
| /assign                | 将 issue assign 给⾃⼰               |
| /sig scheduling        | 添加标签 sig/scheduling              |
| /remove-sig scheduling | 去掉标签                             |
| /help                  | 表示需要帮助，会打上标签 help wanted |
| /good-first-issue      | 添加标签 good first issue            |
| /retest                | 重新测试出错的测试⽤例               |
| /ok-to-test            | 准备好开始测试                       |

## **2.4. 编码**

a.  Fork 代码仓库

将 kubernetes/Kubernetes fork 到⾃⼰的 GitHub 账号名下。

b. Clone ⾃⼰的代码仓库

代码语言：javascript

复制

```javascript
git clone git@github.com:<your github id>/kubernetes.git
```

c.  追踪源代码仓库代码变动

- 添加 upstream：

代码语言：javascript

复制

```javascript
git remote add upstream https://github.com/kubernetes/kubernetes.git
```

- git remote -v 检查是否添加成功，成功则显示：

代码语言：javascript

复制

```javascript
origin git@github.com:<your github id>/kubernetes.git 
(fetch) 
origin git@github.com:<your github id>/kubernetes.git 
(push) 
upstream 
https://github.com/kubernetes/kubernetes.git (fetch) 
upstream 
https://github.com/kubernetes/kubernetes.git (push)
```

- 同步 upstream kubernetes 最新代码

代码语言：javascript

复制

```javascript
git checkout master
git pull upstream master git push
```

d.  切分支，编码

代码语言：javascript

复制

```javascript
git checkout -b <branch name>
```

e.  Commit，并提交 PR

- 命令行：

代码语言：javascript

复制

```javascript
git commit -s -m '<change me>'
```

- 注意：
-  commit push 前先执⾏一些检查，如 make update等
-  如果本次修改还没有完成，可以使⽤ Github Draft  模式，并添加 [WIP] 在标题中
- Commit 信息过多，且没有特别⼤的价值，建议合成⼀条 commit 信息

代码语言：javascript

复制

```javascript
git rebase -i HEAD~2
```

f.  提交 PR

在 GitHub ⻚⾯按照模版提交 PR

## **2.5. CI**

a. PR 提交后需要执⾏ Kubernetes CI 流程，此时需要 Kubernetes Member 输入 /ok- to-test 命令，然后会⾃动执⾏ CI，包括验证和各种测试。可以 @ 社区成员帮助打标签。

b. ⼀旦测试失败，修复后可以执⾏ /retest 重新执⾏失败的测试，此时，你已经可以⾃⼰操作。

## **2.6. Code Review**

a. 每次提交需要有 2 个 Reviewer 进⾏ Code Review， 如果通过，他们会打上 /lgtm 标签，表示 looks good to me, 代码审核完成。

b.  另外需要⼀个 Approver 打上 /approve 标签，表示代码可以合⼊主⼲分⽀，GitHub 机器⼈会⾃动执⾏ merge 操作。

c.  PR 跟进没有想像中那么快，有时候 1-2 周也正常。

d.  恭喜，你完成了第⼀个 PR 的提交。

## **3. 如何成为 Kubernetes Member**

## **3.1. 官方文档要求对项目有多个贡献，并得到两个 revieweres 的同意**

代码语言：javascript

复制

```javascript
Sponsored by 2 reviewers and multiple contributions to the project
```

## **3.2. 贡献包括**

代码语言：javascript

复制

```javascript
PR
Issue
Kep
Comment
```

## **3.3. 建议**

a.  多参与社区的讨论，表达⾃⼰的观点

b.  多参与 issue 的解答，帮助提问者解决问题，社区的意义也在于此

c.  可以在看源代码的时候多留意⼀些语法、命名和重复代码问题，做⼀些重构相关的⼯作

d.  从测试⼊⼿也是⼀个好办法，如补全测试，或者修复测试

e.  参与⼀些代码的 review，可以学到不少知识

f. 最有价值的肯定是 feature 的实现，可以提交 kep

## **3.4. 关于如何获得 Reviewer 的同意**

## 可以事先通过邮件打好招呼，把⾃⼰做的⼀些事情同步给他们，以及⾃⼰未来在开源社区的规划。

## **3.5. 结果**

## 成为 Kubernetes Member 不是⽬的，⽽应该是⽔到渠成的结果。

## **4. 常用命令**

参考 Makefile ⽂件

🔗https://github.com/kubernetes/kubernetes/blob/master/build/root/Makefile

- 单元测试（单方法）

代码语言：javascript

复制

```go
go test -v --timeout 30s k8s.io/kubectl/pkg/cmd/get -run 
^TestGetSortedObjects$
```

- 集成测试（单⽅法）

代码语言：javascript

复制

```makefile
make test-integration WHAT=./vendor/k8s.io/kubectl/pkg/cmd/get GOFLAGS="-v" KUBE_TEST_ARGS="-run ^TestRuntimeSortLess$"
```

- E2E 测试

可以使⽤ GitHub 集成的 E2E 测试：

代码语言：javascript

复制

```javascript
/test pull-kubernetes-node-kubelet-serial
```

## **5. 小结**

​	Kubernetes 和 Linux 一样,早已成为 IT 技术的重要事实标准，而开源 Kubernetes 是整个行业的 “上游”，灌溉了数亿的互联网和企业应用。「DaoCloud 道客」融合自身在各行各业的实战经验，持续贡献 Kubernetes开源项目，致力于让以Kubernetes为代表的云原生技术更平稳、高效地落地到产品和生产实践中。此外，「DaoCloud 道客」全面布局开源生态，是最早一批加入 CNCF 基金会的云原生企业，拥有云原生基金会成员，Linux 基金会成员，Linux 基金会培训合作伙伴，Kubernetes 培训合作伙伴，Kubernetes 兼容性认证，以及 Kubernetes 认证服务提供商等资质，坚持构建并维护云原生开源生态圈。在开源贡献这条道路上，「DaoCloud 道客」会一直走下去，也愿意成为开源社区的守护者、暸望塔，并始终坚信开源的力量、原生的力量会与这个时代产生共鸣，迸发出属于自己的光彩。
