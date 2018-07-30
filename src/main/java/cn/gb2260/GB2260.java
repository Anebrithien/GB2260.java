package cn.gb2260;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GB2260 {
    private final Revision revision;
    private HashMap<String, String> data;
    private ArrayList<Division> provinces;
    public static final Revision DEFAULT_REVISION = Revision.STATS_201607;
    /**
     * Direct-controlled municipalities of China
     */
    private static final Set<String> SPECIAL_CITY = new HashSet<>();
    private static final String SP_NAME = "市辖区";

    static {
        // Note: MCA data don't contains "市辖区", we need to mock data for these four cities.
        SPECIAL_CITY.add("110000"); // CN-11: Beijing
        SPECIAL_CITY.add("120000"); // CN-12: Tianjin
        SPECIAL_CITY.add("310000"); // CN-31: Shanghai
        SPECIAL_CITY.add("500000"); // CN-50: Chongqing
    }

    public GB2260() {
        this(DEFAULT_REVISION);
    }

    public GB2260(Revision revision) {
        this.revision = revision;
        data = new HashMap<>();
        provinces = new ArrayList<>();
        String filePath = "/data/" + revision.getSource() + "/" + revision.getVersion() + ".tsv";
        boolean isMCA = isMCA(revision);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            getClass().getResourceAsStream(filePath), StandardCharsets.UTF_8))) {
            reader.lines()
                  .map(line -> line.split("\t"))
                  .peek(fillAllData(isMCA))
                  .filter(checkIsProvince())
                  .forEach(fillProvinces());
        } catch (IOException e) {
            System.err.println("Error in loading GB2260 data!");
            throw new RuntimeException(e);
        }
    }

  private Consumer<String[]> fillProvinces() {
      return split -> {
          Division division = new Division();
          division.setCode(split[2]);
          division.setName(split[3]);
          provinces.add(division);
      };
  }

  private Predicate<String[]> checkIsProvince() {
      return split -> Pattern.matches("^\\d{2}0{4}$", split[2]);
  }

  private Consumer<String[]> fillAllData(boolean isMCA) {
      return split -> {
          data.put(split[2], split[3]);
          if (isMCA && SPECIAL_CITY.contains(split[2])) {
              data.put(split[2].substring(0, 3) + "100", SP_NAME);
          }
      };
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
        division.setPrefecture(data.get(prefectureCode));

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

    private boolean isMCA(Revision revision) {
        return Objects.equals(revision.getSource(), "mca");
    }
}
