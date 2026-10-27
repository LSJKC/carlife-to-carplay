package com.carlink.cast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjectionConfig;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import com.carlink.cast.ui.MetricsView;
import com.carlink.cast.ui.ProtocolContent;
import com.carlink.cast.ui.ProtocolSection;
import com.carlink.cast.ui.StageRingView;
import com.carlink.cast.ui.Ui;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.flow.FlowKt;
import org.bouncycastle.i18n.MessageBundle;
import org.bouncycastle.i18n.TextBundle;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: MainActivity.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000ä\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u0000 ¡\u00012\u00020\u0001:\u0004 \u0001¡\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u000102H\u0014J\b\u00103\u001a\u00020\u0013H\u0002J\b\u00104\u001a\u00020\u0013H\u0002J\b\u00105\u001a\u00020\u0013H\u0002J\b\u00106\u001a\u00020\u0013H\u0002J\b\u00107\u001a\u00020\u0013H\u0002J\b\u00108\u001a\u00020\u0013H\u0002J$\u00109\u001a\u00020\u00152\u0006\u0010:\u001a\u00020!2\b\u0010;\u001a\u0004\u0018\u00010!2\b\b\u0002\u0010<\u001a\u00020%H\u0002J\b\u0010=\u001a\u00020\u0013H\u0002J\u0010\u0010>\u001a\u00020\u000b2\u0006\u0010?\u001a\u00020!H\u0002J\b\u0010@\u001a\u000200H\u0002J\b\u0010A\u001a\u000200H\u0002J\u0010\u0010B\u001a\u0002002\u0006\u0010C\u001a\u00020DH\u0002J\u0010\u0010E\u001a\u0002002\u0006\u0010C\u001a\u00020DH\u0002J\u0010\u0010F\u001a\u00020!2\u0006\u0010C\u001a\u00020DH\u0002J\u0010\u0010G\u001a\u00020!2\u0006\u0010H\u001a\u00020IH\u0002J\u0010\u0010J\u001a\u00020!2\u0006\u0010C\u001a\u00020DH\u0002J\u0010\u0010K\u001a\u00020!2\u0006\u0010L\u001a\u00020.H\u0002J\b\u0010M\u001a\u000200H\u0003J\u001a\u0010N\u001a\u0002002\b\u0010O\u001a\u0004\u0018\u00010P2\u0006\u0010Q\u001a\u00020#H\u0002J\b\u0010R\u001a\u00020%H\u0002J\"\u0010S\u001a\u0002002\u0006\u0010T\u001a\u00020#2\u0006\u0010U\u001a\u00020#2\b\u0010V\u001a\u0004\u0018\u00010PH\u0014J\b\u0010W\u001a\u00020%H\u0002J-\u0010X\u001a\u0002002\u0006\u0010T\u001a\u00020#2\u000e\u0010Y\u001a\n\u0012\u0006\b\u0001\u0012\u00020!0Z2\u0006\u0010[\u001a\u00020\\H\u0016¢\u0006\u0002\u0010]J\b\u0010^\u001a\u000200H\u0003J\b\u0010_\u001a\u000200H\u0002J\u0016\u0010`\u001a\u0010\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020!\u0018\u00010 H\u0002J\b\u0010a\u001a\u000200H\u0002J\b\u0010b\u001a\u000200H\u0002J\b\u0010c\u001a\u00020\u0013H\u0002J\b\u0010d\u001a\u000200H\u0002J\b\u0010e\u001a\u000200H\u0002J\u0014\u0010f\u001a\u000e\u0012\u0004\u0012\u00020g\u0012\u0004\u0012\u00020h0 H\u0002J\b\u0010i\u001a\u00020\u0013H\u0002J\u0010\u0010j\u001a\u00020\u000b2\u0006\u0010?\u001a\u00020!H\u0002J\b\u0010k\u001a\u000200H\u0002J\b\u0010l\u001a\u000200H\u0002J\b\u0010m\u001a\u00020!H\u0002J\u0014\u0010n\u001a\u0002002\n\b\u0002\u0010o\u001a\u0004\u0018\u00010pH\u0002J\u0010\u0010q\u001a\u0002002\u0006\u0010r\u001a\u00020pH\u0002J\b\u0010s\u001a\u000200H\u0002J0\u0010t\u001a\u00020\u00132\u0006\u0010u\u001a\u00020!2\u0006\u0010v\u001a\u00020!2\b\b\u0002\u0010w\u001a\u00020%2\f\u0010x\u001a\b\u0012\u0004\u0012\u0002000yH\u0002J4\u0010z\u001a\u00020\u00132\u0006\u0010u\u001a\u00020!2\u0006\u0010v\u001a\u00020!2\u0006\u0010{\u001a\u00020%2\u0012\u0010|\u001a\u000e\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u0002000}H\u0002J\u001d\u0010~\u001a\u00020\u00132\b\b\u0002\u0010\u007f\u001a\u00020#2\t\b\u0002\u0010\u0080\u0001\u001a\u00020#H\u0002J\u0011\u0010\u0081\u0001\u001a\u00020\u000f2\u0006\u0010:\u001a\u00020!H\u0002J\u0011\u0010\u0082\u0001\u001a\u00020\u000f2\u0006\u0010:\u001a\u00020!H\u0002J4\u0010\u0083\u0001\u001a\u00020\u000f2\u0007\u0010\u0084\u0001\u001a\u00020#2\u0007\u0010\u0085\u0001\u001a\u00020!2\t\b\u0002\u0010\u0086\u0001\u001a\u00020#2\f\u0010x\u001a\b\u0012\u0004\u0012\u0002000yH\u0002J\f\u0010\u0087\u0001\u001a\u0005\u0018\u00010\u0088\u0001H\u0002J\u0014\u0010\u0089\u0001\u001a\u00030\u008a\u00012\b\u0010\u008b\u0001\u001a\u00030\u008c\u0001H\u0002J\u0013\u0010\u008d\u0001\u001a\u00030\u008e\u00012\u0007\u0010\u008f\u0001\u001a\u00020#H\u0002J\u0012\u0010\u0090\u0001\u001a\u00020#2\u0007\u0010\u008f\u0001\u001a\u00020#H\u0002J\u0011\u0010\u0091\u0001\u001a\u00020!2\u0006\u0010H\u001a\u00020IH\u0002J\t\u0010\u0092\u0001\u001a\u00020%H\u0002J\t\u0010\u0093\u0001\u001a\u00020%H\u0002J\t\u0010\u0094\u0001\u001a\u000200H\u0014J\t\u0010\u0095\u0001\u001a\u000200H\u0014J\t\u0010\u0096\u0001\u001a\u000200H\u0002J\t\u0010\u0097\u0001\u001a\u000200H\u0002J\t\u0010\u0098\u0001\u001a\u000200H\u0002J\t\u0010\u0099\u0001\u001a\u000200H\u0002J\u0012\u0010\u0099\u0001\u001a\u0002002\u0007\u0010\u009a\u0001\u001a\u00020!H\u0002J\u0012\u0010\u009b\u0001\u001a\u0002002\u0007\u0010\u009c\u0001\u001a\u00020*H\u0002J\u0012\u0010\u009d\u0001\u001a\u0002002\u0007\u0010\u009e\u0001\u001a\u00020!H\u0002J\t\u0010\u009f\u0001\u001a\u000200H\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0013X\u0082.¢\u0006\u0002\n\u0000R \u0010\u001e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020!0 0\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020#X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020!X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006¢\u0001"}, d2 = {"Lcom/carlink/cast/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "settings", "Lcom/carlink/cast/CastSettings;", "ring", "Lcom/carlink/cast/ui/StageRingView;", "stageDetail", "Landroid/widget/TextView;", "deviceName", "sourceBadge", "startButton", "Lcom/google/android/material/button/MaterialButton;", "stageProgress", "stageSegments", "", "Landroid/view/View;", "fpsMetric", "Lcom/carlink/cast/MainActivity$Metric;", "bitrateMetric", "resolutionMetric", "durationMetric", "fpsSpark", "Lcom/carlink/cast/ui/MetricsView;", "logToggle", "logText", "logBody", "peers", "", "Lkotlin/Pair;", "", "selectedPeerIndex", "", "logExpanded", "", "pendingCapture", "pendingStartSession", "pendingHonorCarConnect", "pendingExport", "Ljava/io/File;", "lastActiveStep", "durationSession", "durationAnchor", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "buildScreen", "buildTopBar", "buildDevicePill", "buildHero", "buildStageCard", "buildMetricsGrid", "metricCard", AnnotatedPrivateKey.LABEL, "unitText", "spark", "buildLogCard", "sectionTitle", TextBundle.TEXT_ENTRY, "toggleLog", "bindState", "render", "state", "Lcom/carlink/cast/CastState;", "renderStages", "sourceChipText", "shortSourceLabel", "mode", "Lcom/carlink/cast/SourceMode;", "durationText", "formatDuration", "millis", "onPrimaryAction", "startSession", CastService.EXTRA_CAPTURE, "Landroid/content/Intent;", CastService.EXTRA_CAPTURE_RESULT, "requestCaptureIfNeeded", "onActivityResult", "requestCode", "resultCode", "data", "ensurePermissions", "onRequestPermissionsResult", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "refreshPeers", "updateDeviceLabel", "selectedPeer", "showPeerSheet", "showSettings", "buildSourceBlock", "editBluetoothAddress", "showProtocol", "sheetContent", "Lcom/google/android/material/bottomsheet/BottomSheetDialog;", "Landroid/widget/LinearLayout;", "dragHandle", "sheetTitle", "showFrameRateSheet", "showSafeAreaSheet", "wallpaperStatus", "showWallpaperSheet", "initialPreview", "Landroid/graphics/Bitmap;", "showWallpaperCropSheet", "bitmap", "launchWallpaperPicker", "navRow", MessageBundle.TITLE_ENTRY, "detail", "secondary", "onClick", "Lkotlin/Function0;", "toggleRow", "checked", "onChange", "Lkotlin/Function1;", "divider", "top", "bottom", "primaryButton", "secondaryButton", "iconButton", "icon", "description", "size", "selectableBackground", "Landroid/graphics/drawable/Drawable;", "card", "Lcom/google/android/material/card/MaterialCardView;", "radiusDp", "", "tint", "Landroid/content/res/ColorStateList;", "res", "color", "sourceDetail", "isTouchServiceEnabled", "isMediaListenerEnabled", "onResume", "onStop", "openAccessibility", "openMediaListenerSettings", "openHonorCarConnect", "exportLog", "sessionId", "shareExported", "file", "toast", "message", "onDestroy", "Metric", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MainActivity extends AppCompatActivity {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final String HONOR_AUTO_MANAGER = "com.hihonor.auto.settings.ManagerActivity";

    @Deprecated
    public static final String HONOR_AUTO_PACKAGE = "com.hihonor.auto";

    @Deprecated
    public static final int REQUEST_CAPTURE = 22;

    @Deprecated
    public static final int REQUEST_EXPORT = 23;

    @Deprecated
    public static final int REQUEST_PERMISSIONS = 21;

    @Deprecated
    public static final int REQUEST_WALLPAPER = 24;
    private Metric bitrateMetric;
    private TextView deviceName;
    private long durationAnchor;
    private Metric durationMetric;
    private Metric fpsMetric;
    private MetricsView fpsSpark;
    private int lastActiveStep;
    private View logBody;
    private boolean logExpanded;
    private TextView logText;
    private TextView logToggle;
    private boolean pendingCapture;
    private File pendingExport;
    private boolean pendingHonorCarConnect;
    private boolean pendingStartSession;
    private Metric resolutionMetric;
    private StageRingView ring;
    private CastSettings settings;
    private TextView sourceBadge;
    private TextView stageDetail;
    private TextView stageProgress;
    private List<? extends View> stageSegments;
    private MaterialButton startButton;
    private final CoroutineScope scope = CoroutineScopeKt.CoroutineScope(Dispatchers.getMain().plus(SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null)));
    private final List<Pair<String, String>> peers = new ArrayList();
    private int selectedPeerIndex = -1;
    private String durationSession = "";

    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[CastStage.values().length];
            try {
                iArr[CastStage.STOPPED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CastStage.FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[SourceMode.values().length];
            try {
                iArr2[SourceMode.AUTO.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr2[SourceMode.CARLIFE.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr2[SourceMode.MIRROR.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$1 = iArr2;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.settings = new CastSettings(this);
        CastLog.INSTANCE.initialize(this);
        getWindow().setStatusBarColor(Color.parseColor("#0B1220"));
        getWindow().setNavigationBarColor(Color.parseColor("#0B1220"));
        setContentView(buildScreen());
        bindState();
        refreshPeers();
    }

    private final View buildScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1);
        root.setBackgroundColor(color(R.color.bg));
        root.addView(buildTopBar());
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setVerticalScrollBarEnabled(false);
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(1);
        column.setPadding(Ui.INSTANCE.dp(this, 20.0f), Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 20.0f), Ui.INSTANCE.dp(this, 28.0f));
        scroll.addView(column, new ViewGroup.LayoutParams(-1, -2));
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        column.addView(buildDevicePill(), new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(this, 46.0f)));
        View viewBuildHero = buildHero();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = Ui.INSTANCE.dp(this, 12.0f);
        Unit unit = Unit.INSTANCE;
        column.addView(viewBuildHero, layoutParams);
        View viewBuildMetricsGrid = buildMetricsGrid();
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.topMargin = Ui.INSTANCE.dp(this, 16.0f);
        Unit unit2 = Unit.INSTANCE;
        column.addView(viewBuildMetricsGrid, layoutParams2);
        View viewBuildStageCard = buildStageCard();
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams3.topMargin = Ui.INSTANCE.dp(this, 12.0f);
        Unit unit3 = Unit.INSTANCE;
        column.addView(viewBuildStageCard, layoutParams3);
        View viewBuildLogCard = buildLogCard();
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams4.topMargin = Ui.INSTANCE.dp(this, 12.0f);
        Unit unit4 = Unit.INSTANCE;
        column.addView(viewBuildLogCard, layoutParams4);
        return root;
    }

    private final View buildTopBar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(0);
        bar.setGravity(16);
        bar.setPadding(Ui.INSTANCE.dp(this, 20.0f), Ui.INSTANCE.dp(this, 16.0f), Ui.INSTANCE.dp(this, 12.0f), Ui.INSTANCE.dp(this, 10.0f));
        MaterialCardView mark = card(12.0f);
        mark.setClickable(false);
        mark.setCardBackgroundColor(color(R.color.surface_high));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_cast);
        imageView.setImageTintList(tint(R.color.cyan));
        mark.addView(imageView, new FrameLayout.LayoutParams(Ui.INSTANCE.dp(this, 20.0f), Ui.INSTANCE.dp(this, 20.0f), 17));
        bar.addView(mark, new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this, 40.0f), Ui.INSTANCE.dp(this, 40.0f)));
        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(1);
        titles.setPadding(Ui.INSTANCE.dp(this, 12.0f), 0, 0, 0);
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.app_name));
        Ui.style$default(Ui.INSTANCE, textView, 18.0f, R.color.text_primary, true, false, 8, null);
        titles.addView(textView);
        TextView textView2 = new TextView(this);
        textView2.setText(getString(R.string.app_subtitle));
        Ui.style$default(Ui.INSTANCE, textView2, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView2.setPadding(0, Ui.INSTANCE.dp(this, 1.0f), 0, 0);
        titles.addView(textView2);
        bar.addView(titles, new LinearLayout.LayoutParams(0, -2, 1.0f));
        int i = R.drawable.ic_info;
        String string = getString(R.string.protocol_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        bar.addView(iconButton$default(this, i, string, 0, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda26
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildTopBar$lambda$13(this.f$0);
            }
        }, 4, null));
        int i2 = R.drawable.ic_settings;
        String string2 = getString(R.string.settings_title);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        bar.addView(iconButton$default(this, i2, string2, 0, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda27
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildTopBar$lambda$14(this.f$0);
            }
        }, 4, null));
        return bar;
    }

    static final Unit buildTopBar$lambda$13(MainActivity this$0) {
        this$0.showProtocol();
        return Unit.INSTANCE;
    }

    static final Unit buildTopBar$lambda$14(MainActivity this$0) {
        this$0.showSettings();
        return Unit.INSTANCE;
    }

    private final View buildDevicePill() {
        MaterialCardView pillCard = card(23.0f);
        pillCard.setClickable(true);
        pillCard.setFocusable(true);
        pillCard.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda32
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.showPeerSheet();
            }
        });
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(0);
        row.setGravity(16);
        row.setPadding(Ui.INSTANCE.dp(this, 14.0f), 0, Ui.INSTANCE.dp(this, 5.0f), 0);
        pillCard.addView(row, new FrameLayout.LayoutParams(-1, -1));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_car);
        imageView.setImageTintList(tint(R.color.accent));
        row.addView(imageView, new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this, 18.0f), Ui.INSTANCE.dp(this, 18.0f)));
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.device_none));
        Ui.style$default(Ui.INSTANCE, textView, 13.0f, R.color.text_primary, false, false, 12, null);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        this.deviceName = textView;
        TextView textView2 = this.deviceName;
        TextView textView3 = null;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("deviceName");
            textView2 = null;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.setMarginStart(Ui.INSTANCE.dp(this, 10.0f));
        Unit unit = Unit.INSTANCE;
        row.addView(textView2, layoutParams);
        TextView textView4 = new TextView(this);
        CastSettings castSettings = this.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        textView4.setText(shortSourceLabel(castSettings.getSourceMode()));
        Ui.style$default(Ui.INSTANCE, textView4, 10.0f, R.color.text_secondary, false, false, 12, null);
        textView4.setPadding(Ui.INSTANCE.dp(this, 8.0f), Ui.INSTANCE.dp(this, 3.0f), Ui.INSTANCE.dp(this, 8.0f), Ui.INSTANCE.dp(this, 3.0f));
        textView4.setBackground(Ui.INSTANCE.pill(this, R.color.track));
        this.sourceBadge = textView4;
        TextView textView5 = this.sourceBadge;
        if (textView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sourceBadge");
        } else {
            textView3 = textView5;
        }
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.setMarginEnd(Ui.INSTANCE.dp(this, 2.0f));
        Unit unit2 = Unit.INSTANCE;
        row.addView(textView3, layoutParams2);
        int i = R.drawable.ic_refresh;
        String string = getString(R.string.device_refresh);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        row.addView(iconButton(i, string, 36, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda33
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildDevicePill$lambda$23(this.f$0);
            }
        }));
        return pillCard;
    }

    static final Unit buildDevicePill$lambda$23(MainActivity this$0) {
        this$0.refreshPeers();
        String string = this$0.getString(R.string.toast_refreshed);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        this$0.toast(string);
        return Unit.INSTANCE;
    }

    private final View buildHero() {
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(1);
        column.setGravity(1);
        int ringSize = getResources().getDimensionPixelSize(R.dimen.ring_size);
        this.ring = new StageRingView(this);
        FrameLayout ringBox = new FrameLayout(this);
        StageRingView stageRingView = this.ring;
        MaterialButton materialButton = null;
        if (stageRingView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ring");
            stageRingView = null;
        }
        ringBox.addView(stageRingView, new FrameLayout.LayoutParams(ringSize, ringSize, 17));
        column.addView(ringBox, new LinearLayout.LayoutParams(-1, ringSize));
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.status_idle_detail));
        Ui.style$default(Ui.INSTANCE, textView, 12.5f, R.color.text_secondary, false, false, 12, null);
        textView.setGravity(17);
        textView.setMaxLines(2);
        textView.setMinHeight(Ui.INSTANCE.dp(this, 38.0f));
        textView.setLineSpacing(Ui.INSTANCE.dp(this, 2.0f), 1.0f);
        textView.setPadding(Ui.INSTANCE.dp(this, 8.0f), Ui.INSTANCE.dp(this, 2.0f), Ui.INSTANCE.dp(this, 8.0f), 0);
        this.stageDetail = textView;
        TextView textView2 = this.stageDetail;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stageDetail");
            textView2 = null;
        }
        column.addView(textView2, new LinearLayout.LayoutParams(-1, -2));
        MaterialButton materialButton2 = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
        materialButton2.setText(getString(R.string.action_start));
        materialButton2.setTextSize(16.0f);
        materialButton2.setAllCaps(false);
        materialButton2.setTypeface(Typeface.create("sans-serif-medium", 1));
        materialButton2.setTextColor(color(R.color.on_accent));
        materialButton2.setBackgroundTintList(tint(R.color.accent));
        materialButton2.setCornerRadius(Ui.INSTANCE.dp(this, 20.0f));
        materialButton2.setInsetTop(0);
        materialButton2.setInsetBottom(0);
        materialButton2.setStateListAnimator(null);
        materialButton2.setIconResource(R.drawable.ic_cast);
        materialButton2.setIconSize(Ui.INSTANCE.dp(this, 20.0f));
        materialButton2.setIconPadding(Ui.INSTANCE.dp(this, 10.0f));
        materialButton2.setIconGravity(2);
        materialButton2.setIconTint(tint(R.color.on_accent));
        materialButton2.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.onPrimaryAction();
            }
        });
        this.startButton = materialButton2;
        MaterialButton materialButton3 = this.startButton;
        if (materialButton3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startButton");
        } else {
            materialButton = materialButton3;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(this, 56.0f));
        layoutParams.topMargin = Ui.INSTANCE.dp(this, 14.0f);
        Unit unit = Unit.INSTANCE;
        column.addView(materialButton, layoutParams);
        return column;
    }

    private final View buildStageCard() {
        MaterialCardView stageCard = card(20.0f);
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(1);
        column.setPadding(Ui.INSTANCE.dp(this, 16.0f), Ui.INSTANCE.dp(this, 14.0f), Ui.INSTANCE.dp(this, 16.0f), Ui.INSTANCE.dp(this, 15.0f));
        stageCard.addView(column);
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(0);
        header.setGravity(16);
        String string = getString(R.string.stages_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        header.addView(sectionTitle(string), new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.stage_not_started));
        Ui.style$default(Ui.INSTANCE, textView, 11.0f, R.color.text_secondary, false, false, 12, null);
        this.stageProgress = textView;
        TextView textView2 = this.stageProgress;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stageProgress");
            textView2 = null;
        }
        header.addView(textView2);
        column.addView(header);
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(0);
        bar.setGravity(16);
        List segments = new ArrayList();
        int i = 0;
        for (Object obj : CastStage.INSTANCE.getLadder()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            View view = new View(this);
            MaterialCardView stageCard2 = stageCard;
            LinearLayout header2 = header;
            view.setBackground(Ui.INSTANCE.pill(this, R.color.track));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 4.0f), 1.0f);
            if (i < CollectionsKt.getLastIndex(CastStage.INSTANCE.getLadder())) {
                layoutParams.setMarginEnd(Ui.INSTANCE.dp(this, 4.0f));
            }
            bar.addView(view, layoutParams);
            segments.add(view);
            i = i2;
            stageCard = stageCard2;
            header = header2;
        }
        MaterialCardView stageCard3 = stageCard;
        this.stageSegments = segments;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.topMargin = Ui.INSTANCE.dp(this, 11.0f);
        Unit unit = Unit.INSTANCE;
        column.addView(bar, layoutParams2);
        return stageCard3;
    }

    private final View buildMetricsGrid() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(0);
        linearLayout2.setWeightSum(2.0f);
        String string = getString(R.string.metrics_fps);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        this.fpsMetric = metricCard(string, getString(R.string.unit_fps), true);
        Metric metric = this.fpsMetric;
        Metric metric2 = null;
        if (metric == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fpsMetric");
            metric = null;
        }
        MaterialCardView card = metric.getCard();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.setMarginEnd(Ui.INSTANCE.dp(this, 5.0f));
        Unit unit = Unit.INSTANCE;
        linearLayout2.addView(card, layoutParams);
        String string2 = getString(R.string.metrics_bitrate);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        this.bitrateMetric = metricCard$default(this, string2, getString(R.string.unit_kbps), false, 4, null);
        Metric metric3 = this.bitrateMetric;
        if (metric3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bitrateMetric");
            metric3 = null;
        }
        MaterialCardView card2 = metric3.getCard();
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams2.setMarginStart(Ui.INSTANCE.dp(this, 5.0f));
        Unit unit2 = Unit.INSTANCE;
        linearLayout2.addView(card2, layoutParams2);
        linearLayout.addView(linearLayout2);
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(0);
        linearLayout3.setWeightSum(2.0f);
        String string3 = getString(R.string.metrics_resolution);
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        this.resolutionMetric = metricCard$default(this, string3, null, false, 4, null);
        Metric metric4 = this.resolutionMetric;
        if (metric4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("resolutionMetric");
            metric4 = null;
        }
        MaterialCardView card3 = metric4.getCard();
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams3.setMarginEnd(Ui.INSTANCE.dp(this, 5.0f));
        Unit unit3 = Unit.INSTANCE;
        linearLayout3.addView(card3, layoutParams3);
        String string4 = getString(R.string.metrics_duration);
        Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
        this.durationMetric = metricCard$default(this, string4, null, false, 4, null);
        Metric metric5 = this.durationMetric;
        if (metric5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("durationMetric");
        } else {
            metric2 = metric5;
        }
        MaterialCardView card4 = metric2.getCard();
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams4.setMarginStart(Ui.INSTANCE.dp(this, 5.0f));
        Unit unit4 = Unit.INSTANCE;
        linearLayout3.addView(card4, layoutParams4);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams5.topMargin = Ui.INSTANCE.dp(this, 10.0f);
        Unit unit5 = Unit.INSTANCE;
        linearLayout.addView(linearLayout3, layoutParams5);
        return linearLayout;
    }

    static /* synthetic */ Metric metricCard$default(MainActivity mainActivity, String str, String str2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return mainActivity.metricCard(str, str2, z);
    }

    private final Metric metricCard(String label, String unitText, boolean spark) {
        MaterialCardView metricCard = card(20.0f);
        metricCard.setMinimumHeight(Ui.INSTANCE.dp(this, 76.0f));
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(1);
        content.setGravity(16);
        content.setPadding(Ui.INSTANCE.dp(this, 14.0f), Ui.INSTANCE.dp(this, 11.0f), Ui.INSTANCE.dp(this, 14.0f), Ui.INSTANCE.dp(this, 11.0f));
        metricCard.addView(content, new FrameLayout.LayoutParams(-1, -2, 16));
        TextView textView = new TextView(this);
        textView.setText(label);
        Ui.style$default(Ui.INSTANCE, textView, 10.5f, R.color.text_tertiary, true, false, 8, null);
        textView.setLetterSpacing(0.04f);
        content.addView(textView);
        LinearLayout valueRow = new LinearLayout(this);
        valueRow.setOrientation(0);
        valueRow.setGravity(16);
        TextView textView2 = new TextView(this);
        textView2.setText("—");
        Ui.style$default(Ui.INSTANCE, textView2, 21.0f, R.color.text_primary, true, false, 8, null);
        textView2.setMaxLines(1);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        valueRow.addView(textView2);
        TextView textView3 = new TextView(this);
        textView3.setText(unitText == null ? "" : unitText);
        Ui.style$default(Ui.INSTANCE, textView3, 10.0f, R.color.text_tertiary, false, false, 12, null);
        textView3.setPadding(Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 4.0f), 0, 0);
        valueRow.addView(textView3);
        if (spark) {
            valueRow.addView(new View(this), new LinearLayout.LayoutParams(0, 1, 1.0f));
            this.fpsSpark = new MetricsView(this);
            MetricsView metricsView = this.fpsSpark;
            if (metricsView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("fpsSpark");
                metricsView = null;
            }
            valueRow.addView(metricsView, new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this, 52.0f), Ui.INSTANCE.dp(this, 22.0f)));
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = Ui.INSTANCE.dp(this, 3.0f);
        Unit unit = Unit.INSTANCE;
        content.addView(valueRow, layoutParams);
        return new Metric(metricCard, textView2, textView3);
    }

    private final View buildLogCard() {
        TextView textView;
        MaterialCardView logCard = card(20.0f);
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(1);
        logCard.addView(column);
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(0);
        header.setGravity(16);
        header.setPadding(Ui.INSTANCE.dp(this, 16.0f), Ui.INSTANCE.dp(this, 14.0f), Ui.INSTANCE.dp(this, 16.0f), Ui.INSTANCE.dp(this, 14.0f));
        header.setClickable(true);
        header.setFocusable(true);
        header.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.toggleLog();
            }
        });
        String string = getString(R.string.logs_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        header.addView(sectionTitle(string), new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView textView2 = new TextView(this);
        textView2.setText(getString(R.string.logs_expand));
        Ui.style$default(Ui.INSTANCE, textView2, 11.5f, R.color.accent, true, false, 8, null);
        this.logToggle = textView2;
        TextView textView3 = this.logToggle;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logToggle");
            textView3 = null;
        }
        header.addView(textView3);
        column.addView(header);
        ScrollView scroll = new ScrollView(this);
        scroll.setVisibility(8);
        scroll.setVerticalScrollBarEnabled(false);
        TextView textView4 = new TextView(this);
        textView4.setText(getString(R.string.logs_empty));
        Ui.style$default(Ui.INSTANCE, textView4, 10.5f, R.color.text_tertiary, false, true, 4, null);
        textView4.setTextIsSelectable(true);
        textView4.setLineSpacing(Ui.INSTANCE.dp(this, 2.0f), 1.0f);
        textView4.setPadding(Ui.INSTANCE.dp(this, 16.0f), 0, Ui.INSTANCE.dp(this, 16.0f), Ui.INSTANCE.dp(this, 14.0f));
        this.logText = textView4;
        TextView textView5 = this.logText;
        if (textView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logText");
            textView = null;
        } else {
            textView = textView5;
        }
        scroll.addView(textView, new ViewGroup.LayoutParams(-1, -2));
        column.addView(scroll, new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(this, 150.0f)));
        this.logBody = scroll;
        return logCard;
    }

    private final TextView sectionTitle(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        Ui.style$default(Ui.INSTANCE, textView, 11.0f, R.color.text_tertiary, true, false, 8, null);
        textView.setLetterSpacing(0.08f);
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void toggleLog() {
        this.logExpanded = !this.logExpanded;
        TextView textView = this.logToggle;
        View view = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logToggle");
            textView = null;
        }
        textView.setText(getString(this.logExpanded ? R.string.logs_collapse : R.string.logs_expand));
        View view2 = this.logBody;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logBody");
        } else {
            view = view2;
        }
        view.setVisibility(this.logExpanded ? 0 : 8);
    }

    /* JADX INFO: renamed from: com.carlink.cast.MainActivity$bindState$1, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.carlink.cast.MainActivity$bindState$1", f = "MainActivity.kt", i = {}, l = {415}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return MainActivity.this.new AnonymousClass1(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX INFO: renamed from: com.carlink.cast.MainActivity$bindState$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: MainActivity.kt */
        @Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "state", "Lcom/carlink/cast/CastState;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.carlink.cast.MainActivity$bindState$1$1", f = "MainActivity.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
        static final class C00041 extends SuspendLambda implements Function2<CastState, Continuation<? super Unit>, Object> {
            /* synthetic */ Object L$0;
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00041(MainActivity mainActivity, Continuation<? super C00041> continuation) {
                super(2, continuation);
                this.this$0 = mainActivity;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                C00041 c00041 = new C00041(this.this$0, continuation);
                c00041.L$0 = obj;
                return c00041;
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CastState castState, Continuation<? super Unit> continuation) {
                return ((C00041) create(castState, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) throws Throwable {
                CastState state = (CastState) this.L$0;
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.this$0.render(state);
                        return Unit.INSTANCE;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (FlowKt.collectLatest(CastLog.INSTANCE.getState(), new C00041(MainActivity.this, null), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    private final void bindState() {
        BuildersKt__Builders_commonKt.launch$default(this.scope, null, null, new AnonymousClass1(null), 3, null);
        BuildersKt__Builders_commonKt.launch$default(this.scope, null, null, new AnonymousClass2(null), 3, null);
    }

    /* JADX INFO: renamed from: com.carlink.cast.MainActivity$bindState$2, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.carlink.cast.MainActivity$bindState$2", f = "MainActivity.kt", i = {}, l = {421}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        AnonymousClass2(Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return MainActivity.this.new AnonymousClass2(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:10:0x002d A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:13:0x0040  */
        /* JADX WARN: Code duplicated, block: B:15:0x0048  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:9:0x002b -> B:11:0x002e). Please report as a decompilation issue!!! */
        /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached at block B:7:0x0018
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
            */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object r6) {
            /*
                r5 = this;
                java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r1 = r5.label
                switch(r1) {
                    case 0: goto L18;
                    case 1: goto L11;
                    default: goto L9;
                }
            L9:
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
                r0.<init>(r1)
                throw r0
            L11:
                kotlin.ResultKt.throwOnFailure(r6)
                r1 = r0
                r0 = r6
                r6 = r5
                goto L2e
            L18:
                kotlin.ResultKt.throwOnFailure(r6)
                r1 = r0
                r0 = r6
                r6 = r5
            L1e:
                r2 = r6
                kotlin.coroutines.Continuation r2 = (kotlin.coroutines.Continuation) r2
                r3 = 1
                r6.label = r3
                r3 = 1000(0x3e8, double:4.94E-321)
                java.lang.Object r2 = kotlinx.coroutines.DelayKt.delay(r3, r2)
                if (r2 != r1) goto L2e
                return r1
            L2e:
                com.carlink.cast.CastLog r2 = com.carlink.cast.CastLog.INSTANCE
                kotlinx.coroutines.flow.StateFlow r2 = r2.getState()
                java.lang.Object r2 = r2.getValue()
                com.carlink.cast.CastState r2 = (com.carlink.cast.CastState) r2
                boolean r3 = r2.getRunning()
                if (r3 == 0) goto L1e
                com.carlink.cast.MainActivity r3 = com.carlink.cast.MainActivity.this
                com.carlink.cast.MainActivity$Metric r3 = com.carlink.cast.MainActivity.access$getDurationMetric$p(r3)
                if (r3 != 0) goto L4e
                java.lang.String r3 = "durationMetric"
                kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
                r3 = 0
            L4e:
                android.widget.TextView r3 = r3.getValue()
                com.carlink.cast.MainActivity r4 = com.carlink.cast.MainActivity.this
                java.lang.String r4 = com.carlink.cast.MainActivity.access$durationText(r4, r2)
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4
                r3.setText(r4)
                goto L1e
            */
            throw new UnsupportedOperationException("Method not decompiled: com.carlink.cast.MainActivity.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:30:0x00a6  */
    public final void render(CastState state) {
        String detail;
        String str;
        if (state.getStage() != CastStage.FAILED && state.getStage() != CastStage.STOPPED) {
            this.lastActiveStep = state.getStage().getStep();
        }
        StageRingView stageRingView = this.ring;
        MaterialButton materialButton = null;
        if (stageRingView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ring");
            stageRingView = null;
        }
        stageRingView.setFailedStep(this.lastActiveStep);
        StageRingView stageRingView2 = this.ring;
        if (stageRingView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ring");
            stageRingView2 = null;
        }
        stageRingView2.setState(state);
        TextView textView = this.stageDetail;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stageDetail");
            textView = null;
        }
        if (state.getStage() == CastStage.STOPPED && StringsKt.isBlank(state.getSessionId())) {
            detail = getString(R.string.status_idle_detail);
        } else {
            detail = state.getDetail();
        }
        textView.setText(detail);
        CastMetric latest = (CastMetric) CollectionsKt.lastOrNull((List) state.getMetrics());
        Metric metric = this.fpsMetric;
        if (metric == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fpsMetric");
            metric = null;
        }
        TextView value = metric.getValue();
        String str2 = "—";
        if (latest != null) {
            String str3 = String.format(Locale.US, "%.1f", Arrays.copyOf(new Object[]{Double.valueOf(latest.getFps())}, 1));
            Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
            if (str3 != null) {
                str = str3;
            }
        }
        value.setText(str);
        if (latest == null) {
            Metric metric2 = this.bitrateMetric;
            if (metric2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitrateMetric");
                metric2 = null;
            }
            metric2.getValue().setText("—");
            Metric metric3 = this.bitrateMetric;
            if (metric3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitrateMetric");
                metric3 = null;
            }
            metric3.getUnit().setText(getString(R.string.unit_kbps));
        } else {
            double kbps = latest.getKbps();
            Metric metric4 = this.bitrateMetric;
            if (kbps >= 1000.0d) {
                if (metric4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("bitrateMetric");
                    metric4 = null;
                }
                TextView value2 = metric4.getValue();
                String str4 = String.format(Locale.US, "%.1f", Arrays.copyOf(new Object[]{Double.valueOf(latest.getKbps() / 1000.0d)}, 1));
                Intrinsics.checkNotNullExpressionValue(str4, "format(...)");
                value2.setText(str4);
                Metric metric5 = this.bitrateMetric;
                if (metric5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("bitrateMetric");
                    metric5 = null;
                }
                metric5.getUnit().setText(getString(R.string.unit_mbps));
            } else {
                if (metric4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("bitrateMetric");
                    metric4 = null;
                }
                TextView value3 = metric4.getValue();
                String str5 = String.format(Locale.US, "%.0f", Arrays.copyOf(new Object[]{Double.valueOf(latest.getKbps())}, 1));
                Intrinsics.checkNotNullExpressionValue(str5, "format(...)");
                value3.setText(str5);
                Metric metric6 = this.bitrateMetric;
                if (metric6 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("bitrateMetric");
                    metric6 = null;
                }
                metric6.getUnit().setText(getString(R.string.unit_kbps));
            }
        }
        Metric metric7 = this.resolutionMetric;
        if (metric7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("resolutionMetric");
            metric7 = null;
        }
        TextView value4 = metric7.getValue();
        String string = StringsKt.trim((CharSequence) StringsKt.substringBefore$default(state.getDisplay(), "·", (String) null, 2, (Object) null)).toString();
        if (!StringsKt.isBlank(string)) {
            str2 = string;
        }
        value4.setText(str2);
        Metric metric8 = this.durationMetric;
        if (metric8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("durationMetric");
            metric8 = null;
        }
        metric8.getValue().setText(durationText(state));
        MetricsView metricsView = this.fpsSpark;
        if (metricsView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fpsSpark");
            metricsView = null;
        }
        metricsView.setSamples(state.getMetrics());
        TextView textView2 = this.logText;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("logText");
            textView2 = null;
        }
        String strJoinToString$default = CollectionsKt.joinToString$default(CollectionsKt.takeLast(state.getEvents(), 80), "\n", null, null, 0, null, new Function1() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.render$lambda$60((CastEvent) obj);
            }
        }, 30, null);
        if (StringsKt.isBlank(strJoinToString$default)) {
            String string2 = getString(R.string.logs_empty);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            strJoinToString$default = string2;
        }
        textView2.setText(strJoinToString$default);
        TextView textView3 = this.sourceBadge;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sourceBadge");
            textView3 = null;
        }
        textView3.setText(sourceChipText(state));
        boolean running = state.getRunning();
        MaterialButton materialButton2 = this.startButton;
        if (materialButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startButton");
            materialButton2 = null;
        }
        materialButton2.setText(getString(running ? R.string.action_stop : R.string.action_start));
        MaterialButton materialButton3 = this.startButton;
        if (materialButton3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startButton");
            materialButton3 = null;
        }
        materialButton3.setIconResource(running ? R.drawable.ic_close : R.drawable.ic_cast);
        int foreground = color(running ? R.color.on_danger : R.color.on_accent);
        MaterialButton materialButton4 = this.startButton;
        if (materialButton4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startButton");
            materialButton4 = null;
        }
        materialButton4.setTextColor(foreground);
        MaterialButton materialButton5 = this.startButton;
        if (materialButton5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startButton");
            materialButton5 = null;
        }
        materialButton5.setIconTint(ColorStateList.valueOf(foreground));
        MaterialButton materialButton6 = this.startButton;
        if (materialButton6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startButton");
        } else {
            materialButton = materialButton6;
        }
        materialButton.setBackgroundTintList(tint(running ? R.color.danger : R.color.accent));
        renderStages(state);
    }

    static final CharSequence render$lambda$60(CastEvent it) {
        Intrinsics.checkNotNullParameter(it, "it");
        int seconds = (int) (it.getElapsedMillis() / 1000);
        String str = String.format(Locale.US, "%02d:%02d  %s", Arrays.copyOf(new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60), it.getMessage()}, 3));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private final void renderStages(CastState state) {
        String string;
        int i;
        int step = state.getStage().getStep();
        int failedStep = state.getStage() == CastStage.FAILED ? this.lastActiveStep : 0;
        Iterable iterable = this.stageSegments;
        TextView textView = null;
        if (iterable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stageSegments");
            iterable = null;
        }
        int i2 = 0;
        for (Object obj : iterable) {
            int i3 = i2 + 1;
            if (i2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            View view = (View) obj;
            int i4 = i2 + 1;
            switch (WhenMappings.$EnumSwitchMapping$0[state.getStage().ordinal()]) {
                case 1:
                    i = state.getFrames() > 0 ? R.color.accent : R.color.track;
                    break;
                case 2:
                    if (failedStep == 0) {
                        i = R.color.track;
                    } else if (i4 < failedStep) {
                        i = R.color.accent;
                    } else if (i4 == failedStep) {
                        i = R.color.danger;
                    } else {
                        i = R.color.track;
                    }
                    break;
                default:
                    if (i4 < step) {
                        i = R.color.accent;
                    } else if (i4 == step) {
                        i = state.getStage() == CastStage.STREAMING ? R.color.success : R.color.cyan;
                    } else {
                        i = R.color.track;
                    }
                    break;
            }
            view.setBackground(Ui.INSTANCE.pill(this, i));
            i2 = i3;
        }
        TextView textView2 = this.stageProgress;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stageProgress");
        } else {
            textView = textView2;
        }
        if (state.getStage() == CastStage.STOPPED) {
            string = getString(StringsKt.isBlank(state.getSessionId()) ? R.string.stage_not_started : R.string.stage_ended);
        } else {
            string = state.getStage() == CastStage.FAILED ? getString(R.string.stage_interrupted) : getString(R.string.stage_step, new Object[]{Integer.valueOf(state.getStage().getStep()), Integer.valueOf(CastStage.STREAMING.getStep())});
        }
        textView.setText(string);
    }

    private final String sourceChipText(CastState state) {
        if (!StringsKt.contains((CharSequence) state.getSource(), (CharSequence) "CarLife", true)) {
            if (StringsKt.isBlank(state.getSource())) {
                CastSettings castSettings = this.settings;
                if (castSettings == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settings");
                    castSettings = null;
                }
                return shortSourceLabel(castSettings.getSourceMode());
            }
            String string = getString(R.string.source_short_mirror);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            return string;
        }
        String string2 = getString(R.string.source_short_carlife);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        return string2;
    }

    private final String shortSourceLabel(SourceMode mode) {
        String string;
        switch (WhenMappings.$EnumSwitchMapping$1[mode.ordinal()]) {
            case 1:
                string = getString(R.string.source_short_auto);
                break;
            case 2:
                string = getString(R.string.source_short_carlife);
                break;
            case 3:
                string = getString(R.string.source_short_mirror);
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        Intrinsics.checkNotNull(string);
        return string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String durationText(CastState state) {
        long elapsed;
        if (StringsKt.isBlank(state.getSessionId())) {
            return "—";
        }
        CastEvent castEvent = (CastEvent) CollectionsKt.lastOrNull((List) state.getEvents());
        long elapsedMillis = castEvent != null ? castEvent.getElapsedMillis() : 0L;
        CastMetric castMetric = (CastMetric) CollectionsKt.lastOrNull((List) state.getMetrics());
        long lastReported = Math.max(elapsedMillis, castMetric != null ? castMetric.getElapsedMillis() : 0L);
        if (!Intrinsics.areEqual(state.getSessionId(), this.durationSession)) {
            this.durationSession = state.getSessionId();
            this.durationAnchor = SystemClock.elapsedRealtime() - lastReported;
        }
        if (state.getRunning()) {
            elapsed = SystemClock.elapsedRealtime() - this.durationAnchor;
        } else {
            elapsed = lastReported;
        }
        return formatDuration(elapsed);
    }

    private final String formatDuration(long millis) {
        long total = RangesKt.coerceAtLeast(millis / 1000, 0L);
        long hours = total / 3600;
        long minutes = (total % 3600) / 60;
        long seconds = total % 60;
        if (hours > 0) {
            String str = String.format(Locale.US, "%d:%02d:%02d", Arrays.copyOf(new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)}, 3));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            return str;
        }
        String str2 = String.format(Locale.US, "%02d:%02d", Arrays.copyOf(new Object[]{Long.valueOf(minutes), Long.valueOf(seconds)}, 2));
        Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onPrimaryAction() {
        if (CastLog.INSTANCE.getState().getValue().getRunning()) {
            startService(new Intent(this, (Class<?>) CastService.class).setAction(CastService.ACTION_STOP));
            return;
        }
        if (this.pendingCapture) {
            return;
        }
        boolean z = true;
        this.pendingStartSession = true;
        if (ensurePermissions()) {
            this.pendingStartSession = false;
            Pair<String, String> pairSelectedPeer = selectedPeer();
            if (pairSelectedPeer == null) {
                MainActivity mainActivity = this;
                String string = mainActivity.getString(R.string.toast_no_peer);
                Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
                mainActivity.toast(string);
                return;
            }
            CastSettings castSettings = this.settings;
            if (castSettings == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
                castSettings = null;
            }
            castSettings.setPeerAddress(pairSelectedPeer.getFirst());
            CastSettings castSettings2 = this.settings;
            if (castSettings2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
                castSettings2 = null;
            }
            castSettings2.setPeerName(pairSelectedPeer.getSecond());
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BluetoothManager.class);
            BluetoothAdapter adapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
            if (adapter == null || !adapter.isEnabled()) {
                z = false;
            }
            if (!z) {
                String string2 = getString(R.string.toast_no_bt);
                Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
                toast(string2);
                return;
            }
            BluetoothDevice device = adapter.getRemoteDevice(pairSelectedPeer.getFirst());
            if (device.getBondState() != 12) {
                String string3 = getString(R.string.toast_pair_first);
                Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
                toast(string3);
                return;
            }
            requestCaptureIfNeeded();
        }
    }

    private final void startSession(Intent capture, int captureResult) {
        Intent request = new Intent(this, (Class<?>) CastService.class).setAction(CastService.ACTION_START).putExtra(CastService.EXTRA_CAPTURE_RESULT, captureResult);
        Intrinsics.checkNotNullExpressionValue(request, "putExtra(...)");
        if (capture != null) {
            if (Build.VERSION.SDK_INT >= 33) {
                request.putExtra(CastService.EXTRA_CAPTURE, capture);
            } else {
                request.putExtra(CastService.EXTRA_CAPTURE, capture);
            }
        }
        ContextCompat.startForegroundService(this, request);
    }

    private final boolean requestCaptureIfNeeded() {
        Intent consent;
        CastSettings castSettings = this.settings;
        CastSettings castSettings2 = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        boolean usesCarLife = castSettings.getSourceMode() != SourceMode.MIRROR && CastEngine.INSTANCE.carLifeAvailable(this);
        if (usesCarLife) {
            startSession(null, 0);
            return true;
        }
        CastSettings castSettings3 = this.settings;
        if (castSettings3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
        } else {
            castSettings2 = castSettings3;
        }
        if (castSettings2.getSourceMode() == SourceMode.CARLIFE) {
            String string = getString(R.string.toast_carlife_missing);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
            return true;
        }
        MediaProjectionManager manager = (MediaProjectionManager) getSystemService(MediaProjectionManager.class);
        this.pendingCapture = true;
        if (Build.VERSION.SDK_INT >= 34) {
            consent = manager.createScreenCaptureIntent(MediaProjectionConfig.createConfigForDefaultDisplay());
        } else {
            consent = manager.createScreenCaptureIntent();
        }
        Intrinsics.checkNotNull(consent);
        startActivityForResult(consent, 22);
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Object objM159constructorimpl;
        Throwable th;
        Throwable th2;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 23) {
            if (requestCode == 24) {
                Uri uri = data != null ? data.getData() : null;
                if (resultCode != -1 || uri == null) {
                    return;
                }
                BuildersKt__Builders_commonKt.launch$default(this.scope, Dispatchers.getIO(), null, new AnonymousClass4(uri, null), 2, null);
                return;
            }
            if (requestCode != 22) {
                return;
            }
            this.pendingCapture = false;
            if (resultCode == -1 && data != null) {
                startSession(data, resultCode);
                return;
            }
            String string = getString(R.string.toast_capture_canceled);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
            return;
        }
        File file = this.pendingExport;
        this.pendingExport = null;
        Uri uri2 = data != null ? data.getData() : null;
        if (resultCode != -1 || uri2 == null || file == null) {
            return;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            OutputStream outputStreamOpenOutputStream = getContentResolver().openOutputStream(uri2);
            if (outputStreamOpenOutputStream == null) {
                throw new IllegalStateException("无法写入所选位置".toString());
            }
            OutputStream outputStream = outputStreamOpenOutputStream;
            try {
                try {
                    OutputStream outputStream2 = outputStream;
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        try {
                            long jCopyTo$default = ByteStreamsKt.copyTo$default(fileInputStream, outputStream2, 0, 2, null);
                            CloseableKt.closeFinally(fileInputStream, null);
                            CloseableKt.closeFinally(outputStream, null);
                            objM159constructorimpl = Result.m159constructorimpl(Long.valueOf(jCopyTo$default));
                        } catch (Throwable th3) {
                            th2 = th3;
                            try {
                                throw th2;
                            } catch (Throwable th4) {
                                CloseableKt.closeFinally(fileInputStream, th2);
                                throw th4;
                            }
                        }
                    } catch (Throwable th5) {
                        th2 = th5;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    try {
                        throw th;
                    } catch (Throwable th7) {
                        CloseableKt.closeFinally(outputStream, th);
                        throw th7;
                    }
                }
            } catch (Throwable th8) {
                th = th8;
                throw th;
            }
        } catch (Throwable th9) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th9));
        }
        if (Result.m166isSuccessimpl(objM159constructorimpl)) {
            ((Number) objM159constructorimpl).longValue();
            String string2 = getString(R.string.toast_export_saved);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            toast(string2);
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            int i = R.string.toast_export_failed;
            String message = thM162exceptionOrNullimpl.getMessage();
            if (message == null) {
                message = thM162exceptionOrNullimpl.getClass().getSimpleName();
            }
            String string3 = getString(i, new Object[]{message});
            Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
            toast(string3);
        }
    }

    /* JADX INFO: renamed from: com.carlink.cast.MainActivity$onActivityResult$4, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.carlink.cast.MainActivity$onActivityResult$4", f = "MainActivity.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass4 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Uri $uri;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Uri uri, Continuation<? super AnonymousClass4> continuation) {
            super(2, continuation);
            this.$uri = uri;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass4 anonymousClass4 = MainActivity.this.new AnonymousClass4(this.$uri, continuation);
            anonymousClass4.L$0 = obj;
            return anonymousClass4;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass4) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            CoroutineScope $this$launch = (CoroutineScope) this.L$0;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Bitmap bitmap = PanelWallpaperStore.decodeUri$default(PanelWallpaperStore.INSTANCE, MainActivity.this, this.$uri, 0, 4, null);
                    BuildersKt__Builders_commonKt.launch$default($this$launch, Dispatchers.getMain(), null, new AnonymousClass1(bitmap, MainActivity.this, null), 2, null);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* JADX INFO: renamed from: com.carlink.cast.MainActivity$onActivityResult$4$1, reason: invalid class name */
        /* JADX INFO: compiled from: MainActivity.kt */
        @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.carlink.cast.MainActivity$onActivityResult$4$1", f = "MainActivity.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            final /* synthetic */ Bitmap $bitmap;
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Bitmap bitmap, MainActivity mainActivity, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.$bitmap = bitmap;
                this.this$0 = mainActivity;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.$bitmap, this.this$0, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) throws Throwable {
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        Bitmap bitmap = this.$bitmap;
                        MainActivity mainActivity = this.this$0;
                        if (bitmap != null) {
                            mainActivity.showWallpaperCropSheet(this.$bitmap);
                        } else {
                            String string = this.this$0.getString(R.string.toast_wallpaper_failed);
                            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
                            mainActivity.toast(string);
                        }
                        return Unit.INSTANCE;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
    }

    private final boolean ensurePermissions() {
        Iterable wanted = (List) new ArrayList();
        if (Build.VERSION.SDK_INT >= 31) {
            ((Collection) wanted).add("android.permission.BLUETOOTH_CONNECT");
            ((Collection) wanted).add("android.permission.BLUETOOTH_SCAN");
        }
        if (Build.VERSION.SDK_INT >= 33) {
            ((Collection) wanted).add("android.permission.NEARBY_WIFI_DEVICES");
            ((Collection) wanted).add("android.permission.POST_NOTIFICATIONS");
        }
        CastSettings castSettings = this.settings;
        CastSettings castSettings2 = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        if (castSettings.getAudio()) {
            CastSettings castSettings3 = this.settings;
            if (castSettings3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
            } else {
                castSettings2 = castSettings3;
            }
            if (castSettings2.getSourceMode() != SourceMode.CARLIFE) {
                ((Collection) wanted).add("android.permission.RECORD_AUDIO");
            }
        }
        Collection arrayList = new ArrayList();
        Iterator it = wanted.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            if (ContextCompat.checkSelfPermission(this, (String) next) != 0) {
                arrayList.add(next);
            }
        }
        List missing = (List) arrayList;
        if (missing.isEmpty()) {
            return true;
        }
        requestPermissions((String[]) missing.toArray(new String[0]), 21);
        return false;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean z;
        Intrinsics.checkNotNullParameter(permissions, "permissions");
        Intrinsics.checkNotNullParameter(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 21) {
            return;
        }
        int length = grantResults.length;
        int i = 0;
        while (true) {
            z = true;
            if (i >= length) {
                break;
            }
            if (!(grantResults[i] == 0)) {
                z = false;
                break;
            }
            i++;
        }
        if (z) {
            refreshPeers();
            if (this.pendingHonorCarConnect) {
                this.pendingHonorCarConnect = false;
                openHonorCarConnect();
                return;
            } else {
                if (this.pendingStartSession) {
                    this.pendingStartSession = false;
                    onPrimaryAction();
                    return;
                }
                return;
            }
        }
        this.pendingStartSession = false;
        this.pendingHonorCarConnect = false;
        String string = getString(R.string.toast_no_bt);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        toast(string);
    }

    private final void refreshPeers() {
        if (Build.VERSION.SDK_INT >= 31 && ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_CONNECT") != 0) {
            ensurePermissions();
            return;
        }
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BluetoothManager.class);
        CastSettings castSettings = null;
        BluetoothAdapter adapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
        Iterable bondedDevices = adapter != null ? adapter.getBondedDevices() : null;
        if (bondedDevices == null) {
            bondedDevices = SetsKt.emptySet();
        }
        Iterable<BluetoothDevice> iterable = bondedDevices;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (BluetoothDevice bluetoothDevice : iterable) {
            String address = bluetoothDevice.getAddress();
            String name = bluetoothDevice.getName();
            if (name == null) {
                name = getString(R.string.device_unnamed);
                Intrinsics.checkNotNullExpressionValue(name, "getString(...)");
            }
            arrayList.add(TuplesKt.to(address, name));
        }
        List bonded = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.carlink.cast.MainActivity$refreshPeers$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues((String) ((Pair) t).getSecond(), (String) ((Pair) t2).getSecond());
            }
        });
        this.peers.clear();
        CollectionsKt.addAll(this.peers, bonded);
        int i = 0;
        Iterator it = bonded.iterator();
        while (true) {
            if (it.hasNext()) {
                String str = (String) ((Pair) it.next()).getFirst();
                CastSettings castSettings2 = this.settings;
                if (castSettings2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settings");
                    castSettings2 = null;
                }
                if (StringsKt.equals(str, castSettings2.getPeerAddress(), true)) {
                    break;
                } else {
                    i++;
                }
            } else {
                i = -1;
                break;
            }
        }
        this.selectedPeerIndex = i;
        if (this.selectedPeerIndex >= 0) {
            CastSettings castSettings3 = this.settings;
            if (castSettings3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
            } else {
                castSettings = castSettings3;
            }
            castSettings.setPeerName((String) ((Pair) bonded.get(this.selectedPeerIndex)).getSecond());
        }
        updateDeviceLabel();
    }

    /* JADX WARN: Code duplicated, block: B:21:0x007c  */
    /* JADX WARN: Code duplicated, block: B:34:0x00a9  */
    private final void updateDeviceLabel() {
        String str;
        int i;
        if (this.deviceName == null) {
            return;
        }
        Pair peer = (Pair) CollectionsKt.getOrNull(this.peers, this.selectedPeerIndex);
        CastSettings castSettings = this.settings;
        CastSettings castSettings2 = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        String fallbackName = castSettings.getPeerName();
        TextView textView = this.deviceName;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("deviceName");
            textView = null;
        }
        if (peer != null) {
            str = getString(R.string.device_title) + " · " + peer.getSecond();
        } else if (fallbackName == null) {
            String string = getString(R.string.device_none);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            str = string;
        } else {
            CastSettings castSettings3 = this.settings;
            if (castSettings3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
                castSettings3 = null;
            }
            if (castSettings3.getPeerAddress() != null) {
                str = getString(R.string.device_title) + " · " + fallbackName;
            } else {
                String string2 = getString(R.string.device_none);
                Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
                str = string2;
            }
        }
        textView.setText(str);
        TextView textView2 = this.deviceName;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("deviceName");
            textView2 = null;
        }
        if (peer != null) {
            i = R.color.text_primary;
        } else {
            CastSettings castSettings4 = this.settings;
            if (castSettings4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
            } else {
                castSettings2 = castSettings4;
            }
            if (castSettings2.getPeerAddress() != null) {
                i = R.color.text_primary;
            } else {
                i = R.color.text_tertiary;
            }
        }
        textView2.setTextColor(color(i));
    }

    private final Pair<String, String> selectedPeer() {
        Pair<String, String> pair = (Pair) CollectionsKt.getOrNull(this.peers, this.selectedPeerIndex);
        if (pair != null) {
            return pair;
        }
        CastSettings castSettings = this.settings;
        CastSettings castSettings2 = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        String peerAddress = castSettings.getPeerAddress();
        if (peerAddress == null) {
            return null;
        }
        CastSettings castSettings3 = this.settings;
        if (castSettings3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
        } else {
            castSettings2 = castSettings3;
        }
        String peerName = castSettings2.getPeerName();
        if (peerName == null) {
            peerName = peerAddress;
        }
        return TuplesKt.to(peerAddress, peerName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showPeerSheet() {
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        final BottomSheetDialog sheet = pairSheetContent.component1();
        LinearLayout content = pairSheetContent.component2();
        String string = getString(R.string.device_pick_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        content.addView(sheetTitle(string));
        final LinearLayout listBox = new LinearLayout(this);
        listBox.setOrientation(1);
        listBox.setPadding(0, Ui.INSTANCE.dp(this, 6.0f), 0, 0);
        content.addView(listBox);
        showPeerSheet$renderList(listBox, this, sheet);
        String string2 = getString(R.string.device_refresh);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        MaterialButton materialButtonSecondaryButton = secondaryButton(string2);
        materialButtonSecondaryButton.setIconResource(R.drawable.ic_refresh);
        materialButtonSecondaryButton.setIconSize(Ui.INSTANCE.dp(this, 18.0f));
        materialButtonSecondaryButton.setIconPadding(Ui.INSTANCE.dp(this, 8.0f));
        materialButtonSecondaryButton.setIconGravity(2);
        materialButtonSecondaryButton.setIconTint(tint(R.color.text_secondary));
        materialButtonSecondaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda25
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showPeerSheet$lambda$88$lambda$87(this.f$0, listBox, sheet, view);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(this, 44.0f));
        layoutParams.topMargin = Ui.INSTANCE.dp(this, 14.0f);
        Unit unit = Unit.INSTANCE;
        content.addView(materialButtonSecondaryButton, layoutParams);
        sheet.show();
    }

    private static final void showPeerSheet$renderList(LinearLayout listBox, final MainActivity this$0, final BottomSheetDialog sheet) {
        listBox.removeAllViews();
        int i = 0;
        if (this$0.peers.isEmpty()) {
            TextView textView = new TextView(this$0);
            textView.setText(this$0.getString(R.string.device_hint));
            Ui.style$default(Ui.INSTANCE, textView, 12.0f, R.color.text_tertiary, false, false, 12, null);
            textView.setLineSpacing(Ui.INSTANCE.dp(this$0, 3.0f), 1.0f);
            textView.setPadding(0, Ui.INSTANCE.dp(this$0, 8.0f), 0, Ui.INSTANCE.dp(this$0, 8.0f));
            listBox.addView(textView);
            return;
        }
        Iterable iterable = this$0.peers;
        int i2 = 0;
        final int i3 = 0;
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            int i4 = i3 + 1;
            if (i3 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            final Pair pair = (Pair) next;
            LinearLayout linearLayout = new LinearLayout(this$0);
            linearLayout.setOrientation(i);
            linearLayout.setGravity(16);
            Iterable iterable2 = iterable;
            int i5 = i2;
            Iterator it2 = it;
            linearLayout.setPadding(Ui.INSTANCE.dp(this$0, 4.0f), Ui.INSTANCE.dp(this$0, 12.0f), Ui.INSTANCE.dp(this$0, 4.0f), Ui.INSTANCE.dp(this$0, 12.0f));
            linearLayout.setBackground(this$0.selectableBackground());
            linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.showPeerSheet$renderList$lambda$86$lambda$79$lambda$78(this.f$0, i3, pair, sheet, view);
                }
            });
            ImageView imageView = new ImageView(this$0);
            imageView.setImageResource(R.drawable.ic_car);
            imageView.setImageTintList(this$0.tint(R.color.accent));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this$0, 20.0f), Ui.INSTANCE.dp(this$0, 20.0f));
            layoutParams.setMarginEnd(Ui.INSTANCE.dp(this$0, 12.0f));
            Unit unit = Unit.INSTANCE;
            linearLayout.addView(imageView, layoutParams);
            LinearLayout linearLayout2 = new LinearLayout(this$0);
            linearLayout2.setOrientation(1);
            TextView textView2 = new TextView(this$0);
            textView2.setText((CharSequence) pair.getSecond());
            Ui.style$default(Ui.INSTANCE, textView2, 14.0f, R.color.text_primary, false, false, 12, null);
            textView2.setMaxLines(1);
            textView2.setEllipsize(TextUtils.TruncateAt.END);
            linearLayout2.addView(textView2);
            TextView textView3 = new TextView(this$0);
            textView3.setText((CharSequence) pair.getFirst());
            Ui.style$default(Ui.INSTANCE, textView3, 11.0f, R.color.text_tertiary, false, true, 4, null);
            textView3.setPadding(0, Ui.INSTANCE.dp(this$0, 2.0f), 0, 0);
            linearLayout2.addView(textView3);
            linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(0, -2, 1.0f));
            if (i3 == this$0.selectedPeerIndex) {
                ImageView imageView2 = new ImageView(this$0);
                imageView2.setImageResource(R.drawable.ic_check);
                imageView2.setImageTintList(this$0.tint(R.color.accent));
                linearLayout.addView(imageView2, new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this$0, 18.0f), Ui.INSTANCE.dp(this$0, 18.0f)));
            }
            listBox.addView(linearLayout, new LinearLayout.LayoutParams(-1, -2));
            i3 = i4;
            it = it2;
            iterable = iterable2;
            i2 = i5;
            i = 0;
        }
    }

    static final void showPeerSheet$renderList$lambda$86$lambda$79$lambda$78(MainActivity this$0, int $index, Pair $peer, BottomSheetDialog $sheet, View it) {
        this$0.selectedPeerIndex = $index;
        CastSettings castSettings = this$0.settings;
        CastSettings castSettings2 = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        castSettings.setPeerAddress((String) $peer.getFirst());
        CastSettings castSettings3 = this$0.settings;
        if (castSettings3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
        } else {
            castSettings2 = castSettings3;
        }
        castSettings2.setPeerName((String) $peer.getSecond());
        this$0.updateDeviceLabel();
        $sheet.dismiss();
    }

    static final void showPeerSheet$lambda$88$lambda$87(MainActivity this$0, LinearLayout $listBox, BottomSheetDialog $sheet, View it) {
        this$0.refreshPeers();
        showPeerSheet$renderList($listBox, this$0, $sheet);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void showSettings() {
        String string;
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        BottomSheetDialog bottomSheetDialogComponent1 = pairSheetContent.component1();
        LinearLayout linearLayoutComponent2 = pairSheetContent.component2();
        String string2 = getString(R.string.settings_title);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        linearLayoutComponent2.addView(sheetTitle(string2));
        linearLayoutComponent2.addView(buildSourceBlock());
        linearLayoutComponent2.addView(divider(2, 2));
        String string3 = getString(R.string.settings_audio);
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        String string4 = getString(R.string.settings_audio_detail);
        Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
        CastSettings castSettings = this.settings;
        String str = null;
        CastSettings castSettings2 = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        linearLayoutComponent2.addView(toggleRow(string3, string4, castSettings.getAudio(), new Function1() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.showSettings$lambda$90(this.f$0, ((Boolean) obj).booleanValue());
            }
        }));
        CastSettings castSettings3 = this.settings;
        if (castSettings3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings3 = null;
        }
        if (castSettings3.getSourceMode() != SourceMode.MIRROR) {
            String string5 = getString(R.string.settings_wallpaper);
            Intrinsics.checkNotNullExpressionValue(string5, "getString(...)");
            String string6 = getString(R.string.settings_wallpaper_detail, new Object[]{wallpaperStatus()});
            Intrinsics.checkNotNullExpressionValue(string6, "getString(...)");
            linearLayoutComponent2.addView(navRow$default(this, string5, string6, false, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.showSettings$lambda$91(this.f$0);
                }
            }, 4, null));
        }
        String string7 = getString(R.string.settings_touch);
        Intrinsics.checkNotNullExpressionValue(string7, "getString(...)");
        String string8 = isTouchServiceEnabled() ? getString(R.string.settings_touch_enabled) : getString(R.string.settings_touch_detail);
        Intrinsics.checkNotNull(string8);
        linearLayoutComponent2.addView(navRow$default(this, string7, string8, false, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$92(this.f$0);
            }
        }, 4, null));
        String string9 = getString(R.string.settings_media_access);
        Intrinsics.checkNotNullExpressionValue(string9, "getString(...)");
        String string10 = isMediaListenerEnabled() ? getString(R.string.settings_media_access_enabled) : getString(R.string.settings_media_access_detail);
        Intrinsics.checkNotNull(string10);
        linearLayoutComponent2.addView(navRow$default(this, string9, string10, false, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$93(this.f$0);
            }
        }, 4, null));
        String string11 = getString(R.string.settings_auto_connect);
        Intrinsics.checkNotNullExpressionValue(string11, "getString(...)");
        String string12 = getString(R.string.settings_auto_connect_detail);
        Intrinsics.checkNotNullExpressionValue(string12, "getString(...)");
        CastSettings castSettings4 = this.settings;
        if (castSettings4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings4 = null;
        }
        linearLayoutComponent2.addView(toggleRow(string11, string12, castSettings4.getAutoConnect(), new Function1() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.showSettings$lambda$94(this.f$0, ((Boolean) obj).booleanValue());
            }
        }));
        String string13 = getString(R.string.settings_carlife_auto_session);
        Intrinsics.checkNotNullExpressionValue(string13, "getString(...)");
        String string14 = getString(R.string.settings_carlife_auto_session_detail);
        Intrinsics.checkNotNullExpressionValue(string14, "getString(...)");
        CastSettings castSettings5 = this.settings;
        if (castSettings5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings5 = null;
        }
        linearLayoutComponent2.addView(toggleRow(string13, string14, castSettings5.getCarLifeAutoSession(), new Function1() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.showSettings$lambda$95(this.f$0, ((Boolean) obj).booleanValue());
            }
        }));
        String string15 = getString(R.string.settings_connect_guide);
        Intrinsics.checkNotNullExpressionValue(string15, "getString(...)");
        String string16 = getString(R.string.settings_connect_guide_detail);
        Intrinsics.checkNotNullExpressionValue(string16, "getString(...)");
        linearLayoutComponent2.addView(navRow$default(this, string15, string16, false, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$96(this.f$0);
            }
        }, 4, null));
        String string17 = getString(R.string.settings_frame_rate);
        Intrinsics.checkNotNullExpressionValue(string17, "getString(...)");
        int i = R.string.settings_frame_rate_detail;
        CastSettings castSettings6 = this.settings;
        if (castSettings6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings6 = null;
        }
        Integer numValueOf = Integer.valueOf(castSettings6.getFrameRateOverride());
        if ((numValueOf.intValue() > 0 ? 1 : 0) == 0) {
            numValueOf = null;
        }
        String string18 = getString(i, new Object[]{Integer.valueOf(numValueOf != null ? numValueOf.intValue() : 30)});
        Intrinsics.checkNotNullExpressionValue(string18, "getString(...)");
        linearLayoutComponent2.addView(navRow$default(this, string17, string18, false, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$98(this.f$0);
            }
        }, 4, null));
        String string19 = getString(R.string.settings_safe_area);
        Intrinsics.checkNotNullExpressionValue(string19, "getString(...)");
        String string20 = getString(R.string.settings_safe_area_detail);
        Intrinsics.checkNotNullExpressionValue(string20, "getString(...)");
        linearLayoutComponent2.addView(navRow$default(this, string19, string20, false, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$99(this.f$0);
            }
        }, 4, null));
        linearLayoutComponent2.addView(divider(6, 6));
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.settings_advanced));
        Ui.style$default(Ui.INSTANCE, textView, 10.5f, R.color.text_tertiary, true, false, 8, null);
        textView.setLetterSpacing(0.08f);
        textView.setPadding(0, 0, 0, Ui.INSTANCE.dp(this, 2.0f));
        linearLayoutComponent2.addView(textView);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BluetoothManager.class);
        BluetoothAdapter adapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
        if (adapter != null) {
            BluetoothAdapter bluetoothAdapter = adapter;
            LocalBluetoothAddress localBluetoothAddress = LocalBluetoothAddress.INSTANCE;
            MainActivity mainActivity = this;
            CastSettings castSettings7 = this.settings;
            if (castSettings7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
            } else {
                castSettings2 = castSettings7;
            }
            str = localBluetoothAddress.read(mainActivity, bluetoothAdapter, castSettings2.getManualBluetoothAddress());
        }
        String string21 = getString(R.string.settings_bluetooth);
        Intrinsics.checkNotNullExpressionValue(string21, "getString(...)");
        if (str == null || (string = getString(R.string.settings_bluetooth_current, new Object[]{str})) == null) {
            string = getString(R.string.settings_bluetooth_detail);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        }
        linearLayoutComponent2.addView(navRow(string21, string, true, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$103(this.f$0);
            }
        }));
        String string22 = getString(R.string.settings_clear_pair);
        Intrinsics.checkNotNullExpressionValue(string22, "getString(...)");
        String string23 = getString(R.string.settings_clear_pair_detail);
        Intrinsics.checkNotNullExpressionValue(string23, "getString(...)");
        linearLayoutComponent2.addView(navRow(string22, string23, true, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$104(this.f$0);
            }
        }));
        String string24 = getString(R.string.settings_export);
        Intrinsics.checkNotNullExpressionValue(string24, "getString(...)");
        String string25 = getString(R.string.settings_export_detail);
        Intrinsics.checkNotNullExpressionValue(string25, "getString(...)");
        linearLayoutComponent2.addView(navRow(string24, string25, true, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$105(this.f$0);
            }
        }));
        TextView textView2 = new TextView(this);
        textView2.setText(getString(R.string.settings_version, new Object[]{BuildConfig.VERSION_NAME}));
        Ui.style$default(Ui.INSTANCE, textView2, 10.5f, R.color.text_tertiary, false, false, 12, null);
        textView2.setPadding(0, Ui.INSTANCE.dp(this, 18.0f), 0, 0);
        linearLayoutComponent2.addView(textView2);
        bottomSheetDialogComponent1.show();
    }

    static final Unit showSettings$lambda$90(MainActivity this$0, boolean it) {
        CastSettings castSettings = this$0.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        castSettings.setAudio(it);
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$91(MainActivity this$0) {
        showWallpaperSheet$default(this$0, null, 1, null);
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$92(MainActivity this$0) {
        this$0.openAccessibility();
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$93(MainActivity this$0) {
        this$0.openMediaListenerSettings();
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$94(MainActivity this$0, boolean it) {
        CastSettings castSettings = this$0.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        castSettings.setAutoConnect(it);
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$95(MainActivity this$0, boolean it) {
        CastSettings castSettings = this$0.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        castSettings.setCarLifeAutoSession(it);
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$96(MainActivity this$0) {
        this$0.openHonorCarConnect();
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$98(MainActivity this$0) {
        this$0.showFrameRateSheet();
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$99(MainActivity this$0) {
        this$0.showSafeAreaSheet();
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$103(MainActivity this$0) {
        this$0.editBluetoothAddress();
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$104(MainActivity this$0) {
        Pair<String, String> pairSelectedPeer = this$0.selectedPeer();
        if (pairSelectedPeer == null) {
            String string = this$0.getString(R.string.toast_no_peer);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            this$0.toast(string);
        } else {
            CastSettings castSettings = this$0.settings;
            CastSettings castSettings2 = null;
            if (castSettings == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
                castSettings = null;
            }
            castSettings.removePair(pairSelectedPeer.getFirst());
            CastSettings castSettings3 = this$0.settings;
            if (castSettings3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
            } else {
                castSettings2 = castSettings3;
            }
            castSettings2.clearConnected(pairSelectedPeer.getFirst());
            String string2 = this$0.getString(R.string.toast_pair_cleared);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            this$0.toast(string2);
        }
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$105(MainActivity this$0) {
        this$0.exportLog();
        return Unit.INSTANCE;
    }

    private final View buildSourceBlock() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(1);
        float f = 12.0f;
        box.setPadding(0, Ui.INSTANCE.dp(this, 10.0f), 0, Ui.INSTANCE.dp(this, 12.0f));
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.source_title));
        Ui.style$default(Ui.INSTANCE, textView, 14.0f, R.color.text_primary, false, false, 12, null);
        box.addView(textView);
        final TextView textView2 = new TextView(this);
        CastSettings castSettings = this.settings;
        AttributeSet attributeSet = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        textView2.setText(sourceDetail(castSettings.getSourceMode()));
        Ui.style$default(Ui.INSTANCE, textView2, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView2.setLineSpacing(Ui.INSTANCE.dp(this, 2.0f), 1.0f);
        final List buttons = new ArrayList();
        LinearLayout group = new LinearLayout(this);
        group.setOrientation(0);
        int i = 0;
        for (Object obj : SourceMode.getEntries()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            final SourceMode sourceMode = (SourceMode) obj;
            MaterialButton materialButton = new MaterialButton(this, attributeSet, com.google.android.material.R.attr.materialButtonStyle);
            materialButton.setText(shortSourceLabel(sourceMode));
            materialButton.setTextSize(f);
            materialButton.setAllCaps(false);
            materialButton.setCornerRadius(Ui.INSTANCE.dp(this, 20.0f));
            materialButton.setInsetTop(0);
            materialButton.setInsetBottom(0);
            materialButton.setMinimumHeight(0);
            materialButton.setStateListAnimator(null);
            materialButton.setPadding(Ui.INSTANCE.dp(this, 4.0f), 0, Ui.INSTANCE.dp(this, 4.0f), 0);
            materialButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda23
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.buildSourceBlock$lambda$114$lambda$113$lambda$112(this.f$0, sourceMode, textView2, buttons, view);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 40.0f), 1.0f);
            if (i > 0) {
                layoutParams.setMarginStart(Ui.INSTANCE.dp(this, 8.0f));
            }
            group.addView(materialButton, layoutParams);
            buttons.add(TuplesKt.to(sourceMode, materialButton));
            i = i2;
            attributeSet = null;
            f = 12.0f;
        }
        buildSourceBlock$paint(buttons, this);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(this, 40.0f));
        layoutParams2.topMargin = Ui.INSTANCE.dp(this, 10.0f);
        Unit unit = Unit.INSTANCE;
        box.addView(group, layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams3.topMargin = Ui.INSTANCE.dp(this, 8.0f);
        Unit unit2 = Unit.INSTANCE;
        box.addView(textView2, layoutParams3);
        return box;
    }

    private static final void buildSourceBlock$paint(List<Pair<SourceMode, MaterialButton>> list, MainActivity this$0) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            SourceMode sourceMode = (SourceMode) pair.component1();
            MaterialButton materialButton = (MaterialButton) pair.component2();
            CastSettings castSettings = this$0.settings;
            if (castSettings == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
                castSettings = null;
            }
            boolean z = sourceMode == castSettings.getSourceMode();
            materialButton.setBackgroundTintList(this$0.tint(z ? R.color.accent_soft : R.color.surface_high));
            materialButton.setTextColor(this$0.color(z ? R.color.accent : R.color.text_secondary));
            materialButton.setStrokeWidth(z ? Ui.INSTANCE.dp(this$0, 1.0f) : 0);
            if (z) {
                materialButton.setStrokeColor(this$0.tint(R.color.accent));
            }
        }
    }

    static final void buildSourceBlock$lambda$114$lambda$113$lambda$112(MainActivity this$0, SourceMode $mode, TextView $detail, List $buttons, View it) {
        CastSettings castSettings = this$0.settings;
        TextView textView = null;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        if (castSettings.getSourceMode() == $mode) {
            return;
        }
        CastSettings castSettings2 = this$0.settings;
        if (castSettings2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings2 = null;
        }
        castSettings2.setSourceMode($mode);
        $detail.setText(this$0.sourceDetail($mode));
        TextView textView2 = this$0.sourceBadge;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sourceBadge");
        } else {
            textView = textView2;
        }
        textView.setText(this$0.shortSourceLabel($mode));
        buildSourceBlock$paint($buttons, this$0);
        if (CastLog.INSTANCE.getState().getValue().getRunning()) {
            String string = this$0.getString(R.string.toast_source_next);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            this$0.toast(string);
        }
    }

    private final void editBluetoothAddress() {
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        final BottomSheetDialog sheet = pairSheetContent.component1();
        LinearLayout content = pairSheetContent.component2();
        String string = getString(R.string.dialog_bt_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        content.addView(sheetTitle(string));
        final TextInputLayout inputLayout = new TextInputLayout(this);
        inputLayout.setBoxBackgroundMode(2);
        inputLayout.setBoxBackgroundColor(color(R.color.surface));
        inputLayout.setBoxStrokeColorStateList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_focused}, new int[0]}, new int[]{color(R.color.accent), color(R.color.stroke)}));
        inputLayout.setBoxStrokeWidth(Ui.INSTANCE.dp(this, 1.0f));
        inputLayout.setBoxStrokeWidthFocused(Ui.INSTANCE.dp(this, 1.0f));
        inputLayout.setBoxCornerRadii(Ui.INSTANCE.dp(this, 14.0f), Ui.INSTANCE.dp(this, 14.0f), Ui.INSTANCE.dp(this, 14.0f), Ui.INSTANCE.dp(this, 14.0f));
        inputLayout.setHint(getString(R.string.dialog_bt_title));
        inputLayout.setHelperText(getString(R.string.dialog_bt_help));
        inputLayout.setHelperTextColor(tint(R.color.text_tertiary));
        final TextInputEditText textInputEditText = new TextInputEditText(inputLayout.getContext());
        CastSettings castSettings = this.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        String manualBluetoothAddress = castSettings.getManualBluetoothAddress();
        if (manualBluetoothAddress == null) {
            manualBluetoothAddress = "";
        }
        textInputEditText.setText(manualBluetoothAddress);
        textInputEditText.setInputType(528385);
        textInputEditText.setSingleLine();
        textInputEditText.setTextSize(15.0f);
        textInputEditText.setTextColor(color(R.color.text_primary));
        textInputEditText.setHintTextColor(tint(R.color.text_tertiary));
        inputLayout.addView(textInputEditText, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = Ui.INSTANCE.dp(this, 12.0f);
        Unit unit = Unit.INSTANCE;
        content.addView(inputLayout, layoutParams);
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(0);
        actions.setGravity(16);
        String string2 = getString(R.string.dialog_clear);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        MaterialButton materialButtonSecondaryButton = secondaryButton(string2);
        materialButtonSecondaryButton.setTextSize(12.0f);
        materialButtonSecondaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda34
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.editBluetoothAddress$lambda$122$lambda$121(this.f$0, sheet, view);
            }
        });
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f);
        layoutParams2.setMarginEnd(Ui.INSTANCE.dp(this, 8.0f));
        Unit unit2 = Unit.INSTANCE;
        actions.addView(materialButtonSecondaryButton, layoutParams2);
        String string3 = getString(R.string.dialog_cancel);
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        MaterialButton materialButtonSecondaryButton2 = secondaryButton(string3);
        materialButtonSecondaryButton2.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda35
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                sheet.dismiss();
            }
        });
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, Ui.INSTANCE.dp(this, 44.0f));
        layoutParams3.setMarginEnd(Ui.INSTANCE.dp(this, 8.0f));
        Unit unit3 = Unit.INSTANCE;
        actions.addView(materialButtonSecondaryButton2, layoutParams3);
        String string4 = getString(R.string.dialog_save);
        Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
        MaterialButton materialButtonPrimaryButton = primaryButton(string4);
        materialButtonPrimaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda36
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.editBluetoothAddress$save(textInputEditText, this, sheet, inputLayout);
            }
        });
        actions.addView(materialButtonPrimaryButton, new LinearLayout.LayoutParams(-2, Ui.INSTANCE.dp(this, 44.0f)));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams4.topMargin = Ui.INSTANCE.dp(this, 18.0f);
        Unit unit4 = Unit.INSTANCE;
        content.addView(actions, layoutParams4);
        sheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void editBluetoothAddress$save(TextInputEditText input, MainActivity this$0, BottomSheetDialog sheet, TextInputLayout inputLayout) {
        String string;
        Editable text = input.getText();
        CastSettings castSettings = null;
        String value = (text == null || (string = text.toString()) == null) ? null : StringsKt.trim((CharSequence) string).toString();
        if (value == null) {
            value = "";
        }
        if (StringsKt.isBlank(value)) {
            CastSettings castSettings2 = this$0.settings;
            if (castSettings2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settings");
                castSettings2 = null;
            }
            castSettings2.setManualBluetoothAddress(null);
            String string2 = this$0.getString(R.string.toast_bt_cleared);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            this$0.toast(string2);
            sheet.dismiss();
            return;
        }
        if (LocalBluetoothAddress.INSTANCE.normalize(value) == null) {
            inputLayout.setError(this$0.getString(R.string.dialog_bt_invalid));
            return;
        }
        CastSettings castSettings3 = this$0.settings;
        if (castSettings3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
        } else {
            castSettings = castSettings3;
        }
        castSettings.setManualBluetoothAddress(value);
        String string3 = this$0.getString(R.string.toast_bt_saved);
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        this$0.toast(string3);
        sheet.dismiss();
    }

    static final void editBluetoothAddress$lambda$122$lambda$121(MainActivity this$0, BottomSheetDialog $sheet, View it) {
        CastSettings castSettings = this$0.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        castSettings.setManualBluetoothAddress(null);
        String string = this$0.getString(R.string.toast_bt_cleared);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        this$0.toast(string);
        $sheet.dismiss();
    }

    private final void showProtocol() {
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        BottomSheetDialog sheet = pairSheetContent.component1();
        LinearLayout content = pairSheetContent.component2();
        String string = getString(R.string.protocol_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        content.addView(sheetTitle(string));
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.protocol_subtitle));
        Ui.style$default(Ui.INSTANCE, textView, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView.setPadding(0, Ui.INSTANCE.dp(this, 2.0f), 0, Ui.INSTANCE.dp(this, 4.0f));
        content.addView(textView);
        for (ProtocolSection protocolSection : ProtocolContent.INSTANCE.getSections()) {
            TextView textView2 = new TextView(this);
            textView2.setText(protocolSection.getTitle());
            Ui.style$default(Ui.INSTANCE, textView2, 13.5f, R.color.accent, true, false, 8, null);
            textView2.setPadding(0, Ui.INSTANCE.dp(this, 16.0f), 0, Ui.INSTANCE.dp(this, 4.0f));
            content.addView(textView2);
            TextView textView3 = new TextView(this);
            textView3.setText(protocolSection.getBody());
            Ui.style$default(Ui.INSTANCE, textView3, 12.5f, R.color.text_secondary, false, false, 12, null);
            textView3.setLineSpacing(Ui.INSTANCE.dp(this, 4.0f), 1.0f);
            content.addView(textView3);
        }
        sheet.show();
    }

    private final Pair<BottomSheetDialog, LinearLayout> sheetContent() {
        final BottomSheetDialog sheet = new BottomSheetDialog(this);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(1);
        content.setPadding(Ui.INSTANCE.dp(this, 24.0f), Ui.INSTANCE.dp(this, 6.0f), Ui.INSTANCE.dp(this, 24.0f), Ui.INSTANCE.dp(this, 28.0f));
        NestedScrollView nestedScrollView = new NestedScrollView(this);
        nestedScrollView.setFillViewport(false);
        nestedScrollView.setVerticalScrollBarEnabled(false);
        nestedScrollView.addView(content, new ViewGroup.LayoutParams(-1, -2));
        sheet.setContentView(nestedScrollView);
        sheet.getBehavior().setMaxHeight((int) (getResources().getDisplayMetrics().heightPixels * 0.9f));
        sheet.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda7
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                MainActivity.sheetContent$lambda$136(sheet, dialogInterface);
            }
        });
        content.addView(dragHandle());
        return TuplesKt.to(sheet, content);
    }

    static final void sheetContent$lambda$136(BottomSheetDialog $sheet, DialogInterface it) {
        $sheet.getBehavior().setSkipCollapsed(true);
        $sheet.getBehavior().setState(3);
    }

    private final View dragHandle() {
        LinearLayout wrap = new LinearLayout(this);
        wrap.setOrientation(0);
        wrap.setGravity(17);
        wrap.setPadding(0, Ui.INSTANCE.dp(this, 4.0f), 0, Ui.INSTANCE.dp(this, 16.0f));
        View view = new View(this);
        view.setBackground(Ui.INSTANCE.pill(this, R.color.track));
        wrap.addView(view, new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this, 36.0f), Ui.INSTANCE.dp(this, 4.0f)));
        return wrap;
    }

    private final TextView sheetTitle(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        Ui.style$default(Ui.INSTANCE, textView, 20.0f, R.color.text_primary, true, false, 8, null);
        textView.setPadding(0, 0, 0, Ui.INSTANCE.dp(this, 4.0f));
        return textView;
    }

    private final void showFrameRateSheet() {
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        final BottomSheetDialog sheet = pairSheetContent.component1();
        LinearLayout content = pairSheetContent.component2();
        String string = getString(R.string.settings_frame_rate);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        content.addView(sheetTitle(string));
        Iterable<Pair> options = CollectionsKt.listOf((Object[]) new Pair[]{TuplesKt.to(0, getString(R.string.frame_rate_auto)), TuplesKt.to(30, getString(R.string.frame_rate_30)), TuplesKt.to(60, getString(R.string.frame_rate_60))});
        CastSettings castSettings = this.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        int current = castSettings.getFrameRateOverride();
        for (Pair pair : options) {
            final int iIntValue = ((Number) pair.component1()).intValue();
            Object objComponent2 = pair.component2();
            Intrinsics.checkNotNullExpressionValue(objComponent2, "component2(...)");
            String str = (String) objComponent2;
            String string2 = iIntValue == current ? getString(R.string.frame_rate_selected) : "";
            Intrinsics.checkNotNull(string2);
            content.addView(navRow$default(this, str, string2, false, new Function0() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda24
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.showFrameRateSheet$lambda$141$lambda$140(this.f$0, iIntValue, sheet);
                }
            }, 4, null));
        }
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.frame_rate_note));
        Ui.style$default(Ui.INSTANCE, textView, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView.setPadding(Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 10.0f), Ui.INSTANCE.dp(this, 4.0f), 0);
        textView.setLineSpacing(Ui.INSTANCE.dp(this, 3.0f), 1.0f);
        content.addView(textView);
        sheet.show();
    }

    static final Unit showFrameRateSheet$lambda$141$lambda$140(MainActivity this$0, int $value, BottomSheetDialog $sheet) {
        CastSettings castSettings = this$0.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        castSettings.setFrameRateOverride($value);
        String string = this$0.getString(R.string.toast_source_next);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        this$0.toast(string);
        $sheet.dismiss();
        return Unit.INSTANCE;
    }

    private final void showSafeAreaSheet() {
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        final BottomSheetDialog bottomSheetDialogComponent1 = pairSheetContent.component1();
        LinearLayout linearLayoutComponent2 = pairSheetContent.component2();
        String string = getString(R.string.safe_area_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        linearLayoutComponent2.addView(sheetTitle(string));
        CastSettings castSettings = this.settings;
        if (castSettings == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        }
        boolean z = true;
        CarPanelGeometry.SafeAreaPolicy safeAreaPolicySafeAreaPolicy$default = CastSettings.safeAreaPolicy$default(castSettings, null, 1, null);
        final MaterialSwitch materialSwitch = new MaterialSwitch(this);
        materialSwitch.setText(getString(R.string.safe_area_enabled));
        materialSwitch.setChecked(safeAreaPolicySafeAreaPolicy$default.getEnabled());
        materialSwitch.setTextColor(color(R.color.text_primary));
        linearLayoutComponent2.addView(materialSwitch, new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(this, 48.0f)));
        int i = 2;
        char c = 3;
        List listListOf = CollectionsKt.listOf((Object[]) new Pair[]{TuplesKt.to(CarPanelGeometry.SafeAreaMode.STRICT, getString(R.string.safe_area_strict)), TuplesKt.to(CarPanelGeometry.SafeAreaMode.RELAXED, getString(R.string.safe_area_relaxed)), TuplesKt.to(CarPanelGeometry.SafeAreaMode.CUSTOM, getString(R.string.safe_area_custom)), TuplesKt.to(CarPanelGeometry.SafeAreaMode.OFF, getString(R.string.safe_area_off))});
        final RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(1);
        int i2 = 0;
        for (Object obj : listListOf) {
            int i3 = i2 + 1;
            if (i2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Pair pair = (Pair) obj;
            char c2 = c;
            CarPanelGeometry.SafeAreaMode safeAreaMode = (CarPanelGeometry.SafeAreaMode) pair.component1();
            Object objComponent2 = pair.component2();
            boolean z2 = z;
            Intrinsics.checkNotNullExpressionValue(objComponent2, "component2(...)");
            String str = (String) objComponent2;
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i2 + 54000);
            radioButton.setTag(safeAreaMode);
            radioButton.setText(str);
            radioButton.setTextColor(color(R.color.text_primary));
            radioButton.setButtonTintList(tint(R.color.accent));
            radioGroup.addView(radioButton);
            i2 = i3;
            z = z2;
            c = c2;
            i = i;
        }
        boolean z3 = z;
        int i4 = i;
        char c3 = c;
        linearLayoutComponent2.addView(radioGroup);
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.safe_area_margin_note));
        Ui.style$default(Ui.INSTANCE, textView, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView.setLineSpacing(Ui.INSTANCE.dp(this, 2.0f), 1.0f);
        textView.setPadding(Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 6.0f), Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 6.0f));
        linearLayoutComponent2.addView(textView);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        String[] strArr = new String[4];
        strArr[0] = "左";
        strArr[z3 ? 1 : 0] = "上";
        strArr[i4] = "右";
        strArr[c3] = "下";
        List listListOf2 = CollectionsKt.listOf((Object[]) strArr);
        int i5 = 0;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(listListOf2, 10));
        List<String> list = listListOf2;
        int i6 = 0;
        for (String str2 : list) {
            List list2 = listListOf2;
            EditText editText = new EditText(this);
            editText.setHint(str2);
            editText.setInputType(i4);
            editText.setSingleLine(z3);
            editText.setTextColor(color(R.color.text_primary));
            editText.setHintTextColor(tint(R.color.text_tertiary));
            editText.setBackgroundTintList(tint(R.color.accent));
            linearLayout.addView(editText, new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 52.0f), 1.0f));
            arrayList.add(editText);
            listListOf2 = list2;
            i5 = i5;
            safeAreaPolicySafeAreaPolicy$default = safeAreaPolicySafeAreaPolicy$default;
            listListOf = listListOf;
            list = list;
            i6 = i6;
            z3 = true;
            i4 = 2;
        }
        CarPanelGeometry.SafeAreaPolicy safeAreaPolicy = safeAreaPolicySafeAreaPolicy$default;
        List list3 = listListOf;
        final ArrayList arrayList2 = arrayList;
        Integer[] numArr = new Integer[4];
        numArr[0] = Integer.valueOf(safeAreaPolicy.getLeft());
        numArr[1] = Integer.valueOf(safeAreaPolicy.getTop());
        numArr[2] = Integer.valueOf(safeAreaPolicy.getRight());
        numArr[c3] = Integer.valueOf(safeAreaPolicy.getBottom());
        List listListOf3 = CollectionsKt.listOf((Object[]) numArr);
        int i7 = 0;
        for (Object obj2 : listListOf3) {
            int i8 = i7 + 1;
            if (i7 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            ((EditText) arrayList2.get(i7)).setText(String.valueOf(((Number) obj2).intValue()));
            i7 = i8;
            listListOf3 = listListOf3;
        }
        linearLayoutComponent2.addView(linearLayout);
        int i9 = 0;
        Iterator it = list3.iterator();
        while (it.hasNext()) {
            if (((Pair) it.next()).getFirst() == safeAreaPolicy.getMode()) {
                radioGroup.check(RangesKt.coerceAtLeast(i9, 0) + 54000);
                LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setOrientation(0);
                linearLayout2.setGravity(16);
                String string2 = getString(R.string.dialog_cancel);
                Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
                MaterialButton materialButtonSecondaryButton = secondaryButton(string2);
                materialButtonSecondaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda21
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        bottomSheetDialogComponent1.dismiss();
                    }
                });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f);
                layoutParams.setMarginEnd(Ui.INSTANCE.dp(this, 8.0f));
                Unit unit = Unit.INSTANCE;
                linearLayout2.addView(materialButtonSecondaryButton, layoutParams);
                String string3 = getString(R.string.dialog_save);
                Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
                MaterialButton materialButtonPrimaryButton = primaryButton(string3);
                materialButtonPrimaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda22
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        MainActivity.showSafeAreaSheet$lambda$159$lambda$158(radioGroup, arrayList2, this, materialSwitch, bottomSheetDialogComponent1, view);
                    }
                });
                linearLayout2.addView(materialButtonPrimaryButton, new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f));
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.topMargin = Ui.INSTANCE.dp(this, 16.0f);
                Unit unit2 = Unit.INSTANCE;
                linearLayoutComponent2.addView(linearLayout2, layoutParams2);
                bottomSheetDialogComponent1.show();
            }
            i9++;
        }
        i9 = -1;
        radioGroup.check(RangesKt.coerceAtLeast(i9, 0) + 54000);
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        String string4 = getString(R.string.dialog_cancel);
        Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
        MaterialButton materialButtonSecondaryButton2 = secondaryButton(string4);
        materialButtonSecondaryButton2.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda21
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                bottomSheetDialogComponent1.dismiss();
            }
        });
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f);
        layoutParams3.setMarginEnd(Ui.INSTANCE.dp(this, 8.0f));
        Unit unit3 = Unit.INSTANCE;
        linearLayout3.addView(materialButtonSecondaryButton2, layoutParams3);
        String string5 = getString(R.string.dialog_save);
        Intrinsics.checkNotNullExpressionValue(string5, "getString(...)");
        MaterialButton materialButtonPrimaryButton2 = primaryButton(string5);
        materialButtonPrimaryButton2.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda22
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showSafeAreaSheet$lambda$159$lambda$158(radioGroup, arrayList2, this, materialSwitch, bottomSheetDialogComponent1, view);
            }
        });
        linearLayout3.addView(materialButtonPrimaryButton2, new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams4.topMargin = Ui.INSTANCE.dp(this, 16.0f);
        Unit unit4 = Unit.INSTANCE;
        linearLayoutComponent2.addView(linearLayout3, layoutParams4);
        bottomSheetDialogComponent1.show();
    }

    static final void showSafeAreaSheet$lambda$159$lambda$158(RadioGroup $modes, List $inputs, MainActivity this$0, MaterialSwitch $enabled, BottomSheetDialog $sheet, View it) {
        CastSettings castSettings;
        RadioButton radioButton = (RadioButton) $modes.findViewById($modes.getCheckedRadioButtonId());
        Object tag = radioButton != null ? radioButton.getTag() : null;
        CarPanelGeometry.SafeAreaMode safeAreaMode = tag instanceof CarPanelGeometry.SafeAreaMode ? (CarPanelGeometry.SafeAreaMode) tag : null;
        if (safeAreaMode == null) {
            safeAreaMode = CarPanelGeometry.SafeAreaMode.STRICT;
        }
        CarPanelGeometry.SafeAreaMode mode = safeAreaMode;
        List list = $inputs;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it2 = list.iterator();
        while (true) {
            int iCoerceAtLeast = 0;
            if (!it2.hasNext()) {
                break;
            }
            Integer intOrNull = StringsKt.toIntOrNull(((EditText) it2.next()).getText().toString());
            if (intOrNull != null) {
                iCoerceAtLeast = RangesKt.coerceAtLeast(intOrNull.intValue(), 0);
            }
            arrayList.add(Integer.valueOf(iCoerceAtLeast));
        }
        List values = (List) arrayList;
        if (mode == CarPanelGeometry.SafeAreaMode.CUSTOM && (((Number) values.get(0)).intValue() + ((Number) values.get(2)).intValue() > CarPanelGeometry.PANEL_WIDTH - 320 || ((Number) values.get(1)).intValue() + ((Number) values.get(3)).intValue() > CarPanelGeometry.PANEL_HEIGHT - 160)) {
            this$0.toast("边距过大，请至少保留 320×160");
            return;
        }
        CastSettings castSettings2 = this$0.settings;
        if (castSettings2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settings");
            castSettings = null;
        } else {
            castSettings = castSettings2;
        }
        CastSettings.saveSafeAreaPolicy$default(castSettings, null, new CarPanelGeometry.SafeAreaPolicy($enabled.isChecked(), mode, ((Number) values.get(0)).intValue(), ((Number) values.get(1)).intValue(), ((Number) values.get(2)).intValue(), ((Number) values.get(3)).intValue()), 1, null);
        String string = this$0.getString(R.string.toast_source_next);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        this$0.toast(string);
        $sheet.dismiss();
    }

    private final String wallpaperStatus() {
        if (PanelWallpaperStore.INSTANCE.hasCustomWallpaper(this)) {
            String string = getString(R.string.wallpaper_custom);
            Intrinsics.checkNotNull(string);
            return string;
        }
        String string2 = getString(R.string.wallpaper_default);
        Intrinsics.checkNotNull(string2);
        return string2;
    }

    static /* synthetic */ void showWallpaperSheet$default(MainActivity mainActivity, Bitmap bitmap, int i, Object obj) {
        if ((i & 1) != 0) {
            bitmap = null;
        }
        mainActivity.showWallpaperSheet(bitmap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showWallpaperSheet(Bitmap initialPreview) {
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        final BottomSheetDialog sheet = pairSheetContent.component1();
        LinearLayout content = pairSheetContent.component2();
        String string = getString(R.string.settings_wallpaper);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        content.addView(sheetTitle(string));
        final ImageView preview = new ImageView(this);
        preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        preview.setBackgroundColor(color(R.color.surface_high));
        preview.setContentDescription(getString(R.string.wallpaper_preview));
        int previewWidth = RangesKt.coerceAtLeast(getResources().getDisplayMetrics().widthPixels - (Ui.INSTANCE.dp(this, 24.0f) * 2), 1);
        int previewHeight = WallpaperCropGeometry.INSTANCE.viewportHeight(previewWidth, CarPanelGeometry.PANEL_WIDTH, CarPanelGeometry.PANEL_HEIGHT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, previewHeight);
        layoutParams.topMargin = Ui.INSTANCE.dp(this, 12.0f);
        Unit unit = Unit.INSTANCE;
        content.addView(preview, layoutParams);
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.wallpaper_alpha_note));
        Ui.style$default(Ui.INSTANCE, textView, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView.setLineSpacing(Ui.INSTANCE.dp(this, 2.0f), 1.0f);
        textView.setPadding(Ui.INSTANCE.dp(this, 2.0f), Ui.INSTANCE.dp(this, 8.0f), Ui.INSTANCE.dp(this, 2.0f), 0);
        content.addView(textView);
        if (initialPreview == null) {
            BuildersKt__Builders_commonKt.launch$default(this.scope, Dispatchers.getIO(), null, new AnonymousClass3(preview, null), 2, null);
        } else {
            showWallpaperSheet$updatePreview(preview, this, initialPreview);
        }
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(0);
        actions.setGravity(16);
        String string2 = getString(R.string.wallpaper_reset);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        MaterialButton materialButtonSecondaryButton = secondaryButton(string2);
        materialButtonSecondaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showWallpaperSheet$lambda$166$lambda$165(this.f$0, preview, view);
            }
        });
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f);
        layoutParams2.setMarginEnd(Ui.INSTANCE.dp(this, 8.0f));
        Unit unit2 = Unit.INSTANCE;
        actions.addView(materialButtonSecondaryButton, layoutParams2);
        String string3 = getString(R.string.wallpaper_choose);
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        MaterialButton materialButtonPrimaryButton = primaryButton(string3);
        materialButtonPrimaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showWallpaperSheet$lambda$169$lambda$168(sheet, this, view);
            }
        });
        actions.addView(materialButtonPrimaryButton, new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f));
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams3.topMargin = Ui.INSTANCE.dp(this, 16.0f);
        Unit unit3 = Unit.INSTANCE;
        content.addView(actions, layoutParams3);
        sheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showWallpaperSheet$updatePreview(ImageView preview, MainActivity this$0, Bitmap bitmap) {
        preview.setImageBitmap(bitmap);
        preview.setBackgroundColor(bitmap == null ? 0 : this$0.color(R.color.surface_high));
    }

    /* JADX INFO: renamed from: com.carlink.cast.MainActivity$showWallpaperSheet$3, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.carlink.cast.MainActivity$showWallpaperSheet$3", f = "MainActivity.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ ImageView $preview;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(ImageView imageView, Continuation<? super AnonymousClass3> continuation) {
            super(2, continuation);
            this.$preview = imageView;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass3 anonymousClass3 = MainActivity.this.new AnonymousClass3(this.$preview, continuation);
            anonymousClass3.L$0 = obj;
            return anonymousClass3;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            CoroutineScope $this$launch = (CoroutineScope) this.L$0;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Bitmap bitmap = PanelWallpaperStore.INSTANCE.decode(MainActivity.this, 1280);
                    BuildersKt__Builders_commonKt.launch$default($this$launch, Dispatchers.getMain(), null, new AnonymousClass1(MainActivity.this, bitmap, this.$preview, null), 2, null);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* JADX INFO: renamed from: com.carlink.cast.MainActivity$showWallpaperSheet$3$1, reason: invalid class name */
        /* JADX INFO: compiled from: MainActivity.kt */
        @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.carlink.cast.MainActivity$showWallpaperSheet$3$1", f = "MainActivity.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            final /* synthetic */ Bitmap $bitmap;
            final /* synthetic */ ImageView $preview;
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(MainActivity mainActivity, Bitmap bitmap, ImageView imageView, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = mainActivity;
                this.$bitmap = bitmap;
                this.$preview = imageView;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, this.$bitmap, this.$preview, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) throws Throwable {
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        if (!this.this$0.isFinishing() && !this.this$0.isDestroyed()) {
                            MainActivity.showWallpaperSheet$updatePreview(this.$preview, this.this$0, this.$bitmap);
                        }
                        return Unit.INSTANCE;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
    }

    static final void showWallpaperSheet$lambda$166$lambda$165(MainActivity this$0, ImageView $preview, View it) {
        if (PanelWallpaperStore.INSTANCE.reset(this$0)) {
            showWallpaperSheet$updatePreview($preview, this$0, null);
            String string = this$0.getString(R.string.toast_wallpaper_reset);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            this$0.toast(string);
            return;
        }
        String string2 = this$0.getString(R.string.wallpaper_default);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        this$0.toast(string2);
    }

    static final void showWallpaperSheet$lambda$169$lambda$168(BottomSheetDialog $sheet, MainActivity this$0, View it) {
        $sheet.dismiss();
        this$0.launchWallpaperPicker();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showWallpaperCropSheet(final Bitmap bitmap) {
        Pair<BottomSheetDialog, LinearLayout> pairSheetContent = sheetContent();
        final BottomSheetDialog sheet = pairSheetContent.component1();
        LinearLayout content = pairSheetContent.component2();
        String string = getString(R.string.wallpaper_crop_title);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        content.addView(sheetTitle(string));
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.wallpaper_crop_ratio));
        Ui.style$default(Ui.INSTANCE, textView, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView.setPadding(0, Ui.INSTANCE.dp(this, 2.0f), 0, Ui.INSTANCE.dp(this, 8.0f));
        content.addView(textView);
        int frameWidth = RangesKt.coerceAtLeast(getResources().getDisplayMetrics().widthPixels - (Ui.INSTANCE.dp(this, 24.0f) * 2), 1);
        int frameHeight = WallpaperCropGeometry.INSTANCE.viewportHeight(frameWidth, CarPanelGeometry.PANEL_WIDTH, CarPanelGeometry.PANEL_HEIGHT);
        final WallpaperCropView wallpaperCropView = new WallpaperCropView(this, bitmap);
        wallpaperCropView.setBackgroundColor(color(R.color.surface_high));
        content.addView(wallpaperCropView, new LinearLayout.LayoutParams(-1, frameHeight));
        sheet.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda28
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                MainActivity.showWallpaperCropSheet$lambda$173(bitmap, dialogInterface);
            }
        });
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(0);
        actions.setGravity(16);
        String string2 = getString(R.string.wallpaper_crop_reselect);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        MaterialButton materialButtonSecondaryButton = secondaryButton(string2);
        materialButtonSecondaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showWallpaperCropSheet$lambda$176$lambda$175(sheet, this, view);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f);
        layoutParams.setMarginEnd(Ui.INSTANCE.dp(this, 8.0f));
        Unit unit = Unit.INSTANCE;
        actions.addView(materialButtonSecondaryButton, layoutParams);
        String string3 = getString(R.string.wallpaper_crop_apply);
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        final MaterialButton materialButtonPrimaryButton = primaryButton(string3);
        materialButtonPrimaryButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda30
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showWallpaperCropSheet$lambda$181$lambda$180(materialButtonPrimaryButton, sheet, this, wallpaperCropView, view);
            }
        });
        actions.addView(materialButtonPrimaryButton, new LinearLayout.LayoutParams(0, Ui.INSTANCE.dp(this, 44.0f), 1.0f));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.topMargin = Ui.INSTANCE.dp(this, 16.0f);
        Unit unit2 = Unit.INSTANCE;
        content.addView(actions, layoutParams2);
        sheet.show();
    }

    static final void showWallpaperCropSheet$lambda$173(Bitmap $bitmap, DialogInterface it) {
        if (!$bitmap.isRecycled()) {
            $bitmap.recycle();
        }
    }

    static final void showWallpaperCropSheet$lambda$176$lambda$175(BottomSheetDialog $sheet, MainActivity this$0, View it) {
        $sheet.dismiss();
        this$0.launchWallpaperPicker();
    }

    static final void showWallpaperCropSheet$lambda$181$lambda$180(MaterialButton $this_apply, BottomSheetDialog $sheet, MainActivity this$0, WallpaperCropView $crop, View button) {
        Object objM159constructorimpl;
        button.setEnabled(false);
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl($crop.createCroppedBitmap());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m162exceptionOrNullimpl(objM159constructorimpl) != null) {
            button.setEnabled(true);
            String string = this$0.getString(R.string.toast_wallpaper_failed);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            this$0.toast(string);
            return;
        }
        Bitmap cropped = (Bitmap) objM159constructorimpl;
        $sheet.dismiss();
        BuildersKt__Builders_commonKt.launch$default(this$0.scope, Dispatchers.getIO(), null, new MainActivity$showWallpaperCropSheet$5$1$1(this$0, cropped, null), 2, null);
    }

    private final void launchWallpaperPicker() {
        Object objM159constructorimpl;
        Intent picker = new Intent("android.intent.action.OPEN_DOCUMENT");
        picker.addCategory("android.intent.category.OPENABLE");
        picker.setType("image/*");
        try {
            Result.Companion companion = Result.INSTANCE;
            startActivityForResult(picker, 24);
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m162exceptionOrNullimpl(objM159constructorimpl) != null) {
            String string = getString(R.string.toast_wallpaper_picker_failed);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
        }
    }

    static /* synthetic */ View navRow$default(MainActivity mainActivity, String str, String str2, boolean z, Function0 function0, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return mainActivity.navRow(str, str2, z, function0);
    }

    private final View navRow(String title, String detail, boolean secondary, final Function0<Unit> onClick) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(0);
        row.setGravity(16);
        row.setPadding(Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 12.0f), Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 12.0f));
        row.setBackground(selectableBackground());
        row.setClickable(true);
        row.setFocusable(true);
        row.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke();
            }
        });
        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(1);
        TextView textView = new TextView(this);
        textView.setText(title);
        Ui.style$default(Ui.INSTANCE, textView, 14.0f, secondary ? R.color.text_secondary : R.color.text_primary, false, false, 12, null);
        texts.addView(textView);
        TextView textView2 = new TextView(this);
        textView2.setText(detail);
        Ui.style$default(Ui.INSTANCE, textView2, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView2.setPadding(0, Ui.INSTANCE.dp(this, 2.0f), 0, 0);
        textView2.setLineSpacing(Ui.INSTANCE.dp(this, 2.0f), 1.0f);
        texts.addView(textView2);
        row.addView(texts, new LinearLayout.LayoutParams(0, -2, 1.0f));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_chevron_right);
        imageView.setImageTintList(tint(R.color.text_tertiary));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this, 16.0f), Ui.INSTANCE.dp(this, 16.0f));
        layoutParams.setMarginStart(Ui.INSTANCE.dp(this, 8.0f));
        Unit unit = Unit.INSTANCE;
        row.addView(imageView, layoutParams);
        return row;
    }

    private final View toggleRow(String title, String detail, boolean checked, final Function1<? super Boolean, Unit> onChange) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(0);
        row.setGravity(16);
        row.setPadding(Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 10.0f), Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 10.0f));
        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(1);
        TextView textView = new TextView(this);
        textView.setText(title);
        Ui.style$default(Ui.INSTANCE, textView, 14.0f, R.color.text_primary, false, false, 12, null);
        texts.addView(textView);
        TextView textView2 = new TextView(this);
        textView2.setText(detail);
        Ui.style$default(Ui.INSTANCE, textView2, 11.0f, R.color.text_tertiary, false, false, 12, null);
        textView2.setPadding(0, Ui.INSTANCE.dp(this, 2.0f), 0, 0);
        textView2.setLineSpacing(Ui.INSTANCE.dp(this, 2.0f), 1.0f);
        texts.addView(textView2);
        row.addView(texts, new LinearLayout.LayoutParams(0, -2, 1.0f));
        MaterialSwitch materialSwitch = new MaterialSwitch(this);
        materialSwitch.setChecked(checked);
        materialSwitch.setContentDescription(title);
        materialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda37
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MainActivity.toggleRow$lambda$198$lambda$197(onChange, compoundButton, z);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMarginStart(Ui.INSTANCE.dp(this, 12.0f));
        Unit unit = Unit.INSTANCE;
        row.addView(materialSwitch, layoutParams);
        return row;
    }

    static final void toggleRow$lambda$198$lambda$197(Function1 $onChange, CompoundButton compoundButton, boolean value) {
        Intrinsics.checkNotNullParameter(compoundButton, "<unused var>");
        $onChange.invoke(Boolean.valueOf(value));
    }

    private final View divider(int top, int bottom) {
        View view = new View(this);
        view.setBackgroundColor(color(R.color.track));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(this, 1.0f));
        layoutParams.topMargin = Ui.INSTANCE.dp(this, top);
        layoutParams.bottomMargin = Ui.INSTANCE.dp(this, bottom);
        view.setLayoutParams(layoutParams);
        return view;
    }

    static /* synthetic */ View divider$default(MainActivity mainActivity, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 4;
        }
        if ((i3 & 2) != 0) {
            i2 = 4;
        }
        return mainActivity.divider(i, i2);
    }

    private final MaterialButton primaryButton(String label) {
        MaterialButton materialButton = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
        materialButton.setText(label);
        materialButton.setTextSize(14.0f);
        materialButton.setAllCaps(false);
        materialButton.setBackgroundTintList(tint(R.color.accent));
        materialButton.setTextColor(color(R.color.on_accent));
        materialButton.setCornerRadius(Ui.INSTANCE.dp(this, 14.0f));
        materialButton.setInsetTop(0);
        materialButton.setInsetBottom(0);
        materialButton.setMinimumHeight(0);
        materialButton.setStateListAnimator(null);
        materialButton.setPadding(Ui.INSTANCE.dp(this, 18.0f), 0, Ui.INSTANCE.dp(this, 18.0f), 0);
        return materialButton;
    }

    private final MaterialButton secondaryButton(String label) {
        MaterialButton materialButton = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        materialButton.setText(label);
        materialButton.setTextSize(13.0f);
        materialButton.setAllCaps(false);
        materialButton.setTextColor(color(R.color.text_secondary));
        materialButton.setStrokeColor(tint(R.color.stroke));
        materialButton.setStrokeWidth(Ui.INSTANCE.dp(this, 1.0f));
        materialButton.setCornerRadius(Ui.INSTANCE.dp(this, 14.0f));
        materialButton.setInsetTop(0);
        materialButton.setInsetBottom(0);
        materialButton.setMinimumHeight(0);
        materialButton.setStateListAnimator(null);
        materialButton.setPadding(Ui.INSTANCE.dp(this, 12.0f), 0, Ui.INSTANCE.dp(this, 12.0f), 0);
        return materialButton;
    }

    static /* synthetic */ MaterialButton iconButton$default(MainActivity mainActivity, int i, String str, int i2, Function0 function0, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            i2 = 40;
        }
        return mainActivity.iconButton(i, str, i2, function0);
    }

    private final MaterialButton iconButton(int icon, String description, int size, final Function0<Unit> onClick) {
        MaterialButton materialButton = new MaterialButton(this, null, com.google.android.material.R.attr.materialIconButtonStyle);
        materialButton.setIconResource(icon);
        materialButton.setIconTint(tint(R.color.text_secondary));
        materialButton.setIconSize(Ui.INSTANCE.dp(this, 20.0f));
        materialButton.setContentDescription(description);
        materialButton.setInsetTop(0);
        materialButton.setInsetBottom(0);
        materialButton.setOnClickListener(new View.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke();
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Ui.INSTANCE.dp(this, size), Ui.INSTANCE.dp(this, size));
        layoutParams.setMarginStart(Ui.INSTANCE.dp(this, 4.0f));
        materialButton.setLayoutParams(layoutParams);
        return materialButton;
    }

    private final Drawable selectableBackground() {
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
        if (value.resourceId != 0) {
            return ContextCompat.getDrawable(this, value.resourceId);
        }
        return null;
    }

    private final MaterialCardView card(float radiusDp) {
        MaterialCardView materialCardView = new MaterialCardView(this);
        materialCardView.setCardBackgroundColor(color(R.color.surface));
        materialCardView.setRadius(Ui.INSTANCE.dp(this, radiusDp));
        materialCardView.setStrokeColor(color(R.color.stroke));
        materialCardView.setStrokeWidth(Ui.INSTANCE.dp(this, 1.0f));
        materialCardView.setCardElevation(0.0f);
        materialCardView.setRippleColor(tint(R.color.stroke_soft));
        return materialCardView;
    }

    private final ColorStateList tint(int res) {
        ColorStateList colorStateListValueOf = ColorStateList.valueOf(color(res));
        Intrinsics.checkNotNullExpressionValue(colorStateListValueOf, "valueOf(...)");
        return colorStateListValueOf;
    }

    private final int color(int res) {
        return ContextCompat.getColor(this, res);
    }

    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0002\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f¨\u0006\u000e"}, d2 = {"Lcom/carlink/cast/MainActivity$Metric;", "", "card", "Lcom/google/android/material/card/MaterialCardView;", "value", "Landroid/widget/TextView;", "unit", "<init>", "(Lcom/google/android/material/card/MaterialCardView;Landroid/widget/TextView;Landroid/widget/TextView;)V", "getCard", "()Lcom/google/android/material/card/MaterialCardView;", "getValue", "()Landroid/widget/TextView;", "getUnit", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Metric {
        private final MaterialCardView card;
        private final TextView unit;
        private final TextView value;

        public Metric(MaterialCardView card, TextView value, TextView unit) {
            Intrinsics.checkNotNullParameter(card, "card");
            Intrinsics.checkNotNullParameter(value, "value");
            Intrinsics.checkNotNullParameter(unit, "unit");
            this.card = card;
            this.value = value;
            this.unit = unit;
        }

        public final MaterialCardView getCard() {
            return this.card;
        }

        public final TextView getValue() {
            return this.value;
        }

        public final TextView getUnit() {
            return this.unit;
        }
    }

    private final String sourceDetail(SourceMode mode) {
        String string;
        switch (WhenMappings.$EnumSwitchMapping$1[mode.ordinal()]) {
            case 1:
                string = getString(R.string.source_auto_detail);
                break;
            case 2:
                string = getString(R.string.source_carlife_detail);
                break;
            case 3:
                string = getString(R.string.source_mirror_detail);
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        Intrinsics.checkNotNull(string);
        return string;
    }

    private final boolean isTouchServiceEnabled() {
        boolean z;
        String expected = getPackageName() + "/" + TouchService.class.getName();
        String enabled = Settings.Secure.getString(getContentResolver(), "enabled_accessibility_services");
        if (enabled == null) {
            enabled = "";
        }
        Iterable iterableSplit$default = StringsKt.split$default((CharSequence) enabled, new char[]{':'}, false, 0, 6, (Object) null);
        if (!(iterableSplit$default instanceof Collection) || !((Collection) iterableSplit$default).isEmpty()) {
            Iterator it = iterableSplit$default.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                if (StringsKt.equals((String) it.next(), expected, true)) {
                    z = true;
                    break;
                }
            }
        } else {
            z = false;
        }
        return z || TouchBridge.INSTANCE.available();
    }

    private final boolean isMediaListenerEnabled() {
        String expected = getPackageName() + "/" + MediaListenerService.class.getName();
        String enabled = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (enabled == null) {
            enabled = "";
        }
        Iterable iterableSplit$default = StringsKt.split$default((CharSequence) enabled, new char[]{':'}, false, 0, 6, (Object) null);
        if ((iterableSplit$default instanceof Collection) && ((Collection) iterableSplit$default).isEmpty()) {
            return false;
        }
        Iterator it = iterableSplit$default.iterator();
        while (it.hasNext()) {
            if (StringsKt.equals((String) it.next(), expected, true)) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.sourceBadge != null) {
            TextView textView = this.sourceBadge;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sourceBadge");
                textView = null;
            }
            textView.setText(sourceChipText(CastLog.INSTANCE.getState().getValue()));
        }
        if (!StringsKt.isBlank(CastLog.INSTANCE.getState().getValue().getSessionId())) {
            CastLog.INSTANCE.event("INFO", "主界面回到前台");
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        if (!StringsKt.isBlank(CastLog.INSTANCE.getState().getValue().getSessionId())) {
            CastLog.INSTANCE.event("INFO", "主界面进入后台，finishing=" + isFinishing());
        }
        super.onStop();
    }

    private final void openAccessibility() {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m162exceptionOrNullimpl(objM159constructorimpl) != null) {
            String string = getString(R.string.toast_accessibility_failed);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
        }
    }

    private final void openMediaListenerSettings() {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m162exceptionOrNullimpl(objM159constructorimpl) != null) {
            String string = getString(R.string.toast_media_access_failed);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
        }
    }

    private final void openHonorCarConnect() {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Object obj = false;
        if (!CastLog.INSTANCE.getState().getValue().getRunning() && !this.pendingCapture) {
            CastLog.INSTANCE.event("INFO", "打开荣耀车联的同时在后台开始投屏");
            this.pendingHonorCarConnect = true;
            if (!ensurePermissions()) {
                return;
            }
            this.pendingHonorCarConnect = false;
            requestCaptureIfNeeded();
        }
        List candidates = CollectionsKt.listOf((Object[]) new Intent[]{new Intent().setClassName(HONOR_AUTO_PACKAGE, HONOR_AUTO_MANAGER), new Intent().setClassName(HONOR_AUTO_PACKAGE, "com.hihonor.auto.settings.carlifeSettingsAlias"), new Intent("android.intent.action.MAIN").setClassName(HONOR_AUTO_PACKAGE, "com.hihonor.auto.HonorAutoMainActivity")});
        for (Object obj2 : candidates) {
            Intrinsics.checkNotNullExpressionValue(obj2, "next(...)");
            Intent intent = (Intent) obj2;
            try {
                Result.Companion companion = Result.INSTANCE;
                startActivity(intent);
                objM159constructorimpl2 = Result.m159constructorimpl(true);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (thM162exceptionOrNullimpl != null) {
                CastLog castLog = CastLog.INSTANCE;
                ComponentName component = intent.getComponent();
                castLog.event("WARN", "打开荣耀车联失败（" + (component != null ? component.getClassName() : null) + "）：" + thM162exceptionOrNullimpl.getMessage());
                objM159constructorimpl2 = obj;
            }
            boolean reached = ((Boolean) objM159constructorimpl2).booleanValue();
            if (reached) {
                return;
            }
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            startActivity(new Intent("com.android.settings").setClassName("com.android.settings", "com.android.settings.Settings$HonorAutoSettingsActivity"));
            objM159constructorimpl = Result.m159constructorimpl(true);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        boolean settingsFallback = ((Boolean) (Result.m165isFailureimpl(objM159constructorimpl) ? false : objM159constructorimpl)).booleanValue();
        if (!settingsFallback) {
            try {
                Result.Companion companion5 = Result.INSTANCE;
                startActivity(new Intent("android.settings.SETTINGS"));
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            String string = getString(R.string.toast_connect_guide_failed);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
        }
    }

    private final void exportLog() {
        final List<CastSessionRecord> listSessions = CastLog.INSTANCE.sessions();
        if (listSessions.isEmpty()) {
            String string = getString(R.string.toast_export_empty);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
        List<CastSessionRecord> list = listSessions;
        int i = 0;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterable<CastSessionRecord> iterable = list;
        for (CastSessionRecord castSessionRecord : iterable) {
            String str = formatter.format(new Date(castSessionRecord.getStartedAtMillis()));
            String source = castSessionRecord.getSource();
            if (StringsKt.isBlank(source)) {
                source = castSessionRecord.getLegacy() ? "旧版会话" : "未知来源";
            }
            arrayList.add(str + " · " + source + " · " + (castSessionRecord.getHasCrash() ? "有退出/崩溃记录" : castSessionRecord.getStage()) + " · " + castSessionRecord.getFrames() + " 帧");
            formatter = formatter;
            list = list;
            i = i;
            iterable = iterable;
        }
        Collection labels = (List) arrayList;
        new AlertDialog.Builder(this).setTitle(R.string.export_session_title).setItems((CharSequence[]) labels.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.carlink.cast.MainActivity$$ExternalSyntheticLambda31
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                this.f$0.exportLog(((CastSessionRecord) listSessions.get(i2)).getSessionId());
            }
        }).setNegativeButton(R.string.dialog_cancel, (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void exportLog(String sessionId) {
        Object objM159constructorimpl;
        File file = CastLog.INSTANCE.export(sessionId);
        if (file == null) {
            String string = getString(R.string.toast_export_empty);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            toast(string);
            return;
        }
        this.pendingExport = file;
        String name = "carlink-" + StringsKt.take(sessionId, 8) + ".zip";
        Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("application/zip");
        intent.putExtra("android.intent.extra.TITLE", name);
        try {
            Result.Companion companion = Result.INSTANCE;
            startActivityForResult(intent, 23);
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m162exceptionOrNullimpl(objM159constructorimpl) != null) {
            this.pendingExport = null;
            shareExported(file);
        }
    }

    private final void shareExported(File file) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        try {
            Result.Companion companion = Result.INSTANCE;
            MainActivity mainActivity = this;
            objM159constructorimpl = Result.m159constructorimpl(FileProvider.getUriForFile(mainActivity, mainActivity.getPackageName() + ".logs", file));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        Uri uri = (Uri) objM159constructorimpl;
        if (uri == null) {
            MainActivity mainActivity2 = this;
            String string = mainActivity2.getString(R.string.toast_export_failed, new Object[]{file.getName()});
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            mainActivity2.toast(string);
            return;
        }
        Intent share = new Intent("android.intent.action.SEND");
        share.setType("application/zip");
        share.putExtra("android.intent.extra.STREAM", uri);
        share.addFlags(1);
        try {
            Result.Companion companion3 = Result.INSTANCE;
            MainActivity mainActivity3 = this;
            mainActivity3.startActivity(Intent.createChooser(share, mainActivity3.getString(R.string.settings_export)));
            objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
        if (thM162exceptionOrNullimpl != null) {
            int i = R.string.toast_export_failed;
            String message = thM162exceptionOrNullimpl.getMessage();
            if (message == null) {
                message = "";
            }
            String string2 = getString(i, new Object[]{message});
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            toast(string2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void toast(String message) {
        Toast.makeText(this, message, 0).show();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        if (!StringsKt.isBlank(CastLog.INSTANCE.getState().getValue().getSessionId())) {
            CastLog.INSTANCE.event("INFO", "主界面销毁，finishing=" + isFinishing() + "，changingConfigurations=" + isChangingConfigurations());
        }
        CoroutineScopeKt.cancel$default(this.scope, null, 1, null);
        super.onDestroy();
    }

    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/carlink/cast/MainActivity$Companion;", "", "<init>", "()V", "REQUEST_PERMISSIONS", "", "REQUEST_CAPTURE", "REQUEST_EXPORT", "REQUEST_WALLPAPER", "HONOR_AUTO_PACKAGE", "", "HONOR_AUTO_MANAGER", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
