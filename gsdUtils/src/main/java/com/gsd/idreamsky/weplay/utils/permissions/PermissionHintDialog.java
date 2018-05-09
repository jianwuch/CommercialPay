package com.gsd.idreamsky.weplay.utils.permissions;



import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PermissionHintDialog extends Dialog implements View.OnClickListener{

	private String TAG = getClass().getSimpleName();
	private Context context;
	private Button btnCancel, btnOk;
	private TextView tvContent;
	private String strContent,strCancel,strOk;
	private MyDialogListener cancelListener,okListener;
	public interface MyDialogListener{
		public void onClick();
	}
	public PermissionHintDialog(Context context, String strContent, String strCancel, String strOk,
			MyDialogListener cancelListener, MyDialogListener okListener) {
		super(context, ResourseId.getStyleByName(context, context.getPackageName(),  "DGC_Pay_Dialog"));
		Log.i(TAG, "PermissionHintDialog show");
		this.context = context;
		this.strContent = strContent;
		this.strCancel = strCancel;
		this.strOk = strOk;
		this.cancelListener = cancelListener;
		this.okListener = okListener;
		initView();
	}

	private void initView() {
		int id = ResourseId.getLayoutId(context, context.getPackageName(), "dgc_permission_hint_dialog");
		Log.i(TAG, id + "");
		setContentView(id);
		setCanceledOnTouchOutside(false);
		btnCancel = (Button) findViewById(ResourseId.getId(context, context.getPackageName(), "dgc_permission_hint_cancel"));
		btnOk = (Button) findViewById(ResourseId.getId(context, context.getPackageName(), "dgc_permission_hint_ok"));
		tvContent = (TextView) findViewById(ResourseId.getId(context, context.getPackageName(), "dgc_permission_hint_content"));
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		btnCancel.setText(strCancel);
		btnOk.setText(strOk);
		tvContent.setText(strContent);
	}

	@Override
	public void cancel() {
		cancelListener.onClick();
		super.cancel();
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == btnCancel.getId()){
			cancelListener.onClick();
			super.dismiss();
		}
		if(view.getId() == btnOk.getId()){
			okListener.onClick();
			super.dismiss();
		}
	}

}
