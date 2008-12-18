package net.verza.jdict.verbs;

import java.util.HashMap;
import net.verza.jdict.exceptions.DataNotFoundException;
import org.apache.log4j.Logger;


public class ArabVerbPresentParadigm2ShortVowels {

	
	private static Logger log;
	public static HashMap<String,String> table;

	
	public ArabVerbPresentParadigm2ShortVowels()	{
		log = Logger.getLogger("net.verza.jdict.verbs");
		log.trace("called class "+this.getClass().getName());
		
		table = new HashMap<String,String>();
		
		table.put("1",	"1412");
		table.put("2",	"1432");
		table.put("3",	"1422");
		table.put("4",	"1412");
		table.put("5",	"1432");
		table.put("6",	"1422");
		table.put("7",	"2192");
		table.put("8",	"21A3");
		table.put("9",	"243");
		table.put("10",	"11182");
		table.put("11",	"111A12");
		table.put("12",	"14132");
		table.put("13",	"14132");
		table.put("13/1",	"1832");
		table.put("13/2",	"14132");
		table.put("13/3",	"14132");
		table.put("13/4",	"1832");
		table.put("13/5",	"1832");
		table.put("13/6",	"14132");
		table.put("13/7",	"1832");
		table.put("13/8",	"1832");
		table.put("14",	"141S");
		table.put("15",	"14143");
		table.put("16",	"130");
		table.put("17",	"120");
		table.put("18",	"110");
		table.put("19",	"21A0");
		table.put("20",	"23S");
		table.put("21",	"111AS");
		table.put("22",	"1410");
		table.put("23",	"1410");
		table.put("24",	"14130");
		table.put("25",	"1412");
		table.put("25/1",	"1422");
		table.put("26",	"2192");
		table.put("27",	"21A32");
		table.put("28",	"2432");
		table.put("29",	"11182");
		table.put("30",	"11M12");
		table.put("31",	"14132");
		table.put("31/1",	"1832");
		table.put("32",	"141432");
		table.put("33",	"1412");
		table.put("33/1",	"1412");
		table.put("34",	"1432");
		table.put("34",	"1412");
		table.put("35",	"1412");
		table.put("36",	"1422");
		table.put("37",	"2192");
		table.put("38",	"21A32");
		table.put("39",	"2432");
		table.put("40",	"11182");
		table.put("41",	"111A12");
		table.put("42",	"14132");
		table.put("43",	"141432");
		table.put("44",	"1412");
		table.put("45",	"1412");
		table.put("46",	"1422");
		table.put("47",	"2192");
		table.put("48",	"21A32");
		table.put("49",	"2432");
		table.put("50",	"11182");
		table.put("51",	"111A12");
		table.put("52",	"14132");
		table.put("53",	"14132");
		table.put("54",	"141432");
		table.put("55",	"1412");
		table.put("55/1",	"112");
		table.put("56",	"132");
		table.put("57",	"2W32");
		table.put("58",	"1832");
		table.put("59",	"141432");
		table.put("60",	"1412");
		table.put("61",	"2W32");
		table.put("62",	"141432");
		table.put("63",	"12W2");
		table.put("63/1",	"12W2");
		table.put("64",	"1412");
		table.put("64/1",	"11A2");
		table.put("65",	"2432");
		table.put("65/1",	"23Y2");
		table.put("66",	"141A2");
		table.put("67",	"141A2");
		table.put("68",	"1413Y2");
		table.put("69",	"13Y2");
		table.put("70",	"142W");
		table.put("71",	"219Y");
		table.put("72",	"21A3Y");
		table.put("73",	"243Y");
		table.put("74",	"1118Q");
		table.put("75",	"111A1Q");
		table.put("76",	"1413Y");
		table.put("77",	"1413Y");
		table.put("78",	"14143Y");
		table.put("79",	"141Q");
		table.put("80",	"143Y");
		table.put("81",	"141Q");
		table.put("82",	"130");
		table.put("83",	"120");
		table.put("84",	"1410");
		table.put("85",	"120");
		table.put("86",	"110");
		table.put("87",	"12W2");
		table.put("88",	"13Y2");
		table.put("89",	"142W");
		table.put("90",	"219Y");
		table.put("91",	"21A3Y");
		table.put("92",	"243Y");
		table.put("93",	"1118Q");
		table.put("94",	"11M1Q");
		table.put("95",	"14143Y");
		table.put("96",	"141Q");
		table.put("97",	"143Y");
		table.put("98",	"141Q");
		table.put("99",	"132");
		table.put("100",	"1412");
		table.put("101",	"2432");
		table.put("102",	"142W");
		table.put("103",	"141Q");
		table.put("103/1",	"11Q");
		table.put("104",	"21A3Y");
		table.put("105",	"243Y");
		table.put("105/1",	"23Y");
		table.put("106",	"1118Q");
		table.put("107",	"111A1Q");
		table.put("108",	"1413Y");
		table.put("109",	"1412");
		table.put("110",	"1412");
		table.put("110/1",	"112");
		table.put("111",	"1422");
		table.put("112",	"12W2");
		table.put("113",	"23Y2");
		table.put("114",	"141A2");
		table.put("115",	"1413Y2");
		table.put("116",	"13Y2");
		table.put("117",	"11A2");
		table.put("117/1",	"11A2");
		table.put("118",	"13Y");
		table.put("119",	"1W41Q");
		table.put("120",	"13Y");
		table.put("121",	"2W3Y");
		table.put("122",	"141W43Y");
		table.put("123",	"141A");
		table.put("124",	"219Y");
		table.put("125",	"243Y");
		table.put("126",	"14143Y");
		table.put("127",	"143Y");
		table.put("128",	"21432");
		table.put("129",	"111412");
		table.put("130",	"141432");
		table.put("131",	"14130");
		table.put("132",	"21432");
		table.put("133",	"21432");
		table.put("134",	"14130");
		table.put("135",	"21432");

	}
	
	
	public static String getShortVowels(String _paradigm)	throws DataNotFoundException { 
		log.trace("called method getShortVowels with argument " +_paradigm );
		if(!table.containsKey(_paradigm))  throw new DataNotFoundException("the paradigm code is invalid");
		return table.get(_paradigm);
	}
	
	
}
