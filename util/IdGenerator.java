package util;

import java.util.ArrayList;

public class IdGenerator {

    public static String generateNextId(String prefix, String filePath) {
        ArrayList<String> lines = FileUtil.readAllLines(filePath);

        int max = 0;

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length > 0 && parts[0].startsWith(prefix)) {
                String numberPart = parts[0].substring(prefix.length());
                try {
                    int num = Integer.parseInt(numberPart);
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException e) {
                    // ignore invalid id
                }
            }
        }

        return prefix + String.format("%03d", max + 1);
    }
}