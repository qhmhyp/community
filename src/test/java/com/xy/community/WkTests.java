package com.xy.community;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "D:/Coder/tools/wkhtmltopdf/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com D:/Coder/tools/data/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}