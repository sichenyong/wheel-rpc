package com.scy.register;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地注册
 */
public class LocalRegister {
    private static Map<String, Class> map = new HashMap<>();

    public static void regist(String interfaceName, String version, Class clazz) {
        map.put(interfaceName+version, clazz);
    }

    public static Class get(String interfaceName, String version) {
        return map.get(interfaceName+version);
    }
}
