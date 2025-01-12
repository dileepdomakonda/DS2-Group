import java.util.*;
import java.io.*;

public class Main {
     static String input_path = "/Users/dileepdomakonda/Documents/DS2/Files";
     static String merged_path = "/Users/dileepdomakonda/Documents/DS2/merged.csv";
     static String graph_data = "/Users/dileepdomakonda/Documents/DS2/graph_data";
     static String output_path = "/Users/dileepdomakonda/Documents/DS2/output";
     static Long no_of_records = Long.valueOf(0);
     static int no_of_data_points = 100;
    private static void mergeFiles() throws IOException {
        File dir = new File(input_path);
        Writer pw = new FileWriter(merged_path, false);
        String[] fileNames = dir.list();
        int c = 0;
        for (String fileName : fileNames) {
            //System.out.println("Reading from " + fileName);
            File f = new File(dir, fileName);
            BufferedReader br = new BufferedReader(new FileReader(f));
            //pw.println("Contents of file " + fileName);
            String line = br.readLine();
            while (line != null) {
                if (!line.contains("Txn Hash")) {
                    pw.write(line+"\n");
                    no_of_records++;
                }
                if (line.contains("Txn Hash") && c == 0) {
                    pw.write(line+"\n");
                    c = 1;
                }
                line = br.readLine();

            }
            pw.flush();
        }
    }

    private static NFTTracker setNftTracker(String[] words) {
        NFTTracker nftTracker = new NFTTracker();
        for (int i = 0; i < words.length; i++) {
            //System.out.println(words[i]);
            switch (i) {
                case 0:
                    List<String> txnHash = new ArrayList<>();
                    txnHash.add(words[i]);
                    nftTracker.setTxn_Hash(txnHash);
                    break;
                case 1:
                    List<String> unixTimeStamp = new ArrayList<>();
                    unixTimeStamp.add(words[i]);
                    nftTracker.setUnixTimestamp(unixTimeStamp);
                    break;
                case 2:
                    List<String> dateTime = new ArrayList<>();
                    dateTime.add(words[i]);
                    nftTracker.setDate_Time(dateTime);
                    break;
                case 3:
                    List<String> action = new ArrayList<>();
                    action.add(words[i]);
                    nftTracker.setAction(action);
                    break;
                case 4:
                    List<String> buyer = new ArrayList<>();
                    buyer.add(words[i]);
                    nftTracker.setBuyer(buyer);
                    break;
                case 5:
                    List<String> nft = new ArrayList<>();
                    nft.add(words[i]);
                    nftTracker.setNFT(nft);
                    break;
                case 6:
                    nftTracker.setToken_ID(words[i]);
                    break;
                case 7:
                    List<String> type = new ArrayList<>();
                    type.add(words[i]);
                    nftTracker.setType(type);
                    break;
                case 8:
                    List<String> quantity = new ArrayList<>();
                    quantity.add(words[i]);
                    nftTracker.setQuantity(quantity);
                    break;
                case 9:
                    List<String> price = new ArrayList<>();
                    price.add(words[i]);
                    nftTracker.setPrice(price);
                    break;
                case 10:
                    List<String> market = new ArrayList<>();
                    market.add(words[i]);
                    nftTracker.setMarket(market);
                    break;
                default:
                    System.out.println("Error****  !! at loading data at setNftTracker");
                    break;
            }

        }
        nftTracker.setNoOfTransactions(1);
        return nftTracker;
    }

    private static HashMap<String, NFTTracker> loadData(Long interval) throws IOException {

        HashMap<String, NFTTracker> map = new HashMap<>();
        Scanner sc = new Scanner(new FileReader(merged_path));
        String line = sc.nextLine();

        while (line != null) {
            if(interval == 0)
                break;
             
            String words[] = line.split("\",\"");
            words[0] = words[0].substring(1); // remove first character of first string
            words[words.length - 1] = words[words.length - 1].substring(0, words[words.length - 1].length() - 1); // remove last character of last string

            NFTTracker nftTracker = setNftTracker(words);

            if (!map.containsKey(nftTracker.getToken_ID())) {
                map.put(nftTracker.getToken_ID(), nftTracker);
            } else {
                NFTTracker nftTrackerStored = map.get(nftTracker.getToken_ID());
                nftTrackerStored.getTxn_Hash().addAll(nftTracker.getTxn_Hash());
                nftTrackerStored.getUnixTimestamp().addAll(nftTracker.getUnixTimestamp());
                nftTrackerStored.getDate_Time().addAll(nftTracker.getDate_Time());
                nftTrackerStored.getAction().addAll(nftTracker.getAction());
                nftTrackerStored.getBuyer().addAll(nftTracker.getBuyer());
                nftTrackerStored.getNFT().addAll(nftTracker.getNFT());
                nftTrackerStored.getType().addAll(nftTracker.getType());
                nftTrackerStored.getQuantity().addAll(nftTracker.getQuantity());
                nftTrackerStored.getPrice().addAll(nftTracker.getPrice());
                nftTrackerStored.getMarket().addAll(nftTracker.getMarket());
                nftTrackerStored.setNoOfTransactions(nftTrackerStored.getNoOfTransactions() + 1);
            }
            interval--;
            line = sc.nextLine();
        }
        return map;
    }

