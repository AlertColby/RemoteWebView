package com.cqs.weblib;

import android.content.Context;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\16 0016 17:48
 * 邮箱:chen510470614@163.com
 */
public class CommandManager {

    private Context mContext;

    private static CommandManager commandManager;

    private CommandManager(Context context){
        mContext = context;
    }

    public static CommandManager getInstance(Context context){
        if ( commandManager == null){
            synchronized (CommandManager.class){
                if (commandManager == null){
                    commandManager = new CommandManager(context);
                }
            }
        }
        return commandManager;
    }

    public void initAIDLConnection(){

    }


}
