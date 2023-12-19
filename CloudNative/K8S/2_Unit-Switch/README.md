# Unit





# Ubuntu Deploy Minikube

> https://kubernetes.io/zh-cn/docs/tasks/tools/install-kubectl-linux/#install-using-other-package-management
> https://minikube.sigs.k8s.io/docs/start/

```bash
sudo apt-get update
# apt-transport-https 可以是一个虚拟包
sudo apt-get install -y apt-transport-https ca-certificates curl

curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.28/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

# 源	覆盖 /etc/apt/sources.list.d/kubernetes.list 中的所有现存配置
echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.28/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list

sudo apt-get update
sudo apt-get install -y kubectl
# OR
snap install kubectl --classic
kubectl version --client

curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube_latest_amd64.deb
sudo dpkg -i minikube_latest_amd64.deb

minikube config set driver docker

minikube start --force --driver=docker

minikube dashboard
# kubectl proxy --port=8001 --address='120.46.36.46' --accept-hosts='^.*' &
kubectl expose service kubernetes-dashboard --type=NodePort --name=kube-dashboard-service --port=8001 --target-port=80
nohup kubectl proxy --port=8001 --address='0.0.0.0' --accept-hosts='^.*' &
```





# Switch



## Docker To

```bash
crictl info
# Config
vim /etc/containerd/config.toml

[plugins."io.containerd.grpc.v1.cri".registry]
      [plugins."io.containerd.grpc.v1.cri".registry.mirrors]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
          endpoint = ["https://registry-1.docker.io"]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."registry.cn-shanghai.aliyuncs.com"]
          endpoint = ["https://registry.cn-hangzhou.aliyuncs.com"]
      [plugins."io.containerd.grpc.v1.cri".registry.configs]
        [plugins."io.containerd.grpc.v1.cri".registry.configs."registry.cn-shanghai.aliyuncs.com".tls]
          insecure_skip_verify = true
        [plugins."io.containerd.grpc.v1.cri".registry.configs."registry.cn-shanghai.aliyuncs.com".auth]
          username = "GardeniaR"
          password = "1516Chen"

systemctl restart containerd.service

crictl pull registry.cn-shanghai.aliyuncs.com/gardenia_hadoop/centos-hadoop-kafka
# 至此，镜像准备完成 ~

kubectl get svc -n kubernetes-dashboard

# 获取 Token
kubectl create token --namespace kubernetes-dashboard --duration 2592000s kubernetes-dashboard
eyJhbGciOiJSUzI1NiIsImtpZCI6ImpwSVg3UDJyM3lUNVBNeWpmME5razIwdTNTSjY5dVVlQ25pSGlBZldjWFkifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiXSwiZXhwIjoxNzAyNDUzMzM2LCJpYXQiOjE2OTk4NjEzMzYsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsInNlcnZpY2VhY2NvdW50Ijp7Im5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsInVpZCI6ImE1ZjMzOTQ3LTM5MTMtNGQwYi1hMGI3LWVkNDMwNjAzMjFjNiJ9fSwibmJmIjoxNjk5ODYxMzM2LCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQifQ.coNkWOv5GT9slBHkG1gWdRSh27l6svFuJB1hSuEOdo8ew9u9TgqYszeFhavOwOkLtiThubvcgX5jpQVRoTte5g0KVToJZdjwnqLntDPXM6NEeiazjch12NPYs9jKJhAGTrtAFTBx2mLy6bu69Ud8fT4a_j4Wg5eqNoIuqIc81CmvXkE9ErGMfy7BVy1rg10f8YEOVY9orTOYPtG600oErnpWeHKsH1_RK9rrpR1OuZ2MrQkEEcwvLErP-QXPGTIF4j8CFFL0DRd8QVaIbvWpCe68qBDaRTJv7dJ9x885tT0HoYQ_V_6WWzHFCqFvZognQUJFedzGLHIfnvrTmz26oQ


kubectl create ns hadoop

kubectl apply -f /opt/centos-hadoop-kafka.yml
kubectl get pods -n hadoop --show-labels -o wide
kubectl get svc -n hadoop

kubectl expose deploy hadoop1 --port=22 --target-port=22 --type=NodePort

vim /opt/centos-hadoop-kafka.yml

ssh hadoop1.hadoop.svc

curl -I hadoop1.hadoop.svc:9870
ss -tlnp | grep 22
```

```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hadoop1
  namespace: hadoop
  labels:
    node: hadoop1
spec:
  replicas: 1
  selector:
    matchLabels:
      node: hadoop1
  template:
    metadata:
      labels:
        node: hadoop1
    spec:
      hostname: hadoop1
      containers:
      - name: hadoop1
        image: registry.cn-shanghai.aliyuncs.com/gardenia_hadoop/centos-hadoop-kafka
        securityContext:
          privileged: true
        command: ["/usr/sbin/init", "-c","--"]
        ports:
        - containerPort: 22
        - containerPort: 9870
        - containerPort: 19888
        - containerPort: 50070
        - containerPort: 8088
        - containerPort: 9001
---
apiVersion: v1
kind: Service
metadata:
  name: hadoop1
  namespace: hadoop
spec:
  selector:
    node: hadoop1
  ports:
  - name: port-22
    port: 22
    targetPort: 22
  - name: port-9870
    port: 9870
    targetPort: 9870
  - name: port-19888
    port: 19888
    targetPort: 19888
  - name: port-50070
    port: 50070
    targetPort: 50070
  - name: port-8088
    port: 8088
    targetPort: 8088
  - name: port-9001
    port: 9001
    targetPort: 9001
```



















