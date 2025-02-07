package com.scy.register;

import com.scy.comomon.URL;

import java.io.*;
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

        saveFile();
    }

    public static List<URL> get(String interfaceName) {
        map = getFile();
        return map.get(interfaceName);
    }

    public static void removeKey(String interfaceName) {
        map = getFile();
        // 移除对应key
        assert map != null;
        map.remove(interfaceName);
        saveFile();
    }

    private static void saveFile() {
        try (FileOutputStream fileOutputStream = new FileOutputStream("D:\\UserData\\temp.txt");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<URL>> getFile() {
        try (FileInputStream fileInputStream = new FileInputStream("D:\\UserData\\temp.txt");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);){

            return (Map<String, List<URL>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
