package com.being.base.application;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.being.base.ui.widget.ptr.lib.PtrDefaultHandler;
import com.being.base.ui.widget.ptr.lib.PtrFrameLayout;
import com.being.base.ui.widget.ptr.lib.util.PtrCLog;
import com.being.base.ui.widget.recyclerview.AbstractViewHolder;
import com.being.base.ui.widget.recyclerview.SimpleRecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PtrFrameLayout mPtrFrameLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mList = new ArrayList<>();
    private SimpleRecyclerAdapter<String, VH> mAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.layout_ptr);
        mPtrFrameLayout.DEBUG = true;
        PtrCLog.setLogLevel(PtrCLog.LEVEL_VERBOSE);
        LinearLayoutManager ll = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(ll);
        mAdapter = new SimpleRecyclerAdapter<String, VH>() {
            @Override
            protected VH createViewHolder(ViewGroup parent) {
                return new VH(parent);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                for (int i = 0; i < 100; i++) {
                    mList.add("text: " + i);
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                        mAdapter.setData(mList);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class VH extends AbstractViewHolder<String> {

        private TextView mTextView;

        public VH(ViewGroup parent) {
            super(parent, R.layout.item_test);
            mTextView = (TextView) convertView.findViewById(R.id.textView);
        }

        @Override
        public void bindDate(String data) {
            mTextView.setText(data);
        }
    }

}
