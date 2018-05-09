package com.gsd.idreamsky.weplay.model;

/**
 * Created by jove.chen on 2018/3/12.
 * BusEvent消息类型
 */

public class EbMsgEvent {

    //type类型
    public static final int TYPE_SYSTEM = 0;//系统相关
    public static final int TYPE_CHAT = 1;//聊天相关
    public static final int TYPE_USER = 2;//用户相关
    public static final int TYPE_FRIEND = 3;//好友相关
    public static final int TYPE_GAME = 4;//游戏相关

    //what类型
    public static final int WHAT_CHAT_LEAVE = 0;//用户离开聊天
    public static final int WHAT_SYSTEM_BARRAGE = 1;//弹幕
    public static final int WHAT_FRIEND_REQ_ADD = 4;//主动添加好友
    public static final int WHAT_UPDATE_UNREAD_MSG_NUM = 5;//未读消息数量更新
    public static final int WHAT_UPDATE_ADD_FRIEND_UNREAD_MSG_NUM = 6;//添加好友未读消息数量更新
    public static final int WHAT_ADD_FRIEND = 7;//有人请求添加我为好友

    /************************
     * 以下是TYPE_USER的子类型*
     * ********************/
    public static final int WHAT_SYSTEM_USER_LEVEL = 2;//用户更新经验
    public static final int WHAT_SYSTEM_USER_CONS = 3;//用户更新金币
    public static final int WHAT_SYSTEM_USER_INFO_CHANGED = 4;//用户信息更新

    //what-game
    public static final int WHAT_START_GAME = 0;//开始游戏
    public static final int WHAT_GAME_OVER_EXIT = 1;//游戏玩完后离开
    public static final int WHAT_GAME_OVER = 2;//对战游戏结束通知app
    public static final int WHAT_GAME_USER_KILL_APP = 3;//游戏中用户杀掉app进程
    public static final int WHAT_GAME_ONE_MORE = 4;//游戏结束再次PK的接受方收到的响应
    public static final int WHAT_GAME_ONE_MORE_RESPONSE = 5;//游戏结束再次PK的发起方收到的响应

    //消息类型
    public int type;//大类型
    public int what;//大类型下的子类型

    //消息描述，只用于描述改消失是用来干嘛的，不作为数据传输
    private String message;

    //用于传递参数的
    public Object extraObj;

    public EbMsgEvent(int mType) {
        this.type = mType;
    }

    public EbMsgEvent(int mType, String mMessage) {
        this.type = mType;
        this.message = mMessage;
    }
}
