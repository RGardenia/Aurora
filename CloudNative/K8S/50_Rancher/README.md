# 使用Rancher2.0搭建Kubernetes集群

中文文档：https://docs.rancher.cn/docs/rancher2

## 安装 Rancher 2.0

使用下面命令，快速的安装

```bash
# 启动 rancher【没有的话会从后台拉取】
docker run -d -p 80:80 -p 443:443 rancher/rancher:v2.0.0
# 查看
docker ps -a
```

![image-20201123160929063](images/image-20201123160929063.png)

可以来查看的日志

```bash
docker logs  eloquent_curie
```

同时，可以直接访问新建的Rancher集群

```bash
https://192.168.177.150/
```

![image-20201123161958206](images/image-20201123161958206.png)

第一次登录，需要填写密码，自己的密码后，点击下一步，完成后即可进入到的控制台

![image-20201123180845117](images/image-20201123180845117.png)

## 导入 K8S 集群

在安装好Rancher2.0后，就可以导入的K8S集群进行管理了

首先点击 Add Cluster ，然后选择 IMPORT 导入的集群

![image-20201123194902242](images/image-20201123194902242.png)

然后会有Add Cluster页面，下面通过命令来添加

![image-20201123194958021](images/image-20201123194958021.png)

首先选择上面这条，在的master节点上执行，将的集群被Rancher接管

```bash
kubectl apply -f https://192.168.177.130/v3/import/6pqf9w75fmx4pt94tpbpklxd2t5qkq2fm9v6dgl6w8z6rc8727bpdk.yaml
```

如果执行命令有问题的话，可以提前把脚本下载下来，然后拷贝到里面的 rancher.yaml

 ```bash
kubectl apply -f rancher.yaml
 ```

![image-20201123200210728](images/image-20201123200210728.png)

在执行上述命令，可能会出现这个问题，只需要把里面的 extensions/v1beta1 修改成  apps/v1 即可

修改完成后，再次执行即可

![image-20201123200337426](images/image-20201123200337426.png)

通过下面命令，查看创建的pods

```bash
kubectl get pods  -n cattle-system
```

![image-20201123200558313](images/image-20201123200558313.png)

执行完上述操作后，到Rancher的UI界面，点击Done，即可看到的集群被成功导入

![image-20201123200834363](images/image-20201123200834363.png)