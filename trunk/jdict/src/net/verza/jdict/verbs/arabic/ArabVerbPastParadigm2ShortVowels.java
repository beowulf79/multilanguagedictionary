package net.verza.jdict.verbs.arabic;

import java.util.HashMap;

import net.verza.jdict.exceptions.DataNotFoundException;

import org.apache.log4j.Logger;

public class ArabVerbPastParadigm2ShortVowels {

    private static Logger log;
    private static HashMap<String, String> table;

    public ArabVerbPastParadigm2ShortVowels() {
	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());

	table = new HashMap<String, String>();

	table.put("1", "114");
	table.put("2", "	114");
	table.put("3", "111");
	table.put("4", "	131");
	table.put("5", "131");
	table.put("6", "121");
	table.put("7	", "181");
	table.put("8", "1A1");
	table.put("9", "141");
	table.put("10", "1181");
	table.put("11", "11A11");
	table.put("12", "34111");
	table.put("13", "34111");
	table.put("13/1", "3811");
	table.put("13/2", "34111");
	table.put("13/3", "34111");
	table.put("13/4", "3811");
	table.put("13/5", "3811");
	table.put("13/6", "34111");
	table.put("13/7", "3811");
	table.put("13/8", "3811");
	table.put("14", "3418");
	table.put("15", "341411");
	table.put("16", "18");
	table.put("17", "18");
	table.put("18", "18");
	table.put("19", "1A8");
	table.put("20", "118");
	table.put("21", "11A8");
	table.put("22", "3418");
	table.put("23", "3418");
	table.put("24", "34118");
	table.put("25", "111");
	table.put("25/1", "111");
	table.put("26", "181");
	table.put("27", "M11");
	table.put("28", "M11");
	table.put("29", "1181");
	table.put("30", "1M11");
	table.put("31", "3Y111");
	table.put("31/1", "3811");
	table.put("32", "341411");
	table.put("33", "111");
	table.put("33/1", "111");
	table.put("34", "111");
	table.put("34", "131");
	table.put("35", "131");
	table.put("36", "121");
	table.put("37", "181");
	table.put("38", "1A11");
	table.put("39", "1411");
	table.put("40", "1181");
	table.put("41", "11A11");
	table.put("42", "34111");
	table.put("43", "341411");
	table.put("44", "111");
	table.put("45", "131");
	table.put("46", "121");
	table.put("47", "181");
	table.put("48", "1A11");
	table.put("49", "1411");
	table.put("50", "1181");
	table.put("51", "11A11");
	table.put("52", "34111");
	table.put("3", "34111");
	table.put("54", "341411");
	table.put("55", "111");
	table.put("55/1", "111");
	table.put("56", "111");
	table.put("57", "1411");
	table.put("58", "3811");
	table.put("59", "341411");
	table.put("60", "111");
	table.put("61", "1411");
	table.put("62", "341411");
	table.put("63", "1A1");
	table.put("63/1", "1A1");
	table.put("64", "131");
	table.put("64/1", "1A1");
	table.put("65", "1411");
	table.put("65/1", "11A1");
	table.put("66", "341A1");
	table.put("67", "341A1");
	table.put("68", "3411A1");
	table.put("69", "1A1");
	table.put("70", "11A");
	table.put("71", "18Q");
	table.put("72", "1A1Q");
	table.put("73", "141Q");
	table.put("74", "118Q");
	table.put("75", "11A1Q");
	table.put("76", "3411Q");
	table.put("77", "3411Q");
	table.put("78", "34141Q");
	table.put("79", "11Q");
	table.put("80", "11QQ");
	table.put("81", "131");
	table.put("82", "18");
	table.put("83", "18");
	table.put("84", "3Y18");
	table.put("85", "18");
	table.put("86", "18");
	table.put("87", "M1");
	table.put("88", "M1");
	table.put("89", "11A");
	table.put("90", "18Q");
	table.put("91", "M1Q");
	table.put("92", "M1Q");
	table.put("93", "118Q");
	table.put("94", "1M1Q");
	table.put("95", "34141Q");
	table.put("96", "11Q");
	table.put("97", "11Q");
	table.put("98", "131");
	table.put("99", "111");
	table.put("100", "131");
	table.put("101", "1411");
	table.put("102", "1M");
	table.put("103", "11Q");
	table.put("103/1", "11Q");
	table.put("104", "1A1Q");
	table.put("105", "141Q");
	table.put("105/1", "11Q");
	table.put("106", "118Q");
	table.put("107", "11A1Q");
	table.put("108", "3411Q");
	table.put("109", "111");
	table.put("110", "131");
	table.put("110/1", "131");
	table.put("111", "121");
	table.put("112", "1A1");
	table.put("113", "11A1");
	table.put("114", "341A1");
	table.put("115", "3411A1");
	table.put("116", "1A1");
	table.put("117", "1A1");
	table.put("117/1", "1A1");
	table.put("118", "11Q");
	table.put("119", "131");
	table.put("120", "131");
	table.put("121", "1W1Q");
	table.put("122", "34141Q");
	table.put("123", "131");
	table.put("124", "18A");
	table.put("125", "141A");
	table.put("126", "34141A");
	table.put("127", "11Q");
	table.put("128", "1411");
	table.put("129", "11411");
	table.put("130", "341411");
	table.put("131", "34118");
	table.put("132", "1411");
	table.put("133", "1411");
	table.put("134", "34118");
	table.put("135", "1411");

    }

    public static String getShortVowels(String _paradigm)
	    throws DataNotFoundException {
	log.trace("called method getShortVowels with argument " + _paradigm);
	if (!table.containsKey(_paradigm))
	    throw new DataNotFoundException("the paradigm code is invalid");
	return table.get(_paradigm);
    }

}
