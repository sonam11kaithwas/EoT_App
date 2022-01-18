package com.eot_app.utility.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.TeamMemrConverter;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.userchat_db.UserChatDao;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.dbappointment.AppointmentDao;
import com.eot_app.nav_menu.audit.audit_db.AuditDao;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquCategoryConvrtr;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentTypeConverter;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.client_db.ClientDao;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactDao;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.SiteCustomFieldConverter;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.SiteDao;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.jobs.add_job.JobRecurTypeConvert;
import com.eot_app.nav_menu.jobs.job_db.EquArrayConvrtr;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JobDao;
import com.eot_app.nav_menu.jobs.job_db.SelecetedDaysConverter;
import com.eot_app.nav_menu.jobs.job_db.TagDataConverter;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.typeconver_pkg.InvoiceItemDataModelConverter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.invebtry_items_dao.invebtry_ItemDao;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxesLocation;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxesLocationDao;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.tax_dao.Invoice_TaxDao;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.tax_dao.TaxConverter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOfflineDataDao;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOfflineDataModel;
import com.eot_app.time_shift_pkg.ShiftTimeDao;
import com.eot_app.time_shift_pkg.ShiftTimeReSModel;
import com.eot_app.utility.settings.client_refrence_db.ClientRefrenceDao;
import com.eot_app.utility.settings.client_refrence_db.ClientRefrenceModel;
import com.eot_app.utility.settings.clientaccount_db.ClientAccountDao;
import com.eot_app.utility.settings.clientaccount_db.ClientAccountType;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryDao;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryModel;
import com.eot_app.utility.settings.contractdb.ContractDao;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.equipmentdb.Equipment;
import com.eot_app.utility.settings.equipmentdb.EquipmentDao;
import com.eot_app.utility.settings.jobtitle.TaxDataConverter;
import com.eot_app.utility.settings.setting_db.ErrorLog;
import com.eot_app.utility.settings.setting_db.ErrorLogDao;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.FieldWorkerDao;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.settings.setting_db.JobTitleDao;
import com.eot_app.utility.settings.setting_db.OfflineDao;
import com.eot_app.utility.settings.setting_db.Offlinetable;
import com.eot_app.utility.settings.setting_db.SuggestionConverter;
import com.eot_app.utility.settings.setting_db.TagDao;
import com.eot_app.utility.settings.setting_db.TagData;

/**
 * Created by geet-pc on 11/4/18.
 */

@Database(entities = {Job.class, Client.class, ContactData.class, Site_model.class, JobTitle.class,
        ClientAccountType.class, Offlinetable.class, FieldWorker.class, TagData.class, ErrorLog.class,
        UserChatModel.class, Appointment.class
        , ClientIndustryModel.class, Tax.class, Inventry_ReS_Model.class, JobOfflineDataModel.class
        , AuditList_Res.class, ContractRes.class, Equipment.class, TaxesLocation.class, ClientRefrenceModel.class, ShiftTimeReSModel.class},
        version = 21, exportSchema = false)
@TypeConverters({TaxDataConverter.class, TagDataConverter.class, InvoiceItemDataModelConverter.class, TaxConverter.class
        , EquipmentTypeConverter.class, EquArrayConvrtr.class, EquCategoryConvrtr.class
        , SiteCustomFieldConverter.class, JobRecurTypeConvert.class, SelecetedDaysConverter.class
        , SuggestionConverter.class, TeamMemrConverter.class
})

public abstract class AppDataBase extends RoomDatabase {

