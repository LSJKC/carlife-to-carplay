package com.carplay.adapter.carlife;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;

/* JADX INFO: compiled from: CarLifeProtocol.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b1\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0005\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J&\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020\u00052\u0006\u0010I\u001a\u00020\u00052\u0006\u0010J\u001a\u00020\u00052\u0006\u0010K\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011¢\u0006\b\n\u0000\u001a\u0004\bA\u0010\u0013R\u000e\u0010B\u001a\u00020CX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010E\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006L"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeProtocol;", "", "<init>", "()V", "PORT_CMD", "", "PORT_VIDEO", "PORT_MEDIA", "PORT_TTS", "PORT_VR", "PORT_TOUCH", "PORT_DATA", "AUX_PORTS", "", "getAUX_PORTS", "()[I", "REQUIRED_AUX_PORTS", "", "getREQUIRED_AUX_PORTS", "()Ljava/util/Set;", "CHANNEL_CMD", "CHANNEL_VIDEO", "CHANNEL_MEDIA", "CHANNEL_TTS", "CHANNEL_VR", "CHANNEL_TOUCH", "HEADER_CMD", "HEADER_STREAM", "MAX_PAYLOAD", "HU_PROTOCOL_VERSION", "HU_INFO", "HU_AUTHEN_REQUEST", "HU_AUTHEN_RESULT", "HU_FEATURE_CONFIG_RESPONSE", "VIDEO_ENCODER_INIT", "VIDEO_ENCODER_START", "VIDEO_ENCODER_PAUSE", "VIDEO_ENCODER_RESET", "VIDEO_ENCODER_FRAME_RATE_CHANGE", "HU_STATISTIC_INFO", "PROTOCOL_VERSION_MATCH_STATUS", "MD_INFO", "VIDEO_ENCODER_INIT_DONE", "VIDEO_ENCODER_FRAME_RATE_CHANGE_DONE", "MD_AUTHEN_RESPONSE", "MD_AUTHEN_RESULT", "MD_FEATURE_CONFIG_REQUEST", "MD_EXIT", "MSG_TOUCH_ACTION", "MSG_TOUCH_ACTION_DOWN", "MSG_TOUCH_ACTION_UP", "MSG_TOUCH_ACTION_MOVE", "MSG_TOUCH_CAR_HARD_KEY_CODE", "STREAM_VIDEO_DATA", "STREAM_VIDEO_HEARTBEAT", "STREAM_MEDIA_INIT", "STREAM_MEDIA_STOP", "STREAM_MEDIA_PAUSE", "STREAM_MEDIA_RESUME", "STREAM_MEDIA_SEEK", "STREAM_MEDIA_DATA", "STREAM_TTS_INIT", "STREAM_TTS_END", "STREAM_TTS_DATA", "STREAM_IDS", "getSTREAM_IDS", "AUTHEN_RANDOM_VALUE", "", "PROTOCOL_MAJOR", "PROTOCOL_MINOR", "header", "", "channel", "serviceType", "payloadLength", "timestampSeconds", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeProtocol {
    public static final String AUTHEN_RANDOM_VALUE = "8.8.4_8.8.4";
    public static final int CHANNEL_CMD = 1;
    public static final int CHANNEL_MEDIA = 3;
    public static final int CHANNEL_TOUCH = 6;
    public static final int CHANNEL_TTS = 4;
    public static final int CHANNEL_VIDEO = 2;
    public static final int CHANNEL_VR = 5;
    public static final int HEADER_CMD = 8;
    public static final int HEADER_STREAM = 12;
    public static final int HU_AUTHEN_REQUEST = 98376;
    public static final int HU_AUTHEN_RESULT = 98378;
    public static final int HU_FEATURE_CONFIG_RESPONSE = 98386;
    public static final int HU_INFO = 98307;
    public static final int HU_PROTOCOL_VERSION = 98305;
    public static final int HU_STATISTIC_INFO = 98343;
    public static final int MAX_PAYLOAD = 67108864;
    public static final int MD_AUTHEN_RESPONSE = 65609;
    public static final int MD_AUTHEN_RESULT = 65611;
    public static final int MD_EXIT = 65625;
    public static final int MD_FEATURE_CONFIG_REQUEST = 65617;
    public static final int MD_INFO = 65540;
    public static final int MSG_TOUCH_ACTION = 425985;
    public static final int MSG_TOUCH_ACTION_DOWN = 425986;
    public static final int MSG_TOUCH_ACTION_MOVE = 425988;
    public static final int MSG_TOUCH_ACTION_UP = 425987;
    public static final int MSG_TOUCH_CAR_HARD_KEY_CODE = 425992;
    public static final int PORT_CMD = 7240;
    public static final int PROTOCOL_MAJOR = 1;
    public static final int PROTOCOL_MINOR = 0;
    public static final int PROTOCOL_VERSION_MATCH_STATUS = 65538;
    private static final Set<Integer> REQUIRED_AUX_PORTS;
    private static final Set<Integer> STREAM_IDS;
    public static final int STREAM_MEDIA_DATA = 196614;
    public static final int STREAM_MEDIA_INIT = 196609;
    public static final int STREAM_MEDIA_PAUSE = 196611;
    public static final int STREAM_MEDIA_RESUME = 196612;
    public static final int STREAM_MEDIA_SEEK = 196613;
    public static final int STREAM_MEDIA_STOP = 196610;
    public static final int STREAM_TTS_DATA = 262147;
    public static final int STREAM_TTS_END = 262146;
    public static final int STREAM_TTS_INIT = 262145;
    public static final int STREAM_VIDEO_DATA = 131073;
    public static final int STREAM_VIDEO_HEARTBEAT = 131074;
    public static final int VIDEO_ENCODER_FRAME_RATE_CHANGE = 98316;
    public static final int VIDEO_ENCODER_FRAME_RATE_CHANGE_DONE = 65549;
    public static final int VIDEO_ENCODER_INIT = 98311;
    public static final int VIDEO_ENCODER_INIT_DONE = 65544;
    public static final int VIDEO_ENCODER_PAUSE = 98314;
    public static final int VIDEO_ENCODER_RESET = 98315;
    public static final int VIDEO_ENCODER_START = 98313;
    public static final CarLifeProtocol INSTANCE = new CarLifeProtocol();
    public static final int PORT_VIDEO = 8240;
    public static final int PORT_MEDIA = 9240;
    public static final int PORT_TTS = 9241;
    public static final int PORT_VR = 9242;
    public static final int PORT_TOUCH = 9340;
    public static final int PORT_DATA = 9440;
    private static final int[] AUX_PORTS = {PORT_VIDEO, PORT_MEDIA, PORT_TTS, PORT_VR, PORT_TOUCH, PORT_DATA};

    private CarLifeProtocol() {
    }

    static {
        int[] $this$filter$iv = AUX_PORTS;
        Collection destination$iv$iv = new ArrayList();
        int length = $this$filter$iv.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                REQUIRED_AUX_PORTS = CollectionsKt.toSet((List) destination$iv$iv);
                STREAM_IDS = SetsKt.setOf((Object[]) new Integer[]{Integer.valueOf(STREAM_VIDEO_DATA), Integer.valueOf(STREAM_VIDEO_HEARTBEAT), Integer.valueOf(STREAM_MEDIA_INIT), Integer.valueOf(STREAM_MEDIA_STOP), Integer.valueOf(STREAM_MEDIA_PAUSE), Integer.valueOf(STREAM_MEDIA_RESUME), Integer.valueOf(STREAM_MEDIA_SEEK), Integer.valueOf(STREAM_MEDIA_DATA), Integer.valueOf(STREAM_TTS_INIT), Integer.valueOf(STREAM_TTS_END), Integer.valueOf(STREAM_TTS_DATA)});
                return;
            } else {
                int element$iv$iv = $this$filter$iv[i];
                if (element$iv$iv != 9440) {
                    destination$iv$iv.add(Integer.valueOf(element$iv$iv));
                }
                i++;
            }
        }
    }

    public final int[] getAUX_PORTS() {
        return AUX_PORTS;
    }

    public final Set<Integer> getREQUIRED_AUX_PORTS() {
        return REQUIRED_AUX_PORTS;
    }

    public final Set<Integer> getSTREAM_IDS() {
        return STREAM_IDS;
    }

    public final byte[] header(int channel, int serviceType, int payloadLength, int timestampSeconds) {
        int i;
        switch (channel) {
            case 1:
            case 6:
                i = 8;
                break;
            default:
                i = 12;
                break;
        }
        byte[] out = new byte[i];
        if (out.length == 8) {
            out[0] = (byte) (payloadLength >>> 8);
            out[1] = (byte) payloadLength;
            out[2] = 0;
            out[3] = 0;
            out[4] = (byte) (serviceType >>> 24);
            out[5] = (byte) (serviceType >>> 16);
            out[6] = (byte) (serviceType >>> 8);
            out[7] = (byte) serviceType;
        } else {
            out[0] = (byte) (payloadLength >>> 24);
            out[1] = (byte) (payloadLength >>> 16);
            out[2] = (byte) (payloadLength >>> 8);
            out[3] = (byte) payloadLength;
            out[4] = (byte) (timestampSeconds >>> 24);
            out[5] = (byte) (timestampSeconds >>> 16);
            out[6] = (byte) (timestampSeconds >>> 8);
            out[7] = (byte) timestampSeconds;
            out[8] = (byte) (serviceType >>> 24);
            out[9] = (byte) (serviceType >>> 16);
            out[10] = (byte) (serviceType >>> 8);
            out[11] = (byte) serviceType;
        }
        return out;
    }
}
