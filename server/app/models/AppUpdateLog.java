package models;

import models.enums.AppType;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * app 更新日志
 */
@Entity
public class AppUpdateLog extends BaseModel {

    /**
     * app类型
     */
    @Enumerated(EnumType.STRING)
    public AppType appType;

    /**
     * 操作管理员
     */
    @ManyToOne
    public Admin admin;

    /**
     * 更新内容
     */
    @Column(columnDefinition = "TEXT")
    public String description;

    /**
     * 备注
     */
    @Constraints.MaxLength(255)
    public String remark;

    /**
     * 版本名
     */
    @Constraints.MaxLength(255)
    public String apkVersionName;

    /**
     * 版本号
     */
    @Constraints.Required
    @Constraints.MaxLength(8)
    public int apkVersion;

    /**
     * apk 路径
     */
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String apkPath;

    /**
     * 是否发布
     */
    public boolean isPublic;

    /**
     * 是否推送更新信息
     */
    public boolean isPush;


    public static final Finder<Long, AppUpdateLog> find = new Finder<Long, AppUpdateLog>(
            Long.class, AppUpdateLog.class);

    /**
     * 获取某次更新信息
     *
     * @param id
     * @return
     */
    public static AppUpdateLog findById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }

    /**
     * 获取最新版 更新信息
     *
     * @return
     */
    public static AppUpdateLog findLatestLog() {
        return find
                .where()
                .eq("isPublic", true)
                .setOrderBy("whenCreated desc")
                .findList().get(0);
    }

    /**
     * 获取最新版 更新信息
     *
     * @return
     */
    public static AppUpdateLog findLatestLogForAndroid() {
        return find
                .where()
                .eq("appType", AppType.ANDROID.getName())
                .eq("isPublic", true)
                .setOrderBy("whenCreated desc")
                .findList().get(0);
    }

    /**
     * 获取所有更新日志
     *
     * @return
     */
    public static List<AppUpdateLog> getAppUpdateLogs(int page, int pageSize) {
        return find
                .where()
                .eq("appType", AppType.ANDROID.getName())
                .eq("isPublic", true)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

}
