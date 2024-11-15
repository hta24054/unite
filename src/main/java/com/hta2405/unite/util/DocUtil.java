package com.hta2405.unite.util;

import com.hta2405.unite.dao.DocDao;

public class DocUtil {
    public static boolean isMySignTurn(Long docId, String empId) {
        String nowSigner = new DocDao().getNowSigner(docId);
        return nowSigner.equals(empId);
    }
}
