package cn.gb2260;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GB2260 {
    private final Revision revision;
    private HashMap<String, String> data;
    private ArrayList<Division> provinces;

    public GB2260() {
        this(Revision.STATS_201607);
    }

    public GB2260(Revision revision) {
        this.revision = revision;
        data = new HashMap<>();
        provinces = new ArrayList<>();
        String filePath = "/data/" + revision.getSource() + "/" + revision.getVersion() + ".tsv";
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            reader.lines()
                  .map(line -> line.split("\t"))
                  .peek(split -> data.put(split[2], split[3]))
                  .filter(split -> Pattern.matches("^\\d{2}0{4}$", split[2]))
                  .forEach(split -> {
                      Division division = new Division();
                      division.setCode(split[2]);
                      division.setName(split[3]);
                      provinces.add(division);
                  });
        } catch (IOException e) {
            System.err.println("Error in loading GB2260 data!");
            throw new RuntimeException(e);
        }
    }

    public Division getDivision(String code) {
        if (code.length() != 6) {
            throw new InvalidCodeException("Invalid code");
        }

        if (!data.containsKey(code)) {
            return null;
        }

        Division division = new Division();
        division.setName(data.get(code));
        division.setSource(revision.getSource());
        division.setRevision(revision.getVersion());
        division.setCode(code);

        if (Pattern.matches("^\\d{2}0{4}$", code)) {
            return division;
        }

        String provinceCode = code.substring(0, 2) + "0000";
        division.setProvince(data.get(provinceCode));

        if (Pattern.matches("^\\d{4}0{2}$", code)) {
            return division;
        }

        String prefectureCode = code.substring(0, 4) + "00";
        division.setPrefecture(data.get(prefectureCode) == null ? "市辖区" : data.get(prefectureCode));

        return division;
    }

    public Revision getRevision() {
        return revision;
    }

    public ArrayList<Division> getProvinces() {
        return provinces;
    }

    public List<Division> getPrefectures(String code) {
        if (!Pattern.matches("^\\d{2}0{4}$", code)) {
            throw new InvalidCodeException("Invalid province code");
        }

        if (!data.containsKey(code)) {
            throw new InvalidCodeException("Province code not found");
        }

        Division province = getDivision(code);

        Pattern pattern = Pattern.compile("^" + code.substring(0, 2) + "\\d{2}00$");
        return data.keySet()
                   .stream()
                   .filter(key -> pattern.matcher(key).matches())
                   .map(key -> {
                       Division division = getDivision(key);
                       division.setProvince(province.getName());
                       return division;
                   })
                   .collect(Collectors.toList());
    }

    public List<Division> getCounties(String code) {
        if (!Pattern.matches("^\\d+[1-9]0{2,3}$", code)) {
            throw new InvalidCodeException("Invalid prefecture code");
        }

        if (!data.containsKey(code)) {
            throw new InvalidCodeException("Prefecture code not found");
        }

        Division prefecture = getDivision(code);
        Division province = getDivision(code.substring(0, 2) + "0000");

        Pattern pattern = Pattern.compile("^" + code.substring(0, 4) + "\\d+$");
        return data.keySet()
                   .stream()
                   .filter(key -> pattern.matcher(key).matches())
                   .map(key -> {
                       Division division = getDivision(key);
                       division.setProvince(province.getName());
                       division.setPrefecture(prefecture.getName());
                       return division;
                   })
                   .collect(Collectors.toList());
    }
}
