package com.eot_app.nav_menu.audit.audit_list.documents.doc_model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ubuntu on 9/10/18.
 */

public class GetFileList_Res implements Parcelable {
    public static final Creator<GetFileList_Res> CREATOR = new Creator<GetFileList_Res>() {
        @Override
        public GetFileList_Res createFromParcel(Parcel in) {
            return new GetFileList_Res(in);
        }

        @Override
        public GetFileList_Res[] newArray(int size) {
            return new GetFileList_Res[size];
        }
    };
    private String attachmentId;
    private String deleteTable;
    private String userId;
    private String image_name;
    private String attachFileName;
    private String attachThumnailFileName;
    private String attachFileActualName;
    private String type;
    private String createdate;
    private String audImg_name;
    //acutalFileName
    private String attachAuditSign;
    //audit thumbnail image
    private String attachThumnailAuditSign;
    //audit file name
    private String attachAuditActualSign;

    public GetFileList_Res() {

    }

    protected GetFileList_Res(Parcel in) {
        attachmentId = in.readString();
        deleteTable = in.readString();
        userId = in.readString();
        image_name = in.readString();
        attachFileName = in.readString();
        attachThumnailFileName = in.readString();
        attachFileActualName = in.readString();
        type = in.readString();
        createdate = in.readString();
        audImg_name = in.readString();
        attachAuditSign = in.readString();
        attachThumnailAuditSign = in.readString();
        attachAuditActualSign = in.readString();
    }

    public GetFileList_Res(String attachmentId, String deleteTable, String userId, String attachFileName, String attachFileActualName, String type, String createdate) {
        this.attachmentId = attachmentId;
        this.deleteTable = deleteTable;
        this.userId = userId;
        this.attachFileName = attachFileName;
        this.attachFileActualName = attachFileActualName;
        this.type = type;
        this.createdate = createdate;
    }

    public GetFileList_Res(String attachFileActualName) {
        this.attachFileActualName = attachFileActualName;
    }

    public String getAudImg_name() {
        return audImg_name;
    }

    public void setAudImg_name(String audImg_name) {
        this.audImg_name = audImg_name;
    }

    public String getAttachAuditSign() {
        return attachAuditSign;
    }

    public void setAttachAuditSign(String attachAuditSign) {
        this.attachAuditSign = attachAuditSign;
    }

    public String getAttachThumnailAuditSign() {
        return attachThumnailAuditSign;
    }

    public void setAttachThumnailAuditSign(String attachThumnailAuditSign) {
        this.attachThumnailAuditSign = attachThumnailAuditSign;
    }

    public String getAttachAuditActualSign() {
        return attachAuditActualSign;
    }

    public void setAttachAuditActualSign(String attachAuditActualSign) {
        this.attachAuditActualSign = attachAuditActualSign;
    }

    public String getAttachThumnailFileName() {
        return attachThumnailFileName;
    }

    public void setAttachThumnailFileName(String attachThumnailFileName) {
        this.attachThumnailFileName = attachThumnailFileName;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getDeleteTable() {
        return deleteTable;
    }

    public void setDeleteTable(String deleteTable) {
        this.deleteTable = deleteTable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public String getAttachFileActualName() {
        return attachFileActualName;
    }

    public void setAttachFileActualName(String attachFileActualName) {
        this.attachFileActualName = attachFileActualName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(attachmentId);
        parcel.writeString(deleteTable);
        parcel.writeString(userId);
        parcel.writeString(image_name);
        parcel.writeString(attachFileName);
        parcel.writeString(attachThumnailFileName);
        parcel.writeString(attachFileActualName);
        parcel.writeString(type);
        parcel.writeString(createdate);
        parcel.writeString(audImg_name);
        parcel.writeString(attachAuditSign);
        parcel.writeString(attachThumnailAuditSign);
        parcel.writeString(attachAuditActualSign);
    }
}
