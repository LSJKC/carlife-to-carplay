package com.baidu.carlife.mixing.aidl;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import com.baidu.carlife.mixing.CastConfig;

/**
 * Mirror of the host's ICarLifeCastManager AIDL.
 *
 * Transaction codes come from the decompiled host:
 *   prepareCast = 1, stopCast = 2, setSystemAudioCallback = 3,
 *   setCastCallback = 4, invokeMethod = 5
 */
public interface ICarLifeCastManager extends IInterface {
    String DESCRIPTOR = "com.baidu.carlife.mixing.aidl.ICarLifeCastManager";

    void prepareCast(CastConfig config) throws RemoteException;
    void stopCast() throws RemoteException;
    void setSystemAudioCallback(ISystemAudioCallback cb) throws RemoteException;
    void setCastCallback(ICarLifeCastCallback cb) throws RemoteException;
    Bundle invokeMethod(Intent intent) throws RemoteException;

    /** Server side: what the host actually binds to. */
    abstract class Stub extends android.os.Binder implements ICarLifeCastManager {
        private static final String TAG = "CarLifeProbe";

        public static ICarLifeCastManager asInterface(IBinder obj) {
            if (obj == null) return null;
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof ICarLifeCastManager) return (ICarLifeCastManager) iin;
            return null;
        }

        @Override
        public IBinder asBinder() { return this; }

        private static <T> T readTypedObject(Parcel p, Parcelable.Creator<T> c) {
            if (p.readInt() != 0) return c.createFromParcel(p);
            return null;
        }

        private static <T extends Parcelable> void writeTypedObject(Parcel p, T v, int flags) {
            if (v == null) {
                p.writeInt(0);
            } else {
                p.writeInt(1);
                v.writeToParcel(p, flags);
            }
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            Log.i(TAG, "onTransact code=" + code);
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(DESCRIPTOR);
            }
            switch (code) {
                case 1598968902: // INTERFACE_TRANSACTION
                    reply.writeString(DESCRIPTOR);
                    return true;
                case 1: { // prepareCast
                    CastConfig cfg = readTypedObject(data, CastConfig.CREATOR);
                    prepareCast(cfg);
                    if (reply != null) reply.writeNoException();
                    return true;
                }
                case 2: { // stopCast
                    stopCast();
                    if (reply != null) reply.writeNoException();
                    return true;
                }
                case 3: { // setSystemAudioCallback
                    setSystemAudioCallback(ISystemAudioCallback.Stub.asInterface(
                            data.readStrongBinder()));
                    if (reply != null) reply.writeNoException();
                    return true;
                }
                case 4: { // setCastCallback
                    setCastCallback(ICarLifeCastCallback.Stub.asInterface(
                            data.readStrongBinder()));
                    if (reply != null) reply.writeNoException();
                    return true;
                }
                case 5: { // invokeMethod
                    Bundle b = invokeMethod((Intent) readTypedObject(data, Intent.CREATOR));
                    if (reply != null) {
                        reply.writeNoException();
                        writeTypedObject(reply, b, 1);
                    }
                    return true;
                }
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }
}
