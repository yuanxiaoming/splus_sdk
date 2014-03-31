
package com.android.splus.sdk.ui;

import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.sharedPreferences.SharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName:FloatWindow
 * @author
 */

public class FloatWindow extends ImageView {

    private final static String PREFS_FILE = "FloatWindow";

    private final static int UPDATEVIEW_TYPE_BUTTON = 0;

    private final static int UPDATEVIEW_TYPE_MENU = 1;

    /**
     * 左边为true，右边为false
     */
    private final static String ALIGN = "align";

    private final static String POSITION = "position";

    private Activity mActivity;

    private FloatToolBarAlign mFloatToolBarAlign;

    private float mPosition;

    /**
     * 按钮
     */
    private WindowManager.LayoutParams mButtonWmParams;

    private WindowManager.LayoutParams mMenuWmParams;

    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    /**
     * 最小移动距离
     */
    private final static double DISTANCE = 9;

    /**
     * 按下时的坐标偏移
     */
    private double mStartX, mStartY;

    /**
     * 触摸点坐标
     */
    private double mTouchX, mTouchY;

    /**
     * 屏幕高宽
     */
    private int mScreenHeight, mScreenWidth;

    private float mDensity;

    /**
     * 按钮背景
     */
    private Drawable mIconNormal, mIconExpanded;

    private Drawable mBitmapRightDrawable, mBitmapLiftDrawable;

    private FloatMenu mFloatMenu;

    /**
     * 是否移动中
     */
    private boolean mMoving = false;

    /**
     * 速度
     */
    private final static int SPEED = 20;

    private int mSpeed;

    private final static int TIME = 10;

    /**
     * 配置文件
     */
    private SharedPreferences mSharedPreferences;

    /**
     * 创建一个新的实例 FloatWindow.
     * @param context
     * @param attrs
     * @param defStyle
     */

    public FloatWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 创建一个新的实例 FloatWindow.
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param context
     * @param attrs
     */

    public FloatWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 创建一个新的实例 FloatWindow.
     * @param context
     */

    public FloatWindow(Context context) {
        super(context);
    }

    /**
     * @param activity
     * @param showlasttime 是否显示上次位置
     * @param align 对齐方式
     * @param position 位置，相对于屏幕高百分比
     */
    public FloatWindow(Activity activity, boolean showlasttime, FloatToolBarAlign align,
            float position) {
        super(activity);
        this.mActivity = activity;
        this.mFloatToolBarAlign = align;
        this.mPosition = position;

        mSharedPreferences = activity.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        if (showlasttime) {
            boolean sides = mSharedPreferences.getBoolean(ALIGN, true);
            if (sides) {
                this.mFloatToolBarAlign = FloatToolBarAlign.Left;
            } else {
                this.mFloatToolBarAlign = FloatToolBarAlign.Right;
            }

            Editor sharedPreferencesEdit = activity.getSharedPreferences(PREFS_FILE,
                    Context.MODE_PRIVATE).edit();
            sharedPreferencesEdit.putFloat(POSITION, this.mPosition);
            sharedPreferencesEdit.commit();
            this.mPosition = mSharedPreferences.getFloat(POSITION, 0);
        } else {
            Editor sharedPreferencesEdit = activity.getSharedPreferences(PREFS_FILE,
                    Context.MODE_PRIVATE).edit();
            sharedPreferencesEdit.putFloat(POSITION, position);
            if (align == FloatToolBarAlign.Right) {
                sharedPreferencesEdit.putBoolean(ALIGN, false);
            } else {
                sharedPreferencesEdit.putBoolean(ALIGN, true);
            }
            sharedPreferencesEdit.commit();
        }
        initVariables();
        initViews();
        initButtonWindow();
        initMenuWindow();

    }

