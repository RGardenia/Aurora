









## MySQL

```bash


## 创建数据目录文件夹（mysql 运行时的数据目录，约定好的）
mkdir /var/lib/mysql
## 配置文件
mkdir /opt/mysql_config

# 创建 Secret 对象
## 创建 secret，generic：基于普通文本格式，--from-literal：从字面量创建
kubectl create secret generic mysql-auth --from-literal=username=root --from-literal=password=151613 -n garmysql
kubectl get secret mysql-auth
kubectl get secret mysql-auth -o yaml
echo MTUxNjEz | base64 -d

# ConfigMap 存储 Mysql 配置文件	my.cnf
vim mysql-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mysql-config
  labels:
    app: mysql
data:
  my.cnf: |-
    [client]
    default-character-set=utf8mb4
    [mysql]
    default-character-set=utf8mb4
    [mysqld] 
    max_connections = 2000
    secure_file_priv=/var/lib/mysql
    sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION

kubectl create ns garmysql
kubectl create -f mysql-config.yaml -n garmysql

# 创建 PV、PVC	Skip
vim mysql-storage.yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mysql
  labels:
    app: mysql             # 设置 pv 的 label 标签
spec:
  capacity:          
    storage: 39Gi
  accessModes:       
  - ReadWriteOnce
  mountOptions:
  - hard
  - nfsvers=4.1
  nfs:
    server: 192.168.2.11
    path: /nfs/mysql       # 指定 NFS 共享目录的位置，且需提前在该目录中创建 mysql 目录
  persistentVolumeReclaimPolicy: Retain  
---
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: mysql
spec:
  resources:
    requests:
      storage: 39Gi
  accessModes:
  - ReadWriteOnce
  selector:
    matchLabels:
      app: mysql

# 创建 Deployment
vim mysql-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  labels:
    app: mysql
  namespace: garmysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: gardenia-mysql
        image: mysql:8.2.0
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 3306
        volumeMounts:
        - name: data
          mountPath: /var/lib/mysql
        - name: config
          mountPath: /etc/mysql/conf.d
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-auth
              key: password
      volumes:
      - name: data
        hostPath:
          path: /var/lib/mysql
          type: Directory
      - name: config
        hostPath:
          path: /etc/mysql/conf
          type: Directory

kubectl apply -f mysql-deployment.yaml
kubectl get pods -n garmysql
kubectl describe pods mysql-fdd58cd88-dkzqc -n garmysql
kubectl logs mysql-fdd58cd88-cf5l5 -n garmysql

# 创建 Service
vim mysql-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
  namespace: garmysql
spec:
  selector:
    app: mysql
  type: NodePort
  ports:
  - name: mysql
    protocol: TCP
    port: 3306
    targetPort: 3306
    nodePort: 30169

kubectl apply -f mysql-service.yaml
kubectl get svc -n garmysql

mysql -h106.14.45.61 -P30169 -uroot -p151613
kubectl get all -n garmysql
```

> https://cloud.tencent.com/developer/article/1783227

```bash
vim /run/flannel/subnet.env

FLANNEL_NETWORK=10.244.0.0/16
FLANNEL_SUBNET=10.244.0.1/24
FLANNEL_MTU=1450
FLANNEL_IPMASQ=true
```





## KuBoard

```bash
## 线安装
wget https://addons.kuboard.cn/kuboard/kuboard-v3.yaml
kubectl apply -f https://addons.kuboard.cn/kuboard/kuboard-v3.yaml

kubectl get pods -n kuboard
kubectl get all -n kube-system | grep kuboard

echo $(kubectl -n kube-system get secret $(kubectl -n kube-system get secret | grep kuboard-user | awk '{print $1}') -o go-template='{{.data.token}}' | base64 -d)
```





# Nginx

## Download

```bash
vim default.conf
server {
    listen       80;
    server_name gardenia.com;
    error_page 500  502 504 503  /50x.html;
    
    location /download {
          alias  /usr/local/webapp/download/;
          sendfile on;
          autoindex on;  # 开启目录文件列表
          autoindex_exact_size on;  # 显示出文件的确切大小，单位是bytes
          autoindex_localtime on;  # 显示的文件时间为文件的服务器时间
          charset utf-8,gbk;  # 避免中文乱码
    }
}
kubectl create configmap nginx-config --from-file=./default.conf
kubectl describe pod/nginxsvcdp-57b9f6b5df-cfjf4

mkdir -p /mnt/nginx
kubectl apply -f deploy.yaml
```

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginxsvcdp
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 1
  minReadySeconds: 1
  strategy:
      rollingUpdate:
          maxSurge: 1
          maxUnavailable: 0
      type: RollingUpdate
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx
        ports:
        - containerPort: 80
        volumeMounts:
        -  mountPath: /etc/nginx/conf.d
           name: config
        -  mountPath: /usr/local/webapp/download/
           name: data
      volumes:
        - name: data
          hostPath:
            path: /mnt/nginx
            type: Directory
        -  name: config
           configMap:
              name: nginx-config
              items:
              -  key: default.conf
                 path: ./default.conf
 
---
apiVersion: v1
kind: Service
metadata:
    name: nginxdpsvc
spec:
    type: NodePort
    selector:
        app: nginx
    ports:
    -  port: 80
       targetPort: 80
       nodePort: 30001
```



