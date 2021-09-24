
import java.util.*;
import java.io.*;
import java.lang.*;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.*;
import org.apache.hadoop.util.ToolRunner;

public class graphMining extends Configured implements Tool {

    public int run(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Usage: " + " <in> <output> <minsup><no_of_mapper><no_of_reducer>");

            return -1;
        }
        String output_dir = args[1];
        String minsup = args[2];

        JobConf conf1 = new JobConf(getConf(), graphMining.class);

        conf1.setInputFormat(MyInputFormat.class);
        conf1.setMapperClass(mapperReader.class);

        conf1.setMapOutputKeyClass(Text.class);
        conf1.setMapOutputValueClass(BytesWritable.class);
        conf1.setOutputKeyClass(Text.class);

        conf1.setOutputValueClass(BytesWritable.class);
        conf1.setOutputFormat(SequenceFileOutputFormat.class);


        conf1.set("mapred.task.timeout", "7200");

        conf1.set("mapred.output.compress", "true");
        conf1.set("mapred.output.compression.codec", "org.apache.hadoop.io.compress.BZip2Codec");
        conf1.set("minsup", minsup);

        conf1.setNumMapTasks(Integer.parseInt(args[3]));
        conf1.setNumReduceTasks(Integer.parseInt(String.valueOf(3)));

        FileInputFormat.addInputPaths(conf1, args[0]);

        FileOutputFormat.setOutputPath(conf1, new Path(output_dir + "/temp"));

        long start = System.currentTimeMillis();
        RunningJob job1 = JobClient.runJob(conf1);
        job1.getCounters();
        long end = System.currentTimeMillis();
        System.out.println("running time " + (end - start) / 1000 + "s");
        return 0;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new graphMining(), args));
    }


}
