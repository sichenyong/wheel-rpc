package com.scy.register;

import com.scy.comomon.URL;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalCallbackRegister {
    private static Map<String, Class> map = new HashMap<>();

    public static void regist(String interfaceName, String version, Class clazz) {
        map.put(interfaceName+version, clazz);
        saveFile();
    }

    public static Class get(String interfaceName, String version) {
        map = getFile();
        return map.get(interfaceName+version);
    }

    private static void saveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\UserData\\callback.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Class> getFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\UserData\\callback.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Map<String, Class>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
