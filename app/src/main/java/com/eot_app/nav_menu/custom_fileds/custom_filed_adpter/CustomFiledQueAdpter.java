package com.eot_app.nav_menu.custom_fileds.custom_filed_adpter;

import static com.eot_app.utility.AppUtility.updateTime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.OptionModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.DropdownListBean;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyAdapter;
import com.eot_app.utility.util_interfaces.NoDefaultSpinner;
import com.hypertrack.hyperlog.HyperLog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sonam-11 on 17/9/20.
 */
public class CustomFiledQueAdpter extends RecyclerView.Adapter<CustomFiledQueAdpter.ViewHolder> {
    Context context;
    CustomFiledCallBack myFormInterFace;
    String time = "", date = "";
    ArrayList<CustOmFormQuestionsRes> questionList = new ArrayList<>();
    // int count = 0;

    public CustomFiledQueAdpter(ArrayList<CustOmFormQuestionsRes> typeList, Context context, CustomFiledCallBack myFormInterFace) {
        this.context = context;
        this.questionList = typeList;
        this.myFormInterFace = myFormInterFace;
        String currentDateTime = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate(
                "dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm"));
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

        setIndex();
    }

    private void setIndex() {
        if (questionList != null && questionList.size() > 0) {
            int i = 0;
            for (CustOmFormQuestionsRes custOmFormQuestionsRes : questionList) {
                if (!custOmFormQuestionsRes.getType().equals("9")) {
                    i++;
                    custOmFormQuestionsRes.setIndex(i);
                } else custOmFormQuestionsRes.setIndex(0);

            }
        }
    }

    public void updateCustomFiled(ArrayList<CustOmFormQuestionsRes> typeList) {
        this.questionList = typeList;
        HyperLog.i("CustomFiledQueAdpter", "adpter Notify----" + questionList.size());
        notifyDataSetChanged();
    }

    public ArrayList<CustOmFormQuestionsRes> getTypeList() {
        return questionList;
    }

    @Override
    public CustomFiledQueAdpter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // View view = inflater.inflate(R.layout.question_list_layout, parent, false);
        View view = inflater.inflate(R.layout.customfiled_questionlist, parent, false);
        CustomFiledQueAdpter.ViewHolder viewHolder = new CustomFiledQueAdpter.ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //  holder.que_no.setVisibility(View.GONE);
      /*  if ((questionList.get(position).getIndex() > 0)) {
            //count++;
            holder.que_no.setVisibility(View.VISIBLE);
            holder.que_no.setText(questionList.get(position).getIndex() + ".");
        } else {
            holder.que_no.setText("");
            holder.que_no.setVisibility(View.GONE);
        }
*/

        HyperLog.i("CustomFiledQueAdpter", "onBindViewHolder----" + position);

        holder.tvQuestion.setVisibility(View.VISIBLE);
        if (questionList.get(position).getMandatory().equals("1")) {
            holder.tvQuestion.setText(questionList.get(position).getDes() + " *");
        } else {
            holder.tvQuestion.setText(questionList.get(position).getDes());
        }
        try {
            holder.tvQuestion.setTypeface(holder.tvQuestion.getTypeface(), Typeface.NORMAL);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        switch (questionList.get(position).getType()) {
            case "1"://type 1 for text type quest
                holder.type_text.setVisibility(View.VISIBLE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                // holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);

                holder.type_text.setTag(position);
                if (questionList.get(position).getAns().isEmpty())
                    holder.type_text.setText("");
                else if (questionList.get(position).getAns().size() > 0)
                    holder.type_text.setText(questionList.get(position).getAns().get(0).getValue());
                break;
            case "2"://TextArea
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.VISIBLE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                //  holder.txt_lable.setVisibility(View.GONE);
                holder.type_text_area.setTag(position);
                holder.checkbox_single.setVisibility(View.GONE);

                if (questionList.get(position).getAns().isEmpty())
                    holder.type_text_area.setText("");
                else if (questionList.get(position).getAns().size() > 0)
                    holder.type_text_area.setText(questionList.get(position).getAns().get(0).getValue());
                break;
            case "3"://checkbox type
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.VISIBLE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                //holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);

                setCheckBoxOption(holder, position);
                break;
            case "4"://dropdown type
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.VISIBLE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                //  holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);

                setDropDownOptions(holder, position);
                break;
            case "5"://date type
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.VISIBLE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                // holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);


                holder.tvDate.setTag(position);
                if (questionList.get(position).getAns().isEmpty())
                    holder.tvDate.setText("");
                else if (questionList.get(position).getAns().size() > 0) {
                    try {
                        if (!(questionList.get(position).getAns().get(0).getValue()).equals("")) {
                            String[] dateConvert = AppUtility.getFormatedTime(questionList.get(position).
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
            case "6"://Time type
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.VISIBLE);
                holder.linearDateTime.setVisibility(View.GONE);
                // holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);

                holder.tvTime.setTag(position);
                if (questionList.get(position).getAns().isEmpty())
                    holder.tvTime.setText("");
                else if (questionList.get(position).getAns().size() > 0) {
                    try {
                        if (!(questionList.get(position).getAns().get(0).
                                getValue().equals(""))) {
                            String time = AppUtility.getDateWithFormate2((Long.parseLong(questionList.get(position).
                                            getAns().get(0).
                                            getValue()) * 1000),
                                    AppUtility.dateTimeByAmPmFormate(
                                            "hh:mm a", "kk:mm"));
                            holder.tvTime.setText(time);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "7": //Date Time Type
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.VISIBLE);
                // holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);

                holder.tvTimeDate.setTag(position);
                if (questionList.get(position).getAns().isEmpty())
                    holder.tvTimeDate.setText("");
                else if (questionList.get(position).getAns().size() > 0) {
                    try {
                        if (!questionList.get(position).getAns().get(0).getValue().equals("")) {
                            Long dateLong = Long.parseLong(questionList.get(position).getAns().get(0).getValue());
                            String formate = AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm");
                            String dateConvert = AppUtility.getDate(dateLong, formate);
                            holder.tvTimeDate.setText(dateConvert);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "8":
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                //  holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.VISIBLE);

                holder.tvQuestion.setVisibility(View.GONE);
                holder.checkbox_single.setText(questionList.get(position).getDes());

                if (questionList.get(position).getAns() != null && questionList.get(position).getAns().size() > 0) {
                    boolean check = questionList.get(position).getAns().get(0).getValue().equals("1");
                    holder.checkbox_single.setChecked(questionList.get(position).getAns().get(0).getValue().equals("1"));

                }

                holder.checkbox_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (questionList.get(position).getAns() != null && questionList.get(position).getAns().size() > 0) {
                            if (questionList.get(position).getAns().get(0).getValue().equals("1"))
                                questionList.get(position).getAns().get(0).setValue("0");
                            else questionList.get(position).getAns().get(0).setValue("1");
                        } else {
                            questionList.get(position).getAns().add(new AnswerModel("0", "1"));
                        }
                    }
                });


                break;
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

                break;
            default: {
                holder.type_text.setVisibility(View.GONE);
                holder.type_text_area.setVisibility(View.GONE);
                holder.linearCheck.setVisibility(View.GONE);
                holder.linearSpinner.setVisibility(View.GONE);
                holder.linearDate.setVisibility(View.GONE);
                holder.linearTime.setVisibility(View.GONE);
                holder.linearDateTime.setVisibility(View.GONE);
                //   holder.txt_lable.setVisibility(View.GONE);
                holder.checkbox_single.setVisibility(View.GONE);
            }
        }

    }


    private void setCheckBoxOption(CustomFiledQueAdpter.ViewHolder holder, final int position) {
        if (holder.linearCheck.getChildCount() > 0)
            holder.linearCheck.removeAllViews();

        for (final OptionModel optionModel : questionList.get(position).getOpt()) {
            final CheckBox checkBox = new CheckBox(context);
            checkBox.setText("");
            checkBox.setText(optionModel.getValue());
            checkBox.setTag(optionModel);


            checkBox.setTextAppearance(context, R.style.header_text_style);
            checkBox.setTypeface(ResourcesCompat.getFont(context, R.font.arimo_regular));
            checkBox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.txt_sub_color)));

            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.linearCheck.addView(checkBox);

            if (!questionList.get(position).getAns().isEmpty()) {
                for (final AnswerModel answerModel : questionList.get(position).getAns()) {
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
                        if (questionList.get(position).getAns() != null && questionList.get(position).getAns().size() > 0) {
                            List<AnswerModel> ans = questionList.get(position).getAns();
                            for (AnswerModel ansmodel : ans)
                                if (ansmodel.getKey().equals(selectedOptionModel.getKey()))
                                    return;
                            ans.add(new AnswerModel(selectedOptionModel.getKey(), selectedOptionModel.getValue()));
                            questionList.get(position).setAns(ans);
                        } else {
                            List<AnswerModel> answerModelList = new ArrayList<>();
                            answerModelList.add(new AnswerModel(selectedOptionModel.getKey(), selectedOptionModel.getValue()));
                            questionList.get(position).setAns(answerModelList);
                        }
                    } else {
                        if (questionList.get(position).getAns() != null && questionList.get(position).getAns().size() > 0) {
                            List<AnswerModel> ans = questionList.get(position).getAns();
                            for (AnswerModel ansmodel : ans)
                                if (ansmodel.getKey().equals(selectedOptionModel.getKey())) {
                                    ans.remove(ansmodel);
                                    questionList.get(position).setAns(ans);
                                    return;
                                }
                        }
                    }

                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void setDropDownOptions(final CustomFiledQueAdpter.ViewHolder holder, final int position) {
        if (questionList.get(position).getAns().isEmpty())
            holder.spinner_text.setText("");
        else if (questionList.get(position).getAns().size() > 0)
            holder.spinner_text.setText(questionList.get(position).getAns().get(0).getValue());


        final MyAdapter<OptionModel> spinnerAdapter = new MyAdapter<>(context, R.layout.custom_adapter_item_layout, questionList.get(position).getOpt());
        holder.spinner.setAdapter(spinnerAdapter);


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //   ansListt.put(typeList.get(position).getQueId(), typeList.get(position).getOpt().get(pos).getValue());

                List<AnswerModel> ans = new ArrayList<>();

                ans.add(new AnswerModel(((DropdownListBean) parent.getItemAtPosition(pos)).getKey(),
                        ((DropdownListBean) parent.getItemAtPosition(pos)).getName()));

                questionList.get(position).setAns(ans);


                String text = ((DropdownListBean) parent.getItemAtPosition(pos)).getName();
                holder.spinner_text.setText(text);

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
                                    String s = new SimpleDateFormat(
                                            AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a"
                                                    , "dd-MMM-yyyy kk:mm"))
                                            .format(new SimpleDateFormat(
                                                    AppUtility.dateTimeByAmPmFormate(
                                                            "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"
                                                    )).parse(newdateTime));
                                    textView.setText(s);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true
            );
        }
        timePickerDialog.show();
    }

    public interface CustomFiledCallBack {
        void CustomFiledsList();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        TextView tvQuestion, tvDate, que_no, spinner_text, tvTime, tvTimeDate;//, txt_lable;//, textView1;
        EditText type_text, type_text_area;
        LinearLayout linearCheck, linearDate, linearTime, linearDateTime;
        RelativeLayout linearSpinner;
        NoDefaultSpinner spinner;
        ImageView timeImg, dateImg;
        CheckBox checkbox_single;
        // RelativeLayout lay;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvDate = itemView.findViewById(R.id.tvDate);


//            lay = itemView.findViewById(R.id.lay);
//            textView1 = itemView.findViewById(R.id.textView1);
            checkbox_single = itemView.findViewById(R.id.checkbox_single);

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
                            if (questionList.get(position).getAns() != null && questionList.get(position).getAns().size() > 0)
                                answerModelList.add(new AnswerModel(questionList.get(position).getAns().get(0).getKey(),
                                        startDate + ""));
                            else
                                answerModelList.add(new AnswerModel("0", startDate + ""));
                            questionList.get(position).setAns(answerModelList);
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
                        List<AnswerModel> ans = questionList.get(position).getAns();
                        if (ans != null && ans.size() > 0) {
                            ans.set(0, new AnswerModel(ans.get(0).getKey(), s.toString()));
                            questionList.get(position).setAns(ans);
                        } else {
                            List<AnswerModel> answerModels = new ArrayList<>();
                            answerModels.add(new AnswerModel("0", s.toString()));
                            questionList.get(position).setAns(answerModels);
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

                        List<AnswerModel> ans = questionList.get(position).getAns();
                        if (ans != null && ans.size() > 0) {
                            ans.set(0, new AnswerModel(ans.get(0).getKey(), s.toString()));
                            questionList.get(position).setAns(ans);
                        } else {
                            List<AnswerModel> answerModels = new ArrayList<>();
                            answerModels.add(new AnswerModel("0", s.toString()));
                            questionList.get(position).setAns(answerModels);
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
            que_no.setVisibility(View.GONE);
            spinner_text = itemView.findViewById(R.id.spinner_text);
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
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"));
                        try {
                            Date parseDate = sdf.parse(newDateTime);
                            tvTimeDate.setText(new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                                    "dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm")).format(parseDate));
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
                            if (questionList.get(position).getAns() != null && questionList.get(position).getAns().size() > 0)
                                answerModelList.add(new AnswerModel(questionList.get(position).getAns().get(0).getKey(),
                                        startDate + ""));
                            else
                                answerModelList.add(new AnswerModel("0", startDate + ""));
                            questionList.get(position).setAns(answerModelList);
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
                                        AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a"
                                                , "dd-MMM-yyyy kk:mm"), Locale.US);
                                Date date = sdf.parse(s.toString());
                                startDate = date.getTime() / 1000;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            List<AnswerModel> answerModelList = new ArrayList<>();
                            if (questionList.get(position).getAns() != null && questionList.get(position).getAns().size() > 0)
                                answerModelList.add(new AnswerModel(questionList.get(position).getAns().get(0).getKey(),
                                        startDate + ""));
                            else
                                answerModelList.add(new AnswerModel("0", startDate + ""));
                            questionList.get(position).setAns(answerModelList);
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

            //   txt_lable = itemView.findViewById(R.id.txt_lable);
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

    }
}

