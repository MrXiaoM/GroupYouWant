package top.mrxiaom.groupyouwant

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import top.mrxiaom.groupyouwant.GroupYouWant.reload


object Command : CompositeCommand(
    owner = GroupYouWant,
    primaryName = "groupyouwant",
    secondaryNames = arrayOf("guw"),
    description = "群聊联合插件主命令",
    parentPermission = GroupYouWant.parentPermission
) {
    @SubCommand
    @Description("重载配置文件")
    suspend fun reload(sender: CommandSender) {
        PluginConfig.reload()
        GroupYouWant.reloadGroupConfig()
        sender.sendMessage("配置文件已重载")
    }
    @OptIn(ConsoleExperimentalApi::class)
    @SubCommand
    @Description("检查重复群员 (不填配置时检查全部)")
    suspend fun check(
        sender: CommandSender,
        @Name("配置") config: String? = null
    ) {
        if (config == null) {
            sender.sendMessage("正在执行检查 *全部配置")
            for (cfg in GroupYouWant.groupsList) GroupYouWant.check(cfg)
        }
        else {
            val cfg = GroupYouWant.groupsList.firstOrNull { it.name.equals(config, true) }
            if (cfg == null) {
                sender.sendMessage("配置 $config 不存在")
                return
            }
            sender.sendMessage("正在执行检查 $config")
            GroupYouWant.check(cfg)
        }
        sender.sendMessage("检查完毕")
    }
    @SubCommand
    @Description("查看所有已加载的配置")
    suspend fun list(sender: CommandSender) {
        sender.sendMessage(GroupYouWant.groupsList.map {
            "${it.name}:\n\t" + listOf(
                "机器人: ${it.bot}",
                "群聊列表:",
                *it.groups.keys.map { "\t$it" }.toTypedArray()
            ).joinToString("\n\t")
        }.joinToString("\n"))
    }
}