    /**
     * 初始化变量
     *
     * @author
     */
    private void initVariables() {
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mDensity = getResources().getDisplayMetrics().density;
        // 获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        mIconNormal = getResources().getDrawable(
                ResourceUtil.getDrawableId(mActivity, KR.drawable.splus_float_icon_normal));
        mIconExpanded = getResources().getDrawable(
                ResourceUtil.getDrawableId(mActivity, KR.drawable.splus_float_icon_press));
    }

    /**
     * 初始化视图
     *
     * @author
     */
    private void initViews() {
        setImageDrawable(mIconNormal);
        setClickable(true);
        setId(android.R.id.toggle);
        mFloatMenu = new FloatMenu(mActivity);

    }

    /**
     * 初始化WindowManager
     *
     * @author
     */
    private void initButtonWindow() {
        mButtonWmParams = new WindowManager.LayoutParams();
        // 设置window type
        mButtonWmParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
        // 设置图片格式，效果为背景透明
        mButtonWmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mButtonWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        if (mFloatToolBarAlign == FloatToolBarAlign.Right) {
            mButtonWmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        } else {
            mButtonWmParams.gravity = Gravity.LEFT | Gravity.TOP;
        }

        // 设置悬浮窗口长宽数据
        mButtonWmParams.width = (int) dpTopx(40);
        mButtonWmParams.height = (int) dpTopx(40);
        // buttonWmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        // buttonWmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        setVisibility(View.GONE);
        mWindowManager.addView(this, mButtonWmParams);
    }

    private void initMenuWindow() {
        mMenuWmParams = new WindowManager.LayoutParams();
        // 设置window type
        mMenuWmParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
        // 设置图片格式，效果为背景透明
        mMenuWmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mMenuWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置窗口展示动画
        mMenuWmParams.windowAnimations = android.R.style.Animation_Dialog;
        setFloatMenubackground(mFloatToolBarAlign);
        mFloatMenu.setVisibility(View.GONE);
        mWindowManager.addView(mFloatMenu, mMenuWmParams);
    }

    /**
     * 设置位置
     *
     * @author
     * @param tempX
     * @param tempY
     */

    private void setPosition(double tempX, double tempY) {
        mButtonWmParams.x = (int) tempX;
        mButtonWmParams.y = (int) (tempY - getStatusBarHeight());
        // 刷新
        handler.sendEmptyMessage(UPDATEVIEW_TYPE_BUTTON);
    }

    /**
     * 两边靠
     *
     * @author
     * @param side
     */
    private void alongSides(final FloatToolBarAlign side) {
        mMoving = true;
        mSpeed = (int) (SPEED * mDensity);
        new Thread() {
            public void run() {
                if (mFloatToolBarAlign == side) {
                    while (mButtonWmParams.x > 0) {
                        mButtonWmParams.x = mButtonWmParams.x - mSpeed;
                        mButtonWmParams.x = mButtonWmParams.x <= 0 ? 0 : mButtonWmParams.x;
                        try {
                            sleep(TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(UPDATEVIEW_TYPE_BUTTON);
                    }

                } else {
                    while (mButtonWmParams.x < mScreenWidth) {
                        mButtonWmParams.x = mButtonWmParams.x + mSpeed;
                        mButtonWmParams.x = mButtonWmParams.x > mScreenWidth ? mScreenWidth
                                : mButtonWmParams.x;
                        try {
                            sleep(TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(UPDATEVIEW_TYPE_BUTTON);
                    }
                }
                mFloatToolBarAlign = side;
                if (mFloatToolBarAlign == FloatToolBarAlign.Left) {
                    mButtonWmParams.gravity = Gravity.LEFT | Gravity.TOP;
                } else if (mFloatToolBarAlign == FloatToolBarAlign.Right) {
                    mButtonWmParams.gravity = Gravity.RIGHT | Gravity.TOP;
                } else {
                    mButtonWmParams.gravity = Gravity.LEFT | Gravity.TOP;
                }
                mButtonWmParams.x = 0;
                handler.sendEmptyMessage(UPDATEVIEW_TYPE_BUTTON);
                mMoving = false;
            };
        }.start();

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEVIEW_TYPE_BUTTON:
                    mWindowManager.updateViewLayout(FloatWindow.this, mButtonWmParams);
                    break;
                case UPDATEVIEW_TYPE_MENU:
                    mWindowManager.updateViewLayout(mFloatMenu, mMenuWmParams);
                    break;
                default:
                    break;
            }

        };
    };

    /**
     * 保存位置
     *
     * @author
     * @param align
     * @param position
     */
    private void savePosition(boolean align, float position) {
        position = position / mScreenHeight * 1.0f;
        Editor sharedPreferencesEdit = mActivity.getSharedPreferences(PREFS_FILE,
                Context.MODE_PRIVATE).edit();
        sharedPreferencesEdit.putBoolean(ALIGN, align);
        sharedPreferencesEdit.putFloat(POSITION, position);
        sharedPreferencesEdit.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMoving) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int[] location = new int[2];
                getLocationOnScreen(location);
                double x = location[0];
                double y = location[1];
                mTouchX = event.getRawX();
                mTouchY = event.getRawY();
                mStartX = mTouchX - x;
                mStartY = mTouchY - y;
                break;
            case MotionEvent.ACTION_MOVE:
                double tempX = event.getRawX();
                double tempY = event.getRawY();
                double temp = Math.pow(mTouchX - tempX, 2) + Math.pow(mTouchY - tempY, 2);
                if (Math.sqrt(temp) > DISTANCE) {
                    hideMenu();
                    mTouchX = tempX;
                    mTouchY = tempY;
                    if (mFloatToolBarAlign == FloatToolBarAlign.Left) {
                        setPosition(tempX - mStartX, tempY - mStartY);
                    } else {
                        setPosition(mScreenWidth - (tempX + mStartX), tempY - mStartY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mMoving) {
                    int[] position = new int[2];
                    getLocationOnScreen(position);
                    if ((position[0] + getMeasuredWidth() / 2) <= mScreenWidth / 2) {
                        alongSides(FloatToolBarAlign.Left);
                        savePosition(true, position[1] + getMeasuredHeight() / 2.0f);
                    } else {
                        alongSides(FloatToolBarAlign.Right);
                        savePosition(false, position[1] + getMeasuredHeight() / 2.0f);
                    }
                }

                break;
        }
        return detector.onTouchEvent(event);
    }

    private GestureDetector detector = new GestureDetector(mActivity, new SimpleOnGestureListener() {

        // 触发触摸事件
        public boolean onSingleTapConfirmed(MotionEvent e) {
            int location[] = new int[2];
            getLocationOnScreen(location);
            int y = (int) (location[1] + getMeasuredHeight() / 2.0f - mFloatMenu
                    .getMeasuredHeight() / 2);
            int x = getMeasuredWidth();
            showMenu(x, y);
            return false;
        };

        public void onLongPress(MotionEvent e) {
            int location[] = new int[2];
            getLocationOnScreen(location);
            int y = (int) (location[1] + getMeasuredHeight() / 2.0f - mFloatMenu
                    .getMeasuredHeight() / 2);
            int x = getMeasuredWidth();
            showMenu(x, y);
        };
    });

    /**
     * 显示菜单
     *
     * @author
     */
    private void showMenu(int x, int y) {
        setImageDrawable(mIconExpanded);
        if (mWindowManager == null || mMenuWmParams == null || mFloatMenu == null) {
            return;
        }
        if (mFloatMenu.getVisibility() == View.VISIBLE) {
            hideMenu();
            return;
        }
        mMenuWmParams.y = y;
        mMenuWmParams.x = x;
        setFloatMenubackground(mFloatToolBarAlign);
        mMenuWmParams.gravity = mButtonWmParams.gravity;
        mFloatMenu.startAnimation(floatMenuInScaleAnimation());
        mFloatMenu.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(UPDATEVIEW_TYPE_MENU);

    }

    private void setFloatMenubackground(FloatToolBarAlign align){
        if (align == FloatToolBarAlign.Right) {
            mBitmapRightDrawable =  getResources().getDrawable(
                    ResourceUtil.getDrawableId(mActivity, KR.drawable.splus_float_menuright_bg));
            mFloatMenu.setBackgroundDrawable(mBitmapRightDrawable);

            // mFloatMenu.setPadding(20, 10, 20, 10);
        } else {
            mBitmapLiftDrawable =  getResources().getDrawable(
                    ResourceUtil.getDrawableId(mActivity, KR.drawable.splus_float_menuleft_bg));
            mFloatMenu.setBackgroundDrawable(mBitmapLiftDrawable);
            //   mFloatMenu.setPadding(20, 10, 20, 10);
        }
        mFloatMenu.setPadding(10, 5, 10, 5);

        //    mFloatMenu.setPadding(mFloatMenu.getPaddingLeft(), 5, mFloatMenu.getPaddingRight(), 5);
        // 设置悬浮窗口长宽数据
        mMenuWmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        // menuWmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mMenuWmParams.height = (int) dpTopx(45);
    }

    /**
     * 隐藏菜单
     *
     * @author
     */
    private void hideMenu() {
        setImageDrawable(mIconNormal);
        if (mWindowManager == null || mMenuWmParams == null || mFloatMenu == null) {
            return;
        }
        mFloatMenu.startAnimation(floatMenuOutScaleAnimation());
        mFloatMenu.setVisibility(View.GONE);
    }

    /**
     * 获取状态栏高度
     *
     * @author
     * @return
     */
    private int getStatusBarHeight() {
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 隐藏
     *
     * @author
     */
    public void hide() {
        if (!isShowing()) {
            return;
        }
        if (mWindowManager == null || mButtonWmParams == null) {
            return;
        }
        hideMenu();
        setVisibility(View.GONE);
    }

    /**
     * 显示
     *
     * @author
     */
    public void show() {
        if (mWindowManager == null || mButtonWmParams == null) {
            return;
        }
        mPosition = mSharedPreferences.getFloat(POSITION, 0);
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mButtonWmParams.x = 0;
        mPosition = mPosition < 0 ? 0 : mPosition;
        mPosition = mPosition > 1 ? 1 : mPosition;
        mButtonWmParams.y = (int) ((mScreenHeight - getStatusBarHeight()) * mPosition - mIconNormal
                .getIntrinsicHeight() / 2);
        setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(UPDATEVIEW_TYPE_BUTTON);
    }

    /**
     * @author
     */
    public void recycle() {
        // 移除悬浮窗口
        if (mWindowManager != null) {
            mWindowManager.removeView(this);
            mButtonWmParams = null;
            if (mFloatMenu != null) {
                mWindowManager.removeView(mFloatMenu);
                mMenuWmParams = null;
            }
        }
    }

    /**
     * @return 是否显示
     */
    public boolean isShowing() {
        if (mButtonWmParams == null) {
            return false;
        }
        if (getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    private class FloatMenu extends LinearLayout {
        private Item account, forum, help, announcements;

        public FloatMenu(Context context) {
            super(context);
            setGravity(Gravity.CENTER);
            setOrientation(LinearLayout.HORIZONTAL);
            account = new Item(mActivity, ResourceUtil.getDrawableId(mActivity,
                    KR.drawable.splus_float_account_selector));
            forum = new Item(mActivity, ResourceUtil.getDrawableId(mActivity,
                    KR.drawable.splus_float_forum_selector));
            help = new Item(mActivity, ResourceUtil.getDrawableId(mActivity,
                    KR.drawable.splus_float_help_selector));
            announcements = new Item(mActivity, ResourceUtil.getDrawableId(mActivity,
                    KR.drawable.splus_float_announcement_selector));
            //            LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            //                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //            params.setMargins(20, 0, 20, 0);
            //            LayoutParams params1 = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            //                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //            params1.setMargins(0, 0, 20, 0);
            addView(help);
            addView(account );
            addView(forum);
            addView(announcements);

            account.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    hideMenu();
                    if (!SharedPreferencesHelper.getInstance().getLoginStatusPreferences(mActivity,
                            SplusPayManager.getInstance().getAppkey())) {
                        hideMenu();
                        return;
                    } else {
                        SplusPayManager.getInstance().enterUserCenter(mActivity,SplusPayManager.getInstance().getLogoutCallBack());
                    }
                }

            });
            forum.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    hideMenu();
                    if (!SharedPreferencesHelper.getInstance().getLoginStatusPreferences(mActivity,
                            SplusPayManager.getInstance().getAppkey())) {
                        hideMenu();
                        return;
                    } else {
                        SplusPayManager.getInstance().enterBBS(mActivity);
                    }
                }

            });
            help.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    hideMenu();
                    if (!SharedPreferencesHelper.getInstance().getLoginStatusPreferences(mActivity,
                            SplusPayManager.getInstance().getAppkey())) {
                        hideMenu();
                        return;
                    } else {
                        //密码找回界面
                        Intent intent = new Intent(mActivity, PersonActivity.class);
                        intent.putExtra(PersonActivity.INTENT_TYPE,
                                PersonActivity.INTENT_SQ);
                        mActivity.startActivity(intent);

                    }
                }

            });
            announcements.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    hideMenu();
                    if (!SharedPreferencesHelper.getInstance().getLoginStatusPreferences(mActivity,
                            SplusPayManager.getInstance().getAppkey())) {
                        return;
                    } else {
                        //论坛
                        Intent intent = new Intent(mActivity, PersonActivity.class);
                        intent.putExtra(PersonActivity.INTENT_TYPE,
                                PersonActivity.INTENT_FORUM);
                        mActivity.startActivity(intent);

                    }
                }

            });

        }

    }

    /**
     * 按钮
     */
    private class Item extends TextView {

        public Item(Context context, int background) {
            super(context);
            // setTextColor(0x525252);
            // setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            setGravity(Gravity.CENTER);
            float left = dpTopx(12);
            float top = dpTopx(5);
            float right = dpTopx(12);
            float bottom = dpTopx(3);
            Drawable drawable = (Drawable) getResources().getDrawable(background);
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                setBackground(drawable);
            } else {
                setBackgroundDrawable(drawable);
            }
            setPadding((int) left, (int) top, (int) right, (int) bottom);
        }

        public void setIcon(BitmapDrawable drawable) {
            drawable.setAntiAlias(true);
            setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }

        @SuppressLint("NewApi")
        @Override
        public void setBackground(Drawable background) {
            super.setBackground(background);
        }

    }

    /**
     * dp转px
     * @author
     * @param dp
     * @return
     */
    private float dpTopx(float dp) {
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
                .getDisplayMetrics());
        return size;

    }

    /**
     * Menu菜单显示时的动画
     */
    private ScaleAnimation floatMenuInScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(500);
        return scaleAnimation;
    }

    /**
     * Menu菜单隐藏时的动画
     */
    private ScaleAnimation floatMenuOutScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(500);
        return scaleAnimation;
    }

}
