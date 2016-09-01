package com.actiknow.ctaudit.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.actiknow.ctaudit.R;
import com.actiknow.ctaudit.activity.AllQuestionListActivity;
import com.actiknow.ctaudit.model.Question;
import com.actiknow.ctaudit.model.Response;
import com.actiknow.ctaudit.utils.AppConfigTags;
import com.actiknow.ctaudit.utils.Constants;
import com.actiknow.ctaudit.utils.Utils;

import java.util.List;

public class AllQuestionsAdapter extends BaseAdapter {
    Dialog dialogEnterComment;
    Typeface font;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Question> questionList;
    public AllQuestionsAdapter (Activity activity, List<Question> questionList) {
        this.activity = activity;
        this.questionList = questionList;
        font = Typeface.createFromAsset (activity.getAssets (), "Kozuka-Gothic.ttf");
    }

    @Override
    public int getCount () {
        return questionList.size ();
    }

    @Override
    public Object getItem (int location) {
        return questionList.get (location);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        final QuestionViewHolder holder;
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null) {
            convertView = inflater.inflate (R.layout.listview_item_question, null);
            holder = new QuestionViewHolder ();
            holder.tvQuestionText = (TextView) convertView.findViewById (R.id.tvQuestionInList);
        holder.tvImage = (TextView) convertView.findViewById (R.id.tvImageInList);
        holder.tvComments = (TextView) convertView.findViewById (R.id.tvCommentInList);
            holder.switchYesNo = (Switch) convertView.findViewById (R.id.switchYesNoInList);
//            convertView.setTag (holder);
//        } else {
//            holder = (QuestionViewHolder) convertView.getTag ();
//        }

        holder.tvQuestionText.setTypeface (font);
        holder.tvComments.setTypeface (font);
        holder.tvImage.setTypeface (font);

        holder.switchYesNo.setTextOn ("Yes");
        holder.switchYesNo.setTextOff ("No");
        final Question question = questionList.get (position);

        try {

            for (int i = 0; i < Constants.questionsList.size (); i++) {
                final Response response;
                response = Constants.responseList.get (i);
                if (question.getQuestion_id () == response.getQuestion_id ()) {
                    if (response.getSwitch_flag () == 0)
                        holder.switchYesNo.setChecked (false);
                    else
                        holder.switchYesNo.setChecked (true);

                    if (response.getImage1 ().length () != 0) {
                        holder.tvImage.setTextColor (Color.parseColor ("#311b92"));
                        holder.tvImage.setCompoundDrawablesWithIntrinsicBounds (R.drawable.ic_camera_selected, 0, 0, 0);
                    }
                    if (response.getComment ().length () != 0) {
                        if (i == 0 && (response.getComment ().length () == Constants.atm_location_in_manual.length ())) {

                        } else {
                            holder.tvComments.setTextColor (Color.parseColor ("#311b92"));
                            holder.tvComments.setCompoundDrawablesWithIntrinsicBounds (R.drawable.ic_comment_selected, 0, 0, 0);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Utils.showLog (Log.ERROR, "Exception found", "" + e.getMessage (), true);
        }



//        Utils.setTypefaceToAllViews (activity, holder.tvQuestionText);


        holder.switchYesNo.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        for (int i = 0; i < Constants.questionsList.size (); i++) {
                            final Response response;
                            response = Constants.responseList.get (i);
                            if (question.getQuestion_id () == response.getQuestion_id ()) {
                                response.setSwitch_flag (1);
                            }
                        }
                    } else {
                        for (int i = 0; i < Constants.questionsList.size (); i++) {
                            final Response response;
                            response = Constants.responseList.get (i);
                            if (question.getQuestion_id () == response.getQuestion_id ()) {
                                response.setSwitch_flag (0);
                            }
                        }
                    }
                    Utils.setSeekBar (AllQuestionListActivity.sbRating, AllQuestionListActivity.tvRatingNumber);
                } catch (IndexOutOfBoundsException e) {
                    Utils.showLog (Log.ERROR, "Exception found", "" + e.getMessage (), true);
                }
            }
        });

        holder.tvImage.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent mIntent = null;
                if (Utils.isPackageExists (activity, "com.google.android.camera")) {
                    mIntent = new Intent ();
                    mIntent.setPackage ("com.google.android.camera");
                    mIntent.setAction (android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
                    mIntent.setAction (android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
                if (mIntent.resolveActivity (activity.getPackageManager ()) != null)
                    activity.startActivityForResult (mIntent, question.getQuestion_id ());

                holder.tvImage.setTextColor (Color.parseColor ("#311b92"));
                holder.tvImage.setCompoundDrawablesWithIntrinsicBounds (R.drawable.ic_camera_selected, 0, 0, 0);
            }
        });


        holder.tvComments.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Button btEnterComment;
                final EditText etEnterComment;
                Response response = new Response ();
                response = Constants.responseList.get (position);
                dialogEnterComment = new Dialog (activity);
                dialogEnterComment.setContentView (R.layout.dialog_enter_comment);
                dialogEnterComment.setCancelable (true);
                btEnterComment = (Button) dialogEnterComment.findViewById (R.id.btEnterComment);
                etEnterComment = (EditText) dialogEnterComment.findViewById (R.id.etEnterComment);
                if (position == 0)
                    etEnterComment.setText (response.getComment ().replace (Constants.atm_location_in_manual, ""));
                else
                    etEnterComment.setText (response.getComment ());

                Utils.setTypefaceToAllViews (activity, etEnterComment);
                dialogEnterComment.getWindow ().setBackgroundDrawable (new ColorDrawable (android.graphics.Color.TRANSPARENT));
                dialogEnterComment.show ();
                btEnterComment.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        for (int i = 0; i < Constants.questionsList.size (); i++) {
                            final Response response;
                            response = Constants.responseList.get (i);
                            //   etEnterComment.setText (response.getResponse_comment ());
                            if (question.getQuestion_id () == response.getQuestion_id ()) {
                                if (position == 0 && Constants.atm_location_in_manual.length () != 0)
                                    response.setComment (etEnterComment.getText ().toString () + " " + Constants.atm_location_in_manual);
                                else
                                    response.setComment (etEnterComment.getText ().toString ());
                            }
                        }
                        dialogEnterComment.dismiss ();
                    }
                });
                holder.tvComments.setTextColor (Color.parseColor ("#311b92"));
                holder.tvComments.setCompoundDrawablesWithIntrinsicBounds (R.drawable.ic_comment_selected, 0, 0, 0);
            }
        });

        holder.tvQuestionText.setText (question.getQuestion ());
        return convertView;
    }

    static class QuestionViewHolder {
        TextView tvQuestionText;
        Switch switchYesNo;
        TextView tvImage;
        TextView tvComments;
    }

}