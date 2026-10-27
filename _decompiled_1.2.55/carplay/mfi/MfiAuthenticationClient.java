package com.carplay.mfi;

import androidx.core.app.NotificationCompat;
import com.carplay.iap2.transport.I2cTransport;
import com.carplay.iap2.transport.I2cTransportException;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: MfiAuthenticationClient.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\u0018\u0000 /2\u00020\u0001:\u0001/B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\b\u0010\u000e\u001a\u00020\u0005H\u0016J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0005H\u0016J\u0010\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J\u000f\u0010\u0014\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0002\u0010\u0015J\u0010\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0005H\u0002J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u0005H\u0002J\u0010\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0005H\u0002J\u0018\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0005H\u0002J\u0010\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u0017\u001a\u00020\u0005H\u0002J\u0010\u0010\u001f\u001a\u00020\u00192\u0006\u0010\u0017\u001a\u00020\u0005H\u0002J\u0018\u0010 \u001a\u00020\u00192\u0006\u0010\u0017\u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u0010H\u0002J)\u0010\"\u001a\u0002H#\"\u0004\b\u0000\u0010#2\u0006\u0010$\u001a\u00020%2\f\u0010&\u001a\b\u0012\u0004\u0012\u0002H#0'H\u0002¢\u0006\u0002\u0010(J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020*H\u0002J\u0010\u0010,\u001a\u00020\u00192\u0006\u0010-\u001a\u00020*H\u0002J\u0010\u0010.\u001a\u00020\u00192\u0006\u0010+\u001a\u00020*H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000bX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u00060"}, d2 = {"Lcom/carplay/mfi/MfiAuthenticationClient;", "Lcom/carplay/mfi/MfiAuthenticator;", NotificationCompat.CATEGORY_TRANSPORT, "Lcom/carplay/iap2/transport/I2cTransport;", "address7Bit", "", "<init>", "(Lcom/carplay/iap2/transport/I2cTransport;I)V", "getAddress7Bit", "()I", "certificateType", "Lcom/carplay/mfi/MfiCertificateType;", "getCertificateType", "()Lcom/carplay/mfi/MfiCertificateType;", "protocolMajor", "readCertificate", "", "maximumOutputLength", "signChallenge", "challenge", "bestEffortErrorCode", "()Ljava/lang/Integer;", "readUnsignedBigEndianShort", "register", "writeUnsignedBigEndianShort", "", "value", "readByte", "readRegister", "length", "selectRegister", "selectRegisterOnce", "writeRegister", "data", "retryIo", "T", "operation", "", "action", "Lkotlin/Function0;", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "deadlineAfter", "", "millis", "sleepMicros", "micros", "sleepMillis", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MfiAuthenticationClient implements MfiAuthenticator {
    private static final int AUTH_CONTROL_STATUS_REGISTER = 16;
    private static final long AUTH_POLL_MILLIS = 10;
    private static final int AUTH_START_COMMAND = 1;
    private static final int AUTH_SUCCESS_STATUS = 16;
    private static final long AUTH_TIMEOUT_MILLIS = 3000;
    private static final int BYTE_MASK = 255;
    private static final int CERTIFICATE_DATA_REGISTER = 49;
    private static final int CERTIFICATE_LENGTH_REGISTER = 48;
    private static final int CERTIFICATE_REGISTER_WINDOW_BYTES = 128;
    private static final int CHALLENGE_DATA_REGISTER = 33;
    private static final int CHALLENGE_LENGTH_REGISTER = 32;
    public static final int DEFAULT_MAXIMUM_CERTIFICATE_OUTPUT_LENGTH = 65525;
    private static final int ERROR_REGISTER = 5;
    private static final long INITIAL_AUTH_DELAY_MILLIS = 10;
    private static final long IO_RETRY_DELAY_MICROS = 20000;
    private static final long IO_RETRY_TIMEOUT_MILLIS = 2000;
    private static final int MAXIMUM_CHALLENGE_BYTES = 128;
    private static final int MAX_REGISTER_READ_BYTES = 65525;
    private static final long MICROS_PER_MILLISECOND = 1000;
    private static final int MINIMUM_CHALLENGE_BYTES = 1;
    private static final long NANOS_PER_MICROSECOND = 1000;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    private static final int PROTOCOL_MAJOR_REGISTER = 2;
    private static final int RESPONSE_DATA_REGISTER = 18;
    private static final int RESPONSE_LENGTH_REGISTER = 17;
    private final int address7Bit;
    private final MfiCertificateType certificateType;
    private final I2cTransport transport;
    private static final Object COPROCESSOR_LOCK = new Object();

    public MfiAuthenticationClient(I2cTransport transport, int address7Bit) {
        Intrinsics.checkNotNullParameter(transport, "transport");
        this.transport = transport;
        this.address7Bit = address7Bit;
        this.certificateType = MfiCertificateType.MFI;
        int i = this.address7Bit;
        boolean z = false;
        if (i >= 0 && i < 128) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("address7Bit must be in 0x00..0x7f".toString());
        }
    }

    @Override // com.carplay.mfi.MfiAuthenticator
    public BaaCertificatePair baaCertificates() {
        return super.baaCertificates();
    }

    public final int getAddress7Bit() {
        return this.address7Bit;
    }

    @Override // com.carplay.mfi.MfiAuthenticator
    public MfiCertificateType getCertificateType() {
        return this.certificateType;
    }

    @Override // com.carplay.mfi.MfiAuthenticator
    public int protocolMajor() {
        int i;
        synchronized (COPROCESSOR_LOCK) {
            i = readByte(2);
        }
        return i;
    }

    @Override // com.carplay.mfi.MfiAuthenticator
    public byte[] readCertificate(int maximumOutputLength) {
        byte[] certificate;
        synchronized (COPROCESSOR_LOCK) {
            boolean z = false;
            try {
                if (!(1 <= maximumOutputLength && maximumOutputLength < 65526)) {
                    throw new IllegalArgumentException("maximumOutputLength must be in 1..65525".toString());
                }
                int length = readUnsignedBigEndianShort(48);
                if (1 <= length && length <= maximumOutputLength) {
                    z = true;
                }
                if (!z) {
                    throw new MfiInvalidDataException("Certificate length " + length + " is outside 1.." + maximumOutputLength, null, 2, null);
                }
                certificate = new byte[length];
                int register = 49;
                int offset = 0;
                while (offset < offset) {
                    int count = Math.min(128, certificate.length - offset);
                    ArraysKt.copyInto$default(readRegister(register, count), certificate, offset, 0, 0, 12, (Object) null);
                    offset += count;
                    register++;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return certificate;
    }

    @Override // com.carplay.mfi.MfiAuthenticator
    public byte[] signChallenge(byte[] challenge) {
        byte[] register;
        Intrinsics.checkNotNullParameter(challenge, "challenge");
        synchronized (COPROCESSOR_LOCK) {
            int length = challenge.length;
            boolean z = false;
            if (!(1 <= length && length < 129)) {
                throw new MfiInvalidDataException("challenge must be 1..128 bytes", null, 2, null);
            }
            byte[] input = Arrays.copyOf(challenge, challenge.length);
            Intrinsics.checkNotNullExpressionValue(input, "copyOf(...)");
            writeUnsignedBigEndianShort(32, input.length);
            writeRegister(33, input);
            writeRegister(16, new byte[]{1});
            sleepMillis(10L);
            long deadlineNanos = deadlineAfter(AUTH_TIMEOUT_MILLIS);
            while (readByte(16) != 16) {
                if (System.nanoTime() >= deadlineNanos) {
                    throw new MfiAuthenticationFailedException(bestEffortErrorCode(), null, 2, null);
                }
                sleepMillis(10L);
            }
            int length2 = readUnsignedBigEndianShort(17);
            if (1 <= length2 && length2 < 65526) {
                z = true;
            }
            if (!z) {
                throw new MfiInvalidDataException("Invalid signature length " + length2, null, 2, null);
            }
            register = readRegister(18, length2);
        }
        return register;
    }

    private final Integer bestEffortErrorCode() throws MfiAuthenticationFailedException {
        try {
            selectRegister(5);
            byte[] result = this.transport.transaction(this.address7Bit, new byte[0], 1);
            if (result.length == 1) {
                return Integer.valueOf(result[0] & UByte.MAX_VALUE);
            }
            return null;
        } catch (I2cTransportException e) {
            return null;
        }
    }

    private final int readUnsignedBigEndianShort(int register) {
        byte[] bytes = readRegister(register, 2);
        return ((bytes[0] & UByte.MAX_VALUE) << 8) | (bytes[1] & UByte.MAX_VALUE);
    }

    private final void writeUnsignedBigEndianShort(int register, int value) throws I2cTransportException, MfiAuthenticationFailedException {
        if (!(value >= 0 && value < 65536)) {
            throw new IllegalArgumentException("value must be in 0..65535".toString());
        }
        writeRegister(register, new byte[]{(byte) (value >>> 8), (byte) value});
    }

    private final int readByte(int register) {
        return readRegister(register, 1)[0] & UByte.MAX_VALUE;
    }

    private final byte[] readRegister(final int register, final int length) {
        boolean z = false;
        if (1 <= length && length < 65526) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("read length must be in 1..65525".toString());
        }
        String string = Integer.toString(register, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return (byte[]) retryIo("read at 0x" + StringsKt.padStart(string, 2, '0'), new Function0() { // from class: com.carplay.mfi.MfiAuthenticationClient$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MfiAuthenticationClient.readRegister$lambda$7(this.f$0, register, length);
            }
        });
    }

    static final byte[] readRegister$lambda$7(MfiAuthenticationClient this$0, int $register, int $length) throws MfiInvalidDataException, I2cTransportException {
        this$0.selectRegisterOnce($register);
        byte[] result = this$0.transport.transaction(this$0.address7Bit, new byte[0], $length);
        if (result.length != $length) {
            String string = Integer.toString($register, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new MfiInvalidDataException("Register 0x" + string + " returned " + result.length + " bytes; expected " + $length, null, 2, null);
        }
        byte[] bArrCopyOf = Arrays.copyOf(result, result.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    private final void selectRegister(final int register) throws I2cTransportException, MfiAuthenticationFailedException {
        String string = Integer.toString(register, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        retryIo("register select 0x" + StringsKt.padStart(string, 2, '0'), new Function0() { // from class: com.carplay.mfi.MfiAuthenticationClient$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MfiAuthenticationClient.selectRegister$lambda$8(this.f$0, register);
            }
        });
    }

    static final Unit selectRegister$lambda$8(MfiAuthenticationClient this$0, int $register) throws MfiInvalidDataException, I2cTransportException {
        this$0.selectRegisterOnce($register);
        return Unit.INSTANCE;
    }

    private final void selectRegisterOnce(int register) throws MfiInvalidDataException, I2cTransportException {
        byte[] result = this.transport.transaction(this.address7Bit, new byte[]{(byte) register}, 0);
        if (!(result.length == 0)) {
            String string = Integer.toString(register, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new MfiInvalidDataException("Register select 0x" + string + " returned data", null, 2, null);
        }
    }

    private final void writeRegister(final int register, byte[] data) throws I2cTransportException, MfiAuthenticationFailedException {
        final byte[] request = new byte[data.length + 1];
        request[0] = (byte) register;
        ArraysKt.copyInto$default(data, request, 1, 0, 0, 12, (Object) null);
        String string = Integer.toString(register, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        retryIo("write at 0x" + StringsKt.padStart(string, 2, '0'), new Function0() { // from class: com.carplay.mfi.MfiAuthenticationClient$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MfiAuthenticationClient.writeRegister$lambda$9(this.f$0, request, register);
            }
        });
    }

    static final Unit writeRegister$lambda$9(MfiAuthenticationClient this$0, byte[] $request, int $register) throws MfiInvalidDataException, I2cTransportException {
        byte[] result = this$0.transport.transaction(this$0.address7Bit, $request, 0);
        if (!(result.length == 0)) {
            String string = Integer.toString($register, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new MfiInvalidDataException("Write to register 0x" + string + " returned data", null, 2, null);
        }
        return Unit.INSTANCE;
    }

    private final <T> T retryIo(String operation, Function0<? extends T> action) throws I2cTransportException, MfiAuthenticationFailedException {
        long deadlineNanos = deadlineAfter(IO_RETRY_TIMEOUT_MILLIS);
        while (true) {
            try {
                return action.invoke();
            } catch (I2cTransportException failure) {
                if (System.nanoTime() >= deadlineNanos) {
                    throw failure;
                }
                sleepMicros(IO_RETRY_DELAY_MICROS);
            }
        }
    }

    private final long deadlineAfter(long millis) {
        long now = System.nanoTime();
        long delta = 1000000 * millis;
        if (Long.MAX_VALUE - now < delta) {
            return Long.MAX_VALUE;
        }
        return now + delta;
    }

    private final void sleepMicros(long micros) throws MfiAuthenticationFailedException {
        try {
            Thread.sleep(micros / 1000, (int) ((micros % 1000) * 1000));
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new MfiAuthenticationFailedException(null, interrupted);
        }
    }

    private final void sleepMillis(long millis) throws MfiAuthenticationFailedException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new MfiAuthenticationFailedException(null, interrupted);
        }
    }
}
