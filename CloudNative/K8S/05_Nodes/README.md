# Nodes







### 扩容

```bash
# 扩容操作 以 Nginx 为例
kubectl scale deployment nginx --replicas=3
```





## ROLES

```bash
# 查看 Node 的标签
kubectl get nodes --show-labels

# 增加标签
kubectl label nodes node001 node-role.kubernetes.io/node=
kubectl label nodes node002 node-role.kubernetes.io/node=
```



