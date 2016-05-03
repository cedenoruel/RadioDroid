package net.programmierecke.radiodroid2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PlayerServiceUtil {
    public static void bind(Context context){
        Intent anIntent = new Intent(context, PlayerService.class);
        context.bindService(anIntent, svcConn, Context.BIND_AUTO_CREATE);
        context.startService(anIntent);
    }

    public static void unBind(Context context){
        PlayerService thisService = new PlayerService();
        thisService.unbindSafely(context, svcConn);
    }

    static IPlayerService itsPlayerService;
    private static ServiceConnection svcConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.i("PLAYER", "Service came online");
            itsPlayerService = IPlayerService.Stub.asInterface(binder);
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i("PLAYER", "Service offline");
            itsPlayerService = null;
        }
    };

    public static boolean isPlaying(){
        if (itsPlayerService != null){
            try {
                return itsPlayerService.getCurrentStationID() != null;
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public static void stop() {
        if (itsPlayerService != null) {
            try {
                itsPlayerService.Stop();
            } catch (RemoteException e) {
                Log.e("", "" + e);
            }
        }
    }

    public static void play(String result, String name, String id) {
        if (itsPlayerService != null) {
            try {
                itsPlayerService.Play(result,name,id);
            } catch (RemoteException e) {
                Log.e("", "" + e);
            }
        }
    }
}