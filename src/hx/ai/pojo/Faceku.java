package hx.ai.pojo;

public class Faceku {
    private String fid;

    private String userinfo;

    private String code;

    private String gid;

    private String uid;

    public Faceku() {
    }

    public Faceku(String fid, String userinfo, String code, String gid, String uid) {
        this.fid = fid;
        this.userinfo = userinfo;
        this.code = code;
        this.gid = gid;
        this.uid = uid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid == null ? null : fid.trim();
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo == null ? null : userinfo.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid == null ? null : gid.trim();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }
}