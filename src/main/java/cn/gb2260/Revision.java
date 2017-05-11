package cn.gb2260;

public enum Revision {

    MCA_198012("mca", "198012"),
    MCA_198112("mca", "198112"),
    MCA_198212("mca", "198212"),
    MCA_198312("mca", "198312"),
    MCA_198412("mca", "198412"),
    MCA_198512("mca", "198512"),
    MCA_198612("mca", "198612"),
    MCA_198712("mca", "198712"),
    MCA_198812("mca", "198812"),
    MCA_198912("mca", "198912"),
    MCA_199012("mca", "199012"),
    MCA_199112("mca", "199112"),
    MCA_199212("mca", "199212"),
    MCA_199312("mca", "199312"),
    MCA_199412("mca", "199412"),
    MCA_199512("mca", "199512"),
    MCA_199612("mca", "199612"),
    MCA_199712("mca", "199712"),
    MCA_199812("mca", "199812"),
    MCA_199912("mca", "199912"),
    MCA_200012("mca", "200012"),
    MCA_200112("mca", "200112"),
    MCA_200212("mca", "200212"),
    MCA_200312("mca", "200312"),
    MCA_200412("mca", "200412"),
    MCA_200512("mca", "200512"),
    MCA_200612("mca", "200612"),
    MCA_200712("mca", "200712"),
    MCA_200812("mca", "200812"),
    MCA_200912("mca", "200912"),
    MCA_201012("mca", "201012"),
    MCA_201112("mca", "201112"),
    MCA_201312("mca", "201312"),
    MCA_201412("mca", "201412"),
    MCA_201501("mca", "201501"),
    MCA_201502("mca", "201502"),
    MCA_201503("mca", "201503"),
    MCA_201504("mca", "201504"),
    MCA_201505("mca", "201505"),
    MCA_201506("mca", "201506"),
    MCA_201507("mca", "201507"),
    MCA_201508("mca", "201508"),
    MCA_201509("mca", "201509"),
    MCA_201510("mca", "201510"),
    MCA_201511("mca", "201511"),
    MCA_201512("mca", "201512"),
    MCA_201601("mca", "201601"),
    MCA_201602("mca", "201602"),
    MCA_201603("mca", "201603"),
    MCA_201604("mca", "201604"),
    MCA_201605("mca", "201605"),
    MCA_201608("mca", "201608"),
    MCA_201610("mca", "201610"),
    MCA_201612("mca", "201612"),

    STATS_200212("stats", "200212"),
    STATS_200306("stats", "200306"),
    STATS_200312("stats", "200312"),
    STATS_200403("stats", "200403"),
    STATS_200409("stats", "200409"),
    STATS_200412("stats", "200412"),
    STATS_200506("stats", "200506"),
    STATS_200512("stats", "200512"),
    STATS_200612("stats", "200612"),
    STATS_200712("stats", "200712"),
    STATS_200812("stats", "200812"),
    STATS_200912("stats", "200912"),
    STATS_201010("stats", "201010"),
    STATS_201110("stats", "201110"),
    STATS_201210("stats", "201210"),
    STATS_201308("stats", "201308"),
    STATS_201410("stats", "201410"),
    STATS_201509("stats", "201509"),
    STATS_201607("stats", "201607"),;

    private final String source;

    private final String version;

    Revision(String source, String version) {
        this.source = source;
        this.version = version;
    }

    public String getSource() {
        return source;
    }

    public String getVersion() {
        return version;
    }
}
