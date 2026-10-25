package com.baidu.carlife.mixing.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MotionEvent;

/** Stub definition of the host's remote touch listener AIDL. */
public interface IRemoteTouchListener extends IInterface {
    String DESCRIPTOR = "com.baidu.carlife.mixing.aidl.IRemoteTouchListener";

    boolean onTouchEvent(MotionEvent event) throws RemoteException;

    abstract class Stub extends android.os.Binder implements IRemoteTouchListener {
        public static IRemoteTouchListener asInterface(IBinder obj) {
            if (obj == null) return null;
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IRemoteTouchListener) return (IRemoteTouchListener) iin;
            return null; // only ever the local side here; the host does not hand us one
        }

        @Override
        public IBinder asBinder() { return this; }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                MotionEvent ev = null;
                if (data.readInt() != 0) {
                    ev = MotionEvent.CREATOR.createFromParcel(data);
                }
                boolean r = onTouchEvent(ev);
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
