记录IPC学习过程

## Android中的多进程模式
- 如何设置多进程？  
设置多进程其实很简单，但是要用好多进程就不是那么简单了。在配置Activity是多加一个`android:process`设置就这么简单的一句。  
- 总结了会造成的几个问题  
1. 静态变量和单例模式完全失效
2. 线程同步机制完全失效
3. `SharePreferences`的可靠性下降
4. `Application`多次被创建
## IPC基础概念
- Serializable接口  
序列号和反序列化都很简单，只要声明一个`serialVersionUID`就可实现序列化，采用`ObjectOutputStream`和`ObjectInputStream`就可轻松将对象序列化到文件或者反序列化读取文件
- Parcelable接口  
实现序列化可通过`Intent`和`Binder`传递，在Android中使用消耗更少。

总结：本地存储以及网络传输优先考虑`Serializable`,进程间通信优先考虑`Parcelable`

- Android中IPC的方式
1. 使用Bundle  
可通过`Bundle`传递被序列化对象，实现进程间通信。
2. 文件共享  
不同的进程读写同一文件实现进程间通信。
3. 使用Messenger  
使用`Messenger`和`Message`进行进程间通信,主要以客户端和服务端的形式进行通信。不适合并发大的。
4. 使用AIDL  
支持哪些数据？  
(1) 基本类型数据 (2) List,只支持ArrayList (3)Map,只支持HashMap (4)Parcelable (5)AIDL本身可使用的 (6)String和CharSquence。  
防止恶意调用可加权限验证。
5. 使用ContentProvider  
`DbOpenHelper`,`SQLiteDatabase`等进行处理。
6. 使用Socket  
简单`Socket`操作

- Binder连接池  
简单的说，通过`BinderPool`获取到不同类型的`Binder`,再进行调用。

![image](/img/ipc.png)