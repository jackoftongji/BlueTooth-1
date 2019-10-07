package ych.com.bluetooth.test;

public class Ultrasound {
    private long id;
    private String waveform;
    private String constructionSite;
    private String section;
    private String mileage;
    private String author;
    private String uploadDate;
    private String foundDate;

    public Ultrasound(long id, String waveform, String constructionSite, String section, String mileage, String author, String uploadDate, String foundDate) {
        this.id = id;
        this.waveform = waveform;
        this.constructionSite = constructionSite;
        this.section = section;
        this.mileage = mileage;
        this.author = author;
        this.uploadDate = uploadDate;
        this.foundDate = foundDate;
    }
}


