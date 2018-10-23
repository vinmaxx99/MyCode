package com.example.hp.adjonline;

public class ListData {

    private String text1;
    private String judge;
    private String date;

    public ListData(String t1,String t2,String t3)
    {
        text1=t1;
        judge=t2;
        date=t3;
    }

    public String getDate() {
        return date;
    }

    public String getJudge() {
        return judge;
    }

    public String getText1() {
        return text1;
    }
}
