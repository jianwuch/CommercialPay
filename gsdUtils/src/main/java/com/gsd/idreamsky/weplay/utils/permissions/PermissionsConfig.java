package com.gsd.idreamsky.weplay.utils.permissions;

import java.util.ArrayList;

/**
 * 权限组,用于匹配权限
 * @author bzai.xiao
 */
public class PermissionsConfig {

	public static ArrayList<String> CALENDAR;//日历
	public static ArrayList<String> CAMERA;  //相机
	public static ArrayList<String> CONTACTS;//联系人
	public static ArrayList<String> LOCATION;//位置信息
	public static ArrayList<String> PHONE;//电话
	public static ArrayList<String> SENSORS;//传感器
	public static ArrayList<String> SMS;//短信
	public static ArrayList<String> STORAGE;//存储
	public static ArrayList<String> MICROPHONE;//麦克风


	public static ArrayList<String> getCalendar(){
		CALENDAR = new ArrayList<String>();
		CALENDAR.add("android.permission.READ_CALENDAR");
		CALENDAR.add("android.permission.WRITE_CALENDAR");
		return CALENDAR;
	}

	public static ArrayList<String> getCamera(){
		CAMERA = new ArrayList<String>();
		CAMERA.add("android.permission.CAMERA");
		return CAMERA;
	}

	public static ArrayList<String> getContacts(){
		CONTACTS = new ArrayList<String>();
		CONTACTS.add("android.permission.WRITE_CONTACTS");
		CONTACTS.add("android.permission.GET_ACCOUNTS");
		CONTACTS.add("android.permission.READ_CONTACTS");
		return CONTACTS;
	}

	public static ArrayList<String> getLocation(){
		LOCATION = new ArrayList<String>();
		LOCATION.add("android.permission.ACCESS_FINE_LOCATION");
		LOCATION.add("android.permission.ACCESS_COARSE_LOCATION");
		return LOCATION;
	}

	public static ArrayList<String> getMicrophone(){
		MICROPHONE = new ArrayList<String>();
		MICROPHONE.add("android.permission.RECORD_AUDIO");
		return MICROPHONE;
	}


	public static ArrayList<String> getPhone(){
		PHONE = new ArrayList<String>();
		PHONE.add("android.permission.READ_CALL_LOG");
		PHONE.add("android.permission.READ_PHONE_STATE");
		PHONE.add("android.permission.CALL_PHONE");
		PHONE.add("android.permission.WRITE_CALL_LOG");
		PHONE.add("android.permission.USE_SIP");
		PHONE.add("android.permission.PROCESS_OUTGOING_CALLS");
		PHONE.add("com.android.voicemail.permission.ADD_VOICEMAIL");
		return PHONE;
	}

	public static ArrayList<String> getSensors(){
		SENSORS = new ArrayList<String>();
		SENSORS.add("android.permission.BODY_SENSORS");
		return SENSORS;
	}

	public static ArrayList<String> getSms(){
		SMS = new ArrayList<String>();
		SMS.add("android.permission.READ_SMS");
		SMS.add("android.permission.RECEIVE_WAP_PUSH");
		SMS.add("android.permission.RECEIVE_MMS");
		SMS.add("android.permission.RECEIVE_SMS");
		SMS.add("android.permission.SEND_SMS");
		SMS.add("android.permission.READ_CELL_BROADCASTS");
		return SMS;
	}

	public static ArrayList<String> getStorage(){
		STORAGE = new ArrayList<String>();
		STORAGE.add("android.permission.READ_EXTERNAL_STORAGE");
		STORAGE.add("android.permission.WRITE_EXTERNAL_STORAGE");
		return STORAGE;
	}


	public static String getMatchPermission(String permission){

		String permissionStr = "";
		if (getCalendar().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Calendar"))+ "\n";
		}else if (getCamera().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Camera"))+ "\n";
		}else if (getContacts().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Contacts"))+ "\n";
		}else if (getLocation().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Location"))+ "\n";
		}else if (getMicrophone().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Microphone"))+ "\n";
		}else if (getPhone().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Phone"))+ "\n";
		}else if (getSensors().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Sensors"))+ "\n";
		}else if (getSms().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Sms"))+ "\n";
		}else if (getStorage().contains(permission)) {
			permissionStr = PermissionsHelper.getContext().getString(ResourseId.getStringId(PermissionsHelper.getContext(),
					PermissionsHelper.getContext().getPackageName(),"DGC_Permissons_Storage"))+ "\n";
		}

		return permissionStr;
	}

