package com.idcard.Service;

import java.time.Year;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcard.CommonData.Juleandate;
import com.idcard.Model.TransactionEntity;
import com.idcard.Repository.TransactionRepository;


@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
    private TransactionRepository transactionRepository;

    @Override
	public String generateTransactionNumber(String stateCode) {
        String currentYear = String.valueOf(Year.now().getValue());
        String counter = "00001";
        int serialNumber;
        Date now = new Date(); // current timestamp
        Optional<TransactionEntity> entyOpt = transactionRepository.findByScodeAndYear(currentYear,stateCode);
        if (entyOpt.isPresent()) {
            TransactionEntity ent = entyOpt.get();
            int serialnumber=ent.getSerialNumber(); 
            serialnumber=serialnumber + 1;
            counter=String.format("%05d", serialnumber);
			ent.setSerialNumber(serialnumber); 
			ent.setStateCode(stateCode);
			ent.setYear(currentYear);
			ent.setUpdateDate(Juleandate.getCurrentDateTime());
           
            transactionRepository.save(ent);
        } else {
            serialNumber = 1;
            TransactionEntity newEntity = new TransactionEntity();
            newEntity.setStateCode(stateCode);
            newEntity.setYear(currentYear);
            newEntity.setSerialNumber(serialNumber);
            newEntity.setCreateDate(now);
            transactionRepository.save(newEntity);
        }
        return stateCode + currentYear + counter;
    }
}
