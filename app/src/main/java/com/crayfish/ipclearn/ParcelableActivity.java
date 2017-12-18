package com.crayfish.ipclearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/13.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class ParcelableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParcelableItem item = getIntent().getParcelableExtra("item");
        Toast.makeText(this, ""+item.id+item.name, Toast.LENGTH_SHORT).show();
    }
}
