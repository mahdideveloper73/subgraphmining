import java.util.*;
import java.io.*;

public class MR_CannonicalCode implements java.io.Serializable {

	public int v1id,v2id,v1label,v2label,elabel;

	public MR_CannonicalCode(){}
	public MR_CannonicalCode(int id1, int id2, int l1, int e,int l2)
	{

		v1id = id1;
		v2id = id2;
		v1label = l1;
		v2label = l2;
		elabel = e;
		
	}
	public boolean lessthan(MR_CannonicalCode c2)
	{
		boolean is_fwd=(this.v1id<this.v2id);
    	boolean rhs_is_fwd=(c2.v1id<c2.v2id);

    if(!is_fwd && rhs_is_fwd)
      return true;

    if(!is_fwd && !rhs_is_fwd && this.v2id<c2.v2id)
      return true;

    if(!is_fwd && !rhs_is_fwd && this.v2id==c2.v2id && this.elabel<c2.elabel)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id>c2.v1id)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id==c2.v1id && this.v1label<c2.v1label)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id==c2.v1id && this.v1label==c2.v1label && this.elabel < c2.elabel)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id==c2.v1id && this.v1label==c2.v1label && this.elabel == c2.elabel && this.v2label<c2.v2label)
      return true;

    return false;

	}
	public String getCan_code_()
	{
		String can_cod = new String(Integer.toString(this.v1id));
		can_cod = can_cod +"_"+Integer.toString(this.v2id);
		can_cod = can_cod +"_"+Integer.toString(this.v1label);
		can_cod = can_cod +"_"+Integer.toString(this.elabel);
		can_cod = can_cod +"_"+Integer.toString(this.v2label);
		//can_cod = can_cod +";";
		//System.out.println(can_cod);
		return can_cod;
	}
	public String getCan_code_size1()
	{
		String can_cod = new String(Integer.toString(this.v1id));
		can_cod = can_cod +"_"+Integer.toString(this.v2id);
		can_cod = can_cod +"_"+Integer.toString(this.v1label);
		can_cod = can_cod +"_"+Integer.toString(this.elabel);
		can_cod = can_cod +"_"+Integer.toString(this.v2label);
		can_cod = can_cod +";";
		//System.out.println(can_cod);
		return can_cod;
	}

	public int get_elabel()
	{
		return this.elabel;
	}
	
}

class mindfs implements Cloneable
{
	public List<Integer> right_most_path;
	public List<Map<Integer,Integer>> gid_to_cid;
	public List<Map<Integer,Integer>> cid_to_gid;
	public List<MR_CannonicalCode> codes;

	public mindfs(int id1, int id2, int l1, int e,int l2, int gi, int gj)
	{
		MR_CannonicalCode c = new MR_CannonicalCode(id1,id2,l1,e,l2);
		right_most_path = new LinkedList();
		right_most_path.add(id1);
		right_most_path.add(id2);
		gid_to_cid = new LinkedList();
		cid_to_gid = new LinkedList();
		Map<Integer,Integer> m1 = new HashMap();
		m1.put(gi,id1);
		Map<Integer,Integer> m2 = new HashMap();
		m2.put(gj,id2);
		gid_to_cid.add(m1);
		gid_to_cid.add(m2);
		Map<Integer,Integer> m3 = new HashMap();
		m3.put(id1,gi);
		Map<Integer,Integer> m4 = new HashMap();
		m4.put(id2,gj);
		cid_to_gid.add(m3);
		cid_to_gid.add(m4);
		codes = new LinkedList();
		codes.add(c);
	}

	public mindfs()
	{
		right_most_path = new LinkedList();
		gid_to_cid = new LinkedList();
		cid_to_gid = new LinkedList();
		codes = new LinkedList();
	}
	public Object clone(int extensionpoint)
	{
		mindfs obj = new mindfs();
		
		for(int i=0;i<this.right_most_path.size();i++)
		{
		
				((List)(obj.right_most_path)).add(this.right_most_path.toArray()[i]);
		}

		for(int i=0; i<this.gid_to_cid.size();i++)
		{
			int key = (Integer)(((Map)(this.gid_to_cid.toArray()[i])).keySet().toArray()[0]);
		
			int value = (Integer)(((Map)(this.gid_to_cid.toArray()[i])).get(key));
			Map<Integer,Integer> m1 = new HashMap();
			m1.put(key,value);
			obj.gid_to_cid.add(m1);
		}

		for(int i=0; i<this.cid_to_gid.size();i++)
		{
			int key = (Integer)(((Map)(this.cid_to_gid.toArray()[i])).keySet().toArray()[0]);
		
			int value = (Integer)(((Map)(this.cid_to_gid.toArray()[i])).get(key));
			Map<Integer,Integer> m1 = new HashMap();
			m1.put(key,value);
			obj.cid_to_gid.add(m1);
		}

		for(int i=0;i<this.codes.size();i++)
		{
			((List)(obj.codes)).add(this.codes.toArray()[i]);
		}
		return obj;
	}
	public void append(MR_CannonicalCode c, int gi, int gj)
	{

		this.codes.add(c);
		Map<Integer,Integer> m1 = new HashMap();
		m1.put(gi,c.v1id);
		Map<Integer,Integer> m2 = new HashMap();
		m2.put(gj,c.v2id);
		this.gid_to_cid.add(m1);
		this.gid_to_cid.add(m2);
		Map<Integer,Integer> m3 = new HashMap();
		m3.put(c.v1id,gi);
		Map<Integer,Integer> m4 = new HashMap();
		m4.put(c.v2id,gj);
		this.cid_to_gid.add(m3);
		this.cid_to_gid.add(m4);

	}
	public void append_rmp(int extensionpoint, int lastv)
	{

		List<Integer> temp = new LinkedList();
		int index=0;
		int i=0;
		for(i=0; i<this.right_most_path.size(); i++)
		{
			temp.add(this.right_most_path.get(i));
			if((Integer)(this.right_most_path.toArray()[i]) == extensionpoint)
				break;
		}
		this.right_most_path.clear();
		temp.add(lastv);
		this.right_most_path.addAll(temp);
		index=i+1;

	}
	public int gid(int cid)
	{
		Iterator it = cid_to_gid.iterator();
		while(it.hasNext())
		{
			Object obj1 = it.next();
			Iterator it1 = ((Map)obj1).keySet().iterator();
			Object obj2 = it1.next();
			if(cid == ((Integer)obj2).intValue())
			{
				return (Integer)(((Map)obj1).get(obj2));

			} 

		}
		return -1;

	}

	public int cid(int gid)
	{
		Iterator it = gid_to_cid.iterator();
		while(it.hasNext())
		{
			Object obj1 = it.next();
			Iterator it1 = ((Map)obj1).keySet().iterator();
			Object obj2 = it1.next();
			if(gid == (Integer)obj2)
			{
				return ((Integer)(((Map)obj1).get(obj2))).intValue();

			} 

		}
		return -1;

	}


}

