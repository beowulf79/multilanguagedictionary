package net.verza.jdict.dataloaders;

import java.util.*;
import net.verza.jdict.exceptions.LabelNotFoundException;

/**
 * @author ChristianVerdelli
 * 
 */
public interface FileExtensionBinder {

	public int read(String path, int arg1) throws LabelNotFoundException;

	public List<String> get();

}
