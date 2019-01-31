package com.cetc.spinnertest;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cetc.spinnertest.showContacts.ArmyVehicleActivity;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    Spinner spinPlanets;
    Button btnContacts, btnShow, btnDelete, btnSave, btnShowVehicle;
    EditText etID, etName;
    TextView tvDB;
    Spinner spinVehicle;
    SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase db = LitePal.getDatabase();
        setContentView(R.layout.activity_main);
        spinPlanets = (Spinner) findViewById(R.id.spin_city);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPlanets.setAdapter(adapter);

        spinVehicle = (Spinner) findViewById(R.id.spin_vehicle);

        simpleAdapter = new SimpleAdapter(this, getData(), R.layout.item_vehicle_spin, new String[]{"img", "tvName", "tvId"},
                new int[]{R.id.iv_spin_icon, R.id.tv_spin_name, R.id.tv_spin_sim});

        simpleAdapter.setDropDownViewResource(R.layout.item_vehicle_spin);
        spinVehicle.setAdapter(simpleAdapter);
        spinVehicle.setOnItemSelectedListener(this);
        btnContacts = (Button) findViewById(R.id.btn_to_contacts);
        btnShow = (Button) findViewById(R.id.btn_show);
        tvDB = (TextView) findViewById(R.id.tv_vehicle);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnShowVehicle = (Button) findViewById(R.id.btn_show_vehicle);
        etID = (EditText) findViewById(R.id.et_id);
        etName = (EditText) findViewById(R.id.et_name);
//        SQLiteDatabase db = Connector.getDatabase();
//        ArmyVehicle armyVehicle1 = LitePal.find(ArmyVehicle.class, 1);
//        if (armyVehicle1 != null ) {
//            armyVehicle1.setName("车辆11一");
//            armyVehicle1.saveAsync();
//        } else {
//            armyVehicle1 = new ArmyVehicle();
//
//            armyVehicle1.setBDSimID(1);
//            armyVehicle1.setName("车辆11一");
//            armyVehicle1.saveAsync();
//        }

        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
                startActivity(intent);

//                for (int i = 1; i < 10; i++) {
//                    ArmyVehicle armyVehicle = new ArmyVehicle();
//                    armyVehicle.setName("车辆二" + i);
//
//                    armyVehicle.setBDSimID(i * 10 + i);
//                    armyVehicle.save();
//                }
//                ArmyVehicle armyVehicle = new ArmyVehicle();
//                armyVehicle.setName("车辆二" + 1);
//                armyVehicle.setBDSimID(11);
//                armyVehicle.save();
//                if (!armyVehicle.isSaved()) {
//                    Toast.makeText(MainActivity.this, "此ID已存在", Toast.LENGTH_SHORT).show();
//                }

            }
        });
        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {

            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {

            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArmyVehicle armyVehicle = LitePal.find(ArmyVehicle.class, 1);
                if (armyVehicle == null) {
                    Toast.makeText(MainActivity.this, "无此ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvDB.setText(armyVehicle.getName());
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArmyVehicle armyVehicle = LitePal.find(ArmyVehicle.class, 5);
                List<ArmyVehicle> armyVehicle1 = LitePal.where("BDSimID=?", "5").find(ArmyVehicle.class);
                if (armyVehicle == null) {
                    Toast.makeText(MainActivity.this, "无此ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                LitePal.delete(ArmyVehicle.class, 5);
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etID.getText().toString().equals("") && !"".equals(etName.getText().toString())) {
                    ArmyVehicle armyVehicle = new ArmyVehicle();
                    armyVehicle.setName(etName.getText().toString());
                    armyVehicle.setBDSimID(Integer.parseInt(etID.getText().toString()));
                    armyVehicle.setPosition(0);
                    armyVehicle.save();
//                    try {
//                        armyVehicle.saveThrows();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
                    if (!armyVehicle.isSaved()) {
                        Toast.makeText(MainActivity.this, "此ID已存在", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnShowVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArmyVehicleActivity.class);
                startActivity(intent);
            }
        });
    }

    public List<Map<String,Object>> getData() {
        List<ArmyVehicle> armyVehicleList = LitePal.where("bdsimid>?","0")
                .order("bdsimid asc")
                .find(ArmyVehicle.class);
        data = new ArrayList<>();
        if (armyVehicleList.size() == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", R.mipmap.vehicle);
            map.put("tvName", "未添加车辆编号");
            map.put("tvId", 0);
            data.add(map);
            return data;
        }

        for (ArmyVehicle armyVehicle : armyVehicleList
        ) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", R.mipmap.vehicle);
            map.put("tvName", armyVehicle.getName());
            map.put("tvId", armyVehicle.getBDSimID());
            data.add(map);
        }
        return data;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        simpleAdapter = new SimpleAdapter(this, getData(), R.layout.item_vehicle_spin, new String[]{"img", "tvName", "tvId"},
                new int[]{R.id.iv_spin_icon, R.id.tv_spin_name, R.id.tv_spin_sim});
        simpleAdapter.notifyDataSetChanged();
        spinVehicle.setAdapter(simpleAdapter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = (HashMap) simpleAdapter.getItem(position);
        int simId = ((Integer) map.get("tvId")).intValue();
        Toast.makeText(this, "simId" + simId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
