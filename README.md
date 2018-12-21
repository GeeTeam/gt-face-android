## SDK下载
### SDK更新日志

|版本号|更新日期|更新说明|
|-----|-----|-----|
|V0.0.7|2018-6-25|拍照像素提高|
|V0.0.8|2018-7-16|对扫描框内图片进行裁剪|

[SDK下载]()
## DEMO下载
### DEMO更新日志
|版本号|更新日期|更新说明|
|-----|-----|-----|
|V0.0.7|2018-6-25|拍照像素提高|
|V0.0.8|2018-7-16|对扫描框内图片进行裁剪|

[身份证OCR]()

[银行卡OCR]()

[护照OCR]()

[静默活体检测]()

[人脸对比-图片与图片对比]()

[人脸对比-视频与图片对比]()

[人脸身份核验-图片与身份核验]()

[人脸身份核验-视频与身份核验]()


## 说明文档
### 文档更新日志

|版本号|更新日期|更新说明|
|-----|-----|-----|
|V1.0.1|2018-6-25|增加了具体不一致详情|
|V1.0.2|2018-7-16|更新签名认证ID和KEY的使用说明以及状态码详细描述|
### 概述与资源
Android SDK提供给集成Android原生客户端开发的开发者使用。
### 环境需求
条目	|资源
------	|------------
开发目标|4.0以上
开发环境|Android Studio 2.1.3
系统依赖|`v7包`
sdk三方依赖|无

### 安装
#### 获取SDK

1. 在demo的`libs`包下，将获取的`.jar`文件拖拽到工程中的libs文件夹下。

2. 添加权限

```java
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permissio  android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />

```

##### 配置接口

集成用户需要使用Android SDK完成提供的以下接口:

1. 配置并初始化
2. 调用校验接口
3. 处理结果
4. 处理错误

>集成代码参考下方的代码示例

-

### 身份证OCR
#### 初始化

在项目的具体页面的`onCreate`方法里面进行初始化。

```java
cardApi = new CardApi(this);

```

#### 配置参数

```java
cardApi.init(baseCardListener,API_KEY,API_SECRET);
//第一个参数为所需接口
//第二个参数为配置的key
//第三个参数为配置的secret

```

#### 点击执行

```java
cardApi.play(cardType);
//可选的样式,如:CardType.BC_CARDTYPE_BANKCARD,CardType.BC_CARDTYPE_IDCARD

```

#### 图片数据获取


```java
  @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        cardApi.inputScanImage(bytes, this.mCamera.getPreviewSize(),
                this.mCameraPreview.convertViewRectToCameraPreview(this.mOverlayView.getCardRect()),
                this.mCamera.getRotationDegrees());
    }
//第一个参数位扫描图片数据
//第二个参数位图片的尺寸
//第三个参数位卡片区域
//第四个参数位摄像头方向

```

#### 上传文件的实现方式

```java
      cardApi.inputCard(CardType.BC_CARDTYPE_IDCARD, idCardImagePath);
      //CardType.BC_CARDTYPE_IDCARD表示选择的是身份证
      //idCardImagePath为身份证图片的路径

```


#### 接口实现

实现接口进行校验。

```java
BaseCardListener baseCardListener =new BaseCardListener() {

	@Override
	public void onError(int code,String error) {
		//过程中出现的错误码以及错误描述
	}


    @Override
    public void onResult(String token) {
      //验证成功返回的token
    }

};

```

#### 页面关闭

在页面关闭的时候执行此方法。

```java
@Override
protected void onPause() {
 	super. onPause();
 	cardApi.cancel();
}

```

#### 混淆规则

```
-dontwarn com.geetest.oneperson.**
-keep class com.geetest.oneperson.** {
*;
}
```
-
### 银行卡OCR
#### 初始化

在项目的具体页面的`onCreate`方法里面进行初始化。

```java
cardApi = new CardApi(this);

```

#### 配置参数

```java
cardApi.init(baseCardListener,API_KEY,API_SECRET);
//第一个参数为所需接口
//第二个参数为配置的key
//第三个参数为配置的secret

```

#### 点击执行

```java
cardApi.play(cardType); //可选的样式,如:CardType.BC_CARDTYPE_BANKCARD,CardType.BC_CARDTYPE_IDCARD

```

#### 图片数据获取


```java
  @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        cardApi.inputScanImage(bytes, this.mCamera.getPreviewSize(),
                 this.mCameraPreview.convertViewRectToCameraPreview(this.mOverlayVi
 ew.getCardRect()),
                 this.mCamera.getRotationDegrees());
    }
//第一个参数位扫描图片数据
//第二个参数位图片的尺寸
//第三个参数位卡片区域
//第四个参数位摄像头方向

```

#### 上传文件的实现方式

```java
      cardApi.inputCard(CardType.BC_CARDTYPE_BANKCARD, idCardImagePath);
      //CardType.BC_CARDTYPE_IDCARD表示选择的是银行卡
      //idCardImagePath为银行卡图片的路径

```


#### 接口实现

实现接口进行校验。

```java
BaseCardListener baseCardListener =new BaseCardListener() {

	@Override
	public void onError(int code,String error) {
		//过程中出现的错误码以及错误描述
	}


    @Override
    public void onResult(String token) {
      //验证成功返回的token
    }

};

```

#### 页面关闭

在页面关闭的时候执行此方法。

```java
@Override
protected void onPause() {
 	super. onPause();
 	cardApi.cancel();
}

```

#### 混淆规则

```
-dontwarn com.geetest.oneperson.**
-keep class com.geetest.oneperson.** {
*;
}
```
-
### 护照OCR
#### 初始化

