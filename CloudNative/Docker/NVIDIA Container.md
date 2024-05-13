# NVIDIA Container Toolkit



> https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html



## Installation[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#installation)

### Installing with Apt[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#installing-with-apt)

1. Configure the production repository:

   ```
   $ curl -fsSL https://nvidia.github.io/libnvidia-container/gpgkey | sudo gpg --dearmor -o /usr/share/keyrings/nvidia-container-toolkit-keyring.gpg \
     && curl -s -L https://nvidia.github.io/libnvidia-container/stable/deb/nvidia-container-toolkit.list | \
       sed 's#deb https://#deb [signed-by=/usr/share/keyrings/nvidia-container-toolkit-keyring.gpg] https://#g' | \
       sudo tee /etc/apt/sources.list.d/nvidia-container-toolkit.list
   ```

   

   Optionally, configure the repository to use experimental packages:

   ```
   $ sed -i -e '/experimental/ s/^#//g' /etc/apt/sources.list.d/nvidia-container-toolkit.list
   ```

   

2. Update the packages list from the repository:

   ```
   $ sudo apt-get update
   ```

   

3. Install the NVIDIA Container Toolkit packages:

   ```
   $ sudo apt-get install -y nvidia-container-toolkit
   ```

   

### Installing with Yum or Dnf[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#installing-with-yum-or-dnf)

1. Configure the production repository:

   ```
   $ curl -s -L https://nvidia.github.io/libnvidia-container/stable/rpm/nvidia-container-toolkit.repo | \
     sudo tee /etc/yum.repos.d/nvidia-container-toolkit.repo
   ```

   

   Optionally, configure the repository to use experimental packages:

   ```
   $ sudo yum-config-manager --enable nvidia-container-toolkit-experimental
   ```

   

2. Install the NVIDIA Container Toolkit packages:

   ```
   $ sudo yum install -y nvidia-container-toolkit
   ```

   

### Installing with Zypper[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#installing-with-zypper)

1. Configure the production repository:

   ```
   $ sudo zypper ar https://nvidia.github.io/libnvidia-container/stable/rpm/nvidia-container-toolkit.repo
   ```

   

   Optionally, configure the repository to use experimental packages:

   ```
   $ sudo zypper modifyrepo --enable nvidia-container-toolkit-experimental
   ```

   

2. Install the NVIDIA Container Toolkit packages:

   ```
   $ sudo zypper --gpg-auto-import-keys install -y nvidia-container-toolkit
   ```

   

## Configuration[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#configuration)

### Prerequisites[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#prerequisites)

- You installed a supported container engine (Docker, Containerd, CRI-O, Podman).
- You installed the NVIDIA Container Toolkit.



### Configuring Docker[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#configuring-docker)

1. Configure the container runtime by using the `nvidia-ctk` command:

   ```
   $ sudo nvidia-ctk runtime configure --runtime=docker
   ```

   

   The `nvidia-ctk` command modifies the `/etc/docker/daemon.json` file on the host. The file is updated so that Docker can use the NVIDIA Container Runtime.

2. Restart the Docker daemon:

   ```
   $ sudo systemctl restart docker
   ```

   

#### Rootless mode[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#rootless-mode)

To configure the container runtime for Docker running in [Rootless mode](https://docs.docker.com/engine/security/rootless/), follow these steps:

1. Configure the container runtime by using the `nvidia-ctk` command:

   ```
   $ nvidia-ctk runtime configure --runtime=docker --config=$HOME/.config/docker/daemon.json
   ```

   

2. Restart the Rootless Docker daemon:

   ```
   $ systemctl --user restart docker
   ```

   

3. Configure `/etc/nvidia-container-runtime/config.toml` by using the `sudo nvidia-ctk` command:

   ```
   $ sudo nvidia-ctk config --set nvidia-container-cli.no-cgroups --in-place
   ```

   

### Configuring containerd (for Kubernetes)[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#configuring-containerd-for-kubernetes)

1. Configure the container runtime by using the `nvidia-ctk` command:

   ```
   $ sudo nvidia-ctk runtime configure --runtime=containerd
   ```

   

   The `nvidia-ctk` command modifies the `/etc/containerd/config.toml` file on the host. The file is updated so that containerd can use the NVIDIA Container Runtime.

2. Restart containerd:

   ```
   $ sudo systemctl restart containerd
   ```

   

### Configuring containerd (for nerdctl)[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#configuring-containerd-for-nerdctl)

No additional configuration is needed. You can just run `nerdctl run --gpus=all`, with root or without root. You do not need to run the `nvidia-ctk` command mentioned above for Kubernetes.

See also the [nerdctl documentation](https://github.com/containerd/nerdctl/blob/main/docs/gpu.md).

### Configuring CRI-O[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#configuring-cri-o)

1. Configure the container runtime by using the `nvidia-ctk` command:

   ```
   $ sudo nvidia-ctk runtime configure --runtime=crio
   ```

   

   The `nvidia-ctk` command modifies the `/etc/crio/crio.conf` file on the host. The file is updated so that CRI-O can use the NVIDIA Container Runtime.

2. Restart the CRI-O daemon:

   ```
   $ sudo systemctl restart crio
   ```

   

### Configuring Podman[](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#configuring-podman)

For Podman, NVIDIA recommends using [CDI](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/cdi-support.html) for accessing NVIDIA devices in containers.





