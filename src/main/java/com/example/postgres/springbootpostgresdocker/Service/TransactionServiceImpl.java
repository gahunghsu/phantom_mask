package com.example.postgres.springbootpostgresdocker.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.postgres.springbootpostgresdocker.Model.PurchaseHis;
import com.example.postgres.springbootpostgresdocker.repository.PharmacyInfoRepository;
import com.example.postgres.springbootpostgresdocker.repository.PurchaseHisRepository;
import com.example.postgres.springbootpostgresdocker.repository.UserInfoRepository;

@Service
public class TransactionServiceImpl implements TransactionService{
	@Autowired
    private PharmacyInfoRepository pharmacyInfoRepository;
    
    @Autowired
    private UserInfoRepository userInfoRepository;
    
    @Autowired
    private PurchaseHisRepository purchaseHisRepository;
    
    /**
     * 購買口罩
     *
     * @param userName
     * @param maskName
     * @param pharmacy
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, readOnly = false, rollbackFor = Exception.class)
    public String purchaseMask(PurchaseHis purchaseHis) {
    	Pattern pattern = Pattern.compile("[0-9]+ per pack");
        Matcher matcher = null;
        String  regEx="[^0-9]"; 
        Pattern numPattern = Pattern.compile(regEx);
        Matcher maskQtyMatcher = null;
        matcher = pattern.matcher(purchaseHis.getMaskName());
		if(matcher.find()){                 	          
			maskQtyMatcher = numPattern.matcher(matcher.group());          	        
			purchaseHis.setQty(Integer.parseInt(maskQtyMatcher.replaceAll("").trim()));
	    }
    	System.out.println("purchaseMask ==> " + purchaseHis.getPharmacyName() + "," + purchaseHis.getTransactionAmount());
    	pharmacyInfoRepository.updateCashByName(purchaseHis.getPharmacyName(), purchaseHis.getTransactionAmount());
		PurchaseHis his = new PurchaseHis(purchaseHis.getUserName(), purchaseHis.getPharmacyName(), purchaseHis.getMaskName(), purchaseHis.getTransactionAmount(), purchaseHis.getTransactionDate(),purchaseHis.getQty());
		purchaseHisRepository.save(his);
		userInfoRepository.updateCashByName(purchaseHis.getUserName(), purchaseHis.getTransactionAmount());   	
        return "Purchase Success";
    }
}
