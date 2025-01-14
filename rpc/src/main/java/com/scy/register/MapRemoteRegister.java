package com.scy.register;

import com.scy.comomon.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRemoteRegister {
    private static Map<String, List<URL>> map = new HashMap<>();

    public static void regist(String interfaceName, URL url) {
        List<URL> list = map.get(interfaceName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(url);
        map.put(interfaceName, list);
    }

    public static List<URL> get(String interfaceName) {
        return map.get(interfaceName);
    }
}
