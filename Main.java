import java.util.*;

public class Main {
    public static void main(String[] args) {
        // 화면에 출력할 내용을 작성하세요.
        System.out.println("Hello com.final_project!");

        function(Main.list);

    }
    static void function(List<String> list)
    {
        list.add(new String("234124"));
        System.out.println("Hello com.function!");

        function(list);
    }

    static List<String> list = new ArrayList<>();
}


