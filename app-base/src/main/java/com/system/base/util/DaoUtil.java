package com.system.base.util;

public class DaoUtil {
    public static boolean isInsertSuccess(int affectedRows) {
        return affectedRows > 0;
    }

    public static boolean isInsertFail(int affectedRows) {
        return affectedRows == 0;
    }

    public static boolean isUpdateSuccess(int affectedRows) {
        return affectedRows > 0;
    }

    public static boolean isUpdateFail(int affectedRows) {
        return affectedRows == 0;
    }

    public static boolean isDeleteSuccess(int affectedRows) {
        return affectedRows > 0;
    }

    public static boolean isDeleteFail(int affectedRows) {
        return affectedRows == 0;
    }
}
