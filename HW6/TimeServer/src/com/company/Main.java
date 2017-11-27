package com.company;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.*;
import java.net.InetAddress;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;


class ReadProperties {
    public String loadProperty(String Property) {
        Properties prop = new Properties();
        InputStream input = null;
        String proprtyValue = null;

        try {
            input = new FileInputStream("config.properties");

            prop.load(input);

            //System.out.println(proprtyValue);
            proprtyValue = prop.getProperty(Property);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return proprtyValue;
    }
}

interface IClock{
    Date currentTimeMillis();
}

class Timer{
    long start;

    void start(){
        start = System.currentTimeMillis();
    }

    long getLatencey(){
        return System.currentTimeMillis() - start;
    }
}

class TimeServerClock implements IClock{
    Timer t = new Timer();
    ReadProperties propertyReader = new ReadProperties();

    public Date currentTimeMillis(){

        String[] hosts = new String[]{
                propertyReader.loadProperty("host1"), propertyReader.loadProperty("host2"),
                propertyReader.loadProperty("host3")};

        NTPUDPClient client = new NTPUDPClient();

        client.setDefaultTimeout(5000);

        for (String host : hosts) {

            try {
                t.start();
                InetAddress hostAddr = InetAddress.getByName(host);
                //System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
                TimeInfo info = client.getTime(hostAddr);
                Date date = new Date(info.getMessage().getTransmitTimeStamp().getTime() + t.getLatencey());
                return date;
            }

            catch (IOException e) {
                if (hosts[hosts.length - 1].equals(host)){
                    e.printStackTrace();
                    throw new RuntimeException("No hosts found");
                }else{
                    System.out.println("Server unsuccessful. Attempting next server");
                    continue;
                }
            }
        }

        client.close();

        return null;

    }
}


public class Main {

    public static void main(String[] args) {
        TimeServerClock t = new TimeServerClock();

        t.currentTimeMillis();
    }
}
