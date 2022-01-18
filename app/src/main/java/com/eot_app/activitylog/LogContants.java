package com.eot_app.activitylog;

public interface LogContants {

    /**
     * module name -> 1-login, 2-client, 3-site, 4-contact, 5-job,
     * 6-quaotation, 7-user, 8-contract, 9-item, 10-inventory,
     * 11-logout, 12-invoice, 13-audit, 14-equipment, 15-dashboard,
     * 16-schedular, 17-map, 18-reports, 19-setting, 20-project
     */
    //current visitedModule
    // Module Names
    String LOGIN_MODULE = "1";
    String JOB_MODULE = "5";
    String CLIENT_MODULE = "2";
    String AUDIT_MODULE = "13";
    String QUOTE_MODULE = "6";


    //Login apis
    String LOGIN_URL = "get url";
    String LOGIN_USER = "user login";
    String LOGIN_SYNC_SETTING = "sync default setting";
    String LOGIN_SYNC_USER_LIST_CHAT = "sync group user list chat";
    String LOGIN_CLIENT_SITE_SYN = "client site sync";
    String LOGIN_CLIENT_CONTACT_SYN = "client contact sync";
    String LOGIN_CLIENT_SYNC = "client sync";
    String LOGIN_JOB_SYNC = "job sync";

    //Quote apis
    String QUOTE_LIST = "Quote list";
    String QUOTE_ADD = "add quote";
    String QUOTE_UPDATE = "update quote";
    String QUOTE_TERM = "get term";
    String QUOTE_INVOICE_DETAILS = "Quote invoice details";
    String QUOTE_GENERATE_PDF = "Quote pdf generate";
    String QUOTE_REMOVE_ITEM = "Quote item removed";
    String QUOUTE_CONVERT_JOB = "Quote to job convert";


    //Audit apis
    String AUDIT_LIST = "seen audit list";
    String AUDIT_DETAILS = "seen audit details";
    String AUDIT_STATUS = "change audit status";
    String AUDIT_EQUIP = "seen equipment list";
    String AUDIT_REMARK = "add remark";
    String AUDIT_SCAN_EQUIP = "scan barcode";
    String AUDIT_ADD_REPORT = "add report";
    String AUDIT_DOC_LIST = "seen audit document list";
    String AUDIT_UPLOAD_DOC = "seen upload document";
    String AUDIT_ADD = "Add audit";
    //Client apis
    String CLIENT_SYNC = "sync client list";
    String CLIENT_ADD = "client add";
    String SITE_LIST = "site list";
    String SITE_ADD = "site add";
    String SITE_EDIT = "site edit";
    String CONTACT_SYNC = "contact sync";
    String UPDATE_CLIENT = "update client";
    String CLIENT_ADD_CONTACT = "Add Contact";
    String CLIENT_EDIT_CONTACT = "Edit Contact";


    //Job APi Names
    String JOB_LIST = "seen Job List";
    String JOB_ADD = "Add Job";
    String JOB_DETAILS = "seen Job Details";
    String JOB_STATUS = "Change Job Status";
    String JOB_COMPLETION_NOTE = "Add Completion note";
    String JOB_ADD_INVOICE_ITEM = "Add/update invoice item";
    String JOB_INVOICE_LIST = "seen Invoice List";
    String JOB_INVOICE_ITEM_DELETE = "Delete Invoice Item";
    String JOB_FEEDBACK = "Submit Feedback";
    String JOB_HISTORY = "seen Job History";
    String JOB_DOCUMENT_LIST = "seen Job Document List";
    String JOB_UPLOAD_DOC = "Upload document";
    String JOB_INVOICE_DETAILS = "seen Invoice Details";
    String JOB_POST_PAYMENT_INVOICE = "Post payment invoice";
    String JOB_CUSTOM_FORM_LIST = "seen custom form list";
    String JOB_SUBMIT_ANSER = "submit answer";
    String JOB_GET_QUESTON = "get question";
    String JOB_GET_INVOICE_TEMP = "get invoice email template";
    String JOB_SEND_INVOICE_TEMP = "send email template";
    String JOB_SEND_QUOTE_TEMP = "send quotation email template";
    String JOB_GET_EMAIL_QOUTE = "get quote for email template";
    String JOB_GENERATE_INVOICE_PDF = "generate invoice pdf";


}
