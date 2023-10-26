













# Environment



Visual Studio：https://learn.microsoft.com/zh-cn/visualstudio/productinfo/vs-roadmap

CUDA 驱动：https://developer.nvidia.com/cuda-toolkit-archive



[CUDA 版本和驱动的对应表](https://docs.nvidia.com/cuda/cuda-toolkit-release-notes/index.html#cuda-toolkit-major-component-versions)

[`VS + CUDA`  新建项目里没有CUDA选项](https://blog.csdn.net/weixin_39591031/article/details/124462430)

[`Windows` 下 CUDA 的卸载以及安装](https://blog.csdn.net/m0_37605642/article/details/99100924)

Reference：https://blog.csdn.net/weixin_43610114/article/details/129905558





















# FAQ



1. `requirements.txt`  中 使用 Git

```bash
git config --global url."git@github.com:".insteadOf https://github.com/

git config --global --list
user.name=Gardenia
user.email=1511502172@qq.com
url.git@github.com:.insteadof=https://github.com/
```



2. PyTorch 安装版本

   ```bash
   pip install torch==2.1.0+cu121 -f https://download.pytorch.org/whl/torch_stable.html
   
   
   ```

   