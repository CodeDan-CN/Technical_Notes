# 搭建FTP文件服务器

>  首先搭建文件服务器之前要说明一下FTP文件服务器的两种不同的连接方式，即主动连接以及被动连接。

### 主动连接与被动连接

##### 主动连接(PORT)

在主动模式下，FTP客户端随机开启一个大于1024的端口(N)向文件服务器的21号端口发起连接，发送FTP用户名和密码，然后FTP客户端开放N+1号端口进行监听，并通过21端口向服务器发出PORT N+1命令，告诉服务端客户端采用主动模式并开放了N+1端口。FTP服务器接收到PORT命令后，会用其本地的FTP数据端口（通常是20）来连接客户端指定的端口N+1，进行数据传输。



##### 被动连接(PASV)

在被动模式下，FTP客户端随机开启一个大于1024的端口(N)向服务器的21号端口发起连接，发送用户名和密码进行登陆，同时会本身的开启N+1端口。然后向服务器发送PASV命令，通知服务器自己处于被动模式。服务器收到命令后，会开放一个大于1024的端口P（端口P的范围是可以设置的）进行监听，然后用PORT P命令通知客户端，自己的数据端口是P。客户端收到命令后，会通过N+1号端口连接服务器的端口P，然后在两个端口之间进行数据传输。



### 搭建FTP文件服务器(网络安装以及被动模式)

##### 一、查看是否安装了vsftpd

```
> vsftpd -version
```



##### 二、进行vsftpd 服务端的安装

```tex
> yum install -y vsftpd
```



##### 三、创建FTP文件服务器中指定的目录(这个目录可随意)

```tex
> mkdir -p /data/ftp
```



##### 四、创建专属用于FTP协议操作文件的账户

```tex
//username为你为该ftp创建的用户名，/data/ftp 为登录进去后的位置
> useradd -d /data/ftp -s /bin/bash username
//为新建的用户设置密码
> passwd username
```



##### 五、设置目录权限

```tex
将/data/ftp目录权限设置为username用户，否则ftp客户端将无法写入文件
> chown -Rc guoke. /data/ftp
```



##### 六、防火墙添加FTP服务(如果不打算开防火墙这步可忽略)

```tex
> firewall-cmd --permanent --zone=public --add-service=ftp
> firewall-cmd --reload
```



##### 七、修改Selinux（如果不打算开Selinux这步可忽略）

```tex
> setsebool -P ftpd_full_access on
```

如果开启失败，那就需要去进行配置文件的修改后再启动了

```tex
> sudo vim /etc/selinux/config
```

```pro
//找到SELINUX=disabled
SELINUX = disabled
//强制模式SELINUX=enforcing：表示所有违反安全策略的行为都将被禁止。
//宽容模式SELINUX=permissive：表示所有违反安全策略的行为不被禁止，但是会在日志中作记录。
```

```tex
//修改完成后执行这个，创建隐藏文件，怕你修改错了直接重启失败
touch /.autorelabel
```

保存退出之后直接重启linux服务器即可



##### 八、配置ftp服务器配置文件(被动模式)

