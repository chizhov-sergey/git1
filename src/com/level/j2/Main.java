package com.level.j2;


// not random comment
import java.io.BufferedReader;
        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String filePath = "data/test_2.xml";

        String[] inputData = readFile(filePath);

        xmlChange(inputData);
    }


    private static String[] readFile(String filePath) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        List<String> lines = new ArrayList<>();
        try {
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines.toArray(new String[lines.size()]);
    }



    private static void xmlChange( String[] inputData) {
        StringBuilder resultString = new StringBuilder();

        if (inputData.length != 0){
            resultString.append("<members>");
            resultString.append("\n");
        } else return;

        String projectName = null;
        String role;
        String name;

        //pattern for search "project name"
        Pattern p1 = Pattern.compile("project name=\".*?\"");
        //pattern for search "member role"
        Pattern p2 = Pattern.compile("member role=\".*?\"");
        //pattern for search "name"
        Pattern p3 = Pattern.compile("name=\".*?\"");

        // work with all the lines except the first and last
        for(int i=1;i<inputData.length-1;i++){

            Matcher m1 = p1.matcher(inputData[i]);
            Matcher m2 = p2.matcher(inputData[i]);
            Matcher m3 = p3.matcher(inputData[i]);

            if (m1.find()){
                projectName = inputData[i].substring(m1.start()+14, m1.end()-1);
            }else if (m2.find() && m3.find()){
                role = inputData[i].substring(m2.start()+13, m2.end()-1);
                name = inputData[i].substring(m3.start() + 6, m3.end() - 1);

                //pattern for search current "name"
                Pattern p4 = Pattern.compile(".*?"+name+".*?");
                Matcher m4 = p4.matcher(resultString);

                if(!m4.find()){
                    resultString.append("    <member name=\"").append(name).append("\">\n");
                    resultString.append("        <role name=\"").append(role).append("\" project=\"").append(projectName).append("\"/>\n");

                    String tempProjectName = projectName;
                    String tempRole;

                    for(int j=i+1;j<inputData.length-1;j++){
                        Matcher m5 = p4.matcher(inputData[j]);
                        Matcher m6 = p2.matcher(inputData[j]);
                        Matcher m7 = p1.matcher(inputData[j]);

                        if(m7.find()) {
                            tempProjectName = inputData[j].substring(m7.start()+14, m7.end()-1);
                        } else if (m5.find() && m6.find()){
                            tempRole = inputData[j].substring(m6.start()+13, m6.end()-1);
                            resultString.append("        <role name=\"").append(tempRole).append("\" project=\"").append(tempProjectName).append("\"/>\n");
                        }
                    }
                    resultString.append("    </member>\n");
                }
            }
        }
        resultString.append("</members>");
        System.out.println(resultString);
    }
}