    static final Migration MIGRATION_14_15 = new Migration(14, 15) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Equipment ADD COLUMN extraField1 TEXT");
            database.execSQL("ALTER TABLE Equipment ADD COLUMN extraField2 TEXT");
            database.execSQL("ALTER TABLE Equipment ADD COLUMN usrManualDoc TEXT");
            database.execSQL("ALTER TABLE Job ADD COLUMN signature TEXT");

        }
    };

    /**
     * Migrate DataBase version 2
     **/
    static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Job ADD COLUMN recurType TEXT");
            database.execSQL("ALTER TABLE Job ADD COLUMN selectedDays TEXT");
            database.execSQL("ALTER TABLE Job ADD COLUMN recurData TEXT");
            database.execSQL("ALTER TABLE Job ADD COLUMN isRecur TEXT");
        }
    };
    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Appointment ADD COLUMN jobId TEXT");
            database.execSQL("ALTER TABLE Appointment ADD COLUMN jobLabel TEXT");
            database.execSQL("ALTER TABLE AuditList_Res ADD COLUMN tempId TEXT");
        }
    };


    static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE AuditList_Res ADD COLUMN attachCount TEXT");
            database.execSQL("ALTER TABLE Job ADD COLUMN attachCount TEXT");
            database.execSQL("ALTER TABLE Appointment ADD COLUMN attachCount TEXT");
            database.execSQL("ALTER TABLE Tax ADD COLUMN percentage TEXT");
        }
    };


    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            /***CREATE Table for equipment List**/
            database.execSQL("CREATE TABLE IF NOT EXISTS `Equipment` (`equId` TEXT NOT NULL UNIQUE,'cltId' TEXT," +
                    "'equnm' TEXT,'nm' TEXT,'mno' TEXT,'parentId' TEXT," +
                    "'sno' TEXT,'brand' TEXT,'rate' TEXT," +
                    "'supId' TEXT,'supplier' TEXT,'notes' TEXT," +
                    "'expiryDate' TEXT,'manufactureDate' TEXT,'purchaseDate' TEXT," +
                    "'barcode' TEXT,'isusable' TEXT," +
                    "'barcodeImg' TEXT,'adr' TEXT," +
                    "'city' TEXT,'state' TEXT," +
                    "'ctry' TEXT,'zip' TEXT," +
                    "'status' TEXT,'type' TEXT," +
                    "'ecId' TEXT,'egId' TEXT," +
                    "'ebId' TEXT,'isdelete' TEXT," +
                    "'image' TEXT,'isDisable' TEXT," +
                    "'lastAuditLabel' TEXT,'lastAuditDate' TEXT," +
                    "'equStatusOnAudit' TEXT,'lastAuditId' TEXT," +
                    "'lastJobLabel' TEXT,'lastJobDate' TEXT," +
                    "'equStatusOnJob' TEXT,'lastJobId' TEXT," +
                    "'groupName' TEXT,"
                    + " PRIMARY KEY(`equId`)) ");


            /***CREATE Table for contract List**/
            database.execSQL("CREATE TABLE IF NOT EXISTS `ContractRes` (`contrId` TEXT NOT NULL UNIQUE,'label' TEXT," +
                    "'amount' TEXT,'type' TEXT,'payType' TEXT," +
                    "'startDate' TEXT,'endDate' TEXT,'status' TEXT," +
                    "'nm' TEXT,'remainingAmt' TEXT,'cltId' TEXT," +
                    "'paidAmt' TEXT,'invAmount' TEXT,'ccId' TEXT," +
                    "'catgy' TEXT,'isdelete' TEXT,"
                    + " PRIMARY KEY(`contrId`)) ");


            /***add Audit cagatory Array****/

            database.execSQL("ALTER TABLE AuditList_Res ADD COLUMN equCategory TEXT");
            database.execSQL("ALTER TABLE AuditList_Res ADD COLUMN auditType TEXT");


            /**add Job Equipment cagatory Array & type****/
            database.execSQL("ALTER TABLE job ADD COLUMN equCategory TEXT");

            /**add Client Site CustomFiled's****/
            database.execSQL("ALTER TABLE Site_model ADD COLUMN customFieldArray TEXT");
        }
    };


    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE client ADD COLUMN referral TEXT");
            database.execSQL("ALTER TABLE Inventry_ReS_Model ADD COLUMN searchKey TEXT");
            database.execSQL("ALTER TABLE Inventry_ReS_Model ADD COLUMN serialNo TEXT");

        }
    };
    static final Migration MIGRATION_16_17 = new Migration(16, 17) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE Inventry_ReS_Model ADD COLUMN isBillable TEXT");


        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table job add column equArray text");
            database.execSQL("alter table job add column contrId text");

            database.execSQL("alter table Appointment add column quotId text");
            database.execSQL("alter table Appointment add column quotLabel text");

            /***CREATE Table for Audit List**/
            database.execSQL("CREATE TABLE IF NOT EXISTS `AuditList_Res` (`audId` TEXT NOT NULL UNIQUE,'parentId' TEXT," +
                    "'cltId' TEXT,'siteId' TEXT,'conId' TEXT," + "'contrId' TEXT,'label' TEXT,'nm' TEXT," + "'cnm' TEXT,'snm' TEXT,'des' TEXT," +
                    "'type' TEXT,'prty' TEXT,'kpr' TEXT," + "'athr' TEXT,'schdlStart' TEXT,'schdlFinish' TEXT," + "'inst' TEXT,'email' TEXT,'mob1' TEXT," +
                    "'mob2' TEXT,'adr' TEXT,'city' TEXT," + "'state' TEXT,'ctry' TEXT,'zip' TEXT," + "'createDate' TEXT,'updateDate' TEXT,'lat' TEXT," +
                    "'lng' TEXT,'compid' TEXT,'landmark' TEXT," +
                    "'status' TEXT,'isdelete' TEXT, 'tagData' TEXT,'equArray' TEXT,"
                    + " PRIMARY KEY(`audId`)) ");
        }
    };


    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table job add column itemData text");
            database.execSQL("alter table job add column canInvoiceCreated text");

            database.execSQL("alter table Appointment add column snm text");
            database.execSQL("alter table Appointment add column cnm text");


            /***CREATE Table for Job Offline Dao**/
            database.execSQL(("CREATE TABLE IF NOT EXISTS `InvoiceItemDataModel` (`itemId` TEXT NOT NULL UNIQUE," +
                    "'tempNm' TEXT, 'inm' TEXT, 'ijmmId' TEXT, 'dataType' TEXT, 'itemType' TEXT, 'rate' TEXT," +
                    "'qty' TEXT,'discount' TEXT,'des' TEXT,'hsncode' TEXT,'pno' TEXT,'unit' TEXT," +
                    " taxamnt' TEXT', 'supplierCost' TEXT,'isGrouped' TEXT,'tax' TEXT,'jtId' TEXT,"
                    + " PRIMARY KEY(`itemId`)) "));


            /***CREATE Table for Taxex**/
            database.execSQL("CREATE TABLE IF NOT EXISTS `Tax` (`taxId` TEXT NOT NULL UNIQUE,'label' TEXT," +
                    "'isactive' TEXT,'show_Invoice' TEXT,'rate' TEXT,"
                    + " PRIMARY KEY(`taxId`)) ");

            /***CREATE Table for Inventry_ReS_Model ITEM's**/
            database.execSQL("CREATE TABLE IF NOT EXISTS `Inventry_ReS_Model` (`itemId` TEXT NOT NULL UNIQUE,'inm' TEXT," +
                    "'ides' TEXT,'pno' TEXT,'qty' TEXT,'rate' TEXT  ,'discount'" +
                    " TEXT,'type' TEXT,'isactive' TEXT,'show_Invoice' TEXT,'lowStock' TEXT,  " +
                    "'image' TEXT ,'tax' TEXT,'unit' TEXT,'hsncode' TEXT,'supplierCost' TEXT,"
                    + " PRIMARY KEY(`itemId`)) ");


            /***CREATE Table for Job Offline Dao**/
            database.execSQL(("CREATE TABLE IF NOT EXISTS `JobOfflineDataModel` (`id` INTEGER NOT NULL UNIQUE," +
                    "'service_name' TEXT,'tempId' TEXT, " +
                    "'params' TEXT,'timestamp' TEXT,"
                    + " PRIMARY KEY(`id`)) "));

        }
    };
    /***create table for Item***/
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE client ADD COLUMN industryName TEXT");

            database.execSQL("ALTER TABLE ContactData ADD COLUMN siteId TEXT");

            database.execSQL("ALTER TABLE Site_model ADD COLUMN conId TEXT");
            database.execSQL("ALTER TABLE Site_model ADD COLUMN cnm TEXT");


            database.execSQL("CREATE TABLE IF NOT EXISTS `Appointment` (`appId` TEXT NOT NULL UNIQUE,'tempId' TEXT," +
                    "'cltId' TEXT ,'label' TEXT," +
                    "'des' TEXT ,'type' TEXT," +
                    "'kpr' TEXT ,'athr' TEXT," +
                    "'schdlStart' TEXT ,'schdlFinish' TEXT," +
                    "'nm' TEXT ,'email' TEXT," +
                    "'mob1' TEXT ,'mob2' TEXT," +
                    "'adr' TEXT ,'city' TEXT," +
                    "'state' TEXT ,'ctry' TEXT," +
                    "'zip' TEXT ,'createDate' TEXT," +
                    "'updateDate' TEXT ,'compid' TEXT," +
                    "'lat' TEXT ,'lng' TEXT," +
                    "'conId' TEXT ,'siteId' TEXT," +
                    "'landmark' TEXT ,'status' TEXT," +
                    " 'isdelete' TEXT, PRIMARY KEY(`appId`)) ");

            database.execSQL("CREATE TABLE IF NOT EXISTS `ClientIndustryModel` (`industryId` TEXT NOT NULL UNIQUE," +
                    " 'industryName' TEXT," +
                    " 'isDefault' TEXT , PRIMARY KEY(`industryId`)) ");

        }
    };

    /**
     * Migrate DataBase version 2
     **/
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE client ADD COLUMN lat TEXT");
            database.execSQL("ALTER TABLE client ADD COLUMN lng TEXT");
        }
    };

    /**Migrate DataBase version 3**/
    /**
     * introduce new field in job dao
     **/
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table job add column landmark text");
        }
    };


    /**Migrate DataBase version 4 this migrate in version 2.12**/
    /**
     * iadd new field in Client dao
     **/
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE client ADD COLUMN zip TEXT");
        }
    };


    /**Migrate DataBase version 5 this migrate in version 2.12**/
    /**
     * iadd new field in Client dao
     **/
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Job ADD COLUMN complNote TEXT");
        }
    };
    static final Migration MIGRATION_15_16 = new Migration(15, 16) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE JobTitle ADD COLUMN  suggestionList TEXT");
        }
    };

    static final Migration MIGRATION_18_19 = new Migration(18, 19) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Job ADD COLUMN quotLabel TEXT");
        }
    };

    static final Migration MIGRATION_19_20 = new Migration(19, 20) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `ShiftTimeReSModel` (`shiftId` TEXT NOT NULL UNIQUE,'shiftNm' TEXT," +
                    "'isDefault' TEXT,'shiftStartTime' TEXT,'shiftEndTime' TEXT,"
                    + " PRIMARY KEY(`shiftId`)) ");
        }
    };


    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `UserChatModel` (`usrId` TEXT NOT NULL UNIQUE,'fnm' TEXT," +
                    "'lnm' TEXT ,'img' TEXT, 'readCount' INTEGER NOT NULL, PRIMARY KEY(`usrId`)) ");
        }
    };


    static final Migration MIGRATION_17_18 = new Migration(17, 18) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table job add column locId text");
            database.execSQL("alter table Tax add column locId text");
            database.execSQL("alter table Inventry_ReS_Model add column taxType text");
            database.execSQL("alter table JobTitle add column taxType text");
            database.execSQL("alter table Inventry_ReS_Model add column isBillableChange text");


            /***CREATE Table for Tax Location List**/
            database.execSQL("CREATE TABLE IF NOT EXISTS `TaxesLocation` (`locId` TEXT NOT NULL UNIQUE,'stateId' TEXT," +
                    "'location' TEXT," + " PRIMARY KEY(`locId`)) ");

            database.execSQL("CREATE TABLE IF NOT EXISTS `ClientRefrenceModel` (`refId` TEXT NOT NULL UNIQUE,'refName' TEXT," +
                    "'isDefault' TEXT," + " PRIMARY KEY(`refId`)) ");
        }
    };


    static final Migration MIGRATION_20_21 = new Migration(20, 21) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserChatModel ADD COLUMN isTeam TEXT");
            database.execSQL("ALTER TABLE UserChatModel ADD COLUMN teamMemId TEXT");
        }
    };

    private static final String DB_NAME = "eot_db";

    private static AppDataBase INSTANCE;

    public static AppDataBase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDataBase.class, DB_NAME)
                    // To simplify the codelab, allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)//data migration for client
                    .addMigrations(MIGRATION_2_3)//data migration for job

                    /***client details issue **/
                    .addMigrations(MIGRATION_3_4)//data migration for Client section

                    /***job completion note added **/
                    .addMigrations(MIGRATION_4_5)//data migration for job section
                    .addMigrations(MIGRATION_5_6)
                    .addMigrations(MIGRATION_6_7)
                    .addMigrations(MIGRATION_7_8)
                    .addMigrations(MIGRATION_8_9)
                    .addMigrations(MIGRATION_9_10)
                    .addMigrations(MIGRATION_10_11)
                    .addMigrations(MIGRATION_11_12)
                    .addMigrations(MIGRATION_12_13)
                    .addMigrations(MIGRATION_13_14)
                    .addMigrations(MIGRATION_14_15)
                    .addMigrations(MIGRATION_15_16)
                    .addMigrations(MIGRATION_16_17)
                    .addMigrations(MIGRATION_17_18)
                    .addMigrations(MIGRATION_18_19)
                    .addMigrations(MIGRATION_19_20)
                    .addMigrations(MIGRATION_20_21)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract JobDao jobModel();

    public abstract ClientDao clientModel();

    public abstract ContactDao contactModel();

    public abstract SiteDao sitemodel();

    public abstract JobTitleDao jobTitleModel();

    public abstract ClientAccountDao clientAccount();

    public abstract FieldWorkerDao fieldWorkerModel();

    public abstract OfflineDao offlinemodel();

    public abstract TagDao tagmodel();

    public abstract ErrorLogDao errorLogmodel();

    public abstract UserChatDao userChatModel();

    public abstract AppointmentDao appointmentModel();

    public abstract ClientIndustryDao clientIndustryDao();

    public abstract invebtry_ItemDao invoiceItemDao();

    public abstract Invoice_TaxDao invoiceTaxDao();

    public abstract JobOfflineDataDao jobOfflineDao();

    public abstract AuditDao auditDao();

    public abstract ContractDao contractDao();

    public abstract EquipmentDao equipmentDao();

    public abstract TaxesLocationDao taxesLocationDao();

    public abstract ClientRefrenceDao clientRefrenceDao();

    public abstract ShiftTimeDao shiftTimeDao();


}
