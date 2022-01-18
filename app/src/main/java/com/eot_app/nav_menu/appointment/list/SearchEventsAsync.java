package com.eot_app.nav_menu.appointment.list;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.eot_app.nav_menu.appointment.calendar.data.Event;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.list.common.CommonAppointmentModel;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mahendra Dabi on 14-08-2020.
 * this class to show dot indicator on calendar if appointment/job/audit find on the given dates
 */
public class SearchEventsAsync {
    private final SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
    List<Event> events = new ArrayList<>();

    List<CommonAppointmentModel> commonList = new ArrayList<>();
    OnEventSearchCompletion onEventSearchCompletion;
    String startDate;

    public SearchEventsAsync(String startDate) {
        this.startDate = startDate;

    }

    public void setOnEventSearchCompletion(OnEventSearchCompletion onEventSearchCompletion) {
        this.onEventSearchCompletion = onEventSearchCompletion;
    }

    public void addItemDotsForAllInBackGround() {
        try {
            ExecutorService service = Executors.newSingleThreadExecutor();

            service.execute(new Runnable() {
                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();

                    try {

                        Date parseStart = f.parse(startDate);
                        long startTime = parseStart.getTime();

                        calendar.setTime(parseStart);
                        calendar.add(Calendar.MONTH, 1);

                        long endTime = calendar.getTime().getTime();

                        List<Appointment> appointmentList = new ArrayList<>();

                        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsAppointmentVisible() == 0) {

                            appointmentList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                    .appointmentModel().getAppointmentByDate(startTime, endTime);
                            if (appointmentList != null)
                                for (Appointment appointment : appointmentList) {
                                    CommonAppointmentModel cm = new CommonAppointmentModel();
                                    cm.setStartDateTime(appointment.getSchdlStart());
                                    cm.setEndDateTime(appointment.getSchdlFinish());
                                    cm.setJobId(appointment.getAppId());
                                    commonList.add(cm);

                                }
                        }

                        List<Job> jobList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                .appointmentModel().getJobsByDate(startTime, endTime);


                        if (jobList != null)
                            for (Job job : jobList) {
                                CommonAppointmentModel cm = new CommonAppointmentModel();
                                cm.setStartDateTime(job.getSchdlStart());
                                cm.setJobId(job.getJobId());
                                cm.setEndDateTime(job.getSchdlFinish());
                                commonList.add(cm);
                            }

                        /**
                         * check permission is allowed th audit or not
                         * */

                        List<AuditList_Res> auditList = new ArrayList<>();
                        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsAuditVisible() == 0) {
                            auditList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                    .auditDao().getAuditListByDate(startTime, endTime);
                            if (auditList != null)
                                for (AuditList_Res audit : auditList) {
                                    CommonAppointmentModel cm = new CommonAppointmentModel();
                                    cm.setStartDateTime(audit.getSchdlStart());
                                    cm.setEndDateTime(audit.getSchdlFinish());
                                    cm.setJobId(audit.getAudId());
                                    commonList.add(cm);
                                }
                        }


                        Collections.sort(commonList);


                        Date previousAddedDate2 = null;
//                         *****Show Dot for Start Date*********
                        if (commonList.size() > 0) {
                            for (CommonAppointmentModel model : commonList) {
                                {
                                    if (model.getStartDateTime() != null && !model.getStartDateTime().isEmpty()) {
                                        Date date = new Date(Long.parseLong(model.getStartDateTime()) * 1000);
                                        calendar.setTime(date);
                                        if (previousAddedDate2 == null) {
                                            Event event = new Event(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                            event.setmColor(Color.parseColor("#6ed120"));
                                            events.add(event);
                                        } else if (!AppUtility.isSameDay(previousAddedDate2, date)) {
                                            Event event = new Event(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                            event.setmColor(Color.parseColor("#6ed120"));
                                            events.add(event);
                                        }

                                        previousAddedDate2 = date;
                                    }
                                }
                            }

                        }


                        if (commonList.size() > 0) {
                            Date previousAddedDate = null;
                            /*****Show Dot for END....... Date*********/
                            for (CommonAppointmentModel model : commonList) {
                                {
                                    if (model.getEndDateTime() != null && !model.getEndDateTime().isEmpty()) {
                                        Date date = new Date(Long.parseLong(model.getEndDateTime()) * 1000);
                                        calendar.setTime(date);
                                        if (previousAddedDate == null) {
                                            Event event = new Event(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                            event.setmColor(Color.parseColor("#6ed120"));
                                            events.add(event);
                                        } else if (!AppUtility.isSameDay(previousAddedDate, date)) {
                                            Event event = new Event(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                            event.setmColor(Color.parseColor("#6ed120"));
                                            events.add(event);
                                        }

                                        previousAddedDate = date;
                                    }
                                }
                            }


                            /*****Job list Dot for between dates*******/

                            try {
                                if (jobList != null && jobList.size() > 0) {
                                    Calendar stratTempCal = Calendar.getInstance();
                                    Calendar endTempCal = Calendar.getInstance();
                                    for (Job tempJob : jobList) {
                                        if (tempJob.getSchdlStart() != null && !tempJob.getSchdlStart().isEmpty()) {
                                            Date tempParseStart = new Date(Long.parseLong(tempJob.getSchdlStart()) * 1000);
                                            Date tempParseEnd = new Date(Long.parseLong(tempJob.getSchdlFinish()) * 1000);

                                            stratTempCal.setTime(tempParseStart);
                                            endTempCal.setTime(tempParseEnd);

                                            Calendar startCalendar = getCalendarWithoutTime(tempParseStart);
                                            Calendar endCalendar = getCalendarWithoutTime(tempParseEnd);

                                            while (startCalendar.before(endCalendar)) {
                                                startCalendar.add(Calendar.DATE, 1);
                                                Event event = new Event(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
                                                if (!events.equals(event)) {
                                                    event.setmColor(Color.parseColor("#6ed120"));
                                                    events.add(event);
                                                }
                                            }

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            /***********Appoinmnet for all between dates****************/
                            try {
                                if (appointmentList != null && appointmentList.size() > 0) {
                                    Calendar stratTempCal = Calendar.getInstance();
                                    Calendar endTempCal = Calendar.getInstance();
                                    for (Appointment tempJob : appointmentList) {
                                        if (tempJob.getSchdlStart() != null && !tempJob.getSchdlStart().isEmpty()) {

                                            Date tempParseStart = new Date(Long.parseLong(tempJob.getSchdlStart()) * 1000);
                                            Date tempParseEnd = new Date(Long.parseLong(tempJob.getSchdlFinish()) * 1000);

                                            stratTempCal.setTime(tempParseStart);
                                            endTempCal.setTime(tempParseEnd);

                                            Calendar startCalendar = getCalendarWithoutTime(tempParseStart);
                                            Calendar endCalendar = getCalendarWithoutTime(tempParseEnd);

                                            while (startCalendar.before(endCalendar)) {
                                                startCalendar.add(Calendar.DATE, 1);
                                                Event event = new Event(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
                                                if (!events.equals(event)) {
                                                    event.setmColor(Color.parseColor("#6ed120"));
                                                    events.add(event);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            /***********AUDIT........ for all between dates****************/
                            try {
                                if (auditList != null && auditList.size() > 0) {
                                    Calendar stratTempCal = Calendar.getInstance();
                                    Calendar endTempCal = Calendar.getInstance();
                                    for (AuditList_Res tempJob : auditList) {
                                        if (tempJob.getSchdlStart() != null && !tempJob.getSchdlStart().isEmpty()) {

                                            Date tempParseStart = new Date(Long.parseLong(tempJob.getSchdlStart()) * 1000);
                                            Date tempParseEnd = new Date(Long.parseLong(tempJob.getSchdlFinish()) * 1000);

                                            stratTempCal.setTime(tempParseStart);
                                            endTempCal.setTime(tempParseEnd);


                                            Calendar startCalendar = getCalendarWithoutTime(tempParseStart);
                                            Calendar endCalendar = getCalendarWithoutTime(tempParseEnd);

                                            while (startCalendar.before(endCalendar)) {
                                                startCalendar.add(Calendar.DATE, 1);
                                                Event event = new Event(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
                                                if (!events.equals(event)) {
                                                    event.setmColor(Color.parseColor("#6ed120"));
                                                    events.add(event);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            // code goes here
                            if (onEventSearchCompletion != null && events != null)
                                onEventSearchCompletion.onEventFounds(events);
                        }
                    });


                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }


    private Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public interface OnEventSearchCompletion {
        void onEventFounds(List<Event> eventList);
    }
}
