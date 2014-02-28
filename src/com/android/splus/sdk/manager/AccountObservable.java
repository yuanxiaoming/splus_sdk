package com.android.splus.sdk.manager;

import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.file.AppUtil;

import java.util.ArrayList;
import java.util.Observable;
/**
 *
 * @ClassName: AccountObservable
 * @author xiaoming.yuan
 * @date 2014-2-19 下午3:30:59
 */
public class AccountObservable extends Observable {
    public static final String TAG = "AccountObservable";
    private static final int FIRST_POSITION = 0;

    private static AccountObservable mObservable = null;
    private static final byte[] lock = new byte[0];
    private ArrayList<UserModel> mUsers = new ArrayList<UserModel>();

    private AccountObservable() {
    }

    public static AccountObservable getInstance() {
        if (mObservable == null) {
            synchronized (lock) {
                if (mObservable == null) {
                    mObservable = new AccountObservable();
                }
            }
        }
        return mObservable;
    }

    public void init(ArrayList<UserModel> users) {
        mUsers.clear();
        mUsers.addAll(users);
    }

    // --------------------------------
    // 添加
    public void addUser(UserModel user) {
        mUsers.add(FIRST_POSITION, user);
        AppUtil.saveDatatoFile(user);
        setChanged();
        notifyObservers();
    }

    // --------------------------------
    // 删除
    public void delUser(int position) {
        int count = mUsers.size();
        if (position >= count || position < 0) {
            return;
        }
        AppUtil.deleteDataFile(mUsers.get(position));
        mUsers.remove(position);
        setChanged();
        notifyObservers();
    }

    // --------------------------------
    // 查询
    public ArrayList<UserModel> getAllUserData() {
        return mUsers;
    }

    public UserModel getUserData(int position) {
        int count = mUsers.size();
        if (position >= count || position < 0) {
            return null;
        } else {
            return mUsers.get(position);
        }
    }

    // --------------------------------
    // 修改
    public void modifyUser(int position, UserModel user) {
        int count = mUsers.size();
        if (position >= count || position < 0) {
            return;
        }
        if (user == null) {
            return;
        }
        mUsers.remove(position);
        mUsers.add(FIRST_POSITION, user);
        AppUtil.saveDatatoFile(user);
        setChanged();
        notifyObservers();
    }

    public void modifyUser(UserModel user) {
        if (user == null) {
            return;
        }
        int position = -1;
        for (int i = 0; i < mUsers.size(); i++) {
            UserModel temp = mUsers.get(i);
            if (temp.getPassport().equals(user.getPassport())) {
                position = i;
                break;
            }
        }
        if (position >= 0) {
            mUsers.remove(position);
        }
        addUser(user);
    }
}
