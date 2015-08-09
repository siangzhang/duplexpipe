# 穿越防火墙 #

你是否曾经尝试过去连接一台远程计算机，却因为被防火墙拦截或路由器没有转发而造成无法通信？这是主动式连接的一个弊端：它依赖服务器的状态，而对服务器有生杀大权的只有管理员。如果能让服务器主动尝试连接我们的计算机，那就没问题了！因为防火墙、路由等一般不会过滤向外的连接（反弹式木马就是利用这一原理）。

但以往的服务端程序都是采用监听本地端口的方式（比如` Windows `远程桌面程序监听本地` 3389 `端口），我们需要将它的连接方式改成主动连接外部网络的方式。我想你也一定要将想到（如果你看过《[DuplexPipe二三事（四）](http://blog.csdn.net/redraiment/archive/2009/09/04/4518794.aspx)》），用` DuplexPipe`来转换它的连接方式！

# 反弹式远程桌面 #

在此以` Windows `远程桌面为例，演示如何用一个正向连接的程序实现反向连接。本次演示的环境：

  1. 主机`A`（客户端）：处于外网，`IP `地址为` 60.180.*.*`。系统环境为` Vista `家庭版，自带远程桌面连接客户端；
  1. 主机`B`（服务端）：处于内网。系统环境为` Win XP SP2`，并开启了远程桌面服务。

具体操作步骤如下：

一、将客户端的“连接式”转换为“监听式”。
```
打开命令提示符，执行： java -jar DuplexPipe.jar -l 1234 -l 3389
```
![http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/client.jpg](http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/client.jpg)

二、将服务器端“监听式”转换为“连接式”，并连接客户端监听的 1234 端口。
```
打开命令提示符，执行： java -jar DuplexPipe.jar -c 60.180.*.* 1234 -c localhost 3389
```
![http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/server.jpg](http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/server.jpg)

三、启动客户端上的远程桌面连接程序，连接本地的 3389 （默认）端口，即可连接成功！

![http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/term.jpg](http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/term.jpg)

连接成功后：

![http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/localhost.jpg](http://p.blog.csdn.net/images/p_blog_csdn_net/redraiment/EntryImages/20090904/localhost.jpg)

# 总结 #

如果你能理解上面的操作，就可以照样画葫芦去连接任何其他程序！我在前面提到过，`DuplexPipe `目前还有待完善，那它以后还会添加些什么功能？会不会发展成为一个反弹式木马？请看《[DuplexPipe 二三事（六）](http://blog.csdn.net/redraiment/archive/2009/09/05/4522213.aspx)》。