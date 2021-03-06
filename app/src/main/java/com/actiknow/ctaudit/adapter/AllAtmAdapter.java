package com.actiknow.ctaudit.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.ctaudit.R;
import com.actiknow.ctaudit.activity.AtmDetailActivity;
import com.actiknow.ctaudit.activity.MainActivity;
import com.actiknow.ctaudit.model.Atm;
import com.actiknow.ctaudit.utils.Constants;
import com.actiknow.ctaudit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AllAtmAdapter extends RecyclerView.Adapter<AllAtmAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Atm> atmList = new ArrayList<Atm> ();
    private Typeface typeface;

    public AllAtmAdapter (Activity activity, List<Atm> atmList) {
        this.activity = activity;
        this.atmList = atmList;
        typeface = Typeface.createFromAsset (activity.getAssets (), "Kozuka-Gothic.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.listview_item_atm, parent, false);
        return new ViewHolder (sView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Atm atm = atmList.get (position);
        holder.atm_last_audit_date.setTypeface (typeface);
        holder.atm_atm_unique_id.setTypeface (typeface);
        holder.atm_bank_name.setTypeface (typeface);
        holder.atm_address.setTypeface (typeface);
        holder.atm_city.setTypeface (typeface);
        holder.atm_pincode.setTypeface (typeface);
        holder.atm_last_audit_date.setText (atm.getAtm_last_audit_date ());
        holder.atm_atm_unique_id.setText (atm.getAtm_unique_id ().toUpperCase ());
        holder.atm_bank_name.setText (atm.getAtm_bank_name ().toUpperCase ());
        holder.atm_address.setText (atm.getAtm_address ().toUpperCase ());
        holder.atm_city.setText (atm.getAtm_city ().toUpperCase ());
        holder.atm_pincode.setText (atm.getAtm_pincode ().toUpperCase ());
    }

    @Override
    public int getItemCount () {
        return atmList.size ();
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public boolean checkPermissions () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions (new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.PERMISSION_REQUEST_CODE);
                return false;
            } else if (activity.checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions (new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
                return false;
            } else if (activity.checkSelfPermission (Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions (new String[] {Manifest.permission.CAMERA}, MainActivity.PERMISSION_REQUEST_CODE);
                return false;
            }
/*
            if (activity.checkSelfPermission (Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
					activity.checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
					activity.checkSelfPermission (Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
					activity.checkSelfPermission (Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
					activity.checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

				activity.requestPermissions (new String[] {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
								Manifest.permission.INTERNET, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WRITE_EXTERNAL_STORAGE},
						PERMISSION_REQUEST_CODE);
                return false;

*/
            else {
                return true;
            }
/*
            if (checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.INTERNET}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.RECEIVE_BOOT_COMPLETED,}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            }
  */

        } else {
            return true;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView vName, vSex, vId, vAge;

        TextView atm_last_audit_date;
        TextView atm_atm_unique_id;
        TextView atm_bank_name;
        TextView atm_address;
        TextView atm_city;
        TextView atm_pincode;

        public ViewHolder (View view) {
            super (view);
            atm_last_audit_date = (TextView) view.findViewById (R.id.tvLastAuditDate);
            atm_atm_unique_id = (TextView) view.findViewById (R.id.tvAtmUniqueId);
            atm_bank_name = (TextView) view.findViewById (R.id.tvBankName);
            atm_address = (TextView) view.findViewById (R.id.tvAddress);
            atm_city = (TextView) view.findViewById (R.id.tvCity);
            atm_pincode = (TextView) view.findViewById (R.id.tvPincode);

            view.setOnClickListener (this);
        }

        @Override
        public void onClick (View v) {
            final Atm atm = atmList.get (getLayoutPosition ());
            if (checkPermissions ()) {
                Constants.report.setAtm_id (atm.getAtm_id ());
                Constants.report.setAuditor_id (Constants.auditor_id_main);
                Constants.report.setAgency_id (atm.getAtm_agency_id ());
                Constants.report.setAtm_unique_id (atm.getAtm_unique_id ().toUpperCase ());
                Constants.report.setLatitude (String.valueOf (Constants.latitude));
                Constants.report.setLongitude (String.valueOf (Constants.longitude));

                Intent intent = new Intent (activity, AtmDetailActivity.class);
                activity.startActivity (intent);
                activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                Utils.showToast (activity, "Permissions are required for proceeding further");
            }
        }
    }
}