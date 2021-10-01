import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

public class OutPutFileReader {
    public static void main(String[] args) {

        Configuration conf = new Configuration();
        try {
            Path inFile = new Path("C:\\out7\\output-graph-1\\part-۰۰۰۰۰");
            Reader reader = null;
            try {
                Text key = new Text();
                BytesWritable value = new BytesWritable();
                reader = new Reader(conf, Reader.file(inFile), Reader.bufferSize(4096));
                while (reader.next(key, value)) {
                    ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(value.getBytes()));
                    MR_Serialize mr_serialize = (MR_Serialize) input.readObject();
                    System.out.println("Key ==> " + key + "   Value ==> " + mr_serialize.toString());
                }

            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch bloc
            e.printStackTrace();
        }
    }
}