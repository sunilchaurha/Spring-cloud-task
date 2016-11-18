package com.emc.it.configuration.write2db;

import org.apache.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * This class is used to map <code>EASProjectLookupDTO</code> attribute from the
 * NAS file rows.
 * 
 * @author chaurs
 * 
 * 
 */

public class FtpFieldMapper implements FieldSetMapper<FtpDTO> {

	private Logger logger = Logger.getLogger(FtpFieldMapper.class);
	@Override
	public FtpDTO mapFieldSet(FieldSet fieldSet) {
		logger.info("Mapping File record to DTO.");

		FtpDTO dTO = new FtpDTO();
		dTO.setName(fieldSet.readString(0));
		dTO.setEmail(fieldSet.readString(1));
		dTO.setMobile(fieldSet.readString(2));
		return dTO;
	}

}
