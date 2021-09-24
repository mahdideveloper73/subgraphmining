
import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.StringTokenizer;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.*;
import org.apache.hadoop.util.ToolRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class mapperReader extends MapReduceBase implements Mapper<NullWritable, Text, Text, BytesWritable> {

    private JobConf conf;

    @Override
    public void configure(JobConf conf) {
        this.conf = conf;

    }

    public void map(NullWritable key, Text value, OutputCollector<Text, BytesWritable> output, Reporter reporter) throws IOException {
        FSDataInputStream currentStream;
        BufferedReader currentReader;
        FileSystem fs;
        Text map_key = new Text();
        Text map_value = new Text();
        Path path = new Path(value.toString());
        fs = path.getFileSystem(conf);
        currentStream = fs.open(path);
        currentReader = new BufferedReader(new InputStreamReader(currentStream));


        Util_functions func1 = new Util_functions();
        func1.readDB_VAT_L1map(currentReader);

//        Set<String> pat_code = new HashSet();
//        pat_code = func1.l1vat.keySet();
        for (int i = 0; i < func1.all.size(); i++) {
            MR_Serialize Serial = new MR_Serialize();
            Serial.Pattern = func1.all.get(i);
            Serial.Pattern.support = ((Map) (func1.l1vat.get(Serial.Pattern.getCan_code()))).keySet().size();
            Serial.Pattern.vat = (Map) (func1.l1vat.get(Serial.Pattern.getCan_code()));
            Serial.l1map_v2 = func1.l1map_v2;
            Serial.l1vat = func1.l1vat;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(Serial);
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            String key1 = ((MR_Pattern) (func1.all.get(i))).getCan_code();

//            map_key.set(key1);
            String id = ((MR_Pattern) (func1.all.get(i))).getId().toString();
            map_key.set(id);
            output.collect(map_key, new BytesWritable(baos.toByteArray()));
            System.out.println(map_key);

        }
        func1.l1map_v2.clear();
        func1.l1vat.clear();
        func1.check_unique.clear();

    }
}
