package com.developer.gavine.ebooks;

public class Ebooks {

    String bib_key;
    String preview_url;
    String info_url;

    public String getBib_key() {
        return bib_key;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public String getInfo_url() {
        return info_url;
    }

    public String getPreview() {
        return preview;
    }

    public String getThumnail_url() {
        return thumnail_url;
    }

    String preview;
    String thumnail_url;


    public Ebooks(String b, String pu, String iu, String p, String tu)
    {
        bib_key = b;
        preview_url = pu;
        info_url = iu;
        preview = p;
        thumnail_url = tu;
    }
}
