package com.company;

public class Main {

    public static void main(String[] args) {
//        demoFileWriter();
//        demoFileReader();
       // System.out.println(unicodeTable(97, 122));
//        System.out.println(unicodeTable('a', 'z'));
        //demoWriteUnicodeTable();
//        demoReadAll();
        demoReadLines(); /*REPLACED_BY_CHECKER*/
    }

    public static void demoReadAll() { /*REPLACED_BY_CHECKER*/
        try {
            byte[] bytes = Files/*REPLACED_BY_CHECKER*/.readAllBytes(Paths.get()); // automatically close file
            String s = new String();
            /*REPLACED_BY_CHECKER*/
        } /*catch (IOException e) {
            e.printStackTrace();
        aa}asd
        }*/
    }
}