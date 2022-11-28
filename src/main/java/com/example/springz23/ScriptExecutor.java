package com.example.springz23;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ScriptExecutor {

    static public void executePython(String script, String username, String path){
        System.out.println("Toto su parametre v funkcii executePython: " + script + " " + username + " "+ path);
        Process mProcess = null;
        Process process;
        try{
            process = Runtime.getRuntime().exec("python C:\\Users\\brani\\IdeaProjects\\SpringZ23\\src\\main\\java\\com\\example\\springz23\\"+script+ " "+username+" "+path);
            mProcess = process;
        }catch(Exception e) {
            System.out.println("Exception Raised" + e.toString());
        }
        InputStream stdout = Objects.requireNonNull(mProcess).getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        String line;
        try{
            while((line = reader.readLine()) != null){
                System.out.println("stderr: "+ line);
            }
        }catch(IOException e){
            System.out.println("Exception in reading output"+ e.toString());
        }
    }

    public static void execute(String script, String name, String path) throws IOException {
        Process process = new ProcessBuilder("/var/www/html/"+script, name,path ).start();
    }
}
