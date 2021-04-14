package com.example.postgres.springbootpostgresdocker;

import com.example.postgres.springbootpostgresdocker.Bean.MaskPharmacyName;
import com.example.postgres.springbootpostgresdocker.Model.MaskInfo;
import com.example.postgres.springbootpostgresdocker.Model.PharmacyInfo;
import com.example.postgres.springbootpostgresdocker.Model.PharmacyTime;
import com.example.postgres.springbootpostgresdocker.Model.PurchaseHis;
import com.example.postgres.springbootpostgresdocker.Model.QueryCondition;
import com.example.postgres.springbootpostgresdocker.Model.TopUserCondition;
import com.example.postgres.springbootpostgresdocker.Model.UserInfo;
import com.example.postgres.springbootpostgresdocker.Service.TransactionService;
import com.example.postgres.springbootpostgresdocker.repository.MaskInfoRepository;
import com.example.postgres.springbootpostgresdocker.repository.PharmacyInfoRepository;
import com.example.postgres.springbootpostgresdocker.repository.PharmacyTimeRepository;
import com.example.postgres.springbootpostgresdocker.repository.PurchaseHisRepository;
import com.example.postgres.springbootpostgresdocker.repository.UserInfoRepository;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    
    @Autowired
    private PharmacyTimeRepository pharmacyTimeRepository;
    
    @Autowired
    private MaskInfoRepository maskInfoRepository;
    
    @Autowired
    private PharmacyInfoRepository pharmacyInfoRepository;
    
    @Autowired
    private UserInfoRepository userInfoRepository;
    
    @Autowired
    private PurchaseHisRepository purchaseHisRepository;
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/userJson")
    public String initUserData(@RequestBody String userJson) {
    	logger.info("Processing userJson...");
    	userInfoRepository.deleteAll();
    	purchaseHisRepository.deleteAll();
    	Pattern pattern = Pattern.compile("[0-9]+ per pack");
        Matcher matcher = null;
        String  regEx="[^0-9]"; 
        Pattern numPattern = Pattern.compile(regEx);
        Matcher maskQtyMatcher = null;
        
    	UserInfo[] usersInfo = new Gson().fromJson(userJson, UserInfo[].class);
    	for(UserInfo userInfo : usersInfo) {
    		userInfoRepository.save(userInfo);
    		for(PurchaseHis purchaseHis : userInfo.getPurchaseHistories()) {
    			purchaseHis.setUserName(userInfo.getName());
    			matcher = pattern.matcher(purchaseHis.getMaskName());
    			if(matcher.find()){                 	          
    				maskQtyMatcher = numPattern.matcher(matcher.group());    
    				logger.info("matcher..." + matcher);
    				logger.info("maskQtyMatcher..." + maskQtyMatcher);
    				purchaseHis.setQty(Integer.parseInt(maskQtyMatcher.replaceAll("").trim()));
    		    }
    			purchaseHisRepository.save(purchaseHis);
    		}
    	}
    	return "Success initial user data";
    }
    

    @PostMapping("/pharmacyJson")
    public String initPharmacyData(@RequestBody String pharmacyJson) {
        logger.info("Processing pharmacyJson...");
        pharmacyInfoRepository.deleteAll();
        maskInfoRepository.deleteAll();
        pharmacyTimeRepository.deleteAll();
        
        PharmacyInfo[] pharmacyInfo = new Gson().fromJson(pharmacyJson, PharmacyInfo[].class);
        for(PharmacyInfo pInfo : pharmacyInfo) {        	
        	pharmacyInfoRepository.save(pInfo);        	
        	for(MaskInfo maskInfo : pInfo.getMasks()) {
        		maskInfo.setPharName(pInfo.getName());        		
        		maskInfoRepository.save(maskInfo);
        	}
        	
        	String[] openPeriod = pInfo.getOpeningHours().split("/");
        	
        	for(String period : openPeriod) {
        		//System.out.println(period + "/ " + period.substring(period.length()-14, period.length()));
        		String weekDays = period.substring(0, period.length()-14);
        		String[] timePeriod = period.substring(period.length()-14, period.length()).split(" - ");
        		String openTime = timePeriod[0].trim();
        		String closeTime = timePeriod[1].trim();
        		
        		weekDays = weekDays.trim();
        		weekDays = weekDays.replace(",", "-");
        		
        		if(weekDays.contains("-")){
        			String[] weekAry = weekDays.split("-");
        			for(String weekDay : weekAry) {
        				savePharmacyTime(closeTime, openTime,pInfo.getName(), weekDay.trim());        				
        			}
        		}else {
        			savePharmacyTime(closeTime, openTime,pInfo.getName(), weekDays);    				
        		}        		
        	}
        }        
        /*System.out.println(pharmacyInfo);*/        
        return "Success initial pharmacy data";
    }
    
    
    @GetMapping("/pharmacies/{datetime}")
    public List<String> getOpenPharmacyByDateTime( @PathVariable(value = "datetime") String datetime) {
        logger.info("Start to getOpenPharmacyByDateTime...");
        List<String> result = new ArrayList<String>();
        if(datetime != null && !datetime.equals("")) {
        	logger.info("Start to getOpenPharmacyByDateTime..." + datetime);
        	result = pharmacyTimeRepository.findOpenPharmacyByDateTime(datetime);
        }
        return result;
    }
    
    @GetMapping("/pharmacies/week/{time}")
    public List<PharmacyTime> getOpenPharmacyByTime(@PathVariable(value = "time") String time) {
        logger.info("Start to getOpenPharmacyByTime...");
        return pharmacyTimeRepository.findOpenPharmacyByTime(time);
    }
    
    @GetMapping("/mask/pharmacy/{name}/{sort}")
    public List<MaskInfo> getMaskByPharmacy(@PathVariable(value = "name") String name, @PathVariable(value = "sort") String sort) {
        logger.info("Start to getMaskByPharmacy...");
        if("price".equals(sort)) {
        	return maskInfoRepository.findMaskByPharmacyOrderByPrice(name);
        }else {
        	return maskInfoRepository.findMaskByPharmacyOrderByName(name);
        }
    }
    
    @GetMapping("/mask/pharmacy/{lowprice}/{highprice}/{qty}/{ml}")
    public List<String> getPharmaciesByPriceQty(@PathVariable(value = "lowprice") String lowprice, @PathVariable(value = "highprice") String highprice, @PathVariable(value = "qty") String qty, @PathVariable(value = "ml") String ml ) {
        logger.info("Start to getPharmaciesByPriceQty..." + lowprice + "," + highprice + "," + qty + "," + ml);
        if("more".equals(ml)) {
        	return maskInfoRepository.findPharmacyByPriceMore(Double.valueOf(lowprice),Double.valueOf(highprice),Integer.parseInt(qty));
        }else {
        	return maskInfoRepository.findPharmacyByPriceLess(Double.valueOf(lowprice),Double.valueOf(highprice),Integer.parseInt(qty));
        }
    }
    
    @PostMapping("/mask/pharmacy/name")
    public List<MaskPharmacyName> getMaskPharmacyName(@RequestBody String condition) {
    	QueryCondition sqlCondition = new Gson().fromJson(condition, QueryCondition.class);
    	logger.info("Start to getMaskPharmacyName..." + sqlCondition.getCondition()  + ", " + sqlCondition.getType());
        
    	//PHAR, MASK
    	String type = sqlCondition.getType().toUpperCase();
    	List<String> typeList = new ArrayList<String>();
    	if(type.equals("ALL")) {
    		typeList.add("PHAR");
    		typeList.add("MASK");
    	}else {
    		typeList.add(type);
    	}
    	List<Object[]> objList = maskInfoRepository.findMaskPharmacyName(sqlCondition.getCondition(),typeList);
    	List<MaskPharmacyName> resultList = new ArrayList<MaskPharmacyName>();
    	
    	if(objList != null && objList.size()>0) {    	
    		for(Object[] objs:objList) {
    			resultList.add(new MaskPharmacyName(String.valueOf(objs[0]), String.valueOf(objs[1])));
        	}
    	}
        return resultList;
    }
    
    @PostMapping("/users/amt")
    public List<String> getTopUserByAmt(@RequestBody String condition) {
    	TopUserCondition topUserCondition = new Gson().fromJson(condition, TopUserCondition.class);
    	logger.info("Start to getMaskPharmacyName..." + topUserCondition.getStartDate()  + ", " + topUserCondition.getEndDate()+ ", " + topUserCondition.getX());        
    	List<String> resultList = purchaseHisRepository.findTopUserByAmt(topUserCondition.getStartDate(), topUserCondition.getEndDate(),topUserCondition.getX());    	
        return resultList;
    }
    
    @PostMapping("/transtions")
    public List<Object[]> getTtlAmtDolByDate(@RequestBody String dateRangeJson) {
    	TopUserCondition topUserCondition = new Gson().fromJson(dateRangeJson, TopUserCondition.class);
    	logger.info("Start to getTtlAmtDolByDate..." + topUserCondition.getStartDate()  + ", " + topUserCondition.getEndDate());        
    	List<Object[]> dateSet = purchaseHisRepository.findTtlAmtDol(topUserCondition.getStartDate() , topUserCondition.getEndDate());    	
        return dateSet;
    }
    
    @PutMapping("/pharmacy/{name}/{mask}")
    public String PharmacyInfoByName(@PathVariable(value = "name") String name, @PathVariable(value = "mask") String mask,@RequestBody PharmacyInfo updatedPharmacyInfo) throws Exception {
        logger.info("Update PharmacyInfoByName..." + name + "," + updatedPharmacyInfo);
        List<PharmacyInfo> pharmacies = pharmacyInfoRepository.findByName(name);         
        List<MaskInfo> masks = mask != null && !mask.equals("")?maskInfoRepository.findMaskByPharMaskName(name, mask):maskInfoRepository.findMaskByPharmacyOrderByName(name);        
        List<PharmacyTime> times = pharmacyTimeRepository.findPharmacyByName(name);        
        for(PharmacyInfo pharmacy:pharmacies) {        	
        	pharmacy.setName(updatedPharmacyInfo.getName());
        	pharmacyInfoRepository.save(pharmacy);
        }
        for(MaskInfo maskInfo : masks) {
        	maskInfo.setPharName(updatedPharmacyInfo.getName());
        	List<MaskInfo> newMaskInfo = updatedPharmacyInfo.getMasks();
        	if(mask != null && !mask.equals("")) {
        		maskInfo.setName(newMaskInfo.get(0).getName());
        		maskInfo.setPrice(newMaskInfo.get(0).getPrice());
        	}
        	maskInfoRepository.save(maskInfo);
        }
        for(PharmacyTime time : times) {
        	time.setPharName(updatedPharmacyInfo.getName());
        	pharmacyTimeRepository.save(time);
        }
        return "Update Success";
    }
    
    @DeleteMapping("/mask/{name}")
    public void deleteMask(@PathVariable(value = "name") String name) throws Exception {
        logger.info("Delete Mask...");
        
        List<MaskInfo> maskInfos = maskInfoRepository.findByName(name);
        
        for(MaskInfo maskInfo : maskInfos) {
        	maskInfoRepository.delete(maskInfo);
        }
        
    }
    
    @PostMapping("/purchase/mask")
    public String purchaseMask(@RequestBody String purchaseDataJson) {
    	logger.info("Processing purchaseMask...");
    	PurchaseHis[] purchaseHis = new Gson().fromJson(purchaseDataJson, PurchaseHis[].class);
    	if(purchaseHis != null && purchaseHis.length > 0) {
    		for(PurchaseHis his : purchaseHis) {
    			System.out.println("his  ==> " + his);
    			transactionService.purchaseMask(his);
    		}
    	}
        return "Purchase Success";
    }
    
    private PharmacyTime savePharmacyTime(String closeTime, String openTime, String pharName, String weekDay) {
    	PharmacyTime pt = new PharmacyTime();
		pt.setClose(closeTime);
		pt.setOpen(openTime);
		pt.setPharName(pharName);
		pt.setWeek(weekDay);
		return pharmacyTimeRepository.save(pt);
    }
}