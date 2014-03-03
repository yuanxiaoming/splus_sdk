/**
 * @Title: LoginAdapter.java
 * @Package com.sanqi.android.sdk.adapter
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-9-5 下午4:15:04
 * @version V1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.manager.AccountObservable;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @ClassName: LoginAdapter
 * @author xiaoming.yuan
 * @date 2013-9-5 下午4:15:04
 */

public class LoginAdapter extends BaseAdapter {
    private TextView mUserName;

    private TextView mPassword;

    private CheckBox cb_remember_pwd;

    private PopupWindow popView;

    private Context mContext;

    private ArrayList<UserModel> mUsers = new ArrayList<UserModel>();


    public LoginAdapter(Context context, ArrayList<UserModel> mUsers, TextView mUserName,
            TextView mPassword, CheckBox cb_mindPassword,PopupWindow popView) {
        this.mUsers = mUsers;
        this.mContext = context;
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        this.cb_remember_pwd = cb_mindPassword;
        this.popView = popView;
    }

    public void changeUsers(ArrayList<UserModel> mUsers) {
        if (mUsers == null) {
            mUsers = new ArrayList<UserModel>();
        }
        this.mUsers = mUsers;
        notifyDataSetChanged();
    }

    public void setPopView(PopupWindow popView) {
        this.popView = popView;

    }

    /**
     * Title: getCount Description:
     *
     * @return
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {

        return mUsers.size();

    }

    /**
     * Title: getItem Description:
     *
     * @param position
     * @return
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {

        return mUsers.get(position);

    }

    /**
     * Title: getItemId Description:
     *
     * @param position
     * @return
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {

        return position;

    }

    /**
     * Title: getView Description:
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @see android.widget.Adapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    ResourceUtil.getLayoutId(mContext, KR.layout.splus_login_dropdown_item_layout), null);
            holder.ll = (LinearLayout) convertView.findViewById(ResourceUtil.getId(mContext,
                    KR.id.splus_login_dropdown_layout));
            holder.ll.setFocusable(false);
            holder.btn = (ImageView) convertView.findViewById(ResourceUtil.getId(mContext,
                    KR.id.splus_login_dropdown_delete));
            holder.btn.setFocusable(false);
            holder.tv = (TextView) convertView.findViewById(ResourceUtil.getId(mContext,
                    KR.id.splus_login_dropdown_text));
            holder.tv.setFocusable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final UserModel UserMode = mUsers.get(position);
        holder.tv.setText(UserMode.getUserName());
        holder.tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUserName.setText(UserMode.getUserName());
                if (null != UserMode && null != UserMode.getUserName()) {
                    if (UserMode.getChecked()) {
                        // 对应的账户点击了 保存密码，将用户密码自动填写到密码框
                        mPassword.setText(UserMode.getPassword());
                        cb_remember_pwd.setChecked(true);
                    } else {
                        mPassword.setText(null);
                        cb_remember_pwd.setChecked(false);
                    }
                }
                popView.dismiss();
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUsers != null && mUsers.size() > 1) {
                    if (mUsers.get(position).getUserName().equals(mUserName.getText().toString())) {
                        AccountObservable.getInstance().delUser(position);
                        UserModel UserMode = mUsers.get(0);
                        if (UserMode != null) {
                            // 保存用户数据
                            AppUtil.saveDatatoFile(UserMode);
                            mUserName.setText(UserMode.getUserName());
                            mPassword.setText(UserMode.getPassword());
                        }
                    } else {
                        AccountObservable.getInstance().delUser(position);
                    }
                }
                notifyDataSetChanged();
            }
        });
        if (position % 2 == 0) {
            // 偶数则颜色为
            holder.ll.setBackgroundColor(Color.parseColor("#ebebeb"));
        } else {
            // 奇数为白色
            holder.ll.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return convertView;
    }

    class ViewHolder {
        private LinearLayout ll;

        private TextView tv;

        private ImageView btn;
    }
}
