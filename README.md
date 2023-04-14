# GroupYouWant
> 「你想要的插件」系列作品

[![](https://shields.io/github/downloads/MrXiaoM/GroupYouWant/total)](https://github.com/MrXiaoM/GroupYouWant/releases) [![](https://img.shields.io/badge/mirai--console-2.14.0-blue)](https://github.com/mamoe/mirai) [![](https://img.shields.io/badge/MiraiForum-post-yellow)](https://mirai.mamoe.net/topic/2184)

只允许用户加入其中一个联合群聊

## 简介

灵感来源于论坛坛友，将几个群添加到联合群聊配置中后，  
运行配置检查，当联合群聊的所有群中，有群员在两个或以上的群聊时，将会把群员踢出群，只留他在其中的一个群。
配置检查会在用户申请入群的时候运行，或者使用命令手动运行。

## 用法

本插件除了首次加载之外不会有任何写配置的行为，你可以放心在 mirai 开启的情况下编辑配置文件。

第一次启动该插件，会生成默认配置 `/config/top.mrxiaom.groupyouwant/groups/default.yml`。  
编辑该配置文件，根据配置文件中的注释补全信息，然后执行命令 `/guw reload` 重载配置文件。

使用命令 `/guw list` 查看已加载的配置。  
使用命令 `/guw check [配置]` 来运行配置检查，以便踢出重复群员。  
使用命令 `/guw check` 可运行所有的配置检查。

拥有权限 `top.mrxiaom.groupyouwant:bypass` 的人将不会被检查和踢出。(详见 [Permission 模块](https://docs.mirai.mamoe.net/console/Permissions.html#%E4%BD%BF%E7%94%A8%E5%86%85%E7%BD%AE%E6%9D%83%E9%99%90%E6%9C%8D%E5%8A%A1%E6%8C%87%E4%BB%A4))  
> 给予权限示例: `/perm permit u114514 top.mrxiaom.groupyouwant:bypass`  
> 撤销权限示例: `/perm cancel u114514 top.mrxiaom.groupyouwant:bypass`

在配置文件中可配置在其中一个群有管理员的群员不会被检查和踢出。

如果你需要添加更多的联合群聊配置，复制默认配置，修改文件名称，对配置文件做出修改，使用命令重载配置文件即可

## 下载

到 [Releases](https://github.com/MrXiaoM/GroupYouWant/releases) 下载插件并放入 plugins 文件夹进行安装

> 下载 GroupYouWant-*.mirai2.jar  
> 从 2.12.0-RC 起，mirai 修复了配置文件子路径问题，故本插件仅能运行在 mirai 2.12.0-RC 或更高版本。

安装完毕后，编辑配置文件作出你想要的修改。在控制台执行 /guw reload 重载配置即可~

## 捐助

前往 [爱发电](https://afdian.net/a/mrxiaom) 捐助我。
