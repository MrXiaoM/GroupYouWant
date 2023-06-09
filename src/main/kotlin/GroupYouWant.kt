package top.mrxiaom.groupyouwant

import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.events.MemberJoinRequestEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info
import java.io.File

object GroupYouWant : KotlinPlugin(
    JvmPluginDescription(
        id = "top.mrxiaom.groupyouwant",
        name = "GroupYouWant",
        version = "0.1.1",
    ) {
        author("MrXiaoM")
    }
) {
    internal val groupsList = mutableListOf<GroupConfig>()
    lateinit var permBypass: Permission
    override fun onEnable() {
        permBypass = PermissionService.INSTANCE.register(permissionId("bypass"), "绕过配置检查")

        PluginConfig.reload()
        reloadGroupConfig()
        val channel = globalEventChannel(coroutineContext).parentScope(this)
        channel.subscribeAlways<MemberJoinRequestEvent> { event ->
            groupsList.firstOrNull { it.bot == bot.id && it.groups.containsKey(groupId) }?.apply {
                // 检查绕过权限
                if (permBypass.isUserHas(fromId)) return@subscribeAlways

                val group = event.group ?: return@subscribeAlways
                val groupSize = groups[groupId] ?: return@subscribeAlways
                if (group.members.size >= groupSize && rejectFull) {
                    reject(false, rejectMessageFull)
                    return@subscribeAlways
                }

                val memberInGroups = groups.mapNotNull { event.bot.getGroup(it.key)?.getMember(event.fromId) }
                // 不拒绝群主
                if (!memberInGroups.noneOwner) return@subscribeAlways
                // 不拒绝管理员
                if (ignoreAdmin && !memberInGroups.noneAdmin) return@subscribeAlways
                // 用户在1个或以上的群时拒绝
                if (memberInGroups.isNotEmpty()) {
                    event.reject(rejectBlock, rejectMessage)
                    // 运行检查配置
                    if (memberInGroups.size > 1 && rejectCheck) {
                        check(this)
                    }
                }
                // 自动接受
                else if (autoAccept) event.accept()
            }
        }
        Command.register(true)
        logger.info { "Plugin loaded" }
    }

    fun reloadGroupConfig() {
        groupsList.clear()
        val path = File(configFolder, "groups")
        if (!path.exists()) {
            groupsList.add(GroupConfig("default").also { it.save() })
            path.mkdirs()
        } else for (file in path.also { it.mkdirs() }.listFiles()) {
            groupsList.add(GroupConfig(file.nameWithoutExtension))
        }
    }

    suspend fun check(config: GroupConfig) {
        val bot = Bot.getInstanceOrNull(config.bot)
        if (bot == null) {
            logger.warning("配置 ${config.name}.yml 检查失败: 机器人 ${config.bot} 不在线")
            return
        }
        // 筛选出机器人有权限的群
        val groups = config.groups.mapNotNull { bot.getGroup(it.key) }.filter { it.botPermission.isOperator() }
        // 收集所有群员
        val kickMembers = groups.flatMap { it.members }.groupBy { it.id }
            .asSequence()
            // 筛选出没有绕过权限的人
            .filterNot { permBypass.isUserHas(it.key) }
            // 筛选出不是群主的人
            .filter { it.value.noneOwner }
            // 筛选出没有管理员的人
            .filter { !config.ignoreAdmin || it.value.noneAdmin }
            // 筛选出在多个群内的人
            .filter { it.value.size > 1 }
            // 踢出除第一个群以外
            .map { list -> list.value.also { it.drop(0) } }.flatMap { it }
        for (member in kickMembers) {
            member.kick(config.kickMessage, config.kickBlock)
            if (config.kickDelay > 0) delay(config.kickDelay)
        }
        logger.info("配置 ${config.name} 踢出了 ${kickMembers.count()} 人")
    }
}

fun Permission.isUserHas(userId: Long): Boolean {
    return AbstractPermitteeId.ExactUser(userId).hasPermission(this)
}

val Iterable<Member>.noneAdmin: Boolean
    get() = none { it.isAdministrator() }
val Iterable<Member>.noneOwner: Boolean
    get() = none { it.isOwner() }

