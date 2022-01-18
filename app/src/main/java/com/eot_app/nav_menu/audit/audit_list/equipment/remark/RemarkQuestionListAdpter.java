package com.eot_app.nav_menu.audit.audit_list.equipment.remark;

import static com.eot_app.utility.AppUtility.updateTime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.customform.MyAttachment;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.MyFormInterFace;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.OptionModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesRspncModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.DropdownListBean;
import com.eot_app.utility.SignatureView;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyAdapter;
import com.eot_app.utility.util_interfaces.NoDefaultSpinner;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sonam-11 on 17/12/20.
 */
public class RemarkQuestionListAdpter extends RecyclerView.Adapter<RemarkQuestionListAdpter.ViewHolder> {
    ArrayList<QuesRspncModel> typeList;
    ArrayList<QuesRspncModel> duplicateCopy = new ArrayList<>();
    Context context;
    MyFormInterFace myFormInterFace;
    boolean isLoadtoBottom = false;
    String time = "", date = "";
    //   int count = 0;

    private MyAttachment myAttachment;


    public RemarkQuestionListAdpter(ArrayList<QuesRspncModel> typeList, Context context, MyFormInterFace myFormInterFace) {
        this.context = context;
        this.typeList = typeList;
        if (this.typeList != null)
            duplicateCopy.addAll(typeList);
        this.myFormInterFace = myFormInterFace;
        this.isLoadtoBottom = false;

        String currentDateTime = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a",
                "dd-MMM-yyyy kk:mm"));
        String[] currentDateTimeArry = currentDateTime.split(" ");
        date = currentDateTimeArry[0];
        time = currentDateTimeArry[1] + " " + currentDateTimeArry[2];
        this.myAttachment = myAttachment;
        setIndex();
    }

    public RemarkQuestionListAdpter(ArrayList<QuesRspncModel> typeList, Context context, MyFormInterFace myFormInterFace,
                                    MyAttachment myAttachment) {
        this.context = context;
        this.typeList = typeList;
        if (this.typeList != null)
            duplicateCopy.addAll(typeList);
        this.myFormInterFace = myFormInterFace;
        this.isLoadtoBottom = false;
        String currentDateTime = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a",
                "dd-MMM-yyyy kk:mm"));
        String[] currentDateTimeArry = currentDateTime.split(" ");
        date = currentDateTimeArry[0];

        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                time = currentDateTimeArry[1] + " " + currentDateTimeArry[2];
            else time = currentDateTimeArry[1] + "";

        } catch (Exception e) {
            e.getMessage();
        }

        this.myAttachment = myAttachment;
        setIndex();
    }

    private void setIndex() {
        if (typeList != null && typeList.size() > 0) {
            int i = 0;
            for (QuesRspncModel custOmFormQuestionsRes : typeList) {
                if (!custOmFormQuestionsRes.getType().equals("9")) {
                    i++;
                    custOmFormQuestionsRes.setIndex(i);
                } else custOmFormQuestionsRes.setIndex(0);

            }
        }
    }

    public ArrayList<QuesRspncModel> getDuplicateCopy() {
        return duplicateCopy;
    }

    public ArrayList<QuesRspncModel> getTypeList() {
        return typeList;
    }

    @Override
    public RemarkQuestionListAdpter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.remark_quest_adpter_lay, parent, false);
        RemarkQuestionListAdpter.ViewHolder viewHolder = new RemarkQuestionListAdpter.ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RemarkQuestionListAdpter.ViewHolder holder, final int position) {

        holder.tvQuestion.setVisibility(View.VISIBLE);
        if (typeList.get(position).getMandatory().equals("1")) {
            holder.tvQuestion.setText(typeList.get(position).getDes() + " *");
        } else {
            holder.tvQuestion.setText(typeList.get(position).getDes());
        }

        try {
            holder.tvQuestion.setTypeface(holder.tvQuestion.getTypeface(), Typeface.BOLD);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        switch (typeList.get(position).getType()) {
            /*****type 1 for text type quest*/
            case "1":
                holder.type_text.setVisibility(View.VISIBLE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);


                holder.type_text.setTag(position);
                if (typeList.get(position).getAns().isEmpty())
                    holder.type_text.setText("");
                else if (typeList.get(position).getAns().size() > 0)
                    holder.type_text.setText(typeList.get(position).getAns().get(0).getValue());


                break;
            /******TextArea***/
            case "2":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.VISIBLE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);
                holder.type_text_area.setTag(position);


                if (typeList.get(position).getAns().isEmpty())
                    holder.type_text_area.setText("");
                else if (typeList.get(position).getAns().size() > 0)
                    holder.type_text_area.setText(typeList.get(position).getAns().get(0).getValue());

                break;
            /****checkbox type***/
            case "3":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.VISIBLE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);

                setCheckBoxOption(holder, position);

                break;
            /***dropdown type****/
            case "4":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.VISIBLE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);

                setDropDownOptions(holder, position);
                break;
            /***date type****/
            case "5":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.VISIBLE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);


                holder.tvDate.setTag(position);
                if (typeList.get(position).getAns().isEmpty())
                    holder.tvDate.setText("");
                else if (typeList.get(position).getAns().size() > 0) {
                    try {
                        if (!(typeList.get(position).getAns().get(0).getValue()).equals("")) {
                            String[] dateConvert = AppUtility.getFormatedTime(typeList.get(position).
                                    getAns().get(0).getValue());
                            String s = dateConvert[0];
                            String[] date = s.split(",");
                            holder.tvDate.setText(date[1].trim().replace(" ", "-"));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            /*****Time type****/
            case "6":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.VISIBLE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);

                holder.tvTime.setTag(position);
                if (typeList.get(position).getAns().isEmpty())
                    holder.tvTime.setText("");
                else if (typeList.get(position).getAns().size() > 0) {
                    try {
                        if (!(typeList.get(position).getAns().get(0).
                                getValue().equals(""))) {
                            String time = AppUtility.getDateWithFormate2((Long.parseLong(typeList.get(position).
                                            getAns().get(0).
                                            getValue()) * 1000),
                                    AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm"));
                            holder.tvTime.setText(time);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            /*****Date Time Type****/
            case "7":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.VISIBLE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);

                holder.tvTimeDate.setTag(position);
                if (typeList.get(position).getAns().isEmpty())
                    holder.tvTimeDate.setText("");
                else if (typeList.get(position).getAns().size() > 0) {
                    try {
                        if (!typeList.get(position).getAns().get(0).getValue().equals("")) {
                            Long dateLong = Long.parseLong(typeList.get(position).getAns().get(0).getValue());
                            String dateConvert = AppUtility.getDate(dateLong,
                                    AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm"));
                            holder.tvTimeDate.setText(dateConvert);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                break;
            /***CheckBox for Only single option (True/false) ****/
            case "8":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.VISIBLE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);


                holder.tvQuestion.setVisibility(View.GONE);
                if (typeList.get(position).getMandatory().equals("1")) {
                    holder.checkbox_single.setText(typeList.get(position).getDes() + " *");
                } else {
                    holder.checkbox_single.setText(typeList.get(position).getDes());
                }

                if (typeList.get(position).getAns() != null && typeList.get(position).getAns().size() > 0) {
                    holder.checkbox_single.setChecked(typeList.get(position).getAns().get(0).getValue().equals("1"));

                }


                holder.checkbox_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (typeList.get(position).getAns() != null && typeList.get(position).getAns().size() > 0) {
                            if (typeList.get(position).getAns().get(0).getValue().equals("1"))
                                typeList.get(position).getAns().get(0).setValue("0");
                            else typeList.get(position).getAns().get(0).setValue("1");
                        } else {
                            typeList.get(position).getAns().add(new AnswerModel("0", "1"));
                        }
                    }
                });
                break;
            /***Show Only Lable****/
            case "9":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                try {
                    holder.tvQuestion.setTypeface(holder.tvQuestion.getTypeface(), Typeface.BOLD);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                holder.tvQuestion.setVisibility(View.VISIBLE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);
                break;
            /***For Signature***/
            case "10":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.VISIBLE);
                holder.Attchment_layout.setVisibility(View.GONE);
                holder.add_sign_lable.setText(holder.que_no.getText().toString() + " " + holder.tvQuestion.getText().toString());
                holder.que_no.setText("");
                holder.tvQuestion.setText("");
                holder.add_sign.setTag(position);
                holder.delete_sign.setTag(position);


                if (typeList.get(position).getAns().size() > 0 && !typeList.get(position).getAns().get(0).getValue().isEmpty()) {
                    holder.signature_set.setVisibility(View.VISIBLE);
                    holder.delete_sign.setVisibility(View.VISIBLE);

                    Glide.with(context).load(App_preference.getSharedprefInstance().getBaseURL() +
                            typeList.get(position).getAns().get(0).getValue())
                            .placeholder(R.drawable.picture).into(holder.signature_set);
                    holder.signature_set.setScaleType(ImageView.ScaleType.FIT_XY);

                } else {
                    holder.add_sign.setVisibility(View.VISIBLE);
                    holder.delete_sign.setVisibility(View.GONE);
                }
                holder.signature_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            //check the url has local selected attachment if yes than we are return not redirect and open it.
                            boolean exists = new File(typeList.get(position).getAns().get(0).getValue()).exists();
                            if (exists) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        App_preference.getSharedprefInstance().getBaseURL() +
                                                typeList.get(position).getAns().get(0).getValue()));
                        context.startActivity(browserIntent);

                    }
                });


                break;
            /***For Attachment***/
            case "11":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.VISIBLE);
                holder.Attchment_lable.setText(holder.que_no.getText().toString() + " " + holder.tvQuestion.getText().toString());
                holder.que_no.setText("");
                holder.tvQuestion.setText("");
                holder.buttonAttchment.setTag(position);
                holder.delete_attchment.setTag(position);

                if (typeList.get(position).getAns().size() > 0 && !typeList.get(position).getAns().get(0).getValue().isEmpty()) {
                    final String ext = typeList.get(position).getAns().get(0).getValue().
                            substring((typeList.get(position).getAns().get(0).getValue().lastIndexOf(".")) + 1);
                    holder.attchment_set.setVisibility(View.VISIBLE);
                    holder.delete_attchment.setVisibility(View.VISIBLE);
                    holder.buttonAttchment.setVisibility(View.GONE);
                    //  holder.buttonAttchment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit_attchment));
                    if (!ext.isEmpty()) {
                        if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
                            Glide.with(context).load(App_preference.getSharedprefInstance().getBaseURL()
                                    + typeList.get(position).getAns().get(0).getValue())
                                    .placeholder(R.drawable.picture).into(holder.attchment_set);
                            holder.attchment_set.setScaleType(ImageView.ScaleType.FIT_XY);
                        } else if (ext.equals("doc") || ext.equals("docx")) {
                            holder.attchment_set.setImageResource(R.drawable.word);
                            holder.attchment_set.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                        } else if (ext.equals("pdf")) {
                            holder.attchment_set.setImageResource(R.drawable.pdf);
                            holder.attchment_set.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                        } else if (ext.equals("xlsx") || ext.equals("xls")) {
                            holder.attchment_set.setImageResource(R.drawable.excel);
                            holder.attchment_set.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        } else if (ext.equals("csv")) {
                            holder.attchment_set.setImageResource(R.drawable.csv);
                            holder.attchment_set.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        } else {
                            holder.attchment_set.setImageResource(R.drawable.doc);
                            holder.attchment_set.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        }
                    }
                } else {
                    holder.buttonAttchment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_attachment));
                    holder.delete_attchment.setVisibility(View.GONE);
                    holder.attchment_set.setVisibility(View.GONE);
                }

                holder.attchment_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //check the url has local selected attachment if yes than we are return not redirect and open it.
                        try {
                            boolean exists = new File(typeList.get(position).getAns().get(0).getValue()).exists();
                            if (exists) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(App_preference.getSharedprefInstance().getBaseURL() +
                                        typeList.get(position).getAns().get(0).getValue()));
                        context.startActivity(browserIntent);
                    }
                });


                break;
            default: {
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
                holder.signature_layout.setVisibility(View.GONE);
                holder.Attchment_layout.setVisibility(View.GONE);
            }
        }

    }


    private void setCheckBoxOption(RemarkQuestionListAdpter.ViewHolder holder, final int position) {
        if (holder.linearCheck.getChildCount() > 0)
            holder.linearCheck.removeAllViews();

        for (final OptionModel optionModel : typeList.get(position).getOpt()) {
            final CheckBox checkBox = new CheckBox(context);
            checkBox.setText("");
            checkBox.setText(optionModel.getValue());
            checkBox.setTag(optionModel);


            checkBox.setTextAppearance(context, R.style.header_text_style);
            checkBox.setTypeface(ResourcesCompat.getFont(context, R.font.arimo_regular));
            checkBox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.txt_sub_color)));

            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.linearCheck.addView(checkBox);

            if (!typeList.get(position).getAns().isEmpty()) {
                for (final AnswerModel answerModel : typeList.get(position).getAns()) {
                    if (optionModel.getKey().equals(answerModel.getKey())) {
                        checkBox.setChecked(true);
                        break;
                    }
                }
            } else checkBox.setChecked(false);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    OptionModel selectedOptionModel = ((OptionModel) compoundButton.getTag());

                    if (compoundButton.isChecked()) {
                        if (((OptionModel) compoundButton.getTag()).getOptHaveChild().equals("1")) {
                            myFormInterFace.getAnsId(optionModel.getKey()); // its pending work
                        }
                        if (typeList.get(position).getAns() != null && typeList.get(position).getAns().size() > 0) {
                            List<AnswerModel> ans = typeList.get(position).getAns();
                            for (AnswerModel ansmodel : ans)
                                if (ansmodel.getKey().equals(selectedOptionModel.getKey()))
                                    return;
                            ans.add(new AnswerModel(selectedOptionModel.getKey(), selectedOptionModel.getValue()));
                            typeList.get(position).setAns(ans);
                        } else {
                            List<AnswerModel> answerModelList = new ArrayList<>();
                            answerModelList.add(new AnswerModel(selectedOptionModel.getKey(), selectedOptionModel.getValue()));
                            typeList.get(position).setAns(answerModelList);
                        }
                    } else {
                        if (typeList.get(position).getAns() != null && typeList.get(position).getAns().size() > 0) {
                            List<AnswerModel> ans = typeList.get(position).getAns();
                            for (AnswerModel ansmodel : ans)
                                if (ansmodel.getKey().equals(selectedOptionModel.getKey())) {
                                    ans.remove(ansmodel);
                                    typeList.get(position).setAns(ans);
                                    return;
                                }
                        }
                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    public void setDropDownOptions(final RemarkQuestionListAdpter.ViewHolder holder, final int position) {
        if (typeList.get(position).getAns().isEmpty())
            holder.spinner_text.setText("");
        else if (typeList.get(position).getAns().size() > 0)
            holder.spinner_text.setText(typeList.get(position).getAns().get(0).getValue());


        final MyAdapter<OptionModel> spinnerAdapter = new MyAdapter<>(context, R.layout.custom_adapter_item_layout, typeList.get(position).getOpt());
        holder.spinner.setAdapter(spinnerAdapter);


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                List<AnswerModel> ans = new ArrayList<>();

                ans.add(new AnswerModel(((DropdownListBean) parent.getItemAtPosition(pos)).getKey(),
                        ((DropdownListBean) parent.getItemAtPosition(pos)).getName()));

                typeList.get(position).setAns(ans);


                String text = ((DropdownListBean) parent.getItemAtPosition(pos)).getName();
                holder.spinner_text.setText(text);

                if (((OptionModel) parent.getItemAtPosition(pos)).getOptHaveChild().equals("1")) {
                    myFormInterFace.getAnsId(((DropdownListBean) parent.getItemAtPosition(pos)).getKey());
                }
                //  myFormInterFace.removeAnswers((OptionModel) parent.getItemAtPosition(pos));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    /**
     * Take time from picker for Question Type 6 & 7
     ***/
    private void getTimeFromPicker(Calendar myCalendar, final String queType, final TextView textView) {
        final String timeString;
        TimePickerDialog timePickerDialog = null;
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog(context,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String timeData = updateTime(hourOfDay, minute);

                            DecimalFormat formatter = new DecimalFormat("00");
                            String[] timeary = timeData.split(":");
                            timeData = ((formatter.format(Integer.parseInt(timeary[0]))) + ":" + timeary[1]);
                            if (queType.equals("TimeType6")) {
                                textView.setText(timeData);
                            } else if (queType.equals("TimeType7")) {
                                time = "";
                                time = timeData;
                                String newdateTime = date + " " + timeData;
                                try {
                                    //Unparseable date: "02-Jul-2021 22:31"
                                    String s = new SimpleDateFormat(
                                            AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a"
                                                    , "dd-MMM-yyyy kk:mm")).
                                            format(new SimpleDateFormat(
                                                    AppUtility.dateTimeByAmPmFormate(
                                                            "dd-MM-yyyy hh:mm a"
                                                            , "dd-MM-yyyy kk:mm")).parse(newdateTime));
                                    textView.setText(s);
                                } catch (ParseException e) {
                                    textView.setText(newdateTime);
                                    e.printStackTrace();
                                }

                            }
                        }
                    }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true
            );
        }
        timePickerDialog.show();
    }

    private void getSignatureDialog(final ImageView signature_set, final String tag, final ImageView addSign, final ImageView deleteSign) {
        LinearLayout signt = null;
        File mypath;
        final SignatureView mSignature;
        final Button sbmt_btn, skip_btn;
        ImageView close_diaolg;
        final Dialog dialog = new Dialog(context);

        try {
            final String current;
            final String getPath;
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.customform_signature_layout);

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);


            sbmt_btn = dialog.findViewById(R.id.sbmt_btn);
            skip_btn = dialog.findViewById(R.id.skip_btn);
            signt = dialog.findViewById(R.id.signt);
            close_diaolg = dialog.findViewById(R.id.close_diaolg);


            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir(context.getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);
            current = "eot_" + AppUtility.getDateByMiliseconds() + ".png";
            mypath = new File(directory, current);
            getPath = mypath.getAbsolutePath();

            mSignature = new SignatureView(context);
            signt.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            skip_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));
            sbmt_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok));

            sbmt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File mfile = mSignature.exportFile(getPath, current);
                    if (!mSignature.isSignatureEmpty()) {
                        setSignatureInView(mfile, tag, signature_set, addSign, deleteSign);//signature_set
                    } else {
                        signature_set.setImageResource(android.R.color.transparent);
                        signature_set.setVisibility(View.GONE);
                    }
                    dialog.dismiss();
                }
            });

            skip_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSignature.clear();
                }
            });

            close_diaolg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.show();
    }


    /*****Show Attachment Visibility****/
    public void showAttchmentView(int pos, String path, final ImageView attachmentView, ImageView deleteAttchment, Button addAttachment) {//, ImageView attachmentView, ImageView deleteAttchment
        try {
            File mfile = new File(path);
            if (mfile.exists()) {
                List<AnswerModel> ans = typeList.get(pos).getAns();
                if (ans != null && ans.size() > 0) {
                    ans.set(0, new AnswerModel(ans.get(0).getKey(),
                            mfile + ""));
                    typeList.get(pos).setAns(ans);
                } else {
                    List<AnswerModel> answerModels = new ArrayList<>();
                    answerModels.add(new AnswerModel("0",
                            mfile + ""));
                    typeList.get(pos).setAns(answerModels);
                }


                final String ext = typeList.get(pos).getAns().get(0).getValue().
                        substring((typeList.get(pos).getAns().get(0).getValue().lastIndexOf(".")) + 1);
                if (!ext.isEmpty()) {

                    addAttachment.setVisibility(View.GONE);
                    deleteAttchment.setVisibility(View.VISIBLE);
                    attachmentView.setVisibility(View.VISIBLE);
                    deleteAttchment.setTag(pos);

                    if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {

                      /*  Bitmap myBitmap = BitmapFactory.decodeFile(mfile.getAbsolutePath());
                        attachmentView.setImageBitmap(myBitmap);
                        attachmentView.setScaleType(ImageView.ScaleType.FIT_XY);*/
                        Picasso.with(context).load(mfile).into(attachmentView);

                    } else if (ext.equals("doc") || ext.equals("docx")) {
                        attachmentView.setImageResource(R.drawable.word);
                        attachmentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    } else if (ext.equals("pdf")) {
                        attachmentView.setImageResource(R.drawable.pdf);
                        attachmentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    } else if (ext.equals("xlsx") || ext.equals("xls")) {
                        attachmentView.setImageResource(R.drawable.excel);
                        attachmentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    } else if (ext.equals("csv")) {
                        attachmentView.setImageResource(R.drawable.csv);
                        attachmentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    } else {
                        attachmentView.setImageResource(R.drawable.doc);
                        attachmentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                }

                Log.e("", "");
            } else {
                addAttachment.setVisibility(View.VISIBLE);
                deleteAttchment.setVisibility(View.GONE);
                attachmentView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            addAttachment.setVisibility(View.VISIBLE);
            deleteAttchment.setVisibility(View.GONE);
            attachmentView.setVisibility(View.GONE);
            e.printStackTrace();
        }

    }

    /***set Signature in imageView****/
    private void setSignatureInView(final File mfile, final String tag, ImageView signature_set, ImageView addSign, ImageView deleteSign) {//, ImageView signature_set
        try {
            if (mfile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(mfile.getAbsolutePath());
                /********/
                signature_set.setVisibility(View.VISIBLE);
                signature_set.setImageBitmap(myBitmap);
                signature_set.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                addSign.setVisibility(View.GONE);
                deleteSign.setVisibility(View.VISIBLE);


                int position = Integer.parseInt(tag);
                List<AnswerModel> ans = typeList.get(position).getAns();

                if (ans != null && ans.size() > 0) {
                    ans.set(0, new AnswerModel(ans.get(0).getKey(), mfile + ""));
                    typeList.get(position).setAns(ans);

                } else {
                    List<AnswerModel> answerModels = new ArrayList<>();
                    answerModels.add(new AnswerModel("0",
                            mfile + ""));
                    typeList.get(position).setAns(answerModels);
                }

                Log.e("", "");
            } else {
                signature_set.setVisibility(View.GONE);
                addSign.setVisibility(View.VISIBLE);
                deleteSign.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            signature_set.setVisibility(View.GONE);
            addSign.setVisibility(View.VISIBLE);
            deleteSign.setVisibility(View.GONE);
            e.printStackTrace();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnClickListener {
        // private final ClientChat_View mlistener;
        TextView tvQuestion, tvDate, que_no, spinner_text, tvTime, tvTimeDate;//, txt_lable;//, textView1;
        EditText type_text, type_text_area;
        LinearLayout linearCheck, linearDate, linearTime, linearDateTime;
        RelativeLayout linearSpinner;
        NoDefaultSpinner spinner;
        ImageView timeImg, dateImg;
        CheckBox checkbox_single;
        Button buttonAttchment;
        ImageView signature_set, edit_sign, delete_sign, add_sign, attchment_set, delete_attchment;
        LinearLayout signature_layout, Attchment_layout;
        TextView add_sign_lable, Attchment_lable;
        //   private String img_url = "";


        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            //  this.mlistener = (ClientChat_View) context;
            Attchment_layout = itemView.findViewById(R.id.Attchment_layout);
            Attchment_lable = itemView.findViewById(R.id.Attchment_lable);
            buttonAttchment = itemView.findViewById(R.id.buttonAttchment);
            attchment_set = itemView.findViewById(R.id.attchment_set);
            delete_attchment = itemView.findViewById(R.id.delete_attchment);
            buttonAttchment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getTag() != null) {
                        int position = Integer.parseInt(view.getTag().toString());
                        if (myAttachment != null && context instanceof MyAttachment) {
                            myAttachment.selectFileWithoutAttchment(position, attchment_set, delete_attchment, buttonAttchment);
                        }
                    }
                }
            });

            delete_attchment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getTag() != null) {
                        int pos = Integer.parseInt(view.getTag().toString());
                        attchment_set.setImageResource(android.R.color.transparent);
                        attchment_set.setVisibility(View.GONE);
                        buttonAttchment.setVisibility(View.VISIBLE);
                        delete_attchment.setVisibility(View.GONE);
                        buttonAttchment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_attachment));
                        if (typeList.get(pos).getAns() != null && typeList.get(pos).getAns().size() > 0) {
                            typeList.get(pos).getAns().get(0).setValue("");
                            typeList.get(pos).getAns().get(0).setKey("");
                        }
                    }
                }
            });


            signature_layout = itemView.findViewById(R.id.signature_layout);
            signature_set = itemView.findViewById(R.id.signature_set);
            //   signature_set.setOnClickListener(this);


            edit_sign = itemView.findViewById(R.id.edit_sign);
            delete_sign = itemView.findViewById(R.id.delete_sign);
            add_sign = itemView.findViewById(R.id.add_sign);
            add_sign_lable = itemView.findViewById(R.id.add_sign_lable);

            add_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Tag", view.getTag() + "");
                    getSignatureDialog(signature_set, view.getTag().toString(), add_sign, delete_sign);
                }
            });


            delete_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (context instanceof FormQueAns_Activity) {
//                        ((FormQueAns_Activity) context).selectFile();
//                    }
                    int pos = Integer.parseInt(view.getTag().toString());
                    signature_set.setImageResource(android.R.color.transparent);
                    signature_set.setVisibility(View.GONE);
                    delete_sign.setVisibility(View.GONE);
                    add_sign.setVisibility(View.VISIBLE);
                    if (typeList.get(pos).getAns() != null && typeList.get(pos).getAns().size() > 0) {
                        typeList.get(pos).getAns().get(0).setValue("");
                        typeList.get(pos).getAns().get(0).setKey("");
                    }
                }
            });


            checkbox_single = itemView.findViewById(R.id.checkbox_single);

            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tvDate.getTag() != null) {
                        String pos = tvDate.getTag().toString();
                        int position = Integer.parseInt(pos);
                        if (s != null && s.toString().length() > 0) {
                            long startDate = 0;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                                Date date = sdf.parse(s.toString());
                                startDate = date.getTime() / 1000;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            List<AnswerModel> answerModelList = new ArrayList<>();
                            if (typeList.get(position).getAns() != null && typeList.get(position).getAns().size() > 0)
                                answerModelList.add(new AnswerModel(typeList.get(position).getAns().get(0).getKey(),
                                        startDate + ""));
                            else
                                answerModelList.add(new AnswerModel("0", startDate + ""));
                            typeList.get(position).setAns(answerModelList);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            type_text = itemView.findViewById(R.id.type_text);
            type_text.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_your_ans));

            type_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (type_text.getTag() != null) {
                        String pos = type_text.getTag().toString();
                        int position = Integer.parseInt(pos);
                        List<AnswerModel> ans = typeList.get(position).getAns();
                        if (ans != null && ans.size() > 0) {
                            ans.set(0, new AnswerModel(ans.get(0).getKey(), s.toString()));
                            typeList.get(position).setAns(ans);
                        } else {
                            List<AnswerModel> answerModels = new ArrayList<>();
                            answerModels.add(new AnswerModel("0", s.toString()));
                            typeList.get(position).setAns(answerModels);
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            type_text_area = itemView.findViewById(R.id.type_text_area);
            type_text_area.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_your_ans));

            type_text_area.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (type_text_area.getTag() != null) {
                        String pos = type_text_area.getTag().toString();
                        int position = Integer.parseInt(pos);

                        List<AnswerModel> ans = typeList.get(position).getAns();
                        if (ans != null && ans.size() > 0) {
                            ans.set(0, new AnswerModel(ans.get(0).getKey(), s.toString()));
                            typeList.get(position).setAns(ans);
                        } else {
                            List<AnswerModel> answerModels = new ArrayList<>();
                            answerModels.add(new AnswerModel("0", s.toString()));
                            typeList.get(position).setAns(answerModels);
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            linearCheck = itemView.findViewById(R.id.linearCheck);
            linearDate = itemView.findViewById(R.id.linearDate);
            spinner = itemView.findViewById(R.id.dropdown_spinner);
            linearSpinner = itemView.findViewById(R.id.linearSpinner);
            que_no = itemView.findViewById(R.id.que_no);
            spinner_text = itemView.findViewById(R.id.spinner_text);
            //   txt_lable = itemView.findViewById(R.id.txt_lable);
            spinner_text.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_option));
            linearSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinner.performClick();
                    Log.e("TAG", "THAG");
                }
            });


            final Calendar myCalendar = Calendar.getInstance();
            /** Get Date from picker call back***/
            final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    /**type 5 for Time Type Question**/
                    if (view.getTag().equals("DateType5")) {

                        String month_String = String.valueOf((monthOfYear + 1));
                        String completeDate;
                        if (month_String.length() == 1) {
                            month_String = "0" + month_String;
                            completeDate = dayOfMonth + "-" + month_String + "-" + year;
                            // tvDate.setText(dayOfMonth + "-" + month_String + "-" + year + " ");
                        } else
                            completeDate = dayOfMonth + "-" + month_String + "-" + year;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date parse = sdf.parse(completeDate.trim());
                            tvDate.setText(new SimpleDateFormat("dd-MMM-yyyy").format(parse));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    /**type 7 for DateTime type Question***/
                    else if (view.getTag().equals("DateType7")) {
                        String monthString = String.valueOf((monthOfYear + 1));
                        if (monthString.length() == 1) {
                            monthString = "0" + monthString;
                        }
                        date = "";
                        date = (dayOfMonth + "-" + monthString + "-" + year + " ");
                        String newDateTime = date + " " + time;
                        SimpleDateFormat sdf = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                                "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"));
                        try {
                            Date parseDate = sdf.parse(newDateTime);
                            tvTimeDate.setText(new SimpleDateFormat(
                                    AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm")).format(parseDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };

            /**initialize Date picker***/
            final DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePicker, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));


            linearDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePickerDialog.getDatePicker().setTag("DateType5");
                    datePickerDialog.show();
                }
            });

            type_text.setOnTouchListener(this);
            type_text_area.setOnTouchListener(this);

            linearTime = itemView.findViewById(R.id.linearTime);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tvTime.getTag() != null) {
                        String pos = tvTime.getTag().toString();
                        int position = Integer.parseInt(pos);
                        if (s != null && s.toString().length() > 0) {
                            long startDate = 10;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                                        "hh:mm a", "kk:mm"), Locale.US);
                                Date date = sdf.parse(s.toString());
                                startDate = date.getTime() / 1000;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            List<AnswerModel> answerModelList = new ArrayList<>();
                            if (typeList.get(position).getAns() != null && typeList.get(position).getAns().size() > 0)
                                answerModelList.add(new AnswerModel(typeList.get(position).getAns().get(0).getKey(),
                                        startDate + ""));
                            else
                                answerModelList.add(new AnswerModel("0", startDate + ""));
                            typeList.get(position).setAns(answerModelList);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            linearTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTimeFromPicker(myCalendar, "TimeType6", tvTime);

                }
            });


            /**question type & for date time ***/
            linearDateTime = itemView.findViewById(R.id.linearDateTime);
            tvTimeDate = itemView.findViewById(R.id.tvTimeDate);
            tvTimeDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tvTimeDate.getTag() != null) {
                        String pos = tvTimeDate.getTag().toString();
                        int position = Integer.parseInt(pos);
                        if (s != null && s.toString().length() > 0) {
                            long startDate = 0;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        AppUtility.dateTimeByAmPmFormate(
                                                "dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm"), Locale.US);
                                Date date = sdf.parse(s.toString());
                                startDate = date.getTime() / 1000;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            List<AnswerModel> answerModelList = new ArrayList<>();
                            if (typeList.get(position).getAns() != null && typeList.get(position).getAns().size() > 0)
                                answerModelList.add(new AnswerModel(typeList.get(position).getAns().get(0).getKey(),
                                        startDate + ""));
                            else
                                answerModelList.add(new AnswerModel("0", startDate + ""));
                            typeList.get(position).setAns(answerModelList);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            timeImg = itemView.findViewById(R.id.timeImg);
            dateImg = itemView.findViewById(R.id.dateImg);

            dateImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.getDatePicker().setTag("DateType7");
                    datePickerDialog.show();
                }
            });


            timeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTimeFromPicker(myCalendar, "TimeType7", tvTimeDate);
                }
            });
        }

        /**
         * Required for edittext scrolling
         */
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == R.id.type_text) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            } else if (view.getId() == R.id.type_text_area) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {

        }
    }
}


