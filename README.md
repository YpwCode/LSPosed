# LspMod

## 项目初始化

### 项目主体

```shell
git clone git@github.com:YpwCode/LSPosed.git
git submodule update --init --recursive
```

### 项目依赖库

```shell
git clone git@github.com:YpwCode/libxposed-api.git
git checkout -b 100 origin/100
./gradlew publishToMavenLocal
```

```shell
git clone git@github.com:YpwCode/libxposed-service.git
./gradlew publishToMavenLocal
```
