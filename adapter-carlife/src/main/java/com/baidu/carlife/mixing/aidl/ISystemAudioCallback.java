package com.baidu.carlife.mixing.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Stub definition of the host's system audio callback AIDL.
 *
 * Transaction codes from the host's generated Stub:
 *   onAudioStreamReady=1, onAudioStateChange=2, onAudioStateChangeWithParams=3
 *
 * The host hands us one of these, so asInterface() must be able to wrap a
 * remote binder in a Proxy — queryLocalInterface() returns null cross-process.
 */
public interface ISystemAudioCallback extends IInterface {
    String DESCRIPTOR = "com.baidu.carlife.mixing.aidl.ISystemAudioCallback";

    void onAudioStreamReady(byte[] data, int offset, int length, int rate, int channels)
            throws RemoteException;

    void onAudioStateChange(int state, int param) throws RemoteException;

    void onAudioStateChangeWithParams(int state, int param) throws RemoteException;

    /** Client side: talks to the host's callback object. */
    class Proxy implements ISystemAudioCallback {
        private final IBinder remote;

        public Proxy(IBinder remote) { this.remote = remote; }

        @Override
        public IBinder asBinder() { return remote; }

        private Parcel withToken() {
            Parcel p = Parcel.obtain();
            p.writeInterfaceToken(DESCRIPTOR);
            return p;
        }

        private void exec(int code, Parcel data) throws RemoteException {
            Parcel reply = Parcel.obtain();
            try {
                remote.transact(code, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public void onAudioStreamReady(byte[] data, int offset, int length, int rate, int channels)
                throws RemoteException {
            Parcel d = withToken();
            d.writeByteArray(data);
            d.writeInt(offset);
            d.writeInt(length);
            d.writeInt(rate);
            d.writeInt(channels);
            exec(1, d);
        }

        @Override
        public void onAudioStateChange(int state, int param) throws RemoteException {
            Parcel d = withToken();
            d.writeInt(state);
            d.writeInt(param);
            exec(2, d);
        }

        @Override
        public void onAudioStateChangeWithParams(int state, int param) throws RemoteException {
            Parcel d = withToken();
            d.writeInt(state);
            d.writeInt(param);
            exec(3, d);
        }
    }

    abstract class Stub extends android.os.Binder implements ISystemAudioCallback {
        public static ISystemAudioCallback asInterface(IBinder obj) {
            if (obj == null) return null;
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof ISystemAudioCallback) return (ISystemAudioCallback) iin;
            return new Proxy(obj);
        }

        @Override
        public IBinder asBinder() { return this; }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            switch (code) {
                case 1: {
                    data.enforceInterface(DESCRIPTOR);
                    byte[] b = data.createByteArray();
                    int off = data.readInt();
                    int len = data.readInt();
                    int rate = data.readInt();
                    int ch = data.readInt();
                    onAudioStreamReady(b, off, len, rate, ch);
                    if (reply != null) reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(DESCRIPTOR);
                    onAudioStateChange(data.readInt(), data.readInt());
                    if (reply != null) reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(DESCRIPTOR);
                    onAudioStateChangeWithParams(data.readInt(), data.readInt());
                    if (reply != null) reply.writeNoException();
                    return true;
                }
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }
}
