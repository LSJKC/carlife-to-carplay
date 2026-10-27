package com.carplay.iap2.transport;

import com.google.android.material.timepicker.TimeModel;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import org.slf4j.Marker;

/* JADX INFO: compiled from: Iap2LocationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001:\u0002\u001b\u001cB\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J)\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\u0012\u0010\u0015\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u0016\"\u00020\u0001H\u0002¢\u0006\u0002\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/carplay/iap2/transport/NmeaLocationEncoder;", "", "<init>", "()V", "encode", "", "fix", "Lcom/carplay/iap2/transport/CarPlayLocationFix;", "degreesToNmea", "Lcom/carplay/iap2/transport/NmeaLocationEncoder$NmeaCoordinate;", "value", "", "latitude", "", "checksum", "body", "ofEpochMilli", "Lcom/carplay/iap2/transport/NmeaLocationEncoder$Timestamp;", "millis", "", "format", "arguments", "", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "KNOTS_PER_METER_PER_SECOND", "MINUTES_PER_DEGREE", "MINUTE_ROUNDING_TOLERANCE", "NmeaCoordinate", "Timestamp", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class NmeaLocationEncoder {
    public static final NmeaLocationEncoder INSTANCE = new NmeaLocationEncoder();
    private static final double KNOTS_PER_METER_PER_SECOND = 1.94384449d;
    private static final double MINUTES_PER_DEGREE = 60.0d;
    private static final double MINUTE_ROUNDING_TOLERANCE = 5.0E-5d;

    private NmeaLocationEncoder() {
    }

    /* JADX WARN: Code duplicated, block: B:15:0x003a  */
    /* JADX WARN: Code duplicated, block: B:37:0x0100  */
    /* JADX WARN: Code duplicated, block: B:39:0x0110  */
    /* JADX WARN: Code duplicated, block: B:40:0x0113  */
    /* JADX WARN: Code duplicated, block: B:43:0x0118  */
    /* JADX WARN: Code duplicated, block: B:45:0x011c  */
    /* JADX WARN: Code duplicated, block: B:48:0x0134  */
    /* JADX WARN: Code duplicated, block: B:51:0x01b0  */
    /* JADX WARN: Code duplicated, block: B:53:0x01c0  */
    /* JADX WARN: Code duplicated, block: B:54:0x01c3  */
    /* JADX WARN: Code duplicated, block: B:59:0x01cd  */
    /* JADX WARN: Code duplicated, block: B:62:0x01d1  */
    /* JADX WARN: Code duplicated, block: B:64:0x01d5  */
    /* JADX WARN: Code duplicated, block: B:67:0x01f4  */
    /* JADX WARN: Code duplicated, block: B:70:0x0200  */
    /* JADX WARN: Code duplicated, block: B:72:0x0210  */
    /* JADX WARN: Code duplicated, block: B:73:0x0213  */
    /* JADX WARN: Code duplicated, block: B:76:0x0218  */
    /* JADX WARN: Code duplicated, block: B:78:0x021c  */
    /* JADX WARN: Code duplicated, block: B:79:0x0234  */
    /* JADX WARN: Code duplicated, block: B:80:0x0237  */
    /* JADX WARN: Code duplicated, block: B:83:0x023c  */
    public final String encode(CarPlayLocationFix fix) {
        Timestamp timestamp;
        double hdop;
        Double altitudeMeters;
        String altitude;
        Double speedMetersPerSecond;
        String speedKnots;
        Double bearingDegrees;
        String course;
        double it;
        boolean z;
        double it2;
        boolean z2;
        boolean z3;
        double it3;
        boolean z4;
        Intrinsics.checkNotNullParameter(fix, "fix");
        Long timestampMillis = fix.getTimestampMillis();
        if (timestampMillis != null) {
            long it4 = timestampMillis.longValue();
            if (!(it4 > 0)) {
                timestampMillis = null;
            }
            if (timestampMillis != null) {
                long p0 = timestampMillis.longValue();
                timestamp = ofEpochMilli(p0);
                if (timestamp == null) {
                    timestamp = ofEpochMilli(System.currentTimeMillis());
                }
            } else {
                timestamp = ofEpochMilli(System.currentTimeMillis());
            }
        } else {
            timestamp = ofEpochMilli(System.currentTimeMillis());
        }
        String time = format("%02d%02d%02d.00", Integer.valueOf(timestamp.getHour()), Integer.valueOf(timestamp.getMinute()), Integer.valueOf(timestamp.getSecond()));
        String date = format("%02d%02d%02d", Integer.valueOf(timestamp.getDay()), Integer.valueOf(timestamp.getMonth()), Integer.valueOf(timestamp.getYear() % 100));
        NmeaCoordinate latitude = degreesToNmea(fix.getLatitudeDegrees(), true);
        NmeaCoordinate longitude = degreesToNmea(fix.getLongitudeDegrees(), false);
        Double accuracyMeters = fix.getAccuracyMeters();
        double d = 0.0d;
        if (accuracyMeters != null) {
            double it5 = accuracyMeters.doubleValue();
            if (!(((Math.abs(it5) > Double.MAX_VALUE ? 1 : (Math.abs(it5) == Double.MAX_VALUE ? 0 : -1)) <= 0) && it5 > 0.0d)) {
                accuracyMeters = null;
            }
            if (accuracyMeters != null) {
                double it6 = accuracyMeters.doubleValue();
                d = 0.0d;
                hdop = Math.min(50.0d, Math.max(0.5d, it6 / 5.0d));
            }
            altitudeMeters = fix.getAltitudeMeters();
            if (altitudeMeters != null) {
                it3 = altitudeMeters.doubleValue();
                if (Math.abs(it3) <= Double.MAX_VALUE) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (!z4) {
                    altitudeMeters = null;
                }
                if (altitudeMeters != null) {
                    double it7 = altitudeMeters.doubleValue();
                    altitude = INSTANCE.format("%.1f", Double.valueOf(it7));
                    if (altitude == null) {
                        altitude = "0.0";
                    }
                } else {
                    altitude = "0.0";
                }
            } else {
                altitude = "0.0";
            }
            String ggaBody = "GPGGA," + time + "," + latitude.getValue() + "," + latitude.getHemisphere() + "," + longitude.getValue() + "," + longitude.getHemisphere() + ",1,08," + format("%.1f", Double.valueOf(hdop)) + "," + altitude + ",M,0.0,M,,";
            speedMetersPerSecond = fix.getSpeedMetersPerSecond();
            if (speedMetersPerSecond != null) {
                it2 = speedMetersPerSecond.doubleValue();
                if (Math.abs(it2) <= Double.MAX_VALUE) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2 || it2 < d) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                if (!z3) {
                    speedMetersPerSecond = null;
                }
                if (speedMetersPerSecond != null) {
                    double it8 = speedMetersPerSecond.doubleValue();
                    speedKnots = INSTANCE.format("%.2f", Double.valueOf(KNOTS_PER_METER_PER_SECOND * it8));
                    if (speedKnots == null) {
                        speedKnots = "0.00";
                    }
                } else {
                    speedKnots = "0.00";
                }
            } else {
                speedKnots = "0.00";
            }
            bearingDegrees = fix.getBearingDegrees();
            if (bearingDegrees != null) {
                it = bearingDegrees.doubleValue();
                if (Math.abs(it) <= Double.MAX_VALUE) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    bearingDegrees = null;
                }
                if (bearingDegrees != null) {
                    double it9 = bearingDegrees.doubleValue();
                    course = INSTANCE.format("%.2f", Double.valueOf(it9));
                }
                if (course == null) {
                    course = "";
                }
                String rmcBody = "GPRMC," + time + ",A," + latitude.getValue() + "," + latitude.getHemisphere() + "," + longitude.getValue() + "," + longitude.getHemisphere() + "," + speedKnots + "," + course + "," + date + ",,";
                return "$" + ggaBody + Marker.ANY_MARKER + checksum(ggaBody) + "\r\n$" + rmcBody + Marker.ANY_MARKER + checksum(rmcBody) + "\r\n";
            }
            course = null;
            if (course == null) {
                course = "";
            }
            String rmcBody2 = "GPRMC," + time + ",A," + latitude.getValue() + "," + latitude.getHemisphere() + "," + longitude.getValue() + "," + longitude.getHemisphere() + "," + speedKnots + "," + course + "," + date + ",,";
            return "$" + ggaBody + Marker.ANY_MARKER + checksum(ggaBody) + "\r\n$" + rmcBody2 + Marker.ANY_MARKER + checksum(rmcBody2) + "\r\n";
        }
        hdop = 1.0d;
        altitudeMeters = fix.getAltitudeMeters();
        if (altitudeMeters != null) {
            it3 = altitudeMeters.doubleValue();
            if (Math.abs(it3) <= Double.MAX_VALUE) {
                z4 = true;
            } else {
                z4 = false;
            }
            if (!z4) {
                altitudeMeters = null;
            }
            if (altitudeMeters != null) {
                double it10 = altitudeMeters.doubleValue();
                altitude = INSTANCE.format("%.1f", Double.valueOf(it10));
                if (altitude == null) {
                    altitude = "0.0";
                }
            } else {
                altitude = "0.0";
            }
        } else {
            altitude = "0.0";
        }
        String ggaBody2 = "GPGGA," + time + "," + latitude.getValue() + "," + latitude.getHemisphere() + "," + longitude.getValue() + "," + longitude.getHemisphere() + ",1,08," + format("%.1f", Double.valueOf(hdop)) + "," + altitude + ",M,0.0,M,,";
        speedMetersPerSecond = fix.getSpeedMetersPerSecond();
        if (speedMetersPerSecond != null) {
            it2 = speedMetersPerSecond.doubleValue();
            if (Math.abs(it2) <= Double.MAX_VALUE) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                z3 = false;
            } else {
                z3 = false;
            }
            if (!z3) {
                speedMetersPerSecond = null;
            }
            if (speedMetersPerSecond != null) {
                double it11 = speedMetersPerSecond.doubleValue();
                speedKnots = INSTANCE.format("%.2f", Double.valueOf(KNOTS_PER_METER_PER_SECOND * it11));
                if (speedKnots == null) {
                    speedKnots = "0.00";
                }
            } else {
                speedKnots = "0.00";
            }
        } else {
            speedKnots = "0.00";
        }
        bearingDegrees = fix.getBearingDegrees();
        if (bearingDegrees != null) {
            it = bearingDegrees.doubleValue();
            if (Math.abs(it) <= Double.MAX_VALUE) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                bearingDegrees = null;
            }
            if (bearingDegrees != null) {
                double it12 = bearingDegrees.doubleValue();
                course = INSTANCE.format("%.2f", Double.valueOf(it12));
            }
            if (course == null) {
                course = "";
            }
            String rmcBody3 = "GPRMC," + time + ",A," + latitude.getValue() + "," + latitude.getHemisphere() + "," + longitude.getValue() + "," + longitude.getHemisphere() + "," + speedKnots + "," + course + "," + date + ",,";
            return "$" + ggaBody2 + Marker.ANY_MARKER + checksum(ggaBody2) + "\r\n$" + rmcBody3 + Marker.ANY_MARKER + checksum(rmcBody3) + "\r\n";
        }
        course = null;
        if (course == null) {
            course = "";
        }
        String rmcBody4 = "GPRMC," + time + ",A," + latitude.getValue() + "," + latitude.getHemisphere() + "," + longitude.getValue() + "," + longitude.getHemisphere() + "," + speedKnots + "," + course + "," + date + ",,";
        return "$" + ggaBody2 + Marker.ANY_MARKER + checksum(ggaBody2) + "\r\n$" + rmcBody4 + Marker.ANY_MARKER + checksum(rmcBody4) + "\r\n";
    }

    private final NmeaCoordinate degreesToNmea(double value, boolean latitude) {
        String hemisphere;
        double absolute = Math.abs(value);
        int degrees = (int) absolute;
        double minutes = (absolute - ((double) degrees)) * MINUTES_PER_DEGREE;
        if (minutes >= 59.99995d) {
            degrees++;
            minutes = 0.0d;
        }
        Object[] objArr = {Integer.valueOf(degrees)};
        String degreeText = latitude ? format(TimeModel.ZERO_LEADING_NUMBER_FORMAT, objArr) : format("%03d", objArr);
        if (latitude) {
            hemisphere = value >= 0.0d ? "N" : "S";
        } else {
            hemisphere = value >= 0.0d ? "E" : "W";
        }
        return new NmeaCoordinate(degreeText + format("%07.4f", Double.valueOf(minutes)), hemisphere);
    }

    private final String checksum(String body) {
        int value = 0;
        int length = body.length();
        for (int i = 0; i < length; i++) {
            char character = body.charAt(i);
            value ^= character;
        }
        return format("%02X", Integer.valueOf(value));
    }

    private final Timestamp ofEpochMilli(long millis) {
        return new Timestamp(millis);
    }

    private final String format(String format, Object... arguments) {
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Locale locale = Locale.US;
        Object[] objArrCopyOf = Arrays.copyOf(arguments, arguments.length);
        String str = String.format(locale, format, Arrays.copyOf(objArrCopyOf, objArrCopyOf.length));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: compiled from: Iap2LocationClient.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0013"}, d2 = {"Lcom/carplay/iap2/transport/NmeaLocationEncoder$NmeaCoordinate;", "", "value", "", "hemisphere", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", "getValue", "()Ljava/lang/String;", "getHemisphere", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class NmeaCoordinate {
        private final String hemisphere;
        private final String value;

        public static /* synthetic */ NmeaCoordinate copy$default(NmeaCoordinate nmeaCoordinate, String str, String str2, int i, Object obj) {
            if ((i & 1) != 0) {
                str = nmeaCoordinate.value;
            }
            if ((i & 2) != 0) {
                str2 = nmeaCoordinate.hemisphere;
            }
            return nmeaCoordinate.copy(str, str2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getValue() {
            return this.value;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getHemisphere() {
            return this.hemisphere;
        }

        public final NmeaCoordinate copy(String value, String hemisphere) {
            Intrinsics.checkNotNullParameter(value, "value");
            Intrinsics.checkNotNullParameter(hemisphere, "hemisphere");
            return new NmeaCoordinate(value, hemisphere);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof NmeaCoordinate)) {
                return false;
            }
            NmeaCoordinate nmeaCoordinate = (NmeaCoordinate) other;
            return Intrinsics.areEqual(this.value, nmeaCoordinate.value) && Intrinsics.areEqual(this.hemisphere, nmeaCoordinate.hemisphere);
        }

        public int hashCode() {
            return (this.value.hashCode() * 31) + this.hemisphere.hashCode();
        }

        public String toString() {
            return "NmeaCoordinate(value=" + this.value + ", hemisphere=" + this.hemisphere + ")";
        }

        public NmeaCoordinate(String value, String hemisphere) {
            Intrinsics.checkNotNullParameter(value, "value");
            Intrinsics.checkNotNullParameter(hemisphere, "hemisphere");
            this.value = value;
            this.hemisphere = hemisphere;
        }

        public final String getHemisphere() {
            return this.hemisphere;
        }

        public final String getValue() {
            return this.value;
        }
    }

    /* JADX INFO: compiled from: Iap2LocationClient.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u000e\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u0011\u0010\u0010\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000bR\u0011\u0010\u0012\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000bR\u0011\u0010\u0014\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000b¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/transport/NmeaLocationEncoder$Timestamp;", "", "millis", "", "<init>", "(J)V", "fields", "Ljava/util/GregorianCalendar;", "hour", "", "getHour", "()I", "minute", "getMinute", "second", "getSecond", "day", "getDay", "month", "getMonth", "year", "getYear", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Timestamp {
        private final int day;
        private final GregorianCalendar fields;
        private final int hour;
        private final int minute;
        private final int month;
        private final int second;
        private final int year;

        public Timestamp(long millis) {
            GregorianCalendar $this$fields_u24lambda_u240 = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
            $this$fields_u24lambda_u240.setGregorianChange(new Date(Long.MIN_VALUE));
            $this$fields_u24lambda_u240.setTimeInMillis(millis);
            this.fields = $this$fields_u24lambda_u240;
            this.hour = this.fields.get(11);
            this.minute = this.fields.get(12);
            this.second = this.fields.get(13);
            this.day = this.fields.get(5);
            this.month = this.fields.get(2) + 1;
            this.year = this.fields.get(1);
        }

        public final int getHour() {
            return this.hour;
        }

        public final int getMinute() {
            return this.minute;
        }

        public final int getSecond() {
            return this.second;
        }

        public final int getDay() {
            return this.day;
        }

        public final int getMonth() {
            return this.month;
        }

        public final int getYear() {
            return this.year;
        }
    }
}
