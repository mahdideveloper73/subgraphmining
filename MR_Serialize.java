import java.util.*;

class MR_Serialize implements java.io.Serializable
{
   public MR_Pattern Pattern;
   public Map<Integer,List<Map<Integer,List<Integer>>>> l1map_v2;
   public Map<String,Map<Integer,List<List<Map<Integer,Integer>>>>> l1vat;

   MR_Serialize()
   {
       l1vat = new HashMap();
       l1map_v2 = new HashMap();
   }
  
}
