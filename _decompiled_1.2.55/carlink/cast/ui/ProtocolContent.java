package com.carlink.cast.ui;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;

/* JADX INFO: compiled from: ProtocolContent.kt */
/* JADX INFO: loaded from: classes14.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lcom/carlink/cast/ui/ProtocolContent;", "", "<init>", "()V", "sections", "", "Lcom/carlink/cast/ui/ProtocolSection;", "getSections", "()Ljava/util/List;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ProtocolContent {
    public static final ProtocolContent INSTANCE = new ProtocolContent();
    private static final List<ProtocolSection> sections = CollectionsKt.listOf((Object[]) new ProtocolSection[]{new ProtocolSection("第一次连接", "1. 在系统蓝牙设置里和车机配对。\n2. 打开本应用，确认「车机」一栏显示的是你的车。\n3. 在车机屏幕上选择本机（有的车机需要先点「连接手机」）。\n4. 回到本应用点「开始投屏」。\n\n配对只需做一次。之后打开应用点开始即可，车机会记住这台手机。"), new ProtocolSection("上车自动连接", "在设置里打开「上车自动连接」。之后只要车机蓝牙连上，应用会自己开始投屏，不用掏手机。\n\n注意：屏幕镜像模式每次都要手动授权录屏，系统不允许后台静默开启，所以自动连接只在 CarLife 模式下生效。"), new ProtocolSection("画面来源怎么选", "「CarLife 车机模式」：用手机自带的 CarLife 渲染车机界面，画面清晰、不用录屏授权，推荐优先使用。\n\n「屏幕镜像」：把手机屏幕整个投到车机，需要每次授权录屏，触控回传还要求开启无障碍服务。\n\n「自动」：装了 CarLife 就用 CarLife，没装才用镜像。"), new ProtocolSection("没有画面怎么办", "按顺序检查：\n1. 车机屏幕上是否已选择本机（这一步在车机上操作，不是手机）。\n2. 蓝牙是否已配对、是否打开。\n3. 点「停止投屏」再重新「开始投屏」。\n4. 如果之前进过车机原生界面，退回来时黑屏或透明，重新开始一次即可。"), new ProtocolSection("没有声音怎么办", "确认设置里「发送媒体音频」是打开的，然后重新开始投屏。\n\nCarLife 模式下声音由 CarLife 自己送到车机，应用只负责转发，所以请先把手机媒体音量调大，并确认车机音源切换到「手机」或「蓝牙」而不是收音机。"), new ProtocolSection("触控和按键", "触控回传需要在系统设置的无障碍里开启本应用的「CarLink 触控」。没开启时车机能看不能点。\n\n方向盘按键、电话键、语音键由车机通过 CarPlay 通道直接送来，不需要额外设置。语音键会调起手机上的语音助手；如果没反应，检查手机是否装了语音助手并已允许语音唤醒。"), new ProtocolSection("画面偏左或不居中", "部分车机的屏幕比 CarPlay 可显示区域更宽，会把画面放在最左侧。这是车机自身对非苹果设备的显示方式，目前无法在手机端修正；在车上把显示区域调整到中间后再重新连接试试。"), new ProtocolSection("出问题时导出日志", "设置 → 导出诊断日志，把 zip 发给我。日志只保存在你手机上，不会上传到任何服务器，也不包含 Wi-Fi 密码。\n\n日志里记录了每一步是否成功、画面和声音有没有发出去，比描述现象更有用。"), new ProtocolSection("隐私", "不需要 root，不修改系统，不读取通讯录、短信和照片。\n配对密钥保存在系统密钥库里；诊断日志只存在本机，可以随时删除。")});

    private ProtocolContent() {
    }

    public final List<ProtocolSection> getSections() {
        return sections;
    }
}
