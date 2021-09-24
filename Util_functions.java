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
        all=new ArrayList<>();
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

                            if (!l1vat.containsKey(g1.getCan_code())) {
                                Map<Integer, Integer> m1 = new HashMap();
                                m1.put(v1id, v2id);
                                List<Map<Integer, Integer>> l = new ArrayList();
                                List<List<Map<Integer, Integer>>> l2 = new ArrayList();
                                l.add(m1);
                                l2.add(l);
                                Map<Integer, List<List<Map<Integer, Integer>>>> m2 = new HashMap();
                                m2.put(tid, l2);
                                l1vat.put(g1.getCan_code(), m2);
                                freq_pats.add(g1);
                                code_to_pat.put(g1.getCan_code(), g1);
                                g1.get_to_insert(tid, v1id, v2id);

                            } else if (l1vat.containsKey(g1.getCan_code())) {

                                int flag2 = 0;
                                if (((Map) (l1vat.get(g1.getCan_code()))).containsKey(tid)) {
                                    flag2 = 1;
                                }

                                if (flag2 == 1) {
                                    Map<Integer, Integer> m1 = new HashMap();
                                    m1.put(v1id, v2id);

                                    Iterator it1 = ((List) (((Map) (l1vat.get(g1.getCan_code()))).get(tid))).iterator();
                                    ((List) (it1.next())).add(m1);

                                    ((MR_Pattern) (code_to_pat.get(g1.getCan_code()))).insert_vid_hs(tid, v1id, v2id);

                                }
                                if (flag2 == 0) {
                                    Map<Integer, Integer> m1 = new HashMap();
                                    m1.put(v1id, v2id);
                                    List<Map<Integer, Integer>> l = new ArrayList();
                                    List<List<Map<Integer, Integer>>> l1 = new ArrayList();
                                    l.add(m1);
                                    l1.add(l);
                                    Map<Integer, List<List<Map<Integer, Integer>>>> m2 = new HashMap();
                                    m2.put(tid, l1);

                                    l1vat.get(g1.getCan_code()).put(tid, l1);


                                    ((MR_Pattern) (code_to_pat.get(g1.getCan_code()))).get_to_insert(tid, v1id, v2id);

                                }

                            }
                        }

                    }

                }

                if (flag == 1) {
                    break;
                }
            }

        } catch (IOException e) {
        }
        Iterator itr = freq_pats.iterator();


        while (itr.hasNext()) {
            MR_Pattern t = ((MR_Pattern) itr.next());
            t.support = t.vat.size();

        }

        itr = freq_pats.iterator();

        while (itr.hasNext()) {
            MR_Pattern t = ((MR_Pattern) itr.next());
            int srcl = t.idtolabels.get(1);
            int destl = t.idtolabels.get(2);
            int el = ((MR_CannonicalCode) (t.can_cod.toArray()[0])).get_elabel();
            int iter = 1;
            int temp = 0;
            if (srcl != destl) {
                iter = 2;
            }
            while (iter > 0) {


                if (l1map_v2.size() == 0) {
                    List<Integer> l1 = new ArrayList();
                    l1.add(el);
                    Map<Integer, List<Integer>> m1 = new HashMap();
                    m1.put(destl, l1);
                    List<Map<Integer, List<Integer>>> l2 = new ArrayList();
                    l2.add(m1);
                    Map<Integer, List<Map<Integer, List<Integer>>>> m3 = new HashMap();

                    l1map_v2.put(srcl, l2);
                    if (srcl != destl) {
                        List<Integer> l11 = new ArrayList();
                        l11.add(el);
                        Map<Integer, List<Integer>> m11 = new HashMap();
                        m11.put(srcl, l11);
                        List<Map<Integer, List<Integer>>> l21 = new ArrayList();
                        l21.add(m11);
                        Map<Integer, List<Map<Integer, List<Integer>>>> m31 = new HashMap();

                        l1map_v2.put(destl, l21);
                    }
                    iter--;
                    continue;

                }
                int flag1 = 0;
                int flag11 = 0;
                Object Obj1 = new Object();
                Object Obj2 = new Object();

                if (l1map_v2.containsKey(srcl)) {
                    flag1 = 1;
                }


                if (flag1 == 1) {
                    List<Map<Integer, List<Integer>>> l2 = new ArrayList();

                    l2 = (List) l1map_v2.get(srcl);
                    Iterator itr2 = l2.iterator();
                    int flag2 = 0;
                    while (itr2.hasNext()) {
                        Obj2 = itr2.next();
                        if (((Map) Obj2).containsKey(destl)) {
                            flag2 = 1;
                            break;
                        }
                    }
                    if (flag2 == 1) {
                        ((List) ((Map) Obj2).get(destl)).add(el);

                    }
                    if (flag2 == 0) {
                        Map<Integer, List<Integer>> m2 = new HashMap();
                        List<Integer> l1 = new ArrayList();
                        l1.add(el);
                        m2.put(destl, l1);

                        ((List) (l1map_v2.get(srcl))).add(m2);

                    }

                }
                if (flag1 == 0) {
                    List<Integer> l1 = new ArrayList();
                    l1.add(el);
                    Map<Integer, List<Integer>> m1 = new HashMap();
                    m1.put(destl, l1);
                    List<Map<Integer, List<Integer>>> l2 = new ArrayList();
                    l2.add(m1);
                    Map<Integer, List<Map<Integer, List<Integer>>>> m3 = new HashMap();

                    l1map_v2.put(srcl, l2);

                }
                iter--;
                if (iter == 1) {
                    temp = srcl;
                    srcl = destl;
                    destl = temp;
                }
            }

        }
    }

}

