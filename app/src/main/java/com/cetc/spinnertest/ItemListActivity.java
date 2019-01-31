package com.cetc.spinnertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cetc.spinnertest.dummy.DummyContent;

import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private String m_Text = "";
    private boolean mTwoPane;
    private static final String TAG = "ItemListActivity";
    List<ArmyVehicle> armyVehicleList;
    private DividerItemDecoration mDecoration;
    RecyclerView recyclerView;
    SimpleItemRecyclerViewAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        SQLiteDatabase db = LitePal.getDatabase();

        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                ArmyVehicle armyVehicle = new ArmyVehicle();
                armyVehicle.setName("车辆一" );
                armyVehicle.setBDSimID(233536);
                armyVehicle.save();
                ArmyVehicle armyVehicle1 = new ArmyVehicle();
                armyVehicle1.setName("车辆二");

                armyVehicle1.setBDSimID(235678);
                armyVehicle1.save();
                if (armyVehicle1.isSaved()) {
                    Log.i(TAG, "保存失败");

                }
                Log.i(TAG, "创建数据库成功");

            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {

            }
        });

        ArmyVehicle armyVehicle = new ArmyVehicle();
        armyVehicle.setName("车辆一" );
        armyVehicle.setBDSimID(233536);
        armyVehicle.save();

        armyVehicle.setName("车辆二");

        armyVehicle.setBDSimID(235678);
        armyVehicle.saveAsync();
        Log.i(TAG, "创建数据库成功");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                showNewDialog(ItemListActivity.this);


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        mDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        armyVehicleList = LitePal.where("id>?", "0").find(ArmyVehicle.class);
        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        assert recyclerView != null;
         recyclerView.addItemDecoration(mDecoration);
         recyclerView.setItemAnimator(new DefaultItemAnimator());
         simpleAdapter = new SimpleItemRecyclerViewAdapter(this, armyVehicleList, mTwoPane);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void showNewDialog(final Context context) {
        LayoutInflater inflater = ItemListActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_item, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ItemListActivity.this);
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

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
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

                        simpleAdapter.addItem(currentPosition+1,armyVehicle);

                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "已存在相同的北斗卡号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, armyVehicleList, mTwoPane));
        recyclerView.setAdapter(simpleAdapter);

    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
//        private final List<DummyContent.DummyItem> mValues;
        private final List<ArmyVehicle> mVehicleList;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                ArmyVehicle item = (ArmyVehicle) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(item.getBDSimID()));
                    arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID,item);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(item.getBDSimID()));

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<ArmyVehicle> items,
                                      boolean twoPane) {
//                                                  List<DummyContent.DummyItem> items,
            mVehicleList = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_vehicle, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mContentView.setText(mVehicleList.get(position).getName());
            holder.mIdView.setText(String.valueOf(mVehicleList.get(position).getBDSimID()));

            mVehicleList.get(position).setPosition(position);

            holder.itemView.setTag(mVehicleList.get(position));

//            holder.itemView.setTag(mVehicleList.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeItem(position);
                    return true;
                }
            });
        }

        public void addItem(int position,ArmyVehicle armyVehicle) {
            mVehicleList.add(position, armyVehicle);
            notifyItemInserted(position);
        }

        private void removeItem(int position) {
            mVehicleList.remove(position);
            notifyItemRemoved(position);
        }
        @Override
        public int getItemCount() {
            return mVehicleList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mIcon;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.tv_sim);
                mContentView = (TextView) view.findViewById(R.id.tv_name);
                mIcon = (ImageView) view.findViewById(R.id.iv_icon);
            }
        }
    }
}
