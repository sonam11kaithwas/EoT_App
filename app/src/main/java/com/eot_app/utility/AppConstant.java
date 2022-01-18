package com.eot_app.utility;


/**
 * Created by aplite_pc302 on 6/1/18.
 */

public class AppConstant {


    /******Add AppConstant file*******/

    /**
     * live url
     **/
//    public static final String BASEURL = "https://www.eyeontask.com/eotServices/";

    /**
     * URL's for Live Registration
     *****/
//    public static final String REGISTER_US = "https://us.eyeontask.com/en/eotServices/";
//    public static final String REGISTER_AU = "https://au.eyeontask.com/en/eotServices/";
//    public static final String REGISTER_AS = "https://as.eyeontask.com/en/eotServices/";
//    public static final String REGISTER_UK = "https://uk.eyeontask.com/en/eotServices/";


    /**
     * Staging url's
     **/

//    public static final String BASEURL = BuildConfig.BASEURL;

    public static final String BASEURL = "https://staging.eyeontask.com/eotServices/";
    //  public static final String BASEURL = "http://103.231.46.90:8435/eotServices/";

    /**
     * URL's for Staging For Registration
     *****/
//    public static final String REGISTER_US = BuildConfig.REGISTER_US;
//    public static final String REGISTER_AU = BuildConfig.REGISTER_AU;
//    public static final String REGISTER_AS = BuildConfig.REGISTER_AS;
//    public static final String REGISTER_UK = BuildConfig.REGISTER_UK;

    public static final String REGISTER_US = "https://us1.eyeontask.com/en/eotServices/";
    public static final String REGISTER_AU = "https://au1.eyeontask.com/en/eotServices/";
    public static final String REGISTER_AS = "https://as1.eyeontask.com/en/eotServices/";
    public static final String REGISTER_UK = "https://uk1.eyeontask.com/en/eotServices/";


    //    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss a";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";// 2021-12-09 23:00:00