    public static void main(String[] args) throws IOException {

        mergeFiles();



        Long interval = no_of_records / no_of_data_points;
        List<String> bubbleSortTime = new ArrayList<>();
        List<String> quickSortTime = new ArrayList<>();
        List<String> radixSortTime = new ArrayList<>();
        List<NFTTracker> nftTrackerList_radix = null;
        List<NFTTracker> nftTrackerList_quicksort = null;
        System.out.println("Initializations Done.. !");
        System.out.println("Preparing for Asymptotic runs..");
        System.out.println("Asymptotic runs started.. would take 2 to 3 minutes");

        for (int i = 1; i <= no_of_data_points ; i++) {
            HashMap<String, NFTTracker> map = loadData(i * interval);
            List<NFTTracker> nftTrackerList = new ArrayList<>();
            for (Map.Entry<String, NFTTracker> ele : map.entrySet()) {
                if (ele.getKey().equals("Token ID")) {
                    //System.out.println("token....... continue");
                    continue;
                }
                nftTrackerList.add(ele.getValue());
            }

            //Collections.sort(nftTrackerList, new TXnComparator());

            long startTime, endTime;
            long bubbleSumTime = 0;
            /*for (int k = 0; k < 100; k++) {
                BubbleSort bubbleSort = new BubbleSort();
                startTime = System.currentTimeMillis();
                List<NFTTracker> nftTrackerList_bubblesort = bubbleSort.start(nftTrackerList);
                endTime = System.currentTimeMillis();
                //System.out.println("Bubble Sort execution time: " + (endTime - startTime));
                bubbleSumTime = bubbleSumTime + (endTime - startTime);
            }
            bubbleSortTime.add(bubbleSumTime);*/

            long quickSumTime = 0;
            for (int k = 0; k < 100; k++) {
                QuickSort quickSort = new QuickSort();
                startTime = System.currentTimeMillis();
                nftTrackerList_quicksort = quickSort.start(nftTrackerList);
                endTime = System.currentTimeMillis();
                //System.out.println("Quick Sort execution time: " + (endTime - startTime));
                quickSumTime = quickSumTime + (endTime - startTime);
            }
            Double pq = (quickSumTime * 1.0) / 100;
            pq = pq * 1000000;
            String sq = String.format("%.1f", pq);
            quickSortTime.add(sq);

            long radixSumTime = 0;

            //int x = nftTrackerList.size();
            for (int k = 0; k < 100; k++) {
                Radix radix = new Radix();
                startTime = System.currentTimeMillis();
                nftTrackerList_radix = radix.start(nftTrackerList);
                //Collections.reverse(nftTrackerList_radix);
                endTime = System.currentTimeMillis();
                //System.out.println("Radix Sort execution time: " + (endTime - startTime));
                radixSumTime = radixSumTime + (endTime - startTime);
            }
            Double pr = (radixSumTime * 1.0) / 100;
            pr = pr * 1000000;
            String sr = String.format("%.1f", pr);
            radixSortTime.add(sr);
        }
        System.out.println("Asymptotic runs.. Completed");
        System.out.println("Preparing Graphs..");

        Graph g = new Graph();
        g.createGraph(bubbleSortTime, quickSortTime, radixSortTime, graph_data, no_of_records, no_of_data_points);
        System.out.println("Graphs Created..");
        System.out.println("check, quickSort.png and radixsort.png in the current directory");
        writeOutputPath(nftTrackerList_quicksort, nftTrackerList_radix);

    }
    private static void writeOutputPath(List<NFTTracker> nftTrackerList_quicksort, List<NFTTracker> nftTrackerList_radix) throws IOException {
        File theDir = new File(output_path);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        String quickdataoutput = output_path + "/quicksortoutput.csv";
        String radixdataoutput = output_path + "/radixsortoutput.csv";
        Writer quickWriter = new FileWriter(quickdataoutput, false);
        Writer radixWriter = new FileWriter(radixdataoutput, false);
        int prev = 0;
        for(int i = 0; i < nftTrackerList_quicksort.size(); i++) {
            NFTTracker curr = nftTrackerList_quicksort.get(i);
            if(prev == 0 || prev != curr.getNoOfTransactions()) {
                quickWriter.write("Token Id : " + curr.getToken_ID() + " (frequency = " + curr.getNoOfTransactions() + " )" + "\n\n");
                prev = curr.getNoOfTransactions();
            }
            for(int k = 0; k < curr.getNoOfTransactions(); k++) {
                quickWriter.write(curr.getToken_ID() + " : " + curr.getTxn_Hash().get(k) + "," +
                        curr.getDate_Time().get(k) + "," + curr.getBuyer().get(k) + "," +
                        curr.getNFT().get(k) + "," + curr.getType().get(k) + "," + curr.getQuantity().get(k) +
                        curr.getPrice().get(k) + "\n");
            }
            //quickWriter.write(curr.toString() +"\n");

        }
        Collections.reverse(nftTrackerList_radix);
        prev = 0;
        for(int i = 0; i < nftTrackerList_radix.size(); i++) {
            NFTTracker curr = nftTrackerList_radix.get(i);
            if(prev == 0 || prev != curr.getNoOfTransactions()) {
                radixWriter.write(curr.getToken_ID() + " (frequency = " + curr.getNoOfTransactions() + " )" + "\n");
                prev = curr.getNoOfTransactions();
            }
            for(int k = 0; k < curr.getNoOfTransactions(); k++) {
                radixWriter.write(curr.getToken_ID() + " : " + curr.getTxn_Hash().get(k) + "," +
                        curr.getDate_Time().get(k) + "," + curr.getBuyer().get(k) + "," +
                        curr.getNFT().get(k) + "," + curr.getType().get(k) + "," + curr.getQuantity().get(k) +
                        curr.getPrice().get(k) + "\n");
            }

        }
        System.out.println("Output saved to " + output_path);
    }
}
