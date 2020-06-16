import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class JeasyGradle {

    private static String build_gradle = (
        "\n// checked\n" +
        "\njar {\n" +
        "    manifest {\n" +
        "        attributes 'Main-Class': '%s'\n" +
        "    }\n" +
        "}"
    );

    private static Scanner scanner = new Scanner(System.in);

    private static String input(String output_note) {
        System.out.print(output_note);
        return scanner.nextLine();
    }

    private static void writeFile(String path, String content) throws IOException {
        File file = new File(path);
        FileWriter fr = new FileWriter(file, true);
        fr.write(content);
        fr.close();
    }

    private static String readFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return new String(data, "UTF-8");
    }

    private static String system(String command) throws IOException, InterruptedException {

        String temp, result="";
        Process process;

        process = Runtime.getRuntime().exec(command);
        BufferedReader br = new BufferedReader(
            new InputStreamReader(process.getInputStream())
        );

        while ((temp = br.readLine()) != null) {
            result += temp;
        }

        process.waitFor();
        process.destroy();

        return result;

    }

    public static void main(String[] args) throws Exception {

        System.out.println(String.format(
            "\n[+] Initiation\n%s\n````````````````\n" +
            "\n[+] List classes\n%s",
            system("gradle init --type java-application"),
            system("find ./src/main/java/ -type f")
        ));

        if (!readFile("build.gradle").contains("checked")) {
            String className = input(
                "\nExp: ./src/main/java/App.java             ->   Class Name: App\n" +
                "     ./src/main/java/package/Class.java   ->   Class Name: package.Class\n\n" +
                "~~> Specify Main Class: "
            );
            writeFile(
                "./build.gradle",
                String.format(build_gradle, className)
            );
        }
        System.out.print("````````````````\n");

        System.out.println(String.format(
            "\n[+] Build\n%s\n``````````````\n" +
            "\n[+] Runable Jar: \n%s\n````````````````",
            system("gradle build"),
            system("find ./build/libs/ -type f")
        ));

    }

}
