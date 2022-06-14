package top.dreamstartcloud.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
@Slf4j
public class PythonUtils {
    public static String runPy(String id,String code,String compiler) throws Exception{
        String[] args = new String[] { "python", "D:\\Projects\\Java\\code-train-back-end\\Other\\python\\main.py" ,id,code,compiler};
        Process proc = Runtime.getRuntime().exec(args);
        log.info("compiler" + compiler);
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        log.warn(in.toString());
        while ((line = in.readLine()) != null ) {
            System.out.println(line);
            if (line.contains("\"queued\":-1")){
                log.info("ans:"+ line);
                return line;
            }
        }
        return line;
    }
}