```properties
# Example config file /etc/vsftpd/vsftpd.conf
#
# The default compiled in settings are fairly paranoid. This sample file
# loosens things up a bit, to make the ftp daemon more usable.
# Please see vsftpd.conf.5 for all compiled in defaults.
# READ THIS: This example file is NOT an exhaustive list of vsftpd options.
# Please read the vsftpd.conf.5 manual page to get a full idea of vsftpd's
# capabilities.
#
# Allow anonymous FTP? (Beware - allowed by default if you comment this out).
anonymous_enable=NO
#
# Uncomment this to allow local users to log in.
# When SELinux is enforcing check for SE bool ftp_home_dir
local_enable=YES
#
# Uncomment this to enable any form of FTP write command.
write_enable=YES
#
# Default umask for local users is 077. You may wish to change this to 022,
# if your users expect that (022 is used by most other ftpd's)
local_umask=022
#
# Uncomment this to allow the anonymous FTP user to upload files. This only
# has an effect if the above global write enable is activated. Also, you will
# obviously need to create a directory writable by the FTP user.
# When SELinux is enforcing check for SE bool allow_ftpd_anon_write, allow_ftpd_full_access
#anon_upload_enable=YES
#
# Uncomment this if you want the anonymous FTP user to be able to create
# new directories.
#anon_mkdir_write_enable=YES
#
# Activate directory messages - messages given to remote users when they
# go into a certain directory.
dirmessage_enable=YES
#
# Activate logging of uploads/downloads.
xferlog_enable=YES
#
# Make sure PORT transfer connections originate from port 20 (ftp-data).
connect_from_port_20=YES
#
# If you want, you can arrange for uploaded anonymous files to be owned by
# a different user. Note! Using "root" for uploaded files is not
# recommended!
#chown_uploads=YES
#chown_username=whoever
#
# You may override where the log file goes if you like. The default is shown
# below.
#xferlog_file=/var/log/xferlog
#
# If you want, you can have your log file in standard ftpd xferlog format.
# Note that the default log file location is /var/log/xferlog in this case.
xferlog_std_format=YES
#
# You may change the default value for timing out an idle session.
#idle_session_timeout=600
#
# You may change the default value for timing out a data connection.
#data_connection_timeout=120
#
# It is recommended that you define on your system a unique user which the
# ftp server can use as a totally isolated and unprivileged user.
#nopriv_user=ftpsecure
#
# Enable this and the server will recognise asynchronous ABOR requests. Not
# recommended for security (the code is non-trivial). Not enabling it,
# however, may confuse older FTP clients.
#async_abor_enable=YES
#
# By default the server will pretend to allow ASCII mode but in fact ignore
# the request. Turn on the below options to have the server actually do ASCII
# mangling on files when in ASCII mode. The vsftpd.conf(5) man page explains
# the behaviour when these options are disabled.
# Beware that on some FTP servers, ASCII support allows a denial of service
# attack (DoS) via the command "SIZE /big/file" in ASCII mode. vsftpd
# predicted this attack and has always been safe, reporting the size of the
# raw file.
# ASCII mangling is a horrible feature of the protocol.
ascii_upload_enable=YES
ascii_download_enable=YES
#
# You may fully customise the login banner string:
#ftpd_banner=Welcome to blah FTP service.
# You may specify an explicit list of local users to chroot() to their home
# directory. If chroot_local_user is YES, then this list becomes a list of
# users to NOT chroot().
# (Warning! chroot'ing can be very dangerous. If using chroot, make sure that
# the user does not have write access to the top level directory within the
# chroot)
chroot_local_user=YES
chroot_list_enable=YES
# (default follows)
chroot_list_file=/etc/vsftpd/chroot_list
#
# You may activate the "-R" option to the builtin ls. This is disabled by
# default to avoid remote users being able to cause excessive I/O on large
# sites. However, some broken FTP clients such as "ncftp" and "mirror" assume
# the presence of the "-R" option, so there is a strong case for enabling it.
#ls_recurse_enable=YES
#
# When "listen" directive is enabled, vsftpd runs in standalone mode and
# listens on IPv4 sockets. This directive cannot be used in conjunction
# with the listen_ipv6 directive.
listen=NO
#
# This directive enables listening on IPv6 sockets. By default, listening
# on the IPv6 "any" address (::) will accept connections from both IPv6
# and IPv4 clients. It is not necessary to listen on *both* IPv4 and IPv6
# sockets. If you want that (perhaps because you want to listen on specific
# addresses) then you must run two copies of vsftpd with two configuration
# files.
# Make sure, that one of the listen options is commented !!
listen_ipv6=YES

pam_service_name=vsftpd
userlist_enable=YES
tcp_wrappers=YES
allow_writeable_chroot=YES
# 访问路径
local_root=/data/ftp
# 是否为被动模式
pasv_enable=YES
# 被动模式开放端口下限
pasv_min_port=20200
# 被动模式开放端口上限
pasv_max_port=20300
```



##### 九、在chroot_list中添加guoke用户

```propr
vim /etc/vsftpd/chroot_list
```

在里面写入你之前添加的username即可



##### 十、启动、重启

```tex
# 启动
systemctl start  vsftpd.service

# 停止
systemctl stop  vsftpd.service

# 设置开机启动
systemctl enable vsftpd.service
```



##### 十一、禁止ftp用户ssh登录(可选)

由于需要限制ftp用户在自己的目录，在21端口下没有问题，但当ftp用户用sftp登录时，还是可以访问上级目录，于是禁止ftp用户ssh登录，切断22端口的通信。

查看`/etc/shells`文件，看禁止登录的shell为`/usr/sbin/nologin`。如果没有，在文件后面添加

```bash
usermod -s /usr/sbin/nologin guoke
```

如果要恢复guoke的ssh登录

```bash
usermod -s /bin/bash guoke
```