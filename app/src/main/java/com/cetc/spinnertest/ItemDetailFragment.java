package com.cetc.spinnertest;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cetc.spinnertest.dummy.DummyContent;

import org.litepal.LitePal;

import java.util.List;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ArmyVehicle armyVehicle;

    private EditText etID, etName;
    private TextView tvError;
    private Button btnUpdate, btnSave;
    private RecyclerView recyclerView;
    private String idStr;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            armyVehicle = (ArmyVehicle) getArguments().getSerializable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        etID = (EditText) rootView.findViewById(R.id.et_vehicle_id);
        etName = (EditText) rootView.findViewById(R.id.et_vehicle_name);
        tvError = (TextView) rootView.findViewById(R.id.tv_error);
        btnUpdate = rootView.findViewById(R.id.btn_item_update);
        btnSave= rootView.findViewById(R.id.btn_item_save);
        btnUpdate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        // Show the dummy content as text in a TextView.
        if (armyVehicle != null) {
//            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
//            ((TextView) rootView.findViewById(R.id.item_detail)).setText(armyVehicle.getName());
            etID.setText(String.valueOf(armyVehicle.getBDSimID()));
            etName.setText(armyVehicle.getName());
            idStr = String.valueOf(armyVehicle.getBDSimID());

        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_item_save:
                if (etID.length() < 6 || etID.length() > 8) {
                    tvError.setTextColor(Color.RED);
                    break;
                }
                tvError.setTextColor(Color.DKGRAY);
                armyVehicle.setName(etName.getText().toString());
                armyVehicle.setBDSimID(Integer.parseInt(etID.getText().toString()));
                if (!idStr.equals(etID.getText().toString())) {
                    List<ArmyVehicle> armyVehicle1 = LitePal.where("bdsimid=?", etID.getText().toString()).find(ArmyVehicle.class);
                    if (armyVehicle1.size() > 0) {
                        Toast.makeText(getActivity(), armyVehicle1.get(0).getName()+"已使用此号", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    armyVehicle.saveThrows();
                    if (armyVehicle.isSaved()) {
                        recyclerView = ((ItemListActivity) getActivity()).findViewById(R.id.item_list);
                        recyclerView.getAdapter().notifyItemChanged(armyVehicle.getPosition());
                        etID.setEnabled(false);
                        etName.setEnabled(false);
                    } else {
                        Toast.makeText(getActivity(), "此ID已存在", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 未修改ID，不需要其他操作
                    etID.setEnabled(false);
                    etName.setEnabled(false);
                }
                break;
            case R.id.btn_item_update:
                etID.setEnabled(true);
                tvError.setVisibility(View.VISIBLE);
//                etName.setEnabled(true);
                break;
            default:
                break;
        }
    }
}
