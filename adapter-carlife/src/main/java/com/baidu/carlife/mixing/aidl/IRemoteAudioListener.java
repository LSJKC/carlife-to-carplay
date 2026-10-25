package com.baidu.carlife.mixing.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/** Stub definition of the host's remote audio listener AIDL. */
public interface IRemoteAudioListener extends IInterface {
    String DESCRIPTOR = "com.baidu.carlife.mixing.aidl.IRemoteAudioListener";

    void onReceiveAudioStream(byte[] data, int offset, int length, int sampleRate)
            throws RemoteException;

    abstract class Stub extends android.os.Binder implements IRemoteAudioListener {
        public static IRemoteAudioListener asInterface(IBinder obj) {
            if (obj == null) return null;
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IRemoteAudioListener) return (IRemoteAudioListener) iin;
            return null;
        }

        @Override
        public IBinder asBinder() { return this; }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                byte[] b = data.createByteArray();
                int off = data.readInt();
                int len = data.readInt();
                int rate = data.readInt();
                onReceiveAudioStream(b, off, len, rate);
                if (reply != null) reply.writeNoException();
                return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
