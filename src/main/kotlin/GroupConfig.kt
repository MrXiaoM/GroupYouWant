package top.mrxiaom.groupyouwant

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

class GroupConfig(
    val name: String
) : ReadOnlyPluginConfig("groups/$name") {
    @ValueDescription("启用该配置的机器人账号")
    val bot by value(123456L)

    @ValueDescription(
        "在该组内的群聊列表及其最大人数\n" +
                "因 mirai 目前无法获取群聊最大人数，只能由用户自己定义最大人数。\n" +
                "格式如下\n" +
                "  群号: 最大人数"
    )
    val groups by value(
        mapOf(
            114514L to 500,
            1919810L to 1000
        )
    )

    @ValueDescription("当用户在所有群的其中一个群有管理员时，不将其视为重复群员")
    val ignoreAdmin by value(true)

    @ValueDescription("踢出时是否勾选“不再接受此人的加群申请”")
    val kickBlock by value(false)

    @ValueDescription("踢出消息，”踢出消息“是旧版本QQ遗留，可不配置此项")
    val kickMessage by value("你已加入过群组中的其中一个群聊")

    @ValueDescription("踢出群员延迟 (ms, 1000ms = 1s)")
    val kickDelay by value(1000L)

    @ValueDescription("拒绝时是否勾选“不再接受此人的加群申请”")
    val rejectBlock by value(false)

    @ValueDescription("拒绝加群申请理由")
    val rejectMessage by value("你已加入过群组中的其中一个群聊")

    @ValueDescription("群满人时自动拒绝")
    val rejectFull by value(true)

    @ValueDescription("群满人拒绝加群申请理由")
    val rejectMessageFull by value("该群已达最大人数")

    @ValueDescription("拒绝加群时运行检查配置")
    val rejectCheck by value(true)

    @ValueDescription("自动同意进群")
    val autoAccept by value(false)

}