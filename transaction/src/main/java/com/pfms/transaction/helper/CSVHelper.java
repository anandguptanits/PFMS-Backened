package com.pfms.transaction.helper;

import com.pfms.transaction.Model.Transactions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CSVHelper {
    public final static String TYPE = "text/csv";
    public final static String TYPE2 = "application/vnd.ms-excel";

    private static final String[] HEADERS = {"Date", "Wallet", "Category", "Amount"};
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withHeader(HEADERS);

    public static boolean isCSV(MultipartFile file){
        return (TYPE.equals(file.getContentType())||TYPE2.equals(file.getContentType()));
    }

    public static List<Transactions> csvToTransactions(InputStream data,String userEmail){
        try(Reader fileReader = new BufferedReader(new InputStreamReader(data, StandardCharsets.UTF_8));
            CSVParser csvparser = new CSVParser(fileReader,CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())){
        List<Transactions> transactions = new LinkedList<>();
        Iterable<CSVRecord> csvRecords =  csvparser.getRecords();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for(CSVRecord record : csvRecords){
            if(record.get(0).split("-")[0].length()==4){
                Transactions transaction = new Transactions(
                        LocalDate.parse(record.get(0)),
                        record.get(1),record.get(2),Long.parseLong(record.get(3)),
                        userEmail
                );
                transactions.add(transaction);
                continue;
            }
            Transactions transaction = new Transactions(
                    LocalDate.parse(record.get(0),formatter),
                    record.get(1),record.get(2),Long.parseLong(record.get(3)),
                    userEmail
            );
            transactions.add(transaction);
        }
        return transactions;
        }
        catch(Exception e){
            throw new RuntimeException("fail to parse");
        }
    }

    public static ByteArrayInputStream writeDataToCsv(List<Transactions> transactions ) {
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
            for (final Transactions transaction : transactions) {
                final List<String> data = Arrays.asList(
                        transaction.getTransactionDate().toString(),
                        transaction.getWalletName(),
                        transaction.getCategoryName(),
                        String.valueOf(transaction.getAmount())
                );
                printer.printRecord(data);
            }

            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException("Csv writing error: " + e.getMessage());
        }
    }
}
