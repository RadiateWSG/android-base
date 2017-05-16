package com.being.base.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.being.base.R;

/**
 * 自定义
 * 请不要使用这个类构造对话框
 * 如需要显示对话框，请使用DialogUtils
 * @author Administrator
 *
 */
@SuppressWarnings("unused")
public class NHDialog extends Dialog {

	protected NHDialog(Context context) {
		super(context);
	}

	protected NHDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {

		private Context context;

		private CharSequence title;
		private CharSequence content;
		private CharSequence leftBtnStr;
		private CharSequence rightBtnStr;
		private OnClickListener leftBtnClickListener;
		private OnClickListener rightBtnClickListener;
		private int curIndex;

		@SuppressWarnings("rawtypes")
		private ListDialogInterface impl;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setTitle(CharSequence title) {
			this.title = title;
			return this;
		}

        public Builder setTitle(int titleId) {
            this.title = context.getString(titleId);
            return this;
        }

		public Builder setContent(CharSequence content) {
			this.content = content;
			return this;
		}

        public Builder setContent(int contentId) {
            this.content = context.getString(contentId);
            return this;
        }

		public Builder setLeftBtn(CharSequence leftBtnStr, OnClickListener leftBtnClickListener) {
			this.leftBtnStr = leftBtnStr;
			this.leftBtnClickListener = leftBtnClickListener;
			return this;
		}

        public Builder setLeftBtn(int leftBtnId, OnClickListener leftBtnClickListener) {
            return setLeftBtn(context.getString(leftBtnId), leftBtnClickListener);
        }

		public Builder setRightBtn(CharSequence rightBtnStr, OnClickListener rightBtnClickListener) {
			this.rightBtnStr = rightBtnStr;
			this.rightBtnClickListener = rightBtnClickListener;
			return this;
		}

        public Builder setRightBtn(int rightBtnId, OnClickListener rightBtnClickListener) {
            return setRightBtn(context.getString(rightBtnId), rightBtnClickListener);
        }

        public Builder setItems(int strArrayId, int curIndex, final OnItemClickListener listener,
								AdapterView.OnItemLongClickListener longClickListener) {
            String[] items = context.getResources().getStringArray(strArrayId);
           return setItems(items, curIndex, listener, longClickListener);
        }

		@SuppressWarnings("rawtypes")
		public Builder setItems(final CharSequence[] items, final int curIndex, final OnItemClickListener listener,
								final AdapterView.OnItemLongClickListener longClickListener) {
			ListDialogInterface impl = new ListDialogInterface<CharSequence>() {

				@Override
				public void onItemClicked(AdapterView<?> parent, View view, int position, long id, CharSequence item) {
					if (listener != null) {
						listener.onItemClick(parent, view, position, id);
					}
				}

				@Override
				public boolean onItemLongClicked(AdapterView<?> parent, View view, int position, long id, CharSequence item) {
					if (longClickListener != null) {
						return longClickListener.onItemLongClick(parent, view, position, id);
					}
					return false;
				}

				@Override
				public View getView(int position, CharSequence item, View convertView) {
                    if (convertView == null) {
                        convertView = View.inflate(context, R.layout.widget_dialog_item, null);
                    }

					TextView tv = (TextView) convertView.findViewById(R.id.text);
					tv.setText(item);

					return convertView;
				}

				@Override
				public CharSequence[] getData() {
					return items;
				}

			};

			return setItems(curIndex, impl);
		}

		@SuppressWarnings("rawtypes")
		public Builder setItems(int curIndex, ListDialogInterface impl) {
			this.curIndex = curIndex;
			this.impl = impl;
			return this;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public NHDialog createNHDialog() {
			final NHDialog dialog;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				dialog = new NHDialog(context, android.R.style.Theme_Material_Dialog_NoActionBar);
			} else {
				dialog = new NHDialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
			}
			dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			View layout = LayoutInflater.from(context).inflate(R.layout.widget_dialog, null);
			dialog.setContentView(layout);
			if(this.impl != null && this.impl.getData().length > 5){
			  dialog.adjustWidthAndHeight(true);
			}
			dialog.setCanceledOnTouchOutside(false);

			TextView titleTv = (TextView) layout.findViewById(R.id.dialog_title);
			TextView contentTv = (TextView) layout.findViewById(R.id.dialog_content);
			ListView listView = (ListView) layout.findViewById(R.id.dialog_listview);
			Button leftBtn = (Button) layout.findViewById(R.id.dialog_left_button);
			Button rightBtn = (Button) layout.findViewById(R.id.dialog_right_button);

			View titleSeperator = layout.findViewById(R.id.dialog_title_seperator);
			View contentSeperator = layout.findViewById(R.id.dialog_content_seperator);
			View btnSeperator = layout.findViewById(R.id.dialog_button_seperator);

			// 设置标题
			if (!TextUtils.isEmpty(this.title)) {
				titleTv.setText(this.title);
			} else {
				titleTv.setVisibility(View.GONE);
				titleSeperator.setVisibility(View.GONE);
			}

			// 设置内容
			if (!TextUtils.isEmpty(this.content)) {
				contentTv.setText(this.content);
			} else {
				contentTv.setVisibility(View.GONE);
				contentSeperator.setVisibility(View.GONE);
			}

			// 设置列表选项
			if (impl != null) {
				final Object[] data = impl.getData();
				if (curIndex >= 0 && curIndex < data.length) {
					listView.setSelection(curIndex);
				}

				listView.setAdapter(new InternalAdapter(data, impl));
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						impl.onItemClicked(parent, view, position, id, data[position]);
						dialog.dismiss();
					}
				});
				listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
						return impl.onItemLongClicked(parent, view, position, id, data[position]);
					}
				});
			} else {
				listView.setVisibility(View.GONE);
			}

			// 设置左按键
			if (!TextUtils.isEmpty(this.leftBtnStr)) {
				leftBtn.setText(leftBtnStr);
				leftBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (leftBtnClickListener != null) {
							leftBtnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
						dialog.dismiss();
					}
				});
			} else {
				leftBtn.setVisibility(View.GONE);
				btnSeperator.setVisibility(View.GONE);
			}

			// 设置右按键
			if (!TextUtils.isEmpty(this.rightBtnStr)) {
				rightBtn.setText(rightBtnStr);
				rightBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (rightBtnClickListener != null) {
							rightBtnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
						dialog.dismiss();
					}
				});
			} else {
				rightBtn.setVisibility(View.GONE);
				btnSeperator.setVisibility(View.GONE);
			}

			return dialog;
		}
	}
	

	private void adjustWidthAndHeight(boolean ajdustHeight) {
		int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

		LayoutParams params = getWindow().getAttributes();
		if (ajdustHeight) {
			params.height = screenHeight - dip2px(140);
		}
		params.width = screenWidth - dip2px(40f);
		getWindow().setAttributes(params);
	}

	private int dip2px(float dpValue) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5f);
	}

	public interface ListDialogInterface<T> {
		public CharSequence[] getData();

		public View getView(int position, T item, View convertView);

		public void onItemClicked(AdapterView<?> parent, View view, int position, long id, T item);

		public boolean onItemLongClicked(AdapterView<?> parent, View view, int position, long id, T item);
	}

	private static class InternalAdapter<T> extends BaseAdapter {

		private T[] data;
		private ListDialogInterface<T> impl;

		public InternalAdapter(T[] data, ListDialogInterface<T> impl) {
			this.data = data;
			this.impl = impl;
		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.length;
		}

		@Override
		public T getItem(int position) {
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			return impl.getView(position, getItem(position), convertView);
		}
	}

}
