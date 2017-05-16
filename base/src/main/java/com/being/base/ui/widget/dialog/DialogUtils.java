package com.being.base.ui.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.being.base.R;
import com.being.base.ui.widget.menu.CustomArcMenu;
import com.being.base.utils.DeviceInfoUtils;

/**
 * 对话框工具类
 */
public class DialogUtils {

    /**
     * 对话框：标题，内容，中间按键
     * @param context
     * @param title
     * @param content
     * @param btnStr
     * @param btnListener
     * @return
     */
    public static Dialog showDialog(Context context, int title, int content, int btnStr, DialogInterface.OnClickListener btnListener){
        return showDialog(context, title, content, btnStr, btnListener, 0, null);
    }

    /**
     * 对话框：标题，内容，中间按键
     * @param context
     * @param title
     * @param content
     * @param btnStr
     * @param btnListener
     * @return
     */
    public static Dialog showDialog(Context context, CharSequence title, CharSequence content, CharSequence btnStr, DialogInterface.OnClickListener btnListener){
        return showDialog(context, title, content, btnStr, btnListener, null, null);
    }
    

    /**
     * 对话框：标题，内容，左右按键
     * @param context
     * @param title
     * @param content
     * @param leftBtnStr
     * @param leftBtnListener
     * @param rightBtnStr
     * @param rightBtnListener
     */
    public static Dialog showDialog(Context context, CharSequence title, CharSequence content, CharSequence leftBtnStr, final DialogInterface.OnClickListener leftBtnListener, CharSequence rightBtnStr, final DialogInterface.OnClickListener rightBtnListener) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return null;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if(!TextUtils.isEmpty(content)) {
            builder.setMessage(content);
        }
        if(!TextUtils.isEmpty(leftBtnStr)) {
            builder.setNegativeButton(leftBtnStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (leftBtnListener != null) {
                        leftBtnListener.onClick(dialogInterface, i);
                    }
                }
            });
        }
        if(!TextUtils.isEmpty(rightBtnStr)) {
            builder.setPositiveButton(rightBtnStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (rightBtnListener != null) {
                        rightBtnListener.onClick(dialogInterface, i);
                    }
                }
            });
        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        return dialog;
    }

    /**
     * 对话框：标题，内容，左右按键
     * @param context
     * @param title
     * @param content
     * @param leftBtnRes
     * @param leftBtnListener
     * @param rightBtnRes
     * @param rightBtnListener
     * @return
     */
    public static Dialog showDialog(Context context, int title, int content, int leftBtnRes, final DialogInterface.OnClickListener leftBtnListener, int rightBtnRes, final DialogInterface.OnClickListener rightBtnListener) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return null;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(title > 0) {
            builder.setTitle(title);
        }
        if(content > 0) {
            builder.setMessage(content);
        }
        if(leftBtnRes > 0) {
            builder.setNegativeButton(leftBtnRes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (leftBtnListener != null) {
                        leftBtnListener.onClick(dialogInterface, i);
                    }
                }
            });
        }
        if(rightBtnRes > 0) {
            builder.setPositiveButton(rightBtnRes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (rightBtnListener != null) {
                        rightBtnListener.onClick(dialogInterface, i);
                    }
                }
            });
        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        return dialog;
    }

    /**
     * 列表对话框: 标题，选项
     * @param context
     * @param title
     * @param items
     * @param itemListener
     * @return
     */
    public static Dialog showListDialog(Context context, CharSequence title, CharSequence[] items, AdapterView.OnItemClickListener itemListener){
        return showListDialog(context, title, items, -1, itemListener, null, null, null, null);
    }

    /**
     * 底部列表对话框: 标题，选项
     * @param context
     * @param title
     * @param items
     * @param itemListener
     * @return
     */
    public static Dialog showListDialogBottom(Context context, CharSequence title, CharSequence[] items, AdapterView.OnItemClickListener itemListener){
        return showListDialogBottom(context, title, items, -1, itemListener, null, null, null, null);
    }

    /**
     * 底部列表对话框: 标题，选项
     * @param context
     * @param title
     * @param items
     * @return
     */
    public static Dialog showListDialogBottom(Context context, CharSequence title, CharSequence[] items,
                                              AdapterView.OnItemClickListener listener, AdapterView.OnItemLongClickListener longClickListener){
        return showListDialogBottom(context, title, items, -1, listener, longClickListener, null, null, null, null);
    }

    /**
     * 列表对话框: 标题，选项
     * @param context
     * @param title
     * @param itemsArray
     * @param itemListener
     * @return
     */
    public static Dialog showListDialog(Context context, int title, int itemsArray, AdapterView.OnItemClickListener itemListener){
        return showListDialog(context, title, itemsArray, -1, itemListener, -1, null, -1, null);
    }

    /**
     * 列表对话框: 标题，选项，默认项
     * @param context
     * @param title
     * @param items
     * @param index
     * @param itemListener
     * @return
     */
    public static Dialog showListDialog(Context context, CharSequence title, CharSequence[] items, int index, AdapterView.OnItemClickListener itemListener){
        return showListDialog(context, title, items, index, itemListener, null, null, null, null);
    }

    /**
     * 列表对话框: 标题，选项，默认项
     * @param context
     * @param title
     * @param itemsArray
     * @param index
     * @param itemListener
     * @return
     */
    public static Dialog showListDialog(Context context, int title, int itemsArray, int index, AdapterView.OnItemClickListener itemListener){
        return showListDialog(context, title, itemsArray, index, itemListener, 0, null, 0, null);
    }


    /**
     * 列表对话框: 标题，选项，默认项，左右按键
     * @param context
     * @param title
     * @param items
     * @param index  默认项  从0开始  -1表示没有默认值
     * @param itemListener
     * @param leftBtnStr
     * @param leftBtnListener
     * @param rightBtnStr
     * @param rightBtnListener
     * @return
     */
    public static Dialog showListDialog(Context context, CharSequence title, CharSequence[] items, int index, AdapterView.OnItemClickListener itemListener, CharSequence leftBtnStr, DialogInterface.OnClickListener leftBtnListener, CharSequence rightBtnStr, DialogInterface.OnClickListener rightBtnListener) {
        NHDialog.Builder builder = new NHDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, index, itemListener, null);
        builder.setLeftBtn(leftBtnStr, leftBtnListener);
        builder.setRightBtn(rightBtnStr, rightBtnListener);
        Dialog dialog = builder.createNHDialog();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }
    /**
     * 底部列表对话框: 标题，选项，默认项，左右按键
     * @param context
     * @param title
     * @param items
     * @param index  默认项  从0开始  -1表示没有默认值
     * @param itemListener
     * @param leftBtnStr
     * @param leftBtnListener
     * @param rightBtnStr
     * @param rightBtnListener
     * @return
     */
    public static Dialog showListDialogBottom(Context context, CharSequence title, CharSequence[] items, int index, AdapterView.OnItemClickListener itemListener, CharSequence leftBtnStr, DialogInterface
            .OnClickListener leftBtnListener, CharSequence rightBtnStr, DialogInterface.OnClickListener rightBtnListener) {
        return showListDialogBottom(context, title, items, index, itemListener, null, leftBtnStr, leftBtnListener, rightBtnStr, rightBtnListener);
    }

    /**
     * 底部列表对话框: 标题，选项，默认项，左右按键
     * @param context
     * @param title
     * @param items
     * @param index  默认项  从0开始  -1表示没有默认值
     * @param itemListener
     * @param longClickListener
     * @param leftBtnStr
     * @param leftBtnListener
     * @param rightBtnStr
     * @param rightBtnListener
     * @return
     */
    public static Dialog showListDialogBottom(Context context, CharSequence title, CharSequence[] items,
                                              int index, AdapterView.OnItemClickListener itemListener,
                                              AdapterView.OnItemLongClickListener longClickListener,
                                              CharSequence leftBtnStr, DialogInterface
            .OnClickListener leftBtnListener, CharSequence rightBtnStr, DialogInterface.OnClickListener rightBtnListener) {
        NHDialog.Builder builder = new NHDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, index, itemListener, longClickListener);
        builder.setLeftBtn(leftBtnStr, leftBtnListener);
        builder.setRightBtn(rightBtnStr, rightBtnListener);
        Dialog dialog = builder.createNHDialog();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlideBottom;
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = DeviceInfoUtils.getScreenWidth(context);
//        params.height = DeviceInfoUtils.getScreenHeight(context)/2;
        params.height= WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();
        return dialog;
    }

    /**
     * 列表对话框: 标题，选项，默认项，左右按键
     * @param context
     * @param title 资源id
     * @param itemsArray
     * @param index 默认项  从0开始  -1表示没有默认值
     * @param itemListener
     * @param leftBtnRes
     * @param leftBtnListener
     * @param rightBtnRes
     * @param rightBtnListener
     * @return
     */
    public static Dialog showListDialog(Context context, int title, int itemsArray, int index, AdapterView.OnItemClickListener itemListener, int leftBtnRes, DialogInterface.OnClickListener leftBtnListener, int rightBtnRes, DialogInterface.OnClickListener rightBtnListener) {
        NHDialog.Builder builder = new NHDialog.Builder(context);
        if(title > 0) {
            builder.setTitle(title);
        }
        if(itemsArray > 0) {
            builder.setItems(itemsArray, index, itemListener, null);
        }
        if(leftBtnRes > 0) {
            builder.setLeftBtn(leftBtnRes, leftBtnListener);
        }
        if(rightBtnRes > 0) {
            builder.setRightBtn(rightBtnRes, rightBtnListener);
        }
        Dialog dialog = builder.createNHDialog();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    /**
     *
     * @param context
     * @param callBack 回调接口
     * @param imgs 图片id数组 int[]
     * @param radius 扇形半径
     * @param imgRadius 图片宽高
     * @param centerView 圆心处view，用于计算中心坐标
     * @return
     */
    public static Dialog showArcMenuDialog(Context context, CustomArcMenu.ClickCallBack callBack,
                                           int[] imgs, int radius, int imgRadius, int startAgle, int sweepAngle, View centerView) {
        HorizonArcMenuDialog.Builder builder = new HorizonArcMenuDialog.Builder(context);
        HorizonArcMenuDialog dialog = builder
                .setCallBack(callBack)
                .setImgs(imgs)
                .setRadius(radius)
                .setImgRadius(imgRadius)
                .setToggleDuration(300)
                .setAngl(sweepAngle)
                .setStartAngl(startAgle)
                .setIsTogglForOpen(false)
                .setCenterView(centerView)
                .create();

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }
    /**
     *
     * @param context
     * @param callBack 回调接口
     * @param imgs 图片id数组 int[]
     * @param radius 扇形半径
     * @param imgRadius 图片宽高
     * @return
     */
    public static Dialog showArcMenuDialog(Context context, CustomArcMenu.ClickCallBack callBack,
                                           int[] imgs, int radius, int imgRadius, int startAgle, int sweepAngle) {
        HorizonArcMenuDialog.Builder builder = new HorizonArcMenuDialog.Builder(context);
        HorizonArcMenuDialog dialog = builder
                .setCallBack(callBack)
                .setImgs(imgs)
                .setRadius(radius)
                .setImgRadius(imgRadius)
                .setToggleDuration(300)
                .setAngl(sweepAngle)
                .setStartAngl(startAgle)
                .setIsTogglForOpen(false)
                .create();

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    /**
     * 自定义列表对话框: 标题，选项，默认项   通过接口ListDialogInterface定义数据，每项的UI，每项的按键响应
     * @param context
     * @param title
     * @param index   默认项  从0开始  -1表示没有默认值
     * @param ymListDialogInterface
     * @return
     */
    public static Dialog showCustomListDialog(Context context, CharSequence title, int index, NHDialog.ListDialogInterface ymListDialogInterface){
        NHDialog.Builder builder = new NHDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(index, ymListDialogInterface);
        Dialog dialog = builder.createNHDialog();
        dialog.show();
        return dialog;
    }

    /**
     * 自定义列表对话框: 标题，选项，默认项   通过接口ListDialogInterface定义数据，每项的UI，每项的按键响应
     * @param context
     * @param title
     * @param index   默认项  从0开始  -1表示没有默认值
     * @param ymListDialogInterface
     * @return
     */
    public static Dialog showCustomListDialog(Context context, int title, int index, NHDialog.ListDialogInterface ymListDialogInterface){
        NHDialog.Builder builder = new NHDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(index, ymListDialogInterface);
        Dialog dialog = builder.createNHDialog();
        dialog.show();
        return dialog;
    }

}
