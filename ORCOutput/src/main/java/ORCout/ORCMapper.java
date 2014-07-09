package ORCout;


import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautam on 5/27/14.
 */
public class ORCMapper extends Mapper<LongWritable,Text,NullWritable,Writable> {

    private final OrcSerde serde = new OrcSerde();

    //Define the struct which will represent each row in the ORC file
    private final String typeString = "struct<air_temp:double,station_id:string,lat:double,lon:double>";

    private final TypeInfo typeInfo = TypeInfoUtils
            .getTypeInfoFromTypeString(typeString);
    private final ObjectInspector oip = TypeInfoUtils
            .getStandardJavaObjectInspectorFromTypeInfo(typeInfo);

    private final NCDCParser parser = new NCDCParser();


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

           parser.parse(value.toString());

            if(parser.isValid()) {

               List<Object> struct =new ArrayList<Object>(4);
               struct.add(0, parser.getAirTemp());
               struct.add(1, parser.getStationId());
               struct.add(2, parser.getLatitude());
               struct.add(3, parser.getLongitude());

               Writable row = serde.serialize(struct, oip);

               context.write(NullWritable.get(), row);
            }
    }
}
