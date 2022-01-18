package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.appointment_Details_presrnter;

import com.eot_app.nav_menu.appointment.details.AppointmentAttachment;

import java.util.List;

public interface Appointment_Deatils_View {

    void setAppointmentAttachment(List<AppointmentAttachment> appointmentAttachments);

    void sessionExpire(String message);
}
