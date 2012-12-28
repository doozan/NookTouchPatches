package com.android.internal.policy.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class ModUtils {
  
  private static final String VERSION          = "0.1.2rd";
  private static final String TAG              = "NookMod";
  
  private static volatile com.android.internal.policy.impl.PhoneWindowManager mContext;
  
  private static volatile Keyinfo[] keys = {new Keyinfo(92,"topleft"),
                                            new Keyinfo(93,"bottomleft"),
                                            new Keyinfo(94,"topright"),
                                            new Keyinfo(95,"bottomright")
                                           };
  
  private static class Keyinfo {
    public volatile int     keycode;
    public volatile String  settingname;
    public volatile long    startpressmillis  = 0;
    public volatile boolean isActionPerformed = false;
    public volatile boolean actionresult      = false;

    public Keyinfo (int keycode, String settingname) {
      this.keycode = keycode;
      this.settingname = settingname;
    }
    
    public void setKeyPressed(long millis) {
      startpressmillis = millis;
      isActionPerformed = false;
    }
    
    public void setActionPerformed(boolean result) {
      isActionPerformed = true;
      actionresult = result;
    }
  }

  public static String getVersion() { return VERSION; }

  public static boolean onHomeKeyPress(Context context) {
    ContentResolver resolver = context.getContentResolver();
    String action = Settings.System.getString(resolver, "mod.key.home.cmd");
    
    //Log.v(TAG, "OnHomeKeyPress()");
    return doAction(context, action);
  }

  public static boolean onHomeKeyPressLong(Context context) {
    ContentResolver resolver = context.getContentResolver();
    String action = Settings.System.getString(resolver, "mod.key.home_long.cmd");
    
    //Log.v(TAG, "OnHomeKeyLongPress()");
    return doAction(context, action);
  }

  // call doAction in the other package just to avoid having to duplicate the functionality here
  private static boolean doAction(Context context, String action) {
    return com.android.server.status.ModUtils.doAction(context,  action);
  }
  
  public static void setContext(com.android.internal.policy.impl.PhoneWindowManager context)
  {
    //Log.v(TAG, "setContext =  " + context );
    mContext = context;
  }
  
  public static com.android.internal.policy.impl.PhoneWindowManager getContext()
  {
    //Log.v(TAG, "getContext =  " + mContext );
    return mContext;
  }
  
  public static boolean interceptKeyTi(int keycode, boolean keydown, int repeatCount) {
    //Log.v(TAG, "interceptKeyTi =  " + keycode + ";" + keydown );

    Context context;
    try {
      context = (Context) Class.forName("com.android.internal.policy.impl.PhoneWindowManager").getDeclaredField("mContext").get(getContext());
    } catch (Exception e) {
      Log.v(TAG, "interceptKeyTi context failed " + e.toString() + "\n" + e.getCause() );
      return false;
    }
    ContentResolver resolver = context.getContentResolver();

    long sidebtndelay_long;
    try {
      sidebtndelay_long = Long.parseLong(Settings.System.getString(resolver, "mod_sidebtndelay_long"));
    }
    catch (Exception e) {
      sidebtndelay_long = 2000;
    }
    
    // Log.v(TAG, "interceptKeyTi() sidebtndelay_long = " + sidebtndelay_long);
    
    for (int i = 0; i<keys.length; i++) {
      if (keycode == keys[i].keycode) {
        String action = Settings.System.getString(resolver, "mod.key." + keys[i].settingname + ".cmd");
        String actionlong = Settings.System.getString(resolver, "mod.key." + keys[i].settingname + "_long.cmd");
        if((action == null || action.length() == 0) && (actionlong == null || actionlong.length() == 0))
          return false;
        else {
          //Log.v(TAG, "interceptKeyTi();" + keycode + ";" + keys[i].settingname + ";" + keydown + ";" + ";" + repeatCount + ";" + keys[i].startpressmillis);
          if (keydown && repeatCount == 0) {
            keys[i].setKeyPressed(System.currentTimeMillis());
            return true;
          }
          else if (keydown && repeatCount != 0) {
            if (!keys[i].isActionPerformed && System.currentTimeMillis() - keys[i].startpressmillis >= sidebtndelay_long) {
              keys[i].setActionPerformed(doAction(context, actionlong));
              return true;
            }
          } else {
            if (!keys[i].isActionPerformed) {
              if (System.currentTimeMillis() - keys[i].startpressmillis < sidebtndelay_long) {
                keys[i].setActionPerformed(doAction(context, action));
              }
              else {
                keys[i].setActionPerformed(doAction(context, actionlong));
              }
            }
            return keys[i].actionresult;
          }
          return true;
        }
      }
    }

    return false;
  }
}