    public static final String DATE_TIME_FORMAT_new = "dd-MMM-yyyy hh:mm:ss a";
    public static final int LIMIT_MID = 50;
    public static final int LIMIT_HIGH = 120;
    public static final String SESSION_EXPIRE = "401";
    public static final String network_error = "Please check your internet connection.";
    public static final String[] status = {
            "New",
            "Accepted",
            "Reject",
            "Cancel",
            "Travelling",
            "Break",
            "In Progress",
            "Job Break",
            "Completed",
            "Closed",
            "On Hold"
    };
    public static final String[] Prioty = {"Low", "Medium", "High"};
    public static final String Low = "Low";
    public static final String Low_id = "1";
    public static final String medium = "medium";
    public static final String Medium_id = "2";
    //     job states id
    public static final String Not_Started = "1";
    public static final String Accepted = "2";
    public static final String Reject = "3";
    public static final String Cancel = "4";
    public static final String Travelling = "5";//travel start
    public static final String Break = "6";
    public static final String In_Progress = "7";
    public static final String Pending = "8";
    public static final String Completed = "9";
    public static final String Closed = "10";
    public static final String Multi = "11";
    public static final String New_On_Hold = "12";
    //   job Priority
    public static final String High = "High";
    public static final String High_id = "3";
    public static final int ForEdit = 2;
    public static final int ForAdd = 1;
    public static final String Edit = "EDIT";
    public static final String Add = "ADD";
    public static final String COUNTRYFILE = "countries.json";
    public static final String CURRENCYFILE = "currency.json";
    public static final String CURRENCYFORMATFILE = "currency_format.json";
    public static final String TIMEZONEFILE = "timezones.json";
    public static final String STATEFILE = "states.json";
    public static final String LANGUAGEFILE = "language.json";
    public static final String SERVER_LOCATIONFILE = "server_location.json";
    //quotes states code
    //1- new,2- approved,3-reject,4-on hold
    public static final String QuoteNew = "1";
    public static final String QuoteAproved = "2";
    public static final String QuoteOnHold = "8"; // its not good hebit
    public static final String QuoteReject = "3";
    //Expense status code
    public static final String EXPENSE_CLAIM = "1";
    public static final String EXPENSE_APPROVED = "2";
    public static final String EXPENSE_REJECT = "3";
    public static final String EXPENSE_PAID = "4";
    public static final String EXPENSE_OPEN = "5";
    public static final String NEW_LEAVE_STATUS = "1";
    public static final String APPRO_LEAVE_STATUS = "2";
    public static final String REJECT_LEAVE_STATUS = "3";
    public static final String userName = "Please enter valid username";
    //forgot pass
    public static final String frgt_pass = "Please enter password";
    //offline data sync
    public static final String syn_data = "Switching user will cancel all the offline pending request from previously logged in user ";
    public static final String desc = "Description";
    public static final String status_new_key = "New";
    public static final String status_reje = "Reject";
    public static final String status_onhold = "On Hold";
    public static final String status_com = "Completed";
    public static final String status_acc = "Accepted";
    public static final String approved = "Approved";
    public static final String rejected = "Rejected";
    //Expense Status name
    public static final String Expense_Claim_Reimbursement = "expense_Claim_Reimbursement";
    public static final String Expense_Approved = "expense_Approved";
    public static final String Expense_Reject = "reject";
    public static final String Expense_Paid = "expense_Paid";
    public static final String Expense_Open = "expense_Open";
    //forgot password
    public static final String fr_alr_key = "Already have key";
    public static final String fr_reset_key = "Reset password";
    public static final String fr_dont_key = "Don't have key";
    public static final String fr_set_key = "Please enter a password reset key";
    public static final String fr_pass = "New Password";
    public static final String fr_cnfrm = "Confirm Password";
    public static final String inCorrect_user_name = "inCorrect_user_name";
    public static final String password_error = "password_error";
    public static final String key = "key";
    public static final String frgt_pass_match = "frgt_pass_match";
    public static final String empty_client_list = "empty_client_list";
    public static final String client_name = "client_name";
    public static final String account_type = "account_type";
    public static final String client_email = "email";
    public static final String mob_no = "mob_no";
    public static final String industry = "industry";
    public static final String state = "state";
    public static final String save_btn = "save_btn";
    public static final String email_empty = "email_empty";
    public static final String email_error = "email_error";
    public static final String c_e_mob = "c_e_mob";
    public static final String country_error = "country_error";
    public static final String state_error = "state_error";
    public static final String status_radio_btn = "status_radio_btn";
    public static final String active_radio_btn = "active_radio_btn";
    public static final String inactive_radio_btn = "inactive_radio_btn";
    public static final String fax = "fax";
    public static final String skype = "skype";
    public static final String twitter = "twitter";
    public static final String cont_name = "cont_name";
    public static final String site_name = "site_name";
    public static final String site_default_checkBox = "site_default_checkBox";
    public static final String err_site_name = "err_site_name";
    public static final String pm = "pm";
    public static final String am = "am";
    public static final String sort_by_date = "sort_by_date";
    public static final String sort_by_recent = "sort_by_recent";
    public static final String search_job_code_client_name_address = "search_job_code_client_name_address";
    public static final String err_no_jobs_found = "err_no_jobs_found";
    public static final String jobs = "jobs";
    public static final String clients = "clients";
    public static final String logout = "logout";
    public static final String quotes = "quotes";
    public static final String add_quote = "add_quote";
    public static final String edit_quote = "edit_quote";
    public static final String quotes_end_date = "quotes_end_date";
    public static final String quotes_start_date = "quotes_start_date";
    public static final String err_check_network = "err_check_network";
    public static final String job_status = "job_status";
    public static final String reset = "reset";
    public static final String filter = "filter";
    public static final String sites_screen_title = "sites_screen_title";
    public static final String search = "search";
    public static final String add_site_screen_title = "add_site_screen_title";
    public static final String address = "address";
    public static final String submit_btn = "submit_btn";
    public static final String site_add = "site_add";
    public static final String edit_site_screen_title = "edit_site_screen_title";
    public static final String update_btn = "update";
    public static final String title_add_job = "title_add_job";
    public static final String job_title = "job_title";
    public static final String job_desc = "job_desc";
    public static final String job_priority = "job_priority";
    public static final String job_inst = "job_inst";
    public static final String client_mand = "client_mand";
    public static final String contact_name = "contact_name";
    public static final String alt_mobile_number = "alt_mobile_number";
    public static final String email = "email";
    public static final String country = "country";
    public static final String city = "city";
    public static final String postal_code = "postal_code";
    public static final String assign_to = "assign_to";
    public static final String shdl_start = "shdl_start";
    public static final String shdl_end = "shdl_end";
    public static final String date_form = "date_form";
    public static final String time_form = "time_form";
    public static final String tag = "tag";
    public static final String add = "add";
    public static final String assign_to_fw = "assign_to_fw";
    public static final String items_selected = "items_selected";
    public static final String self_key = "self_key";
    public static final String clear = "clear";
    public static final String err_job_title = "err_job_title";
    public static final String err_client_name = "err_client_name";
    public static final String err_mob_lent = "err_mob_lent";
    public static final String err_alter_mob_lent = "err_alter_mob_lent";
    public static final String err_addr = "err_addr";
    public static final String err_empty_Title = "err_empty_Title";
    public static final String err_start_end_date = "err_start_end_date";
    public static final String err_due_start_date = "err_due_start_date";
    public static final String err_start_end_time = "err_start_end_time";
    public static final String err_due_start_time = "err_due_start_time";
    public static final String title_detail = "title_detail";//title for bottom navigation
    public static final String location = "location";
    public static final String map = "map";
    public static final String view = "view";
    public static final String no_desc = "no_desc";
    public static final String instr = "instr";
    public static final String no_instr = "no_instr";
    public static final String fieldworkers = "fieldworkers";
    public static final String no_fw_available = "no_fw_available";
    public static final String tags = "tags";
    public static final String job_tags = "job_tags";
    public static final String no_tag = "no_tag";
    public static final String status_dialog = "status_dialog";
    public static final String job_status_change = "job_status_change";
    public static final String dialog_alert = "dialog_alert";
    public static final String item_sync = "item_sync";
    public static final String job_det_email = "job_det_email";
    public static final String desc_no = "desc_no";
    public static final String no_inst = "no_inst";
    public static final String no_avail = "no_avail";
    public static final String ok = "ok";
    public static final String no_contact_available = "no_contact_available";
    public static final String high = "high";
    public static final String no_Item_generate_inv = "no_Item_generate_inv";
    public static final String dialog_error_title = "dialog_error_title";
    public static final String title_chat = "title_chat";
    public static final String title_invoice = "title_invoice";
    public static final String title_payment = "title_payment";
    public static final String items_screen_title = "items_screen_title";
    public static final String list_item = "list_item";
    public static final String qty = "qty";
    public static final String addItem_screen_title = "addItem_screen_title";
    public static final String item = "item";
    public static final String fieldworker = "fieldworker";
    public static final String items_name = "items_name";
    public static final String part_no = "part_no";
    public static final String description = "description";
    public static final String rate = "rate";
    public static final String unit = "unit";
    public static final String discount = "discount";
    public static final String tax = "tax";
    public static final String tax_amount = "tax_amount";
    public static final String no_invoice = "no_invoice";
    public static final String invoice_date = "invoice_date";
    public static final String due_date = "due_date";
    public static final String print_invoice = "print_invoice";
    public static final String email_invoice = "email_invoice";
    public static final String email_quotes = "email_quotes";
    public static final String quote_to_job = "quote_to_job";
    public static final String subject = "subject";
    public static final String message = "message";
    public static final String discountError = "discountError";
    public static final String please_enter_amount = "please_enter_amount";
    public static final String contact_added_successfully = "contact_added_successfully";
    public static final String contact_updated_successfully = "contact_updated_successfully";
    public static final String chat_msg_hint = "chat_msg_hint";
    public static final String users = "users";
    public static final String camera = "camera";
    public static final String gallery = "gallery";
    public static final String today = "today";
    public static final String time = "time";
    public static final String title_feedback = "title_feedback";
    public static final String feed_head = "feed_head";
    public static final String feed_sub_head = "feed_sub_head";
    public static final String sign = "sign";
    public static final String title_history = "title_history";
    public static final String title_documents = "title_documents";
    public static final String total_inv_amt = "total_inv_amt";
    public static final String paid_amt = "paid_amt";
    public static final String due_amt = "due_amt";
    public static final String pay_date = "pay_date";
    public static final String lable_pay_type = "lable_pay_type";
    public static final String amt = "amt";
    public static final String notes = "notes";
    public static final String title_cutomform = "title_cutomform";
    public static final String fieldworkers_name = "fieldworkers_name";
    public static final String confirmation = "confirmation";
    public static final String cancel = "cancel";
    public static final String remove = "remove";
    public static final String done = "done";
    public static final String settings = "settings";
    public static final String update = "update";
    public static final String contacts_screen_title = "contacts_screen_title";
    public static final String add_contacts_screen_title = "add_contacts_screen_title";
    public static final String success = "success";
    public static final String reject = "reject";
    public static final String install_the_twitter_app = "install_the_twitter_app";
    public static final String install_the_skype_app = "install_the_skype_app";
    public static final String do_you_want_change_name_image = "do_you_want_change_name_image";
    public static final String fill_all_mandatory_questions = "fill_all_mandatory_questions";
    public static final String enter_subject = "enter_subject";
    public static final String input_text_email = "input_text_email";
    public static final String enter_receipt_email_id = "enter_receipt_email_id";
    public static final String limit_mid = "limit_mid";
    public static final String limit_high = "limit_high";
    public static final String session_expire = "session_expire";
    public static final String status_new = "status_new";//we can't give name new keyword
    public static final String accepted = "accepted";
    public static final String travelling = "travelling";
    public static final String break_key = "break";    //we can't give name break keyword
    public static final String In_progress = "In_progress";
    public static final String on_hold = "on_hold";
    public static final String completed = "completed";
    public static final String edit = "edit";
    public static final String job_code = "job_code";
    public static final String status_tr_fin_st = "status_tr_fin_st";
    public static final String no = "no";
    public static final String yes = "yes";
    public static final String job_not_fount = "job_not_fount";
    public static final String payment_recv = "payment_recv";
    public static final String overview = "overview";
    public static final String add_client = "add_client";
    public static final String edit_client = "edit_client";
    public static final String edit_contact = "edit_contact";
    public static final String more = "more";
    public static final String documet_appear = "documet_appear";
    public static final String save_for_future_use = "save_for_future_use";
    public static final String add_your_ans = "add_your_ans";
    public static final String add_new_item = "add_new_item";
    public static final String item_desc = "item_desc";
    public static final String qty_hr = "qty_hr";
    public static final String item_not_found = "item_not_found";
    public static final String user_logout_msg = "user_logout_msg";
    public static final String hint_empty = "hint_empty";
    public static final String custom_form_list = "custom_form_list";
    public static final String client_syn = "client_syn";
    public static final String no_filter = "no_filter";
    public static final String empty_tag = "empty_tag";
    public static final String enter_ans = "enter_ans";
    public static final String update_item = "update_item";
    public static final String item_empty = "item_empty";
    public static final String remove_item_msg = "remove_item_msg";
    public static final String invoice_remove = "invoice_remove";
    public static final String doc_remove = "doc_remove";
    public static final String update_app_message = "update_app_message";
    public static final String offline_feature_alert = "offline_feature_alert";
    public static final String payment_ntwrk = "payment_ntwrk";
    public static final String payment_type = "payment_type";
    public static final String amount_required = "amount_required";
    public static final String invoice_details = "invoice_details";
    public static final String add_edit_item = "add_edit_item";
    public static final String fw_valid = "fw_valid";
    public static final String job_resume = "job_resume";
    public static final String job_finish = "job_finish";
    public static final String resume = "resume";
    public static final String travel_start = "travel_start";
    public static final String accept = "accept";
    public static final String something_wrong = "something_wrong";
    public static final String Sche_end_start_time = "Sche_end_start_time";
    public static final String quotes_end_start_time = "quotes_end_start_time";
    public static final String no_chat = "no_chat";
    public static final String no_location = "no_location";
    public static final String job_details = "job_details";
    public static final String sync_msg = "sync_msg";
    public static final String client_details = "client_details";
    public static final String account_details = "account_details";
    public static final String contact_chkBox = "contact_chkBox";
    public static final String title_language = "language";
    public static final String add_fieldworker = "add_fieldworker";
    public static final String add_tag = "add_tag";
    public static final String skip = "skip";
    public static final String file_cant_open = "file_cant_open";
    public static final String changeLanguage = "changeLanguage";
    public static final String to = "to";
    public static final String cc = "cc";
    public static final String err_valid_email_in_cc = "err_valid_email_in_cc";
    public static final String loading = "loading";
    public static final String select_option = "select_option";
    public static final String please_select_country_first = "please_select_country_first";
    public static final String document = "document";
    public static final String date = "date";
    public static final String zip = "zip";
    public static final String recent = "recent";
    public static final String client_detail = "client_detail";
    public static final String clt_added = "clt_added";
    public static final String no_form_added_for_this_job = "no_form_added_for_this_job";
    public static final String project_site_updated_successfully = "project_site_updated_successfully";
    public static final String clt_updated = "clt_updated";
    public static final String enter_file_name = "enter_file_name";
    public static final String contact_not_sync = "contact_not_sync";
    public static final String view_details = "view_details";
    public static final String supplier_cost = "supplier_cost";
    public static final String services_name = "services_name";
    public static final String service_error = "service_error";
    public static final String check_out = "check_out";
    public static final String check_in = "check_in";
    public static final String latitude = "latitude";
    public static final String longitued = "longitued";
    public static final String check_in_out_fail = "check_in_out_fail";
    public static final int MOBILE_LIMIT = 7;
    public static final String title_cutom_field = "title_cutom_field";
    public static final String title_equ_details = "title_equ_details";
    public static final String status_detail = "status_detail";
    public static final String tariff_rate = "tariff_rate";
    public static final String brand_name = "brand_name";
    public static final String go_to_job = "go_to_job";
    public static final String go_to_audit = "go_to_audit";
    public static final String setting_report = "setting_report";
    public static final String report_msg = "report_msg";
    public static final String item_qty_update = "item_qty_update";
    public static final String equipment_model = "equipment_model";
    public static final String equipment_serial = "equipment_serial";
    public static final String equipment_btn = "equipment_btn";
    public static final String err_auditorsname = "err_auditorsname";
    /**
     * key's for registration
     */
    public static final String err_compny_message1 = "Please enter company name";
    public static final String compny_length_check1 = "Minimum 3 characters required for company name. ";
    public static final String email_empty1 = "Please enter email id";
    public static final String email_error1 = "Please enter valid email";
    public static final String empty_pass1 = "Please enter password";
    public static final String pass_length_check1 = "Password length must be between 3 to 15 characters";
    public static final String err_server_type1 = "Please Select a server location";
    public static final String please_select_country_first1 = "Please select country first";
    public static final String state_error1 = "Please enter state";
    public static final String err_currenyformat_type1 = "Please Select a currency format";
    public static final String err_timezone_type1 = "Please Select a timezone";
    public static final String email_add_alredy_taken1 = "Email adrress already taken, please try another";
    public static final String almost_done_message1 = "Your account has been activated successfully. You can now login.";
    public static final String invalid_domain = "You have entered invalid domain name.";
    public static final String mark_as_done = "mark_as_done";
    public static final String capture_barcode = "capture_barcode";
    public static final String barcode_capture_note = "barcode_capture_note";
    public static final String add_contact_name = "add_contact_name";
    /**
     * **** new keys for language support
     ******/


