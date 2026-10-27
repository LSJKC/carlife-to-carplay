package com.carplay.iap2.catalog;

import androidx.core.app.NotificationCompat;
import com.carplay.iap2.transport.Iap2IdentificationClient;
import com.carplay.iap2.transport.Iap2LocationMessages;
import com.carplay.iap2.transport.Iap2VehicleStatus;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import org.bouncycastle.pqc.crypto.crystals.kyber.KyberEngine;

/* JADX INFO: compiled from: Iap2Endpoint.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b{\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0013\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u00052\b\u0010\u0087\u0001\u001a\u00030\u0086\u0001J\u0011\u0010\u0088\u0001\u001a\u00020\u00052\b\u0010\u0087\u0001\u001a\u00030\u0086\u0001JP\u0010\u0089\u0001\u001a\u00020\u00052\b\u0010\u0087\u0001\u001a\u00030\u0086\u00012\b\u0010\u008a\u0001\u001a\u00030\u008b\u00012\b\u0010\u008c\u0001\u001a\u00030\u008b\u00012\b\u0010\u008d\u0001\u001a\u00030\u008e\u00012\n\b\u0002\u0010\u008f\u0001\u001a\u00030\u0090\u00012\u0011\b\u0002\u0010\u0091\u0001\u001a\n\u0012\u0005\u0012\u00030\u0092\u00010\u0081\u0001H\u0002J1\u0010\u0093\u0001\u001a\u00020\u00052\b\u0010\u0087\u0001\u001a\u00030\u0086\u00012\b\u0010\u008a\u0001\u001a\u00030\u008b\u00012\b\u0010\u008c\u0001\u001a\u00030\u008b\u00012\b\u0010\u008d\u0001\u001a\u00030\u008e\u0001H\u0002JS\u0010\u0094\u0001\u001a\u00030\u0092\u00012\b\u0010\u0087\u0001\u001a\u00030\u0086\u00012\b\u0010\u008a\u0001\u001a\u00030\u008b\u00012\b\u0010\u0095\u0001\u001a\u00030\u0096\u00012\n\b\u0002\u0010\u0097\u0001\u001a\u00030\u0098\u00012\n\b\u0002\u0010\u0099\u0001\u001a\u00030\u0098\u00012\u0011\b\u0002\u0010\u009a\u0001\u001a\n\u0012\u0005\u0012\u00030\u0092\u00010\u0081\u0001H\u0002R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0011\u0010\n\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u0011\u0010\f\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0007R\u0011\u0010\u000e\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0007R\u0011\u0010\u0010\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0007R\u0011\u0010\u0012\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0007R\u0011\u0010\u0014\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R\u0011\u0010\u0016\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0007R\u0011\u0010\u0018\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0007R\u0011\u0010\u001a\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0007R\u0011\u0010\u001c\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0007R\u0011\u0010\u001e\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0007R\u0011\u0010 \u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0007R\u0011\u0010\"\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0007R\u0011\u0010$\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0007R\u0011\u0010&\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u0007R\u0011\u0010(\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0007R\u0011\u0010*\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0007R\u0011\u0010,\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0007R\u0011\u0010.\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b/\u0010\u0007R\u0011\u00100\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b1\u0010\u0007R\u0011\u00102\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b3\u0010\u0007R\u0011\u00104\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b5\u0010\u0007R\u0011\u00106\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b7\u0010\u0007R\u0011\u00108\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b9\u0010\u0007R\u0011\u0010:\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b;\u0010\u0007R\u0011\u0010<\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b=\u0010\u0007R\u0011\u0010>\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b?\u0010\u0007R\u0011\u0010@\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bA\u0010\u0007R\u0011\u0010B\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bC\u0010\u0007R\u0011\u0010D\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bE\u0010\u0007R\u0011\u0010F\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bG\u0010\u0007R\u0011\u0010H\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bI\u0010\u0007R\u0011\u0010J\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bK\u0010\u0007R\u0011\u0010L\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bM\u0010\u0007R\u0011\u0010N\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bO\u0010\u0007R\u0011\u0010P\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bQ\u0010\u0007R\u0011\u0010R\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bS\u0010\u0007R\u0011\u0010T\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bU\u0010\u0007R\u0011\u0010V\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bW\u0010\u0007R\u0011\u0010X\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bY\u0010\u0007R\u0011\u0010Z\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b[\u0010\u0007R\u0011\u0010\\\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b]\u0010\u0007R\u0011\u0010^\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b_\u0010\u0007R\u0011\u0010`\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\ba\u0010\u0007R\u0011\u0010b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bc\u0010\u0007R\u0011\u0010d\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\be\u0010\u0007R\u0011\u0010f\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bg\u0010\u0007R\u0011\u0010h\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bi\u0010\u0007R\u0011\u0010j\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bk\u0010\u0007R\u0011\u0010l\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bm\u0010\u0007R\u0011\u0010n\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bo\u0010\u0007R\u0011\u0010p\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bq\u0010\u0007R\u0011\u0010r\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bs\u0010\u0007R\u0011\u0010t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bu\u0010\u0007R\u0011\u0010v\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bw\u0010\u0007R\u0011\u0010x\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\by\u0010\u0007R\u0011\u0010z\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b{\u0010\u0007R\u0011\u0010|\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b}\u0010\u0007R\u0011\u0010~\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u007f\u0010\u0007R\u001b\u0010\u0080\u0001\u001a\t\u0012\u0004\u0012\u00020\u00050\u0081\u0001¢\u0006\n\n\u0000\u001a\u0006\b\u0082\u0001\u0010\u0083\u0001R\u001d\u0010\u0084\u0001\u001a\u0010\u0012\u0005\u0012\u00030\u0086\u0001\u0012\u0004\u0012\u00020\u00050\u0085\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u009b\u0001"}, d2 = {"Lcom/carplay/iap2/catalog/Iap2Endpoints;", "", "<init>", "()V", "START_IDENTIFICATION", "Lcom/carplay/iap2/catalog/Iap2Endpoint;", "getSTART_IDENTIFICATION", "()Lcom/carplay/iap2/catalog/Iap2Endpoint;", "IDENTIFICATION_INFORMATION", "getIDENTIFICATION_INFORMATION", "IDENTIFICATION_ACCEPTED", "getIDENTIFICATION_ACCEPTED", "IDENTIFICATION_REJECTED", "getIDENTIFICATION_REJECTED", "CANCEL_IDENTIFICATION", "getCANCEL_IDENTIFICATION", "IDENTIFICATION_INFORMATION_UPDATE", "getIDENTIFICATION_INFORMATION_UPDATE", "REQUEST_AUTHENTICATION_CERTIFICATE", "getREQUEST_AUTHENTICATION_CERTIFICATE", "AUTHENTICATION_CERTIFICATE", "getAUTHENTICATION_CERTIFICATE", "REQUEST_AUTHENTICATION_CHALLENGE_RESPONSE", "getREQUEST_AUTHENTICATION_CHALLENGE_RESPONSE", "AUTHENTICATION_RESPONSE", "getAUTHENTICATION_RESPONSE", "AUTHENTICATION_FAILED", "getAUTHENTICATION_FAILED", "AUTHENTICATION_SUCCEEDED", "getAUTHENTICATION_SUCCEEDED", "AUTHENTICATION_CERTIFICATE_SERIAL", "getAUTHENTICATION_CERTIFICATE_SERIAL", "REQUEST_DEVICE_CERTIFICATE", "getREQUEST_DEVICE_CERTIFICATE", "DEVICE_AUTHENTICATION_CERTIFICATE", "getDEVICE_AUTHENTICATION_CERTIFICATE", "REQUEST_DEVICE_CHALLENGE_RESPONSE", "getREQUEST_DEVICE_CHALLENGE_RESPONSE", "DEVICE_AUTHENTICATION_RESPONSE", "getDEVICE_AUTHENTICATION_RESPONSE", "DEVICE_AUTHENTICATION_FAILED", "getDEVICE_AUTHENTICATION_FAILED", "DEVICE_AUTHENTICATION_SUCCEEDED", "getDEVICE_AUTHENTICATION_SUCCEEDED", "CARPLAY_AVAILABILITY", "getCARPLAY_AVAILABILITY", "CARPLAY_START_SESSION", "getCARPLAY_START_SESSION", "AVAILABLE_DIGITAL_CAR_KEYS", "getAVAILABLE_DIGITAL_CAR_KEYS", "MATCHED_DIGITAL_CAR_KEYS", "getMATCHED_DIGITAL_CAR_KEYS", "REQUEST_WIFI_INFORMATION", "getREQUEST_WIFI_INFORMATION", "WIFI_INFORMATION", "getWIFI_INFORMATION", "REQUEST_ACCESSORY_WIFI_CONFIGURATION", "getREQUEST_ACCESSORY_WIFI_CONFIGURATION", "ACCESSORY_WIFI_CONFIGURATION", "getACCESSORY_WIFI_CONFIGURATION", "GPRMC_DATA_STATUS_VALUES", "getGPRMC_DATA_STATUS_VALUES", "START_LOCATION_INFORMATION", "getSTART_LOCATION_INFORMATION", "LOCATION_INFORMATION", "getLOCATION_INFORMATION", "STOP_LOCATION_INFORMATION", "getSTOP_LOCATION_INFORMATION", "START_POWER_UPDATES", "getSTART_POWER_UPDATES", "POWER_UPDATE", "getPOWER_UPDATE", "STOP_POWER_UPDATES", "getSTOP_POWER_UPDATES", "POWER_SOURCE_UPDATE", "getPOWER_SOURCE_UPDATE", "ACCESSORY_POWER_UPDATE", "getACCESSORY_POWER_UPDATE", "ACCESSORY_RESET_BASE_CURRENT", "getACCESSORY_RESET_BASE_CURRENT", "START_ROUTE_GUIDANCE_UPDATES", "getSTART_ROUTE_GUIDANCE_UPDATES", "STOP_ROUTE_GUIDANCE_UPDATES", "getSTOP_ROUTE_GUIDANCE_UPDATES", "START_NOW_PLAYING_UPDATES", "getSTART_NOW_PLAYING_UPDATES", "STOP_NOW_PLAYING_UPDATES", "getSTOP_NOW_PLAYING_UPDATES", "START_COMMUNICATIONS_UPDATES", "getSTART_COMMUNICATIONS_UPDATES", "START_CALL_STATE_UPDATES", "getSTART_CALL_STATE_UPDATES", "STOP_COMMUNICATIONS_UPDATES", "getSTOP_COMMUNICATIONS_UPDATES", "STOP_CALL_STATE_UPDATES", "getSTOP_CALL_STATE_UPDATES", "DEVICE_INFORMATION_UPDATE", "getDEVICE_INFORMATION_UPDATE", "DEVICE_LANGUAGE_UPDATE", "getDEVICE_LANGUAGE_UPDATE", "DEVICE_TIME_UPDATE", "getDEVICE_TIME_UPDATE", "DEVICE_UUID_UPDATE", "getDEVICE_UUID_UPDATE", "WIRELESS_CARPLAY_UPDATE", "getWIRELESS_CARPLAY_UPDATE", "DEVICE_TRANSPORT_IDENTIFIER", "getDEVICE_TRANSPORT_IDENTIFIER", "EXTERNAL_ACCESSORY_OPEN_SESSION", "getEXTERNAL_ACCESSORY_OPEN_SESSION", "EXTERNAL_ACCESSORY_CLOSE_SESSION", "getEXTERNAL_ACCESSORY_CLOSE_SESSION", "EXTERNAL_ACCESSORY_STATUS_SESSION", "getEXTERNAL_ACCESSORY_STATUS_SESSION", "PING", "getPING", "USER_NOTIFICATION", "getUSER_NOTIFICATION", "SEND_PING_ACC", "getSEND_PING_ACC", "PING_ACC", "getPING_ACC", "DIGITAL_AUDIO_START", "getDIGITAL_AUDIO_START", "DIGITAL_AUDIO_INFORMATION_UPDATE", "getDIGITAL_AUDIO_INFORMATION_UPDATE", "DIGITAL_AUDIO_STOP", "getDIGITAL_AUDIO_STOP", "ALL", "", "getALL", "()Ljava/util/List;", "byId", "", "", "id", "require", "endpoint", "name", "", "feature", "direction", "Lcom/carplay/iap2/catalog/Iap2Direction;", "confidence", "Lcom/carplay/iap2/catalog/Iap2Confidence;", "fields", "Lcom/carplay/iap2/catalog/Iap2FieldSpec;", "registryEndpoint", "field", "type", "Lcom/carplay/iap2/catalog/Iap2WireType;", "required", "", "repeatable", "children", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2Endpoints {
    private static final Map<Integer, Iap2Endpoint> byId;
    public static final Iap2Endpoints INSTANCE = new Iap2Endpoints();
    private static final Iap2Endpoint START_IDENTIFICATION = endpoint$default(INSTANCE, Iap2IdentificationClient.START_IDENTIFICATION, "StartIdentification", "Identification", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint IDENTIFICATION_INFORMATION = endpoint$default(INSTANCE, Iap2IdentificationClient.IDENTIFICATION_INFORMATION, "IdentificationInformation", "Identification", Iap2Direction.ACCESSORY_TO_IPHONE, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "name", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 1, "modelIdentifier", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 2, "manufacturer", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 3, "serialNumber", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 4, "firmwareVersion", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 5, "hardwareVersion", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 6, "MessagesSentByAccessory", Iap2WireType.U16_LIST, true, false, null, 48, null), field$default(INSTANCE, 7, "MessagesReceivedFromDevice", Iap2WireType.U16_LIST, true, false, null, 48, null), field$default(INSTANCE, 8, "powerProvidingCapability", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 9, "maximumCurrentDrawnFromDevice", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 10, "externalAccessoryProtocols", Iap2WireType.GROUP, false, false, null, 56, null), field$default(INSTANCE, 12, "currentLanguage", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 13, "supportedLanguages", Iap2WireType.STRING_LIST, true, false, null, 48, null), field$default(INSTANCE, 16, "USBHostTransport", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "componentID", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 1, "name", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 2, "isSupported", Iap2WireType.VOID, false, false, null, 56, null), field$default(INSTANCE, 3, "interfaceNumber", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 4, "isAvailable", Iap2WireType.VOID, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 17, "BluetoothTransport", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "componentID", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 1, "name", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 2, "isSupported", Iap2WireType.VOID, false, false, null, 56, null), field$default(INSTANCE, 3, "bluetoothMAC", Iap2WireType.BYTES, false, false, null, 56, null), field$default(INSTANCE, 4, "transportName", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 5, "isAvailable", Iap2WireType.VOID, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 20, "VehicleInformation", Iap2WireType.GROUP, false, false, null, 56, null), field$default(INSTANCE, 21, "VehicleStatus", Iap2WireType.GROUP, false, false, null, 56, null), field$default(INSTANCE, 22, "LocationInformation", Iap2WireType.GROUP, false, false, null, 56, null), field$default(INSTANCE, 24, "WirelessCarPlayTransport", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "componentID", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 1, "SSID", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 2, "isSupported", Iap2WireType.VOID, false, false, null, 56, null), field$default(INSTANCE, 3, "transportIdentifier", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 4, "isAvailable", Iap2WireType.VOID, false, false, null, 56, null), field$default(INSTANCE, 5, "isEnabled", Iap2WireType.VOID, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 30, "RouteGuidanceDisplayComponent", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "Identifier", Iap2WireType.U16, true, false, null, 48, null), field$default(INSTANCE, 1, "Name", Iap2WireType.STRING, true, false, null, 48, null), field$default(INSTANCE, 2, "MaxCurrentRoadNameLength", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 4, "MaxAfterManeuverRoadNameLength", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 6, "MaxGuidanceManeuverStorageCapacity", Iap2WireType.U16, false, false, null, 56, null)}), 24, null)}), 16, null);
    private static final Iap2Endpoint IDENTIFICATION_ACCEPTED = endpoint$default(INSTANCE, Iap2IdentificationClient.IDENTIFICATION_ACCEPTED, "IdentificationAccepted", "Identification", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint IDENTIFICATION_REJECTED = endpoint$default(INSTANCE, Iap2IdentificationClient.IDENTIFICATION_REJECTED, "IdentificationRejected", "Identification", Iap2Direction.BIDIRECTIONAL, null, null, 48, null);
    private static final Iap2Endpoint CANCEL_IDENTIFICATION = endpoint$default(INSTANCE, 7429, "CancelIdentification", "Identification", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint IDENTIFICATION_INFORMATION_UPDATE = endpoint$default(INSTANCE, 7430, "IdentificationInformationUpdate", "Identification", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint REQUEST_AUTHENTICATION_CERTIFICATE = endpoint$default(INSTANCE, 43520, "RequestAuthenticationCertificate", "AccAuthentication", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint AUTHENTICATION_CERTIFICATE = endpoint$default(INSTANCE, 43521, "AuthenticationCertificate", "AccAuthentication", Iap2Direction.ACCESSORY_TO_IPHONE, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "certificate", Iap2WireType.BYTES, true, false, null, 48, null)), 16, null);
    private static final Iap2Endpoint REQUEST_AUTHENTICATION_CHALLENGE_RESPONSE = endpoint$default(INSTANCE, 43522, "RequestAuthenticationChallengeResponse", "AccAuthentication", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "challenge", Iap2WireType.BYTES, true, false, null, 48, null)), 16, null);
    private static final Iap2Endpoint AUTHENTICATION_RESPONSE = endpoint$default(INSTANCE, 43523, "AuthenticationResponse", "AccAuthentication", Iap2Direction.ACCESSORY_TO_IPHONE, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "signatureResponse", Iap2WireType.BYTES, true, false, null, 48, null)), 16, null);
    private static final Iap2Endpoint AUTHENTICATION_FAILED = endpoint$default(INSTANCE, 43524, "AuthenticationFailed", "AccAuthentication", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint AUTHENTICATION_SUCCEEDED = endpoint$default(INSTANCE, 43525, "AuthenticationSucceeded", "AccAuthentication", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint AUTHENTICATION_CERTIFICATE_SERIAL = endpoint$default(INSTANCE, 43526, "AuthenticationCertificateSerial", "AccAuthentication", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint REQUEST_DEVICE_CERTIFICATE = endpoint$default(INSTANCE, 43536, "RequestDeviceCertificate", "DeviceAuthentication", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint DEVICE_AUTHENTICATION_CERTIFICATE = endpoint$default(INSTANCE, 43537, "AuthenticationCertificate", "DeviceAuthentication", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint REQUEST_DEVICE_CHALLENGE_RESPONSE = endpoint$default(INSTANCE, 43538, "RequestDeviceChallengeResponse", "DeviceAuthentication", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint DEVICE_AUTHENTICATION_RESPONSE = endpoint$default(INSTANCE, 43539, "AuthenticationResponse", "DeviceAuthentication", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint DEVICE_AUTHENTICATION_FAILED = endpoint$default(INSTANCE, 43540, "DeviceAuthenticationFailed", "DeviceAuthentication", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint DEVICE_AUTHENTICATION_SUCCEEDED = endpoint$default(INSTANCE, 43541, "DeviceAuthenticationSucceeded", "DeviceAuthentication", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint CARPLAY_AVAILABILITY = endpoint$default(INSTANCE, 17152, "CarPlayAvailability", "CarPlayConnectionRequest", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "wired", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "wiredAvailable", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 1, "usbIdentifier", Iap2WireType.STRING, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 1, "wireless", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "wirelessAvailable", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 1, "bluetoothIdentifier", Iap2WireType.STRING, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 2, "themeAssets", Iap2WireType.GROUP, false, false, CollectionsKt.listOf(field$default(INSTANCE, 0, "themeAssetsAvailable", Iap2WireType.U8, false, false, null, 56, null)), 24, null)}), 16, null);
    private static final Iap2Endpoint CARPLAY_START_SESSION = endpoint$default(INSTANCE, 17153, "CarPlayStartSession", "CarPlayConnectionRequest", Iap2Direction.ACCESSORY_TO_IPHONE, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "wired", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "wiredIP", Iap2WireType.STRING, false, true, null, 40, null), field$default(INSTANCE, 1, "reserved", Iap2WireType.U32, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 1, "wireless", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "SSID", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 1, "passphrase", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 2, "channel", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 3, "wirelessIP", Iap2WireType.STRING, false, true, null, 40, null), field$default(INSTANCE, 4, "securityType", Iap2WireType.U8, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 2, "airPlayPort", Iap2WireType.U32, false, false, null, 56, null), field$default(INSTANCE, 3, "deviceIdentifier", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 4, "publicKey", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 5, "sourceVersion", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 6, "SDKVersion", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 7, "clusterAsset", Iap2WireType.GROUP, false, false, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "assetID", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 1, "assetVersion", Iap2WireType.U32, false, false, null, 56, null)}), 24, null), field$default(INSTANCE, 8, "mutualAuth", Iap2WireType.U8, false, false, null, 56, null)}), 16, null);
    private static final Iap2Endpoint AVAILABLE_DIGITAL_CAR_KEYS = endpoint$default(INSTANCE, 17154, "AvailableDigitalCarKeys", "DigitalCarKey_Matching", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint MATCHED_DIGITAL_CAR_KEYS = endpoint$default(INSTANCE, 17155, "MatchedDigitalCarKeys", "DigitalCarKey_Matching", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint REQUEST_WIFI_INFORMATION = endpoint$default(INSTANCE, 22272, "RequestWiFiInformation", "WiFiSharing", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint WIFI_INFORMATION = endpoint$default(INSTANCE, 22273, "WiFiInformation", "WiFiSharing", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, NotificationCompat.CATEGORY_STATUS, Iap2WireType.U8, true, false, null, 48, null), field$default(INSTANCE, 1, "securityType", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 2, "SSID", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 3, "passphrase", Iap2WireType.STRING, false, false, null, 56, null)}), 16, null);
    private static final Iap2Endpoint REQUEST_ACCESSORY_WIFI_CONFIGURATION = endpoint$default(INSTANCE, 22274, "RequestAccessoryWiFiConfigurationInformation", "WiFiSharing", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint ACCESSORY_WIFI_CONFIGURATION = endpoint$default(INSTANCE, 22275, "AccessoryWiFiConfigurationInformation", "WiFiSharing", Iap2Direction.ACCESSORY_TO_IPHONE, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "BSSID", Iap2WireType.BYTES, false, false, null, 56, null), field$default(INSTANCE, 1, "SSID", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 2, "passphrase", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 3, "securityType", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 4, "channel", Iap2WireType.U8, false, false, null, 56, null)}), 16, null);
    private static final Iap2Endpoint GPRMC_DATA_STATUS_VALUES = endpoint$default(INSTANCE, 65520, "GPRMCDataStatusValues", "Location", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint START_LOCATION_INFORMATION = endpoint$default(INSTANCE, Iap2LocationMessages.START_LOCATION_INFORMATION, "StartLocationInformation", "Location", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint LOCATION_INFORMATION = endpoint$default(INSTANCE, Iap2LocationMessages.LOCATION_INFORMATION, "LocationInformation", "Location", Iap2Direction.ACCESSORY_TO_IPHONE, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "NMEA", Iap2WireType.STRING, false, false, null, 56, null)), 16, null);
    private static final Iap2Endpoint STOP_LOCATION_INFORMATION = endpoint$default(INSTANCE, Iap2LocationMessages.STOP_LOCATION_INFORMATION, "StopLocationInformation", "Location", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint START_POWER_UPDATES = endpoint$default(INSTANCE, 44544, "StartPowerUpdates", "Power", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint POWER_UPDATE = endpoint$default(INSTANCE, 44545, "PowerUpdate", "Power", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint STOP_POWER_UPDATES = endpoint$default(INSTANCE, 44546, "StopPowerUpdates", "Power", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint POWER_SOURCE_UPDATE = endpoint$default(INSTANCE, 44547, "PowerSourceUpdate", "Power", Iap2Direction.ACCESSORY_TO_IPHONE, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "AvailableCurrentForDevice", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 1, "IsDeviceBatteryCharging", Iap2WireType.U8, false, false, null, 56, null), field$default(INSTANCE, 3, "AvailableSiphoningCurrent", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 4, "ReserveCurrent", Iap2WireType.U16, false, false, null, 56, null), field$default(INSTANCE, 5, "MaxNonSiphoningCurrentExceeded", Iap2WireType.VOID, false, false, null, 56, null)}), 16, null);
    private static final Iap2Endpoint ACCESSORY_POWER_UPDATE = endpoint$default(INSTANCE, 44549, "AccessoryPowerUpdate", "Power", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint ACCESSORY_RESET_BASE_CURRENT = endpoint$default(INSTANCE, 44550, "AccessoryResetBaseCurrent", "Power", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint START_ROUTE_GUIDANCE_UPDATES = endpoint$default(INSTANCE, 20992, "StartRouteGuidanceUpdates", "Navigation", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint STOP_ROUTE_GUIDANCE_UPDATES = endpoint$default(INSTANCE, 20995, "StopRouteGuidanceUpdates", "Navigation", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint START_NOW_PLAYING_UPDATES = endpoint$default(INSTANCE, 20480, "StartNowPlayingUpdates", "Now Playing", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint STOP_NOW_PLAYING_UPDATES = endpoint$default(INSTANCE, 20482, "StopNowPlayingUpdates", "Now Playing", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint START_COMMUNICATIONS_UPDATES = endpoint$default(INSTANCE, 16727, "startCommunicationsUpdatesHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint START_CALL_STATE_UPDATES = endpoint$default(INSTANCE, 16724, "startCallStateUpdatesHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint STOP_COMMUNICATIONS_UPDATES = endpoint$default(INSTANCE, 16729, "stopCommunicationsUpdatesHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint STOP_CALL_STATE_UPDATES = endpoint$default(INSTANCE, 16726, "stopCallStateUpdatesHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint DEVICE_INFORMATION_UPDATE = endpoint$default(INSTANCE, 19977, "DeviceInformationUpdate", "DeviceNotifications", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "deviceName", Iap2WireType.STRING, false, false, null, 56, null)), 16, null);
    private static final Iap2Endpoint DEVICE_LANGUAGE_UPDATE = endpoint$default(INSTANCE, 19978, "DeviceLanguageUpdate", "DeviceNotifications", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "language", Iap2WireType.STRING, false, false, null, 56, null)), 16, null);
    private static final Iap2Endpoint DEVICE_TIME_UPDATE = endpoint$default(INSTANCE, 19979, "DeviceTimeUpdate", "DeviceNotifications", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "secondsSinceReferenceDate", Iap2WireType.U64, false, false, null, 56, null), field$default(INSTANCE, 1, "timeZoneOffsetMinutes", Iap2WireType.I16, false, false, null, 56, null), field$default(INSTANCE, 2, "daylightSavingsOffsetMinutes", Iap2WireType.I8, false, false, null, 56, null)}), 16, null);
    private static final Iap2Endpoint DEVICE_UUID_UPDATE = endpoint$default(INSTANCE, 19980, "DeviceUuidUpdate", "DeviceNotifications", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "deviceUUID", Iap2WireType.STRING, false, false, null, 56, null)), 16, null);
    private static final Iap2Endpoint WIRELESS_CARPLAY_UPDATE = endpoint$default(INSTANCE, 19981, "WirelessCarPlayUpdate", "DeviceNotifications", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf(field$default(INSTANCE, 0, "availability", Iap2WireType.U8, true, false, null, 48, null)), 16, null);
    private static final Iap2Endpoint DEVICE_TRANSPORT_IDENTIFIER = endpoint$default(INSTANCE, 19982, "DeviceTransportIdentifierNotification", "DeviceNotifications", Iap2Direction.IPHONE_TO_ACCESSORY, null, CollectionsKt.listOf((Object[]) new Iap2FieldSpec[]{field$default(INSTANCE, 0, "bluetoothMAC", Iap2WireType.STRING, false, false, null, 56, null), field$default(INSTANCE, 1, "usbTransportIdentifier", Iap2WireType.STRING, false, false, null, 56, null)}), 16, null);
    private static final Iap2Endpoint EXTERNAL_ACCESSORY_OPEN_SESSION = endpoint$default(INSTANCE, 59904, "externalAccessory_openEASession", "ExternalAccessory", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint EXTERNAL_ACCESSORY_CLOSE_SESSION = endpoint$default(INSTANCE, 59905, "externalAccessory_closeEASession", "ExternalAccessory", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint EXTERNAL_ACCESSORY_STATUS_SESSION = endpoint$default(INSTANCE, 59907, "externalaccessory_statusEASessionHandler", "ExternalAccessory", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint PING = endpoint$default(INSTANCE, 0, "pingHandler", "Test", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint USER_NOTIFICATION = endpoint$default(INSTANCE, 1, "userNotificationHandler", "Test", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint SEND_PING_ACC = endpoint$default(INSTANCE, 4, "sendPingAccHandler", "Test", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint PING_ACC = endpoint$default(INSTANCE, 5, "pingAccHandler", "Test", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint DIGITAL_AUDIO_START = endpoint$default(INSTANCE, 55808, "digitalAudio_start", "DigitalAudio", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final Iap2Endpoint DIGITAL_AUDIO_INFORMATION_UPDATE = endpoint$default(INSTANCE, 55809, "DigitalAudioInformationUpdate", "DigitalAudio", Iap2Direction.IPHONE_TO_ACCESSORY, null, null, 48, null);
    private static final Iap2Endpoint DIGITAL_AUDIO_STOP = endpoint$default(INSTANCE, 55810, "digitalAudio_stop", "DigitalAudio", Iap2Direction.ACCESSORY_TO_IPHONE, null, null, 48, null);
    private static final List<Iap2Endpoint> ALL = CollectionsKt.listOf((Object[]) new Iap2Endpoint[]{START_IDENTIFICATION, IDENTIFICATION_INFORMATION, IDENTIFICATION_ACCEPTED, IDENTIFICATION_REJECTED, CANCEL_IDENTIFICATION, IDENTIFICATION_INFORMATION_UPDATE, REQUEST_AUTHENTICATION_CERTIFICATE, AUTHENTICATION_CERTIFICATE, REQUEST_AUTHENTICATION_CHALLENGE_RESPONSE, AUTHENTICATION_RESPONSE, AUTHENTICATION_FAILED, AUTHENTICATION_SUCCEEDED, AUTHENTICATION_CERTIFICATE_SERIAL, REQUEST_DEVICE_CERTIFICATE, DEVICE_AUTHENTICATION_CERTIFICATE, REQUEST_DEVICE_CHALLENGE_RESPONSE, DEVICE_AUTHENTICATION_RESPONSE, DEVICE_AUTHENTICATION_FAILED, DEVICE_AUTHENTICATION_SUCCEEDED, CARPLAY_AVAILABILITY, CARPLAY_START_SESSION, AVAILABLE_DIGITAL_CAR_KEYS, MATCHED_DIGITAL_CAR_KEYS, REQUEST_WIFI_INFORMATION, WIFI_INFORMATION, REQUEST_ACCESSORY_WIFI_CONFIGURATION, ACCESSORY_WIFI_CONFIGURATION, GPRMC_DATA_STATUS_VALUES, START_LOCATION_INFORMATION, LOCATION_INFORMATION, STOP_LOCATION_INFORMATION, START_POWER_UPDATES, POWER_UPDATE, STOP_POWER_UPDATES, POWER_SOURCE_UPDATE, ACCESSORY_POWER_UPDATE, ACCESSORY_RESET_BASE_CURRENT, START_ROUTE_GUIDANCE_UPDATES, STOP_ROUTE_GUIDANCE_UPDATES, START_NOW_PLAYING_UPDATES, STOP_NOW_PLAYING_UPDATES, START_COMMUNICATIONS_UPDATES, START_CALL_STATE_UPDATES, STOP_COMMUNICATIONS_UPDATES, STOP_CALL_STATE_UPDATES, DEVICE_INFORMATION_UPDATE, DEVICE_LANGUAGE_UPDATE, DEVICE_TIME_UPDATE, DEVICE_UUID_UPDATE, WIRELESS_CARPLAY_UPDATE, DEVICE_TRANSPORT_IDENTIFIER, EXTERNAL_ACCESSORY_OPEN_SESSION, EXTERNAL_ACCESSORY_CLOSE_SESSION, EXTERNAL_ACCESSORY_STATUS_SESSION, PING, USER_NOTIFICATION, SEND_PING_ACC, PING_ACC, DIGITAL_AUDIO_START, DIGITAL_AUDIO_INFORMATION_UPDATE, DIGITAL_AUDIO_STOP, INSTANCE.registryEndpoint(Iap2VehicleStatus.START_VEHICLE_STATUS_UPDATES, "StartVehicleStatusUpdates", "Vehicle", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(Iap2VehicleStatus.VEHICLE_STATUS_UPDATE, "VehicleStatusUpdate", "Vehicle", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(Iap2VehicleStatus.STOP_VEHICLE_STATUS_UPDATES, "StopVehicleStatusUpdates", "Vehicle", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(20481, "NowPlayingUpdate", "Now Playing", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(20483, "SetNowPlayingInfo", "Now Playing", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19456, "StartMediaLibraryInformation", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19457, "MediaLibraryInformation", "Media Library", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(19458, "StopMediaLibraryInformation", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19459, "StartMediaLibraryUpdates", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19460, "MediaLibraryUpdate", "Media Library", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(19461, "StopMediaLibraryUpdates", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19462, "PlayMediaLibraryCurrentSelection", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19463, "PlayMediaLibraryItems", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19464, "PlayMediaLibraryCollection", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19465, "PlayMediaLibrarySpecial", "Media Library", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(20993, "RouteGuidanceUpdate", "Navigation", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(20994, "RouteGuidanceManeuverUpdate", "Navigation", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(20996, "LaneGuidanceInfoUpdate", "Navigation", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(16725, "CallStateUpdate", "Communications", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(16728, "CommunicationsUpdate", "Communications", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(16730, "initiateCallHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16731, "acceptCallHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16732, "endCallHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16733, "swapCallHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16734, "mergeCallHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16735, "holdStatusUpdateHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16736, "muteStatusUpdateHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16737, "sendDTMFHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16752, "startListUpdatesHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(16753, "ListUpdate", "Communications", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(16754, "stopListUpdatesHandler", "Communications", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19969, "BluetoothComponentInfo", "Bluetooth connection status", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19971, "StartBluetoothConnectionUpdates", "Bluetooth connection status", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(19972, "BluetoothConnectionUpdate", "Bluetooth connection status", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(19973, "StopBluetoothConnectionUpdates", "Bluetooth connection status", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(44288, "appLinks_start", "App links and launch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(44289, "AppLinksUpdate", "App links and launch", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(44290, "appLinks_stop", "App links and launch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(44291, "appLinks_requestAppIcons", "App links and launch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(44292, "AppIconResponse", "App links and launch", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(59906, "requestAppLaunchHandler", "External Accessory", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(26624, "hid_StartHIDMsgHandler", "HID and USB host", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(26625, "hid_OutReport", "HID and USB host", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(26626, "hid_AccessoryHIDReportHandler", "HID and USB host", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(26627, "hid_StopHIDMsgHandler", "HID and USB host", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(26628, "hid_GetReport", "HID and USB host", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(26629, "hid_AccessoryHIDGetReportResponseHandler", "HID and USB host", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(26631, "hid_ComponentUpdate", "HID and USB host", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(32256, "usbHostMode_StartUSBHostModeHandler", "HID and USB host", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(32258, "usbHostMode_StopUSBHostModeHandler", "HID and USB host", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(21504, "assistiveTouch_startAssistiveTouchHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(21505, "assistiveTouch_stopAssistiveTouchHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(21506, "assistiveTouch_startAssistiveTouchUpdateHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(21507, "NotifyStatusUpdate", "VoiceOver and AssistiveTouch", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(21508, "assistiveTouch_stopAssistiveTouchUpdateHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22017, "voiceOver_moveCursorHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22018, "voiceOver_selectCursorHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22019, "voiceOver_scrollPageHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22022, "voiceOver_speakTextHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22024, "voiceOver_pauseSpeakingHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22025, "voiceOver_resumeSpeakingHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22027, "voiceOver_startInformationHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22028, "VoiceOverInformation", "VoiceOver and AssistiveTouch", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(22029, "voiceOver_stopInformationHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22030, "voiceOver_updateInformationHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22031, "voiceOver_startCursorInformationHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22032, "VoiceOverCursorInformation", "VoiceOver and AssistiveTouch", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(22033, "voiceOver_stopCursorInformationHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22034, "VoiceOver_startVoiceOverHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(22035, "VoiceOver_stopVoiceOverHandler", "VoiceOver and AssistiveTouch", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(2816, "oobBtPairing2_startPairing", "BLE and OOB pairing", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(2817, "oobBtPairing2_accessoryInfoHandler", "BLE and OOB pairing", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(2818, "oobBtPairing2_statusHandler", "BLE and OOB pairing", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(2819, "oobBtPairing2_stopPairing", "BLE and OOB pairing", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(4314, "destinationSharing_StartDestinationInformation", "Destination sharing", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(4315, "DestinationInformationUpdate", "Destination sharing", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(4316, "destinationSharing_StopDestinationInformation", "Destination sharing", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(4317, "destinationSharing_DestinationInformationStatus", "Destination sharing", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(176, "oobBtPairing_startPairing", "Other endpoints", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(177, "oobBtPairing_accessoryInfoHandler", "Other endpoints", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(178, "oobBtPairing_pairingLinkKeyInfo", "Other endpoints", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(179, "oobBtPairing_completionInfoHandler", "Other endpoints", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(180, "oobBtPairing_stopPairing", "Other endpoints", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(3328, "StartRoadObjectDetection", "Other endpoints", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(KyberEngine.KyberQ, "RoadObjectDetectionUpdate", "Other endpoints", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(3330, "StopRoadObjectDetection", "Other endpoints", Iap2Direction.IPHONE_TO_ACCESSORY), INSTANCE.registryEndpoint(45314, "blePairing_accessoryStateUpdateHandler", "Other endpoints", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(45315, "blePairing_accessoryPairingInfoHandler", "Other endpoints", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(45317, "blePairing_accessoryPairingDataHandler", "Other endpoints", Iap2Direction.ACCESSORY_TO_IPHONE), INSTANCE.registryEndpoint(45319, "blePairing_stopBLEUpdates", "Other endpoints", Iap2Direction.IPHONE_TO_ACCESSORY)});

    private Iap2Endpoints() {
    }

    static {
        Iterable $this$associateBy$iv = ALL;
        int capacity$iv = RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault($this$associateBy$iv, 10)), 16);
        Map destination$iv$iv = new LinkedHashMap(capacity$iv);
        for (Object element$iv$iv : $this$associateBy$iv) {
            Iap2Endpoint p0 = (Iap2Endpoint) element$iv$iv;
            destination$iv$iv.put(Integer.valueOf(p0.getId()), element$iv$iv);
        }
        byId = destination$iv$iv;
    }

    public final Iap2Endpoint getSTART_IDENTIFICATION() {
        return START_IDENTIFICATION;
    }

    public final Iap2Endpoint getIDENTIFICATION_INFORMATION() {
        return IDENTIFICATION_INFORMATION;
    }

    public final Iap2Endpoint getIDENTIFICATION_ACCEPTED() {
        return IDENTIFICATION_ACCEPTED;
    }

    public final Iap2Endpoint getIDENTIFICATION_REJECTED() {
        return IDENTIFICATION_REJECTED;
    }

    public final Iap2Endpoint getCANCEL_IDENTIFICATION() {
        return CANCEL_IDENTIFICATION;
    }

    public final Iap2Endpoint getIDENTIFICATION_INFORMATION_UPDATE() {
        return IDENTIFICATION_INFORMATION_UPDATE;
    }

    public final Iap2Endpoint getREQUEST_AUTHENTICATION_CERTIFICATE() {
        return REQUEST_AUTHENTICATION_CERTIFICATE;
    }

    public final Iap2Endpoint getAUTHENTICATION_CERTIFICATE() {
        return AUTHENTICATION_CERTIFICATE;
    }

    public final Iap2Endpoint getREQUEST_AUTHENTICATION_CHALLENGE_RESPONSE() {
        return REQUEST_AUTHENTICATION_CHALLENGE_RESPONSE;
    }

    public final Iap2Endpoint getAUTHENTICATION_RESPONSE() {
        return AUTHENTICATION_RESPONSE;
    }

    public final Iap2Endpoint getAUTHENTICATION_FAILED() {
        return AUTHENTICATION_FAILED;
    }

    public final Iap2Endpoint getAUTHENTICATION_SUCCEEDED() {
        return AUTHENTICATION_SUCCEEDED;
    }

    public final Iap2Endpoint getAUTHENTICATION_CERTIFICATE_SERIAL() {
        return AUTHENTICATION_CERTIFICATE_SERIAL;
    }

    public final Iap2Endpoint getREQUEST_DEVICE_CERTIFICATE() {
        return REQUEST_DEVICE_CERTIFICATE;
    }

    public final Iap2Endpoint getDEVICE_AUTHENTICATION_CERTIFICATE() {
        return DEVICE_AUTHENTICATION_CERTIFICATE;
    }

    public final Iap2Endpoint getREQUEST_DEVICE_CHALLENGE_RESPONSE() {
        return REQUEST_DEVICE_CHALLENGE_RESPONSE;
    }

    public final Iap2Endpoint getDEVICE_AUTHENTICATION_RESPONSE() {
        return DEVICE_AUTHENTICATION_RESPONSE;
    }

    public final Iap2Endpoint getDEVICE_AUTHENTICATION_FAILED() {
        return DEVICE_AUTHENTICATION_FAILED;
    }

    public final Iap2Endpoint getDEVICE_AUTHENTICATION_SUCCEEDED() {
        return DEVICE_AUTHENTICATION_SUCCEEDED;
    }

    public final Iap2Endpoint getCARPLAY_AVAILABILITY() {
        return CARPLAY_AVAILABILITY;
    }

    public final Iap2Endpoint getCARPLAY_START_SESSION() {
        return CARPLAY_START_SESSION;
    }

    public final Iap2Endpoint getAVAILABLE_DIGITAL_CAR_KEYS() {
        return AVAILABLE_DIGITAL_CAR_KEYS;
    }

    public final Iap2Endpoint getMATCHED_DIGITAL_CAR_KEYS() {
        return MATCHED_DIGITAL_CAR_KEYS;
    }

    public final Iap2Endpoint getREQUEST_WIFI_INFORMATION() {
        return REQUEST_WIFI_INFORMATION;
    }

    public final Iap2Endpoint getWIFI_INFORMATION() {
        return WIFI_INFORMATION;
    }

    public final Iap2Endpoint getREQUEST_ACCESSORY_WIFI_CONFIGURATION() {
        return REQUEST_ACCESSORY_WIFI_CONFIGURATION;
    }

    public final Iap2Endpoint getACCESSORY_WIFI_CONFIGURATION() {
        return ACCESSORY_WIFI_CONFIGURATION;
    }

    public final Iap2Endpoint getGPRMC_DATA_STATUS_VALUES() {
        return GPRMC_DATA_STATUS_VALUES;
    }

    public final Iap2Endpoint getSTART_LOCATION_INFORMATION() {
        return START_LOCATION_INFORMATION;
    }

    public final Iap2Endpoint getLOCATION_INFORMATION() {
        return LOCATION_INFORMATION;
    }

    public final Iap2Endpoint getSTOP_LOCATION_INFORMATION() {
        return STOP_LOCATION_INFORMATION;
    }

    public final Iap2Endpoint getSTART_POWER_UPDATES() {
        return START_POWER_UPDATES;
    }

    public final Iap2Endpoint getPOWER_UPDATE() {
        return POWER_UPDATE;
    }

    public final Iap2Endpoint getSTOP_POWER_UPDATES() {
        return STOP_POWER_UPDATES;
    }

    public final Iap2Endpoint getPOWER_SOURCE_UPDATE() {
        return POWER_SOURCE_UPDATE;
    }

    public final Iap2Endpoint getACCESSORY_POWER_UPDATE() {
        return ACCESSORY_POWER_UPDATE;
    }

    public final Iap2Endpoint getACCESSORY_RESET_BASE_CURRENT() {
        return ACCESSORY_RESET_BASE_CURRENT;
    }

    public final Iap2Endpoint getSTART_ROUTE_GUIDANCE_UPDATES() {
        return START_ROUTE_GUIDANCE_UPDATES;
    }

    public final Iap2Endpoint getSTOP_ROUTE_GUIDANCE_UPDATES() {
        return STOP_ROUTE_GUIDANCE_UPDATES;
    }

    public final Iap2Endpoint getSTART_NOW_PLAYING_UPDATES() {
        return START_NOW_PLAYING_UPDATES;
    }

    public final Iap2Endpoint getSTOP_NOW_PLAYING_UPDATES() {
        return STOP_NOW_PLAYING_UPDATES;
    }

    public final Iap2Endpoint getSTART_COMMUNICATIONS_UPDATES() {
        return START_COMMUNICATIONS_UPDATES;
    }

    public final Iap2Endpoint getSTART_CALL_STATE_UPDATES() {
        return START_CALL_STATE_UPDATES;
    }

    public final Iap2Endpoint getSTOP_COMMUNICATIONS_UPDATES() {
        return STOP_COMMUNICATIONS_UPDATES;
    }

    public final Iap2Endpoint getSTOP_CALL_STATE_UPDATES() {
        return STOP_CALL_STATE_UPDATES;
    }

    public final Iap2Endpoint getDEVICE_INFORMATION_UPDATE() {
        return DEVICE_INFORMATION_UPDATE;
    }

    public final Iap2Endpoint getDEVICE_LANGUAGE_UPDATE() {
        return DEVICE_LANGUAGE_UPDATE;
    }

    public final Iap2Endpoint getDEVICE_TIME_UPDATE() {
        return DEVICE_TIME_UPDATE;
    }

    public final Iap2Endpoint getDEVICE_UUID_UPDATE() {
        return DEVICE_UUID_UPDATE;
    }

    public final Iap2Endpoint getWIRELESS_CARPLAY_UPDATE() {
        return WIRELESS_CARPLAY_UPDATE;
    }

    public final Iap2Endpoint getDEVICE_TRANSPORT_IDENTIFIER() {
        return DEVICE_TRANSPORT_IDENTIFIER;
    }

    public final Iap2Endpoint getEXTERNAL_ACCESSORY_OPEN_SESSION() {
        return EXTERNAL_ACCESSORY_OPEN_SESSION;
    }

    public final Iap2Endpoint getEXTERNAL_ACCESSORY_CLOSE_SESSION() {
        return EXTERNAL_ACCESSORY_CLOSE_SESSION;
    }

    public final Iap2Endpoint getEXTERNAL_ACCESSORY_STATUS_SESSION() {
        return EXTERNAL_ACCESSORY_STATUS_SESSION;
    }

    public final Iap2Endpoint getPING() {
        return PING;
    }

    public final Iap2Endpoint getUSER_NOTIFICATION() {
        return USER_NOTIFICATION;
    }

    public final Iap2Endpoint getSEND_PING_ACC() {
        return SEND_PING_ACC;
    }

    public final Iap2Endpoint getPING_ACC() {
        return PING_ACC;
    }

    public final Iap2Endpoint getDIGITAL_AUDIO_START() {
        return DIGITAL_AUDIO_START;
    }

    public final Iap2Endpoint getDIGITAL_AUDIO_INFORMATION_UPDATE() {
        return DIGITAL_AUDIO_INFORMATION_UPDATE;
    }

    public final Iap2Endpoint getDIGITAL_AUDIO_STOP() {
        return DIGITAL_AUDIO_STOP;
    }

    public final List<Iap2Endpoint> getALL() {
        return ALL;
    }

    public final Iap2Endpoint byId(int id) {
        return byId.get(Integer.valueOf(id));
    }

    public final Iap2Endpoint require(int id) {
        Iap2Endpoint iap2EndpointById = byId(id);
        if (iap2EndpointById != null) {
            return iap2EndpointById;
        }
        String string = Integer.toString(id, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        throw new IllegalArgumentException("Unknown iAP2 endpoint 0x" + StringsKt.padStart(string, 4, '0'));
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ Iap2Endpoint endpoint$default(Iap2Endpoints iap2Endpoints, int i, String str, String str2, Iap2Direction iap2Direction, Iap2Confidence iap2Confidence, List list, int i2, Object obj) {
        Iap2Confidence iap2Confidence2;
        List listEmptyList;
        if ((i2 & 16) == 0) {
            iap2Confidence2 = iap2Confidence;
        } else {
            iap2Confidence2 = Iap2Confidence.HIGH;
        }
        if ((i2 & 32) == 0) {
            listEmptyList = list;
        } else {
            listEmptyList = CollectionsKt.emptyList();
        }
        return iap2Endpoints.endpoint(i, str, str2, iap2Direction, iap2Confidence2, listEmptyList);
    }

    private final Iap2Endpoint endpoint(int id, String name, String feature, Iap2Direction direction, Iap2Confidence confidence, List<Iap2FieldSpec> fields) {
        return new Iap2Endpoint(id, name, feature, direction, confidence, fields);
    }

    private final Iap2Endpoint registryEndpoint(int id, String name, String feature, Iap2Direction direction) {
        return new Iap2Endpoint(id, name, feature, direction, null, CollectionsKt.emptyList(), 16, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ Iap2FieldSpec field$default(Iap2Endpoints iap2Endpoints, int i, String str, Iap2WireType iap2WireType, boolean z, boolean z2, List list, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            z = false;
        }
        if ((i2 & 16) != 0) {
            z2 = false;
        }
        if ((i2 & 32) != 0) {
            list = CollectionsKt.emptyList();
        }
        return iap2Endpoints.field(i, str, iap2WireType, z, z2, list);
    }

    private final Iap2FieldSpec field(int id, String name, Iap2WireType type, boolean required, boolean repeatable, List<Iap2FieldSpec> children) {
        return new Iap2FieldSpec(id, name, type, required, repeatable, children);
    }
}