在项目的具体页面的`onCreate`方法里面进行初始化。

```java
cardApi = new CardApi(this);

```

#### 配置参数

```java
cardApi.init(baseCardListener,API_KEY,API_SECRET);
//第一个参数为所需接口
//第二个参数为配置的key
//第三个参数为配置的secret

```

#### 图片数据获取


```java
 cardApi.inputPassPortImage(passPortImagePath);
//passPortImagePath为护照图片的路径

```

#### 接口实现

实现接口进行校验。

```java
BaseCardListener baseCardListener =new BaseCardListener() {

	@Override
	public void onError(int code,String error) {
		//过程中出现的错误码以及错误描述
	}


    @Override
    public void onResult(String token) {
      //验证成功返回的token
    }

};

```

#### 页面关闭

在页面关闭的时候执行此方法。

```java
@Override
protected void onPause() {
 	super. onPause();
 	cardApi.cancel();
}

```

#### 混淆规则

```
-dontwarn com.geetest.oneperson.**
-keep class com.geetest.oneperson.** {
*;
}
```
-
### 静默活体检测
#### 初始化

在项目的具体页面的`onCreate`方法里面进行初始化。

```java
cardApi = new CardApi(this);

```

#### 配置参数

```java
cardApi.init(baseCardListener,API_KEY,API_SECRET);
//第一个参数为所需接口
//第二个参数为配置的key
//第三个参数为配置的secret

```

#### 视频数据获取


```java
 cardApi.inputSlientLiveness(path); //参数为视频保存的路径

```

#### 接口实现

实现接口进行校验。

```java
BaseCardListener baseCardListener =new BaseCardListener() {

	@Override
	public void onError(int code,String error) {
		//过程中出现的错误码以及错误描述
	}


    @Override
    public void onResult(String token) {
      //验证成功返回的token
    }

};

```

#### 页面关闭

在页面关闭的时候执行此方法。

```java
@Override
protected void onPause() {
 	super. onPause();
 	cardApi.cancel();
}

```

#### 混淆规则

```
-dontwarn com.geetest.oneperson.**
-keep class com.geetest.oneperson.** {
*;
}
```
-
### 人脸对比
#### 初始化

在项目的具体页面的`onCreate`方法里面进行初始化。

```java
cardApi = new CardApi(this);

```

#### 配置参数

```java
cardApi.init(baseCardListener,API_KEY,API_SECRET);
//第一个参数为所需接口
//第二个参数为配置的key
//第三个参数为配置的secret

```

#### 视频数据获取

**图片与图片对比**

```java
cardApi.inputImageVerification(firstImagePath, secondImagePath); //firstImagePath为第一张图片的路径
//secondImagePath为第二张图片的路径

```
**视频与图片对比**

```java
 cardApi.inputSilentImageVerification(videoFilePath, imageFilePath); //videoFilePath为视频的路径
//imageFilePath为照片的路径

```
#### 接口实现

实现接口进行校验。

```java
BaseCardListener baseCardListener =new BaseCardListener() {

	@Override
	public void onError(int code,String error) {
		//过程中出现的错误码以及错误描述
	}


    @Override
    public void onResult(String token) {
      //验证成功返回的token
    }

};

```

#### 页面关闭

在页面关闭的时候执行此方法。

```java
@Override
protected void onPause() {
 	super. onPause();
 	cardApi.cancel();
}

```

#### 混淆规则

```
-dontwarn com.geetest.oneperson.**
-keep class com.geetest.oneperson.** {
*;
}
```
-
### 人脸身份核验
#### 初始化

在项目的具体页面的`onCreate`方法里面进行初始化。

```java
cardApi = new CardApi(this);

```

#### 配置参数

```java
cardApi.init(baseCardListener,API_KEY,API_SECRET);
//第一个参数为所需接口
//第二个参数为配置的key
//第三个参数为配置的secret

```

#### 视频数据获取

**图片与身份核验**

```java
cardApi.inputIdNumber(path,name,idnumber); //path为选择图片的路径
//name为名字
//idnumber为身份证号

```
**视频与身份核验**

```java
 cardApi.inputSilentIdNumber(path,name,idnumber); //path为选择视频的路径
//name为名字
//idnumber为身份证号

```
#### 接口实现

实现接口进行校验。

```java
BaseCardListener baseCardListener =new BaseCardListener() {

	@Override
	public void onError(int code,String error) {
		//过程中出现的错误码以及错误描述
	}


    @Override
    public void onResult(String token) {
      //验证成功返回的token
    }

};

```

#### 页面关闭

在页面关闭的时候执行此方法。

```java
@Override
protected void onPause() {
 	super. onPause();
 	cardApi.cancel();
}

```

#### 混淆规则

```
-dontwarn com.geetest.oneperson.**
-keep class com.geetest.oneperson.** {
*;
}
```
-
### ErrorCode

ErrorCode	|Description
----------|------------
2001      |没有相关权限
2002      |没有网络
2003      |upload网络错误
2004      |bankcard网络错误
2005      |idcard网络错误
2006      |upload接口异常
2007      |bankcard接口异常
2008      |idcard接口异常
2009      |无效的参数设置
2010      |liveness网络错误
2011      |liveness接口异常
2012      |idnumber网络错误
2013      |idnumber接口异常
2014      |silentidnumber网络错误
2015      |silentidnumber接口异常
2016      |imageverification网络错误
2017      |imageverification接口异常
2018      |silentimageverification网络错误
2019      |silentimageverification接口异常
2022      |key或者secret为null
2023      |file文件不存在
2024      |passport网络错误
2025      |passport接口异常


