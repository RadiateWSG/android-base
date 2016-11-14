package com.being.base.ui.widget.ptr.lib;

import com.being.base.ui.widget.ptr.lib.*;
import com.being.base.ui.widget.ptr.lib.PtrUIHandler;
import com.being.base.ui.widget.ptr.lib.indicator.PtrIndicator;

/**
 * A single linked list to wrap PtrUIHandler
 */
class PtrUIHandlerHolder implements com.being.base.ui.widget.ptr.lib.PtrUIHandler {

    private com.being.base.ui.widget.ptr.lib.PtrUIHandler mHandler;
    private PtrUIHandlerHolder mNext;

    private boolean contains(com.being.base.ui.widget.ptr.lib.PtrUIHandler handler) {
        return mHandler != null && mHandler == handler;
    }

    private PtrUIHandlerHolder() {

    }

    public boolean hasHandler() {
        return mHandler != null;
    }

    private com.being.base.ui.widget.ptr.lib.PtrUIHandler getHandler() {
        return mHandler;
    }

    public static void addHandler(PtrUIHandlerHolder head, com.being.base.ui.widget.ptr.lib.PtrUIHandler handler) {

        if (null == handler) {
            return;
        }
        if (head == null) {
            return;
        }
        if (null == head.mHandler) {
            head.mHandler = handler;
            return;
        }

        PtrUIHandlerHolder current = head;
        for (; ; current = current.mNext) {

            // duplicated
            if (current.contains(handler)) {
                return;
            }
            if (current.mNext == null) {
                break;
            }
        }

        PtrUIHandlerHolder newHolder = new PtrUIHandlerHolder();
        newHolder.mHandler = handler;
        current.mNext = newHolder;
    }

    public static PtrUIHandlerHolder create() {
        return new PtrUIHandlerHolder();
    }

    public static PtrUIHandlerHolder removeHandler(PtrUIHandlerHolder head, com.being.base.ui.widget.ptr.lib.PtrUIHandler handler) {
        if (head == null || handler == null || null == head.mHandler) {
            return head;
        }

        PtrUIHandlerHolder current = head;
        PtrUIHandlerHolder pre = null;
        do {

            // delete current: link pre to next, unlink next from current;
            // pre will no change, current move to next element;
            if (current.contains(handler)) {

                // current is head
                if (pre == null) {

                    head = current.mNext;
                    current.mNext = null;

                    current = head;
                } else {

                    pre.mNext = current.mNext;
                    current.mNext = null;
                    current = pre.mNext;
                }
            } else {
                pre = current;
                current = current.mNext;
            }

        } while (current != null);

        if (head == null) {
            head = new PtrUIHandlerHolder();
        }
        return head;
    }

    @Override
    public void onUIReset(com.being.base.ui.widget.ptr.lib.PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        do {
            final com.being.base.ui.widget.ptr.lib.PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIReset(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIRefreshPrepare(com.being.base.ui.widget.ptr.lib.PtrFrameLayout frame) {
        if (!hasHandler()) {
            return;
        }
        PtrUIHandlerHolder current = this;
        do {
            final com.being.base.ui.widget.ptr.lib.PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIRefreshPrepare(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIRefreshBegin(com.being.base.ui.widget.ptr.lib.PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        do {
            final com.being.base.ui.widget.ptr.lib.PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIRefreshBegin(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIRefreshComplete(com.being.base.ui.widget.ptr.lib.PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        do {
            final com.being.base.ui.widget.ptr.lib.PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIRefreshComplete(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIPositionChange(com.being.base.ui.widget.ptr.lib.PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        PtrUIHandlerHolder current = this;
        do {
            final PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIPositionChange(frame, isUnderTouch, status, ptrIndicator);
            }
        } while ((current = current.mNext) != null);
    }
}
