import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import items.Dog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class WestTerrier {
    public static void main(String[] args) throws IOException {
        WestTerrier ws = new WestTerrier();
        String url = "http://westieinfo.com/DB/";
        ws.parseTable(url,1);
        ws.parents(37136);

    }

    public void westTerrier(String params) throws IOException {
        try {
            Pattern p = Pattern.compile("id=([^&]+)");
            Matcher m = p.matcher(params);
            while (m.find()) {
                System.out.println(m.group(1));
            }
        } catch (PatternSyntaxException ex) {
            System.out.println("PatternSyntaxException: ");
            System.out.println("Description: " + ex.getDescription());
            System.out.println("Index: " + ex.getIndex());
            System.out.println("Message: " + ex.getMessage());
            System.out.println("Pattern: " + ex.getPattern());
        }
    }

    public static List<String> parser(Integer id) throws IOException {
        List<String> list = new ArrayList<>();
        Document doc = Jsoup.connect("http://westieinfo.com/DB/pes.php?id="+id).get();
        Elements rows = doc.select("tr");
        String str = System.in.toString();
        Pattern p = Pattern.compile("[A-Z]");
        for (Element row : rows) {
            Elements columns = row.select("td");
            for (Element column : columns) {
                //System.out.print(column.text());
                if (p.matcher(str).find()) {
                    list.add(column.text());

                    //System.out.println("");

                }
            }
        }

        //System.out.println(list);
        //System.out.println(list.get(1));
        //System.out.println(list.get(3));
        //System.out.println(list.get(4));
        //System.out.println(list.get(11));

        return list;
    }



    private static String WHO_SCORED_URL = "http://westieinfo.com/DB/seznam.php?pos=1";



    private static List<String> parseTable(String url, Integer page) {
        List<String> tableData = new ArrayList<>();


                try {
                    boolean hasNextPage = true;

                    while (hasNextPage) {
                        String pageUrl = url + "seznam.php?pos=" + page;
                        Document doc = Jsoup.connect(pageUrl).get();
                        Elements tableRows = doc.select("div.list0");
                        Elements westURl = tableRows.select("a[href]");
                        String URL = westURl.text();
                        Elements linkElement = tableRows.select("a[href]");
                        tableData.add(extractHyperlinks(pageUrl));

                        Thread.sleep(3000);
                        System.out.println(tableData);

                        page++;
                        if (page >=11148){
                            hasNextPage = false;
                        }

                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

        return tableData;
    }


    private static List<String> parsePage(String url) {
        List<String> tableData = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Element firstTable = doc.select("table").get(1);

            // Проверяем, что таблица найдена
            if (firstTable != null) {
                Elements rows = firstTable.select("tr");
                for (Element row : rows) {
                    Elements columns = row.select("td");
                    for (Element column : columns) {
                        String[] splitElements = column.text().split(" ");
                        for (String element : splitElements) {
                            tableData.add(element.trim());

                        }
                    }
                    if (tableData.contains("|<<")){
                        tableData.remove(tableData.indexOf("|<<"));
                    }
                    if (tableData.contains("<<\t")){
                    tableData.remove(tableData.indexOf("<<\t"));
                    }
                    if (tableData.contains(">>")) {
                        tableData.remove(tableData.indexOf(">>"));
                    }
                    if (tableData.contains(">>|")) {
                        tableData.remove(tableData.indexOf(">>|"));
                    }
                    tableData.remove(0);
                    tableData.remove(5);
                    tableData.remove(0);
                    //int numberOfElement = tableData.indexOf("7432");
                    System.out.println("Element:"+tableData.get(3));

                }
                System.out.println(tableData);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tableData;
    }


    private static String extractHyperlinks(String url) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        NumberFormat numberFormat = NumberFormat.getInstance();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("href");
                int index = href.indexOf('&');
                if (href.contains("pes.php") && index != -1) {
                    String modifiedHref = href.substring(0, index);
                    int indexId = modifiedHref.indexOf('=');
                    if (indexId != -1) {
                        String id = modifiedHref.substring(indexId + 1);
                        Document west = Jsoup.connect("http://westieinfo.com/DB/pes.php?id=" + id).get();
                        Elements title = west.select("title");
                        String westTitle = title.text();
                        int nameId = westTitle.indexOf(':');
                        if (nameId != -1) {
                            String modifiedWest = westTitle.substring(nameId + 3);
                            Number num = numberFormat.parse(id);
                            Dog dog = new Dog();
                            if (parser(num.intValue()).get(2).equals("Reg. number:")) {
                                if (parser(num.intValue()).contains("Breeder:")) {
                                    dog.setBreeder(parser(num.intValue()).get(5));
                                }
                            } else if (parser(num.intValue()).contains("Breeder:")) {
                                dog.setBreeder(parser(num.intValue()).get(3));
                            } else {
                                dog.setBreeder("BREEDER HAS NOT FOUND");
                            }
                            if (dog.getBreeder() == null) {
                                dog.setBreeder("BREEDER HAS NOT FOUND");
                            }

                            if (parser(num.intValue()).get(0).equals("Date of birth:")) {
                                dog.setBirthday(westBirthDay(parser(num.intValue()).get(1)));
                            } else {
                                dog.setBirthday(null);
                            }
                            dog.setSex(westSex("http://westieinfo.com/DB/pes.php?id=" + id));

                            dog.setName(modifiedWest);
                            dog.setWestURL("http://westieinfo.com/DB/" + modifiedHref);
                            List<Integer> parentsIds = parserURl(Integer.parseInt(id));
                            if (!parentsIds.isEmpty() && parentsIds.size() >= 8) {
                                dog.setFatherID(parentsIds.get(0));
                                dog.setMotherID(parentsIds.get(7));
                            } else {
                                dog.setMotherID(null);
                                dog.setFatherID(null);
                            }
                            String json = objectMapper.writeValueAsString(dog);
                            System.out.println(json);

                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return url;
    }




    public static LocalDate westBirthDay(String birthday) throws ParseException {
// Создание DateTimeFormatterBuilder и комбинация форматов
        Dog dog = new Dog();
        DateTimeFormatter possibleDateFormat = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("d.MM.yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd.M.yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("d.M.yyyy"))
                .toFormatter();

        LocalDate parsedDate = null;

        try {
            // Попытка преобразования строки в дату с помощью DateTimeFormatter
            parsedDate = LocalDate.parse(birthday, possibleDateFormat);
            //dog.setBirthday(parsedDate);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        return parsedDate;

    }

    private static String westSex(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        // Find all <span> elements with class="note0"
        Element spanElements = doc.selectFirst("span.note0");
        // Get the first <span> element (if any)
        String maleSpan = spanElements.text();
        // Get the text content of the <span> element (which is "male" in this case)
        if (maleSpan != null) {
            return maleSpan;
        } else {
            return null;
        }
    }

    private static List<Integer> parserURl(Integer id) throws IOException {
        Document doc = Jsoup.connect("http://westieinfo.com/DB/pes.php?id=" + id).get();
        Elements rows = doc.select("tr");
        Elements links = rows.select("a[href]");
        List<Integer> parentsList = new ArrayList<>();

        // Добавляем проверку наличия ссылок перед началом извлечения идентификаторов
        if (links.isEmpty()) {
            System.out.println("No links found on the page.");
            return parentsList;
        }

        for (Element link : links) {
            String href = link.attr("href");
            int index = href.indexOf('&');
            if (href.contains("pes.php") && index != -1) {
                String modifiedHref = href.substring(0, index);
                int indexId = modifiedHref.indexOf('=');
                if (indexId != -1) {
                    String parentsId = modifiedHref.substring(indexId + 1);
                    // Просто добавляем parentsId в список, не выполняя дополнительные проверки
                    parentsList.add(Integer.parseInt(parentsId));
                }
            }
        }

        return parentsList;
    }

    private static List<String> parents(Integer id) throws IOException {
        Document doc = Jsoup.connect("http://westieinfo.com/DB/pes.php?id=" + id).get();
        Elements rows = doc.select("tr");
        Elements links = rows.select("a[href]");
        List<String> parentsList = new ArrayList<>();

        // Добавляем проверку наличия ссылок перед началом извлечения идентификаторов
        if (links.isEmpty()) {
            System.out.println("No links found on the page.");
            return parentsList;
        }


        for (Element link : links) {
            String href = link.attr("href");
            int index = href.indexOf('&');

            if (href.contains("pes.php") && index != -1) {
                String modifiedHref = href.substring(0, index);
                //System.out.println("http://westieinfo.com/DB/"+modifiedHref+"   "+link.text());
                parentsList.add("http://westieinfo.com/DB/"+modifiedHref+"   "+link.text()+"\n");

            }
        }
        System.out.println(sortParents(parentsList));
        return sortParents(parentsList);
    }

    private static List<String> sortParents(List<String> parentsList) {
        List<String> sortedParents = new ArrayList<>();

        if (parentsList.size() >= 14) {
            // Subtract 1 from each index since list indexes start at 0
            sortedParents.add(parentsList.get(0));
            sortedParents.add(parentsList.get(7)+"\n");

            sortedParents.add(parentsList.get(1));
            sortedParents.add(parentsList.get(4)+"\n");
            sortedParents.add(parentsList.get(8));
            sortedParents.add(parentsList.get(11)+"\n");

            sortedParents.add(parentsList.get(2));
            sortedParents.add(parentsList.get(3));
            sortedParents.add(parentsList.get(5));
            sortedParents.add(parentsList.get(6)+"\n");

            sortedParents.add(parentsList.get(9));
            sortedParents.add(parentsList.get(10));
            sortedParents.add(parentsList.get(12));
            sortedParents.add(parentsList.get(13)+"\n");
        } else {
            System.out.println("The list has fewer than 14 elements.");
        }

        return sortedParents;
    }
    private static List<Integer> getWestId (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        List<Integer> idList = new ArrayList<>();

        for (Element link : links) {
            String href = link.attr("href");
            int index = href.indexOf('&');
            if (href.contains("pes.php") && index != -1) {
                String modifiedHref = href.substring(0, index);
                int indexId = modifiedHref.indexOf('=');
                if (indexId != -1) {
                    String id = modifiedHref.substring(indexId + 1);
                    idList.add(Integer.parseInt(id));
                }
            }
        }
        return idList;
    }

}








