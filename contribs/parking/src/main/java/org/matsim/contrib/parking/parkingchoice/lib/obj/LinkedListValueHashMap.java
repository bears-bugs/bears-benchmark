package org.matsim.contrib.parking.parkingchoice.lib.obj;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
// TODO: write tests.
public class LinkedListValueHashMap<KeyClass,ValueClass> {

	HashMap<KeyClass, LinkedList<ValueClass>> hashMap=new HashMap<KeyClass, LinkedList<ValueClass>>();
	HashMap<ValueClass, KeyClass> hashMapBackPointer=new HashMap<ValueClass, KeyClass>();
	
	
	public Collection<KeyClass> getKeySet(){
		return hashMap.keySet();
	}
	
	public void putAndSetBackPointer(KeyClass key,ValueClass value){
		put(key,value);
		
		if (hashMapBackPointer.containsKey(value)){
			throw new Error("The backpointer concept only works for one-to-one mappings between key and value!");
		}
		
		hashMapBackPointer.put(value, key);
	}
	
	public KeyClass getKey(ValueClass value){
		if (hashMapBackPointer.size()==0){
			throw new Error("use method putAndSetBackPointer(...) for insertion instead of put(...)!");
		}
		
		return hashMapBackPointer.get(value);
	}
	
	public void put(KeyClass key,ValueClass value){
		initKey(key);
		
		LinkedList<ValueClass> list=hashMap.get(key);
		list.add(value);
		
		hashMap.put(key, list);
	}
	
	public LinkedList<ValueClass> get(KeyClass key){
		initKey(key);
		
		return hashMap.get(key);
	}
	
	public ValueClass getValue(KeyClass key){		
		return get(key).get(0);
	}
	
	private void initKey(KeyClass key){
		if (!hashMap.containsKey(key)){
			hashMap.put(key, new LinkedList<ValueClass>());
		}
	}
	
	public int size(){
		return hashMap.size();
	}
	
	public boolean containsKey(KeyClass key){
		return hashMap.containsKey(key);
	}
	
	
	public int getNumberOfEntriesInLongestList(){
		int maxEntries=Integer.MIN_VALUE;
		
		for (KeyClass key:hashMap.keySet()){
			if (maxEntries<hashMap.get(key).size()){
				maxEntries=hashMap.get(key).size();
			}
		}
		
		return maxEntries;
	}
	
	
	
}
