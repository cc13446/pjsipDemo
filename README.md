# pjsipDemo

An application based on PJSIP-Android.

# FreeSwitch Install
[FreeSwitch本体安装](https://freeswitch.org/confluence/display/FREESWITCH/Debian+10+Buster)

[mod_sms](https://freeswitch.org/confluence/display/FREESWITCH/mod_sms)

[mod_python](https://freeswitch.org/confluence/display/FREESWITCH/mod_python)


# freeswitch问题

### Context public not found

默认根据[mod_sms文档](https://freeswitch.org/confluence/display/FREESWITCH/mod_sms)设置`chatplan`出现如下保存，根据提示发现`public`未找到。

```
2019-02-27 17:01:01.225983 [WARNING] mod_sms.c:397 Context public not found
```

解决办法：

修改`conf/chatplan/default.xml`中的`<context name="default">`为`<context name="public">`即可解决。



### Not sending message to ourselves!

[转自](https://blog.csdn.net/zc131/article/details/100780700)

通过在/usr/local/freeswitch/conf/chatplan/default.xml文件中添加以下查看信息

```xml
<action application="info"/>
```

ip地址172开头的是freeswitch服务器内网ip，47开头的是freeswitch服务器外网ip

```log
2019-09-12 14:34:32.244630 [INFO] mod_sms.c:495 CHANNEL_DATA:
Event-Name: [MESSAGE]
Core-UUID: [80c7b344-790f-438b-ab06-67f60a97f191]
FreeSWITCH-Hostname: [iZwz931q3yfo0j7tf9klmvZ]
FreeSWITCH-Switchname: [iZwz931q3yfo0j7tf9klmvZ]
FreeSWITCH-IPv4: [172.*.*.*]
FreeSWITCH-IPv6: [::1]
Event-Date-Local: [2019-09-12 14:34:32]
Event-Date-GMT: [Thu, 12 Sep 2019 06:34:32 GMT]
Event-Date-Timestamp: [1568270072244630]
Event-Calling-File: [sofia_presence.c]
Event-Calling-Function: [sofia_presence_handle_sip_i_message]
Event-Calling-Line-Number: [4896]
Event-Sequence: [15092]
login: [sip:mod_sofia@47.*.*.*:5060]
proto: [sip]
to_proto: [sip]
from: [1002@47.*.*.*]
from_user: [1002]
from_host: [47.*.*.*]
to_user: [1000]
to_host: [47.*.*.*]
from_sip_ip: [14.*.*.*]
from_sip_port: [25922]
to: [1000@47.*.*.*]
subject: [SIMPLE MESSAGE]
context: [public]
type: [text/plain]
from_full: [<sip:1002@47.*.*.*>;tag=n9gRKhdT8]
sip_profile: [internal]
dest_proto: [sip]
max_forwards: [70]
DP_MATCH: [1000@47.*.*.*]
Content-Length: 8
12345678910111213141516171819202122232425262728293031323334
```

发现目标地址`to: [1000@47.*.*.*]`是指定的外网的ip，而不是内网的ip，通过测试发现消息只能通过指定内网ip发送

```bash
fs_cli -x "chat sip|1002|1000@172.*.*.*|hello world"
Sent
fs_cli -x "chat sip|1002|1000@47.*.*.*|hello world"
Error! Message Not Sent
```

解决办法（通过嵌入式脚本修改目标地址）

修改 /usr/local/freeswitch/conf/chatplan/default.xml 文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<include>
  <context name="public">
    <extension name="demo">
      <condition field="to" expression="^(.*)$">
        <action application="python" data="handle_chat"/>
      </condition>
    </extension>
  </context>
</include>
```

添加/usr/local/freeswitch/scripts/handle_chat.py

```python
def chat(message, args):
    message.addHeader("to_host", "172.*.*.*")
    message.addHeader("to", "{to_user}@{to_host}".format(to_user=message.getHeader("to_user"), to_host=message.getHeader("to_host")))
```

执行reloadxml

```bash
fs_cli -x "reloadxml"
```

问题解决。

