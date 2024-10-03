package own.eteryz;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

public class ReadFileUtils {

    public static CognitiveModel readExcel(String path) throws IOException {
        try (InputStream inputStream = ReadFileUtils.class.getResourceAsStream(path)) {
            if (inputStream == null) throw new FileNotFoundException(path);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            String[] labels = extractLabels(sheet);
            System.out.println(Arrays.toString(labels));

            int[][] adjacencyMatrix = extractAdjacencyMatrix(sheet, labels.length);
            Arrays.stream(adjacencyMatrix).forEach(arr -> System.out.println(Arrays.toString(arr)));

            return new CognitiveModel(labels, adjacencyMatrix);
        }
    }

    private static String[] extractLabels(Sheet sheet) {
        Row firstRow = sheet.iterator().next();
        int labelCount = firstRow.getPhysicalNumberOfCells() - 1;
        String[] labels = new String[labelCount];

        for (int i = 0; i < labelCount; i++) {
            String valueCell = firstRow.getCell(i + 1).getStringCellValue();
            if (!valueCell.isEmpty()) {
                labels[i] = valueCell.trim();
            }
        }
        return labels;
    }

    private static int[][] extractAdjacencyMatrix(Sheet sheet, int labelCount) {
        int[][] adjacencyMatrix = new int[labelCount][labelCount];
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Пропускаем первую строку с метками
        int i = 0;

        while (rowIterator.hasNext() && i < labelCount) {
            Row row = rowIterator.next();
            for (int j = 0; j < labelCount; j++) {
                adjacencyMatrix[i][j] = (int) row.getCell(j + 1).getNumericCellValue();
            }
            i++;
        }
        return adjacencyMatrix;
    }
}
