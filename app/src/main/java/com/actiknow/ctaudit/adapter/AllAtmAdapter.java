package com.actiknow.ctaudit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.actiknow.ctaudit.R;
import com.actiknow.ctaudit.activity.AtmDetailActivity;
import com.actiknow.ctaudit.model.Atm;
import com.actiknow.ctaudit.utils.Constants;
import com.actiknow.ctaudit.utils.Utils;

import java.util.List;

public class AllAtmAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Atm> atmList;

	public AllAtmAdapter (Activity activity, List<Atm> atmList) {
		this.activity = activity;
		this.atmList = atmList;
	}

	@Override
	public int getCount() {
		return atmList.size ();
	}

	@Override
	public Object getItem(int location) {
		return atmList.get (location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate (R.layout.listview_item_atm, null);
			holder = new ViewHolder ();
			holder.atm_last_audit_date = (TextView) convertView.findViewById (R.id.tvLastAuditDate);
			holder.atm_atm_unique_id = (TextView) convertView.findViewById (R.id.tvAtmUniqueId);
			holder.atm_bank_name = (TextView) convertView.findViewById (R.id.tvBankName);
			holder.atm_address = (TextView) convertView.findViewById (R.id.tvAddress);
			holder.atm_city = (TextView) convertView.findViewById (R.id.tvCity);
			holder.atm_pincode = (TextView) convertView.findViewById (R.id.tvPincode);
			convertView.setTag (holder);
		} else
			holder = (ViewHolder) convertView.getTag ();

		Utils.setTypefaceToAllViews (activity, holder.atm_pincode);

		final Atm atm = atmList.get (position);
		holder.atm_last_audit_date.setText (atm.getAtm_last_audit_date ());
		holder.atm_atm_unique_id.setText (atm.getAtm_unique_id ().toUpperCase ());
		holder.atm_bank_name.setText (atm.getAtm_bank_name ().toUpperCase ());
		holder.atm_address.setText (atm.getAtm_address ().toUpperCase ());
		holder.atm_city.setText (atm.getAtm_city ().toUpperCase ());
		holder.atm_pincode.setText (atm.getAtm_pincode ().toUpperCase ());

		convertView.setOnClickListener (new View.OnClickListener () {
			private void die () {
			}

			@Override
			public void onClick (View arg0) {
//				Constants.atm_unique_id = atm.getAtm_unique_id ().toUpperCase ();
//				Constants.atm_agency_id = atm.getAtm_agency_id ();

				Constants.report.setAtm_id (atm.getAtm_id ());
				Constants.report.setAuditor_id (Constants.auditor_id_main);
				Constants.report.setAgency_id (atm.getAtm_agency_id ());
				Constants.report.setAtm_unique_id (atm.getAtm_unique_id ().toUpperCase ());
				Constants.report.setLatitude (String.valueOf (Constants.latitude));
				Constants.report.setLongitude (String.valueOf (Constants.longitude));

				Intent intent = new Intent (activity, AtmDetailActivity.class);
				activity.startActivity (intent);
				activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);



/*
				AlertDialog.Builder builder = new AlertDialog.Builder (activity);
				builder.setMessage ("Please take an image of the ATM Machine\nNote : This image will be Geotagged")
						.setCancelable (false)
						.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
							public void onClick (DialogInterface dialog, int id) {
								dialog.dismiss ();
								Intent mIntent = null;
								if (Utils.isPackageExists (activity, "com.google.android.camera")) {
									mIntent = new Intent ();
									mIntent.setPackage ("com.google.android.camera");
									mIntent.setAction (MediaStore.ACTION_IMAGE_CAPTURE);
								} else {
									PackageManager packageManager = activity.getPackageManager ();
									String defaultCameraPackage = null;
									List<ApplicationInfo> list = packageManager.getInstalledApplications (PackageManager.GET_UNINSTALLED_PACKAGES);
									for (int n = 0; n < list.size (); n++) {
										if ((list.get (n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
											Utils.showLog (Log.DEBUG, AppConfigTags.TAG, "Installed Applications  : " + list.get (n).loadLabel (packageManager).toString (), false);
											Utils.showLog (Log.DEBUG, AppConfigTags.TAG, "package name  : " + list.get (n).packageName, false);
											if (list.get (n).loadLabel (packageManager).toString ().equalsIgnoreCase ("Camera")) {
												defaultCameraPackage = list.get (n).packageName;
												break;
											}
										}
									}
									mIntent = new Intent ();
									mIntent.setPackage (defaultCameraPackage);
									mIntent.setAction (MediaStore.ACTION_IMAGE_CAPTURE);
									File f = new File (Environment.getExternalStorageDirectory () + File.separator + "img.jpg");
									mIntent.putExtra (MediaStore.EXTRA_OUTPUT, Uri.fromFile (f));
								}
								if (mIntent.resolveActivity (activity.getPackageManager ()) != null)
									activity.startActivityForResult (mIntent, MainActivity.GEO_IMAGE_REQUEST_CODE);
							}
						})
						.setNegativeButton ("CANCEL", new DialogInterface.OnClickListener () {
							public void onClick (DialogInterface dialog, int id) {
								dialog.dismiss ();
							}
						});
				AlertDialog alert = builder.create ();
				alert.show ();

*/
				/*


				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (activity.checkSelfPermission (Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        activity.requestPermissions (new String[] {Manifest.permission.CAMERA},
		                        MainActivity.GEO_IMAGE_REQUEST_CODE_1);
                    } else {
					}
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder (activity);
					builder.setMessage ("Please take an image of the ATM Machine\nNote : This image will be Geotagged")
							.setCancelable (false)
							.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
								public void onClick (DialogInterface dialog, int id) {
									dialog.dismiss ();
									Intent mIntent = null;
									if (Utils.isPackageExists (activity, "com.google.android.camera")) {
										mIntent = new Intent ();
										mIntent.setPackage ("com.google.android.camera");
										mIntent.setAction (MediaStore.ACTION_IMAGE_CAPTURE);
									} else {
										PackageManager packageManager = activity.getPackageManager ();
										String defaultCameraPackage = null;
										List<ApplicationInfo> list = packageManager.getInstalledApplications (PackageManager.GET_UNINSTALLED_PACKAGES);
										for (int n = 0; n < list.size (); n++) {
											if ((list.get (n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
												Utils.showLog (Log.DEBUG, AppConfigTags.TAG, "Installed Applications  : " + list.get (n).loadLabel (packageManager).toString (), false);
												Utils.showLog (Log.DEBUG, AppConfigTags.TAG, "package name  : " + list.get (n).packageName, false);
												if (list.get (n).loadLabel (packageManager).toString ().equalsIgnoreCase ("Camera")) {
													defaultCameraPackage = list.get (n).packageName;
													break;
												}
											}
										}
										mIntent = new Intent ();
										mIntent.setPackage (defaultCameraPackage);
										mIntent.setAction (MediaStore.ACTION_IMAGE_CAPTURE);
										File f = new File (Environment.getExternalStorageDirectory () + File.separator + "img.jpg");
										mIntent.putExtra (MediaStore.EXTRA_OUTPUT, Uri.fromFile (f));
									}
									if (mIntent.resolveActivity (activity.getPackageManager ()) != null)
										activity.startActivityForResult (mIntent, MainActivity.GEO_IMAGE_REQUEST_CODE);
								}
							})
							.setNegativeButton ("CANCEL", new DialogInterface.OnClickListener () {
								public void onClick (DialogInterface dialog, int id) {
									dialog.dismiss ();
								}
							});
					AlertDialog alert = builder.create ();
					alert.show ();



				}

*/
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView atm_last_audit_date;
		TextView atm_atm_unique_id;
		TextView atm_bank_name;
		TextView atm_address;
		TextView atm_city;
		TextView atm_pincode;
	}


}