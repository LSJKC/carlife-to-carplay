package com.baidu.carlife.mixing.aidl;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import com.baidu.carlife.mixing.MusicInfo;

/**
 * Mirror of the host's ICarLifeCastCallback AIDL.
 *
 * Transaction codes are taken verbatim from the host's generated Stub; they are
 * ordered alphabetically by method name, NOT by declaration order:
 *   onServiceReady=1, onCastPrepared=2, addTouchListener=3, removeTouchListener=4,
 *   addKeyListener=5, removeKeyListener=6, sendMusicInfo=7, notifyMusicProgress=8,
 *   invokeMethod=9, onServiceDestroy=10, notifyAudioState=11,
 *   addAudioListener=12, removeAudioListener=13
 */
public interface ICarLifeCastCallback extends IInterface {
    String DESCRIPTOR = "com.baidu.carlife.mixing.aidl.ICarLifeCastCallback";

    void addAudioListener(IRemoteAudioListener l) throws RemoteException;
    void addKeyListener(IRemoteKeyListener l) throws RemoteException;
    void addTouchListener(IRemoteTouchListener l) throws RemoteException;
    Bundle invokeMethod(Intent intent) throws RemoteException;
    void notifyAudioState(int state) throws RemoteException;
    void notifyMusicProgress(long position) throws RemoteException;
    void onCastPrepared(int result) throws RemoteException;
    void onServiceDestroy() throws RemoteException;
    void onServiceReady() throws RemoteException;
    void removeAudioListener(IRemoteAudioListener l) throws RemoteException;
    void removeKeyListener(IRemoteKeyListener l) throws RemoteException;
    void removeTouchListener(IRemoteTouchListener l) throws RemoteException;
    void sendMusicInfo(MusicInfo info) throws RemoteException;

    /** Client side: used by our app to call into the host's callback. */
    class Proxy implements ICarLifeCastCallback {
        private final IBinder remote;

        public Proxy(IBinder remote) { this.remote = remote; }

        @Override
        public IBinder asBinder() { return remote; }

        /**
         * Returns a parcel with the interface token already written.
         * The token MUST be the first thing in the parcel; the host's
         * enforceInterface() reads it before anything else.
         */
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
        public void addAudioListener(IRemoteAudioListener l) throws RemoteException {
            Parcel d = withToken();
            d.writeStrongInterface(l);
            exec(12, d);
        }

        @Override
        public void addKeyListener(IRemoteKeyListener l) throws RemoteException {
            Parcel d = withToken();
            d.writeStrongInterface(l);
            exec(5, d);
        }

        @Override
        public void addTouchListener(IRemoteTouchListener l) throws RemoteException {
            Parcel d = withToken();
            d.writeStrongInterface(l);
            exec(3, d);
        }

        @Override
        public Bundle invokeMethod(Intent intent) throws RemoteException {
            Parcel d = withToken();
            if (intent != null) {
                d.writeInt(1);
                intent.writeToParcel(d, 0);
            } else {
                d.writeInt(0);
            }
            exec(9, d);
            return null;
        }

        @Override
        public void notifyAudioState(int state) throws RemoteException {
            Parcel d = withToken();
            d.writeInt(state);
            exec(11, d);
        }

        @Override
        public void notifyMusicProgress(long position) throws RemoteException {
            Parcel d = withToken();
            d.writeLong(position);
            exec(8, d);
        }

        @Override
        public void onCastPrepared(int result) throws RemoteException {
            Parcel d = withToken();
            d.writeInt(result);
            exec(2, d);
        }

        @Override
        public void onServiceDestroy() throws RemoteException {
            exec(10, withToken());
        }

        @Override
        public void onServiceReady() throws RemoteException {
            exec(1, withToken());
        }

        @Override
        public void removeAudioListener(IRemoteAudioListener l) throws RemoteException {
            Parcel d = withToken();
            d.writeStrongInterface(l);
            exec(13, d);
        }

        @Override
        public void removeKeyListener(IRemoteKeyListener l) throws RemoteException {
            Parcel d = withToken();
            d.writeStrongInterface(l);
            exec(6, d);
        }

        @Override
        public void removeTouchListener(IRemoteTouchListener l) throws RemoteException {
            Parcel d = withToken();
            d.writeStrongInterface(l);
            exec(4, d);
        }

        @Override
        public void sendMusicInfo(MusicInfo info) throws RemoteException {
            Parcel d = withToken();
            if (info != null) {
                d.writeInt(1);
                info.writeToParcel(d, 0);
            } else {
                d.writeInt(0);
            }
            exec(7, d);
        }
    }

    abstract class Stub extends android.os.Binder implements ICarLifeCastCallback {
        public static ICarLifeCastCallback asInterface(IBinder obj) {
            if (obj == null) return null;
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof ICarLifeCastCallback) return (ICarLifeCastCallback) iin;
            return new Proxy(obj);
        }

        @Override
        public IBinder asBinder() { return this; }
    }
}