	public static int getPermissionsOps(String permission){

		int op = -1;
		if (permission == null) {
			return op;
		}
		if (permission.equals("android.permission.READ_CALENDAR")) {
			op = PermissionsOpsConfig.OP_READ_CALENDAR;

		}else if (permission.equals("android.permission.WRITE_CALENDAR")){
			op = PermissionsOpsConfig.OP_WRITE_CALENDAR;

		}else if (permission.equals("android.permission.CAMERA")) {
			op = PermissionsOpsConfig.OP_CAMERA;

		}else if (permission.equals("android.permission.WRITE_CONTACTS")) {
			op = PermissionsOpsConfig.OP_WRITE_CONTACTS;

		}else if (permission.equals("android.permission.READ_CONTACTS")) {
			op = PermissionsOpsConfig.OP_READ_CONTACTS;

		}else if (permission.equals("android.permission.ACCESS_FINE_LOCATION")) {
			op = PermissionsOpsConfig.OP_FINE_LOCATION;

		}else if (permission.equals("android.permission.ACCESS_COARSE_LOCATION")) {
			op = PermissionsOpsConfig.OP_COARSE_LOCATION;

		}else if (permission.equals("android.permission.RECORD_AUDIO")) {
			op = PermissionsOpsConfig.OP_RECORD_AUDIO;

		}else if (permission.equals("android.permission.READ_CALL_LOG")) {
			op = PermissionsOpsConfig.OP_READ_CALL_LOG;

		}else if (permission.equals("android.permission.READ_PHONE_STATE")) {
			op = PermissionsOpsConfig.OP_READ_PHONE_STATE;

		}else if (permission.equals("android.permission.CALL_PHONE")) {
			op = PermissionsOpsConfig.OP_CALL_PHONE;

		}else if (permission.equals("android.permission.WRITE_CALL_LOG")) {
			op = PermissionsOpsConfig.OP_WRITE_CALL_LOG;

		}else if (permission.equals("android.permission.USE_SIP")) {
			op = PermissionsOpsConfig.OP_USE_SIP;

		}else if (permission.equals("android.permission.PROCESS_OUTGOING_CALLS")) {
			op = PermissionsOpsConfig.OP_PROCESS_OUTGOING_CALLS;

		}else if (permission.equals("com.android.voicemail.permission.ADD_VOICEMAIL")) {
			op = PermissionsOpsConfig.OP_ADD_VOICEMAIL;

		}else if (permission.equals("android.permission.BODY_SENSORS")) {
			op = PermissionsOpsConfig.OP_BODY_SENSORS;

		}else if (permission.equals("android.permission.READ_SMS")) {
			op = PermissionsOpsConfig.OP_READ_SMS;

		}else if (permission.equals("android.permission.RECEIVE_WAP_PUSH")) {
			op = PermissionsOpsConfig.OP_RECEIVE_WAP_PUSH;

		}else if (permission.equals("android.permission.RECEIVE_MMS")) {
			op = PermissionsOpsConfig.OP_RECEIVE_MMS;

		}else if (permission.equals("android.permission.RECEIVE_SMS")) {
			op = PermissionsOpsConfig.OP_RECEIVE_SMS;

		}else if (permission.equals("android.permission.SEND_SMS")) {
			op = PermissionsOpsConfig.OP_SEND_SMS;

		}else if (permission.equals("android.permission.READ_CELL_BROADCASTS")) {
			op = PermissionsOpsConfig.OP_READ_CELL_BROADCASTS;

		}else if (permission.equals("android.permission.READ_EXTERNAL_STORAGE")) {
			op = PermissionsOpsConfig.OP_READ_EXTERNAL_STORAGE;

		}else if (permission.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
			op = PermissionsOpsConfig.OP_WRITE_EXTERNAL_STORAGE;
		}
		return op;
	}

}