    public static String tax_rate = "tax_rate";
    public static String job_not_sync = "job_not_sync";
    public static String site_not_sync = "site_not_sync";
    public static String item_not_sync = "item_not_sync";
    public static String ALREADY_SYNC = "2";
    public static boolean location_checker_enable = true;
    public static String updateAppMsg = "There is newer version of this application available on play store, please update";
    public static String[] cameraPermissions = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };
    public static String[] galleryPermissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };
    public static String No = "No";
    public static String Update = "Update";
    public static String Sync_alert = "Sync Alert";
    public static String Clear_data = "Clear data";
    public static String quote_desc = "quote_desc";
    public static String quote_instr = "quote_instr";
    public static String quote_status = "quote_status";
    public static String quote_date = "quote_date";
    public static String search_quote_code_client_name_address = "search_quote_code_client_name_address";
    public static String quote_detail = "quote_detail";
    public static String email_quote = "email_quote";
    public static String print_quote = "print_quote";
    public static String create_quote = "create_quote";
    public static String create_job = "create_job";
    public static String create_client = "create_client";
    public static String create_contact = "create_contact";
    public static String create_project = "create_project";
    public static String landmark_addjob = "landmark_addjob";
    public static String audit_nav = "audit_nav";
    public static String user_leave = "user_leave";
    public static String detail_audit = "detail_audit";
    public static String detail_equipment = "detail_equipment";
    public static String detail_scan_barcode = "detail_scan_barcode";
    public static String detail_report = "detail_report";
    public static String auditors = "auditors";
    public static String no_auditor_available = "no_auditor_available";
    public static String condition = "condition";
    public static String remark = "remark";
    public static String customer_signature = "customer_signature";
    public static String auditor_signature = "auditor_signature";
    public static String audit_code = "audit_code";
    public static String start = "start";
    public static String end = "end";
    public static String equipment_not_found = "equipment_not_found";
    public static String audit_not_found = "audit_not_found";
    public static String audit_status_change = "audit_status_change";
    public static String audit_document_msg = "audit_document_msg";
    public static String audit_email_msg = "audit_email_msg";
    public static String add_remark = "add_remark";
    public static String update_remark = "update_remark";
    public static String remark_msg = "remark_msg";
    public static String remove_item_mandtry = "remove_item_mandtry";
    public static String scan_barcode_manually = "scan_barcode_manually";
    public static String audit_search_hint = "audit_search_hint";
    public static String report_mandatory_msg = "report_mandatory_msg";
    public static String invoice_details_not_found = "invoice_details_not_found";
    public static String no_quotes_found = "no_quotes_found";
    public static String client_fw_chat = "client_fw_chat";
    public static String completion_note = "completion_note";
    public static String doc_name = "doc_name";
    public static String doc_des_op = "doc_des_op";
    public static String optional = "optional";
    public static String sub_up = "sub_up";
    public static String customize = "customize";
    public static String term_condition = "term_condition";
    public static String side_menu_title_chats = "side_menu_title_chats";
    public static String search_by_username = "search_by_username";
    public static String account_deactivated = "account_deactivated";
    public static String deactivated_msg = "deactivated_msg";
    public static String trying_to_chat = "trying_to_chat";
    public static String today_task = "today_task";
    /****Add expenses***/
    public static String title_expence = "title_expence";
    public static String title_report = "title_report";
    public static String add_leave = "add_leave";
    public static String title_timeSheet = "title_timeSheet";
    public static String expense_nm = "expense_nm";
    public static String expense_date = "expense_date";
    public static String expense_category = "expense_category";
    public static String expense_amount = "expense_amount";
    public static String expense_description = "expense_description";
    public static String expense_group = "expense_group";
    public static String expense_link = "expense_link";
    public static String expense_jobid = "expense_jobid";
    public static String expense_clientid = "expense_clientid";
    public static String expense_upload = "expense_upload";
    public static String add_expense = "add_expense";
    public static String click_here = "click_here";
    public static String expense_none = "expense_none";
    public static String claim_reimbu = "claim_reimbu";
    public static String expense_details = "expense_details";
    public static String expense_search_name = "expense_search_name";
    public static String expense_history = "expense_history";
    public static String expense_nm_required = "expense_nm_required";
    public static String expense_amount_required = "expense_amount_required";
    public static String expense_edit = "expense_edit";
    public static String expense_not_found = "expense_not_found";
    public static String expense_added = "expense_added";
    public static String expense_update = "expense_update";
    public static String job_validation = "job_validation";
    public static String client_validation = "client_validation";
    public static String unschedule_job = "unschedule_job";
    public static String link_site = "link_site";
    public static String select_site = "select_site";
    /****Appoinment key's**/
    public static String title_appointments = "title_appointments";
    public static String appointment_not_found = "appointment_not_found";
    public static String appointment_attach_msg = "appointment_attach_msg";
    public static String appointment_add_new_attach = "appointment_add_new_attach";
    public static String add_appointment = "add_appointment";
    public static String add_attachment = "add_attachment";
    public static String appointment_schedule = "appointment_schedule";
    public static String appointment_start_end = "appointment_start_end";
    public static String appointment_details = "appointment_details";
    public static String view_on_map = "view_on_map";
    public static String appoint_email_not_available = "appoint_email_not_available";
    public static String job = "job";
    public static String appointment = "appointment";
    public static String appointment_not_sync = "appointment_not_sync";
    public static String appointment_edit = "appointment_edit";
    public static String update_appointment = "update_appointment";
    public static String create_quotation = "create_quotation";
    public static String export_document = "export_document";
    public static String select_all = "select_all";
    public static String send_email = "send_email";
    public static String email_document = "email_document";
    public static String view_and_send = "view_and_send";
    public static String contact = "contact";
    public static String existing = "existing";
    public static String create_new_contact = "create_new_contact";
    public static String job_services_use = "job_services_use";
    public static String job_fieldworker_use = "job_fieldworker_use";
    public static String close = "close";
    public static String please_contact_admin = "please_contact_admin";
    public static String send_payment_reciept = "send_payment_reciept";
    public static String invoice_can_not_generate_msg = "invoice_can_not_generate_msg";
    public static String reschedule = "reschedule";
    public static String revisit = "revisit";
    public static String require_revisit = "require_revisit";
    public static String revisit_msg_validation = "revisit_msg_validation";
    public static String schedule_date_required = "schedule_date_required";
    public static String close_completed_job_msg = "close_completed_job_msg";
    public static String user_not_found = "user_not_found";
    public static String euipment_remark_submit = "euipment_remark_submit";
    public static String euipment_remark_update = "euipment_remark_update";
    public static String app_language = "app_language";
    public static String default_view = "default_view";
    public static String start_date_and_time = "start_date_and_time";
    public static String end_date_and_time = "end_date_and_time";
    public static String select_doc_validation = "select_doc_validation";
    public static String report_submit = "report_submit";
    public static String report_offline = "report_offline";
    public static String custom_filed_edit_msg = "custom_filed_edit_msg";
    public static String job_equipment = "job_equipment";
    public static String quotation_label = "quotation_label";
    public static String recent_quote = "recent_quote";
    public static String status_required = "status_required";
    public static String attachment = "attachment";
    public static String reference = "reference";
    public static String doc_here = "doc_here";
    public static String google = "google";
    public static String media = "media";
    public static String other = "other";
    public static String title_add_equipment = "title_add_equipment";
    public static String equipment = "equipment";
    public static String model_no = "model_no";
    public static String supplier = "supplier";
    public static String manufacture_date = "manufacture_date";
    public static String equipment_category = "equipment_category";
    public static String equipment_group = "equipment_group";
    public static String brand = "brand";
    public static String serial_no = "serial_no";
    public static String purchase_date = "purchase_date";
    public static String warranty_expiry_date = "warranty_expiry_date";
    public static String type = "type";
    public static String owner = "owner";
    public static String serv_prov = "serv_prov";
    public static String gen_bar_code = "gen_bar_code";
    public static String equp_nm_req = "equp_nm_req";
    public static String equp_nm_minimun = "equp_nm_minimun";
    public static String add_images = "add_images";
    public static String job_start = "job_start";
    public static String convert_item_to_equ = "convert_item_to_equ";
    public static String contract = "contract";
    public static String err_empty_expense_Group = "err_empty_expense_Group";
    public static String err_empty_expense_Category = "err_empty_expense_Category";
    public static String err_empty_equipment_Category = "err_empty_equipment_Category";
    public static String err_empty_equipment_Group = "err_empty_equipment_Group";
    public static String err_empty_equipment_Brand = "err_empty_equipment_Brand";
    public static String audit_equipment_not_found = "audit_equipment_not_found";
    public static String equipment_sync_button_text = "equipment_sync_button_text";
    public static String equ_as_a_part = "equ_as_a_part";
    public static String item_convrt_count = "item_convrt_count";
    public static String are_you_sure = "are_you_sure";
    public static String remark_on = "remark_on";
    public static String recent_job = "recent_job";
    public static String create_audit = "create_audit";
    public static String audit_desc = "audit_desc";
    public static String audit_instr = "audit_instr";
    public static String auditors1 = "auditors1";
    public static String title_add_audit = "title_add_audit";
    public static String audit_not_sync = "audit_not_sync";
    public static String add_new_site = "add_new_site";
    public static String add_job_offline_msg = "add_job_offline_msg";
    public static String selected_tax_lable = "selected_tax_lable";
    public static String applied_tax = "applied_tax";
    public static String email_job_card = "email_job_card";
    public static String print_job_card = "print_job_card";
    public static String location_permission = "location_permission";
    public static String location_msg = "location_msg";
    public static String turn_on = "turn_on";
    public static String no_thanks = "no_thanks";
    public static String equipment_audit = "equipment_audit";
    public static String equipment_service = "equipment_service";
    public static String upcoming = "upcoming";
    public static String overdue = "overdue";
    public static String work_history = "work_history";
    public static String no_appointment_found = "no_appointment_found";
    public static String add_custom_recur = "add_custom_recur";
    public static String custom_recur = "custom_recur";
    public static String custom_recur_msg1 = "custom_recur_msg1";
    public static String custom_recur_msg2 = "custom_recur_msg2";
    public static String custom_recur_msg3 = "custom_recur_msg3";
    public static String infinity = "infinity";
    public static String add_rucr_for_job = "add_rucr_for_job";
    public static String starting_on = "starting_on";
    public static String recuring_pattenr = "recuring_pattenr";
    public static String range_of_recurence = "range_of_recurence";
    public static String radio_weekDay = "radio_weekDay";
    public static String radio_everyDay = "radio_everyDay";
    public static String schel_start = "schel_start";
    public static String radio_no_end_date = "radio_no_end_date";
    public static String radio_end_after = "radio_end_after";
    public static String radio_end_by = "radio_end_by";
    public static String occurance = "occurance";
    public static String day_s = "day_s";
    public static String date_validation = "date_validation";
    public static String select_end_date = "select_end_date";
    public static String dont_create_recur = "dont_create_recur";
    public static String custom_recur_pattern = "custom_recur_pattern";
    public static String never_end = "never_end";
    public static String site_name_show_permisson = "site_name_show_permisson";
    public static String select_week_days = "select_week_days";
    public static String weekly_msg1 = "weekly_msg1";
    public static String weeks = "weeks";
    public static String months_starting_on = "months_starting_on";
    public static String every = "every";
    public static String check_mon = "check_mon";
    public static String check_tues = "check_tues";
    public static String check_wedns = "check_wedns";
    public static String check_thurs = "check_thurs";
    public static String check_friday = "check_friday";
    public static String check_satur = "check_satur";
    public static String check_sun = "check_sun";
    public static String first = "first";
    public static String second = "second";
    public static String third = "third";
    public static String forth = "forth";
    public static String last = "last";
    public static String weeks_on = "weeks_on";
    public static String of_every = "of_every";
    public static String months_txt = "months_txt";
    public static String daily = "daily";
    public static String weekly = "weekly";
    public static String monthly = "monthly";
    public static String the_radio = "the_radio";
    public static String recuring_job = "recuring_job";
    public static String day = "day";
    public static String max_day = "max_day";
    public static String custom = "custom";
    public static String normal_recur = "normal_recur";
    public static String job_recur = "job_recur";
    public static String stop_recur = "stop_recur";
    public static String sch_time_required = "sch_time_required";
    public static String delete_recur_msg = "delete_recur_msg";
    public static String do_you_want_save_client = "do_you_want_save_client";
    public static String link_equipment = "link_equipment";
    public static String all = "all";
    public static String linked = "linked";
    public static String unlinked = "unlinked";
    public static String contract_link_msg = "contract_link_msg";
    public static String client_link_msg = "client_link_msg";
    public static String link_client_equipment = "link_client_equipment";
    public static String link_own_equipment = "link_own_equipment";
    public static String unlink_info_msg = "unlink_info_msg";
    public static String add_user_mannual = "add_user_mannual";
    public static String view_user_mannual = "view_user_mannual";
    public static String remove_the_image_tag = "remove_the_image_tag";
    public static String after = "after";
    public static String before = "before";
    public static String save_as_completion_notes = "save_as_completion_notes";
    public static String menu_upload_sign = "menu_upload_sign";
    public static String customer_sign_head = "customer_sign_head";
    public static String valid_site_check = "valid_site_check";
    public static String valid_contact_check = "valid_contact_check";
    public static String update_desc_append_complition = "update_desc_append_complition";
    public static String customer_sign_required = "customer_sign_required";
    public static String sign_uploaded = "sign_uploaded";
    public static String modifyed_desc_append_complition = "modifyed_desc_append_complition";
    public static String upld_fil = "upld_fil";
    public static String parts = "parts";
    public static String search_by_site = "search_by_site";
    public static String contact_details = "contact_details";
    public static String schedule_details = "schedule_details";
    public static String get_direction = "get_direction";
    public static String signature_pad = "signature_pad";
    public static String location_not_found = "location_not_found";
    public static String loc_permission_deny = "loc_permission_deny";
    public static String Equipment_img = "Equipment_img";
    public static String lat_and_lng = "lat_and_lng";
    public static String get_current_lat_long = "get_current_lat_long";
    public static String no_suggesstion = "no_suggesstion";
    public static String map_on_view = "map_on_view";
    public static String add_new_audit = "add_new_audit";
    public static String add_audit_offline_msg = "add_audit_offline_msg";
    public static String add_new_auotes = "add_new_auotes";
    public static String add_quotes_offline_msg = "add_quotes_offline_msg";
    public static String add_new_apppinment = "add_new_apppinment";
    public static String add_appoinment_offline_msg = "add_appoinment_offline_msg";
    public static String update_new_quotes = "update_new_quotes";
    public static String update_new_appoinment = "update_new_appoinment";
    public static String update_appoinment_offline_msg = "update_appoinment_offline_msg";
    public static String report_from = "report_from";
    public static String report_to = "report_to";
    public static String download_report = "download_report";
    public static String download_report_lable_stext = "download_report_lable_stext";
    public static String time_sheet_header = "time_sheet_header";
    public static String time_sheet_sub_header = "time_sheet_sub_header";
    public static String time_sheet_from = "time_sheet_from";
    public static String time_sheet_to = "time_sheet_to";
    public static String from_date_error = "from_date_error";
    public static String to_date_error = "to_date_error";
    public static String time_report_date_error = "time_report_date_error";
    public static String select_date_range = "select_date_range";
    public static String time_sheet_date_error = "time_sheet_date_error";
    public static String leave_date_error = "leave_date_error";
    public static String loca_tax_based = "loca_tax_based";
    public static String loc_tax_msg = "loc_tax_msg";
    public static String select = "select";
    public static String equ_not_found = "equ_not_found";
    public static String taxLoc_not_found = "taxLoc_not_found";
    public static String non_billable = "non_billable";
    public static String billable = "billable";
    public static String text_default = "text_default";
    public static String eqi_not_foun_txt = "eqi_not_foun_txt";
    public static String not_msg = "not_msg";
    public static String new_on_hold = "new_on_hold";
    public static String send_client_mail = "send_client_mail";
    public static String quotes_details = "quotes_details";
    public static String quotes_num = "quotes_num";
    public static String add_notes_leave = "add_notes_leave";
    public static String add_reason_leave = "add_reason_leave";
    public static String all_day_leave = "all_day_leave";
    public static String rate_inclu_tax = "rate_inclu_tax";
    public static String last_serv_date = "last_serv_date";
    public static String barcode_num = "barcode_num";


    public static String server(String location) {

        switch (location) {
            case "America":
                return REGISTER_US;

            case "Asia":
                return REGISTER_AS;

            case "Australia":
                return REGISTER_AU;

            case "Europe":
                return REGISTER_UK;

        }
        return BASEURL;

    }

}

//el -> greek, nl ->dutch, es -> spanish , de->german , fr->french ,it ->italin ,iw ->hebrew
// ko->korean pt->Portuguese ru->russia, ja->japanees ,hi->hindi


// keytool -list -v -keystore /home/aplite/Desktop/Live_data/keystorefile -alias  eyeontask -storepass Eyeontask@1 -keypass Eyeontask@1


//SVN URL for WFH  https://103.231.46.90:8444/svn/eyeontask/trunk/EyeOnTask/Android/EoT_App/app/src/main/java/com/eot_app/utility/AppConstant.java

//SVN URL for WFO https://192.168.88.2:8443/svn/eyeontask/trunk/EyeOnTask/Admin/en/services