import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

public class Util_functions {

    public Map<Integer, Integer> vid_to_lbl;
    public Map<String, Map<Integer, List<List<Map<Integer, Integer>>>>> l1vat;
    public Set<MR_Pattern> freq_pats_hadoop;
    public List<MR_Pattern> freq_pats;
    public List<MR_Pattern> all;
    public List<Map<Integer, List<Map<Integer, List<Integer>>>>> l1map;
    public Map<Integer, List<Map<Integer, List<Integer>>>> l1map_v2;
    public int minsup;
    public Map<String, MR_Pattern> code_to_pat;
    Map<String, Integer> check_unique;

    public Util_functions() {
        freq_pats_hadoop = new HashSet();
        freq_pats = new ArrayList();
        check_unique = new HashMap();
        all = new ArrayList<>();
    }

    public void readDB_VAT_L1map(BufferedReader currentReader) {

        vid_to_lbl = new HashMap();
        l1vat = new HashMap();
        freq_pats = new ArrayList();
        l1map = new ArrayList();

        l1map_v2 = new HashMap();
        code_to_pat = new HashMap();

        String line;

        try {
            line = currentReader.readLine();

            String[] temp = line.split(" ");
            int tid = 0;
            int flag = 0;

            while (true) {

                tid = Integer.parseInt(temp[2]);

                if (temp[0].equals("t")) {
                    while (true) {
                        line = currentReader.readLine();
                        if (line == null) {
                            flag = 1;
                            break;
                        }

                        temp = line.split(" ");
                        if (temp[0].equals("t")) {

                            break;
                        }
                        if (temp[0].equals("v")) {
                            vid_to_lbl.put(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                        }

                        if (temp[0].equals("e")) {
                            MR_Pattern g1 = new MR_Pattern();
                            int v1id = Integer.parseInt(temp[1]);
                            int v2id = Integer.parseInt(temp[2]);
                            int v1label = vid_to_lbl.get(v1id);
                            int v2label = vid_to_lbl.get(v2id);
                            if (v1label <= v2label) {
                                g1 = new MR_Pattern(v1label, v2label, Integer.parseInt(temp[3]));
                                all.add(g1);

                            } else {
                                g1 = new MR_Pattern(v2label, v1label, Integer.parseInt(temp[3]));
                                int temp2 = v1id;
                                v1id = v2id;
                                v2id = temp2;
                                all.add(g1);
                            }
                        }

                    }

                }
                if (flag == 1) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

