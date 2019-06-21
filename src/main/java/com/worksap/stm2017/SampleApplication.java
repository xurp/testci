package com.worksap.stm2017;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication {
	private static String getSubDomainFromSuggestAPIUrl() {
    String suggestAPIUrl="https://hrtommt-develop.hue.worksap.com";
	String subDomain = suggestAPIUrl.substring(suggestAPIUrl.indexOf(".") + 1);
    if (subDomain.indexOf("/") > -1) {
        return subDomain.substring(0, subDomain.indexOf("/"));
    } else {
        return subDomain;
    }
	}
	private static String getSubDomainFromSuggestAPIUrl2() throws Exception{
	    String suggestAPIUrl="https://www.hrtommt-develop.hue.worksap.com/ss";
	URI uri = new URI(suggestAPIUrl);
    String domain = uri.getHost();
    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
	private static String getSubDomainFromSuggestAPIUrl3(String uri) {
        try {
            URI suggestAPIUri = new URI(uri);
            String domain = suggestAPIUri.getHost();
            return domain.substring(domain.indexOf(".") + 1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) {
    	System.out.println( getSubDomainFromSuggestAPIUrl3("https://hrtommt-develop.hue.worksap.com/ss"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("http://hrtommt-develop.hue.worksap.com/ss"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("https://www.hrtommt-develop.hue.worksap.com/ss"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("http://www.hrtommt-develop.hue.worksap.com/ss"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("https://hrtommt-develop.hue.worksap.com/ss/s"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("http://hrtommt-develop.hue.worksap.com/ss/s"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("https://www.hrtommt-develop.hue.worksap.com/ss/s"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("http://www.hrtommt-develop.hue.worksap.com/ss/s"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("https://hrtommt-develop.hue.worksap.com"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("http://hrtommt-develop.hue.worksap.com"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("https://www.hrtommt-develop.hue.worksap.com"));
    	System.out.println( getSubDomainFromSuggestAPIUrl3("http://www.hrtommt-develop.hue.worksap.com"));
    	System.out.println("http://127.0.0.1:8083/(paiban/)index");
    	System.out.println("需要开启es，MQ，zookeeper,kafka");
    	System.out.println(StringUtils.isEmpty(""));
    	System.out.println(StringUtils.isEmpty(" "));
    	System.out.println(StringUtils.isEmpty(null));
    	String qry11="select e.emp_id,emp_no,emp_nm,sr_syz_nm from srw_syz_manager sm,srw_syozoku sz,emp e,srw_syainmst sy,srw_jigyosyo ji where  sm.sya_id = e.emp_id AND sm.sdate <=  to_date('2019/04/22','YYYY/MM/DD')  AND sm.edate >=  to_date('2019/04/22','YYYY/MM/DD')  AND sm.sr_syz_cd = sz.sr_syz_cd  AND sz.sdate <=  to_date('2019/04/22','YYYY/MM/DD')  AND sz.edate >=  to_date('2019/04/22','YYYY/MM/DD')  AND sz.status = 0  AND sm.sya_id = sy.sya_id AND sy.boss_kb = 0 AND sy.status = 0 AND sy.sdate <=  to_date('2019/04/22','YYYY/MM/DD')  AND sy.edate >=  to_date('2019/04/22','YYYY/MM/DD')  and ji.sdate <=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and ji.edate >=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and sz.sr_jig_cd = ji.sr_jig_cd and ji.sr_tkt_cd = 'DFLT' UNION SELECT e.emp_id,e.emp_no,e.emp_nm,sr_syz_nm  FROM srw_syz_submanager ss,srw_syozoku sz,emp e,srw_syainmst sy,srw_jigyosyo ji WHERE serviceName = 'root.cws.shuro.boss.time_input_approval' AND ss.sya_id = e.emp_id AND ss.sr_syz_cd = sz.sr_syz_cd  AND sz.sdate <=  to_date('2019/04/22','YYYY/MM/DD')  AND sz.edate >=  to_date('2019/04/22','YYYY/MM/DD')  AND ss.sdate <=  to_date('2019/04/22','YYYY/MM/DD')  AND ss.edate >=  to_date('2019/04/22','YYYY/MM/DD')  AND sz.status = 0  AND sy.sya_id = ss.sya_id AND sy.boss_kb = 0 AND sy.status = 0 AND sy.sdate <=  to_date('2019/04/22','YYYY/MM/DD')  AND sy.edate >=  to_date('2019/04/22','YYYY/MM/DD')  and ji.sdate <=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and ji.edate >=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and sz.sr_jig_cd = ji.sr_jig_cd and ji.sr_tkt_cd = 'DFLT'";
    	System.out.println(qry11.replaceAll("SELECT", "select"));
    	String a="123";
    	String b=a;
    	a="456";
    	System.out.println(b);
    	InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			System.out.println(address.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//String qry = "select e.emp_id,emp_no,emp_nm,sr_syz_nm from srw_syz_manager sm,srw_syozoku sz,emp e,srw_syainmst sy where  sm.sya_id = e.emp_id AND sm.sdate <=  to_date('2019/04/10','YYYY/MM/DD')  AND sm.edate >=  to_date('2019/04/10','YYYY/MM/DD')  AND sm.sr_syz_cd = sz.sr_syz_cd  AND sz.sdate <=  to_date('2019/04/10','YYYY/MM/DD')  AND sz.edate >=  to_date('2019/04/10','YYYY/MM/DD')  AND sz.status = 0  AND sm.sya_id = sy.sya_id AND sy.boss_kb = 0 AND sy.status = 0 AND sy.sdate <=  to_date('2019/04/10','YYYY/MM/DD')  AND sy.edate >=  to_date('2019/04/10','YYYY/MM/DD')  UNION SELECT e.emp_id,e.emp_no,e.emp_nm,sr_syz_nm  FROM srw_syz_submanager ss,srw_syozoku sz,emp e,srw_syainmst sy WHERE serviceName = 'root.cws.shuro.boss.time_input_approval' AND ss.sya_id = e.emp_id AND ss.sr_syz_cd = sz.sr_syz_cd  AND sz.sdate <=  to_date('2019/04/10','YYYY/MM/DD')  AND sz.edate >=  to_date('2019/04/10','YYYY/MM/DD')  AND ss.sdate <=  to_date('2019/04/10','YYYY/MM/DD')  AND ss.edate >=  to_date('2019/04/10','YYYY/MM/DD')  AND sz.status = 0  AND sy.sya_id = ss.sya_id AND sy.boss_kb = 0 AND sy.status = 0 AND sy.sdate <=  to_date('2019/04/10','YYYY/MM/DD')  AND sy.edate >=  to_date('2019/04/10','YYYY/MM/DD') ";
	String qry="select em.sya_id,em.sya_bg,em.sya_nm,em.sya_kn,sz.sr_syz_nm from srw_syainmst sy,srw_syainjh em,srw_syozoku sz where  sy.sya_id = em.sya_id and sz.sr_syz_cd = sy.sr_syz_cd  and sy.sdate <=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and sy.edate >=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and sz.sdate <=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and sz.edate >=  to_date( to_char( sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')  and sy.boss_kb = 0  and sy.status = 0  and sz.sr_syz_nm LIKE('%SilentValleyNationalPark%')  order by em.sya_bg asc ";	
	qry = qry.toLowerCase();
        int selectIndex = qry.indexOf("select");
        while (selectIndex != -1) {
        	System.out.println(qry.length());
        	System.out.println(selectIndex);
            String qry1=qry.substring(0,qry.indexOf(",",selectIndex));
            String qry2=qry.substring(qry.indexOf("from",selectIndex)-1);
            System.out.println(qry1);
            System.out.println(qry2);
            qry = qry1 + qry2;
            selectIndex = qry.indexOf("select", selectIndex+1);
        }
        System.out.println(qry);
		
		
		
		
		
    	
    	SpringApplication.run(SampleApplication.class, args);
    }
    

    
}
                                    


