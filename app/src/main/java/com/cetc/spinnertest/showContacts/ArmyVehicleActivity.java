package com.cetc.spinnertest.showContacts;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cetc.spinnertest.ArmyVehicle;
import com.cetc.spinnertest.DividerItemDecoration;
import com.cetc.spinnertest.EmptyRecyclerView;
import com.cetc.spinnertest.ItemDetailFragment;
import com.cetc.spinnertest.ItemListActivity;
import com.cetc.spinnertest.R;
import com.cetc.spinnertest.adapter.ArmyVehicleAdapter;

import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.util.List;

public class ArmyVehicleActivity extends AppCompatActivity {

    private static final String TAG = "ArmyVehicleActivity";

    private DividerItemDecoration mDecoration;
    List<ArmyVehicle> armyVehicleList;
    RecyclerView recyclerView;
    EmptyRecyclerView emptyRecyclerView;
    ArmyVehicleAdapter adapter;
    FloatingActionButton fab;
    TextView tvHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_army_vehicle);

        SQLiteDatabase db = LitePal.getDatabase();

        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                ArmyVehicle armyVehicle = new ArmyVehicle();
                armyVehicle.setName("车辆一3265" );
                armyVehicle.setBDSimID(294919);
                armyVehicle.save();


                if (armyVehicle.isSaved()) {
                    Log.i(TAG, "创建数据库并保存数据成功");
                }
            }
            @Override
            public void onUpgrade(int oldVersion, int newVersion) {

            }
        });

        ArmyVehicle armyVehicle = new ArmyVehicle();
        armyVehicle.setName("车辆一3265" );
        armyVehicle.setBDSimID(1234124);
        armyVehicle.save();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_vehicle);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);

//        }
        initSource();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showNewDialog(ArmyVehicleActivity.this);
            }
        });


    }
    private void initSource() {
        fab = (FloatingActionButton) findViewById(R.id.fb_add);
        tvHint = (TextView) findViewById(R.id.tv_hint);
        mDecoration = new DividerItemDecoration(ArmyVehicleActivity.this, DividerItemDecoration.VERTICAL_LIST);
        armyVehicleList = LitePal.where("id>?", "0")
                .order("name asc")
                .find(ArmyVehicle.class);
        recyclerView = (RecyclerView) findViewById(R.id.item_list_vehicle);
        assert recyclerView != null;
        recyclerView.addItemDecoration(mDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArmyVehicleAdapter(armyVehicleList);
        adapter.setOnItemClickListener(new ArmyVehicleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArmyVehicle itemVehicle = (ArmyVehicle) view.getTag();
                Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(item.getBDSimID()));
                arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID,itemVehicle);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                tvHint.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                adapter.removeItem(position);
            }
        });
        recyclerView.setAdapter(adapter);



    }

    private void showNewDialog(final Context context) {
        LayoutInflater inflater = ArmyVehicleActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_item, null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ArmyVehicleActivity.this);
        builder.setTitle("添加新的车辆");


// Set up the input
        builder.setView(dialogView);

        final EditText inputSim = (EditText) dialogView.findViewById(R.id.et_new_vehicle_id);
        final EditText inputName = (EditText) dialogView.findViewById(R.id.et_new_vehicle_name);
        final TextView tvShowError = (TextView) dialogView.findViewById(R.id.tv_new_error);


// Set up the buttons
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: dialogClick");

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

//                builder.show();

        final android.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputSim.length() < 6 || inputSim.length() > 8) {
//                    tvShowError.setVisibility(View.VISIBLE);


                } else {
                    List<ArmyVehicle> armyVehicleList = LitePal.where("bdsimid=?", inputSim.getText().toString())
                            .find(ArmyVehicle.class);
                    if (armyVehicleList != null) {
                        ArmyVehicle armyVehicle = new ArmyVehicle();
                        armyVehicle.setName(inputName.getText().toString());
                        armyVehicle.setBDSimID(Integer.parseInt(inputSim.getText().toString()));
                        armyVehicle.save();
                        int currentPosition = 0;
                        if (recyclerView != null && recyclerView.getChildCount() > 0) {
                            try {
                                currentPosition = ((RecyclerView.LayoutParams) recyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                                Log.i("RecyclerView","For currentPosition------------->" + currentPosition);

                            } catch (Exception e) {

                            }
                        }
                        Log.i("RecyclerView","currentPosition------------->" + currentPosition);

                        adapter.addItem(currentPosition, armyVehicle);
//                        simpleAdapter.addItem(currentPosition+1,armyVehicle);

                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "已存在相同的北斗卡号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_back:
                this.finish();
                break;
            case R.id.item_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.ic_info_outline_red_a400_24dp);
                builder.setTitle("确定要清空车辆数据吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.deleteAll("ArmyVehicle", "");
                        armyVehicleList.clear();
                        adapter.updateDataSource(armyVehicleList);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
