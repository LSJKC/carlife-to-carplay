package com.baidu.carlife.mixing.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;

/** Stub definition of the host's remote key listener AIDL. */
public interface IRemoteKeyListener extends IInterface {
    String DESCRIPTOR = "com.baidu.carlife.mixing.aidl.IRemoteKeyListener";

    boolean onKeyEvent(KeyEvent event) throws RemoteException;

    abstract class Stub extends android.os.Binder implements IRemoteKeyListener {
        public static IRemoteKeyListener asInterface(IBinder obj) {
            if (obj == null) return null;
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IRemoteKeyListener) return (IRemoteKeyListener) iin;
            return null;
        }

        @Override
        public IBinder asBinder() { return this; }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                KeyEvent ev = null;
                if (data.readInt() != 0) {
                    ev = KeyEvent.CREATOR.createFromParcel(data);
                }
                boolean r = onKeyEvent(ev);
                if (reply != null) {
                    reply.writeNoException();
                    reply.writeInt(r ? 1 : 0);
                }
                return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
